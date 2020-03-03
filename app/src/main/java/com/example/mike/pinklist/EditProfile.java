package com.example.mike.pinklist;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mike.pinklist.store.SessionManager;
import com.example.mike.pinklist.models.ProfileEdit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    private String email, full_name;
    FirebaseDatabase db;
    DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;
    EditText etEmail,et;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressDialog =new ProgressDialog(getApplicationContext());
        db = FirebaseDatabase.getInstance();
        databaseRef = db.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.edit);
        et = (EditText) findViewById(R.id.fullname);

        Typeface t = Typeface.createFromAsset(getAssets(),"fonts/NotoSans-Bold.ttf");
        et.setTypeface(t);

        Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbar2);
        //mytoolbar.setTitle("EDIT PROFILE");
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_if_arrow_back_1063891);
        sessionManager = new SessionManager(getApplicationContext());
        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.fbb);
        fb.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                full_name = et.getText().toString();
                /*if (email.equals("") || TextUtils.isEmpty(email)){
                    etEmail.setError("Set current email or new email");
                    return;
                }
                else if(full_name.equals("") || TextUtils.isEmpty(email)){
                    etEmail.setError("Username is required");
                    return;
                }*/
                setProfile();
        }});
    }

    private void setEmail() {
                ProfileEdit p =new ProfileEdit();
                p.setEmail(email);
                p.setNames(full_name);
                databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("User_details").setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfile.this,"Updated",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error uploading", Toast.LENGTH_SHORT).show();
                            Log.d("problem", String.valueOf(task.getException()));
                        }
                    }
                });
            }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_logout:
                sessionManager.logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setProfile() {
        if (!haveNetworkConnection()) {
            Toast.makeText(getApplicationContext(),"Please connect to the internet",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!full_name.equals("") || !TextUtils.isEmpty(full_name)) {
            ProfileEdit p = new ProfileEdit();
            p.setNames(full_name);
            //save name
            databaseRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("User_details").setValue(p)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("user_name", full_name);
                            //save email
                            /*if (!email.equals("") || !TextUtils.isEmpty(email)) {
                                progressDialog.setMessage("Loading..");
                                progressDialog.show();
                                firebaseAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    //for email
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditProfile.this, "Success", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else {
                                progressDialog.dismiss();
                                finish();
                            }*/
                        }
                    }
                });
        }

        if (!email.equals("") || !TextUtils.isEmpty(email)){
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                firebaseAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    //for email
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        } else {
                            //or show a dialog that authenticates user
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }}
                    });
        }else {
            finish();
        }

        }
}

