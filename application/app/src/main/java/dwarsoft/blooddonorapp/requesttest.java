package dwarsoft.blooddonorapp;

import android.*;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.ButterKnife;

public class requesttest extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    ProgressBar bar;
    String addressnew,citynew,countrynew;
    Spinner dropdown,dropdown1;
    int PLACE_PICKER_REQUEST = 1;
    String type;
    TextView addressdisplay;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    private Location mLastLocation;

    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    // list of permissions
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;

    EditText postname,postage,units,phone,description;
    TextView address;
    String name,age,unit,phoneno,desc,add,uid,gender,bloodgroup;
    String timeStamp;
    String lat;
    String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesttest);

        bar = (ProgressBar) findViewById(R.id.empty_progress_bar);
        bar.setVisibility(View.GONE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        timeStamp = sdf.format(new Date());
        postname = (EditText) findViewById(R.id.postname);
        postage =  (EditText) findViewById(R.id.postage);
        units = (EditText) findViewById(R.id.units);
        phone = (EditText) findViewById(R.id.phone);
        description = (EditText) findViewById(R.id.description);
        address = (TextView) findViewById(R.id.address);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference("profile/"+uid);
                    databaseref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            type = dataSnapshot.child("type").getValue().toString();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    Toast.makeText(requesttest.this, "Please login again", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        addressdisplay = (TextView) findViewById(R.id.address);
        ButterKnife.bind(this);
        permissionUtils=new PermissionUtils(requesttest.this);
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        dropdown = (Spinner) findViewById(R.id.bloodspinner);
        String[] items = new String[]{"Not Selected","O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                bloodgroup = item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        dropdown1 = findViewById(R.id.genderradio);
        String[] itemss = new String[]{"Male","Female"};
        final ArrayAdapter<String> adapterr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemss);
        dropdown1.setAdapter(adapterr);

        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                gender = item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void currentlocation(View view) {
        getLocation();
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            getAddress();

        } else {
            Toast.makeText(this, "Couldn't get the location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
        }
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

    public void getAddress()
    {

        Address locationAddress=getAddress(latitude,longitude);

        if(locationAddress!=null)
        {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();



            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

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

                addressdisplay.setText(currentLocation);
            }

        }

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
                            status.startResolutionForResult(requesttest.this, REQUEST_CHECK_SETTINGS);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this,data);
                    Geocoder geocoder = new Geocoder(this,Locale.getDefault());
                    try
                    {
                        List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                        addressnew = addresses.get(0).getAddressLine(0);
                        citynew = addresses.get(0).getLocality();
                        countrynew = addresses.get(0).getCountryName();
                        lat = String.valueOf(addresses.get(0).getLatitude());
                        lon = String.valueOf(addresses.get(0).getLongitude());
                        addressdisplay.setText(addressnew);

                    } catch (IOException e)
                    {

                        e.printStackTrace();
                    }
                }
                break;
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

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }




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

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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


    public void postbloodrequest(View view) {

        name = postname.getText().toString();
        age = postage.getText().toString();
        unit = units.getText().toString();
        phoneno = phone.getText().toString();
        desc = description.getText().toString();
        add = address.getText().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(unit) && !TextUtils.isEmpty(phoneno) && !TextUtils.isEmpty(desc))
        {
            bar.setVisibility(View.VISIBLE);
            if (add.equals("PLEASE CLICK ON GET YOUR CURRENT LOCATION"))
            {
                bar.setVisibility(View.GONE);
                Toast.makeText(this, "Please click on Get Location", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //actual function here
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.child("posts/"+uid+timeStamp+"/name").setValue(name);
                databaseReference.child("posts/"+uid+timeStamp+"/age").setValue(age);
                databaseReference.child("posts/"+uid+timeStamp+"/gender").setValue(gender);
                databaseReference.child("posts/"+uid+timeStamp+"/bloodgroup").setValue(bloodgroup);
                databaseReference.child("posts/"+uid+timeStamp+"/units").setValue(unit);
                databaseReference.child("posts/"+uid+timeStamp+"/phone").setValue(phoneno);
                databaseReference.child("posts/"+uid+timeStamp+"/desc").setValue(desc);
                databaseReference.child("posts/"+uid+timeStamp+"/address").setValue(add);
                databaseReference.child("posts/"+uid+timeStamp+"/latitude").setValue(lat);
                databaseReference.child("posts/"+uid+timeStamp+"/longitude").setValue(lon);
                databaseReference.child("posts/"+uid+timeStamp+"/id").setValue(uid);

                databaseReference.child("posts/"+uid+timeStamp+"/type").setValue(type);

                databaseReference.child("posts/"+uid+timeStamp+"/city").setValue(citynew);
                databaseReference.child("posts/"+uid+timeStamp+"/country").setValue(countrynew);


                Toast.makeText(this, "Requested!", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.GONE);
                Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                startActivity(intent);
            }

        }
        else
        {
            Toast.makeText(this, "Please fill in required details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,Main2Activity.class));
    }

    public void assigntext(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }
}
