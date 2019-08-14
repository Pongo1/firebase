package com.pongo.firebase;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.core.Context;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
  private String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
  private String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
  private Boolean locationPermGranted = false;
  private GoogleMap mMap;
  private LocationManager locationManager;
  private FusedLocationProviderClient locationFinder;
  private LatLng mCoord;



  public void showCoord(View v){
    Toast.makeText(this, "Here are you coordinates: "+mCoord.latitude+", "+ mCoord.longitude, Toast.LENGTH_SHORT).show();
  }
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    // Add a marker in Sydney and move the camera
    //LatLng sydney = new LatLng(-34, 151);
    //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    if (locationPermGranted) {
      getDeviceLocation();

      if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        return;
      }
      mMap.setMyLocationEnabled(true);
      mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    getLocationPermission();

  }


  public void getDeviceLocation(){
    locationFinder = LocationServices.getFusedLocationProviderClient(this);
    locationManager = (LocationManager)getSystemService(MapsActivity.LOCATION_SERVICE);
    try{
      final Task locationFinderTask = locationFinder.getLastLocation();
      locationFinderTask.addOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
          if(task.isSuccessful()) {
            Location currentLocation = (Location) task.getResult();
            if(currentLocation !=null) {
              mCoord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
              Toast.makeText(MapsActivity.this, "Your lat is: " + mCoord.latitude + "Your long is: " + mCoord.longitude, Toast.LENGTH_SHORT).show();
              mMap.addMarker(new MarkerOptions().position(mCoord).title("Marker in Accra"));
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoord,15f));
            }else{
              String bestProvider = locationManager.getBestProvider(new Criteria(),true);
              locationManager.requestLocationUpdates(bestProvider,1000,0,MapsActivity.this);
            }
          }else{
            Toast.makeText(MapsActivity.this, "Unable to get current location!", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }catch(SecurityException e){
      Log.d("MapActivity: ",e.getMessage());
    }

  }
  public void initMap(){
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
      .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
  }
  public void getLocationPermission(){
    String[] perm_array = {FINE_LOCATION,COARSE_LOCATION};
    if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
      //if fine_location permission is granted, check for coarse_location too
      Toast.makeText(this, "Fine permission granted!", Toast.LENGTH_SHORT).show();
      if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        locationPermGranted = true;
        Toast.makeText(this, "Coarse Permission Granted!", Toast.LENGTH_SHORT).show();
        initMap();
      }
    }
    else{
      ActivityCompat.requestPermissions(this,perm_array,4000);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch(requestCode){
      case 4000:
        if(grantResults.length >0) {
          for(int i = 0; i < grantResults.length ; i ++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
              locationPermGranted = false;
              return;
            }
          }
          locationPermGranted = true;
          initMap();
        }
    }
  }


}
