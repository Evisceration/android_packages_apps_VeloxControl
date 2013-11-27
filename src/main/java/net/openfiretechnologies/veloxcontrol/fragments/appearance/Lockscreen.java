package net.openfiretechnologies.veloxcontrol.fragments.appearance;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.fragments.dialogs.AdvancedTransparencyDialog;

/**
 * Created by alex on 06.11.13.
 */
public class Lockscreen extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    // ====================
    // Keys
    // ====================
    /* SmartCover Wake */
    private static final String PREF_SMART_COVER_CATEGORY = "smart_cover_category";
    private static final String PREF_SMART_COVER_WAKE = "smart_cover_wake";
    /* LockScreen Carousel */
    private static final String PREF_LOCKSCREEN_USE_CAROUSEL = "lockscreen_use_widget_container_carousel";
    /* Battery Around Lockscreen */
    private static final String BATTERY_AROUND_LOCKSCREEN_RING = "battery_around_lockscreen_ring";
    /* Transparency */
    private static final String STATUS_BAR_TRANSPARENT_ON_KEYGUARD = "status_bar_transparent_on_keyguard";
    private static final String KEY_TRANSPARENCY_DIALOG = "transparency_dialog";

    // ====================
    // Elements
    // ====================
    /* SmartCover Wake */
    private CheckBoxPreference mSmartCoverWake;
    /* LockScreen Carousel */
    CheckBoxPreference mLockscreenUseCarousel;
    /* Battery Around Lockscreen */
    private CheckBoxPreference mLockRingBattery;
    /* Transparency */
    private Preference mTransparencyDialog;
    private CheckBoxPreference mStatusBarTransparentOnKeyguard;

    // ====================
    // Fields
    // ====================
    private ContentResolver resolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_appearance_lockscreen);
        PreferenceScreen prefSet = getPreferenceScreen();
        resolver = getActivity().getContentResolver();


        /* SmartCover Wake */
        mSmartCoverWake = (CheckBoxPreference) findPreference(PREF_SMART_COVER_WAKE);
        mSmartCoverWake.setOnPreferenceChangeListener(this);
        if (!getResources().getBoolean(com.android.internal.R.bool.config_lidControlsSleep)) {
            PreferenceCategory smartCoverOptions = (PreferenceCategory)
                    prefSet.findPreference(PREF_SMART_COVER_CATEGORY);
            prefSet.removePreference(smartCoverOptions);
        }

        /* LockScreen Carousel */
        mLockscreenUseCarousel = (CheckBoxPreference) findPreference(PREF_LOCKSCREEN_USE_CAROUSEL);
        mLockscreenUseCarousel.setOnPreferenceChangeListener(this);
        mLockscreenUseCarousel.setChecked(Settings.System.getInt(resolver,
                Settings.System.LOCKSCREEN_USE_WIDGET_CONTAINER_CAROUSEL, 0) == 1);

        /* Battery Around Lockscreen */
        mLockRingBattery = (CheckBoxPreference) prefSet.findPreference(BATTERY_AROUND_LOCKSCREEN_RING);
        if (mLockRingBattery != null) {
            mLockRingBattery.setChecked(Settings.System.getInt(resolver,
                    Settings.System.BATTERY_AROUND_LOCKSCREEN_RING, 0) == 1);
        }

        /* Transparency */
        mStatusBarTransparentOnKeyguard = (CheckBoxPreference) prefSet.findPreference(STATUS_BAR_TRANSPARENT_ON_KEYGUARD);
        mStatusBarTransparentOnKeyguard.setChecked(Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_TRANSPARENT_ON_KEYGUARD, 1) == 1);
        mStatusBarTransparentOnKeyguard.setOnPreferenceChangeListener(this);

        mTransparencyDialog = findPreference(KEY_TRANSPARENCY_DIALOG);


    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mLockRingBattery) {
            Settings.System.putInt(resolver,
                    Settings.System.BATTERY_AROUND_LOCKSCREEN_RING, mLockRingBattery.isChecked() ? 1 : 0);
            return true;
        } else if (preference == mTransparencyDialog) {
            openTransparencyDialog();
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusBarTransparentOnKeyguard) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.STATUS_BAR_TRANSPARENT_ON_KEYGUARD, value ? 1 : 0);
            return true;
        } else if (preference == mSmartCoverWake) {
            Settings.System.putInt(resolver,
                    Settings.System.LOCKSCREEN_LID_WAKE, (Boolean) newValue ? 1 : 0);
            return true;
        } else if (preference == mLockscreenUseCarousel) {
            Settings.System.putInt(resolver,
                    Settings.System.LOCKSCREEN_USE_WIDGET_CONTAINER_CAROUSEL, (Boolean) newValue ? 1 : 0);
            return true;
        }
        return false;
    }

    /**
     * Adds the Transparency Dialog to the Fragment
     */
    private void openTransparencyDialog() {
        getFragmentManager().beginTransaction().add(new AdvancedTransparencyDialog(), null).commit();
    }
}
