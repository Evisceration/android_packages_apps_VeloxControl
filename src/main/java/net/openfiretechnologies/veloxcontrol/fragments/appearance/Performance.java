package net.openfiretechnologies.veloxcontrol.fragments.appearance;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.util.SystemProperties;

/**
 * Created by alex on 06.11.13.
 */
public class Performance extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    // ====================
    // Keys
    // ====================
    private static final String USE_16BPP_ALPHA_PREF = "pref_use_16bpp_alpha";
    private static final String USE_16BPP_ALPHA_PROP = "persist.sys.use_16bpp_alpha";

    // ====================
    // Elements
    // ====================
    private CheckBoxPreference mUse16bppAlphaPref;

    // ====================
    // Fields
    // ====================
    //private ContentResolver resolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_appearance_performance);
        PreferenceScreen prefSet = getPreferenceScreen();

        mUse16bppAlphaPref = (CheckBoxPreference) prefSet.findPreference(USE_16BPP_ALPHA_PREF);
        String use16bppAlpha = SystemProperties.get(USE_16BPP_ALPHA_PROP, "0");
        mUse16bppAlphaPref.setChecked("1".equals(use16bppAlpha));

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mUse16bppAlphaPref) {
            android.os.SystemProperties.set(USE_16BPP_ALPHA_PROP,
                    mUse16bppAlphaPref.isChecked() ? "1" : "0");
            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}
