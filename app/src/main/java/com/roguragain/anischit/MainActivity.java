package com.roguragain.anischit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.AccessNetworkConstants;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MapEventsReceiver,LocationListener {
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            event.startTracking();
            //Log.d("CLICKED", "HAITTT");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.d("ROSHAN CLICKED", String.valueOf(keyCode));

            String smsMessage = null;
            getLocation();

            while (currentLocation==null)
            {
                getLocation();
            }
            if (currentLocation != null) {
                smsMessage = "LATITUDe="+String.valueOf(currentLocation.getLongitude()) + "SENT FROM APP" + "LONGITUDE="+String.valueOf(currentLocation.getLatitude());
                String scAddress = null;
                //set pending intent to broadcast

                PendingIntent sentIntent = null,deliveryIntent = null;

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("9803103252",scAddress, smsMessage, sentIntent, deliveryIntent);
                Toast.makeText(MainActivity.this,"Message Sent", Toast.LENGTH_LONG).show();
            } else {
                getLocation();
            }


            return true;

            }
           // Toast.makeText(MainActivity.this, "CLICKED", Toast.LENGTH_LONG).show();




        return super.onKeyLongPress(keyCode, event);
    }

    MapView map;
    ImageView getLocation;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    Location currentLocation;
    LocationManager locationManager;
    LocationListener locationListener;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<Marker> items = new ArrayList<>(10);

        final Drawable logo = getResources().getDrawable(R.drawable.ic_menu_mylocation);

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_main);


        getLocation = findViewById(R.id.myLocation);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        final IMapController mapController = map.getController();
        mapController.setZoom(15.5);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);


        Marker marker = new Marker(map);
        GeoPoint geoPoint = new GeoPoint(27.7172, 85.3240);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setImage(logo);
        map.getOverlays().add(marker);
        mapController.setCenter(geoPoint);




        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    //
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                /*
                 *
                 * Location of victim
                 * Add button pressed listener
                 * Add send data to server
                 *
                 * */
                //Send location to server
                // Location of the victim

                getLocation();

                if(currentLocation!=null) {
                    GeoPoint geoPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    Marker marker = new Marker(map);
                    marker.setImage(logo);
                    marker.setTitle(currentLocation.getLatitude()+"sadfasdf"+currentLocation.getLongitude());
                    items.add(0, marker);
                    Log.d("got location","YES");
                    marker.setPosition(geoPoint);
                    mapController.setCenter(geoPoint);
                    mapController.setZoom(16.5);
                    mapController.animateTo(geoPoint);
                    mapController.stopAnimation(false);
                    map.getOverlays().add(marker);
                    map.invalidate();

                }


            }
        });


        map.invalidate();

    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    @Override
    public boolean longPressHelper(GeoPoint p) {

        listItems = getResources().getStringArray(R.array.helps);

        /*
         * Add a dialog when there is tap to add the marker
         * In the dialog put fields like releif and others
         *
         *
         * */
//        Dialog dialog = new Dialog(MainActivity.this);
//        dialog.setTitle(R.string.dialog_top);
//        dialog.setContentView(R.layout.dialog);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle(R.string.dialog_top);


        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if (isChecked) {
                    mUserItems.add(position);

                }
                mUserItems.remove((Integer.valueOf(position)));
            }
        });
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }

                    /*
                     * Add code for correct icon on pressed
                     *
                     * */
                }

                //euta line xaina

                Log.d("CHECKED DATA", item);
            }
        });

        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Marker m = new Marker(map);
        GeoPoint g = new GeoPoint(p.getLatitude(), p.getLongitude());
        m.setPosition(g);
        map.getOverlays().add(m);

        mBuilder.create().show();
        map.invalidate();

        InfoWindow.closeAllInfoWindowsOn(map);
        Toast.makeText(this, "Tapped", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {

        GroundOverlay groundOverlay = new GroundOverlay();
        groundOverlay.setPosition(p);
        groundOverlay.setImage(getResources().getDrawable(R.drawable.person).mutate());
        groundOverlay.setDimensions(200.0f);
        map.getOverlays().add(groundOverlay);

//        Polygon circle = new Polygon(map);
//        circle.setPoints(Polygon.pointsAsCircle(p,100.0));
//        circle.setFillColor(0x12121212);
//        circle.setStrokeColor(Color.RED);
//        circle.setStrokeWidth(2);
//        circle.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble,map));
//        circle.setTitle("Centered on " + p.getLatitude()+" , "+ p.getLongitude());
//        map.getOverlays().add(circle);
        map.invalidate();
        return false;

    }

    public void getLocation() {


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }

        //Location location = locationManager.getLastKnownLocation(bestProvider);

        locationManager.requestLocationUpdates(bestProvider,0,0, MainActivity.this);

            //locationManager.requestLocationUpdates(bestProvider,0,0, MainActivity.this);


        if(currentLocation!=null){

            Toast.makeText(MainActivity.this,"got location", Toast.LENGTH_LONG).show();

        }
        else {
            locationManager.requestLocationUpdates(bestProvider,0,0, MainActivity.this);
        }



       // locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);


    }


    @Override
    public void onLocationChanged(Location location) {
            currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //locationManager.removeUpdates(locationListener);
    }
}


