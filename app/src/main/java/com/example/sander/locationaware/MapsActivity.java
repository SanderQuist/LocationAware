package com.example.sander.locationaware;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout dl;
    private ActionBarDrawerToggle ab;
    private DatabaseReference mDatabase;
    private DatabaseReference mConditionRef;
    private ArrayList<LocationMarker> markers;
    private int markerCount;

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
        } else
            this.recreate();
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return ab.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
        }
        googleMap.setMyLocationEnabled(true);
        addMarker(googleMap, 0);
        addMarker(googleMap, 1);

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Marker Kladde")) {
                    new DetailedFragment().show(getSupportFragmentManager(), "detailed_fragment");
                }


                return false;
            }
        });


//        Polyline line = googleMap.addPolyline(new PolylineOptions()
//                .add(kladde, kladde1)
//                .width(5)
//                .color(Color.RED));


//        googleMap.setMinZoomPreference(10);
//        googleMap.setMaxZoomPreference(22);
//
//        UiSettings settings = googleMap.getUiSettings();
//        settings.setZoomControlsEnabled(true);
    }

    public boolean selectedItem(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.info:
                fragment = new HelpFragment();
                displaySelectedFragment(fragment);
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
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    System.out.println(" denied access by user");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void addMarker(final GoogleMap googleMap, int child) {
            mConditionRef = mDatabase.child(String.valueOf(child));
            markerCount = child;
            mConditionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String markerName = (String) dataSnapshot.child("Name").getValue();
                    String markerDesc = (String) dataSnapshot.child("Description").getValue();
                    long markerDate = (long) dataSnapshot.child("Date").getValue();
                    double markerX = (double) dataSnapshot.child("x").getValue();
                    double markerY = (double) dataSnapshot.child("y").getValue();
                    LocationMarker marker = new LocationMarker(markerName, markerDesc, markerDate, markerX, markerY);
                    markers.add(marker);

                    LatLng markerOnMap = new LatLng(markers.get(markerCount).getX(), markers.get(markerCount).getY());
                    googleMap.addMarker(new MarkerOptions().position(markerOnMap).title(markers.get(markerCount).getName()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(markerOnMap));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOnMap, 16));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
