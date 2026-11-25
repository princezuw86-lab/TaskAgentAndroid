
package com.taskagent.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.*;
import android.view.View;
import android.graphics.Color;

public class MainActivity extends Activity {

    LinearLayout root;
    EditText urlInput;
    WebView browser;
    TextView status;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24,24,24,24);
        root.setBackgroundColor(Color.rgb(12,25,35));

        TextView heading = new TextView(this);
        heading.setText("TASK AI AGENT");
        heading.setTextSize(24);
        heading.setTextColor(Color.WHITE);
        root.addView(heading);

        TextView sub = new TextView(this);
        sub.setText("مساعد مراقبة المهام — النسخة التجريبية");
        sub.setTextColor(Color.LTGRAY);
        root.addView(sub);

        urlInput = new EditText(this);
        urlInput.setSingleLine(true);
        urlInput.setHint("أدخل رابط الموقع (https://...)");
        urlInput.setTextColor(Color.WHITE);
        urlInput.setHintTextColor(Color.GRAY);
        root.addView(urlInput);

        Button open = new Button(this);
        open.setText("فتح الموقع");
        root.addView(open);

        status = new TextView(this);
        status.setText("جاهز. هذه نسخة تجريبية.");
        status.setTextColor(Color.rgb(100,220,150));
        root.addView(status);

        browser = new WebView(this);
        browser.setVisibility(View.GONE);

        WebSettings ws = browser.getSettings();
        ws.setJavaScriptEnabled(false);

        browser.setWebViewClient(new WebViewClient());

        root.addView(browser,
            new LinearLayout.LayoutParams(-1,0,1));

        open.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();

            if (!url.startsWith("https://")) {
                status.setText("يرجى إدخال رابط يبدأ بـ https://");
                return;
            }

            browser.setVisibility(View.VISIBLE);
            browser.loadUrl(url);
            status.setText("تم فتح الموقع. افحص شروط الموقع قبل أي تفاعل.");
        });

        setContentView(root);
    }

    @Override
    public void onBackPressed() {
        if (browser != null && browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
