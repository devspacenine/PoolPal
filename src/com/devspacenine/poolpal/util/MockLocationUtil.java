package com.devspacenine.poolpal.util;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;

public class MockLocationUtil {

	public static final String TAG = "DSN Debug";
	public static final String PROVIDER_NAME = "testProvider";

	public static void publishMockLocation(double latitude, double longitude, Context ctx,
			LocationListener listener) {
		LocationManager manager = (LocationManager) ctx.getSystemService(Service.LOCATION_SERVICE);
		publishMockLocation(latitude, longitude, ctx, manager, listener, PROVIDER_NAME);
	}

	public static void publishMockLocation(double latitude, double longitude, Context ctx,
			LocationManager manager, LocationListener listener, String provider) {
	    Location newLocation = new Location(provider);

	    newLocation.setLatitude(latitude);
	    newLocation.setLongitude(longitude);
	    newLocation.setTime(System.currentTimeMillis());
	    newLocation.setAccuracy(25);

	    manager.requestLocationUpdates(provider, 0, 0, listener);

	    manager.setTestProviderLocation(provider, newLocation);
	    Log.w(TAG, "published location: " + newLocation);

	    Log.w(TAG, "LastKnownLocation of "+provider+" is: "+manager.getLastKnownLocation(provider));
	}

	public static void createMockLocationProvider(LocationManager manager, String provider) {
		Log.w(TAG, "Providers: " + manager.getAllProviders().toString());
	    if(manager.getProvider(provider) != null) {
	      Log.w(TAG, "Removing provider " + provider);
	      manager.removeTestProvider(provider);
	    }
	    Log.w(TAG, "Providers: " + manager.getAllProviders().toString());

	    if(manager.getProvider(provider) == null) {
	      Log.w(TAG, "Adding provider " + provider + " again");
	      manager.addTestProvider(provider, "requiresNetwork" == "", "requiresSatellite" == "",
	          "requiresCell" == "", "hasMonetaryCost" == "", "supportsAltitude" == "", "supportsSpeed" == "",
	          "supportsBearing" == "", android.location.Criteria.POWER_LOW, android.location.Criteria.ACCURACY_FINE);
	    }
	    Log.w(TAG, "Providers: " + manager.getAllProviders().toString());

	    manager.setTestProviderEnabled(provider, true);

	    manager.setTestProviderStatus(provider, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
	}

	public static Location getLastKnownLocationInApplication(Context ctx) {
		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		return getLastKnownLocationInApplication(ctx, lm, PROVIDER_NAME);
	}

	public static Location getLastKnownLocationInApplication(Context ctx, LocationManager manager, String provider) {
		Location testLoc = null;

		if(manager.getAllProviders().contains(provider)) {
		      testLoc = manager.getLastKnownLocation(provider);
		      Log.d(TAG, "TestLocation: " + testLoc);
		}

		if(testLoc != null) {
		  return testLoc;
		} else {
		  return manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
	}
}