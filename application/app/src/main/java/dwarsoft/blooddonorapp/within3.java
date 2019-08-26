package dwarsoft.blooddonorapp;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class within3 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback{

    String uid,city;
    TextView tv2;
    SeekBar sk;

    int result = 0;
    RecyclerView recyclerView;
    within3adapter adapterr;
    FirebaseAuth mAuth;
    String cityyy;
    FirebaseAuth.AuthStateListener mAuthListener;
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



    private final static int PLAY_SERVICES_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    double latitude;
    double longitude;
    String currentlat,currentlon;
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    ArrayList<String> permissions=new ArrayList<>();
    private final static int REQUEST_CHECK_SETTINGS = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_within3);

        tv2 = (TextView) findViewById(R.id.textView2);
        sk=(SeekBar) findViewById(R.id.seekBar);

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Toast.makeText(within3.this, String.valueOf(seekBar.getProgress()+" KM"), Toast.LENGTH_SHORT).show();
                getLocation();
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress(String.valueOf(seekBar.getProgress()));
                } else {
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

            }
        });


        ButterKnife.bind(this);
        permissionUtils = new PermissionUtils(within3.this);
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();

                }
                else
                {

                }
            }
        };


        namelist.clear();
        latlist.clear();
        lonlist.clear();
        desclist.clear();
        bloodlist.clear();
        agelist.clear();
        addresslist.clear();
        phonelist.clear();
        unitlist.clear();
        recyclerView = (RecyclerView) findViewById(R.id.within3rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(within3.this));
        adapterr = new within3adapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
        recyclerView.setAdapter(adapterr);

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



    private void getLocation() {

        if (isPermissionGranted) {

            try
            {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }

        }

    }


    public void getAddress(final String saa)
    {

        Address locationAddress=getAddress(latitude,longitude);

        if(locationAddress!=null)
        {
            String currentLocation;
            String addressass = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            if(!TextUtils.isEmpty(addressass))
            {
                currentLocation=addressass;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+="\n"+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;

                tv2.setText(currentLocation);
            }

            result = 0;
            namelist.clear();
            latlist.clear();
            lonlist.clear();
            desclist.clear();
            bloodlist.clear();
            agelist.clear();
            addresslist.clear();
            phonelist.clear();
            unitlist.clear();
            recyclerView = (RecyclerView) findViewById(R.id.within3rec);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(within3.this));
            adapterr = new within3adapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
            recyclerView.setAdapter(adapterr);

            currentlat = Double.toString(locationAddress.getLatitude());
            currentlon =Double.toString(locationAddress.getLongitude());
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("posts");
            usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot postSnapshots: dataSnapshot.getChildren()) {

                        lat = postSnapshots.child("latitude").getValue().toString();
                        lon = postSnapshots.child("longitude").getValue().toString();
                        address = postSnapshots.child("address").getValue().toString();
                        age = postSnapshots.child("age").getValue().toString();
                        bloodgroup = postSnapshots.child("bloodgroup").getValue().toString();
                        desc = postSnapshots.child("desc").getValue().toString();
                        name = postSnapshots.child("name").getValue().toString();
                        phone = postSnapshots.child("phone").getValue().toString();
                        units = postSnapshots.child("units").getValue().toString();
                        lat = postSnapshots.child("latitude").getValue().toString();
                        lon = postSnapshots.child("longitude").getValue().toString();
                        String add = postSnapshots.child("address").getValue().toString();
                        Location loc1 = new Location("");
                        loc1.setLatitude(Double.parseDouble(currentlat));
                        loc1.setLongitude(Double.parseDouble(currentlon));

                        Location loc2 = new Location("");
                        loc2.setLatitude(Double.parseDouble(lat));
                        loc2.setLongitude(Double.parseDouble(lon));
                        float distanceInMeters = loc1.distanceTo(loc2);

                        int a = Integer.parseInt(saa);
                        a = a * 1000;
                        if (distanceInMeters<a)
                        {
                            namelist.add(name);
                            desclist.add(desc);
                            bloodlist.add(bloodgroup);
                            agelist.add(age);
                            addresslist.add(address);
                            phonelist.add(phone);
                            unitlist.add(units);
                            latlist.add(lat);
                            lonlist.add(lon);
                            adapterr = new within3adapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
                            recyclerView.setAdapter(adapterr);
                            result = result + 1;
                        }
                        else
                        {
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            
            

        }

    }

    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }



    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(within3.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions







    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void heree(View view) {

        getLocation();
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            getAddress("3");
        } else {

            Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
    }

    public void citydata(View view) {

        getLocation();
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            getAddresss();
        } else {

            Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
    }










    public void getAddresss()
    {

        Address locationAddress=getAddresss(latitude,longitude);

        if(locationAddress!=null)
        {

            String currentLocation;
            String addressass = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String cityay = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            if(!TextUtils.isEmpty(addressass))
            {
                currentLocation=addressass;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(cityay))
                {
                    currentLocation+="\n"+cityay;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;

                tv2.setText(currentLocation);
            }


            namelist.clear();
            latlist.clear();
            lonlist.clear();
            desclist.clear();
            bloodlist.clear();
            agelist.clear();
            addresslist.clear();
            phonelist.clear();
            unitlist.clear();
            recyclerView = (RecyclerView) findViewById(R.id.within3rec);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(within3.this));
            adapterr = new within3adapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
            recyclerView.setAdapter(adapterr);
            city = locationAddress.getLocality();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("posts");
            usersRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot postSnapshots: dataSnapshot.getChildren()) {

                        lat = postSnapshots.child("latitude").getValue().toString();
                        lon = postSnapshots.child("longitude").getValue().toString();
                        address = postSnapshots.child("address").getValue().toString();
                        age = postSnapshots.child("age").getValue().toString();
                        bloodgroup = postSnapshots.child("bloodgroup").getValue().toString();
                        desc = postSnapshots.child("desc").getValue().toString();
                        name = postSnapshots.child("name").getValue().toString();
                        phone = postSnapshots.child("phone").getValue().toString();
                        units = postSnapshots.child("units").getValue().toString();
                        lat = postSnapshots.child("latitude").getValue().toString();
                        lon = postSnapshots.child("longitude").getValue().toString();
                        cityyy = postSnapshots.child("city").getValue().toString();
                        if (city.equals(cityyy))
                        {
                            namelist.add(name);
                            desclist.add(desc);
                            bloodlist.add(bloodgroup);
                            agelist.add(age);
                            addresslist.add(address);
                            phonelist.add(phone);
                            unitlist.add(units);
                            latlist.add(lat);
                            lonlist.add(lon);
                            adapterr = new within3adapter(namelist,desclist,bloodlist,agelist,addresslist,phonelist,unitlist,latlist,lonlist);
                            recyclerView.setAdapter(adapterr);
                        }
                        else
                        {
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    public Address getAddresss(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }



}
