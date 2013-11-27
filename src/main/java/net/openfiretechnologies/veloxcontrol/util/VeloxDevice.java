package net.openfiretechnologies.veloxcontrol.util;

import android.os.Build;

/**
 * Created by alex on 11.11.13.
 */
public class VeloxDevice {
    private static VeloxDevice veloxDevice;
    public boolean isSupported = false;
    public String recoveryVersion = "";
    public String recoveryVersionTouch = "";
    public String recoveryPart = "";
    public String recoveryUrl = "";
    public String recoveryChecksum = "";
    public String recoveryUrlTouch = "";
    public String recoveryChecksumTouch = "";

    public static VeloxDevice getInstance() {
        if (veloxDevice == null) {
            veloxDevice = new VeloxDevice();
        }
        return veloxDevice;
    }

    private VeloxDevice() {
        switch (Build.MODEL) {
            case "LG-P970":
                this.isSupported = LGP970.isSupported;
                this.recoveryVersion = LGP970.recoveryVersion;
                this.recoveryVersionTouch = LGP970.recoveryVersionTouch;
                this.recoveryPart = LGP970.recoveryPart;
                this.recoveryUrl = LGP970.recoveryUrl;
                this.recoveryChecksum = LGP970.recoveryChecksum;
                this.recoveryUrlTouch = LGP970.recoveryUrlTouch;
                this.recoveryChecksumTouch = LGP970.recoveryChecksumTouch;
                break;
            case "GT-I9505":
                this.isSupported = GTI9505.isSupported;
                this.recoveryVersion = GTI9505.recoveryVersion;
                this.recoveryVersionTouch = GTI9505.recoveryVersionTouch;
                this.recoveryPart = GTI9505.recoveryPart;
                this.recoveryUrl = GTI9505.recoveryUrl;
                this.recoveryChecksum = GTI9505.recoveryChecksum;
                this.recoveryUrlTouch = GTI9505.recoveryUrlTouch;
                this.recoveryChecksumTouch = GTI9505.recoveryChecksumTouch;
                break;
        }
    }


    public static class LGP970 extends VeloxDevice {
        public static boolean isSupported = true;
        public static String recoveryVersion = "6.0.4.4";
        public static String recoveryVersionTouch = "6.0.1.4";
        public static String recoveryPart = "/dev/block/mmcblk0p4";
        public static String recoveryUrl = "https://raw.github.com/Evisceration/velox_binaries/master/recoveries/p970/cwm_normal_6044.img";
        public static String recoveryChecksum = "cf12d4e912129809317781f1ccd30507";
        public static String recoveryUrlTouch = "https://raw.github.com/Evisceration/velox_binaries/master/recoveries/p970/cwm_touch_6014.img";
        public static String recoveryChecksumTouch = "ed3f38f4d16c3a441f2a771504e71b00";
    }

    public static class GTI9505 extends VeloxDevice {
        public static boolean isSupported = true;
        public static String recoveryVersion = "6.0.4.4";
        public static String recoveryVersionTouch = "6.0.4.4";
        public static String recoveryPart = "/dev/block/platform/msm_sdcc.1/by-name/recovery";
        public static String recoveryUrl = "https://raw.github.com/Evisceration/velox_binaries/master/recoveries/jfltexx/cwm_normal_6044.img";
        public static String recoveryChecksum = "793dadb2f346bc2bc85933436de6824b";
        public static String recoveryUrlTouch = "https://raw.github.com/Evisceration/velox_binaries/master/recoveries/jfltexx/cwm_touch_6044.img";
        public static String recoveryChecksumTouch = "cd57e8163db0cc3071ecffd456217d0f";
    }
}