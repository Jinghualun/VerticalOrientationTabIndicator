# VerticalOrientationTabIndicator
The vertical Tab widgets that are compatible with the ListView o Fragment to improve discoverability of content.<br>

![](https://github.com/Jinghualun/VerticalOrientationTabIndicator/blob/master/AssociatedListView/associated.gif) 
# Usage
1.Include one of the widgets in your view. This could be placed adjacent to the ListView or Fragment it represents.<br>

    <com.jingkang.associatedlistview.util.TabWidgetWithListView
        android:id="@+id/tabWidget"
        android:layout_width="100dp"
        android:layout_height="wrap_content" />

2.In your onCreate method (or onCreateView for a fragment), bind the indicator to the ListView or Fragment.<br>

      ListView listView = (ListView) findViewById(R.id.listView);
      TestAdapter adapter = new TestAdapter<String>(this, android.R.layout.simple_list_item_1, mNames);
      listView.setAdapter(adapter);
      TabWidgetWithListView tabWidget = (TabWidgetWithListView) findViewById(R.id.tabWidget);
      tabWidget.setListView(listView);
      tabWidget.setSectionIndexer(adapter);
