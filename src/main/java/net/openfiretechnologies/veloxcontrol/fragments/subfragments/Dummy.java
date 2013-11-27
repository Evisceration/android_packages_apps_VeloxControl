package net.openfiretechnologies.veloxcontrol.fragments.subfragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.openfiretechnologies.veloxcontrol.R;

/**
 * Created by alex on 06.11.13.
 */
public class Dummy extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_dummy);

    }

}
