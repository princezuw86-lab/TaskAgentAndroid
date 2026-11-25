# TASK AI AGENT — Starter (Phase 1)

A starter browser-monitoring agent. It visits configured public pages, captures page text and screenshots,
and records results. It does **not** log in, solve CAPTCHAs, submit tasks, post engagement, make purchases,
or transfer funds.

## Requirements
- Python 3.10+
- Playwright

## Setup
```bash
python -m venv .venv
# Windows:
.venv\\Scripts\\activate
# macOS/Linux:
source .venv/bin/activate
pip install -r requirements.txt
playwright install chromium
```

## Configure
Edit `sites.json` and add page URLs you are authorized to monitor.

## Run
```bash
python agent.py
```
Results are saved under `reports/` as JSON and screenshots.

## Next
Phase 2: add a task-specific parser for one site, then a review/approval workflow for actions that the
site permits and that do not violate its rules. Keep credentials out of this starter.
