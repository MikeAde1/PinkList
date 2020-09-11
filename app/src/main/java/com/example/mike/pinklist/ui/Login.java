package com.example.mike.pinklist.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.pinklist.R;
import com.example.mike.pinklist.utils.Appstatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String TAG = "email";
    private EditText emails;
    private EditText passwords;
    Button button;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(this);
        TextView tx = (TextView) findViewById(R.id.login);
        Typeface tp = Typeface.createFromAsset(getAssets(),"fonts/Effra_Std_Bd.ttf");
        tx.setTypeface(tp);
        TextView t = (TextView) findViewById(R.id.text_View5);
        TextView te = (TextView) findViewById(R.id.text_view4);
        TextView tt = (TextView) findViewById(R.id.textView6);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/HelveticaLTStd-Fractions.otf");
        t.setTypeface(tf);
        te.setTypeface(tf);
        tt.setTypeface(tf);

        //tx.setPaintFlags(tx.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mAuth = FirebaseAuth.getInstance();
        emails = (EditText) findViewById(R.id.email);
        passwords = (EditText) findViewById(R.id.password2);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emails.getText().toString(), passwords.getText().toString());
                //For Realtime Database
            }
        });
    }
    // Write a message to the database
    private void signIn(String email, String password) {
        if (Appstatus.getInstance(this).isOnline()) {
            pd = ProgressDialog.show(this, "","Please Wait...", true);
        } else {
            Toast.makeText(this,"You are not online!",Toast.LENGTH_SHORT).show();
            Log.v("Home", "############################You are not online!!!!");
        }
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (!task.isSuccessful()){
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{   //for LOGIN
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                Intent intent = new Intent(Login.this, To_do.class);
                                startActivity(intent);
                                //finish();
                        }}
                });
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = emails.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emails.setError("Required.");
            valid = false;
        } else {
            emails.setError(null);
        }
        String password = passwords.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwords.setError("Required.");
            valid = false;
        } else {
            passwords.setError(null);
        }
        return valid;
    }
 /*   public void onClickLogin(View v) {
        signIn(emails.getText().toString(), passwords.getText().toString());
        Intent intent = new Intent(Login.this, To_do.class);
        startActivity(intent);
    }*/
    public void onClickForgot(View v) {
        Intent intent = new Intent(Login.this, Forgot.class);
        startActivity(intent);
    }
    public void toRegister(View v) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

}
