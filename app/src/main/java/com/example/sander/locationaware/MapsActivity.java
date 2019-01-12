package com.example.sander.locationaware;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener {

    private static final String TAG = "this";
    private DrawerLayout dl;
    private ActionBarDrawerToggle ab;
    private DatabaseReference mDatabase;
    private DatabaseReference mConditionRef;
    private ArrayList<LocationMarker> markers;
    HelpFragment helpFragment = new HelpFragment();
    private boolean locationPermissionGranted;
    private FusedLocationProviderClient mFuseLocationProviderClient;
    GoogleMap googleMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private int markerCount;
    private Location previousLocation;
    private static final float DEFAULT_ZOOM = 15;

    LocationMarker locationMarker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.oldmap);
        mapFragment.getMapAsync(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        markers = new ArrayList<LocationMarker>();


        dl = (DrawerLayout) findViewById(R.id.dl);
        ab = new ActionBarDrawerToggle(this, dl, R.string.OpenAB, R.string.CloseAB);
        ab.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(ab);
        ab.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                dl.closeDrawers();
                selectedItem(item);

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
         if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
         }
         if (helpFragment != null && helpFragment.isVisible()){
             this.recreate();
             super.onBackPressed();
         }
         else if(MapsActivity.this.equals(this)){
                 new AlertDialog.Builder(this)
                         .setIcon(android.R.drawable.ic_dialog_alert)
                         .setTitle("Closing Activity")
                         .setMessage("Are you sure you want to exit?")
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 finish();
                             }
                         })
                         .setNegativeButton("No", null)
                         .show();

             }
             else{
        this.recreate();
            super.onBackPressed();
         }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return ab.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       getLocationPermission();

        this.googleMap = googleMap;
        for (int i = 0; i < 13; i++){
            addMarker(googleMap, i);

        }
        googleMap.setMyLocationEnabled(true);
        getDeviceLocation();



        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                System.out.println("title"+ title);
                switch (title){
                    case "Stadskantoor Breda":
                        getMarkerInfo(12);
                        break;
                    case "Bibliotheek centrum":
                        getMarkerInfo(11);
                        break;
                    case "Openbaar toilet Valkenberg":
                        getMarkerInfo(10);
                        break;
                    case "Station Breda":
                        getMarkerInfo(9);
                        break;
                    case "Haven Breda":
                        getMarkerInfo(8);
                        break;
                    case "Potkanstraat":
                        getMarkerInfo(7);
                        break;
                    case "Haven Breda 2":
                        getMarkerInfo(6);
                        break;
                    case "Nieuwe Prinsenkade":
                        getMarkerInfo(5);
                        break;
                    case "Kasteelplein":
                        getMarkerInfo(4);
                        break;
                    case "Doctor Jan Ingen Houszplein":
                        getMarkerInfo(3);
                        break;
                    case "Bushalte centrum":
                        getMarkerInfo(2);
                        break;
                    case "Avans Lovendijkstraat & Hogeschoollaan":
                        getMarkerInfo(1);
                        break;

                    case "McDonalds Breda":
                        getMarkerInfo(0);
                        break;


                }
                return false;
            }
        });




        googleMap.setMinZoomPreference(10);
        googleMap.setMaxZoomPreference(22);
        zoomToCurrentLocation();

        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
    }

    public void getMarkerInfo(int i){
        DetailedFragment f = new DetailedFragment();
        Bundle markerinfo = new Bundle();
        markerinfo.putString("title", markers.get(i).getName());
        System.out.println("title" +  markers.get(i).getName());
        markerinfo.putString("description", markers.get(i).getDescription());
        markerinfo.putString("endescription", markers.get(i).getEnDescription());
        markerinfo.putString("price", markers.get(i).getPrice());
        f.setArguments(markerinfo);
        f.show(getSupportFragmentManager(), "detailed_fragment");
    }

    public boolean selectedItem(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.info:
                if (!helpFragment.isVisible()) {
                    fragment = helpFragment;
                    displaySelectedFragment(fragment);
                }
                break;
            case R.id.back:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameId, fragment).addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;

                                  } else {
                    System.out.println(" denied access by user");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
        updateLocationUI();
    }
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: ");
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    public void addMarker(final GoogleMap googleMap, int child) {
            mConditionRef = mDatabase.child(String.valueOf(child));
            markerCount = child;
            mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String markerName = (String) dataSnapshot.child("Name").getValue();
                    String markerDesc = (String) dataSnapshot.child("Description").getValue();
                    String markerEnDesc = (String) dataSnapshot.child("EnDescription").getValue();
                    String markerPrice = (String ) dataSnapshot.child("Price").getValue();
                    double markerX = (double) dataSnapshot.child("x").getValue();
                    double markerY = (double) dataSnapshot.child("y").getValue();
                    locationMarker = new LocationMarker(markerName, markerDesc,markerEnDesc, markerPrice, markerX, markerY);
                    markers.add(locationMarker);
                    System.out.println("markers after loop " + markers.size());


                    for (int i = 0; i < markers.size(); i++) {

                        LatLng markerOnMap = new LatLng(markers.get(i).getX(), markers.get(i).getY());
                        googleMap.addMarker(new MarkerOptions().position(markerOnMap).title(markers.get(i).getName()));
                        System.out.println("hoe vaak loop ik hier " + i);




                        if (i == 0) {

                            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOnMap, 16));
                        }

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void zoomToCurrentLocation(){
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = null;

        try {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        }catch (SecurityException | NullPointerException e){
            Log.e(TAG, "zoomToCurrentLocation: ", e);
        }

        if (location != null)
        {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));

        }
    }

    private void updateLocationUI() {
        Log.d(TAG, "updateLocationUI: map == null: " + (googleMap == null));
        Log.d(TAG, "updateLocationUI: locationPermissionGranted: " + locationPermissionGranted);
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);

                zoomToCurrentLocation();

            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e)  {
            Log.e(TAG, "updateLocationUI: ", e);
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation:");

        mFuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        try {
            if(locationPermissionGranted) {
                Task location = mFuseLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            try {
                                Log.d(TAG, "onComplete: ");
                                Location currentLocation = (Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }
                            catch(Exception e){
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getApplicationContext());
                                dlgAlert.setMessage("Please turn on GPS before you continue");
                                dlgAlert.setTitle("GPS Error");
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();

                                    }
                                });
                                dlgAlert.setCancelable(true);
                                //dlgAlert.create().show();
                            }
                        } else {
                            Log.d(TAG, "current location");
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "SecurityException");

        }
        catch (Exception ex){
            Log.e(TAG, "getDeviceLocation: "+ ex.toString());

        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }


}
