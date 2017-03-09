package com.liuhesan.app.distributionapp.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Tao on 2017/1/10.
 */

public class ToastUtil{
    private static Toast toast;

    public static void showToast(Context context,String content){
        if (toast == null){
            toast = Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

}
