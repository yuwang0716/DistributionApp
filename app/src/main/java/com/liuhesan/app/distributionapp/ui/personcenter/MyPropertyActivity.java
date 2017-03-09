package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.czp.library.ArcProgress;
import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Response;

public class MyPropertyActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "MyPropertyActivity";
    private ImageButton back;
    private ArcProgress arcProgress;
    private RelativeLayout bankcard;
    private boolean isBind;
    private TextView bind,balance;
    private String assetNo;
    private String assetFor;
    private String progressStr_assetFor;
    private String progressStr_assetNo;
    private Paint textPaint;
    private float textX;
    private float textY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_property);
        initView();
        initData();
    }

    private void initData() {
        OkGo.post(API.BASEURL+"deliver/myAsset")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        s = s.toString().replace("null","0");
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int errno = jsonObject.optInt("errno");
                            if (errno == 200){
                                JSONObject data = jsonObject.optJSONObject("data");
                                assetNo = data.optString("assetNo");
                                assetFor = data.optString("assetFor");
                                if (TextUtils.isEmpty(assetNo))
                                    assetNo = "0.00";
                                if (TextUtils.isEmpty(assetFor))
                                    assetFor = "0.00";
                                Log.i(TAG,(int) Double.parseDouble(assetFor)+(int) Double.parseDouble(assetNo)+ "onSuccess: "+ (int) Double.parseDouble(assetNo));
                                arcProgress.setMax((int) Double.parseDouble(assetFor)+(int) Double.parseDouble(assetNo));
                                arcProgress.setProgress((int) Double.parseDouble(assetNo));
                                balance.setText(assetFor+"元");
                                arcProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
                                    @Override
                                    public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                                        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                                        textPaint.setColor(getResources().getColor(R.color.colorText_title));
                                        textPaint.setTextSize(20);
                                        String progressStr = "待入账金额";
                                        textX = x-(textPaint.measureText(progressStr)/2);
                                        textY = y/2-((textPaint.descent()+ textPaint.ascent())/2);
                                        canvas.drawText(progressStr, textX, textY, textPaint);
                                        textPaint.setTextSize(50);
                                        progressStr_assetNo = assetNo;
                                        textX = x-(textPaint.measureText(progressStr_assetNo)/2);
                                        textY = y/4*3-((textPaint.descent()+ textPaint.ascent())/2);
                                        canvas.drawText(progressStr_assetNo, textX, textY, textPaint);
                                        textPaint.setTextSize(20);
                                        progressStr = "已入账金额";
                                        textX = x-(textPaint.measureText(progressStr)/2);
                                        textY = y-((textPaint.descent()+ textPaint.ascent())/2);
                                        canvas.drawText(progressStr, textX, textY, textPaint);
                                        textPaint.setTextSize(50);
                                        progressStr_assetFor = assetFor;
                                        textX = x-(textPaint.measureText( progressStr_assetFor)/2);
                                        textY = y/4*5-((textPaint.descent()+ textPaint.ascent())/2);
                                        canvas.drawText( progressStr_assetFor, textX, textY, textPaint);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.my_property_back);
        arcProgress = (ArcProgress) findViewById(R.id.myProgress);
        bankcard = (RelativeLayout) findViewById(R.id.my_property_bankcard);
        bind = (TextView) findViewById(R.id.isbind);
        balance = (TextView) findViewById(R.id.my_property_balance);
        back.setOnClickListener(this);
        bankcard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_property_back:
                finish();
                break;
            case R.id.my_property_bankcard:
                startActivityForResult(new Intent(MyPropertyActivity.this,MyBankcardActivity.class),230);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 230 && resultCode == 231){
            isBind = data.getBooleanExtra("isBind", false);
            Log.i(TAG, isBind+"onActivityResult: ");
            if (isBind){
                bind.setText("");
            }
        }
    }
}
