package com.phonegap.geofencing;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.location.LocationManager;

import android.content.SharedPreferences;


public class ProximityReceiver extends BroadcastReceiver {
  public static final String TAG = "Geofencing"; 


  @Override
  public void onReceive(Context context, Intent intent) {
    String id = (String) intent.getExtras().get("id");
    Log.d(TAG, "received proximity alert for region " + id);

    Log.d(TAG, "DGGeofencing.getInstance(): "+ DGGeofencing.getInstance());

    if(DGGeofencing.getInstance() == null) {
    	//If our plugin has been terminated by the OS, just send directly to backend.
		String status = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false) ? "enter" : "exit";
		String regionId = (String) intent.getExtras().get("id");
		
		SharedPreferences settings = context.getSharedPreferences(TAG, 0);

    	RegionPostToServer.scheduleRegionChange(settings, regionId, status);
    }
    else
    	DGGeofencing.getInstance().fireRegionChangedEvent(intent);
  }


}
