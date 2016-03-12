package com.nttdata.batterystatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by kirankumar on 11/03/16.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve an instance of the SharedPreferences object.
        Intent i=new Intent(context,BatteryStatusActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
