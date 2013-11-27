package net.openfiretechnologies.veloxcontrol.util;

import android.util.Log;

import java.io.File;

/**
 * Created by alex on 04.11.13.
 */
public class VeloxMethods implements VeloxConstants {

    public static void logDebug(String msg, boolean debug) {
        if (debug) {
            Log.e(TAG, msg);
        }
    }

    public static void logDebug(String tag, String msg, boolean debug) {
        if (debug) {
            Log.e(tag, msg);
        }
    }

    /**
     * Create the Velox Data Directory
     *
     * @return if directories got created
     */
    public static boolean createDirectories(boolean logDebug) {
        boolean exists = true;
        File f = new File(VeloxDirectories.VELOX_DATA_DIR);
        File f1 = new File(VeloxDirectories.VELOX_FLASHER_DIR);
        File f2 = new File(VeloxDirectories.VELOX_FLASHER_RECOVERY_DIR);
        File f3 = new File(VeloxDirectories.VELOX_BACKUP_DIR);
        File f4 = new File(VeloxDirectories.VELOX_LOG_DIR);

        if (!f.isDirectory()) {
            exists = exists && f.mkdirs();
        } else {
            logDebug(VeloxDirectories.VELOX_DATA_DIR + " exists.", logDebug);
        }

        if (!f1.isDirectory()) {
            exists = exists && f1.mkdirs();
        } else {
            logDebug(VeloxDirectories.VELOX_FLASHER_DIR + " exists.", logDebug);
        }

        if (!f2.isDirectory()) {
            exists = exists && f2.mkdirs();
        } else {
            logDebug(VeloxDirectories.VELOX_FLASHER_RECOVERY_DIR + " exists.", logDebug);
        }

        if (!f3.isDirectory()) {
            exists = exists && f3.mkdirs();
        } else {
            logDebug(VeloxDirectories.VELOX_BACKUP_DIR + " exists.", logDebug);
        }

        if (!f4.isDirectory()) {
            exists = exists && f4.mkdirs();
        } else {
            logDebug(VeloxDirectories.VELOX_LOG_DIR + " exists.", logDebug);
        }

        return exists;
    }

    /**
     * Check if the Velox Data Directory exists
     *
     * @return <b>true</b> if the velox directory exists<br />
     * <b>false</b> if the velox directory doesnt exist
     */
    public static boolean checkDirectories() {
        boolean exists = true;
        exists = exists && new File(VeloxDirectories.VELOX_DATA_DIR).isDirectory();
        exists = exists && new File(VeloxDirectories.VELOX_FLASHER_DIR).isDirectory();
        exists = exists && new File(VeloxDirectories.VELOX_FLASHER_RECOVERY_DIR).isDirectory();
        exists = exists && new File(VeloxDirectories.VELOX_BACKUP_DIR).isDirectory();
        exists = exists && new File(VeloxDirectories.VELOX_LOG_DIR).isDirectory();
        return (exists);
    }

    public static boolean isRecoverySupported() {
        return getDeviceClass().isSupported;
    }

    public static String getRecoveryPartition() {
        return getDeviceClass().recoveryPart;
    }

    public static String getRecoveryDownload() {
        return getDeviceClass().recoveryUrl;
    }

    public static boolean checkRecoveryImage(String md5Checksum) {
        return md5Checksum.equals(getDeviceClass().recoveryChecksum);
    }

    public static String getRecoveryTouchDownload() {
        return getDeviceClass().recoveryUrlTouch;
    }

    public static boolean checkRecoveryTouchImage(String md5Checksum) {
        return md5Checksum.equals(getDeviceClass().recoveryChecksumTouch);
    }

    public static String getRecoveryVersion() {
        return getDeviceClass().recoveryVersion;
    }

    public static String getRecoveryTouchVersion() {
        return getDeviceClass().recoveryVersionTouch;
    }

    public static VeloxDevice getDeviceClass() {
        return VeloxDevice.getInstance();
    }

    public static boolean isVelox(boolean debug) {
        String mVersion = SystemProperties.get("ro.velox.version");
        logDebug("ro.velox.version: " + mVersion, debug);
        mVersion = mVersion.replace(".", "");
        logDebug("ro.velox.version replaced: " + mVersion, debug);
        if (mVersion.length() >= 4) {
            mVersion = mVersion.substring(0, 3);
            logDebug("reduced length: " + mVersion, debug);
        }
        int version = Integer.parseInt(mVersion);
        logDebug("Version: " + version, debug);
        return ((version < 107) && (version != -1));
    }
}
