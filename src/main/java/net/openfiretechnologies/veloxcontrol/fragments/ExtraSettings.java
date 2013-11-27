package net.openfiretechnologies.veloxcontrol.fragments;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.github.GithubViewer;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.SystemProperties;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

import static net.openfiretechnologies.veloxcontrol.util.VeloxConstants.VC_EXTENSIVE_LOGGING;
import static net.openfiretechnologies.veloxcontrol.util.VeloxConstants.VC_OS_DISABLE_SELINUX;

public class ExtraSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String USE_HIGHEND_GFX = "velox.highend.gfx";
    private static final String USE_HIGHEND_GFX_PREF = "use_highend_gfx";
    private static final String ALEX_BUILD_PROP = "alex_extra_build_prop";

    private PreferenceHelper preferenceHelper;
    private PackageManager mPm;
    private boolean mIsVelox = false;

    private SwitchPreference mVcExtensiveLogging;
    private Preference mDynamicChangelog;
    private SwitchPreference mVcLauncherDisable;
    private SwitchPreference mVcOsDisableSelinux;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_extra_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        preferenceHelper = new PreferenceHelper(getActivity());
        boolean mDebug = preferenceHelper.getBoolean(VC_EXTENSIVE_LOGGING);
        mPm = getActivity().getPackageManager();
        mIsVelox = VeloxMethods.isVelox(mDebug);

        PreferenceGroup mBuildProps = (PreferenceGroup) prefSet.findPreference(ALEX_BUILD_PROP);
        CheckBoxPreference mUseHighendGfx = (CheckBoxPreference)
                prefSet.findPreference(USE_HIGHEND_GFX_PREF);
        if (!mIsVelox) {
            prefSet.removePreference(mUseHighendGfx);
            prefSet.removePreference(mBuildProps);
        } else {
            mUseHighendGfx.setChecked(SystemProperties.get(USE_HIGHEND_GFX).equals("1"));
        }

        mVcExtensiveLogging = (SwitchPreference) prefSet.findPreference(VC_EXTENSIVE_LOGGING);
        mVcExtensiveLogging.setChecked(preferenceHelper.getBoolean(VC_EXTENSIVE_LOGGING));
        mVcExtensiveLogging.setOnPreferenceChangeListener(this);

        mVcLauncherDisable = (SwitchPreference) prefSet.findPreference("vc_disable_launcher");
        if (!mIsVelox) {
            mVcLauncherDisable.setChecked(switchLauncher(false));
            mVcLauncherDisable.setEnabled(false);
        } else {
            mVcLauncherDisable.setChecked(switchLauncher(false));
            mVcLauncherDisable.setOnPreferenceChangeListener(this);
        }

        mVcOsDisableSelinux = (SwitchPreference) prefSet.findPreference(VC_OS_DISABLE_SELINUX);
        mVcOsDisableSelinux.setChecked(preferenceHelper.getBoolean(VC_OS_DISABLE_SELINUX));
        mVcOsDisableSelinux.setOnPreferenceChangeListener(this);

        mDynamicChangelog = prefSet.findPreference("dynamic_changelog");

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (preference == mDynamicChangelog) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new GithubViewer())
                    .addToBackStack(null).commit();
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mVcExtensiveLogging) {
            preferenceHelper.setBoolean(VC_EXTENSIVE_LOGGING, mVcExtensiveLogging.isChecked());
            return true;
        } else if (preference == mVcOsDisableSelinux) {
            preferenceHelper.setBoolean(VC_OS_DISABLE_SELINUX, mVcOsDisableSelinux.isChecked());
            return true;
        } else if (preference == mVcLauncherDisable) {
            mVcLauncherDisable.setChecked(switchLauncher(true));
            return true;
        }

        return false;
    }

    private boolean switchLauncher(boolean shouldSwitch) {
        boolean isShowing;

        ComponentName component = new ComponentName("net.openfiretechnologies.veloxcontrol",
                "net.openfiretechnologies.veloxcontrol.activities._DummyLauncher");
        isShowing = ((mPm.getComponentEnabledSetting(component) ==
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
                || (mPm.getComponentEnabledSetting(component) ==
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED));

        if (shouldSwitch) {
            if (mIsVelox) {
                mPm.setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                return true;
            }
            if (isShowing) {
                mPm.setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                mPm.setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
            isShowing = !isShowing;
        }

        return isShowing;
    }

}
