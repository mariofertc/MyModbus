package com.ambatosystem.mymodbus;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * A class that specifies which of the shared preferences you want to backup
 * to the Google Backup Service.
 */
public class MMBackupAgent extends BackupAgentHelper {
    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, MMConstants.SHARED_PREFERENCE_FILE);
        //addHelper(MMConstants.SP_KEY_FOLLOW_LOCATION_CHANGES, helper);
        // TODO Add additional helpers for each of the preferences you want to backup.
    }
}