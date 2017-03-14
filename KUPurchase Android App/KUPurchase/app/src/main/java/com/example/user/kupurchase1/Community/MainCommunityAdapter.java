package com.example.user.kupurchase1.Community;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by user on 2016-03-11.
 */
public class MainCommunityAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;
    NotificationFragment notificationFragment;
    FreeBoardFragment freeBoardFragment;

    public MainCommunityAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                notificationFragment = new NotificationFragment();
                return notificationFragment;

            case 1:
                freeBoardFragment = new FreeBoardFragment();
                return freeBoardFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
