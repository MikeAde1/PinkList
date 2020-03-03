package com.example.mike.pinklist.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mike.pinklist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.textView);
        Typeface tp = Typeface.createFromAsset(getAssets(),"fonts/Effra_Std_Bd.ttf");
        tv.setTypeface(tp);

       //TextView tx = (TextView) findViewById(R.id.textView);
        //Typeface custom_font= Typeface.createFromAsset(getAssets(), "fonts/Facile Sans.ttf ");
        //tx.setTypeface(custom_font);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
                }
                finally {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Intent i = new Intent(MainActivity.this, To_do.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    } else {
                        // No user is signed in
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);

                    }
                    }
                }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
