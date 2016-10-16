package com.jingkang.associatedlistview.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jingkang.associatedlistview.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by jingkang
 */
public class TabWidgetWithListView extends ScrollView implements ListViewIndicator {
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {

        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int oldSelected = mSelectedTabIndex;
            final int newSelected = tabView.getIndex();
            mListView.setSelection(newSelected);
            setSelection(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }

    };

    private final VerticalTabLayout mTabLayout;

    private ListView mListView;
    private SectionIndexer mSectionIndexer;

    private int mMaxTabHeight;
    private int mSelectedTabIndex;
    private int mHeightScreen;

    private OnTabReselectedListener mTabReselectedListener;

    public TabWidgetWithListView(Context context) {
        this(context, null);
    }

    public TabWidgetWithListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVerticalScrollBarEnabled(false);

        mTabLayout = new VerticalTabLayout(context, R.attr.TabPageIndicatorStyle);
        mTabLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mTabLayout, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mHeightScreen = dm.heightPixels;
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    public void setSectionIndexer(SectionIndexer sectionIndexer) {
        this.mSectionIndexer = sectionIndexer;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final boolean lockedExpanded = heightMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST)) {
            mMaxTabHeight = mHeightScreen / 8;
        } else {
            mMaxTabHeight = -1;
        }

        final int oldHeight = getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newHeight = getMeasuredHeight();

        if (lockedExpanded && oldHeight != newHeight) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setSelection(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getTop() - (getHeight() - tabView.getHeight()) / 2;
                smoothScrollTo(0, scrollPos);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    @Override
    public void setListView(ListView view) {
        if (mListView == view) {
            return;
        }
        final ListAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ListView does not have an adapter instance.");
        }
        mListView = view;
        notifyDataSetChanged();
    }

    @Override
    public void setSelection(int position) {
        if (mListView == null) {
            throw new IllegalStateException("ListView has not been bound.");
        }
        mSelectedTabIndex = position;
        int selectionPosition = position;
        if (null != mSectionIndexer) {
            ListAdapter adapter = mListView.getAdapter();
            IconPagerAdapter iconAdapter = null;
            if (adapter instanceof IconPagerAdapter) {
                iconAdapter = (IconPagerAdapter) adapter;
            }
            assert iconAdapter != null;
            selectionPosition = mSectionIndexer.getPositionForSection(iconAdapter.getTitle(position).charAt(0));
        }
        mListView.setSelection(selectionPosition);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == position);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(position);
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        ListAdapter adapter = mListView.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        int count = 0;
        if (iconAdapter != null) {
            count = iconAdapter.getTabCount();
        }
        for (int i = 0; i < count; i++) {
            CharSequence title = null;
            if (iconAdapter != null) {
                title = iconAdapter.getTitle(i);
            }
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setSelection(mSelectedTabIndex);
        requestLayout();
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);
        tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        if (iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
            tabView.setCompoundDrawablePadding(0);
            tabView.setPadding(5, 0, 0, 0);
        }
        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(MATCH_PARENT, mMaxTabHeight));
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.TabPageIndicatorStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Re-measure if we go beyond our maximum size.
            if (mMaxTabHeight > 0 && getMeasuredWidth() > mMaxTabHeight) {
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mMaxTabHeight, MeasureSpec.EXACTLY));
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }
}
