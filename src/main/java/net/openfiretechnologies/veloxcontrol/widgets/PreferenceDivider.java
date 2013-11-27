package net.openfiretechnologies.veloxcontrol.widgets;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import net.openfiretechnologies.veloxcontrol.R;

/**
 * Created by alex on 07.11.13.
 */
public class PreferenceDivider extends Preference {
    public PreferenceDivider(Context context) {
        super(context);
    }

    public PreferenceDivider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PreferenceDivider(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        view.setBackgroundColor(getContext().getResources().getColor(R.color.red_dark));
        view.setClickable(false);
        view.setFocusable(false);
    }

    @Override
    public View getView(View view1, ViewGroup viewGroup) {
        View view = super.getView(view1, viewGroup);
        final int height = 10;
        final int width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(params);
        return view;
    }
}
