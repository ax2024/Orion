package com.starrynight.android.orion.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.starrynight.android.orion.R;

public class AppleFragment extends SherlockListFragment{
	
    /** An array of items to display*/
    String apple_versions[] = new String[]{
            "Mountain Lion",
            "Lion",
            "Snow Leopard",
            "Leopard",
            "Tiger",
            "Panther"
    };
    
    /** An array of images to display*/
    int apple_images[] = new int[]{
            R.drawable.mountainlion,
            R.drawable.lion,
            R.drawable.snowleopard,
            R.drawable.leopard,
            R.drawable.tiger,
            R.drawable.panther
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
     	
    	// Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<5;i++){
                HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", apple_versions[i]);
            hm.put("img", Integer.toString(apple_images[i]  ) );
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "img","txt" };

        // Ids of views in listview_layout
        int[] to = { R.id.img,R.id.txt};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
        
        
        // Setting the adapter to the listView
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }    
}