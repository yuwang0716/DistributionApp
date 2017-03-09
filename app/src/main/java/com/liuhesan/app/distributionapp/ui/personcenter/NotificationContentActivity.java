package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.col.m;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class NotificationContentActivity extends AppCompatActivity {
    private final static String TAG = "NotificationContent";
    private WebView mWebView;
    private String useid;
    private String act;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_content);
        initView();
    }

    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        useid = sharedPreferences.getString("id","");
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String category = intent.getStringExtra("category");
        if (category.equals("merchant")) {
            act = "";
        } else {
            act = "a";
        }
        mWebView = (WebView) findViewById(R.id.wv_content);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(API.BASEURL + "deliver/messgeDetail"+ "/userid/" + useid + "/act/" + act+"/id/"+id);
        back = (ImageButton) findViewById(R.id.activity_notification_content_back);
       back.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               finish();
               return false;
           }
       });
    }
}
