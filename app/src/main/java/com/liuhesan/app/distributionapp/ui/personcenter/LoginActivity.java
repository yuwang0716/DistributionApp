package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.API;
import com.liuhesan.app.distributionapp.utility.AppManager;
import com.liuhesan.app.distributionapp.utility.HookViewClickUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "LoginActivity";
    private EditText et_user;
    private EditText et_pwd;
    private Button login;
    private String user,pwd;
    private TextView forget_pwd;
    private PopupWindow popupwindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getAppManager().addActivity(LoginActivity.this);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                HookViewClickUtil.hookView(login);
            }
        });
        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.user);
        et_pwd = (EditText) findViewById(R.id.pwd);
        login = (Button) findViewById(R.id.login);
        forget_pwd = (TextView) findViewById(R.id.forget_pwd);
        login.setOnClickListener(this);
        forget_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                login.setClickable(false);
                initData(v);
                break;
            case R.id.forget_pwd:
                startActivity(new Intent(LoginActivity.this,ForgetPwdActivity.class));
                break;
        }
    }


    private void initData(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        Random ran =new Random();
        final long phoneIds = ran.nextInt(100)+currentTimeMillis;
        final String phoneId = phoneIds+"";
        if (!TextUtils.isEmpty(et_user.getText()) && !TextUtils.isEmpty(et_pwd.getText())){
            user = et_user.getText().toString().trim();
            pwd = et_pwd.getText().toString().trim();
            View customView = View.inflate(LoginActivity.this,R.layout.popview_logining,null);
            popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            popupwindow.showAtLocation(v, Gravity.CENTER,0,0);
            OkGo.post(API.BASEURL+"deliver/login/")
                    .tag(this)
                    .params("username",user)
                    .params("password",pwd)
                    .params("mass",phoneId)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            s = s.toString().replace("null","0");
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int errno = jsonObject.optInt("errno");
                                if (errno == 300){
                                    Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                                } if (errno == 201){
                                    Toast.makeText(LoginActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
                                }if (errno == 200){  //成功登录
                                    JSONObject data = jsonObject.optJSONObject("data");
                                    String id = data.optString("id");
                                    String deliver = data.optString("deliver");
                                    String username = data.optString("username");
                                    String mobile = data.optString("mobile");
                                    String groupid = data.optString("groupid");
                                    int status = data.optInt("status");
                                    String addtime = data.optString("addtime");
                                    int isauth = data.optInt("isauth");//0手机未认证1认证
                                    String groupname = data.optString("groupname");
                                    String shopids = data.optString("shopids");
                                    String cuser_id = data.optString("cuser_id");
                                    String token = data.optString("token");
                                    String cdate = data.optString("cdate");
                                    String avatar = data.optString("avatar");
                                    int timeout = data.optInt("timeout");
                                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sharedPreferences.edit();
                                    edit.putString("id",id);
                                    edit.putString("deliver",deliver);
                                    edit.putString("pwd",pwd);
                                    edit.putString("username",username);
                                    edit.putString("mobile",mobile);
                                    edit.putString("groupid",groupid);
                                    edit.putInt("status",status);
                                    edit.putString("addtime",addtime);
                                    edit.putInt("isauth",isauth);
                                    edit.putString("groupname",groupname);
                                    edit.putString("shopids",shopids);
                                    edit.putString("cuser_id",cuser_id);
                                    edit.putString("token",token);
                                    edit.putString("cdate",cdate);
                                    edit.putString("headportrait","http://crm.weidab.cn/"+avatar);
                                    edit.putInt("timeout",timeout);
                                    edit.putString("phoneid",phoneId);
                                    edit.putBoolean("isSound",true);
                                    edit.putBoolean("isShake",true);
                                    edit.commit();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                popupwindow.dismiss();
                                login.setClickable(true);
                            }
                        }
                    });
        }else {
            Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }
    }

}