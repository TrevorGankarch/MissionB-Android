package dwarsoft.blooddonorapp;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class showdetails extends AppCompatActivity {

    String postuid,postname,uidname,uidphone;

    TextView showblood,showname,showage,showunits,showaddress,showdescription,showphone;
    String postid;
    String uid;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String sblood,sname,sage,sunits,saddress,sphone,sdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdetails);
        showaddress = (TextView) findViewById(R.id.showaddress);
        showblood = (TextView) findViewById(R.id.showblood);
        showname = (TextView) findViewById(R.id.showname);
        showage = (TextView) findViewById(R.id.showage);
        showunits = (TextView) findViewById(R.id.showunits);
        showdescription = (TextView) findViewById(R.id.showdesc);
        showphone = (TextView) findViewById(R.id.showphone);
        postid = getIntent().getStringExtra("postid");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                }
                else {
                    Toast.makeText(showdetails.this, "Please login again", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("posts/"+postid);
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sname = dataSnapshot.child("name").getValue().toString();
                sage = dataSnapshot.child("age").getValue().toString();
                sdesc = dataSnapshot.child("desc").getValue().toString();
                saddress = dataSnapshot.child("address").getValue().toString();
                sunits = dataSnapshot.child("units").getValue().toString();
                sblood = dataSnapshot.child("bloodgroup").getValue().toString();
                sphone = dataSnapshot.child("phone").getValue().toString();

                showname.setText(sname);
                showaddress.setText(saddress);
                showage.setText(sage);
                showdescription.setText(sdesc);
                showunits.setText(sunits);
                showblood.setText(sblood);
                showphone.setText(sphone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void shareeee(View view) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Help! Blood required!"+"\n"+sname+" requires "+sunits+" units of blood."+"\n"+"Blood group: "+sblood+"\n"+"Contact at "+sphone);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void showhelp(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Donate Blood")
                .setMessage("You want to help?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        //code here
                        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("posts/"+postid);
                        databaseref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                postuid = dataSnapshot.child("id").getValue().toString();
                                //postuid requester
                                //uid     donor
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                if (uid.equals(postuid))
                                {
                                    Toast.makeText(showdetails.this, "Cannot request self!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    DatabaseReference databaseReference = database.getReference();
                                    databaseReference.child("notifications/"+uid+"/"+postid+"/help").setValue("You have requested to help "+sname+" for blood type "+sblood);
                                    databaseReference.child("notifications/"+uid+"/"+postid+"/contact").setValue("Contact at "+sphone);
                                    databaseReference.child("notifications/"+uid+"/"+postid+"/uid").setValue(uid);
                                    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("profile/"+uid);
                                    databaseref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            uidname = dataSnapshot.child("name").getValue().toString();
                                            uidphone = dataSnapshot.child("phone").getValue().toString();
                                            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference1 = database1.getReference();
                                            databaseReference1.child("notifications/"+postuid+"/"+postid+"/help").setValue("You have received help from "+uidname+" for blood type "+sblood);
                                            databaseReference1.child("notifications/"+postuid+"/"+postid+"/contact").setValue("Contact at "+uidphone);
                                            databaseReference1.child("notifications/"+postuid+"/"+postid+"/uid").setValue(postuid);
                                            Toast.makeText(showdetails.this, "Requested Successfully!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
}
