package com.liuhesan.app.distributionapp.ui.personcenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.liuhesan.app.distributionapp.R;

public class RuleActivity extends AppCompatActivity {
    private WebView mWebView;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        mWebView = (WebView) findViewById(R.id.rule_wv);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/rule.html");
        back = (ImageButton) findViewById(R.id.rule_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
