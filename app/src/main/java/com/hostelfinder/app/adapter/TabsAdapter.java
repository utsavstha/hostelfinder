package com.hostelfinder.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hostelfinder.app.fragments.HostelImageFragment;
import com.hostelfinder.app.fragments.HostelInformationFragment;
import com.hostelfinder.app.fragments.HostelLocationFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    String[] title = new String[]{"Information", "Image", "Location"};
    public TabsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return HostelInformationFragment.newInstance();
        }else if (position == 1){
            return HostelImageFragment.newInstance();
        }else{
            return HostelLocationFragment.newInstance();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
