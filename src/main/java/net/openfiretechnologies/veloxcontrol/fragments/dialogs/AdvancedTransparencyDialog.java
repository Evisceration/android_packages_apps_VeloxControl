package net.openfiretechnologies.veloxcontrol.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.widgets.AlphaSeekBar;

/**
 * Created by alex on 06.11.13.
 */
public class AdvancedTransparencyDialog extends DialogFragment {
    private static final int KEYGUARD_ALPHA = 112;
    private static final int LOCKSCREEN_ALPHA = 0;

    AlphaSeekBar mSeekBars[] = new AlphaSeekBar[1];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowsDialog(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View layout = View.inflate(getActivity(), R.layout.dialog_transparency, null);
        mSeekBars[LOCKSCREEN_ALPHA] = (AlphaSeekBar) layout.findViewById(R.id.lockscreen_alpha);

        final ContentResolver resolver = getActivity().getContentResolver();

        try {
            // restore any saved settings
            int lockscreen_alpha = Settings.System.getInt(resolver,
                    Settings.System.LOCKSCREEN_ALPHA_CONFIG, KEYGUARD_ALPHA);
            mSeekBars[LOCKSCREEN_ALPHA].setCurrentAlpha(lockscreen_alpha);
        } catch (Exception e) {
            resetSettings();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setTitle(getString(R.string.transparency_dialog_title));
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Settings.System.putInt(resolver,
                        Settings.System.LOCKSCREEN_ALPHA_CONFIG,
                        mSeekBars[LOCKSCREEN_ALPHA].getCurrentAlpha());
            }
        });
        return builder.create();
    }

    private void resetSettings() {
        Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.LOCKSCREEN_ALPHA_CONFIG, KEYGUARD_ALPHA);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}
