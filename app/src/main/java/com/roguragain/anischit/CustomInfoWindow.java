package com.roguragain.anischit;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.security.acl.LastOwnerException;

public class CustomInfoWindow extends MarkerInfoWindow {

    POI mSelectedPoi;
    /**
     * @param  layout that must contain these ids: bubble_title,bubble_description,
     *                    bubble_subdescription, bubble_image
     * @param mapView
     */
    public CustomInfoWindow( MapView mapView) {
        super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);

        Button btn = mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED","asdfasd");
                if(mSelectedPoi.mUrl!=null) {
                    Log.d("CLICKED",mSelectedPoi.mUrl);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSelectedPoi.mUrl));
                    v.getContext().startActivity(myIntent);
                }
            }
        });
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        Marker marker = (Marker) item;
        mSelectedPoi = (POI) marker.getRelatedObject();
       mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
    }
}
