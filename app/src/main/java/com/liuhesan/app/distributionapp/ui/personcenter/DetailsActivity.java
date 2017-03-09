package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;

public class DetailsActivity extends AppCompatActivity {
    private final static  String TAG = "DetailsActivity";
    private WebView mWebView;
    private String url;
    private String act;
    private String useid;
    private ImageButton back;
    private TextView tv_title,error_show;
    private String title;
    private WebAppInterface appInterface;
    private SharedPreferences sharedPreferences;
    private int timeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        act = intent.getStringExtra("act");
        title = intent.getStringExtra("title");
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        useid= sharedPreferences.getString("id","");
        timeout = sharedPreferences.getInt("timeout", 0);
        initView();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url + "/userid/" + useid+"/timeout/"+timeout + "/act/"+ act );
        back = (ImageButton) findViewById(R.id.details_back);
        tv_title = (TextView) findViewById(R.id.details_title);
        tv_title.setText(title);
        error_show = (TextView) findViewById(R.id.error_show);
        appInterface = new WebAppInterface(DetailsActivity.this);
        mWebView.addJavascriptInterface(appInterface, "app");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mWebView.setVisibility(View.GONE);
                error_show.setVisibility(View.VISIBLE);
            }
        });
    }

    class WebAppInterface {


        private Context context;

        public WebAppInterface(Context ct) {

            this.context = ct;
        }

        @JavascriptInterface
        public void getValues(final String orderid) {
            getOrderDetails(orderid);
        }
    }

    private void getOrderDetails(String orderid) {
        if (!TextUtils.isEmpty(orderid)){
            Intent intent = new Intent(DetailsActivity.this, CompletedOrderDetailsActivity.class);
            intent.putExtra("orderid",orderid);
            startActivity(intent);
        }
    }

}