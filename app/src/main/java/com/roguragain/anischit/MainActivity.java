package com.roguragain.anischit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MapView map;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) locationListener);


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

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);




        IMapController mapController = map.getController();
        mapController.setZoom(15.5);
        //mapController.setZoom(3);
        //GeoPoint startPoint = new GeoPoint(28.3949,84.1240);
//        Log.i("DATA",geoPoint.toString());
        GeoPoint startPoint = new GeoPoint(27.6756,85.3459);
        mapController.setCenter(startPoint);


        /*
        *
        *
        *
        *
        * THIS CODE ADDS CUSTOM MARKER
         */
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        map.invalidate();
        //add the custom marker image
        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_menu_compass));
        //marker on clicked shows the title
        startMarker.setTitle("HELP NEEDED");



        final ArrayList<GeoPoint> wayPoints = new ArrayList<GeoPoint>();
        wayPoints.add(startPoint);
        GeoPoint endPoint = new GeoPoint(27.6915,85.3420);
        wayPoints.add(endPoint);
        final RoadManager roadManager = new MapQuestRoadManager("heU3VYjuj6nAWTNhrhTETQh23qB4QPzu");
        roadManager.addRequestOption("routeType=bicycle");

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                  Road  road = roadManager.getRoad(wayPoints);
                  Polyline roadOverlay = (Polyline) RoadManager.buildRoadOverlay(road);
                    Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_default);

                    for(int i=0;i<road.mNodes.size();i++){
                        RoadNode node = road.mNodes.get(i);
                        Marker nodeMarker = new Marker(map);
                        nodeMarker.setPosition(node.mLocation);
                        nodeMarker.setIcon(nodeIcon);
                        nodeMarker.setTitle("step"+i);
                        nodeMarker.setSnippet(node.mInstructions);
                        nodeMarker.setSubDescription(Road.getLengthDurationText(MainActivity.this, node.mLength, node.mDuration));
                        map.getOverlays().add(nodeMarker);



                    }

                  map.getOverlays().add(roadOverlay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();




        map.invalidate();

    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }



}
