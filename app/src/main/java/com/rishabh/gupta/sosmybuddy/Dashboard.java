package com.rishabh.gupta.sosmybuddy;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment implements LocationListener{

    public static final int REQUEST_CODE_PERMISSION = 1234;

    LocationManager locationManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("LastLocation");
    DatabaseReference databaseContact = firebaseDatabase.getReference().child("Contacts");
    ArrayList<Contacts_class> contactlist;


//    Spinner spinner;
    Button text1,text2,text3,text4,text5,text6;
    ImageButton police,ambulance,fire,women,disaster,child;
//    private static final String[]time = {"5 Min", "10 Min", "15 Min","20 Min","25 Min","30 Min","60 Min"};

//    static int minTime = 0000;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        text1 = view.findViewById(R.id.custombutton1);
        text2 = view.findViewById(R.id.custombutton2);
        text3 = view.findViewById(R.id.custombutton3);
        text4 = view.findViewById(R.id.custombutton4);
        text5 = view.findViewById(R.id.custombutton5);
        text6 = view.findViewById(R.id.custombutton6);
        police = view.findViewById(R.id.police);
        ambulance = view.findViewById(R.id.ambulance);
        fire = view.findViewById(R.id.fireBrigade);
        women = view.findViewById(R.id.women);
        disaster = view.findViewById(R.id.disaster);
        child = view.findViewById(R.id.child_care);
//        spinner = view.findViewById(R.id.spinner1);

        contactlist = new ArrayList<>();

        final Double[] latitude = new Double[1];
        final Double[] longitude = new Double[1];


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE},REQUEST_CODE_PERMISSION);

        }

        else
        {

            databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserLatLon user = dataSnapshot.getValue(UserLatLon.class);


                    latitude[0] = user.getLatitude();
                    longitude[0] = user.getLongitude();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    UserLatLon user = dataSnapshot.getValue(UserLatLon.class);


                    latitude[0] = user.getLatitude();
                    longitude[0] = user.getLongitude();

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



            databaseContact.child(firebaseAuth.getCurrentUser().getPhoneNumber()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Contacts_class contacts_class = dataSnapshot.getValue(Contacts_class.class);
                    contactlist.add(contacts_class);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

//            final Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (lastKnownLocation!=null)
            {
                latitude[0]=lastKnownLocation.getLatitude();
                longitude[0] = lastKnownLocation.getLongitude();
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,000,0,this);

            }
            else
            {
                Toast.makeText(getContext(), "GPS disabled!!Please Enable it", Toast.LENGTH_SHORT).show();
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.e("TAG", "onCreateView: "+lastKnownLocation );
//                latitude[0] = lastKnownLocation.getLatitude();
//                longitude[0] = lastKnownLocation.getLongitude();
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,0,this);

            }




//////////////////////////////////////////////app rate custom dialog///////////////////////////////////////////
            AppRate.with(getContext())
                    .setInstallDays(0) // default 10, 0 means install day.
                    .setLaunchTimes(1) // default 10
                    .setRemindInterval(2) // default 1
                    .setShowLaterButton(true) // default true
                    .setDebug(false) // default false
                    .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                        @Override
                        public void onClickButton(int which) {
                            Log.d(MainActivity.class.getName(), Integer.toString(which));
                        }
                    })
                    .monitor();

            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(getActivity());
            /////////////////////////////Image Call//////////////////////////////////////////////////////////
            police.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "100"));
                    startActivity(intent);
                }
            });

            women.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "181"));
                    startActivity(intent);
                }
            });
            ambulance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "102"));
                    startActivity(intent);
                }
            });
            fire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "101"));
                    startActivity(intent);
                }
            });
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1098"));
                    startActivity(intent);
                }
            });
            disaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "108"));
                    startActivity(intent);
                }
            });
////////////////////////////////////////Text message 1///////////////////////////////////////////////////////
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = null;


                    String message1 = "Hello,I am in Danger\n\nMy current location is: "+"http://maps.google.com/maps?f=q&q=("+latitude[0]+","+longitude[0]+")"+"\nReach out to me as soon as possible";
                    for (int i = 0;i<contactlist.size();i++)
                    {


                        if (i==0)
                        {
                            try {
//                                String text = "This is a test";// Replace with your message.
                                String toNumber = "91+"+contactlist.get(i).getNumber();// Replace with mobile phone number without +Sign or leading zeros.

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+message1));
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            phoneNumber = contactlist.get(i).getNumber();
                        }
                        else if (i==contactlist.size()-1)
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }
                        else
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }

//                        phoneNumber = phoneNumber+contactlist.get(i).getNumber();
                        Log.e("TAG", "onClick: "+contactlist.get(i).getNumber() );
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", message1);
                    startActivity(intent);

                }
            });

            ////////////////////////////////////////Text message 2///////////////////////////////////////////////////////
            text2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = null;


                    String message2 = "Hello, I am not safe\nPlease track my location "+"http://maps.google.com/maps?f=q&q=("+latitude[0]+","+longitude[0]+")";
                    for (int i = 0;i<contactlist.size();i++)
                    {
                        if (i==0)
                        {
                            try {
//                                String text = "This is a test";// Replace with your message.
                                String toNumber = "91+"+contactlist.get(i).getNumber();// Replace with mobile phone number without +Sign or leading zeros.

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+message2));
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            phoneNumber = contactlist.get(i).getNumber();
                        }
                        else if (i==contactlist.size()-1)
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }
                        else
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }

