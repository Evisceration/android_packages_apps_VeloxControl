package net.openfiretechnologies.veloxcontrol.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.openfiretechnologies.veloxcontrol.services.TaskerService;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

import eu.chainfire.libsuperuser.Shell;

public class BootReceiver extends BroadcastReceiver implements VeloxConstants {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get PreferenceHelper
        PreferenceHelper mPrefs = new PreferenceHelper(context);
        // Set SELinux Permissive
        if (mPrefs.getBoolean(VC_OS_DISABLE_SELINUX)) {
            Shell.SU.run("setenforce 0");
            VeloxMethods.logDebug("setenforce 0", true);
        }

        // Start Tasker Service
        Intent service = new Intent(context, TaskerService.class);
        context.startService(service);
    }
}
