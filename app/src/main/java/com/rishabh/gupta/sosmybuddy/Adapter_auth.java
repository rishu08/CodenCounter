package com.rishabh.gupta.sosmybuddy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_auth extends RecyclerView.Adapter<Adapter_auth.ViewHolder> {

        ArrayList<Contacts_class> contactlist;
        Context ctx;
        String UID;



    public Adapter_auth(ArrayList<Contacts_class> contactlist, Context ctx, String UID) {
        this.contactlist = contactlist;
        this.ctx = ctx;
        this.UID = UID;
    }

    @NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_contacts,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

final Contacts_class currentcontact = contactlist.get(position);

        holder.name.setText(currentcontact.getName());
        holder.number.setText(currentcontact.getNumber());

    final String[] array = UID.split("/");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + currentcontact.getNumber()));
                //intent.putExtra("sms_body","Hi track my live location. My id is:\n"+array[array.length-1]);
                Log.e("TAG", "onClick: "+array[array.length-1].substring(3) );
                intent.putExtra("sms_body","Hi track my live location. My id is:\n"+"+"+array[array.length-1].substring(3));
                ctx.startActivity(intent);
            }
        });
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
