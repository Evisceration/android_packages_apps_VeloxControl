package net.openfiretechnologies.veloxcontrol.fragments.tools;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

import net.openfiretechnologies.veloxcontrol.R;
import net.openfiretechnologies.veloxcontrol.util.BackupTool;
import net.openfiretechnologies.veloxcontrol.util.PreferenceHelper;
import net.openfiretechnologies.veloxcontrol.util.VeloxConstants;
import net.openfiretechnologies.veloxcontrol.util.VeloxMethods;

import java.util.Set;

/**
 * Created by alex on 05.11.13.
 */
public class ToolBackup extends PreferenceFragment implements VeloxConstants, Preference.OnPreferenceChangeListener {

    ListPreference mRestore;
    SwitchPreference mAutoBackup;
    MultiSelectListPreference mAutoBackupList;
    Context mContext;
    PreferenceHelper prefs;
    private boolean mDebug = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.alex_tools_backup);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();
        prefs = new PreferenceHelper(mContext);
        mDebug = prefs.getBoolean(VC_EXTENSIVE_LOGGING);

        mAutoBackup = (SwitchPreference) prefSet.findPreference("alex_tools_backup_autobackup");
        mAutoBackup.setChecked(prefs.getBoolean(PREF_AUTOBACKUP_ENABLED));
        mAutoBackup.setOnPreferenceChangeListener(this);

        mAutoBackupList = (MultiSelectListPreference) prefSet.findPreference("alex_tools_backup_autobackup_list");
        mAutoBackupList.setOnPreferenceChangeListener(this);

        mRestore = (ListPreference) prefSet.findPreference("alex_tools_backup_restore");
        mRestore.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        boolean success = false;

        if (preference == mAutoBackup) {
            prefs.setBoolean(PREF_AUTOBACKUP_ENABLED, (Boolean) newValue);
            logDebug("mAutoBackup: " + ((Boolean) newValue ? "true" : "false"));
            return true;
        } else if (preference == mAutoBackupList) {
            StringBuilder sb = new StringBuilder();
            String delimiter = "";
            for (String value : (Set<String>) newValue) {
                sb.append(delimiter);
                sb.append(value);
                delimiter = "|";
            }
            prefs.setString(PREF_AUTOBACKUP_LIST, sb.toString());
            logDebug(sb.toString());
            return true;
        } else if (preference == mRestore) {
            switch ((String) newValue) {
                case "0":
                    logDebug("Restoring: All");
                    success = BackupTool.getInstance(mContext).restore(BackupTool.BACKUP_ALL);
                    break;
                case "1":
                    logDebug("Restoring: Active Display");
                    success = BackupTool.getInstance(mContext).restore(BackupTool.BACKUP_ADVANCED_DISPLAY);
                    break;
                case "2":
                    logDebug("Restoring: Halo");
                    success = BackupTool.getInstance(mContext).restore(BackupTool.BACKUP_HALO);
                    break;
                default:
                    return false;
            }
            logDebug("Restore: " + newValue.toString());
        }

        if (success) {
            Toast.makeText(mContext, "Successfully restored!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Could not restore backup!", Toast.LENGTH_SHORT).show();
        }

        return success;
    }

    private void logDebug(String msg) {
        VeloxMethods.logDebug(msg, mDebug);
    }
}
