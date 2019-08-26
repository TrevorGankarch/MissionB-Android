package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Chetan on 24/2/18.
 */

public class postadapter extends RecyclerView.Adapter<postadapter.ViewHolder> {

    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> desclist = new ArrayList<>();
    ArrayList<String> bloodlist = new ArrayList<>();
    ArrayList<String> agelist = new ArrayList<>();
    ArrayList<String> addresslist = new ArrayList<>();
    ArrayList<String> phonelist = new ArrayList<>();
    ArrayList<String> unitlist = new ArrayList<>();
    ArrayList<String> latlist = new ArrayList<>();
    ArrayList<String> lonlist = new ArrayList<>();

    public postadapter(ArrayList<String> namee,ArrayList<String> descc,ArrayList<String> bloodd,ArrayList<String> agee,ArrayList<String> address,ArrayList<String> phone,ArrayList<String> unit,ArrayList<String> lat,ArrayList<String> lon){
        this.namelist = namee;
        this.desclist = descc;
        this.bloodlist = bloodd;
        this.agelist = agee;
        this.addresslist = address;
        this.phonelist = phone;
        this.unitlist = unit;
        this.latlist = lat;
        this.lonlist = lon;
    }

    @Override
    public postadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText("Name: "+namelist.get(position));
        holder.desc.setText("Desc: "+desclist.get(position));
        holder.bloodgroup.setText("BG: "+bloodlist.get(position));
        holder.age.setText("Age: "+agelist.get(position));
        holder.address.setText("Address: "+addresslist.get(position));
        holder.phone.setText("Phone: "+phonelist.get(position));
        holder.unit.setText("Units: "+unitlist.get(position));

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Help! Blood required!"+"\n"+namelist.get(position)+" requires "+unitlist.get(position)+" units of blood."+"\n"+"Blood group: "+bloodlist.get(position)+"\n"+"Contact at "+phonelist.get(position));
                sendIntent.setType("text/plain");
                v.getContext().startActivity(sendIntent);
            }
        });

        holder.direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "http://maps.google.com/maps?daddr=" + Double.valueOf(latlist.get(position)) + "," + Double.valueOf(lonlist.get(position)) + " (" + namelist.get(position) + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                v.getContext().startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,desc,age,bloodgroup,address,phone,unit;
        Button share,direction;

        public ViewHolder(View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.recphone);
            unit = itemView.findViewById(R.id.recunits);
            name = itemView.findViewById(R.id.recname);
            desc = itemView.findViewById(R.id.recdesc);
            age = itemView.findViewById(R.id.recage);
            bloodgroup = itemView.findViewById(R.id.recblood);
            address = itemView.findViewById(R.id.recaddress);
            share = itemView.findViewById(R.id.recshare);
            direction = itemView.findViewById(R.id.recdirection);
        }
    }
}

