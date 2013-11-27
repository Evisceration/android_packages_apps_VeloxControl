package net.openfiretechnologies.veloxcontrol.fragments.appearance;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import net.openfiretechnologies.veloxcontrol.R;

/**
 * Created by alex on 06.11.13.
 */
public class Statusbar extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    // ====================
    // Keys
    // ====================
    /* Network Stats */
    private static final String STATUS_BAR_NETWORK_STATS = "status_bar_show_network_stats";
    private static final String STATUS_BAR_NETWORK_STATS_UPDATE = "status_bar_network_status_update";
    /* Notifications */
    private static final String SHOW_STATUS_BAR_ON_NOTIFICATION = "show_status_bar_on_notification";
    private static final String KEY_SMS_BREATH = "pref_key_sms_breath";
    private static final String KEY_MISSED_CALL_BREATH = "missed_call_breath";
    /* Aokp Battery Bar */
    private static final String PREF_BATT_BAR = "battery_bar_list";
    private static final String PREF_BATT_BAR_STYLE = "battery_bar_style";
    private static final String PREF_BATT_BAR_COLOR = "battery_bar_color";
    private static final String PREF_BATT_BAR_WIDTH = "battery_bar_thickness";
    private static final String PREF_BATT_ANIMATE = "battery_bar_animate";

    // ====================
    // Elements
    // ====================
    /* Network Stats */
    private ListPreference mStatusBarNetStatsUpdate;
    private CheckBoxPreference mStatusBarNetworkStats;
    /* Notifications */
    private CheckBoxPreference mShowStatusBarOnNotification;
    private CheckBoxPreference mSMSBreath;
    private CheckBoxPreference mMissedCallBreath;
    /* Aokp Battery Bar */
    private ListPreference mBatteryBar;
    private ListPreference mBatteryBarStyle;
    private ListPreference mBatteryBarThickness;
    private CheckBoxPreference mBatteryBarChargingAnimation;
    private ColorPickerPreference mBatteryBarColor;

    // ====================
    // Fields
    // ====================
    private ContentResolver resolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_appearance_statusbar);
        PreferenceScreen prefSet = getPreferenceScreen();
        resolver = getActivity().getContentResolver();

        /* Network Stats */
        mStatusBarNetworkStats = (CheckBoxPreference) prefSet.findPreference(STATUS_BAR_NETWORK_STATS);
        mStatusBarNetStatsUpdate = (ListPreference) prefSet.findPreference(STATUS_BAR_NETWORK_STATS_UPDATE);
        mStatusBarNetworkStats.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_NETWORK_STATS, 0) == 1));

        long statsUpdate = Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_NETWORK_STATS_UPDATE_INTERVAL, 500);
        mStatusBarNetStatsUpdate.setValue(String.valueOf(statsUpdate));
        mStatusBarNetStatsUpdate.setSummary(mStatusBarNetStatsUpdate.getEntry());
        mStatusBarNetStatsUpdate.setOnPreferenceChangeListener(this);

        /* Notifications */
        mShowStatusBarOnNotification = (CheckBoxPreference) prefSet.findPreference(SHOW_STATUS_BAR_ON_NOTIFICATION);
        mShowStatusBarOnNotification.setChecked((Settings.System.getInt(resolver,
                Settings.System.SHOW_STATUS_BAR_ON_NOTIFICATION, 0) == 1));

        mSMSBreath = (CheckBoxPreference) prefSet.findPreference(KEY_SMS_BREATH);
        mSMSBreath.setOnPreferenceChangeListener(this);
        mMissedCallBreath = (CheckBoxPreference) prefSet.findPreference(KEY_MISSED_CALL_BREATH);
        mMissedCallBreath.setOnPreferenceChangeListener(this);

        /* Aokp Battery Bar */
        mBatteryBar = (ListPreference) findPreference(PREF_BATT_BAR);
        mBatteryBar.setOnPreferenceChangeListener(this);
        mBatteryBar.setValue((Settings.System.getInt(resolver, Settings.System.STATUSBAR_BATTERY_BAR, 0)) + "");

        mBatteryBarStyle = (ListPreference) findPreference(PREF_BATT_BAR_STYLE);
        mBatteryBarStyle.setOnPreferenceChangeListener(this);
        mBatteryBarStyle.setValue((Settings.System.getInt(resolver, Settings.System.STATUSBAR_BATTERY_BAR_STYLE, 0)) + "");

        mBatteryBarColor = (ColorPickerPreference) findPreference(PREF_BATT_BAR_COLOR);
        mBatteryBarColor.setOnPreferenceChangeListener(this);

        mBatteryBarChargingAnimation = (CheckBoxPreference) findPreference(PREF_BATT_ANIMATE);
        mBatteryBarChargingAnimation.setChecked(Settings.System.getInt(
                resolver,
                Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE, 0) == 1);

        mBatteryBarThickness = (ListPreference) findPreference(PREF_BATT_BAR_WIDTH);
        mBatteryBarThickness.setOnPreferenceChangeListener(this);
        mBatteryBarThickness.setValue((Settings.System.getInt(resolver, Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, 1)) + "");

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mStatusBarNetStatsUpdate) {
            long updateInterval = Long.valueOf((String) newValue);
            int index = mStatusBarNetStatsUpdate.findIndexOfValue((String) newValue);
            Settings.System.putLong(resolver,
                    Settings.System.STATUS_BAR_NETWORK_STATS_UPDATE_INTERVAL, updateInterval);
            mStatusBarNetStatsUpdate.setSummary(mStatusBarNetStatsUpdate.getEntries()[index]);
            return true;
        } else if (preference == mSMSBreath) {
            Settings.System.putInt(resolver, Settings.System.SMS_BREATH,
                    mSMSBreath.isChecked() ? 0 : 1);
            return true;
        } else if (preference == mMissedCallBreath) {
            Settings.System.putInt(resolver, Settings.System.MISSED_CALL_BREATH,
                    mMissedCallBreath.isChecked() ? 0 : 1);
            return true;
        } else if (preference == mBatteryBarColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                    .valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(resolver,
                    Settings.System.STATUSBAR_BATTERY_BAR_COLOR, intHex);
            return true;
        } else if (preference == mBatteryBar) {
            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(resolver,
                    Settings.System.STATUSBAR_BATTERY_BAR, val);
        } else if (preference == mBatteryBarStyle) {
            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(resolver,
                    Settings.System.STATUSBAR_BATTERY_BAR_STYLE, val);
        } else if (preference == mBatteryBarThickness) {
            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(resolver,
                    Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, val);
        }

        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mStatusBarNetworkStats) {
            Settings.System.putInt(resolver,
                    Settings.System.STATUS_BAR_NETWORK_STATS, mStatusBarNetworkStats.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mSMSBreath) {
            Settings.System.putInt(resolver, Settings.System.SMS_BREATH,
                    mSMSBreath.isChecked() ? 0 : 1);
            return true;
        } else if (preference == mMissedCallBreath) {
            Settings.System.putInt(resolver, Settings.System.MISSED_CALL_BREATH,
                    mMissedCallBreath.isChecked() ? 0 : 1);
            return true;
        } else if (preference == mShowStatusBarOnNotification) {
            Settings.System.putInt(resolver,
                    Settings.System.SHOW_STATUS_BAR_ON_NOTIFICATION,
                    (mShowStatusBarOnNotification.isChecked() ? 1 : 0));
            return true;
        } else if (preference == mBatteryBarChargingAnimation) {
            Settings.System.putInt(resolver,
                    Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


}
