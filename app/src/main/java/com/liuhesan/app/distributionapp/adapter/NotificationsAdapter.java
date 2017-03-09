package com.liuhesan.app.distributionapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.ui.personcenter.NotificationContentActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by Tao on 2017/1/3.
 */

public class NotificationsAdapter extends BaseAdapter {
    List<Map<String,Object>> notifications;
    Context mContext;
    String category;
    public NotificationsAdapter(List<Map<String, Object>> goods, Context mContext,String category) {
        this.notifications = goods;
        this.mContext = mContext;
        this.category = category;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder ;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_notifications, null);
            mViewHolder = new ViewHolder();
            mViewHolder.title = (TextView) convertView.findViewById(R.id.title);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.title.setText(notifications.get(position).get("title").toString());
        mViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotificationContentActivity.class);
                intent.putExtra("id",notifications.get(position).get("id").toString());
                intent.putExtra("category",category);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView title;
    }
}
