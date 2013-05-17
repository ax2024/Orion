package com.starrynight.android.orion.activity;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.starrynight.android.orion.R;

public class TabNavigation extends SherlockFragmentActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Getting an instance of action bar
        ActionBar actionBar = getSupportActionBar();
        
        // Enabling Tab Navigation mode for this action bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Enabling Title
        actionBar.setDisplayShowTitleEnabled(true);
        
        // Creating Android Tab
        Tab tab1 = actionBar.newTab()
        					.setText("Android")
        					.setTabListener(new CustomTabListener<AndroidFragment>(this, "android", AndroidFragment.class) )
        					.setIcon(R.drawable.android);
        
        // Adding Android Tab to action bar
        actionBar.addTab(tab1);
        
        // Creating Apple Tab
        Tab tab2 = actionBar.newTab()
				.setText("Apple")
				.setTabListener(new CustomTabListener<AppleFragment>(this, "apple", AppleFragment.class))
				.setIcon(R.drawable.apple);
        
        // Adding Apple Tab to action bar
        actionBar.addTab(tab2);        
        
        // Orientation Change Occurred
        if(savedInstanceState!=null){
        	int currentTabIndex = savedInstanceState.getInt("tab_index");
        	actionBar.setSelectedNavigationItem(currentTabIndex);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	int currentTabIndex = getSupportActionBar().getSelectedNavigationIndex();
    	outState.putInt("tab_index", currentTabIndex);
    	super.onSaveInstanceState(outState);
    }
}
