package com.starrynight.android.orion;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class OrionConfiguration {

    private final Context mContext;
    
    private final SharedPreferences mSharedPreferences;
    
    private static final String SHARED_PREFS_NAME = "orion.preferences";

    public static final String PREFERENCE_KEY_MEDIABOX_NUM = "mediabox_num";
   
    /**
     * Creates a new Configuration instance 
     * @param appContext application context
     */
    OrionConfiguration(final Context appContext) {
        this.mContext = appContext;
        mSharedPreferences = mContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);  
    }

    public int getMediaboxNum() {
        return this.mSharedPreferences.getInt(PREFERENCE_KEY_MEDIABOX_NUM, 0);
    }
    
    public void setMediaboxNum(int result) {
        mSharedPreferences.edit().putInt(
                PREFERENCE_KEY_MEDIABOX_NUM, result).commit();
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
