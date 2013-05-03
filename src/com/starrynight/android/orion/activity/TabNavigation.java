package com.starrynight.android.orion.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.orion.android.R;


public class TabNavigation extends SherlockActivity implements ActionBar.TabListener {
    private TextView mSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_navigation);
        mSelected = (TextView)findViewById(R.id.text);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 1; i <= 3; i++) {
            Tab tab = getSupportActionBar().newTab();
            tab.setText("Tab " + i);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
        mSelected.setText("Selected: " + tab.getText());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }

}
