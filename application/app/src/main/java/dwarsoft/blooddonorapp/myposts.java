package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myposts extends AppCompatActivity {

    RecyclerView recyclerView;
    mypostsadapter adapterr;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
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
    String address,age,bloodgroup,desc,name,phone,units,uid,postuid,city;
    String posttt,con;
    ArrayList<String> latlist = new ArrayList<>();
    ArrayList<String> lanlist = new ArrayList<>();
    String lat,lan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myposts);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                    postid.clear();
                    countrylist.clear();
                    citylist.clear();
                    namelist.clear();
                    desclist.clear();
                    bloodlist.clear();
                    agelist.clear();
                    addresslist.clear();
                    phonelist.clear();
                    unitlist.clear();
                    recyclerView = (RecyclerView) findViewById(R.id.mypostsrecycler);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(myposts.this));
                    adapterr = new mypostsadapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,postid,citylist,countrylist,latlist,lanlist);
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
                                postuid = postSnapshots.child("id").getValue().toString();
                                city = postSnapshots.child("city").getValue().toString();
                                con = postSnapshots.child("country").getValue().toString();
                                posttt = postSnapshots.getKey();
                                lat = postSnapshots.child("latitude").getValue().toString();
                                lan = postSnapshots.child("longitude").getValue().toString();


                                if(uid.equals(postuid))
                                {
                                    postid.add(posttt);
                                    latlist.add(lat);
                                    lanlist.add(lan);
                                    namelist.add(name);
                                    desclist.add(desc);
                                    bloodlist.add(bloodgroup);
                                    agelist.add(age);
                                    addresslist.add(address);
                                    phonelist.add(phone);
                                    unitlist.add(units);
                                    citylist.add(city);
                                    countrylist.add(con);
                                    adapterr = new mypostsadapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,postid,citylist,countrylist,latlist,lanlist);
                                    recyclerView.setAdapter(adapterr);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
