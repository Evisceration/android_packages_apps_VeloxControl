package net.openfiretechnologies.veloxcontrol.fragments.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.services.TaskerService;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

/**
 * Created by alex on 11.11.13.
 */
public class ScreenOff extends PreferenceFragment implements Preference.OnPreferenceChangeListener, VeloxConstants {

    PreferenceHelper mPreferenceHelper;
    SwitchPreference mScreenOff;
    SwitchPreference mInternetToggle;
    private boolean mDebug = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_tools_tasker_screenoff);

        mPreferenceHelper = new PreferenceHelper(getActivity());
        mDebug = mPreferenceHelper.getBoolean(VC_EXTENSIVE_LOGGING);

        mScreenOff = (SwitchPreference) findPreference("tools_tasker_screenoff");
        mScreenOff.setChecked(mPreferenceHelper.getBoolean(TASKER_SCREENOFF));
        mScreenOff.setOnPreferenceChangeListener(this);

        mInternetToggle = (SwitchPreference) findPreference("tools_tasker_screenoff_internet");
        mInternetToggle.setChecked(mPreferenceHelper.getBoolean(TASKER_SCREENOFF_INTERNET));
        mInternetToggle.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean mChanged = false;

        if (preference == mScreenOff) {
            mPreferenceHelper.setBoolean(TASKER_SCREENOFF, (boolean) newValue);
            mChanged = true;
        } else if (preference == mInternetToggle) {
            mPreferenceHelper.setBoolean(TASKER_SCREENOFF_INTERNET, (boolean) newValue);
            mChanged = true;
        }

        try {
            logDebug("Starting Service!");
            getActivity().startService(new Intent(getActivity(), TaskerService.class));
        } catch (Exception exc) {
            logDebug("Error: " + exc.getMessage());
        }


        return mChanged;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return (super.onPreferenceTreeClick(preferenceScreen, preference));
    }

    private void logDebug(String msg) {
        VeloxMethods.logDebug(msg, mDebug);
    }
}
