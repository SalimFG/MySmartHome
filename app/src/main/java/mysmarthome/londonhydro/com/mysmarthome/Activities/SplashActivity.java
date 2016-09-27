package mysmarthome.londonhydro.com.mysmarthome.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.Constants;
import mysmarthome.londonhydro.com.mysmarthome.R;


public class SplashActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);

                    SharedPreferences sharedPref = getSharedPreferences("mySmartHome",Context.MODE_WORLD_READABLE);
                    int isFirstUse =sharedPref.getInt(Constants.philips_is_integrated,0);
                    Log.e("fistUse",isFirstUse+"");
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);

                }

                catch (InterruptedException e) {

                    e.printStackTrace();
                }

                finally {
                    finish();
                }
            }
        };

        logoTimer.start();
    }

}
