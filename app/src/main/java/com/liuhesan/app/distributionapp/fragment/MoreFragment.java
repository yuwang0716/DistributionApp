package com.liuhesan.app.distributionapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liuhesan.app.distributionapp.R;
import com.liuhesan.app.distributionapp.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao on 2016/12/10.
 */

public class MoreFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button finishorder,loseefficacy;
    private NoScrollViewPager mViewPager;
    private List<Fragment> fragments;
    private FragmentPagerAdapter mAdapter;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_more, container, false);
        initView();
        initAdapter();
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
    private void initAdapter() {
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
    }
    private void initView() {
        finishorder = (Button) view.findViewById(R.id.finishorder);
        loseefficacy = (Button) view.findViewById(R.id.loseefficacy);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.more_viewpager);
        mViewPager.setNoScroll(true);
        FinishOrderFragment finishOrderFragment = new FinishOrderFragment();
        LoseEfficacyFragment loseEfficacyFragment = new LoseEfficacyFragment();
        fragments = new ArrayList<>();
        fragments.add(finishOrderFragment);
        fragments.add(loseEfficacyFragment);
        finishorder.setOnClickListener(this);
        loseefficacy.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishorder:
                mViewPager.setCurrentItem(0);
                finishorder.setBackground(activity.getResources().getDrawable(R.drawable.finishorderbutton));
                loseefficacy.setBackground(activity.getResources().getDrawable(R.drawable.loseefficacybutton));
                finishorder.setTextColor(getResources().getColor(R.color.colorText_title));
                loseefficacy.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.loseefficacy:
                mViewPager.setCurrentItem(1);
                finishorder.setBackground(activity.getResources().getDrawable(R.drawable.finishorderbuttontwo));
                loseefficacy.setBackground(activity.getResources().getDrawable(R.drawable.loseefficacybuttontwo));
                finishorder.setTextColor(getResources().getColor(R.color.colorPrimary));
                loseefficacy.setTextColor(getResources().getColor(R.color.colorText_title));
                break;
        }
    }
}
