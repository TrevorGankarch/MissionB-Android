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
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    String mail,pass;
    EditText email,password;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.loginemail);
        password = (EditText) findViewById(R.id.loginpass);
        bar = (ProgressBar) findViewById(R.id.empty_progress_bar);
        bar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                    startActivity(intent);
                    Toast.makeText(login.this, "Automatically Logged in", Toast.LENGTH_SHORT).show();
                }
                else {
                }
            }
        };

    }

    public void loginuser(View view) {
        mail = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)){

            bar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(login.this, "Success", Toast.LENGTH_SHORT).show();
                        bar.setVisibility(View.GONE);
                        Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                        startActivity(intent);
                    }
                    else {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(login.this,"UserName/Password entered is wrong!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Enter Email-ID and Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset(View view) {
        mail = email.getText().toString().trim();
        if(!TextUtils.isEmpty(mail))
        {
            bar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Reset Password Mail has been Sent!", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Enter Email-ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
