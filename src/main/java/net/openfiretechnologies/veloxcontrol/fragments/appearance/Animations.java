package net.openfiretechnologies.veloxcontrol.fragments.appearance;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

import com.android.internal.util.velox.AwesomeAnimationHelper;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.widgets.AnimBarPreference;

/**
 * Created by alex on 06.11.13.
 */
public class Animations extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    // ====================
    // Keys
    // ====================
    /* System Animations */
    private static final String ACTIVITY_OPEN = "activity_open";
    private static final String ACTIVITY_CLOSE = "activity_close";
    private static final String TASK_OPEN = "task_open";
    private static final String TASK_CLOSE = "task_close";
    private static final String TASK_MOVE_TO_FRONT = "task_move_to_front";
    private static final String TASK_MOVE_TO_BACK = "task_move_to_back";
    private static final String ANIMATION_DURATION = "animation_duration";
    private static final String ANIMATION_NO_OVERRIDE = "animation_no_override";
    private static final String WALLPAPER_OPEN = "wallpaper_open";
    private static final String WALLPAPER_CLOSE = "wallpaper_close";
    private static final String WALLPAPER_INTRA_OPEN = "wallpaper_intra_open";
    private static final String WALLPAPER_INTRA_CLOSE = "wallpaper_intra_close";
    /* ListView Animations */
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";

    // ====================
    // Elements
    // ====================
    /* System Animations */
    private ListPreference mActivityOpenPref;
    private ListPreference mActivityClosePref;
    private ListPreference mTaskOpenPref;
    private ListPreference mTaskClosePref;
    private ListPreference mTaskMoveToFrontPref;
    private ListPreference mTaskMoveToBackPref;
    private ListPreference mWallpaperOpen;
    private ListPreference mWallpaperClose;
    private ListPreference mWallpaperIntraOpen;
    private ListPreference mWallpaperIntraClose;
    private AnimBarPreference mAnimationDuration;
    private CheckBoxPreference mAnimNoOverride;
    /* ListView Animations */
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;

    // ====================
    // Fields
    // ====================
    private int[] mAnimations;
    private String[] mAnimationsStrings;
    private String[] mAnimationsNum;

    private ContentResolver resolver;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        addPreferencesFromResource(R.xml.alex_appearance_animations);
        PreferenceScreen prefSet = getPreferenceScreen();
        resolver = getActivity().getContentResolver();

        /* System Animations */
        mAnimations = AwesomeAnimationHelper.getAnimationsList();
        int animqty = mAnimations.length;
        mAnimationsStrings = new String[animqty];
        mAnimationsNum = new String[animqty];
        for (int i = 0; i < animqty; i++) {
            mAnimationsStrings[i] = AwesomeAnimationHelper.getProperName(
                    getActivity(), mAnimations[i]);
            mAnimationsNum[i] = String.valueOf(mAnimations[i]);
        }

        mAnimNoOverride = (CheckBoxPreference) prefSet.findPreference(ANIMATION_NO_OVERRIDE);
        mAnimNoOverride.setChecked((Settings.System.getInt(resolver,
                Settings.System.ANIMATION_CONTROLS_NO_OVERRIDE, 0) == 1));

        mActivityOpenPref = (ListPreference) prefSet.findPreference(ACTIVITY_OPEN);
        mActivityOpenPref.setOnPreferenceChangeListener(this);
        mActivityOpenPref.setSummary(getProperSummary(mActivityOpenPref));
        mActivityOpenPref.setEntries(mAnimationsStrings);
        mActivityOpenPref.setEntryValues(mAnimationsNum);

        mActivityClosePref = (ListPreference) prefSet.findPreference(ACTIVITY_CLOSE);
        mActivityClosePref.setOnPreferenceChangeListener(this);
        mActivityClosePref.setSummary(getProperSummary(mActivityClosePref));
        mActivityClosePref.setEntries(mAnimationsStrings);
        mActivityClosePref.setEntryValues(mAnimationsNum);

        mTaskOpenPref = (ListPreference) prefSet.findPreference(TASK_OPEN);
        mTaskOpenPref.setOnPreferenceChangeListener(this);
        mTaskOpenPref.setSummary(getProperSummary(mTaskOpenPref));
        mTaskOpenPref.setEntries(mAnimationsStrings);
        mTaskOpenPref.setEntryValues(mAnimationsNum);

        mTaskClosePref = (ListPreference) prefSet.findPreference(TASK_CLOSE);
        mTaskClosePref.setOnPreferenceChangeListener(this);
        mTaskClosePref.setSummary(getProperSummary(mTaskClosePref));
        mTaskClosePref.setEntries(mAnimationsStrings);
        mTaskClosePref.setEntryValues(mAnimationsNum);

        mTaskMoveToFrontPref = (ListPreference) prefSet.findPreference(TASK_MOVE_TO_FRONT);
        mTaskMoveToFrontPref.setOnPreferenceChangeListener(this);
        mTaskMoveToFrontPref.setSummary(getProperSummary(mTaskMoveToFrontPref));
        mTaskMoveToFrontPref.setEntries(mAnimationsStrings);
        mTaskMoveToFrontPref.setEntryValues(mAnimationsNum);

        mTaskMoveToBackPref = (ListPreference) prefSet.findPreference(TASK_MOVE_TO_BACK);
        mTaskMoveToBackPref.setOnPreferenceChangeListener(this);
        mTaskMoveToBackPref.setSummary(getProperSummary(mTaskMoveToBackPref));
        mTaskMoveToBackPref.setEntries(mAnimationsStrings);
        mTaskMoveToBackPref.setEntryValues(mAnimationsNum);

        mWallpaperOpen = (ListPreference) prefSet.findPreference(WALLPAPER_OPEN);
        mWallpaperOpen.setOnPreferenceChangeListener(this);
        mWallpaperOpen.setSummary(getProperSummary(mWallpaperOpen));
        mWallpaperOpen.setEntries(mAnimationsStrings);
        mWallpaperOpen.setEntryValues(mAnimationsNum);

        mWallpaperClose = (ListPreference) prefSet.findPreference(WALLPAPER_CLOSE);
        mWallpaperClose.setOnPreferenceChangeListener(this);
        mWallpaperClose.setSummary(getProperSummary(mWallpaperClose));
        mWallpaperClose.setEntries(mAnimationsStrings);
        mWallpaperClose.setEntryValues(mAnimationsNum);

        mWallpaperIntraOpen = (ListPreference) prefSet.findPreference(WALLPAPER_INTRA_OPEN);
        mWallpaperIntraOpen.setOnPreferenceChangeListener(this);
        mWallpaperIntraOpen.setSummary(getProperSummary(mWallpaperIntraOpen));
        mWallpaperIntraOpen.setEntries(mAnimationsStrings);
        mWallpaperIntraOpen.setEntryValues(mAnimationsNum);

        mWallpaperIntraClose = (ListPreference) prefSet.findPreference(WALLPAPER_INTRA_CLOSE);
        mWallpaperIntraClose.setOnPreferenceChangeListener(this);
        mWallpaperIntraClose.setSummary(getProperSummary(mWallpaperIntraClose));
        mWallpaperIntraClose.setEntries(mAnimationsStrings);
        mWallpaperIntraClose.setEntryValues(mAnimationsNum);

        int defaultDuration = Settings.System.getInt(resolver,
                Settings.System.ANIMATION_CONTROLS_DURATION, 0);
        mAnimationDuration = (AnimBarPreference) prefSet.findPreference(ANIMATION_DURATION);
        mAnimationDuration.setInitValue(defaultDuration);
        mAnimationDuration.setOnPreferenceChangeListener(this);

        /* ListView Animations */
        mListViewAnimation = (ListPreference) findPreference(KEY_LISTVIEW_ANIMATION);
        int listviewanimation = Settings.System.getInt(resolver,
                Settings.System.LISTVIEW_ANIMATION, 0);
        mListViewAnimation.setValue(String.valueOf(listviewanimation));
        mListViewAnimation.setSummary(mListViewAnimation.getEntry());
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) findPreference(KEY_LISTVIEW_INTERPOLATOR);
        int listviewinterpolator = Settings.System.getInt(resolver,
                Settings.System.LISTVIEW_INTERPOLATOR, 0);
        mListViewInterpolator.setValue(String.valueOf(listviewinterpolator));
        mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
        mListViewInterpolator.setOnPreferenceChangeListener(this);
        mListViewInterpolator.setEnabled(listviewanimation > 0);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        boolean result = false;

        if (preference == mActivityOpenPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[0], val);
        } else if (preference == mActivityClosePref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[1], val);
        } else if (preference == mTaskOpenPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[2], val);
        } else if (preference == mTaskClosePref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[3], val);
        } else if (preference == mTaskMoveToFrontPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[4], val);
        } else if (preference == mTaskMoveToBackPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[5], val);
        } else if (preference == mWallpaperOpen) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[6], val);
        } else if (preference == mWallpaperClose) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[7], val);
        } else if (preference == mWallpaperIntraOpen) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[8], val);
        } else if (preference == mWallpaperIntraClose) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.System.putInt(resolver,
                    Settings.System.ACTIVITY_ANIMATION_CONTROLS[9], val);
        } else if (preference == mAnimationDuration) {
            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_CONTROLS_DURATION,
                    val);
        } else if (preference == mListViewAnimation) {
            int listviewanimation = Integer.valueOf((String) newValue);
            int index = mListViewAnimation.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.LISTVIEW_ANIMATION,
                    listviewanimation);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
            mListViewInterpolator.setEnabled(listviewanimation > 0);
            return true;
        } else if (preference == mListViewInterpolator) {
            int listviewinterpolator = Integer.valueOf((String) newValue);
            int index = mListViewInterpolator.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    Settings.System.LISTVIEW_INTERPOLATOR,
                    listviewinterpolator);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);
            return true;
        }

        preference.setSummary(getProperSummary(preference));
        return result;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mAnimNoOverride) {
            Settings.System.putInt(resolver,
                    Settings.System.ANIMATION_CONTROLS_NO_OVERRIDE,
                    (mAnimNoOverride.isChecked() ? 1 : 0));
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private String getProperSummary(Preference preference) {
        String mString = "";

        if (preference == mActivityOpenPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[0];
        } else if (preference == mActivityClosePref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[1];
        } else if (preference == mTaskOpenPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[2];
        } else if (preference == mTaskClosePref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[3];
        } else if (preference == mTaskMoveToFrontPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[4];
        } else if (preference == mTaskMoveToBackPref) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[5];
        } else if (preference == mWallpaperOpen) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[6];
        } else if (preference == mWallpaperClose) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[7];
        } else if (preference == mWallpaperIntraOpen) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[8];
        } else if (preference == mWallpaperIntraClose) {
            mString = Settings.System.ACTIVITY_ANIMATION_CONTROLS[9];
        }

        int mNum = Settings.System.getInt(resolver, mString, 0);
        return mAnimationsStrings[mNum];
    }
}
