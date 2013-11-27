package net.openfiretechnologies.veloxcontrol.util;

import android.content.Context;

import net.openfiretechnologies.veloxcontrol.fragments.tools.backup.ActiveDisplayBackup;
import net.openfiretechnologies.veloxcontrol.fragments.tools.backup.HaloBackup;

/**
 * Created by alex on 08.11.13.
 */
public class BackupTool implements VeloxConstants {
    private static BackupTool backupTool;

    public static final int BACKUP_ALL = 0;
    public static final int BACKUP_ADVANCED_DISPLAY = 1;
    public static final int BACKUP_HALO = 2;

    private final Context mContext;

    public static BackupTool getInstance(Context c) {
        if (backupTool == null) {
            backupTool = new BackupTool(c);
        }
        return backupTool;
    }

    private BackupTool(Context c) {
        mContext = c;
    }

    public boolean backup(int type) {
        boolean success = false;

        switch (type) {
            case BACKUP_ALL:
                success = ActiveDisplayBackup.getInstance(mContext).backup() &&
                        HaloBackup.getInstance(mContext).backup();
                break;
            case BACKUP_ADVANCED_DISPLAY:
                success = ActiveDisplayBackup.getInstance(mContext).backup();
                break;
            case BACKUP_HALO:
                success = HaloBackup.getInstance(mContext).backup();
                break;
            default:
                break;
        }

        return success;
    }

    public boolean restore(int type) {
        boolean success = false;

        switch (type) {
            case BACKUP_ALL:
                success = ActiveDisplayBackup.getInstance(mContext).restore();
                success = success || HaloBackup.getInstance(mContext).restore();
                break;
            case BACKUP_ADVANCED_DISPLAY:
                success = ActiveDisplayBackup.getInstance(mContext).restore();
                break;
            case BACKUP_HALO:
                success = HaloBackup.getInstance(mContext).restore();
                break;
            default:
                break;
        }

        return success;
    }
}
