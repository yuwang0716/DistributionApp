package com.liuhesan.app.distributionapp.ui.personcenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.AppManager;

public class IdentifierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifier);
        AppManager.getAppManager().addActivity(IdentifierActivity.this);
    }
}
