package free.com.mecca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> pins = new ArrayList<OverlayItem>();
	private Context context;
	Mecca mecca = new Mecca();
	MediaPlayer player;
	LocationManager locationManager;
	LocationListener listener;
	Map mapv;
	private final int lang = mecca.getLanguageUsed();
	public AlertDialog alert ;
	public static GeoPoint touched;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private static final int FONT_SIZE = 23;
    private static final int TITLE_MARGIN = 3;
    private int markerHeight;
	public MyOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		markerHeight = ((BitmapDrawable) defaultMarker).getBitmap().getHeight();
		populate();
	}
	
	
	public MyOverlay(Drawable defaultMarker,Context context) {
		this(defaultMarker);
		this.context = context;
		
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapview, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapview, shadow);
		
		for (OverlayItem item:mOverlays)
        {
			 GeoPoint point = item.getPoint();
	         Point markerBottomCenterCoords = new Point();
	         mapview.getProjection().toPixels(point, markerBottomCenterCoords);
	         TextPaint paintText = new TextPaint();
	         Paint paintRect = new Paint();
	         Rect rect = new Rect();
	         paintText.setTextSize(FONT_SIZE);
	         paintText.getTextBounds(item.getTitle(), 0, item.getTitle().length(), rect);

	         rect.inset(-TITLE_MARGIN, -TITLE_MARGIN);
	         rect.offsetTo(markerBottomCenterCoords.x - rect.width()/2, markerBottomCenterCoords.y - markerHeight - rect.height());

	         paintText.setTextAlign(Paint.Align.CENTER);
	         paintText.setTextSize(FONT_SIZE);
	         paintText.setARGB(255, 255, 255, 255);
	         paintRect.setARGB(130, 0, 0, 0);

	         canvas.drawRoundRect( new RectF(rect), 2, 2, paintRect);
	         canvas.drawText(item.getTitle(), rect.left + rect.width() / 2,
	         rect.bottom - TITLE_MARGIN, paintText);
        }
		
	}


	@Override
	protected OverlayItem createItem(int i) {
		
		return pins.get(i);
	}

	@Override
	public int size() {
		
		return pins.size();
	}
	
	public void addOverlay(GeoPoint geo, String title, String snippet)
    {
        OverlayItem item;
        
        
        item = new OverlayItem(geo, title, snippet);
        mOverlays.add(item);
        populate();

    }
	public void InsertPins(OverlayItem item)
	{
		pins.add(item);
		this.populate();
	}
	public void removeOverlay(OverlayItem overlay) {
        pins.remove(overlay);
        populate();
    }
	public void clearPins()
	{
		pins.clear();
		populate();
	}

	public String ConvertPointToAddress(GeoPoint point) {   
        String address = "";
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
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
	
	@Override
	public boolean onTap(final int index) {
		final OverlayItem item = pins.get(index);
		touched = item.getPoint();
		mapv = new Map();
				 alert = new AlertDialog.Builder(context).create();
				
				LayoutInflater inflater  = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View dialogLayout = inflater.inflate(R.layout.alert_dialog, null);
				ImageView imgView = (ImageView)dialogLayout.findViewById(R.id.pinImage);
				
				if(lang==1)
				{	
					alert.setButton(AlertDialog.BUTTON_POSITIVE,"Get Nearest Places",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							touched = item.getPoint();
							if(touched !=null)
							{
							Intent intent = new Intent(context.getApplicationContext(),ViewPlacesOnList.class);
							Bundle bundle = new Bundle();
							bundle.putString("tLat", Double.valueOf(touched.getLatitudeE6()).toString());
							bundle.putString("tLng", Double.valueOf(touched.getLongitudeE6()).toString());
							intent.putExtras(bundle);
							//intent.putExtra("tLan", touched.getLatitudeE6());
							//intent.putExtra("tLng", touched.getLongitudeE6());
							context.startActivity(intent);
							}
						}
					});
					alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Go to here", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							touched = item.getPoint();
							if(touched !=null)
							{
							GeoPoint current = mapv.currentLocation();
							
							String currentLatLng = mapv.ConvertToLocationString(current);
							String dest =mapv.ConvertToLocationString(touched);
							
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+currentLatLng+"&daddr="+dest));
							intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");	
							context.startActivity(intent);
							}
						}
					});
					alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Get Address", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							touched = item.getPoint();
							if(touched !=null)
							{
							final AlertDialog alertD = new AlertDialog.Builder(context).create();
							alertD.setMessage(ConvertPointToAddress(touched));
							alertD.setButton(AlertDialog.BUTTON_POSITIVE, "Okay!", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
								
									alertD.dismiss();
								}
							});
							alertD.show();
						}
						}
						
					});
				}
				
				else
				{
				
				alert.setButton(AlertDialog.BUTTON_POSITIVE,context.getString(R.string.ar_near),new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						touched = item.getPoint();
						if(touched !=null)
						{
						Intent intent = new Intent(context.getApplicationContext(),ViewPlacesOnList.class);
						Bundle bundle = new Bundle();
						bundle.putString("tLat", Double.valueOf(touched.getLatitudeE6()).toString());
						bundle.putString("tLng", Double.valueOf(touched.getLongitudeE6()).toString());
						intent.putExtras(bundle);
						//intent.putExtra("tLan", touched.getLatitudeE6());
						//intent.putExtra("tLng", touched.getLongitudeE6());
						context.startActivity(intent);
						}
					}
				});
				alert.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ar_goHere), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						touched = item.getPoint();
						if(touched !=null)
						{
						GeoPoint current = mapv.currentLocation();
						
						String currentLatLng = mapv.ConvertToLocationString(current);
						String dest =mapv.ConvertToLocationString(touched);
						
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse("http://maps.google.com/maps?saddr="+currentLatLng+"&daddr="+dest));
						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");	
						context.startActivity(intent);
						}
					}
				});
				alert.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.ar_getAddress), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						touched = item.getPoint();
						if(touched !=null)
						{
						final AlertDialog alertD = new AlertDialog.Builder(context).create();
						alertD.setMessage(ConvertPointToAddress(touched));
						alertD.setButton(AlertDialog.BUTTON_POSITIVE, "Okay!", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							
								alertD.dismiss();
							}
						});
						alertD.show();
					}
					}
					
				});
				}
				if(lang ==0)
				{
				switch(index)
				{
				case 0:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.cappa));
					alert.setTitle(R.string.cappa);
					alert.setView(dialogLayout);
					alert.show();
					break;
				case 1:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.haram));
					alert.setTitle(R.string.haram);
					alert.setView(dialogLayout);
					alert.show();
					
					break;		
				case 2:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.ibrahem));
					alert.setTitle(R.string.ibrahem);
					alert.setView(dialogLayout);
					alert.show();
					
					break;
				case 3:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.abd));
					alert.setTitle(R.string.en_abdElaziz);
					alert.setView(dialogLayout);
					alert.show();
					player = MediaPlayer.create(context, R.raw.ar_abdelazez_gate);
					player.start();
					
					break;
				case 4:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.fath));
					alert.setTitle(R.string.fathGate);
					alert.setView(dialogLayout);
					alert.show();
					player = MediaPlayer.create(context, R.raw.ar_fath_gate);
					player.start();
					
					break;
				case 5:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.safamarwa));
					alert.setTitle(R.string.SafaMarwa);
					alert.setView(dialogLayout);
					alert.show();
					
					break;
				case 6:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.omra));
					alert.setTitle(R.string.OmraGate);
					alert.setView(dialogLayout);
					alert.show();
					player = MediaPlayer.create(context, R.raw.ar_omra_gate);
					player.start();
					
					break;
				case 7:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.safa));
					alert.setTitle(R.string.Safa);
					alert.setView(dialogLayout);
					alert.show();
					player = MediaPlayer.create(context, R.raw.ar_safa);
					player.start();
					
					break;
				case 8:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.marwa));
					alert.setTitle(R.string.marwa);
					alert.setView(dialogLayout);
					alert.show();
					player = MediaPlayer.create(context, R.raw.ar_marwa);
					player.start();
					
					break;
				case 9:
					imgView.setImageDrawable(context.getResources().getDrawable(R.raw.fahd));
					alert.setTitle(R.string.FahdGate);
					alert.setView(dialogLayout);
					alert.show();
					player = MediaPlayer.create(context, R.raw.ar_fahd_gate);
					player.start();
					
					break;
				case 10:
					imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.jamra_kobra));
					alert.setTitle(R.string.ar_Jamra_alkobra);
					alert.setView(dialogLayout);
					alert.show();
					break;
				case 11:
					imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.jamra_wasta));
					alert.setTitle(R.string.ar_Jamra_Wasta);
					alert.setView(dialogLayout);
					alert.show();
					break;
				case 12:
					imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.jamra_sogra));
					alert.setTitle(R.string.ar_Jamra_sogra);
					alert.setView(dialogLayout);
					alert.show();
					break;
				case 13:
					imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.arafat));
					alert.setTitle(R.string.ar_arafat);
					alert.setView(dialogLayout);
					alert.show();
					break;
				case 14:
					imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.mozdlfa));
					alert.setTitle(R.string.ar_mozdlfa);
					alert.setView(dialogLayout);
					alert.show();
					break;
				}
			}
				else if(lang ==1)
				{
					switch(index)
					{
					case 0:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.cappa));
						alert.setTitle(R.string.en_capp);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_marwa);
						player.start();
						
						break;
					case 1:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.haram));
						alert.setTitle(R.string.en_haram);
						alert.setView(dialogLayout);
						alert.show();
						
						break;
					case 2:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.ibrahem));
						alert.setTitle(R.string.en_haram);
						alert.setView(dialogLayout);
						alert.show();
						
						break;
					case 3:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.abd));
						alert.setTitle(R.string.en_abdElaziz);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_abdelazez_gate);
						player.start();
						
						break;
					case 4:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.fath));
						alert.setTitle(R.string.en_fathGate);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_fath_gate);
						player.start();
						
						break;
					case 5:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.safamarwa));
						alert.setTitle(R.string.en_safaMarwa);
						alert.setView(dialogLayout);
						alert.show();
						
						break;
					case 6:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.omra));
						alert.setTitle(R.string.en_OmraGate);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_alomra_gate);
						player.start();
						
						break;
					case 7:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.safa));
						alert.setTitle(R.string.en_safa);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_safa);
						player.start();
						
						break;
					case 8:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.marwa));
						alert.setTitle(R.string.en_marwa);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_marwa);
						player.start();
						
						break;
					case 9:
						imgView.setImageDrawable(context.getResources().getDrawable(R.raw.fahd));
						alert.setTitle(R.string.en_FahdGate);
						alert.setView(dialogLayout);
						alert.show();
						player = MediaPlayer.create(context, R.raw.en_fahd_gate);
						player.start();
						
						break;
					case 10:
						imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.jamra_kobra));
						alert.setTitle(R.string.Jamra_alkobra);
						alert.setView(dialogLayout);
						alert.show();
						break;
					case 11:
						imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.jamra_wasta));
						alert.setTitle(R.string.Jamra_Wasta);
						alert.setView(dialogLayout);
						alert.show();
						break;
					case 12:
						imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.jamra_sogra));
						alert.setTitle(R.string.Jamra_sogra);
						alert.setView(dialogLayout);
						alert.show();
						break;
					case 13:
						imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.arafat));
						alert.setTitle(R.string.arafat);
						alert.setView(dialogLayout);
						alert.show();
						break;
					case 14:
						imgView.setImageDrawable(context.getResources().getDrawable(R.drawable.mozdlfa));
						alert.setTitle(R.string.mozdlfa);
						alert.setView(dialogLayout);
						alert.show();
						break;
					}	
					
				}
		/*
		LayoutInflater layoutInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View layout = layoutInf.inflate(R.layout.place_view,null);
		PopupWindow pop = new PopupWindow();
		pop.setContentView(layout);
		pop.setWidth(getLatSpanE6());
		pop.setHeight(getLonSpanE6());
		pop.setFocusable(true);
		pop.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 0, 0);
		*/
         return true;
	}	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapview)
	{
		 if (event.getAction() == 1) {
             GeoPoint geopoint = mapview.getProjection().fromPixels(
                 (int) event.getX(),
                 (int) event.getY());
             // latitude
             double lat = geopoint.getLatitudeE6() / 1E6;
             // longitude
             double lon = geopoint.getLongitudeE6() / 1E6;
             //Toast.makeText(context, "Lat: " + lat + ", Lon: "+lon, Toast.LENGTH_SHORT).show();
         }
         return false;
	}
	@Override
	public boolean onTap(final GeoPoint point, MapView mapview) {
		
		return super.onTap(point, mapview);
	}
}