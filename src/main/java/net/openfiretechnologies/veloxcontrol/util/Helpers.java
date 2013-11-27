package net.openfiretechnologies.veloxcontrol.util;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class Helpers implements VeloxConstants {

    /**
     * Restart the activity smoothly
     *
     * @param activity The activity to restart
     */
    public static void restartActivity(final Activity activity) {
        if (activity == null)
            return;
        final int enter_anim = android.R.anim.fade_in;
        final int exit_anim = android.R.anim.fade_out;
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.finish();
        activity.overridePendingTransition(enter_anim, exit_anim);
        activity.startActivity(activity.getIntent());
    }

    public static String ReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = String.valueOf("KMGTPE".charAt(exp - 1));
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    public static String shExec(List<String> s, Boolean su) {

        VeloxMethods.logDebug(s.toString(), true);
        List<String> result;
        StringBuilder sb = new StringBuilder();

        if (su)
            result = Shell.SU.run(s);
        else
            result = Shell.SH.run(s);

        for (String _s : result) {
            sb.append(_s).append("\n");
        }

        return sb.toString();
    }

    public static class MD5Checksum {

        public static byte[] createChecksum(String filename) throws Exception {
            InputStream fis = new FileInputStream(filename);

            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            return complete.digest();
        }

        public static String getMD5Checksum(String filename) throws Exception {
            byte[] b = createChecksum(filename);
            String result = "";

            for (byte aB : b) {
                result += Integer.toString((aB & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        }
    }
}
