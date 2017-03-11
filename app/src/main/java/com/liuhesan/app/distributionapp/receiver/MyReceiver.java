package com.liuhesan.app.distributionapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.liuhesan.app.distributionapp.utility.MyApplication;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";
	private LocalBroadcastManager localBroadcastManager;
	private Intent intent;
	@Override
	public void onReceive(Context context,Intent intent) {
        Bundle bundle = intent.getExtras();
		SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("login", Context.MODE_PRIVATE);
		String id = sharedPreferences.getString("id", "");
		registerReceiver(bundle,id,context,intent);
	}

	private void registerReceiver(final Bundle bundle, final String id, final Context context, final Intent intent) {
		JPushInterface.setAlias(MyApplication.getInstance(), id, new TagAliasCallback() {
			@Override
			public void gotResult(int i, String s, Set<String> set) {
				Log.i(TAG, i+"gotResult: "+s);
				if (!TextUtils.isEmpty(s)){
					if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
						Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
						processCustomMessage(context, bundle);
					}
				}else {
					registerReceiver(bundle,id,context,intent);
				}
			}
		});
	}


	private void processCustomMessage(Context context, Bundle bundle) {
			localBroadcastManager = LocalBroadcastManager.getInstance(context);
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			Intent msgIntent = new Intent("com.liuhesan.app.distributionapp.JpushMessage");
			msgIntent.putExtra("message",message);
			localBroadcastManager.sendBroadcast(msgIntent);
	}
}
