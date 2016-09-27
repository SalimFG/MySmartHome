package mysmarthome.londonhydro.com.mysmarthome.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.belkin.wemo.localsdk.WeMoDevice;
import com.belkin.wemo.localsdk.WeMoSDKContext;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nestlabs.sdk.Callback;
import com.nestlabs.sdk.GlobalUpdate;
import com.nestlabs.sdk.NestAPI;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;
import com.nestlabs.sdk.NestToken;
import com.nestlabs.sdk.Structure;
import com.nestlabs.sdk.Thermostat;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import mysmarthome.londonhydro.com.mysmarthome.Adapters.AdapterHomePage;
import mysmarthome.londonhydro.com.mysmarthome.CustomViews.OpensansSemiBoldFont;
import mysmarthome.londonhydro.com.mysmarthome.DevicesAsyncThreads.AsyncDevicesOff;
import mysmarthome.londonhydro.com.mysmarthome.DevicesAsyncThreads.AsyncDevicesOn;
import mysmarthome.londonhydro.com.mysmarthome.Fragments.MenuListeningFragment;
import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.Constants;
import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.NestSettings;
import mysmarthome.londonhydro.com.mysmarthome.R;

public class MainActivity extends AppCompatActivity implements android.app.FragmentManager.OnBackStackChangedListener {


    private ViewPager customViewpager;
    int pageValue = -1;
    public int positionOfPager;
    private FloatingActionButton fab_nest_thermostat;

    //Nest variables

    private NestAPI mNest;
    private NestToken mToken;
    private Thermostat mThermostat;
    private Structure mStructure;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;
    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final String THERMOSTAT_KEY = "thermostat_key";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_HOME = "home";
    private static final String DEG_F = "%d°F";
    private static final String DEG_C = "%d°C";
    public OpensansSemiBoldFont txt_nest_therm_current_value;
    public Bundle savedInstanceState;
    public static final String TAG="Nest";
    public LinearLayout lay_philips_switch,lay_philips_add;


    // Philips Variables
    public LinearLayout lay_add_nest_thermostat,lay_details_nest_thermostat;

    //Flip Card variables
    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;

    // FrameLayouts
    FrameLayout lay_flip_nest,lay_flip_philips,lay_flip_wemo,lay_flip_echo;


//    // Wear
//    TeleportClient mTeleportClient;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        savedInstanceState=savedInstance;
        initViews();

       // mTeleportClient = new TeleportClient(this);

        initNest(savedInstance);

        new AsyncDevicesOn().execute("");

        devicesIntents();
        // Flips Card
        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.

