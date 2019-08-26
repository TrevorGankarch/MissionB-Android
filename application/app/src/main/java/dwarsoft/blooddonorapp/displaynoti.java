package dwarsoft.blooddonorapp;

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

public class displaynoti extends AppCompatActivity {

    RecyclerView recyclerView;
    displaynotiadapter adapterr;
    ArrayList<String> msglist = new ArrayList<>();
    ArrayList<String> postlist = new ArrayList<>();
    String po,msg,uid;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaynoti);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                    msglist.clear();
                    postlist.clear();
                    recyclerView = (RecyclerView) findViewById(R.id.displayno);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(displaynoti.this));
                    adapterr = new displaynotiadapter(msglist,postlist);
                    recyclerView.setAdapter(adapterr);



                    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("notify/"+uid);
                    databaseref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("msg") && dataSnapshot.hasChild("postid"))
                            {
                                msg = dataSnapshot.child("msg").getValue().toString();
                                po = dataSnapshot.child("postid").getValue().toString();
                                msglist.add(msg);
                                postlist.add(po);
                                adapterr = new displaynotiadapter(msglist,postlist);
                                recyclerView.setAdapter(adapterr);
                            }
                            else
                            {

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
