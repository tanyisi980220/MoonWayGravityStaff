package com.example.moonwaygravitystaff.Adapter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class staffPagerAdapter extends FragmentPagerAdapter {
    int NumOfTabs;


    private Context mContext;

    public staffPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
//        if(position == 0){
//            return new UserProfileFragment();
//        }else if (position == 1) {
//            return new ChatMessaging();
//        }else{
//            return new ParkingInformationFragment();
//        }
        return null;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
//        // Generate title based on item position
//        switch (position) {
//            case 0:
//                return mContext.getString(R.string.user_profile);
//            case 1:
//                return mContext.getString(R.string.customer_support);
//            case 2:
//                return mContext.getString(R.string.parking_information);
//            default:
//                return null;
//        }
        return null;
    }
}