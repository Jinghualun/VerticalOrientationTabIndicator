package com.jingkang.associatedlistview.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jingkang.associatedlistview.R;
import com.jingkang.associatedlistview.util.TabWidgetWithFragment;

import java.util.ArrayList;
import java.util.List;

public class TestTabWidgetWithFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tabwidget_fragment);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < CONTENT.length; i++) {
            Fragment fragment = TestFragment.newInstance(CONTENT[i % CONTENT.length]);
            fragments.add(fragment);
        }
        TabWidgetWithFragment tabWidget = (TabWidgetWithFragment) findViewById(R.id.tabWidget);
        tabWidget.setFragment(getSupportFragmentManager(), fragments, R.id.framelayout, CONTENT, null);
    }

    private static final String[] CONTENT = new String[]{"Calendar", "Camera", "Alarms", "Location"};

    private static final int[] ICONS = new int[]{
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
}
