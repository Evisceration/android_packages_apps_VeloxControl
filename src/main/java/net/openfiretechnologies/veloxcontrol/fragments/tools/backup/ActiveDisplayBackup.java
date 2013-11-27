package net.openfiretechnologies.veloxcontrol.fragments.tools.backup;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by alex on 12.11.13.
 */
public class ActiveDisplayBackup implements VeloxConstants {

    private static ActiveDisplayBackup mActiveDisplayBackup;
    private ContentResolver resolver;

    public static final String ADVANCED_DISPLAY_PATH = "advanced_display.velox";

    public static ActiveDisplayBackup getInstance(Context context) {
        if (mActiveDisplayBackup == null) {
            mActiveDisplayBackup = new ActiveDisplayBackup(context);
        }
        return mActiveDisplayBackup;
    }

    private ActiveDisplayBackup(Context context) {
        resolver = context.getContentResolver();
    }

    public boolean backup() {
        boolean success = true;
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(VeloxDirectories.VELOX_BACKUP_DIR + File.separator + ADVANCED_DISPLAY_PATH);
            // Enabled
            String toWrite = Settings.System.ENABLE_ACTIVE_DISPLAY + "=" + Settings.System.getInt(resolver,
                    Settings.System.ENABLE_ACTIVE_DISPLAY, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Text
            toWrite = Settings.System.ACTIVE_DISPLAY_TEXT + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_TEXT, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Low-Priority
            toWrite = Settings.System.ACTIVE_DISPLAY_HIDE_LOW_PRIORITY_NOTIFICATIONS + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_HIDE_LOW_PRIORITY_NOTIFICATIONS, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Pocket Mode
            toWrite = Settings.System.ACTIVE_DISPLAY_POCKET_MODE + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_POCKET_MODE, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Sunlight Mode
            toWrite = Settings.System.ACTIVE_DISPLAY_SUNLIGHT_MODE + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_SUNLIGHT_MODE, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Redisplay
            toWrite = Settings.System.ACTIVE_DISPLAY_REDISPLAY + "=" + Settings.System.getLong(resolver,
                    Settings.System.ACTIVE_DISPLAY_REDISPLAY, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Show Date
            toWrite = Settings.System.ACTIVE_DISPLAY_SHOW_DATE + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_SHOW_DATE, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Show AM/PM
            toWrite = Settings.System.ACTIVE_DISPLAY_SHOW_AMPM + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_SHOW_AMPM, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Brightness
            toWrite = Settings.System.ACTIVE_DISPLAY_BRIGHTNESS + "=" + Settings.System.getInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_BRIGHTNESS, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Display Timeout
            toWrite = Settings.System.ACTIVE_DISPLAY_TIMEOUT + "=" + Settings.System.getLong(resolver,
                    Settings.System.ACTIVE_DISPLAY_TIMEOUT, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Excludes
            toWrite = Settings.System.ACTIVE_DISPLAY_EXCLUDED_APPS + "=" + Settings.System.getString(resolver,
                    Settings.System.ACTIVE_DISPLAY_EXCLUDED_APPS) + "\n";
            fileOutputStream.write(toWrite.getBytes());
        } catch (Exception exc) {
            success = false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (Exception ignored) {
            }
        }

        return success;
    }

    public boolean restore() {
        boolean success = true;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(VeloxDirectories.VELOX_BACKUP_DIR + File.separator + ADVANCED_DISPLAY_PATH);
            StringBuilder sb = new StringBuilder("");
            byte[] buffer = new byte[1024];

            while (fileInputStream.read(buffer) != -1) {
                sb.append(new String(buffer));
            }
            String[] backupPairs = sb.toString().split("\\r?\\n");

            // Enabled
            Settings.System.putInt(resolver,
                    Settings.System.ENABLE_ACTIVE_DISPLAY,
                    Integer.parseInt(backupPairs[0].split("=")[1]));
            // Text
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_TEXT,
                    Integer.parseInt(backupPairs[1].split("=")[1]));
            // Low-Priority
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_HIDE_LOW_PRIORITY_NOTIFICATIONS,
                    Integer.parseInt(backupPairs[2].split("=")[1]));
            // Pocket Mode
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_POCKET_MODE,
                    Integer.parseInt(backupPairs[3].split("=")[1]));
            // Sunlight Mode
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_SUNLIGHT_MODE,
                    Integer.parseInt(backupPairs[4].split("=")[1]));
            // Redisplay
            Settings.System.putLong(resolver,
                    Settings.System.ACTIVE_DISPLAY_REDISPLAY,
                    Long.parseLong(backupPairs[5].split("=")[1]));
            // Show Date
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_SHOW_DATE,
                    Integer.parseInt(backupPairs[6].split("=")[1]));
            // Show AM/PM
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_SHOW_AMPM,
                    Integer.parseInt(backupPairs[7].split("=")[1]));
            // Brightness
            Settings.System.putInt(resolver,
                    Settings.System.ACTIVE_DISPLAY_BRIGHTNESS,
                    Integer.parseInt(backupPairs[8].split("=")[1]));
            // Display Timeout
            Settings.System.putLong(resolver,
                    Settings.System.ACTIVE_DISPLAY_TIMEOUT,
                    Long.parseLong(backupPairs[9].split("=")[1]));
            // Excludes
            Settings.System.putString(resolver,
                    Settings.System.ACTIVE_DISPLAY_EXCLUDED_APPS,
                    backupPairs[10].split("=")[1]);
        } catch (Exception exc) {
            success = false;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception ignored) {
            }
        }

        return success;
    }

}
