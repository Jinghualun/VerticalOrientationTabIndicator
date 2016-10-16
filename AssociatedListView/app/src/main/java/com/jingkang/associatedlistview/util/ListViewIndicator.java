package com.jingkang.associatedlistview.util;

import android.widget.ListView;

/**
 * Created by jingkang
 */
public interface ListViewIndicator {
    /**
     * Bind the indicator to a ListView.
     *
     * @param view
     */
    void setListView(ListView view);

    /**
     * Sets the currently selected item. If in touch mode, the item will not be selected
     * but it will still be positioned appropriately. If the specified selection position
     * is less than 0, then the item at position 0 will be selected.
     *
     * @param position Index (starting at 0) of the data item to be selected.
     */
    void setSelection(int position);

    /**
     * Notify the indicator that the ListView has changed.
     */
    void notifyDataSetChanged();
}
