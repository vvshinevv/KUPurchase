package com.example.user.kupurchase1.Purchase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by user on 2016-03-09.
 */
public class MainPurchaseAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;
    KUPurchaseFragment kuPurchaseFragment;
    ETCPurchaseFragment etcPurchaseFragment;

    public MainPurchaseAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                kuPurchaseFragment = new KUPurchaseFragment();
                return kuPurchaseFragment;

            case 1:
                 etcPurchaseFragment = new ETCPurchaseFragment();
                return etcPurchaseFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
