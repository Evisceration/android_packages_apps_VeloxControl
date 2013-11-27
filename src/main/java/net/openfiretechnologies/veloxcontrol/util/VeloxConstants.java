package net.openfiretechnologies.veloxcontrol.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by alex on 03.11.13.
 */
public interface VeloxConstants {

    public static final String TAG = "VeloxControl";

    public static final String VELOXCONTROL_BUILDNUMBER = "1.0.6";

    /* App */
    public static final String APP_FIRST_LAUNCH = "velox_app_first_launch";

    /* Preferences */
    public static final String VC_EXTENSIVE_LOGGING = "vc_extensive_logging";
    public static final String VC_DOUBLE_PRESS_END = "vc_double_press_end";
    public static final String VC_OS_DISABLE_SELINUX = "vc_os_disable_selinux";

    /* Font Tool */
    public static final String TOOLS_FONT_ROBOTOREGULAR = "/system/fonts/Roboto-Regular.ttf";
    public static final String TOOLS_FONT_ROBOTOBOLD = "/system/fonts/Roboto-Bold.ttf";

    /* Tasker */
    public static final String TASKER_SCREENOFF = "tasker_screenoff";
    public static final String TASKER_SCREENOFF_INTERNET = "tasker_screenoff_internet";
    public static final String TASKER_TOOLS_FSTRIM = "tasker_tools_fstrim";
    public static final String TASKER_TOOLS_FSTRIM_INTERVAL = "tasker_tools_fstrim_interval";

    /* PreferenceHelper Keys */
    public static final String PREF_AUTOBACKUP_ENABLED = "velox_autobackup_enabled";
    public static final String PREF_AUTOBACKUP_LIST = "PREF_AUTOBACKUP_LIST";

    public static class VeloxDirectories {
        public static final String VELOX_DATA_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + "VeloxControl";
        public static final String VELOX_FLASHER_DIR = VELOX_DATA_DIR + File.separator + "Flasher";
        public static final String VELOX_FLASHER_RECOVERY_DIR = VELOX_FLASHER_DIR + File.separator + "Recovery";

        public static final String VELOX_BACKUP_DIR = VELOX_DATA_DIR + File.separator + "Backup";
        public static final String VELOX_LOG_DIR = VELOX_DATA_DIR + File.separator + "Logs";
    }

    public static class VeloxFiles {
        public static final String VELOX_LOG_FILE_FSSTRIM = VeloxDirectories.VELOX_LOG_DIR + File.separator + "fstrim.log";
    }

}
