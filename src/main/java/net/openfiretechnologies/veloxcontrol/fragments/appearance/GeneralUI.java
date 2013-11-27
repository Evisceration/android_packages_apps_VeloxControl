package net.openfiretechnologies.veloxcontrol.fragments.appearance;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;

/**
 * Created by alex on 06.11.13.
 */
public class GeneralUI extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_appearance_general_ui);

    }

}
