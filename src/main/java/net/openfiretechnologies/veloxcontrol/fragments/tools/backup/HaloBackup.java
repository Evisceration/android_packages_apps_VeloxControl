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
public class HaloBackup implements VeloxConstants {

    private static HaloBackup mHaloBackup;
    private ContentResolver resolver;

    public static final String HALO_PATH = "halo.velox";

    public static HaloBackup getInstance(Context context) {
        if (mHaloBackup == null) {
            mHaloBackup = new HaloBackup(context);
        }
        return mHaloBackup;
    }

    private HaloBackup(Context context) {
        resolver = context.getContentResolver();
    }

    public boolean backup() {
        boolean success = true;
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(VeloxDirectories.VELOX_BACKUP_DIR + File.separator + HALO_PATH);
            // Active
            String toWrite = Settings.System.HALO_ACTIVE + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_ACTIVE, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Hide
            toWrite = Settings.System.HALO_HIDE + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_HIDE, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Ninja
            toWrite = Settings.System.HALO_NINJA + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_NINJA, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Messagebox
            toWrite = Settings.System.HALO_MSGBOX + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_MSGBOX, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Unlock Ping
            toWrite = Settings.System.HALO_UNLOCK_PING + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_UNLOCK_PING, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Notify Count
            toWrite = Settings.System.HALO_NOTIFY_COUNT + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_NOTIFY_COUNT, 4) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Messagebox Animation
            toWrite = Settings.System.HALO_MSGBOX_ANIMATION + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_MSGBOX_ANIMATION, 2) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Reverse
            toWrite = Settings.System.HALO_REVERSED + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_REVERSED, 0) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Halo Size
            toWrite = Settings.System.HALO_SIZE + "=" + Settings.System.getFloat(resolver,
                    Settings.System.HALO_SIZE, 1.0f) + "\n";
            fileOutputStream.write(toWrite.getBytes());
            // Pause
            toWrite = Settings.System.HALO_PAUSE + "=" + Settings.System.getInt(resolver,
                    Settings.System.HALO_PAUSE, 0) + "\n";
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
            fileInputStream = new FileInputStream(VeloxDirectories.VELOX_BACKUP_DIR + File.separator + HALO_PATH);
            StringBuilder sb = new StringBuilder("");
            byte[] buffer = new byte[1024];

            while (fileInputStream.read(buffer) != -1) {
                sb.append(new String(buffer));
            }
            String[] backupPairs = sb.toString().split("\\r?\\n");

            // Active
            Settings.System.putInt(resolver,
                    Settings.System.HALO_ACTIVE,
                    Integer.parseInt(backupPairs[0].split("=")[1]));
            // Hide
            Settings.System.putInt(resolver,
                    Settings.System.HALO_HIDE,
                    Integer.parseInt(backupPairs[1].split("=")[1]));
            // Ninja
            Settings.System.putInt(resolver,
                    Settings.System.HALO_NINJA,
                    Integer.parseInt(backupPairs[2].split("=")[1]));
            // Messagebox
            Settings.System.putInt(resolver,
                    Settings.System.HALO_MSGBOX_ANIMATION,
                    Integer.parseInt(backupPairs[3].split("=")[1]));
            // Unlock Ping
            Settings.System.putInt(resolver,
                    Settings.System.HALO_UNLOCK_PING,
                    Integer.parseInt(backupPairs[4].split("=")[1]));
            // Notify Count
            Settings.System.putInt(resolver,
                    Settings.System.HALO_NOTIFY_COUNT,
                    Integer.parseInt(backupPairs[5].split("=")[1]));
            // Messagebox Animation
            Settings.System.putInt(resolver,
                    Settings.System.HALO_MSGBOX_ANIMATION,
                    Integer.parseInt(backupPairs[6].split("=")[1]));
            // Reverse
            Settings.System.putInt(resolver,
                    Settings.System.HALO_REVERSED,
                    Integer.parseInt(backupPairs[7].split("=")[1]));
            // Halo Size
            Settings.System.putFloat(resolver,
                    Settings.System.HALO_SIZE,
                    Float.parseFloat(backupPairs[8].split("=")[1]));
            // Pause
            Settings.System.putInt(resolver,
                    Settings.System.HALO_PAUSE,
                    Integer.parseInt(backupPairs[9].split("=")[1]));
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
