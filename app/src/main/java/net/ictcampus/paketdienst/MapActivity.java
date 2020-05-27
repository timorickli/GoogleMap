package net.ictcampus.paketdienst;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Random;

public class MapActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private Marker paketmarker, greenmarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Create Map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //Setzt den Positions marker
        map.setMyLocationEnabled(true);
        //3D Gebäude
        map.setBuildingsEnabled(true);
        //Indoor Ebenen Deaktiviert
        map.getUiSettings().setIndoorLevelPickerEnabled(false);
        //Toolbar
        map.getUiSettings().setMapToolbarEnabled(true);
        //KompassDeaktiviert
        map.getUiSettings().setCompassEnabled(false);
        //Zommsteurerelemente
        map.getUiSettings().setZoomControlsEnabled(true);
        //Setzt die Kamera Position auf den Aktuellen Punkt
        if (map != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(getLocation().getLatitude(), getLocation().getLongitude())).zoom(17.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);
        }
        //Kreiert verschiedene Marker
        //Zuerst einer mit Icon
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.paket_einzeln);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap marker = Bitmap.createScaledBitmap(b, 100, 100, false);
        paketmarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(getLati(), getLong()))
                .icon(BitmapDescriptorFactory.fromBitmap(marker))
        );
        //Danach ein "normaler" Marker
        map.addMarker(new MarkerOptions()
                .position(new LatLng(getLati(), getLong()))
                .title("Hallo ich bin ein Marker")
                .snippet("Ich wohne in Bern")
        );
        //Klick Event auf den Marker
        googleMap.setOnMarkerClickListener(this::onMarkerClick);

    }
    //Marker Click event Hanler
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(paketmarker)) {
            greenmarker= map.addMarker(new MarkerOptions()
                    .position(new LatLng(getLati(), getLong()))
                    .title("Ich bin ein neuer Marker")
                    .icon(BitmapDescriptorFactory.defaultMarker
                            (BitmapDescriptorFactory.HUE_GREEN))
                    .draggable(true)
            );
        }
        if(marker.equals(greenmarker)){
            try{
                boolean success= map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style));
            }
            catch (Resources.NotFoundException e){
                Log.e("Map", "Map style not found");
            }
        }
        return false;
    }


    //Methoden nichts mit google Api zu tun
    //Kreiert ein Zufälliger Längengrad im Umkreis von ca. 100 Meter
    private double getLong(){
        Random random= new Random();
        double randomLong;
        int operatorLong = random.nextInt((2 - 1) + 1) + 1;
        switch (operatorLong) {
            case 1:
                randomLong = getLocation().getLongitude() + (random.nextInt((10 - 1) + 1) + 1) * 0.0001;
                break;
            default:
                randomLong = getLocation().getLongitude() - (random.nextInt((10 - 1) + 1) + 1) * 0.0001;
                break;
        }
        return randomLong;
    }
    //Kreiert ein Zufälliger Breitengrad im Umkreis von ca. 100 Meter
    private double getLati(){
        Random random= new Random();
        int operatorLati = random.nextInt((2 - 1) + 1) + 1;
        double randomLati;
        switch (operatorLati) {
            case 1:
                randomLati = getLocation().getLatitude() + (random.nextInt((10 - 1) + 1) + 1) * 0.0001;
                break;
            default:
                randomLati = getLocation().getLatitude() - (random.nextInt((10 - 1) + 1) + 1) * 0.0001;
                break;
        }
        return randomLati;
    }


    //Nicht von der Google API ist für die Aktuelle Position zu holen/ Für die Marker Positionen
    @SuppressLint("MissingPermission")
    public Location getLocation() {
        Location location = null;
        try {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            //schauen, welcher service verfügbar ist GPS/Netzwerk
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean net = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps && !net) {

            } else {
                if (gps) {
                    location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

                    Log.d("GPS Provider", "GPS Provider");
                } else if (net) {
                    location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    Log.d("Network Provider", "Network Provider");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


}

