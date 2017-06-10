package com.touchclick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    static{

        try {
            System.loadLibrary("airTouch");
        }
        catch (UnsatisfiedLinkError ule) {
            System.err.println("WARNING: Could not load lib3djni.so");
            ule.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //setContentView(R.layout.activity_main);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(AirTouchJNI.mainFromJNI());

        Intent intent = new Intent(this, AirTouchService.class);
        startService(intent);

    }


}
