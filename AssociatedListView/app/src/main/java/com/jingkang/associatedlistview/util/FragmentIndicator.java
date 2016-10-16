package com.jingkang.associatedlistview.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by jingkang
 */
public interface FragmentIndicator {
    /**
     * Bind the indicator to Fragment.
     *
     * @param fm
     * @param list
     * @param id
     */
    void setFragment(FragmentManager fm, List<Fragment> list, int id, String[] titles, int[] icons);

    /**
     * Sets the currently selected Fragment. If in touch mode, the item will not be selected
     * but it will still be positioned appropriately. If the specified selection position
     * is less than 0, then the item at position 0 will be selected.
     *
     * @param position
     */
     void replaceFragment(int position);

    /**
     * Notify the indicator that the ListView has changed.
     */
    void notifyDataSetChanged();
}
