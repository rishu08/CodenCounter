package com.rishabh.gupta.sosmybuddy;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contacts extends Fragment {

    public static final int REQUEST_READ_PERMISSION = 123;

    static final int PICK_CONTACT = 1;
    FloatingActionButton fb;
    RecyclerView rv;
    ArrayList<Contacts_class> contactlist;
    Adapter_Contacts adapter_contacts;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public Contacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Contacts");
        firebaseAuth = FirebaseAuth.getInstance();
        fb = view.findViewById(R.id.fbadd);
        rv = view.findViewById(R.id.rv_contact);

        contactlist = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS
                    , Manifest.permission.WRITE_CONTACTS}, REQUEST_READ_PERMISSION);

        }
        else {
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
            });

            adapter_contacts = new Adapter_Contacts(contactlist, getContext());


//         adapter_contacts = new Adapter_Contacts(contactlist,getContext());
//        rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        rv.setAdapter(adapter_contacts);

/******************************************************/

            databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Contacts_class contacts_class = dataSnapshot.getValue(Contacts_class.class);
                    contactlist.add(contacts_class);

                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv.setAdapter(adapter_contacts);


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

        }
/*******************************************************/
        return view;
     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                String name = "";
                Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getActivity().getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
//                        name = phones.getString(phones.getColumnIndex
//                                (ContactsContract.DisplayNameSources.STRUCTURED_NAME)).replaceAll("[-() ]","");


                    }
                     name =       cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                    phones.close();
                    String Selected_name = name;
                    String Selected_number = number;
                    Log.e("TAG", "onActivityResult: "+Selected_name+"---"+Selected_number+"---" );
                    //Do something with number

                    Contacts_class contacts_class = new Contacts_class(Selected_name,Selected_number);

//                    contactlist.add(new Contacts_class(Selected_name,Selected_number));

//                     DatabaseReference databaseReference1 = databaseReference.child(firebaseAuth.getCurrentUser().getUid());
                    databaseReference.child(firebaseAuth.getCurrentUser().getPhoneNumber()).push().setValue(contacts_class);
//                    adapter_contacts.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }
}