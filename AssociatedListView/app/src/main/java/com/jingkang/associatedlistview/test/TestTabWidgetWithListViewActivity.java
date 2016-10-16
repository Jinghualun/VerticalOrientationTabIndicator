package com.jingkang.associatedlistview.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

import com.jingkang.associatedlistview.R;
import com.jingkang.associatedlistview.util.IconPagerAdapter;
import com.jingkang.associatedlistview.util.TabWidgetWithListView;

public class TestTabWidgetWithListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tabwidget_listview);
        ListView listView = (ListView) findViewById(R.id.listView);
        TestAdapter adapter = new TestAdapter<String>(this,
                android.R.layout.simple_list_item_1, mNames);
        listView.setAdapter(adapter);
        TabWidgetWithListView tabWidget = (TabWidgetWithListView) findViewById(R.id.tabWidget);
        tabWidget.setListView(listView);
        tabWidget.setSectionIndexer(adapter);
    }

    private class TestAdapter<T> extends ArrayAdapter<T> implements IconPagerAdapter, SectionIndexer {

        public TestAdapter(Context context, int resource, T[] objects) {
            super(context, resource, objects);
        }

        @Override
        public CharSequence getTitle(int index) {
            return CONTENT[index];
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
            return mNames.length;
        }

        @Override
        public Object[] getSections() {
            return null;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            int count = getCount();
            for (int i = 0; i < count; i++) {
                char firstChar = mNames[i].charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }

    private static final String[] CONTENT = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

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

    private String[] mNames = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Babybel",
            "Baguette Laonnaise", "Bakers", "Baladi", "Castelleno", "Castelmagno", "Castelo Branco",
            "Castigliano", "Daralagjazsky", "Dauphin", "Delice des Fiouves", "Denhany Dorset Drum",
            "Emental Grand Cru", "Emlett", "Emmental", "Epoisses de Bourgogne", "Esbareich",
            "Frying Cheese", "Fynbo", "Gabriel", "Galette du Paludier", "Galette Lyonnaise",
            "Hipi Iti", "Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",
            "Jermi Tortes", "Jibneh Arabieh", "Jindi Brie", "Jubilee Blue", "Juustoleipa",
            "Kadchgall", "Kaseri", "Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",
            "La Vache Qui Rit", "Laguiole", "Lairobell", "Lajta", "Lanark Blue", "Lancashire",
            "Macconais", "Mahoe Aged Gouda", "Mahon", "Malvern", "Mamirolle", "Manchego",
            "Neufchatel (Australian)", "Niolo", "Nokkelost", "Northumberland", "Oaxaca",
            "Olde York", "Olivet au Foin", "Olivet Bleu", "Olivet Cendre",
            "Pannerone", "Pant ys Gawn", "Parmesan (Parmigiano)", "Parmigiano Reggiano",
            "Queso Para Frier", "Queso Quesadilla", "Rabacal", "Raclette", "Ragusano", "Raschera",
            "Saint-Nectaire", "Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",
            "Tupi", "Turunmaa", "Tymsboro", "Tyn Grug", "Tyning", "Ubriaco", "Ulloa",
            "Vacherin-Fribourgeois", "Valencay", "Vasterbottenost", "Venaco", "Vendomois",
            "Washed Rind Cheese (Australian)", "Waterloo", "Weichkaese", "Wellington",
            "Xanadu", "Xynotyro", "Yarg Cornish", "Yarra Valley Pyramid", "Yorkshire Blue",
            "Zamorano", "Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"
    };
}
