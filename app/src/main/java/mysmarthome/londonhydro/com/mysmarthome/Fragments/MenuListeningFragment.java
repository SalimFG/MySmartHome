package mysmarthome.londonhydro.com.mysmarthome.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlazaro66.wheelindicatorview.WheelIndicatorItem;
import com.dlazaro66.wheelindicatorview.WheelIndicatorView;

import java.util.ArrayList;
import java.util.List;

import mysmarthome.londonhydro.com.mysmarthome.HelperClasses.AnimationTextView;
import mysmarthome.londonhydro.com.mysmarthome.R;


public class MenuListeningFragment extends Fragment {


    View v;
    private ImageView mImgVwIcon;
    private TextView mTxtVwProgress;
    private WheelIndicatorView mWheelIndicator;
    private WheelIndicatorItem mWheelIndicatorItem;
    private TextView mTxtVwTimeSpent, mTxtVwDuration;

    private static final String ARG_PROGRESS = "prog";
    private static final String ARG_SUBJECT = "subject";
    private static final String ARG_POSITION = "position";
    private String mProgress;
    private String mSubject;
    int pagePosition = -1;



    public MenuListeningFragment() {
    }

    public static MenuListeningFragment newInstance(String param1,String param2,int pagePosition) {


        MenuListeningFragment f = new MenuListeningFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PROGRESS, param1);
        b.putString(ARG_SUBJECT, param2);
        b.putInt(ARG_POSITION, pagePosition);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProgress = getArguments().getString(ARG_PROGRESS);
            mSubject = getArguments().getString(ARG_SUBJECT);
            pagePosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.frag_menu_listening, container, false);

        mTxtVwProgress = (TextView)v.findViewById(R.id.txt_profil_progress);
        mImgVwIcon = (ImageView)v.findViewById(R.id.imgVwIcon);
        mWheelIndicator = (WheelIndicatorView)v.findViewById(R.id.wheel_indicator_view);

        mTxtVwTimeSpent = (TextView)v.findViewById(R.id.txtVwTimeSpentValue);
        mTxtVwDuration = (TextView)v.findViewById(R.id.txtVwAccuracyValue);

       /* mWheelIndicator.setFilledPercent(0);
        mWheelIndicator.notifyDataSetChanged();
        mWheelIndicator.clearAnimation();*/

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Set Progress
        mTxtVwProgress.setText(mProgress);



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Home Screen Swipe", "on start called....");
    }


    public void animateWithCountdown()
    {
        AnimationTextView th=new AnimationTextView(mTxtVwTimeSpent,Integer.valueOf(mTxtVwTimeSpent.getText().toString().trim()));
        AnimationTextView th2=new AnimationTextView(mTxtVwDuration,Integer.valueOf(mTxtVwDuration.getText().toString().trim()));
        th.start();
        th2.start();

        mWheelIndicator.clearAnimation();
        //Change imgae drawable
        if(pagePosition!=-1) {

            if (pagePosition == 1) {

                /*mWheelIndicator.setFilledPercent(0);
                mWheelIndicator.notifyDataSetChanged();*/


                mTxtVwTimeSpent.setText(getResources().getString(R.string.str_time_spent_listening_value));
                mTxtVwDuration.setText(getResources().getString(R.string.str_accuracy_listening_value));

                //SEt progress on Circle
                mWheelIndicator.setFilledPercent(100);
                mWheelIndicatorItem = new WheelIndicatorItem(0.7f,R.color.circle_color);

                //Set Values on Circle
                List<WheelIndicatorItem> listIndicat = new ArrayList<WheelIndicatorItem>();
                listIndicat.add(mWheelIndicatorItem);
                mWheelIndicator.setWheelIndicatorItems(listIndicat);
               // mWheelIndicator.notifyDataSetChanged();
                mWheelIndicator.startItemsAnimation();

                //Change Image drawable
                mImgVwIcon.setImageDrawable(getResources().getDrawable(R.drawable.img_mode_home));

            } else if (pagePosition == 0) {


                mTxtVwTimeSpent.setText(getResources().getString(R.string.str_time_spent_speaking_value));
                mTxtVwDuration.setText(getResources().getString(R.string.str_accuracy_speaking_value));

                //SEt progress on Circle
                mWheelIndicator.setFilledPercent(100);
                mWheelIndicatorItem = new WheelIndicatorItem(0.8f, R.color.circle_color);

                //Set Values on Circle
                List<WheelIndicatorItem> listIndicat = new ArrayList<WheelIndicatorItem>();
                listIndicat.add(mWheelIndicatorItem);
                mWheelIndicator.setWheelIndicatorItems(listIndicat);
                //mWheelIndicator.notifyDataSetChanged();
                mWheelIndicator.startItemsAnimation();

                //Change Image drawable
                mImgVwIcon.setImageDrawable(getResources().getDrawable(R.drawable.img_mode_away));

            }
        }
    }


}