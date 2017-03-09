package com.liuhesan.app.distributionapp.ui.personcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.utility.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IdeaActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back;
    private RadioButton rb_product,rb_shop,rb_distributedman,rb_selfdistributed;
    private boolean isProduct,isShop,isDistributedman,isSelfdistributed;
    private EditText et_content,et_phonenums;
    private Button commit;
    private ImageView camera;
    private String type,content,phonenums;
    private Bitmap bitmap;
    private List<File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.idea_back);
        rb_product = (RadioButton) findViewById(R.id.product);
        rb_shop = (RadioButton) findViewById(R.id.shop);
        rb_distributedman = (RadioButton) findViewById(R.id.distributedman);
        rb_selfdistributed = (RadioButton) findViewById(R.id.selfdistributed);
        et_content = (EditText) findViewById(R.id.idea_content);
        et_phonenums = (EditText) findViewById(R.id.idea_phonenums);
        camera = (ImageView) findViewById(R.id.iv_camera);
        commit = (Button) findViewById(R.id.idea_commit);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);
        camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.idea_back:
                finish();
                break;
            case R.id.idea_commit:
                isProduct = rb_product.isChecked();
                isShop = rb_shop.isChecked();
                isDistributedman = rb_distributedman.isChecked();
                isSelfdistributed = rb_selfdistributed.isChecked();
                if (isProduct)
                    type = "产品";
                if (isShop)
                    type = "商家";
                if (isDistributedman)
                    type = "配送员";
                if (isSelfdistributed)
                    type = "66配送工作人员";
                content = et_content.getText().toString().trim();
                phonenums = et_phonenums.getText().toString().trim();
                break;
            case R.id.iv_camera:
                ImageUtils.takeOrChoosePhoto(IdeaActivity.this, ImageUtils.TAKE_OR_CHOOSE_PHOTO);
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
                File f = ImageUtils.getPhotoFromResult(IdeaActivity.this, data);
                camera.setImageDrawable(Drawable.createFromPath(String.valueOf(f)));
                camera.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }
}
