package com.rishabh.gupta.sosmybuddy;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapLoader extends Fragment {

    RecyclerView rv;
    ArrayList<Contacts_class> contactlist;
    ArrayList<Contacts_class> checkPermission;
    Adapter_auth adapter_auth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    EditText uid;
Button track;
    public MapLoader() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_loader, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Contacts");
        firebaseAuth = FirebaseAuth.getInstance();
        rv = view.findViewById(R.id.rv);

        contactlist =new ArrayList<>();
        checkPermission = new ArrayList<>();

        uid = view.findViewById(R.id.et);
        track = view.findViewById(R.id.search);

        adapter_auth = new Adapter_auth(contactlist,getContext(),databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).toString());


        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final String UID = "+91"+uid.getText().toString();
                final String UID = uid.getText().toString();
                uid.setText("");
                final String myNo = firebaseAuth.getCurrentUser().getPhoneNumber();

                /////////////////////////////////////////////////////////////////////


                /////////////////////////////////////////////////////////////////////
                databaseReference.child(UID).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Contacts_class contacts_class = dataSnapshot.getValue(Contacts_class.class);
                        checkPermission.add(contacts_class);
                        Log.e("TAG", "onChildAdded: "+contacts_class.getNumber()+"====="+myNo);

                        if (contacts_class.getNumber() .equals(myNo) )
                        {
                            Log.e("TAG", "onClick: NO. MATCHED "+myNo );
                            Intent maps = new Intent(getContext(),MapsActivity.class);
                            maps.putExtra("uid",UID);
                            startActivity(maps);
                            Log.e("TAG", "onClick: NO. MATCHED "+myNo );
                        }
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

//                String myNo = firebaseAuth.getCurrentUser().getPhoneNumber();
                Log.e("TAG", "onClick: my no.:"+myNo );

//                int flag=1;
//                for (int i=0; i<checkPermission.size();i++)
//                {
//                   if (checkPermission.get(i).getNumber() == myNo)
//                    {
//                        Intent maps = new Intent(getContext(),MapsActivity.class);
//                        maps.putExtra("uid",UID);
//                        startActivity(maps);
//                        Log.e("TAG", "onClick: NO. MATCHED "+myNo );
//                        flag=0;
//                        return;
//                    }
//                }
//                if (flag != 0)
//                {
//                    Toast.makeText(getActivity(), "Looks like you dont have Permission to track user!!", Toast.LENGTH_SHORT).show();
//
//                }


//                Log.e("TAG", "onClick: LOop" );
//                if (checkPermission.contains(firebaseAuth.getCurrentUser().getPhoneNumber()))
//                {
//                    Intent maps = new Intent(getContext(),MapsActivity.class);
//                    maps.putExtra("uid",UID);
//                    startActivity(maps);
//                }
//
//                else {
//                    Toast.makeText(getActivity(), "Looks like you dont have Permission to track user!!", Toast.LENGTH_SHORT).show();
//                }

            }
        });





        databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Contacts_class contacts_class = dataSnapshot.getValue(Contacts_class.class);
                contactlist.add(contacts_class);

                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(adapter_auth);


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


        return view;
    }

}
