package free.com.mecca;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.protobuf.UnknownFieldSet.Field;

public class Map extends MapActivity {

	long start;
	long stop;
	int latX;
	int lngY;
	MapView mapview;
	MyLocationOverlay myLocation;
	MapController controller;
	GeoPoint touched;
	LocationManager locationManager;
	LocationListener listener;
	List<Overlay> mapOverlays;
	AddItemizedOverlay itemizedOverlay;
	OverlayItem overlayitem;
	GestureDetector gd;
	Vibrator vibrator;
	ImageButton zooMe;
	MapController mc;
	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(R.layout.map);
		
		mapview = (MapView)findViewById(R.id.mapview);
		//Touch touch = new Touch();
	
		mc = mapview.getController();
		double lat = Double.parseDouble("21.422575"); // latitude
		double lon = Double.parseDouble("39.826158"); // longitude
		GeoPoint geoPoint = new GeoPoint((int)(lat * 1E6), (int)(lon * 1E6));
		mc.animateTo(geoPoint);
		mc.setZoom(15);
		mapview.invalidate();
	
		
		zooMe = (ImageButton)findViewById(R.id.locationBtn);
		zooMe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mc.animateTo(currentLocation());
				
			}
		});
		
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		listener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 10, listener);
				
				
			}
		};
		//new vibrator instance 
		vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		
		
		//set point of interest // mecca points 
		String locationArray[] = {"21.422575,39.826158","21.42208,39.8264","21.42261,39.826335","21.421067,39.825804",
				"21.423928,39.826416","21.424163,39.827194","21.422854,39.824667","21.42193,39.827317"
				,"21.425286,39.826995","21.421341,39.824072","21.421591,39.870887","21.420712,39.872861","21.419793,39.874234"
				,"21.354778,39.98457","21.387389,39.914317"};
		List<Overlay> mapOverlays = mapview.getOverlays();
		Drawable drawable_user = this.getResources().getDrawable(R.drawable.mark_blue);
		MyOverlay itemizedOverlay = new MyOverlay(drawable_user, this);
		for(int i=0;i<locationArray.length;i++)
		{
			String[] seperated = locationArray[i].split(",");
			int latE6 = (int)(Double.valueOf(seperated[0])*1000000);
			int lngE6 = (int)(Double.valueOf(seperated[1])*1000000);
			
			GeoPoint gp =new GeoPoint(latE6,lngE6);
			
			
			OverlayItem item = new OverlayItem(gp,"","");
			itemizedOverlay.InsertPins(item);
			
			if(i ==7)
			{
				String splitedToInt[] = locationArray[i].split(",");
				 double x = Double.parseDouble(splitedToInt[0]);
				double y = Double.parseDouble(splitedToInt[1]);
				itemizedOverlay.addOverlay(new GeoPoint((int)(x*1E6),(int)(y*1E6)),getString( R.string.safa),getString(R.string.safa));
			}
			else if (i ==8)
			{
				String splitedToInt[] = locationArray[i].split(",");
				 double x = Double.parseDouble(splitedToInt[0]);
				double y = Double.parseDouble(splitedToInt[1]);
				itemizedOverlay.addOverlay(new GeoPoint((int)(x*1E6),(int)(y*1E6)), getString( R.string.mar),getString(R.string.mar));
			}
			else if(i==9)
			{
				String splitedToInt[] = locationArray[i].split(",");
				 double x = Double.parseDouble(splitedToInt[0]);
				double y = Double.parseDouble(splitedToInt[1]);
				itemizedOverlay.addOverlay(new GeoPoint((int)(x*1E6),(int)(y*1E6)), getString( R.string.kingfahdgate),getString(R.string.kingfahdgate));
			}
		}
		
		mapOverlays.add(itemizedOverlay);
		List<Overlay> overlayList = mapview.getOverlays();
		overlayList.add(new MapGestureDetectorOverlay());
		
		myLocation = new MyLocationOverlay(Map.this,mapview);
		myLocation.enableMyLocation();
		overlayList.add(myLocation);
		
		
		
		
		
	}
	/** 
	 * takes GeoPoint as a parameter 
	 * return string
	 * gets string of current Address of GeoPoint 
	 * */
	public String ConvertPointToAddress(GeoPoint point) {   
        String address = "";
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(
                point.getLatitudeE6()  / 1E6, 
                point.getLongitudeE6() / 1E6, 1);

            if (addresses.size() > 0) {
                for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                    address += addresses.get(0).getAddressLine(index) + " ";
            }
        }
        catch (IOException e) {                
            e.printStackTrace();
        }   

        return address;
    } 
	
	/**
	 * Return GeoPoint of Current location
	 * */

	



	public  GeoPoint currentLocation()
	{
		try
		{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,listener);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		Double lat = location.getLatitude()*1E6;
		Double lng = location.getLongitude()*1E6;
		
		GeoPoint point = new GeoPoint(lat.intValue(),lng.intValue());
		return point;
		}
		catch(Exception ex)
		{
			return new GeoPoint(1,1);
		}
	}
	String ConvertToLocationString(GeoPoint point)
{
	double lat = point.getLatitudeE6() / 1E6;
	double lng = point.getLongitudeE6() / 1E6;
	
	String location = Double.toString(lat)+", "+Double.toString(lng);
	return location;
}
	
	
	@Override
	protected void onPause() {
		myLocation.disableCompass();
		locationManager.removeUpdates(listener);
		super.onPause();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		myLocation.enableCompass();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,10,listener);
	}

	

	
	
	public class MapGestureDetectorOverlay extends Overlay implements android.view.GestureDetector.OnGestureListener {
		 private GestureDetector gestureDetector;
		 private OnGestureListener onGestureListener;

		 public MapGestureDetectorOverlay() {
		  gestureDetector = new GestureDetector(this);
		 }

		
		 @Override
		 public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		  if (gestureDetector.onTouchEvent(event)) {
		   return true;
		  }
		  return false;
		 }

		 public boolean isLongpressEnabled() {
		  return gestureDetector.isLongpressEnabled();
		 }

		 public void setIsLongpressEnabled(boolean isLongpressEnabled) {
		  gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
		 }

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		
		/**
		 * Handles LongPress on Mapview 
		 * which later create new Alertdialog with 3 options 
		 * to get Address
		 * to go there "Navigation"
		 * to get nearest places of the touched point
		 *  */
		
		@Override
		public void onLongPress(MotionEvent e) {
			
			latX = (int)e.getX();
			lngY = (int)e.getY();
			
			touched = mapview.getProjection().fromPixels(latX, lngY);
			
			if(((Mecca)getApplication()).getLanguageUsed()==1)
			{
			// TODO Auto-generated method stub
			AlertDialog alert = new AlertDialog.Builder(Map.this).create();
			alert.setTitle("Choose option");
			alert.setMessage("Please Choose an option here");
			
			
			alert.setButton(AlertDialog.BUTTON_POSITIVE,"Get Nearest Places",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getApplicationContext(),ViewPlacesOnList.class);
					Bundle bundle = new Bundle();
					bundle.putString("tLat", Double.valueOf(touched.getLatitudeE6()).toString());
					bundle.putString("tLng", Double.valueOf(touched.getLongitudeE6()).toString());
					intent.putExtras(bundle);
					//intent.putExtra("tLan", touched.getLatitudeE6());
					//intent.putExtra("tLng", touched.getLongitudeE6());
					startActivity(intent);
					
				}
			});
			alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Go to here", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GeoPoint current = currentLocation();
					String currentLatLng = ConvertToLocationString(current);
					String dest = ConvertToLocationString(touched);
					
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+currentLatLng+"&daddr="+dest));
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");	
					startActivity(intent);
					
				}
			});
			alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Get Address", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final AlertDialog alertD = new AlertDialog.Builder(Map.this).create();
					alertD.setMessage(ConvertPointToAddress(touched));
					alertD.setButton(AlertDialog.BUTTON_POSITIVE, "Okay!", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						
							alertD.dismiss();
						}
					});
					alertD.show();
				}
			});
			
			vibrator.vibrate(50);
			alert.show();
			}
			else
			{
				AlertDialog alert = new AlertDialog.Builder(Map.this).create();
				alert.setTitle("");
				alert.setMessage("رجاء الاختيار");
				
				
				alert.setButton(AlertDialog.BUTTON_POSITIVE,"الاماكن القريبه",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getApplicationContext(),ViewPlacesOnList.class);
						Bundle bundle = new Bundle();
						bundle.putString("tLat", Double.valueOf(touched.getLatitudeE6()).toString());
						bundle.putString("tLng", Double.valueOf(touched.getLongitudeE6()).toString());
						intent.putExtras(bundle);
						//intent.putExtra("tLan", touched.getLatitudeE6());
						//intent.putExtra("tLng", touched.getLongitudeE6());
						startActivity(intent);
						
					}
				});
				alert.setButton(AlertDialog.BUTTON_NEUTRAL, "اذهب الى المكان", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GeoPoint current = currentLocation();
						String currentLatLng = ConvertToLocationString(current);
						String dest = ConvertToLocationString(touched);
						
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+currentLatLng+"&daddr="+dest));
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");	
						startActivity(intent);
						
					}
				});
				alert.setButton(AlertDialog.BUTTON_NEGATIVE, "عنوان المكان", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final AlertDialog alertD = new AlertDialog.Builder(Map.this).create();
						alertD.setMessage(ConvertPointToAddress(touched));
						alertD.setButton(AlertDialog.BUTTON_POSITIVE, "Okay!", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							
								alertD.dismiss();
							}
						});
						alertD.show();
					}
				});
				
				vibrator.vibrate(50);
				alert.show();
			}
		}
		}







	
	
	
	
	/*
	class Touch	extends Overlay
	{
		
		public  GeoPoint currentLocation()
		{
			try
			{
			
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			Double lat = location.getLatitude()*1E6;
			Double lng = location.getLongitude()*1E6;
			
			GeoPoint point = new GeoPoint(lat.intValue(),lng.intValue());
			return point;
			}
			catch(Exception ex)
			{
				return new GeoPoint(1,1);
			}
		}
	private String ConvertToLocationString(GeoPoint point)
	{
		double lat = point.getLatitudeE6() / 1E6;
		double lng = point.getLongitudeE6() / 1E6;
		
		String location = Double.toString(lat)+", "+Double.toString(lng);
		return location;
	}
		
	
	
		public boolean 	onTouchEvent(MotionEvent e, MapView map)
		{
			if(e.getAction() == MotionEvent.ACTION_DOWN)
			{
				start = e.getEventTime();
				latX = (int)e.getX();
				lngY = (int)e.getY();
				
				touched = mapview.getProjection().fromPixels(latX, lngY);
				
			}
			
			if(e.getAction()==MotionEvent.ACTION_UP)
			{
				stop= e.getEventTime();
			}
			if(stop-start>1500)
			{
				AlertDialog alert = new AlertDialog.Builder(Map.this).create();
				alert.setTitle("Choose option");
				alert.setMessage("Please Choose an option here");
				alert.setButton(AlertDialog.BUTTON_POSITIVE,"Get Nearest Places",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getApplicationContext(),ViewPlacesOnList.class);
						Bundle bundle = new Bundle();
						bundle.putString("tLat", Double.valueOf(touched.getLatitudeE6()).toString());
						bundle.putString("tLng", Double.valueOf(touched.getLongitudeE6()).toString());
						intent.putExtras(bundle);
						//intent.putExtra("tLan", touched.getLatitudeE6());
						//intent.putExtra("tLng", touched.getLongitudeE6());
						startActivity(intent);
						
					}
				});
				alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Go to here", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GeoPoint current = currentLocation();
						String currentLatLng = ConvertToLocationString(current);
						String dest = ConvertToLocationString(touched);
						
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+currentLatLng+"&daddr="+dest));
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");	
						startActivity(intent);
						
					}
				});
				alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Get Address", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final AlertDialog alertD = new AlertDialog.Builder(Map.this).create();
						alertD.setMessage(ConvertPointToAddress(touched));
						alertD.setButton(AlertDialog.BUTTON_POSITIVE, "Okay!", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							
								alertD.dismiss();
							}
						});
						alertD.show();
					}
				});
				alert.show();
				return true;
			}
			return false;
		}
		
	}

*/

}
