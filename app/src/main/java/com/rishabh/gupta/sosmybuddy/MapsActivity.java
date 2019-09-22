package com.rishabh.gupta.sosmybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String Uid;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("LastLocation");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Intent i = getIntent();
        Uid =i.getStringExtra("uid");
        Log.e("TAG", "onCreate: "+Uid );


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);


        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        databaseReference.child(Uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                UserLatLon user = dataSnapshot.getValue(UserLatLon.class);


                Double latitude = user.getLatitude();
                Double longitude = user.getLongitude();

                Log.e("TAG", "onChildAdded: "+latitude+"********"+longitude);
                LatLng userLatLon = new LatLng(latitude,longitude);
                mMap.addMarker(new MarkerOptions().position(userLatLon).title("User's Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLon,15.5f));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                UserLatLon user = dataSnapshot.getValue(UserLatLon.class);
                LatLng userLatLon = new LatLng(user.getLatitude(),user.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLatLon).title("User's Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLon,15.5f));

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add a marker in Sydney and move the camera
    }
}
