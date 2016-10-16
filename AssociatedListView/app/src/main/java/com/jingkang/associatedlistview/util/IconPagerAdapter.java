package com.jingkang.associatedlistview.util;

/**
 * Created by jingkang
 */
public interface IconPagerAdapter {
    /**
     * Get title representing the page at {@code index} in the adapter.
     */
    CharSequence getTitle(int index);

    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From ListAdapter
    int getTabCount();
}
