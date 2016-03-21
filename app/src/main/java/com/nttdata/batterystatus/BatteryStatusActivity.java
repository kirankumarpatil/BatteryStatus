package com.nttdata.batterystatus;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import static android.os.Build.*;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.nttdata.batterystatus.service.LocationTransitionsIntentService;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class BatteryStatusActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status> {


    private Firebase mFirebaseRef;

    protected static final String TAG = "BatteryStatusActivity";
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;
    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private HashMap<String, List<String>> expandableListData = new HashMap<String, List<String>>();
    private List<String> deviceInfoData  = new ArrayList<String>();
    private List<String> batteryStatusData  = new ArrayList<>();
    private List<String> deviceLocationData  = new ArrayList<>();

    private String deviceCode;
    private ArrayList<String> expandableTitleList = new ArrayList<>();

    private static final String FIREBASE_URL = "https://myjarvis.firebaseio.com/assets";

    private double latitude;

    private double longitude;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * The list of geofences used in this sample.
     */
    protected ArrayList<Geofence> mGeofenceList;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;

    // Buttons for kicking off the process of adding or removing geofences.
    private Button mAddGeofencesButton;
    private Button mRemoveGeofencesButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_battery_status);

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        // Locate the UI widgets.
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        // Get the UI widgets.
        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        mRemoveGeofencesButton = (Button) findViewById(R.id.remove_geofences_button);
        mGeofencePendingIntent = null;
        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
        setButtonsEnabledState();

        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        deviceCode = (SERIAL == null || SERIAL.length()==0)?telephonyManager.getDeviceId():SERIAL;
        this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        mGeofenceList = new ArrayList<>();
        buildGoogleApiClient();
        populateGeofenceList();
        setExpandableTitleList(expandableTitleList);
        deviceInfoData.add("Model: " + MODEL);
        deviceInfoData.add("MANUFACTURER: " + MANUFACTURER);
        deviceInfoData.add("Brand" + BRAND);


        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> adresses = geocoder.getFromLocationName("ls101lj",1);
            deviceLocationData.add("Latitude: "+adresses.get(0).getLatitude());
            deviceLocationData.add("Logitude: "+adresses.get(0).getLongitude());
            longitude = adresses.get(0).getLongitude();
            latitude = adresses.get(0).getLatitude();
            deviceLocationData.add(adresses.get(0).getAddressLine(0)+","+adresses.get(0).getAddressLine(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateExpandableViewData(expandableListData);
        expandableListAdapter = new ExpandableListAdapter(this, expandableTitleList, expandableListData);
        expandableListView.setAdapter(expandableListAdapter);

    }


    /**
     * Set Expandable View Titles
     * @param expandableTitleList
     */
    private void setExpandableTitleList(ArrayList<String> expandableTitleList) {
        expandableTitleList.add("Device Information");
        expandableTitleList.add("Battery Status");
        expandableTitleList.add("Device Location");

    }


    private void updateExpandableViewData(HashMap<String, List<String>> expandableViewData) {
        expandableViewData.put("Device Information", deviceInfoData);
        expandableViewData.put("Battery Status", batteryStatusData);
        expandableViewData.put("Device Location", deviceLocationData);
    }


    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("tttttt",deviceCode);
            /*batteryInfo.setText(

                   ""
            );*/
            if (null != deviceCode && !deviceCode.equals("")) {
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
                mFirebaseRef = new Firebase(FIREBASE_URL).child(deviceCode).child("batteryStatus");
                mFirebaseRef.child("isCharging").setValue(isCharging);
                mFirebaseRef.child("level").setValue(level);

                String sourceofPower = "";
                switch (plugged) {
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        sourceofPower = "USB";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        sourceofPower = "AC Charger";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        sourceofPower = "Wireless";
                        break;
                    default:
                        sourceofPower = "Not Charging";
                }

                batteryStatusData.clear();
                batteryStatusData.add( "Level: " + level + "%");
                batteryStatusData.add( "Is Charging: " + isCharging );
                batteryStatusData.add("Device is connected: " + sourceofPower);

                expandableListAdapter.notifyDataSetChanged();
                expandableListAdapter = new ExpandableListAdapter(context, expandableTitleList, expandableListData);
                expandableListView.setAdapter(expandableListAdapter);

            }
            }

    };

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            //logSecurityException(securityException);
        }
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            /*String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());*/
            Log.e(TAG, status.getStatusCode()+"");
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, LocationTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            latitude,
                            longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    .build());
        }
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }


    private void setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mAddGeofencesButton.setEnabled(false);
            mRemoveGeofencesButton.setEnabled(true);
        } else {
            mAddGeofencesButton.setEnabled(true);
            mRemoveGeofencesButton.setEnabled(false);
        }
    }

}
