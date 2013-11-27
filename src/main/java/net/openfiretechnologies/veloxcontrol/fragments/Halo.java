package net.openfiretechnologies.veloxcontrol.fragments;

import android.app.ActivityManager;
import android.app.INotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.util.BackupTool;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

public class Halo extends PreferenceFragment implements Preference.OnPreferenceChangeListener, VeloxConstants {

    private Context mContext;
    private ContentResolver resolver;
    private PreferenceHelper mPrefs;
    private boolean mDebug = false;
    private Handler mHandler;
    private boolean mAutoBackup = false;
    private boolean mBackupRunning = false;

    private static final String KEY_HALO_ACTIVE = "halo_active";
    private static final String KEY_HALO_STATE = "halo_state";
    private static final String KEY_HALO_HIDE = "halo_hide";
    private static final String KEY_HALO_NINJA = "halo_ninja";
    private static final String KEY_HALO_MSGBOX = "halo_msgbox";
    private static final String KEY_HALO_MSGBOX_ANIMATION = "halo_msgbox_animation";
    private static final String KEY_HALO_NOTIFY_COUNT = "halo_notify_count";
    private static final String KEY_HALO_UNLOCK_PING = "halo_unlock_ping";
    private static final String KEY_HALO_SIZE = "halo_size";
    private static final String KEY_HALO_PAUSE = "halo_pause";
    private static final String KEY_HALO_REVERSED = "halo_reversed";

    private SwitchPreference mHaloActive;
    private ListPreference mHaloState;
    private CheckBoxPreference mHaloHide;
    private ListPreference mHaloSize;
    private ListPreference mHaloNotifyCount;
    private ListPreference mHaloMsgAnimate;

    private CheckBoxPreference mHaloNinja;
    private CheckBoxPreference mHaloMsgBox;
    private CheckBoxPreference mHaloUnlockPing;
    private CheckBoxPreference mHaloPause;
    private CheckBoxPreference mHaloReversed;

