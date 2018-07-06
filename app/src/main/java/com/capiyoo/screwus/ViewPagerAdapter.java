package com.capiyoo.screwus;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
           case 0:
                return new HomeFragment();
            case 1:
                return new BankGuide();
            case 2:
                return new Other();
           case 3:
                return new Other();
        /*      case 4:
                return new TestSeries();
            case 5:
                return new FeedFragment();
            case 6:
                return new FragmentDiscussion();
            case 7:
                return new FragmentStudyMaterial();*/
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Reviews";
            case 1:
                return "FeedBack";
            case 2:
                return "Other";
            case 3:
                return "Notification";

        }
        return null;
    }
}
