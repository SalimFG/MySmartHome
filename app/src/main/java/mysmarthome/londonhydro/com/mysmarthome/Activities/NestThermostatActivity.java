
package mysmarthome.londonhydro.com.mysmarthome.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nestlabs.sdk.Callback;
import com.nestlabs.sdk.GlobalUpdate;
import com.nestlabs.sdk.NestAPI;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;
import com.nestlabs.sdk.NestToken;
import com.nestlabs.sdk.Structure;
import com.nestlabs.sdk.Thermostat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.ColorArcProgressBar;
import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.Constants;
import mysmarthome.londonhydro.com.mysmarthome.CustomViews.FloatingEditText;
import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.NestSettings;
import mysmarthome.londonhydro.com.mysmarthome.R;

public class NestThermostatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = NestThermostatActivity.class.getSimpleName();
    private static final String THERMOSTAT_KEY = "thermostat_key";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_HOME = "home";
    private static final String KEY_HEAT = "heat";
    private static final String KEY_COOL = "cool";
    private static final String KEY_HEAT_COOL = "heat-cool";
    private static final String KEY_OFF = "off";
    private static final String DEG_F = "%d°F";
    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;

    private TextView mAmbientTempText;
    private View mSingleControlContainer;
    private TextView mCurrentTempText;
    private TextView mStructureNameText;
    private View mThermostatView;
    private View mRangeControlContainer;
    private TextView mCurrentCoolTempText;
    private TextView mCurrentHeatTempText;
    private Button mStructureAway;
    private NestAPI mNest;
    private NestToken mToken;
    private Thermostat mThermostat;
    private Structure mStructure;
    private Activity mActivity;
    private Button btn_moin_salim;
    FloatingActionButton btn_plus_salim;
    private Thermostat salimThermostat;
    private FloatingEditText editTextTemp;
    private ColorArcProgressBar progress,progressCooling;
    public static String working;
    public boolean salimCooling=false;
    public boolean salimHeating=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_thermotat);

        getSupportActionBar().setBackgroundDrawable( new ColorDrawable(Color.parseColor("#01a185")));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#01a185"));


        mActivity = this;

        //mThermostatView = findViewById(R.id.thermostat_view);
