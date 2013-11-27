/*
 * Copyright (c) 2013. Alexander Martinz.
 */

package net.openfiretechnologies.veloxcontrol.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public PreferenceHelper(Context c) {
        PreferenceHelper.prefs = PreferenceManager
                .getDefaultSharedPreferences(c);
        PreferenceHelper.editor = prefs.edit();
    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }

    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    public int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    public int getInt(String key, int def) {
        return prefs.getInt(key, def);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return prefs.getBoolean(key, def);
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

}