            //change NEST
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.lay_flip_nest, new CardFrontFragment("nest"))
                    .commit();

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.lay_flip_philips, new CardFrontFragment("philips"))
                    .commit();

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.lay_flip_wemo, new CardFrontFragment("wemo"))
                    .commit();

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.lay_flip_echo, new CardFrontFragment("echo"))
                    .commit();

        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        // Monitor back stack changes to ensure the action bar shows the appropriate
        // button (either "photo" or "info").
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    private void initNest(Bundle savedInstanceState){
        NestAPI.setAndroidContext(this);
        mNest = NestAPI.getInstance();
        mToken = NestSettings.loadAuthToken(this);
        if (mToken != null) {
            authenticateNest(mToken);
            Log.e("salim","nest is connected");
            lay_details_nest_thermostat.setVisibility(View.VISIBLE);
            lay_add_nest_thermostat.setVisibility(View.GONE);

            // show the thermostat
        } else {
            mNest.setConfig(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
            mNest.launchAuthFlow(this, AUTH_TOKEN_REQUEST_CODE);
            Log.e("salim","nest is first use");
//            lay_details_nest_thermostat.setVisibility(View.GONE);
//            lay_add_nest_thermostat.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState != null) {
            mThermostat = savedInstanceState.getParcelable(THERMOSTAT_KEY);
            mStructure = savedInstanceState.getParcelable(STRUCTURE_KEY);
            updateViews();
        }
    }

    private void initViews() {

        // ViewPager
        customViewpager = (ViewPager) findViewById(R.id.viewpager_custom);
        CircleIndicator customIndicator = (CircleIndicator) findViewById(R.id.indicator_custom);
        AdapterHomePage customPagerAdapter = new AdapterHomePage(getSupportFragmentManager());
        customViewpager.setAdapter(customPagerAdapter);
        customIndicator.setViewPager(customViewpager);

        //Nest
        fab_nest_thermostat = (FloatingActionButton) findViewById(R.id.fab_nest_thermostat);
       // txt_nest_therm_current_value= (OpensansSemiBoldFont) findViewById(R.id.txt_nest_therm_current_value);
        lay_add_nest_thermostat=(LinearLayout)findViewById(R.id.lay_add_nest_thermostat);
        lay_details_nest_thermostat=(LinearLayout)findViewById(R.id.lay_details_nest_thermostat);
        lay_details_nest_thermostat=(LinearLayout)findViewById(R.id.lay_details_nest_thermostat);

        // view pager things
        customViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int i, float v, int i2) {


                if(customViewpager.getCurrentItem()==i)
                {
                    MenuListeningFragment profileFragment = (MenuListeningFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager_custom + ":" + customViewpager.getCurrentItem());

                    if(profileFragment!=null)
                    {
                        profileFragment.animateWithCountdown();
                    }
                }
            }

            @Override
            public void onPageSelected(int i) {

                positionOfPager = i;
                Log.e("salim", "value : " + i);
                String awayState = mStructure.getAway();

                switch (i) {
                    case 0: {

                        new AsyncDevicesOn().execute("");


                        if (KEY_AUTO_AWAY.equals(awayState) || KEY_AWAY.equals(awayState))
                            {
                                awayState = KEY_HOME;
                            }
                            pageValue = 80;

                            break;

                    }
                    case 1: {
                       // flipCardToTheBack();
                        if (KEY_HOME.equals(awayState)) {
                            {
                                awayState = KEY_AWAY;
                            }

                            new AsyncDevicesOff().execute("");

                            pageValue = 40;

                            break;
                        }
                    }
                }

                // change the away mode of nest
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

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        fab_nest_thermostat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initNest(savedInstanceState);
            }
        });
    }


    private void devicesIntents(){

        lay_flip_nest=(FrameLayout)findViewById(R.id.lay_flip_nest);
        lay_flip_philips=(FrameLayout)findViewById(R.id.lay_flip_philips);
        lay_flip_wemo=(FrameLayout)findViewById(R.id.lay_flip_wemo);
        lay_flip_echo=(FrameLayout)findViewById(R.id.lay_flip_echo);

        lay_flip_nest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NestThermostatActivity.class));

            }
        });

        lay_flip_wemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WemoActivity.class));

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("*****Home Activity", "on Resume called....");
    }

    @Override
    protected void onStart() {
        Log.i("*****Home Activity", "on start called....");
        super.onStart();
       // mTeleportClient.connect();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        try {
            mNest.removeAllListeners();
        }catch (Exception ex){
            Log.e("NEST","couldn't remove nest listener");
        }
        //mTeleportClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResumeFragments() {
        Log.i("*****Home Activity", "onResumeFragments called....");
        super.onResumeFragments();
    }

    //nest authentification
    private void authenticateNest(NestToken token) {
        mNest.authWithToken(token, new NestListener.AuthListener() {

            @Override
            public void onAuthSuccess() {
                Log.v(TAG, "Authentication succeeded.");
                lay_details_nest_thermostat.setVisibility(View.VISIBLE);
                lay_add_nest_thermostat.setVisibility(View.GONE);
                fetchData();
            }

            @Override
            public void onAuthFailure(NestException exception) {
                Log.e(TAG, "Authentication failed with error: " + exception.getMessage());

                NestSettings.saveAuthToken(MainActivity.this, null);
                mNest.launchAuthFlow(MainActivity.this, AUTH_TOKEN_REQUEST_CODE);
            }

            @Override
            public void onAuthRevoked() {
                Log.e(TAG, "Auth token was revoked!");
                NestSettings.saveAuthToken(MainActivity.this, null);
                mNest.launchAuthFlow(MainActivity.this, AUTH_TOKEN_REQUEST_CODE);
            }
        });
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
            authenticateNest(mToken);
        } else {
            Log.e(TAG, "Unable to resolve access token from payload.");
        }
    }

    // nest fetch data
    private void fetchData() {
        mNest.addGlobalListener(new NestListener.GlobalListener() {
            @Override
            public void onUpdate(@NonNull GlobalUpdate update) {
                mStructure = update.getStructures().get(0);
                updateViews();
                mThermostat = update.getThermostats().get(0);
            }
        });
    }

    // nest update view
    private void updateViews() {
        if (mStructure == null || mThermostat == null) {
            return;
        }
//        txt_nest_therm_current_value.setText(String.format(DEG_C, mThermostat.getTargetTemperatureC()));

    }

    public static class CardFrontFragment extends Fragment {
        public String type_lay;

        public CardFrontFragment(String device) {
            type_lay = device;
        }

        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View cont = inflater.inflate(R.layout.fragment_card_front, container, false);
            ImageView img_front = (ImageView) cont.findViewById(R.id.img_home_mode);

            switch (type_lay) {
                case "nest": {
                    img_front.setImageResource(R.drawable.nest_thermostat);break;
                }
                case "philips": {
                    img_front.setImageResource(R.drawable.philips);break;
                }
                case "wemo": {
                    img_front.setImageResource(R.drawable.blanc);break;
                }
                case "echo": {
                    img_front.setImageResource(R.drawable.echo);break;
                }

            }
            return cont;
        }

    }

    public static class CardBackFragment extends Fragment {
        public String type_lay;

        public CardBackFragment(String device) {
            type_lay = device;
        }

        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View cont = inflater.inflate(R.layout.fragment_card_back, container, false);
            ImageView img_back = (ImageView) cont.findViewById(R.id.img_away_mode);
            switch (type_lay) {
                case "nest": {
                    img_back.setImageResource(R.drawable.nest_thermostat);break;
                }
                case "philips": {
                    img_back.setImageResource(R.drawable.philips);break;
                }
                case "wemo": {
                    img_back.setImageResource(R.drawable.blanc);break;
                }
                case "echo": {
                    img_back.setImageResource(R.drawable.echo);break;
                }

            }
            return cont;
        }
    }

    //Flip Cards

   private void flipCardToTheBack() {
                if (mShowingBack) {
                    getFragmentManager().popBackStack();
                    return;
                }

                // Flip to the back.

                mShowingBack = true;

                // Create and commit a new fragment transaction that adds the fragment for the back of
                // the card, uses custom animations, and is part of the fragment manager's back stack.

                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                        .replace(R.id.lay_flip_nest, new CardBackFragment("nest"))
                        .addToBackStack(null)
                        .commit();

       getFragmentManager()
               .beginTransaction()
               .setCustomAnimations(
                       R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                       R.animator.card_flip_left_in, R.animator.card_flip_left_out)
               .replace(R.id.lay_flip_philips, new CardBackFragment("philips"))
               .addToBackStack(null)
               .commit();

       getFragmentManager()
               .beginTransaction()
               .setCustomAnimations(
                       R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                       R.animator.card_flip_left_in, R.animator.card_flip_left_out)
               .replace(R.id.lay_flip_wemo, new CardBackFragment("wemo"))
               .addToBackStack(null)
               .commit();

       getFragmentManager()
               .beginTransaction()
               .setCustomAnimations(
                       R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                       R.animator.card_flip_left_in, R.animator.card_flip_left_out)
               .replace(R.id.lay_flip_echo, new CardBackFragment("echo"))
               .addToBackStack(null)
               .commit();
            }


   @Override
   public void onBackStackChanged() {
                mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);


            }


//    public void syncDataItem(View v) {
//        //Let's sync a String!
//        mTeleportClient.syncString("hello", "Hello, World!");
//    }

        }
