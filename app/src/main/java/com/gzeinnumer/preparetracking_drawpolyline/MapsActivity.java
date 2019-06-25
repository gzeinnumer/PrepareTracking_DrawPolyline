package com.gzeinnumer.preparetracking_drawpolyline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gzeinnumer.preparetracking_drawpolyline.directionhelper.FetchURL;
import com.gzeinnumer.preparetracking_drawpolyline.directionhelper.TaskLoadedCallback;

//todo 6
//todo 18 TaskLoadedCallback
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    //todo 7
    GoogleMap googleMap;
    Button btnGetDirection;
    //todo 10
    MarkerOptions place1, place2;
    //todo 12
    Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //todo 8
        btnGetDirection = findViewById(R.id.btnGetDirection);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //todo 11
        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");
        //todo 13
        //https://developers.google.com/maps/documentation/directions/start
        //todo 14
        //https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=YOUR_API_KEY
        //todo 15
        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl(place1.getPosition(), place2.getPosition(),"driving");
                //todo 17
                new FetchURL(MapsActivity.this).execute(url, "driving");
            }
        });
    }

    //todo 16
    private String getUrl(LatLng position, LatLng position1, String tipe) {
        //awal
        String str_origin = "origin="+position.latitude + "," + position.longitude;
        //akhir
        String str_dest = "destination="+position1.latitude + "," + position1.longitude;
        //mode
        String mode = "mode="+tipe;
        //param to query
        String params = str_origin +"&"+ str_dest +"&"+ mode;
        //out format
        String output = "json";
        //url
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+params+"&key="+getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //todo 9
        this.googleMap = googleMap;
        googleMap.addMarker(place1);
        googleMap.addMarker(place2);
    }

    //todo 19
    @Override
    public void onTaskDone(Object... values) {
        //todo 20
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
    }
}
