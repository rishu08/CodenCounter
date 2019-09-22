package com.rishabh.gupta.sosmybuddy;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {

    //    public static final int REQUEST_CODE_PERMISSION = 1234;
    LocationManager locationManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReference = firebaseDatabase.getReference().child("LastLocation");
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(this);
        firebaseUser = firebaseAuth.getCurrentUser();

//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
//                &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION
//                    , Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
//
//        }
//
//        else
//        {
//
//            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//            {
//                locationListner();
//            }
//            else
//            {
//                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("latitude").setValue(lastknownLocation.getLatitude());
//                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("longitude").setValue(lastknownLocation.getLongitude());
//                Toast.makeText(this, "Looks like your Location is disabled! please Enable it", Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,new Dashboard())
                    .commit();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();

        if (id == R.id.action_SignOut) {

            AuthUI.getInstance()
                    .signOut(MainActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(Arrays.asList(
//                                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
//                                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.PhoneBuilder().build()))
                                            .setIsSmartLockEnabled(false)
                                            .build(),
                                    100);

                        }
                    });

            return true;
        }
        if (id==R.id.action_share)
        {
            final String appPackageName = getBaseContext().getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            getBaseContext().startActivity(sendIntent);
        }

//        if (id == R.id.aboutus)
//        {
//            Intent i = new Intent(MainActivity.this,AboutUs.class);
//            startActivity(i);
//
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,new Dashboard())
                    .commit();

        }
        else if (id == R.id.nav_contacts) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,new Contacts())
                    .commit();

        }
        else if (id == R.id.nav_track) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,new MapLoader())
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        if (firebaseAuth.getCurrentUser() == null) {


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
//                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
//                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build()))
                            .setIsSmartLockEnabled(false)
                            .build(),
                    100);


        }
        else
        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerview = navigationView.getHeaderView(0);

            name = headerview.findViewById(R.id.name);
            name.setText(firebaseAuth.getCurrentUser().getPhoneNumber());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,new Dashboard())
                    .commit();

        }

    }


//    @SuppressLint("MissingPermission")
//    private void locationListner() {
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,0,this);
//

//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("latitude").setValue(location.getLatitude());
//        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("longitude").setValue(location.getLongitude());
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
