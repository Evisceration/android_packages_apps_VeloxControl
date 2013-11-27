/*
 * Copyright (c) Alexander Martinz @ OpenFire Technologies Pvt Ltd.
 */

package eu.chainfire.libsuperuser;

import android.util.Log;

import net.openfiretechnologies.veloxcontrol.BuildConfig;

/**
 * Utility class that intentionally does nothing when not in debug mode
 */
public class Debug {
    /**
     * Log a message if we are in debug mode
     *
     * @param message The message to log
     */
    public static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d("libsuperuser", "[libsuperuser]" + (!message.startsWith("[") && !message.startsWith(" ") ? " " : "") + message);
        }
    }
}
