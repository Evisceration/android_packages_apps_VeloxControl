package net.openfiretechnologies.veloxcontrol.fragments.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.util.FileChooser;

/**
 * Created by alex on 05.11.13.
 */
public class ToolFont extends PreferenceFragment {

    Preference mRobotoRegular;
    Preference mRobotoBold;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_tools_fonts);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();

        mRobotoRegular = prefSet.findPreference("alex_tools_fonts_robotoregular");
        mRobotoBold = prefSet.findPreference("alex_tools_fonts_robotobold");

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        boolean setMod = false;
        Intent i = new Intent(mContext, FileChooser.class);

        if (preference == mRobotoRegular) {
            i.putExtra("mod", "robotoregular");
            setMod = true;
        } else if (preference == mRobotoBold) {
            i.putExtra("mod", "robotobold");
            setMod = true;
        }

        if (setMod) {
            startActivity(i);
        }

        return (setMod || super.onPreferenceTreeClick(preferenceScreen, preference));
    }
}
