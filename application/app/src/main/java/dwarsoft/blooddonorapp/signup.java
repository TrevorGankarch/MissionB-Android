package dwarsoft.blooddonorapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    EditText email, pass, reenterpass;
    FirebaseAuth mAuth;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        reenterpass = (EditText) findViewById(R.id.passrere);
        bar = (ProgressBar) findViewById(R.id.empty_progress_bar);
        bar.setVisibility(View.GONE);
    }

    public void register(View view) {
        if (pass.getText().toString().equals(reenterpass.getText().toString())) {

            bar.setVisibility(View.VISIBLE);

            if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())) {
                Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                            Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            bar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "User Name exist in our database/the password is too weak!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else
            {
                bar.setVisibility(View.GONE);
                Toast.makeText(this, "Please Enter Email-ID and Password", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            bar.setVisibility(View.GONE);
            Toast.makeText(this, "Password and re-entered password did not match", Toast.LENGTH_SHORT).show();
        }
    }
}
