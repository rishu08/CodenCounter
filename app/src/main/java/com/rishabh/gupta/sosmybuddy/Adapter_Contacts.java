package com.rishabh.gupta.sosmybuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_Contacts extends RecyclerView.Adapter<Adapter_Contacts.ViewHolder> {

    ArrayList<Contacts_class> contactlist;
    Context ctx;

    public Adapter_Contacts(ArrayList<Contacts_class> contactlist, Context ctx) {
        this.contactlist = contactlist;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_contacts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Contacts_class currentcontact = contactlist.get(position);

        holder.name.setText(currentcontact.getName());
        holder.number.setText(currentcontact.getNumber());

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Contacts").child(firebaseAuth.getCurrentUser().getPhoneNumber());
//
//
//                final String temp2 = currentcontact.getNumber();
////                Log.e("TAG", "onLongClick:-------------"+databaseReference.getKey() );
//
//
//                AlertDialog alertDialog = new AlertDialog.Builder(ctx)
//                        .setTitle("Are you sure to Delete this Note")
//                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                databaseReference.child(temp2).removeValue();
//
//                                contactlist.remove(position);
//                                notifyDataSetChanged();
//
//                                Toast.makeText(ctx, "Note Deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(ctx, "Delete Canceled", Toast.LENGTH_SHORT).show();
//                            }
//                        }).create();
//
//
//                alertDialog.show();
//                return false;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return contactlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
{

    TextView name , number;

    public ViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tv_name);
        number = itemView.findViewById(R.id.tv_number);
    }
}
}
