package free.com.mecca;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSManager extends Service implements LocationListener {
	
	private final Context context;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocationNow = false;
	
	Location location = null;
	double lat;
	double lng;
	
	private static final long minDistanceToChangeLocation = 10; //10 meters
	private static final long minTimeToChangeLoaction = 1; //1 milliseconds
	
	protected LocationManager locationManager;
	
	public GPSManager (Context context)
	{
		this.context = context;
		getLocation();
	}
	
	public Location getLocation()
	{
		try
		{
			locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if(!isGPSEnabled && !isNetworkEnabled)
			{
				//GPS And network is not enabled
				AlertDialog alert = new AlertDialog.Builder(GPSManager.this).create();
				alert.setTitle("Warning");
				alert.setMessage("Cannot provide your location at this time");
				alert.show();
			}
			else
			{
				this.canGetLocationNow = true;
				 if (isNetworkEnabled) {
	                    locationManager.requestLocationUpdates(
	                            LocationManager.NETWORK_PROVIDER,
	                            minTimeToChangeLoaction,
	                            minDistanceToChangeLocation, this);
	                    Log.d("Network", "Network Enabled");
	                    if (locationManager != null) {
	                        location = locationManager
	                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                        if (location != null) {
	                            lat= location.getLatitude();
	                            lng = location.getLongitude();
	                        }
	                    }
	                }
	                // if GPS Enabled get lat/long using GPS Services
	                if (isGPSEnabled) {
	                    if (location == null) {
	                        locationManager.requestLocationUpdates(
	                                LocationManager.GPS_PROVIDER,
	                                minTimeToChangeLoaction,
	                                minDistanceToChangeLocation, this);
	                        Log.d("GPS", "GPS Enabled");
	                        if (locationManager != null) {
	                            location = locationManager
	                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                            if (location != null) {
	                                lat = location.getLatitude();
	                                lng = location.getLongitude();
	                            }
	                        }
	                    }
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		return location;
	}
	
	 public void stopUsingGPS() {
	        if (locationManager != null) {
	            locationManager.removeUpdates(GPSManager.this);
	        }
	    }
	 public double getLatitude() {
	        if (location != null) {
	            lat = location.getLatitude();
	        }
	 
	        // return latitude
	        return lat;
	    }
	 public double getLongitude() {
	        if (location != null) {
	            lng = location.getLongitude();
	        }
	 
	        // return longitude
	        return lng;
	    }
	 

		 
	 
	 public void showSettingsAlert() {
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
	 
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");
	 
	        // Setting Dialog Message
	        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
	 
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        Intent intent = new Intent(
	                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                        context.startActivity(intent);
	                    }
	                });
	 
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel",
	                new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        dialog.cancel();
	                    }
	                });
	 
	        // Showing Alert Message
	        alertDialog.show();
	    }
	 
	 
	 public boolean canGetLocation()
	 {
		return canGetLocationNow;
	 }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
