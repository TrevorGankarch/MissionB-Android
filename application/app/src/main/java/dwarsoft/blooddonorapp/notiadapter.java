package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chetan on 25/2/18.
 */

public class notiadapter extends RecyclerView.Adapter<notiadapter.ViewHolder> {

    ArrayList<String> helplist = new ArrayList<>();
    ArrayList<String> contactlist = new ArrayList<>();

    public notiadapter(ArrayList<String> help,ArrayList<String> contact){
        this.helplist = help;
        this.contactlist = contact;
    }



    @Override
    public notiadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notirow, parent, false);
        return new notiadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final notiadapter.ViewHolder holder, final int position) {

        holder.helpp.setText(helplist.get(position));
        holder.contactt.setText(contactlist.get(position));
    }

    @Override
    public int getItemCount() {
        return helplist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView helpp,contactt;

        public ViewHolder(View itemView) {
            super(itemView);
            helpp = itemView.findViewById(R.id.help);
            contactt = itemView.findViewById(R.id.contact);
        }
    }
}
