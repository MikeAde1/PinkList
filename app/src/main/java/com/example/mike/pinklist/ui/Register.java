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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike.pinklist.R;
import com.example.mike.pinklist.utils.Appstatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private static final String TAG ="EmailPassword" ;

    FirebaseAuth mAuth;
    Typeface tp,t;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mconfPasswordField;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView tv = (TextView)findViewById(R.id.register);
        TextView ts= (TextView)findViewById(R.id.textView5);
        TextView tw= (TextView)findViewById(R.id.textView9);
        TextView te= (TextView)findViewById(R.id.textView7);
        TextView tx= (TextView)findViewById(R.id.textView8);
        TextView tt= (TextView)findViewById(R.id.textView4);

        //TextView ts= (TextView)findViewById(R.id.textView5);
        //for Register
        tp = Typeface.createFromAsset(getAssets(),"fonts/Effra_Std_Bd.ttf");
        tv.setTypeface(tp);
        // for other textViews
        t = Typeface.createFromAsset(getAssets(),"fonts/HelveticaLTStd-Fractions.otf");
        ts.setTypeface(t);
        tw.setTypeface(t);
        te.setTypeface(t);
        tx.setTypeface(t);
        tt.setTypeface(t);

        mAuth = FirebaseAuth.getInstance();
        //TextView ttx =(TextView)findViewById(R.id.textView9);
        //ttx.setPaintFlags(ttx.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        mEmailField= (EditText) findViewById(R.id.email2);
        mPasswordField= (EditText) findViewById(R.id.password);
        mconfPasswordField= (EditText) findViewById(R.id.confirmpassword);

    }
        private void createAccount(String email, String password ) {
            if (!validateForm()){
                return;}
            if(!confimationpassword()){
                return;
            }
        Log.d(TAG, "createAccount:" + email);
            if (Appstatus.getInstance(this).isOnline()) {
                progressDialog = ProgressDialog.show(this, "","Please Wait...", true);
                //Toast.makeText(this,"You are online!!!!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
                Log.v("Home", "############################You are not online!!!!");
            }
            //progressDialog = ProgressDialog.show(this, "","Please Wait...", true);
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Intent intent = new Intent(Register.this, To_do.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed."+ task.getException(),
                                    Toast.LENGTH_LONG).show();
                        }}
                    });
                }
    private boolean confimationpassword() {
        String password = mPasswordField.getText().toString();
        String confpassword = mconfPasswordField.getText().toString();
        boolean valid = true;
        if (confpassword.equals(password)){
            mconfPasswordField.setError(null);
                    } else{
            mconfPasswordField.setError("Password not the same");
            valid = false;
        }
        return valid;
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }
    public void onClick(View view) {
        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }
    public void toLogin(View view) {
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
    }
}



