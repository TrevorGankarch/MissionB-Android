package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class alerts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
    }

    public void status(View view) {
        Intent intentt = new Intent(getBaseContext(), notifications.class);
        startActivity(intentt);
    }

    public void send(View view) {
        Intent intentt = new Intent(getBaseContext(), myposts.class);
        startActivity(intentt);
    }

    public void receive(View view) {
        Intent intentt = new Intent(getBaseContext(), displaynoti.class);
        startActivity(intentt);
    }
}
