package net.openfiretechnologies.veloxcontrol.util;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by alex on 17.11.13.
 */
public class SystemProperties {

    public static String get(String key) {
        String s;
        try {
            s = Shell.SH.run("getprop " + key).get(0);
        } catch (Exception exc) {
            return "-1";
        }
        return s.isEmpty() ? "-1" : s;
    }

    public static String get(String key, String def) {
        String s;
        try {
            s = Shell.SH.run("getprop " + key).get(0);
        } catch (Exception exc) {
            return def;
        }
        return s;
    }

}
