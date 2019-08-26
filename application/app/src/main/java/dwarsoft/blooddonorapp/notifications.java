package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class notifications extends AppCompatActivity {

    RecyclerView recyclerView;
    String uid;
    notiadapter adapter;
    ArrayList<String> helplist = new ArrayList<>();
    ArrayList<String> contactlist = new ArrayList<>();
    String helppp,contactttt;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        helplist.clear();
        contactlist.clear();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("notifications/"+uid);
                    usersRef.orderByChild("help").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (final DataSnapshot postSnapshots: dataSnapshot.getChildren()) {
                                helppp = postSnapshots.child("help").getValue().toString();
                                contactttt = postSnapshots.child("contact").getValue().toString();
                                helplist.add(helppp);
                                contactlist.add(contactttt);
                                adapter = new notiadapter(helplist,contactlist);
                                recyclerView.setAdapter(adapter);
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
        recyclerView = (RecyclerView) findViewById(R.id.notificationrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(notifications.this));
        adapter = new notiadapter(helplist,contactlist);
        recyclerView.setAdapter(adapter);

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
