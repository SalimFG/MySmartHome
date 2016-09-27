package mysmarthome.londonhydro.com.mysmarthome.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mysmarthome.londonhydro.com.mysmarthome.Fragments.MenuListeningFragment;


public class AdapterHomePage extends FragmentPagerAdapter {

    private int pagerCount = 2;


    public AdapterHomePage(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return MenuListeningFragment.newInstance("Home", "Home", 0);

            case 1:
                return MenuListeningFragment.newInstance("Away", "Away", 1);

            default:
                return null;



        }
    }

    @Override public int getCount() {
        return pagerCount;
    }
}