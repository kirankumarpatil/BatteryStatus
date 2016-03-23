package com.nttdata.batterystatus.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;

import com.nttdata.batterystatus.PowerConnectionReceiver;

/**
 * Created by kirankumar on 22/03/16.
 */
public class BatteryStatusService extends IntentService {


    public BatteryStatusService() {
        super(BatteryStatusService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.registerReceiver(new PowerConnectionReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

}
