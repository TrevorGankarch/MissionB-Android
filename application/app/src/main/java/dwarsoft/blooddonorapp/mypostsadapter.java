package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Chetan on 1/3/18.
 */

public class mypostsadapter extends RecyclerView.Adapter<mypostsadapter.ViewHolder> {


    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> desclist = new ArrayList<>();
    ArrayList<String> bloodlist = new ArrayList<>();
    ArrayList<String> agelist = new ArrayList<>();
    ArrayList<String> addresslist = new ArrayList<>();
    ArrayList<String> phonelist = new ArrayList<>();
    ArrayList<String> unitlist = new ArrayList<>();
    ArrayList<String> postid = new ArrayList<>();
    ArrayList<String> citylist = new ArrayList<>();
    ArrayList<String> countrylist = new ArrayList<>();
    ArrayList<String> latlist = new ArrayList<>();
    ArrayList<String> lanlist = new ArrayList<>();

    public mypostsadapter(ArrayList<String> namee,ArrayList<String> descc,ArrayList<String> bloodd,ArrayList<String> agee,ArrayList<String> address,ArrayList<String> phone,ArrayList<String> unit,ArrayList<String> postid,ArrayList<String> citylist,ArrayList<String> countrylist,ArrayList<String> latlist,ArrayList<String> lanlist){
        this.namelist = namee;
        this.desclist = descc;
        this.bloodlist = bloodd;
        this.agelist = agee;
        this.addresslist = address;
        this.phonelist = phone;
        this.unitlist = unit;
        this.postid = postid;
        this.citylist = citylist;
        this.countrylist = countrylist;
        this.latlist = latlist;
        this.lanlist = lanlist;
    }

    @Override
    public mypostsadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postslayout, parent, false);
        return new mypostsadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final mypostsadapter.ViewHolder holder, final int position) {

        holder.name.setText("Name: "+namelist.get(position));
        holder.desc.setText("Desc: "+desclist.get(position));
        holder.bloodgroup.setText("BG: "+bloodlist.get(position));
        holder.age.setText("Age: "+agelist.get(position));
        holder.address.setText("Address: "+addresslist.get(position));
        holder.phone.setText("Phone: "+phonelist.get(position));
        holder.unit.setText("Units: "+unitlist.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(holder.itemView.getContext(), notify.class);
                intentt.putExtra("postid", postid.get(position));
                intentt.putExtra("city", citylist.get(position));
                intentt.putExtra("country", countrylist.get(position));
                intentt.putExtra("name", namelist.get(position));
                intentt.putExtra("lat", latlist.get(position));
                intentt.putExtra("lan", lanlist.get(position));
                v.getContext().startActivity(intentt);
            }
        });


    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,desc,age,bloodgroup,address,phone,unit;

        public ViewHolder(View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.recphone);
            unit = itemView.findViewById(R.id.recunits);
            name = itemView.findViewById(R.id.recname);
            desc = itemView.findViewById(R.id.recdesc);
            age = itemView.findViewById(R.id.recage);
            bloodgroup = itemView.findViewById(R.id.recblood);
            address = itemView.findViewById(R.id.recaddress);
        }
    }


}
