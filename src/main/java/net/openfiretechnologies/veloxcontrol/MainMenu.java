package net.openfiretechnologies.veloxcontrol;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.openfiretechnologies.veloxcontrol.fragments.About;
import net.openfiretechnologies.veloxcontrol.fragments.ActiveDisplaySettings;
import net.openfiretechnologies.veloxcontrol.fragments.Appearance;
import net.openfiretechnologies.veloxcontrol.fragments.ExtraSettings;
import net.openfiretechnologies.veloxcontrol.fragments.Halo;
import net.openfiretechnologies.veloxcontrol.fragments.Tasker;
import net.openfiretechnologies.veloxcontrol.fragments.Tools;
import net.openfiretechnologies.veloxcontrol.fragments.WelcomeFragment;
import net.openfiretechnologies.veloxcontrol.fragments.subfragments.NavigationDrawerFragment;
import net.openfiretechnologies.veloxcontrol.services.TaskerService;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

public class MainMenu extends FragmentActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks, VeloxConstants {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private FragmentManager fragmentManager;
    private Context mContext;
    CharSequence mTitle;
    PreferenceHelper prefs;
    private Toast mToast;
    private boolean mDebug = false;
    private boolean mDoublePress = true;
    private static long back_pressed;
    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        mContext = getApplicationContext();
        prefs = new PreferenceHelper(mContext);
        mDebug = prefs.getBoolean(VC_EXTENSIVE_LOGGING);
        mDoublePress = prefs.getBoolean(VC_DOUBLE_PRESS_END);
        logDebug("Extensive Logging Enabled!");
        boolean mIsVelox = VeloxMethods.isVelox(mDebug);
        logDebug("Is Velox: " + (mIsVelox ? "true" : "false"));
        logDebug("MODEL: " + Build.MODEL);

        if (!mIsVelox) {
            ComponentName component = new ComponentName("net.openfiretechnologies.veloxcontrol",
                    "net.openfiretechnologies.veloxcontrol.activities._DummyLauncher");
            PackageManager mPm = getPackageManager();
            if (mPm != null)
                mPm.setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
        }

        try {
            if (!VeloxMethods.checkDirectories()) {
                if (VeloxMethods.createDirectories(mDebug)) {
                    logDebug("Velox Directories Created!");
                } else {
                    logDebug("Could NOT create Velox Directories!");
                    MakeToast("Could not create Velox Directories!");
                }
            } else {
                logDebug("Velox Directories exist!");
            }
        } catch (Exception exc) {
            logDebug("Error at creating / checking directories: " + exc.getLocalizedMessage());
        }

        logDebug("Starting Service");
        startService(new Intent(this, TaskerService.class));

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        fragmentManager.beginTransaction().replace(R.id.content_frame, new WelcomeFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void selectItem(int position) {
        Fragment fragment;
        switch (mNavigationDrawerFragment.mMainDrawerAdapter.getItem(position).title) {
            case R.string.app_name:
                fragment = new WelcomeFragment();
                break;
            case R.string.active_display:
                fragment = new ActiveDisplaySettings();
                break;
            case R.string.halo:
                fragment = new Halo();
                break;
            case R.string.general_appearance:
                fragment = new Appearance();
                break;
            case R.string.tools_tasker_title:
                fragment = new Tasker();
                break;
            case R.string.general_tools:
                fragment = new Tools();
                break;
            case R.string.general_extras:
                fragment = new ExtraSettings();
                break;
            case R.string.general_about:
                fragment = new About();
                break;
            default:
                return;
        }
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * Override backbutton presses to exit
     */
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else if (mDoublePress &&
                !mNavigationDrawerFragment.isDrawerOpen()) {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                if (mToast != null)
                    mToast.cancel();
                finish();
            } else {
                mToast = Toast.makeText(getBaseContext(), getString(R.string.action_press_again), Toast.LENGTH_SHORT);
                mToast.show();
            }
            back_pressed = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void logDebug(String msg) {
        VeloxMethods.logDebug(msg, mDebug);
    }

    private void MakeToast(String msg) {
        if (t != null)
            t.cancel();
        t = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    // Methods

    /**
     * Restores the action bar after closing the drawer
     */
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}
