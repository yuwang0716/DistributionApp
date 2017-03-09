package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class MyBankcardActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final static String TAG = "MyBankcardActivity";
    private ImageButton back;
    private EditText et_cardholder,et_cardnums,et_idnumber;
    private String cardholder,cardnums,idnumber,bankname;
    private Button sure;
    private Spinner sp_bankname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bankcard);
        AppManager.getAppManager().addActivity(MyBankcardActivity.this);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.my_bankcard_back);
        et_cardholder = (EditText) findViewById(R.id.my_bankcard_cardholder);
        et_cardnums = (EditText) findViewById(R.id.my_bankcard_cardnums);
        et_idnumber = (EditText) findViewById(R.id.my_bankcard_idnumber);
        sure = (Button) findViewById(R.id.my_bankcard_sure);
        sp_bankname = (Spinner) findViewById(R.id.my_bankcard_bankname);
        sp_bankname.setOnItemSelectedListener(this);
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_bankcard_back:
                finish();
                break;
            case R.id.my_bankcard_sure:
                if (!TextUtils.isEmpty(et_cardholder.getText()) && !TextUtils.isEmpty(et_cardnums.getText()) && !TextUtils.isEmpty(et_idnumber.getText()) && !TextUtils.isEmpty(bankname) ){
                    idnumber = et_idnumber.getText().toString();
                    cardholder = et_cardholder.getText().toString();
                    cardnums = et_cardnums.getText().toString();
                    OkGo.post(API.BASEURL+"deliver/bindBack")
                            .tag(this)
                            .params("idcard",idnumber)
                            .params("backname",bankname)
                            .params("cardname",cardholder)
                            .params("cardno",cardnums)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    s = s.toString().replace("null","0");
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        int errno = jsonObject.optInt("errno");
                                        if (errno == 200){
                                            Toast.makeText(MyBankcardActivity.this,"银行卡绑定成功",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent();
                                            intent.putExtra("isBind",true);
                                            setResult(231,intent);
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(MyBankcardActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           bankname = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
