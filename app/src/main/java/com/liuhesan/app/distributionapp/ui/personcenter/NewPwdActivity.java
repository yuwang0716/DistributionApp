package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class NewPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "NewPwdActivity";
    private EditText pwd_first,pwd_second;
    private Button finish;
    private String password_first,password_second;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pwd);
        AppManager.getAppManager().addActivity(NewPwdActivity.this);
        initView();
    }

    private void initView() {
        pwd_first = (EditText) findViewById(R.id.newpwd_pwd_first);
        pwd_second = (EditText) findViewById(R.id.newpwd_pwd_second);
        back = (ImageButton) findViewById(R.id.newpwd_back);
        finish = (Button) findViewById(R.id.newpwd_finish);
        finish.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newpwd_finish:
            Intent intent = getIntent();
            String phone = intent.getStringExtra("phone");
            if (!TextUtils.isEmpty(pwd_first.getText()) && !TextUtils.isEmpty(pwd_second.getText())) {
                password_first = pwd_first.getText().toString().trim();
                password_second = pwd_second.getText().toString().trim();
                if (password_first.equals(password_second)) {
                    OkGo.post(API.BASEURL + "deliver/findPassword/")
                            .params("mobile", phone)
                            .params("password", password_first)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        int errno = jsonObject.optInt("errno");
                                        if (errno == 200) {
                                            Toast.makeText(NewPwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(NewPwdActivity.this, LoginActivity.class));
                                            AppManager.getAppManager().finishActivity(ForgetPwdActivity.class);
                                            finish();
                                        } else {
                                            Toast.makeText(NewPwdActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(NewPwdActivity.this, "两次输入不一样", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NewPwdActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.newpwd_back:
                finish();
                break;
        }
    }
}