    private INotificationManager mNotificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_main_halo);
        PreferenceScreen prefSet = getPreferenceScreen();

        mContext = getActivity();
        resolver = getActivity().getContentResolver();
        mPrefs = new PreferenceHelper(mContext);

        mDebug = mPrefs.getBoolean(VC_EXTENSIVE_LOGGING);

        if (mPrefs.getBoolean(PREF_AUTOBACKUP_ENABLED)) {
            String[] backup = mPrefs.getString(PREF_AUTOBACKUP_LIST).split("\\|");
            for (String s : backup) {
                if (s.equals(BackupTool.BACKUP_HALO) || s.equals(BackupTool.BACKUP_ALL))
                    mAutoBackup = true;
            }
        }
        logDebug("Halo - AutoBackup: " + (mAutoBackup ? "true" : "false"));

        mNotificationManager = INotificationManager.Stub.asInterface(
                ServiceManager.getService(Context.NOTIFICATION_SERVICE));

        mHaloActive = (SwitchPreference) prefSet.findPreference(KEY_HALO_ACTIVE);
        mHaloActive.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_ACTIVE, 0) == 1);
        mHaloActive.setOnPreferenceChangeListener(this);

        mHaloState = (ListPreference) prefSet.findPreference(KEY_HALO_STATE);
        mHaloState.setValue(String.valueOf((isHaloPolicyBlack() ? "1" : "0")));
        mHaloState.setOnPreferenceChangeListener(this);

        mHaloHide = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_HIDE);
        mHaloHide.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_HIDE, 0) == 1);
        mHaloHide.setOnPreferenceChangeListener(this);

        mHaloNinja = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_NINJA);
        mHaloNinja.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_NINJA, 0) == 1);
        mHaloNinja.setOnPreferenceChangeListener(this);

        mHaloMsgBox = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_MSGBOX);
        mHaloMsgBox.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_MSGBOX, 1) == 1);
        mHaloMsgBox.setOnPreferenceChangeListener(this);

        mHaloUnlockPing = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_UNLOCK_PING);
        mHaloUnlockPing.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_UNLOCK_PING, 0) == 1);
        mHaloUnlockPing.setOnPreferenceChangeListener(this);

        mHaloNotifyCount = (ListPreference) prefSet.findPreference(KEY_HALO_NOTIFY_COUNT);
        try {
            int haloCounter = Settings.System.getInt(resolver,
                    Settings.System.HALO_NOTIFY_COUNT, 4);
            mHaloNotifyCount.setValue(String.valueOf(haloCounter));
        } catch (Exception ex) {
            // fail...
        }
        mHaloNotifyCount.setOnPreferenceChangeListener(this);

        mHaloMsgAnimate = (ListPreference) prefSet.findPreference(KEY_HALO_MSGBOX_ANIMATION);
        try {
            int haloMsgAnimation = Settings.System.getInt(resolver,
                    Settings.System.HALO_MSGBOX_ANIMATION, 2);
            mHaloMsgAnimate.setValue(String.valueOf(haloMsgAnimation));
        } catch (Exception ex) {
            // fail...
        }
        mHaloMsgAnimate.setOnPreferenceChangeListener(this);

        mHaloReversed = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_REVERSED);
        mHaloReversed.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_REVERSED, 1) == 1);
        mHaloReversed.setOnPreferenceChangeListener(this);

        mHaloSize = (ListPreference) prefSet.findPreference(KEY_HALO_SIZE);
        try {
            float haloSize = Settings.System.getFloat(resolver,
                    Settings.System.HALO_SIZE, 1.0f);
            mHaloSize.setValue(String.valueOf(haloSize));
        } catch (Exception ex) {
            // So what
        }
        mHaloSize.setOnPreferenceChangeListener(this);

        int isLowRAM = (ActivityManager.isLargeRAM()) ? 0 : 1;
        mHaloPause = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_PAUSE);
        mHaloPause.setChecked(Settings.System.getInt(resolver,
                Settings.System.HALO_PAUSE, isLowRAM) == 1);
        mHaloPause.setOnPreferenceChangeListener(this);


    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean changed = false;

        if (preference == mHaloActive) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_ACTIVE, (Boolean) newValue ? 1 : 0);
            changed = true;
        } else if (preference == mHaloState) {
            boolean state = Integer.valueOf((String) newValue) == 1;
            try {
                mNotificationManager.setHaloPolicyBlack(state);
            } catch (android.os.RemoteException ex) {
                // System dead
            }
            changed = true;
        } else if (preference == mHaloMsgAnimate) {
            int haloMsgAnimation = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.HALO_MSGBOX_ANIMATION, haloMsgAnimation);
            changed = true;
        } else if (preference == mHaloNotifyCount) {
            int haloNotifyCount = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.HALO_NOTIFY_COUNT, haloNotifyCount);
            changed = true;
        } else if (preference == mHaloSize) {
            float haloSize = Float.valueOf((String) newValue);
            Settings.System.putFloat(resolver,
                    Settings.System.HALO_SIZE, haloSize);
            changed = true;
        } else if (preference == mHaloActive) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_ACTIVE, mHaloActive.isChecked()
                    ? 1 : 0);
            changed = true;
        } else if (preference == mHaloHide) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_HIDE, mHaloHide.isChecked()
                    ? 1 : 0);
            changed = true;
        } else if (preference == mHaloNinja) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_NINJA, mHaloNinja.isChecked()
                    ? 1 : 0);
            changed = true;
        } else if (preference == mHaloMsgBox) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_MSGBOX, mHaloMsgBox.isChecked()
                    ? 1 : 0);
            changed = true;
        } else if (preference == mHaloUnlockPing) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_UNLOCK_PING, mHaloUnlockPing.isChecked()
                    ? 1 : 0);
            changed = true;
        } else if (preference == mHaloPause) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_PAUSE, mHaloPause.isChecked()
                    ? 1 : 0);
            changed = true;
        } else if (preference == mHaloReversed) {
            Settings.System.putInt(resolver,
                    Settings.System.HALO_REVERSED, mHaloReversed.isChecked()
                    ? 1 : 0);
            changed = true;
        }

        if (mAutoBackup) {
            BackupDelayed();
        }

        return changed;
    }

    private boolean isHaloPolicyBlack() {
        try {
            return mNotificationManager.isHaloPolicyBlack();
        } catch (android.os.RemoteException ex) {
            // System dead
        }
        return true;
    }

    private void BackupDelayed() {

        if (mBackupRunning) {
            mHandler.removeCallbacks(backupRunnable);
            mBackupRunning = false;
            logDebug("Halo - Canceled Backup");
        }
        mHandler = new Handler();
        mHandler.postDelayed(backupRunnable, 5000);
        mBackupRunning = true;
        logDebug("Halo - Scheduled Backup");
    }

    private Runnable backupRunnable = new Runnable() {

        @Override
        public void run() {
            BackupTool.getInstance(mContext).backup(BackupTool.BACKUP_HALO);
            mBackupRunning = false;
            logDebug("Halo - Backup Ran");
        }
    };

    private void logDebug(String msg) {
        VeloxMethods.logDebug(msg, mDebug);
    }
}
