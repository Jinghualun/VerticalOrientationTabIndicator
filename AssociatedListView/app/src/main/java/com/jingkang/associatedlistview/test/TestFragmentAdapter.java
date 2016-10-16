package com.jingkang.associatedlistview.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jingkang.associatedlistview.R;
import com.jingkang.associatedlistview.util.IconPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingkang
 */
public class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private static final String[] CONTENT = new String[] { "Calendar", "Camera", "Alarms", "Location" };
    private static final int[] ICONS = new int[] {
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
            R.mipmap.ic_sina,
    };

    private List<Fragment> fragments;

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public CharSequence getTitle(int index) {
        return CONTENT[index % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

    @Override
    public int getTabCount() {
        return CONTENT.length;
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public static String[] getCONTENT() {
        return CONTENT;
    }
}
