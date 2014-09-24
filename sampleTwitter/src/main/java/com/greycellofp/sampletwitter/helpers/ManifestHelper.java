package com.greycellofp.sampletwitter.helpers;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.greycellofp.sampletwitter.app.SampleTwitterApplication;

/**
 * Created by pawan on 9/23/14.
 */
public class ManifestHelper {
    private static final String TAG = ManifestHelper.class.getSimpleName();
 
    public static Bundle getManifestMeta(){
        try{
            SampleTwitterApplication app = SampleTwitterApplication.getInstance();
            ComponentName comp = new ComponentName(app, app.getClass());
            ApplicationInfo applicationInfo = app.getPackageManager().getApplicationInfo(comp.getPackageName(),
                    PackageManager.GET_META_DATA);
            return applicationInfo.metaData;
        }
        catch (Exception e){
            Log.e(TAG, "Failed to get Meta", e);
        }
        return null;
    }
 
    public static String getMetaValue(String key, String defaultValue) {
        Bundle metaInfo = getManifestMeta();
        if (metaInfo != null) {
            return metaInfo.getString(key);
        }
        return defaultValue;
    }
 
    public static float getMetaValue(String key, float defaultValue){
        Bundle metaInfo = getManifestMeta();
        if (metaInfo != null) {
            return metaInfo.getFloat(key);
        }
        return defaultValue;
    }
 
    public static boolean getMetaValue(String key, boolean defaultValue) {
        Bundle metaInfo = getManifestMeta();
        if (metaInfo != null) {
            return metaInfo.getBoolean(key);
        }
        return defaultValue;
    }
}