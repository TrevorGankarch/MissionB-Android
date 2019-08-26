package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class postrecycler extends AppCompatActivity {

    RecyclerView recyclerView;
    postadapter adapterr;
    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> desclist = new ArrayList<>();
    ArrayList<String> bloodlist = new ArrayList<>();
    ArrayList<String> agelist = new ArrayList<>();
    ArrayList<String> addresslist = new ArrayList<>();
    ArrayList<String> phonelist = new ArrayList<>();
    ArrayList<String> unitlist = new ArrayList<>();
    ArrayList<String> latlist = new ArrayList<>();
    ArrayList<String> lonlist = new ArrayList<>();
    String address,age,bloodgroup,desc,name,phone,units,lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postrecycler);
        namelist.clear();
        latlist.clear();
        lonlist.clear();
        desclist.clear();
        bloodlist.clear();
        agelist.clear();
        addresslist.clear();
        phonelist.clear();
        unitlist.clear();
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(postrecycler.this));
        adapterr = new postadapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
        recyclerView.setAdapter(adapterr);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("posts");
        usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot postSnapshots: dataSnapshot.getChildren()) {

                    address = postSnapshots.child("address").getValue().toString();
                    age = postSnapshots.child("age").getValue().toString();
                    bloodgroup = postSnapshots.child("bloodgroup").getValue().toString();
                    desc = postSnapshots.child("desc").getValue().toString();
                    name = postSnapshots.child("name").getValue().toString();
                    phone = postSnapshots.child("phone").getValue().toString();
                    units = postSnapshots.child("units").getValue().toString();
                    lat = postSnapshots.child("latitude").getValue().toString();
                    lon = postSnapshots.child("longitude").getValue().toString();

                    namelist.add(name);
                    desclist.add(desc);
                    bloodlist.add(bloodgroup);
                    agelist.add(age);
                    addresslist.add(address);
                    phonelist.add(phone);
                    unitlist.add(units);
                    latlist.add(lat);
                    lonlist.add(lon);
                    adapterr = new postadapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
                    recyclerView.setAdapter(adapterr);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,Main2Activity.class));
    }
}
