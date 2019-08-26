package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }.start();

    }
}
