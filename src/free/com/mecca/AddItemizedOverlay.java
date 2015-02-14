package free.com.mecca;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * Class used to place marker or any overlay items on Map
 * */
public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {
 
       private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
       
       
       //Class Context..
       private Context context;
       //set the default marker for the Overlay
       public AddItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
       }
 
       public AddItemizedOverlay(Drawable defaultMarker, Context context) {
            this(defaultMarker);
            this.context = context;
       }
       
       
       // fires when Overlay clicked on map
       @Override
       public boolean onTouchEvent(MotionEvent event, MapView mapView)
       {   
    	   	//get the clicked mapview lat and lng and convert it to pixels to fit mobile screen
    	   // to get best result for clicked point
           if (event.getAction() == 1) {
               GeoPoint geopoint = mapView.getProjection().fromPixels(
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
 
       
       //creates new overlay to set on map
       @Override
       protected OverlayItem createItem(int i) {
          return mapOverlays.get(i);
       }
 
       @Override
       public int size() {
          return mapOverlays.size();
       }
       
       // handles on overlay TAP event this shows alert dialog tapped pin address/name
       @Override
       protected boolean onTap(int index) {
         OverlayItem item = mapOverlays.get(index);
         AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
         dialog.setTitle(item.getTitle());
         dialog.setMessage(item.getSnippet());
         dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
 			public void onClick(DialogInterface dialog, int which) {
 			}
 		});
         dialog.show();
         return true;
       }
       
       //add new overlay to the mapview overlays
       public void addOverlay(OverlayItem overlay) {
          mapOverlays.add(overlay);
       }
       //populate overlay to mapview
       public void populateNow(){
    	   this.populate();
       }
 
    }