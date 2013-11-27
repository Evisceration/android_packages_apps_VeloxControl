package net.openfiretechnologies.veloxcontrol.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.util.SystemProperties;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;

public class About extends PreferenceFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alex_about,
                container, false);

        Typeface light = Typeface.create("sans-serif-light", Typeface.NORMAL);

        TextView tvDeveloper = (TextView) view.findViewById(R.id.tvDeveloper);
        tvDeveloper.setTypeface(light);

        tvDeveloper.setText(tvDeveloper.getText() + formatBuildOnText());

        ImageView ivOFTLogo = (ImageView) view.findViewById(R.id.ivOFTLogo);
        ivOFTLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.openfire-security.net"));
                startActivity(i);
            }
        });

        return view;
    }

    private String formatBuildOnText() {
        StringBuilder mString = new StringBuilder();

        String mHost = SystemProperties.get("velox.extra.buildhost", "unknown").toLowerCase();
        if (!mHost.equals("-1")) {
            mString.append(getString(R.string.velox_about_build_on));
            if (mHost.contains("elementary".toLowerCase())) {
                mString.append("Elementary OS");
            } else {
                mString.append("Unknown Host");
            }
            mString.append("\n\n");
        }

        String mUser = SystemProperties.get("velox.extra.builduser", "unknown").toLowerCase();
        if (!mUser.equals("-1")) {
            mString.append(getString(R.string.velox_about_build_by));
            if (mUser.contains("alex".toLowerCase())) {
                mString.append("Alexander Martinz");
            } else {
                mString.append("Unknown User");
            }
            mString.append("\n\n");
        }

        String mVersion = SystemProperties.get("ro.velox.version");
        if (!mVersion.equals("-1")) {
            mString.append(getString(R.string.velox_about_build_number));
            mString.append(mVersion);
            mString.append("\n\n");
        }

        mString.append(getString(R.string.velox_about_apk_build_number));
        mString.append(VeloxConstants.VELOXCONTROL_BUILDNUMBER);
        mString.append("\n\n");

        return mString.toString();

    }
}
