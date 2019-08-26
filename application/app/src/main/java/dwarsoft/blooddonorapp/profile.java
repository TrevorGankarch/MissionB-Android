package dwarsoft.blooddonorapp;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class profile extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {


    String genderrr;
    StorageReference storageReference;
    private static final int GALLERY_INTENT = 2;
    Uri uri;
    String ab,bc;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ImageView profimg;
    String namee,img;
    String lat,longi;
    ProgressBar bar;
    String type;
    int PLACE_PICKER_REQUEST = 1;

    String bloodgrouppp,genderrrr;

    String addressnew,countrynew,citynew;


    EditText name,age,phone;
    TextView addressss;
    Spinner dropdown,dropdown1,dropdown2;
    Spinner gender;
 //   RadioGroup gender;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String uid,blood,gen;
    String upname,upage,upphone,upaddress;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    String currentLocation;
    private Location mLastLocation;
    String typeee;
    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    // list of permissions
    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;

    String setname,setage,setphone,setaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        addressss = findViewById(R.id.profileaddress);

        bar = (ProgressBar) findViewById(R.id.empty_progress_bar);


        profimg = (ImageView) findViewById(R.id.profileimage);
        ButterKnife.bind(this);
        permissionUtils=new PermissionUtils(profile.this);
        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        dropdown = (Spinner) findViewById(R.id.profileblood);
        String[] items = new String[]{"Not Selected","O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                bloodgrouppp = (String) item;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dropdown1 = (Spinner) findViewById(R.id.profiletype);
        String[] itemss = new String[]{"user","bloodbank"};
        final ArrayAdapter<String> adapterr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemss);
        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                typeee = (String) item;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dropdown2 = findViewById(R.id.profilegender);
        String[] itemsss = new String[]{"Male","Female"};
        final ArrayAdapter<String> adapterrr = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsss);
        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                genderrrr = (String) item;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        name = (EditText) findViewById(R.id.profilename);
        age = (EditText) findViewById(R.id.profileage);
        phone = (EditText) findViewById(R.id.profilephone);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null)
                {
                    uid = user.getUid();
                    DatabaseReference databaseref123;
                    databaseref123 = FirebaseDatabase.getInstance().getReference("profile/"+uid);
                    databaseref123.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            blood = dataSnapshot.child("bloodgroup").getValue().toString();
                            gen = dataSnapshot.child("gender").getValue().toString();
                            type = dataSnapshot.child("type").getValue().toString();
                            countrynew = dataSnapshot.child("country").getValue().toString();
                            citynew = dataSnapshot.child("city").getValue().toString();
                            addressnew = dataSnapshot.child("address").getValue().toString();
                            ab = dataSnapshot.child("latitude").getValue().toString();
                            bc = dataSnapshot.child("longitude").getValue().toString();
                            img = dataSnapshot.child("image").getValue().toString();
                            Picasso.with(getApplicationContext())
                                    .load(img)
                                    .resize(100,100).into(profimg);
                            bar.setVisibility(View.GONE);

                            setname = dataSnapshot.child("name").getValue().toString();
                            setaddress = dataSnapshot.child("address").getValue().toString();
                            setage = dataSnapshot.child("age").getValue().toString();
                            setphone = dataSnapshot.child("phone").getValue().toString();
                            name.setText(setname);
                            addressss.setText(setaddress);
                            age.setText(setage);
                            phone.setText(setphone);
                            dropdown.setAdapter(adapter);
                            int spinnerPosition = adapter.getPosition(blood);
                            dropdown.setSelection(spinnerPosition);

                            dropdown1.setAdapter(adapterr);
                            int spinnerPosition1 = adapterr.getPosition(type);
                            dropdown1.setSelection(spinnerPosition1);

                            dropdown2.setAdapter(adapterrr);
                            int spinnerPosition11 = adapterrr.getPosition(gen);
                            dropdown2.setSelection(spinnerPosition11);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
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
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void updateprofile(View view) {
        upname = name.getText().toString();
        upaddress = addressss.getText().toString();
        upage = age.getText().toString();
        upphone = phone.getText().toString();
        if(!TextUtils.isEmpty(upname) && !TextUtils.isEmpty(upage) && !TextUtils.isEmpty(upphone))
        {
            bar.setVisibility(View.VISIBLE);
            if (upaddress.equals("Click here to set address"))
            {
                bar.setVisibility(View.GONE);
                Toast.makeText(this, "Please select the address", Toast.LENGTH_SHORT).show();
            }
            else
            {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.child("profile/"+uid+"/name").setValue(upname);
                databaseReference.child("profile/"+uid+"/age").setValue(upage);
                databaseReference.child("profile/"+uid+"/phone").setValue(upphone);
                databaseReference.child("profile/"+uid+"/address").setValue(upaddress);
                databaseReference.child("profile/"+uid+"/latitude").setValue(ab);
                databaseReference.child("profile/"+uid+"/longitude").setValue(bc);


                databaseReference.child("profile/"+uid+"/gender").setValue(genderrrr);
                databaseReference.child("profile/"+uid+"/type").setValue(typeee);
                databaseReference.child("profile/"+uid+"/bloodgroup").setValue(bloodgrouppp);

                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1 = database1.getReference();
                databaseReference1.child("country/"+countrynew+"/"+citynew+"/"+type+"/name").setValue(upname);
                databaseReference1.child("country/"+countrynew+"/"+citynew+"/"+type+"/phone").setValue(upphone);
                databaseReference1.child("country/"+countrynew+"/"+citynew+"/"+type+"/address").setValue(upaddress);
                databaseReference1.child("country/"+countrynew+"/"+citynew+"/"+type+"/latitude").setValue(ab);
                databaseReference1.child("country/"+countrynew+"/"+citynew+"/"+type+"/longitude").setValue(bc);
                databaseReference1.child("country/"+countrynew+"/"+citynew+"/"+type+"/uid").setValue(uid);

                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference2 = database2.getReference();
                databaseReference2.child("profile/"+uid+"/country").setValue(countrynew);
                databaseReference2.child("profile/"+uid+"/city").setValue(citynew);
                databaseReference2.child("profile/"+uid+"/address").setValue(addressnew);

                Toast.makeText(this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                bar.setVisibility(View.GONE);
            }
        }
        else
        {
            Toast.makeText(this, "Please enter details!", Toast.LENGTH_SHORT).show();
        }
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
            lat = Double.toString(locationAddress.getLatitude());
            longi =Double.toString(locationAddress.getLongitude());

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

                addressss.setText(currentLocation);
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
                            status.startResolutionForResult(profile.this, REQUEST_CHECK_SETTINGS);

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


        switch(requestCode){
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
                        LatLng latLng = place.getLatLng();
                        Double a = latLng.latitude;
                        Double b = latLng.longitude;
                        ab = String.valueOf(a);
                        bc = String.valueOf(b);

                        //String country = addresses.get(0).getAddressLine(2);

                        addressss.setText(addressnew);

                    } catch (IOException e)
                    {

                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
                    uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        Bitmap a = Bitmap.createScaledBitmap(bitmap,400,400,false);
                        // Bitmap a = resize(bitmap,400,362);
                        uri = getImageUri(this,a);
                        profimg.setImageBitmap(a);
                        namee = "file:///" + compressImage(uri.toString());
                        storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference filepath = storageReference.child("profile/"+uid);
                        bar.setVisibility(View.VISIBLE);
                        filepath.putFile(Uri.parse(namee)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                bar.setVisibility(View.GONE);
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                storageRef.child("profile/"+uid).getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri urilll) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference databaseReference = database.getReference();
                                                databaseReference.child("profile/"+uid+"/image").setValue(urilll.toString());

                                            }
                                        });
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2000:
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
                break;
        }
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

    public void profilelogout(View view) {
        mAuth.signOut();
        Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addimage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            startActivityForResult(intent, GALLERY_INTENT);
        }
    }

    public void piccc(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }


    public class getBitmap extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bit = null;
            if (params.length == 0)
                throw new IllegalArgumentException("You should pass me a url!!");
            final String src = params[0];
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bit = BitmapFactory.decodeStream(input);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bit;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap !=null){
                profimg.setImageBitmap(bitmap);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "FamilyConnect/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,Main2Activity.class));
    }
}
