package dwarsoft.blooddonorapp;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback {
    private Geocoder mGeocoder;
    private static final int MY_REQUEST_INT = 177;
    private GoogleMap mMap;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String uid, usermail;
    DatabaseReference databaseref;
    String markerlat, marketlon, markername, markerphone, markerblood, markerquantity, markerdesc, markerid, markertype;
    double ml, mll;
    int PLACE_PICKER_REQUEST = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentt = new Intent(getBaseContext(), Main2Activity.class);
                    startActivity(intentt);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intenttt = new Intent(getBaseContext(), profile.class);
                    startActivity(intenttt);
                    return true;
                case R.id.navigation_request:
                    Intent intent = new Intent(getBaseContext(), requesttest.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_view:
                    Intent inte = new Intent(getBaseContext(), within3.class);
                    startActivity(inte);
                    return true;
                case R.id.navigation_notifications:
                    Intent intentttt = new Intent(getBaseContext(), alerts.class);
                    startActivity(intentttt);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment1(Fragment fragment, String tag_name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, fragment, tag_name);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                    usermail = user.getEmail();
                    databaseref = FirebaseDatabase.getInstance().getReference();
                    databaseref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("profile/" + uid) && dataSnapshot.hasChild("profile/" + uid + "/uid") && dataSnapshot.hasChild("profile/" + uid + "/name") && dataSnapshot.hasChild("profile/" + uid + "/name") && dataSnapshot.hasChild("profile/" + uid + "/age") && dataSnapshot.hasChild("profile/" + uid + "/gender") && dataSnapshot.hasChild("profile/" + uid + "/bloodgroup") && dataSnapshot.hasChild("profile/" + uid + "/image") && dataSnapshot.hasChild("profile/" + uid + "/phone")) {

                            } else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = database.getReference();
                                databaseReference.child("profile/" + uid + "/uid").setValue(uid);
                                databaseReference.child("profile/" + uid + "/mail").setValue(usermail);
                                databaseReference.child("profile/" + uid + "/name").setValue("");
                                databaseReference.child("profile/" + uid + "/age").setValue("");
                                databaseReference.child("profile/" + uid + "/gender").setValue("");
                                databaseReference.child("profile/" + uid + "/bloodgroup").setValue("Not Selected");
                                databaseReference.child("profile/" + uid + "/image").setValue("https://firebasestorage.googleapis.com/v0/b/blood-59479.appspot.com/o/adduser.jpeg?alt=media&token=77559529-3f16-4273-8798-0c6cfd3341c7");
                                databaseReference.child("profile/" + uid + "/phone").setValue("");
                                databaseReference.child("profile/" + uid + "/address").setValue("Click here to set address");

                                databaseReference.child("profile/" + uid + "/country").setValue("");
                                databaseReference.child("profile/" + uid + "/city").setValue("");
                                databaseReference.child("profile/" + uid + "/latitude").setValue("");
                                databaseReference.child("profile/" + uid + "/longitude").setValue("");
                                databaseReference.child("profile/" + uid + "/type").setValue("user");

                                Intent intent = new Intent(getBaseContext(), profile.class);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(Main2Activity.this, "Login again!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Location loc1 = new Location("");
        loc1.setLatitude(13.1032374);
        loc1.setLongitude(80.2752873);

        Location loc2 = new Location("");
        loc2.setLatitude(13.098529);
        loc2.setLongitude(80.2784948);

        float distanceInMeters = loc1.distanceTo(loc2);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(12.9104668, 80.2228809));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        MapsInitializer.initialize(this);
        addCustomMarker();
    }

    private void addCustomMarker() {
        if (mMap == null) {
            return;
        }


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("posts");
        usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot postSnapshots : dataSnapshot.getChildren()) {

                    markerlat = postSnapshots.child("latitude").getValue().toString();
                    marketlon = postSnapshots.child("longitude").getValue().toString();
                    markername = postSnapshots.child("name").getValue().toString();
                    markerphone = postSnapshots.child("phone").getValue().toString();
                    markerblood = postSnapshots.child("bloodgroup").getValue().toString();
                    markerquantity = postSnapshots.child("units").getValue().toString();
                    markerdesc = postSnapshots.child("desc").getValue().toString();
                    markerid = postSnapshots.getKey();
                    ml = Double.parseDouble(markerlat);
                    mll = Double.parseDouble(marketlon);

                    LatLng lattlng = new LatLng(ml, mll);

                    markertype = postSnapshots.child("type").getValue().toString();

                      Map<LatLng, String> mMarkers = new HashMap<>();

                    if (mMarkers.containsKey(lattlng))
                    {
                        //display marker with + button for the users to know there are more details there
                        Toast.makeText(Main2Activity.this, "There is a marker already", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //display normal marker with the detail
                        mMarkers.put(lattlng,markername);
                    }

                    if (markertype.equals("bloodbank"))
                    {
                        LatLng latlng = new LatLng(ml, mll);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .title(markername)
                                .snippet("Blood group:" + markerblood + "\n" + "Units:" + markerquantity + "\n" + "Desc:" + markerdesc + "\n" + "Contact:" + markerphone)
                                .position(latlng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        );
                        marker.setTag(markerid);
                    }
                    else
                    {
                        LatLng latlng = new LatLng(ml, mll);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .title(markername)
                                .snippet("Blood group:" + markerblood + "\n" + "Units:" + markerquantity + "\n" + "Desc:" + markerdesc + "\n" + "Contact:" + markerphone)
                                .position(latlng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        );
                        marker.setTag(markerid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getBaseContext(), showdetails.class);
                intent.putExtra("postid", (CharSequence) marker.getTag());
                startActivity(intent);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {


                 Place place = PlacePicker.getPlace(this,data);
                Geocoder geocoder = new Geocoder(this,Locale.getDefault());
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String country = addresses.get(0).getCountryName();

                    //String country = addresses.get(0).getAddressLine(2);

                } catch (IOException e)
                {

                    e.printStackTrace();
                }









            }










        }






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


    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void hi(View view) {
        Intent intentt = new Intent(getBaseContext(), social.class);
        startActivity(intentt);
    }

    public void piccc(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

}
