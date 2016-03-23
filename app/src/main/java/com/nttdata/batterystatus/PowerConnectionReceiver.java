package com.nttdata.batterystatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

import com.firebase.client.Firebase;

import static android.os.Build.SERIAL;

/**
 * Created by kirankumar on 11/03/16.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    private Firebase mFirebaseRef;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        String deviceCode = (SERIAL == null || SERIAL.length() == 0) ? telephonyManager.getDeviceId() : SERIAL;
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        int icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
        int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int level = -1;
        if (currentLevel >= 0 && scale > 0) {
            level = (currentLevel * 100) / scale;
        }
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
        Boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL).child(deviceCode).child("batteryStatus");
        mFirebaseRef.child("isCharging").setValue(isCharging);
        mFirebaseRef.child("level").setValue(level);
    }

}
