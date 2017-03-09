package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.utility.ImageUtils;
import com.liuhesan.app.distributionapp.widget.RelativeLayoutForButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CompleteDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CompleteDetailsActivity";
    private RelativeLayoutForButton set_head;
    private LinearLayout set_phone;
    private ImageView iv_head;
    private ImageButton back;
    private Bitmap bitmap;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    private static final int REQUEST_CAMERA = 455;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completedetails);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        AppManager.getAppManager().addActivity(CompleteDetailsActivity.this);
        initView();
    }

    private void initView() {
        set_head = (RelativeLayoutForButton) findViewById(R.id.setting_head);
        set_phone = (LinearLayout) findViewById(R.id.setting_phone);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        back = (ImageButton) findViewById(R.id.complete_details_back);
        set_head.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        back.setOnClickListener(this);
        set_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_head:
                ImageUtils.takeOrChoosePhoto(CompleteDetailsActivity.this, ImageUtils.TAKE_OR_CHOOSE_PHOTO);
                break;
            case R.id.complete_details_back:
                finish();
                break;
            case R.id.setting_phone:
                startActivity(new Intent(CompleteDetailsActivity.this,PhoneVerificationActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ImageUtils.TAKE_OR_CHOOSE_PHOTO:
                //获取到了原始图片
                File f = ImageUtils.getPhotoFromResult(CompleteDetailsActivity.this, data);
                //裁剪方法
                ImageUtils.doCropPhoto(CompleteDetailsActivity.this, f);
                break;
            case ImageUtils.PHOTO_PICKED_WITH_DATA:
                //获取到剪裁后的图片
                bitmap = ImageUtils.getCroppedImage();
                iv_head.setImageBitmap(bitmap);
                File file = ImageUtils.saveImage(bitmap);
                SharedPreferences sharedPreferences =  getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("headportrait",file.toString()).commit();
                intent = new Intent("com.liuhesan.app.distributionapp.MINEFRAGMENT");
                intent.putExtra("headportrait",file.toString());
                localBroadcastManager.sendBroadcast(intent);
                List<File> files = new ArrayList<>();
                files.add(file);
                OkGo.post(API.BASEURL + "deliver/img_upload")
                        .tag(this)
                        .addFileParams("file", files)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Log.i(TAG, s + "onSuccess: ");
                            }
                        });
                break;
        }
    }
}

