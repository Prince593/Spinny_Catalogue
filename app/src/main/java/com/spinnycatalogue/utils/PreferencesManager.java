package com.spinnycatalogue.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helper class to manage shared preference across the application
 */

@SuppressLint("ApplySharedPref")
public class PreferencesManager {

    private final SharedPreferences myPref;

    public PreferencesManager(Context context) {
        String PREF_NAME = "SpinnyPref";
        myPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLongValue(String keyName, long value) {
        myPref.edit().putLong(keyName, value).commit();
    }

    public long getLongValue(String keyName) {
        return myPref.getLong(keyName, 0);
    }

    public void setIntValue(String keyName, int value) {
        myPref.edit().putInt(keyName, value).commit();
    }

    public int getIntValue(String keyName) {
        return myPref.getInt(keyName, 0);
    }

    public int getIntValueColor(String keyName, String defaultColor) {
        return myPref.getInt(keyName, Color.parseColor(defaultColor));
    }

    public void setFloatValue(String keyName, float value) {
        myPref.edit().putFloat(keyName, value).apply();
    }

    public float getFloatValue(String keyName) {
        return myPref.getFloat(keyName, 0);
    }

    public void setStringValue(String keyName, String value) {
        myPref.edit().putString(keyName, value).apply();
    }

    public String getStringValue(String keyName, String defaultValue) {
        return myPref.getString(keyName, defaultValue);
    }

    public String getStringValue(String keyName) {
        return myPref.getString(keyName, "");
    }

    public void setBooleanValue(String keyName, boolean value) {
        myPref.edit().putBoolean(keyName, value).commit();
    }

    public Boolean getBooleanValue(String keyName) {
        boolean defValue = false;
        return myPref.getBoolean(keyName, defValue);
    }

    public void setListValue(String keyName, ArrayList<String> value) {
        myPref.edit().putString(keyName, TextUtils.join(",", value)).commit();
    }

    public ArrayList<String> getListValue(String keyName) {
        String serialized = myPref.getString(keyName, "");
        return new ArrayList<String>(Arrays.asList(TextUtils.split(serialized, ",")));
    }


    /*
        To remove a specific key from preferences
     */
    public void remove(String key) {
        myPref.edit().remove(key).commit();
    }

    /*
       To clear the shared preferences
     */
    public void clear() {
        myPref.edit().clear().commit();
    }
}
