package com.jingkang.associatedlistview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jingkang.associatedlistview.R;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by jingkang
 */
public class TabWidgetWithFragment extends ScrollView implements FragmentIndicator {
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
            replaceFragment(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final VerticalTabLayout mTabLayout;

    private FragmentManager mFragmentManager;
    //private FragmentTransaction mFragmentTransaction;
    private List<Fragment> mFragmentList;
    private int mContainerViewId;
    private String[] mTitles;
    private int[] mIcons;

    private int mMaxTabHeight;
    private int mSelectedTabIndex;
    private int mHeightScreen;

    private OnTabReselectedListener mTabReselectedListener;

    public TabWidgetWithFragment(Context context) {
        this(context, null);
    }

    public TabWidgetWithFragment(Context context, AttributeSet attrs) {
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
            replaceFragment(mSelectedTabIndex);
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

    @SuppressLint("CommitTransaction")
    @Override
    public void setFragment(FragmentManager fragmentManager, List<Fragment> fragmentList, int containerViewId, String[] titles, int[] icons) {
        if (mFragmentManager == fragmentManager && mFragmentList == fragmentList && mContainerViewId == containerViewId && mTitles == titles && mIcons == icons) {
            return;
        }
        if (null == fragmentList || fragmentList.size() == 0) {
            return;
        }
        if (null == titles && null == icons) {
            return;
        }
        this.mFragmentManager = fragmentManager;
        //this.mFragmentTransaction = fragmentManager.beginTransaction();
        this.mFragmentList = fragmentList;
        this.mContainerViewId = containerViewId;
        this.mTitles = titles;
        this.mIcons = icons;
        notifyDataSetChanged();
    }

    @Override
    public void replaceFragment(int position) {
        if (mFragmentManager == null) {
            throw new IllegalStateException("FragmentManager has not been bound.");
        }
        if (mFragmentList == null || mFragmentList.size() == 0) {
            throw new IllegalStateException("FragmentList has not been bound.");
        }
        if (mContainerViewId == 0) {
            throw new IllegalStateException("containerViewId has not been bound.");
        }
        mSelectedTabIndex = position;
        Fragment fragment = mFragmentList.get(position);
        if (null == fragment) {
            throw new NullPointerException("Fragment in position " + position + " is null.");
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(mContainerViewId, fragment, String.valueOf(position)).commit();

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
        int count = 0;
        if (mTitles != null) {
            count = mTitles.length;
        } else if (mIcons != null) {
            count = mIcons.length;
        }
        for (int i = 0; i < count; i++) {
            CharSequence title = null;
            if (mTitles != null) {
                title = mTitles[i];
            }
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (mIcons != null) {
                iconResId = mIcons[i];
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        replaceFragment(mSelectedTabIndex);
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
