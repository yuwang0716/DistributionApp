package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
/*
* 意见反馈后期加
* */
public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView idea,rule;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        idea = (TextView) findViewById(R.id.idea);
        rule = (TextView) findViewById(R.id.rule);
        back = (ImageButton) findViewById(R.id.activity_about_us_back);
        back.setOnClickListener(this);
        idea.setOnClickListener(this);
        rule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_about_us_back:
                finish();
                break;
            case R.id.idea:
                //startActivity(new Intent(AboutUsActivity.this,IdeaActivity.class));
                break;
            case R.id.rule:
                startActivity(new Intent(AboutUsActivity.this,RuleActivity.class));
                break;
        }
    }
}
