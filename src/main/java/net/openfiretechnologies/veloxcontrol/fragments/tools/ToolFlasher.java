package net.openfiretechnologies.veloxcontrol.fragments.tools;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.util.ConnectionDetector;
import net.openfiretechnologies.veloxcontrol.util.Helpers;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;
import net.openfiretechnologies.veloxcontrol.util.WakeLocker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05.11.13.
 */
public class ToolFlasher extends PreferenceFragment implements VeloxConstants {

    Preference mCwmNormal;
    Preference mCwmTouch;
    Context mContext;
    boolean mDebug;
    String mType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_tools_flasher);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();
        mDebug = new PreferenceHelper(mContext).getBoolean(VC_EXTENSIVE_LOGGING);

        mCwmNormal = prefSet.findPreference("alex_tools_flasher_cwm_normal");
        mCwmTouch = prefSet.findPreference("alex_tools_flasher_cwm_touch");

        if (!VeloxMethods.isRecoverySupported()) {
            prefSet.removePreference(mCwmNormal);
            prefSet.removePreference(mCwmTouch);
        } else {
            mCwmNormal.setSummary(VeloxMethods.getRecoveryVersion());
            mCwmTouch.setSummary(VeloxMethods.getRecoveryTouchVersion());
        }

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        int type = 0;
        boolean setMod = false;

        if (preference == mCwmNormal) {
            mType = "cwmnormal";
            type = 1;
            setMod = true;
        } else if (preference == mCwmTouch) {
            mType = "cwmtouch";
            type = 1;
            setMod = true;
        }

        switch (type) {
            case 0:
            default:
                break;
            case 1:
                if (VeloxMethods.isRecoverySupported()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("Warning!");
                    dialog.setMessage("This may or may not brick your device!\n" +
                            "Use on your own risk!\n" +
                            "Proceed?\n\n" +
                            "Your Model: " + Build.MODEL);
                    dialog.setCancelable(true);
                    dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new FlashTask().execute(mType);
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(mContext, "Device not supported!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return (setMod || super.onPreferenceTreeClick(preferenceScreen, preference));
    }

    private void logDebug(String msg) {
        VeloxMethods.logDebug(msg, mDebug);
    }

    private class FlashTask extends AsyncTask<String, String, String> implements VeloxConstants {

        ProgressDialog dialog;
        Context mContext;
        String sSaveTo = "";
        String sType = "";
        boolean mDebug = false;

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            WakeLocker.release();

            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("Flasher");
            dialog.setMessage(s);
            dialog.setCancelable(true);
            dialog.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            mContext = getActivity();
            mDebug = new PreferenceHelper(mContext).getBoolean(VC_EXTENSIVE_LOGGING);
            WakeLocker.acquirePartial(mContext);
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("Downloading");
            dialog.setMessage("Downloading...");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            dialog.setMessage(progress[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            sType = strings[0];
            String sUrl;
            int iType;
            String result;
            logDebug("sType: " + sType);

            switch (sType) {
                case "cwmnormal":
                    sUrl = VeloxMethods.getRecoveryDownload();
                    sSaveTo = VeloxDirectories.VELOX_FLASHER_RECOVERY_DIR + File.separator + "cwm_normal.img";
                    iType = 1;
                    break;
                case "cwmtouch":
                    sUrl = VeloxMethods.getRecoveryTouchDownload();
                    sSaveTo = VeloxDirectories.VELOX_FLASHER_RECOVERY_DIR + File.separator + "cwm_touch.img";
                    iType = 1;
                    break;
                default:
                    return null;
            }
            logDebug("sUrl: " + sUrl);

            if (sUrl.equals("-1") || sUrl.isEmpty()) {
                return "Device not supported!";
            }

            // Download if not existing
            if (!new File(sSaveTo).exists()) {

                if (!new ConnectionDetector(mContext).isConnectingToInternet()) {
                    return "No Internet!";
                }

                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(sUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();

                    input = connection.getInputStream();
                    output = new FileOutputStream(sSaveTo);

                    byte data[] = new byte[4096];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        /*if (isCancelled())
                            return null;*/
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }
            }

            // Flash
            switch (iType) {
                default:
                case 0:
                    return "I do not know what to do!";
                case 1:
                    result = flashRecovery();
                    break;
            }

            return result;
        }

        private String flashRecovery() {

            publishProgress("Checking File");

            if (VeloxMethods.isRecoverySupported()) {
                if (!new File(sSaveTo).exists()) {
                    return "File not properly downloaded!";
                }
                try {
                    if (sType.equals("cwmnormal")) {
                        if (!VeloxMethods.checkRecoveryImage(Helpers.MD5Checksum.getMD5Checksum(sSaveTo)))
                            return "Wrong file checksum!\nPlease delete the File and try again:\n\n" + sSaveTo;
                    } else {
                        if (!VeloxMethods.checkRecoveryTouchImage(Helpers.MD5Checksum.getMD5Checksum(sSaveTo)))
                            return "Wrong file checksum!\nPlease delete the File and try again:\n\n" + sSaveTo;
                    }
                } catch (Exception exc) {
                }

                publishProgress("Flashing");

                String part = VeloxMethods.getRecoveryPartition();

                List<String> commands = new ArrayList<>();
                commands.add("dd if=" + sSaveTo + " of=" + part + "\n");
                Helpers.shExec(commands, true);
            } else {
                return "Device not supported!";
            }
            return "Successfully flashed " + sSaveTo + " as Recovery!";
        }

        private void logDebug(String msg) {
            VeloxMethods.logDebug(msg, mDebug);
        }
    }
}
