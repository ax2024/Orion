package com.starrynight.android.orion;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;
import android.util.Log;


public class OrionApplication extends android.app.Application {
    final static String TAG = "Orion";
    public static final boolean DEBUG = true;
    
    final static public String getApplicationTag() {
        return TAG;
    }

    private static String mInternalPath = null;
    
    /**
     * The application context
     */
    protected static Context mContext = null;
    /**
     * Application configuration
     */
    private static OrionConfiguration mConfiguration = null;
    
    public static Context getContext() {
        return mContext;
    }

    /**
     * Gets the configuration
     * @return the current configuration
     */
    public static OrionConfiguration getConfiguration() {
        return mConfiguration;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        mContext = this;
        
        // Clean installed apk file automatically
        final File apk = new File(Environment.getExternalStorageDirectory() + "/Download/orion.apk");
        if (apk.exists()) {
            // Found update apk in storage, delete it
            Log.i(TAG, "Cleaning existing update file " 
                + apk.getAbsolutePath());
            apk.delete();
        } 
        
        // Configuration
        mConfiguration = new OrionConfiguration(mContext);
        mInternalPath = mContext.getDir(".management",Context.MODE_PRIVATE).getAbsolutePath();

        mConfiguration.registerPreferenceChangeListener(this.mSettingsListener);
    }

    /**
     * Gets the internal storage path
     * @return the internal storage path
     */
    public static String getInternalPath() {
        return mInternalPath;
    }
    
    private final OnSharedPreferenceChangeListener mSettingsListener = 
            new OnSharedPreferenceChangeListener() {
                /* (non-Javadoc)
                 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
                 */
                @Override
                public void onSharedPreferenceChanged(
                        final SharedPreferences sharedPreferences, final String key) {
                    OrionApplication.getConfiguration();
                }
            };

}
