import asyncio, json, os, re
from datetime import datetime, timezone
from pathlib import Path
from playwright.async_api import async_playwright

CONFIG = Path("sites.json")
REPORTS = Path("reports")

def safe_name(value):
    return re.sub(r"[^a-zA-Z0-9_-]+", "_", value).strip("_") or "page"

async def inspect_site(browser, site, report_dir):
    page = await browser.new_page()
    result = {
        "name": site.get("name", "unnamed"),
        "url": site["url"],
        "checked_at": datetime.now(timezone.utc).isoformat(),
        "status": "started"
    }
    try:
        response = await page.goto(site["url"], wait_until="domcontentloaded", timeout=45000)
        await page.wait_for_timeout(1500)
        result["http_status"] = response.status if response else None
        result["title"] = await page.title()
        result["text"] = (await page.locator("body").inner_text())[:20000]
        result["status"] = "ok"
        await page.screenshot(path=str(report_dir / f"{safe_name(site.get('name','page'))}.png"),
                             full_page=True)
    except Exception as e:
        result["status"] = "error"
        result["error"] = str(e)[:1000]
    finally:
        await page.close()
    return result

async def main():
    REPORTS.mkdir(exist_ok=True)
    sites = json.loads(CONFIG.read_text(encoding="utf-8"))
    stamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    report_dir = REPORTS / stamp
    report_dir.mkdir()
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        results = []
        for site in sites:
            print(f"Inspecting: {site.get('name')} — {site.get('url')}")
            results.append(await inspect_site(browser, site, report_dir))
        await browser.close()
    (report_dir / "report.json").write_text(
        json.dumps(results, indent=2, ensure_ascii=False), encoding="utf-8")
    print(f"Done. Report saved in: {report_dir}")

if __name__ == "__main__":
    asyncio.run(main())
