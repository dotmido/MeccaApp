package free.com.mecca;


import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;

	// Place Details
	PlaceDetails placeDetails;
	Bitmap image;
	// Progress dialog
	ProgressDialog pDialog;
	Button goThereBtn;
	LocationManager locationManager;
	private Handler mHandler;
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);

		goThereBtn = (Button) findViewById(R.id.goThere);

		goThereBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GeoPoint current = currentLocation();

				String currentLatLng = ConvertToLocationString(current);
				String dest = ((TextView) findViewById(R.id.location))
						.getText().toString();

				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr="
								+ currentLatLng + "&daddr=" + dest));
				intent.setClassName("com.google.android.apps.maps",
						"com.google.android.maps.MapsActivity");
				startActivity(intent);

			}
		});

		Intent i = getIntent();

		// Place referece id
		String reference = i.getStringExtra(KEY_REFERENCE);

		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
	}

	private String ConvertToLocationString(GeoPoint point) {
		double lat = point.getLatitudeE6() / 1E6;
		double lng = point.getLongitudeE6() / 1E6;

		String location = Double.toString(lat) + ", " + Double.toString(lng);
		return location;
	}

	
	public GeoPoint currentLocation() {
		try {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			Double lat = location.getLatitude() * 1E6;
			Double lng = location.getLongitude() * 1E6;

			GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
			return point;
		} catch (Exception ex) {
			return new GeoPoint(1, 1);
		}
	}

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	@Override
	protected void onStop() {
		if(listener !=null && locationManager !=null)
		{
		locationManager.removeUpdates(listener);
		}// TODO Auto-generated method stub
		super.finish();
		super.onStop();
		
	}


	private final LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// A new location update is received. Do something useful with it.
			// Update the UI with
			// the location update.
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, listener);
			
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];

			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);
				image = googlePlaces.getPlacesPhotos(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if (placeDetails != null) {
						String status = placeDetails.status;

						// check place deatils status
						// Check for all possible status
						if (status.equals("OK")) {
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double
										.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double
										.toString(placeDetails.result.geometry.location.lng);
								// String [] photos = placeDetails.photo;

								Log.d("Place ", name + address + phone
										+ latitude + longitude);

								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);

								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if
																			// name
																			// is
																			// null
																			// display
																			// as
																			// "Not present"
								address = address == null ? "Not present"
										: address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present"
										: latitude;
								longitude = longitude == null ? "Not present"
										: longitude;

								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html
										.fromHtml("<b>Phone:</b> " + phone));
								lbl_location
										.setText(latitude + "," + longitude);

								if (image != null) {
									Bitmap img = image;
									ImageView refImg = (ImageView) findViewById(R.id.ReferenceTx);
									refImg.setImageBitmap(img);

								}
							}

						} else if (status.equals("ZERO_RESULTS")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Near Places", "Sorry no place found.",
									false);
						} else if (status.equals("UNKNOWN_ERROR")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry unknown error occured.", false);
						} else if (status.equals("OVER_QUERY_LIMIT")) {
							alert.showAlertDialog(
									SinglePlaceActivity.this,
									"Places Error",
									"Sorry query limit to google places is reached",
									false);
						} else if (status.equals("REQUEST_DENIED")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry error occured. Request is denied",
									false);
						} else if (status.equals("INVALID_REQUEST")) {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error",
									"Sorry error occured. Invalid Request",
									false);
						} else {
							alert.showAlertDialog(SinglePlaceActivity.this,
									"Places Error", "Sorry error occured.",
									false);
						}
					} else {
						alert.showAlertDialog(SinglePlaceActivity.this,
								"Places Error", "Sorry error occured.", false);
					}

				}
			});

		}

	}

}
