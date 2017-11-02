package com.touchclick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop;
    private SharedPreferences pref;
    private SharedPreferences.Editor mEditor;

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
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = pref.edit();
        mEditor.putInt("count",0);
        mEditor.apply();
        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(AirTouchJNI.mainFromJNI());
        init();
    }

    public void init(){
        btnStart = (Button)findViewById(R.id.start);
        btnStart.setOnClickListener(mClickListener);

        btnStop = (Button)findViewById(R.id.stop);
        btnStop.setOnClickListener(mClickListener);
    }
    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(MainActivity.this, AirTouchService.class);

            int id = arg0.getId();
            switch (id) {
                case R.id.start:
                    startService(intent);
                    Toast.makeText(MainActivity.this, "startService", Toast.LENGTH_SHORT)
                         .show();
                    break;
                case R.id.stop:
                    stopService(intent);
                    Toast.makeText(MainActivity.this, "stopService", Toast.LENGTH_SHORT)
                         .show();
                    break;

            }
        }
    };



}
