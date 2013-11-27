package net.openfiretechnologies.veloxcontrol.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import eu.chainfire.libsuperuser.Shell;

import static java.util.concurrent.TimeUnit.MINUTES;


/**
 * Created by alex on 11.11.13.
 */
public class TaskerService extends Service implements VeloxConstants {

    private Context mContext;
    private boolean mDebug = false;
    private PreferenceHelper mPreferenceHelper;
    private boolean mRegisteredBroadcast = false;
    private boolean mShouldRun = false;

    public boolean mWasScreenOn = true;

    //================
    // Tasker Fields
    //================
    private boolean mScreenOff = false;
    private boolean mFstrim = false;
    // Scheduler
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    ScheduledFuture fstrimHandle;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {

        mContext = this;
        mPreferenceHelper = new PreferenceHelper(mContext);
        mDebug = mPreferenceHelper.getBoolean(VC_EXTENSIVE_LOGGING);
        logDebug("Service Started!");

        update();
        registerBroadcastReceiver();

        if (mFstrim) {
            scheduleFstrim();
        }

        if (!mShouldRun) {
            logDebug("Service is useless, killing it.");
            stopSelf();
        }
        return START_STICKY;
    }

    private void update() {
        logDebug("Service - Update Called!");
        mScreenOff = mPreferenceHelper.getBoolean(TASKER_SCREENOFF);
        logDebug("mScreenOff: " + (mScreenOff ? "true" : "false"));
        mFstrim = mPreferenceHelper.getBoolean(TASKER_TOOLS_FSTRIM);
        logDebug("mFstrim: " + (mFstrim ? "true" : "false"));

        // Set Flag to kill service if no tasker action is used
        mShouldRun = mScreenOff || mFstrim;
        logDebug("mShouldRun: " + (mShouldRun ? "true" : "false"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRegisteredBroadcast) {
            unregisterBroadcastReceiver();
        }
        logDebug("Service - Unregistered Broadcasts");
    }

    private void registerBroadcastReceiver() {
        if (mShouldRun) {
            if (mRegisteredBroadcast) {
                try {
                    mContext.unregisterReceiver(mBroadcastReceiver);
                } catch (Exception exc) {
                    logDebug("Error unregistering Receivers: " + exc.getLocalizedMessage());
                }
            }
            IntentFilter filter = new IntentFilter();
            if (mScreenOff) {
                logDebug("Service - Registering Screen Off");
                filter.addAction(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_SCREEN_OFF);
            }
            mContext.registerReceiver(mBroadcastReceiver, filter);
            mRegisteredBroadcast = true;
            logDebug("Service - Registered Broadcasts");
        }
        logDebug("Service - Not Registered Any Broadcast");
    }

    private void unregisterBroadcastReceiver() {
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    private void onScreenTurnedOn() {
        if (!mScreenOff) return;
        if (mPreferenceHelper.getBoolean(TASKER_SCREENOFF_INTERNET)) {
            setMobileDataEnabled(mContext, true);
        }
    }

    private void onScreenTurnedOff() {
        if (!mScreenOff) return;
        if (mPreferenceHelper.getBoolean(TASKER_SCREENOFF_INTERNET)) {
            setMobileDataEnabled(mContext, false);
        }
    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (Exception exc) {
            logDebug("Service - Error: " + exc.getMessage());
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                onScreenTurnedOff();
                mWasScreenOn = false;
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                onScreenTurnedOn();
                mWasScreenOn = true;
            }
        }
    };

    private void logDebug(String msg) {
        VeloxMethods.logDebug(msg, mDebug);
    }

    //================
    // Runnable
    //================
    final Runnable mFstrimRunnable = new Runnable() {
        public void run() {
            logDebug("FSTRIM RUNNING");
            FileOutputStream fos = null;
            try {
                List<String> mCommands = new ArrayList<>();
                mCommands.add("date");
                mCommands.add("busybox fstrim -v /system");
                mCommands.add("busybox fstrim -v /data");
                mCommands.add("busybox fstrim -v /cache");
                List<String> mResults = Shell.SU.run(mCommands);
                fos = new FileOutputStream(VeloxFiles.VELOX_LOG_FILE_FSSTRIM);
                for (String s : mResults) {
                    logDebug("Result: " + s);
                    fos.write((s + "\n").getBytes());
                }
                fos.write("\n\n".getBytes());
            } catch (Exception exc) {
                logDebug("Fstrim error: " + exc.getLocalizedMessage());
            } finally {
                try {
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                    }
                } catch (Exception exc) {
                    logDebug("Fstrim error: " + exc.getLocalizedMessage());
                }
            }
            logDebug("FSTRIM RAN");
        }
    };

    private void scheduleFstrim() {
        if (fstrimHandle != null) {
            fstrimHandle.cancel(true);
        }
        long period = Integer.parseInt(mPreferenceHelper.getString(TASKER_TOOLS_FSTRIM_INTERVAL, "30"));
        fstrimHandle = scheduler.scheduleAtFixedRate(mFstrimRunnable, 1, period, MINUTES);
        logDebug("Fstrim scheduled every " + period + " Minutes.");
    }

}
