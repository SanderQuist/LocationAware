package com.example.sander.locationaware;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout dl;
    private ActionBarDrawerToggle ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.oldmap);
        mapFragment.getMapAsync(this);



        dl = (DrawerLayout)findViewById(R.id.dl);
        ab = new ActionBarDrawerToggle(this, dl, R.string.OpenAB, R.string.CloseAB);
        ab.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(ab);
        ab.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
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
        } else {
            super.onBackPressed();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        return ab.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng kladde = new LatLng(51.564998,   4.266931);
        googleMap.addMarker(new MarkerOptions().position(kladde).title(" Marker Kladde"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kladde));
//        LatLng kladde = new LatLng(51.591431,  4.778150);
//        googleMap.setMyLocationEnabled(true);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
          googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kladde, 16));
//        googleMap.setMinZoomPreference(10);
//        googleMap.setMaxZoomPreference(22);
//
//        UiSettings settings = googleMap.getUiSettings();
//        settings.setZoomControlsEnabled(true);
    }

    public boolean selectedItem(@NonNull MenuItem item){
        int id = item.getItemId();
        Fragment fragment = null;

        switch(id){
            case R.id.Maps:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);

                break;
            case R.id.info:
                fragment = new MainFragment();
                displaySelectedFragment(fragment);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameId,fragment);
        fragmentTransaction.commit();
    }
}
