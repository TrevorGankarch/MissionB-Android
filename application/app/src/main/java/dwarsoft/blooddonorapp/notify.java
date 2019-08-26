package dwarsoft.blooddonorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notify extends AppCompatActivity {

    String postid,city,country,name,buid,lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        postid = getIntent().getStringExtra("postid");
        city = getIntent().getStringExtra("city");
        country = getIntent().getStringExtra("country");
        name = getIntent().getStringExtra("name");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lan");
    }

    public void allblood(View view) {

        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("country/"+country+"/"+city+"/bloodbank");
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buid = dataSnapshot.child("uid").getValue().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.child("notify/"+buid+"/msg").setValue("You have been requested to donate blood to "+name+" Click to View more.");
                databaseReference.child("notify/"+buid+"/postid").setValue(postid);
                Toast.makeText(notify.this, "Notified!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void alluser(View view) {

        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("country/"+country+"/"+city+"/user");
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buid = dataSnapshot.child("uid").getValue().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.child("notify/"+buid+"/msg").setValue("You have been requested to donate blood to "+name+" Click to View more.");
                databaseReference.child("notify/"+buid+"/postid").setValue(postid);
                Toast.makeText(notify.this, "Notified!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void within3km(View view) {

    }
}
