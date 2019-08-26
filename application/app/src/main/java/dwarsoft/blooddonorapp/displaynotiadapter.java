package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chetan on 1/3/18.
 */

public class displaynotiadapter extends RecyclerView.Adapter<displaynotiadapter.ViewHolder> {

    ArrayList<String> msglist = new ArrayList<>();
    ArrayList<String> postlist = new ArrayList<>();

    public displaynotiadapter(ArrayList<String> msg,ArrayList<String> post){
        this.msglist = msg;
        this.postlist = post;
    }

    @Override
    public displaynotiadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.displayrowq, parent, false);
        return new displaynotiadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final displaynotiadapter.ViewHolder holder, final int position) {

        holder.name.setText(msglist.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), showdetails.class);
                intent.putExtra("postid", postlist.get(position));
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textdisplay);
        }
    }

}
