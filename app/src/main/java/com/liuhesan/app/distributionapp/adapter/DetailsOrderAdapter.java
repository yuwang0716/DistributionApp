package com.liuhesan.app.distributionapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Tao on 2017/1/3.
 */

public class DetailsOrderAdapter extends BaseAdapter {
    List<Map<String,String>> goods;
    Context mContext;

    public DetailsOrderAdapter(List<Map<String, String>> goods, Context mContext) {
        this.goods = goods;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder ;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_goods, null);
            mViewHolder = new ViewHolder();
            mViewHolder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            mViewHolder.goods_num = (TextView) convertView.findViewById(R.id.goods_num);
            mViewHolder.goods_price = (TextView) convertView.findViewById(R.id.goods_price);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.goods_name.setText(goods.get(position).get("name"));
        mViewHolder.goods_num.setText(goods.get(position).get("number"));
        mViewHolder.goods_price.setText(goods.get(position).get("price"));
        return convertView;
    }
    class ViewHolder{
        TextView goods_name,goods_num,goods_price;
    }
}
