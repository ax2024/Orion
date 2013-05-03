package com.starrynight.android.orion;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class OrionConfiguration {

    private final Context mContext;
    
    private final SharedPreferences mSharedPreferences;
    
    private static final String SHARED_PREFS_NAME = "orion.preferences";
    
   
    /**
     * Creates a new Configuration instance 
     * @param appContext application context
     */
    OrionConfiguration(final Context appContext) {
        this.mContext = appContext;
        mSharedPreferences = mContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);  
    }

    /**
     * Registers a new listener, whose callback will be triggered each time the
     * internal shared preferences are modified
     * @param listener to be registered
     */
    public void registerPreferenceChangeListener(
    		final OnSharedPreferenceChangeListener listener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }
}