//        mSingleControlContainer = findViewById(R.id.single_control);
        mCurrentTempText = (TextView) findViewById(R.id.current_temp);
        mStructureNameText = (TextView) findViewById(R.id.structure_name);
        mAmbientTempText = (TextView) findViewById(R.id.ambient_temp);
        mStructureAway = (Button) findViewById(R.id.structure_away_btn);
     //   mRangeControlContainer = findViewById(R.id.range_control);
        mCurrentCoolTempText = (TextView) findViewById(R.id.current_cool_temp);
        mCurrentHeatTempText = (TextView) findViewById(R.id.current_heat_temp);
        btn_plus_salim=(FloatingActionButton)findViewById(R.id.btn_plus_salim);
        btn_moin_salim=(Button)findViewById(R.id.btn_moin_salim);
        editTextTemp=(FloatingEditText)findViewById(R.id.editTextTemp);
        progress=(ColorArcProgressBar)findViewById(R.id.bar2);
        progressCooling=(ColorArcProgressBar)findViewById(R.id.bar2cold);

        mStructureAway.setOnClickListener(this);
        findViewById(R.id.logout_button).setOnClickListener(this);
        findViewById(R.id.heat).setOnClickListener(this);
        findViewById(R.id.cool).setOnClickListener(this);
        findViewById(R.id.heat_cool).setOnClickListener(this);
        findViewById(R.id.off).setOnClickListener(this);


        NestAPI.setAndroidContext(this);
        mNest = NestAPI.getInstance();
        mToken = NestSettings.loadAuthToken(this);

        if (mToken != null) {
            authenticate(mToken);
        } else {
            mNest.setConfig(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
            mNest.launchAuthFlow(this, AUTH_TOKEN_REQUEST_CODE);
        }

        if (savedInstanceState != null) {
            mThermostat = savedInstanceState.getParcelable(THERMOSTAT_KEY);
            mStructure = savedInstanceState.getParcelable(STRUCTURE_KEY);
            updateViews();
        }

        //SalimTestcode

        mNest.addThermostatListener(new NestListener.ThermostatListener() {
            @Override
            public void onUpdate(@NonNull ArrayList<Thermostat> thermostats) {
                // Handle thermostat update...
                //Log.e("salim","iI have the thermostat");
                salimThermostat=thermostats.get(0);

            }
        });


        btn_moin_salim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get id from Thermostat#getDeviceId


                // The temperature in Celsius to set. (Note: type double)

                // Set thermostat target temp (in degrees C) with an optional success callback.
//                long currentTemp = salimThermostat.getTargetTemperatureF();
//                Log.e("salim","currentTemp"+currentTemp);
//                currentTemp-=1;



                working=editTextTemp.getText().toString();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        String thermostatId = salimThermostat.getDeviceId();
                        long newTemp= Long.parseLong(working);

                        // run AsyncTask here.
                        mNest.thermostats.setTargetTemperatureF(thermostatId, newTemp, new Callback() {
                            @Override
                            public void onSuccess() {
                                // The update to the thermostat succeeded.
                                Log.e("salim","changed to -1");
                            }

                            @Override
                            public void onFailure(NestException e) {
                                // The update to the thermostat failed.
                                Log.e("salim",e.getMessage());
                                Log.e("salim",e.getLocalizedMessage());
                                Log.e("salim","failed to -1");
                            }
                        });

                    }
                }, 360000);



            }
        });
        btn_plus_salim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                // Get id from Thermostat#getDeviceId

                String thermostatId = salimThermostat.getDeviceId();

                // The temperature in Celsius to set. (Note: type double)
                // Set thermostat target temp (in degrees C) with an optional success callback.
                long newTempx= Long.parseLong(editTextTemp.getText().toString());
                mNest.thermostats.setTargetTemperatureF(thermostatId, newTempx, new Callback() {
                    @Override
                    public void onSuccess() {
                        // The update to the thermostat succeeded.
                        Log.e("salim","changed to +1");
                    }

                    @Override
                    public void onFailure(NestException e) {
                        // The update to the thermostat failed.
                        Log.e("salim","failed to +1");

                        Log.e("salim",e.getMessage());

                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(THERMOSTAT_KEY, mThermostat);
        outState.putParcelable(STRUCTURE_KEY, mStructure);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK || requestCode != AUTH_TOKEN_REQUEST_CODE) {
            Log.e(TAG, "Finished with no result.");
            return;
        }

        mToken = NestAPI.getAccessTokenFromIntent(intent);
        if (mToken != null) {
            NestSettings.saveAuthToken(this, mToken);
            authenticate(mToken);
        } else {
            Log.e(TAG, "Unable to resolve access token from payload.");
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        mNest.removeAllListeners();
    }

    @Override
    public void onClick(View v) {
        if (mThermostat == null || mStructure == null) {
            return;
        }

        String thermostatID = mThermostat.getDeviceId();
        String mode = mThermostat.getHvacMode();
        String awayState = mStructure.getAway();
        long temp = mThermostat.getTargetTemperatureF();

        switch (v.getId()) {
//            case R.id.temp_up:
//                temp++;
//                mCurrentTempText.setText(String.format(DEG_F, temp));
//                mNest.thermostats.setTargetTemperatureF(mThermostat.getDeviceId(), temp);
//                break;
//            case R.id.temp_down:
//                temp--;
//                mCurrentTempText.setText(String.format(DEG_F, temp));
//                mNest.thermostats.setTargetTemperatureF(mThermostat.getDeviceId(), temp);
//                break;
//            case R.id.temp_heat_up:
//            case R.id.temp_heat_down:
//            case R.id.temp_cool_up:
//            case R.id.temp_cool_down:
//                if (KEY_HEAT_COOL.equals(mode)) {
//                    updateTempRange(v);
//                } else if (KEY_OFF.equals(mode)) {
//                    Log.d(TAG, "Cannot set temperature when HVAC mode is off.");
//                }
//                break;
            case R.id.heat:
                mNest.thermostats.setHVACMode(thermostatID, KEY_HEAT);
                salimHeating=true;
                break;
            case R.id.cool:
                mNest.thermostats.setHVACMode(thermostatID, KEY_COOL);
                salimCooling=true;

                break;
            case R.id.heat_cool:
                mNest.thermostats.setHVACMode(thermostatID, KEY_HEAT_COOL);
                break;
            case R.id.off:
                mNest.thermostats.setHVACMode(thermostatID, KEY_OFF);
                break;
            case R.id.structure_away_btn:
                if (KEY_AUTO_AWAY.equals(awayState) || KEY_AWAY.equals(awayState)) {
                    awayState = KEY_HOME;
                    mStructureAway.setText(R.string.away_state_home);
                } else if (KEY_HOME.equals(awayState)) {
                    awayState = KEY_AWAY;
                    mStructureAway.setText(R.string.away_state_away);
                } else {
                    return;
                }

                mNest.structures.setAway(mStructure.getStructureId(), awayState, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Successfully set away state.");
                    }

                    @Override
                    public void onFailure(NestException exception) {
                        Log.d(TAG, "Failed to set away state: " + exception.getMessage());
                    }
                });
                break;
            case R.id.logout_button:
                NestSettings.saveAuthToken(this, null);
                mNest.launchAuthFlow(this, AUTH_TOKEN_REQUEST_CODE);
                break;
        }

    }

    /**
     * Authenticate with the Nest API and start listening for updates.
     *
     * @param token the token used to authenticate.
     */
    private void authenticate(NestToken token) {
        mNest.authWithToken(token, new NestListener.AuthListener() {

            @Override
            public void onAuthSuccess() {
                Log.v(TAG, "Authentication succeeded.");
                fetchData();
            }

            @Override
            public void onAuthFailure(NestException exception) {
                Log.e(TAG, "Authentication failed with error: " + exception.getMessage());
                NestSettings.saveAuthToken(mActivity, null);
                mNest.launchAuthFlow(mActivity, AUTH_TOKEN_REQUEST_CODE);
            }

            @Override
            public void onAuthRevoked() {
                Log.e(TAG, "Auth token was revoked!");
                NestSettings.saveAuthToken(mActivity, null);
                mNest.launchAuthFlow(mActivity, AUTH_TOKEN_REQUEST_CODE);
            }
        });
    }

    /**
     * Setup global listener, start listening, and update view when update received.
     */
    private void fetchData() {
        mNest.addGlobalListener(new NestListener.GlobalListener() {
            @Override
            public void onUpdate(@NonNull GlobalUpdate update) {
                mThermostat = update.getThermostats().get(0);
                mStructure = update.getStructures().get(0);
                updateViews();
            }
        });
    }

    private void updateTempRange(View v) {
        String thermostatID = mThermostat.getDeviceId();
        long tempHigh = mThermostat.getTargetTemperatureHighF();
        long tempLow = mThermostat.getTargetTemperatureLowF();
//
//        switch (v.getId()) {
//            case R.id.temp_cool_down:
//                tempLow -= 1;
//                mNest.thermostats.setTargetTemperatureLowF(thermostatID, tempLow);
//                mCurrentCoolTempText.setText(String.format(DEG_F, tempLow));
//                break;
//            case R.id.temp_cool_up:
//                tempLow += 1;
//                mNest.thermostats.setTargetTemperatureLowF(thermostatID, tempLow);
//                mCurrentCoolTempText.setText(String.format(DEG_F, tempLow));
//                break;
//            case R.id.temp_heat_down:
//                tempHigh -= 1;
//                mNest.thermostats.setTargetTemperatureHighF(thermostatID, tempHigh);
//                mCurrentHeatTempText.setText(String.format(DEG_F, tempHigh));
//                break;
//            case R.id.temp_heat_up:
//                tempHigh += 1;
//                mNest.thermostats.setTargetTemperatureHighF(thermostatID, tempHigh);
//                mCurrentHeatTempText.setText(String.format(DEG_F, tempHigh));
//                break;
//        }
    }

    private void updateViews() {
        if (mStructure == null || mThermostat == null) {
            return;
        }

        updateAmbientTempTextView();
        updateStructureViews();
        updateThermostatViews();
    }

    private void updateAmbientTempTextView() {

        mAmbientTempText.setText(String.format(DEG_F, mThermostat.getAmbientTemperatureF()));
    }

    private void updateStructureViews() {
        mStructureNameText.setText(mStructure.getName());
        mStructureAway.setText(mStructure.getAway());
    }

    private void updateThermostatViews() {
//        int singleControlVisibility;
//        int rangeControlVisibility;
        String mode = mThermostat.getHvacMode();
        String state = mStructure.getAway();
        boolean isAway = state.equals(KEY_AWAY) || state.equals(KEY_AUTO_AWAY);

        if (isAway) {
//            singleControlVisibility = View.VISIBLE;
//            rangeControlVisibility = View.GONE;
            updateSingleControlView();
        } else if (KEY_HEAT_COOL.equals(mode)) {
//            singleControlVisibility = View.GONE;
//            rangeControlVisibility = View.VISIBLE;
            updateRangeControlView();
        } else if (KEY_OFF.equals(mode)) {
//            singleControlVisibility = View.VISIBLE;
//            rangeControlVisibility = View.GONE;
            //mCurrentTempText.setText(R.string.thermostat_off);
            mThermostatView.setBackgroundResource(R.drawable.off_thermostat_drawable);
        } else {
//            singleControlVisibility = View.VISIBLE;
//            rangeControlVisibility = View.GONE;
            updateSingleControlView();
        }

//        mSingleControlContainer.setVisibility(singleControlVisibility);
//        mRangeControlContainer.setVisibility(rangeControlVisibility);
    }

    private void updateRangeControlView() {
        int thermostatDrawable;
        long lowF = mThermostat.getTargetTemperatureLowF();
        long highF = mThermostat.getTargetTemperatureHighF();
        long ambientF = mThermostat.getAmbientTemperatureF();
        boolean isCooling = (highF - ambientF) < 0;
        boolean isHeating = (lowF - ambientF) > 0;

        if (isCooling) {
            thermostatDrawable = R.drawable.cool_thermostat_drawable;
        } else if (isHeating) {
            thermostatDrawable = R.drawable.heat_thermostat_drawable;
        } else {
            thermostatDrawable = R.drawable.off_thermostat_drawable;
        }

        // Update the view.
        mCurrentHeatTempText.setText(String.format(DEG_F, highF));
        mCurrentCoolTempText.setText(String.format(DEG_F, lowF));
        mThermostatView.setBackgroundResource(thermostatDrawable);
    }

    private void updateSingleControlView() {
        int thermDrawable = R.drawable.off_thermostat_drawable;
        long targetF = mThermostat.getTargetTemperatureF();
        long ambientF = mThermostat.getAmbientTemperatureF();
        long tempDiffF = targetF - ambientF;
        String state = mStructure.getAway();
        String mode = mThermostat.getHvacMode();



        // Update the view.


        long lowF = mThermostat.getAmbientTemperatureF();


        //long lowF = mThermostat.getTargetTemperatureLowF();
        long highF = mThermostat.getTargetTemperatureLowF();



        Log.e("salim","highF :"+highF+"");
        Log.e("salim","lowF :"+lowF+"");

//        progressCooling.setCurrentValues(lowF - 50 );
//        progress.setCurrentValues(targetF - 50 );

        if(mThermostat.getHvacMode().equals("cool")){
            Log.e("salim","isCooling");
            progressCooling.setCurrentValues(targetF - 50 );

        }else{
            if(mThermostat.getHvacMode().equals("heat"))
                Log.e("salim","isHeatings");
            progress.setCurrentValues(targetF - 50 );


        }
        long ambientFX = mThermostat.getAmbientTemperatureF();

//        mCurrentTempText.setText(String.format(DEG_F, targetF));
        Log.e("salim","current ! "+(String.format(DEG_F, targetF)));

//        mThermostatView.setBackgroundResource(thermDrawable);
    }


}