//                        phoneNumber = phoneNumber+contactlist.get(i).getNumber();
                        Log.e("TAG", "onClick: "+contactlist.get(i).getNumber() );
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", message2);
                    startActivity(intent);

                }
            });

            ////////////////////////////////////////Text message 3///////////////////////////////////////////////////////
            text3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = null;


                    String message3 = "Hello,I need HELP!! \nMy current location is: \n"+"http://maps.google.com/maps?f=q&q=("+latitude[0]+","+longitude[0]+")";
                    for (int i = 0;i<contactlist.size();i++)
                    {
                        if (i==0)
                        {
                            try {
//                                String text = "This is a test";// Replace with your message.
                                String toNumber = "91+"+contactlist.get(i).getNumber();// Replace with mobile phone number without +Sign or leading zeros.

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+message3));
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            phoneNumber = contactlist.get(i).getNumber();
                        }
                        else if (i==contactlist.size()-1)
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }
                        else
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }

//                        phoneNumber = phoneNumber+contactlist.get(i).getNumber();
                        Log.e("TAG", "onClick: "+contactlist.get(i).getNumber() );
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", message3);
                    startActivity(intent);

                }
            });

            ////////////////////////////////////////Text message 4///////////////////////////////////////////////////////
            text4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = null;


                    String message4 = "Hello, Track my location!! \nMy current location is: \n"+"http://maps.google.com/maps?f=q&q=("+latitude[0]+","+longitude[0]+")";
                    for (int i = 0;i<contactlist.size();i++)
                    {
                        if (i==0)
                        {
                            try {
//                                String text = "This is a test";// Replace with your message.
                                String toNumber = "91+"+contactlist.get(i).getNumber();// Replace with mobile phone number without +Sign or leading zeros.

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+message4));
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            phoneNumber = contactlist.get(i).getNumber();
                        }
                        else if (i==contactlist.size()-1)
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }
                        else
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }

//                        phoneNumber = phoneNumber+contactlist.get(i).getNumber();
                        Log.e("TAG", "onClick: "+contactlist.get(i).getNumber() );
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", message4);
                    startActivity(intent);

                }
            });
            ////////////////////////////////////////Text message 5///////////////////////////////////////////////////////
            text5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = null;


                    String message5 = "I met with small accident! \n"+"http://maps.google.com/maps?f=q&q=("+latitude[0]+","+longitude[0]+")";
                    for (int i = 0;i<contactlist.size();i++)
                    {
                        if (i==0)
                        {
                            try {
//                                String text = "This is a test";// Replace with your message.
                                String toNumber = "91+"+contactlist.get(i).getNumber();// Replace with mobile phone number without +Sign or leading zeros.

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+message5));
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            phoneNumber = contactlist.get(i).getNumber();
                        }
                        else if (i==contactlist.size()-1)
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }
                        else
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }

//                        phoneNumber = phoneNumber+contactlist.get(i).getNumber();
                        Log.e("TAG", "onClick: "+contactlist.get(i).getNumber() );
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", message5);
                    startActivity(intent);

                }
            });
            ////////////////////////////////////////Text message 6///////////////////////////////////////////////////////
            text6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = null;


                    String message6 = "Hello, I am safe now : \n"+"http://maps.google.com/maps?f=q&q=("+latitude[0]+","+longitude[0]+")";
                    for (int i = 0;i<contactlist.size();i++)
                    {
                        if (i==0)
                        {
                            try {
//                                String text = "This is a test";// Replace with your message.
                                String toNumber = "91+"+contactlist.get(i).getNumber();// Replace with mobile phone number without +Sign or leading zeros.

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+message6));
                                startActivity(intent);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            phoneNumber = contactlist.get(i).getNumber();
                        }
                        else if (i==contactlist.size()-1)
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }
                        else
                        {
                            phoneNumber = phoneNumber +";"+contactlist.get(i).getNumber();
                        }

//                        phoneNumber = phoneNumber+contactlist.get(i).getNumber();
                        Log.e("TAG", "onClick: "+contactlist.get(i).getNumber() );
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                    intent.putExtra("sms_body", message6);
                    startActivity(intent);

                }
            });


//            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

//            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//            {
//                locationListner();
//            }
//            else
//            {
//                Toast.makeText(getContext(), "Looks like your Location is disabled! please Enable it", Toast.LENGTH_SHORT).show();
////                Log.e("TAG", "onCreateView: "+lastknownLocation.getLongitude()+"---"+lastknownLocation.getLatitude() );
//                try
//                {
//                    databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("userLocation").child("latitude").setValue(lastknownLocation.getLatitude());
//                    databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("userLocation").child("longitude").setValue(lastknownLocation.getLongitude());
//                }
//                catch (Exception e)
//                {
//
//                }
//            }

//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                    android.R.layout.simple_spinner_item,time);
//
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);
//            spinner.setOnItemSelectedListener(this);



        }


        return view;
    }

    @SuppressLint("MissingPermission")
    private void locationListner() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,000,0,this);


    }


    @Override
    public void onLocationChanged(Location location) {
        databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("userLocation").child("latitude").setValue(location.getLatitude());
        databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).child("userLocation").child("longitude").setValue(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        switch (position)
//        {
//            case 0:
//            {
//                minTime = 300000;
//                break;
//            }
//            case 1:
//            {
//                minTime = 600000;
//                break;
//            }
//            case 2:
//            {
//                minTime =900000;
//                break;
//            }
//
//            case 3:
//            {
//                minTime =1200000;
//                break;
//            }
//
//            case 4:
//            {
//                minTime =1500000;
//                break;
//            }
//
//            case 5:
//            {
//                minTime =1800000;
//                break;
//            }
//
//            case 6:
//            {
//                minTime =3600000;
//                break;
//            }
//
//
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
