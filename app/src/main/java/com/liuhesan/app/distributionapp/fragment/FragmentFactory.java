package com.liuhesan.app.distributionapp.fragment;

import android.support.v4.app.Fragment;

import java.util.List;

public class FragmentFactory {

    private List<Fragment> fragments;
    public FragmentFactory(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public Fragment createForNoExpand(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = fragments.get(0);
                break;
            case 1:
                fragment = fragments.get(1);;
                break;
            case 2:
                fragment = fragments.get(2);;
                break;
            case 3:
                fragment = fragments.get(3);;
                break;
        }
        return fragment;
    }
}
