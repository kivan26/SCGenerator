package com.example.milan.scgenerator.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.milan.scgenerator.fragments.PageAll;
import com.example.milan.scgenerator.fragments.PageImageVideo;
import com.example.milan.scgenerator.fragments.PageSmsContacts;

/**
 * Created by milan on 18.11.15..
 */
public class PageAdapter extends FragmentPagerAdapter {
    /** Number of pages. */
    private static final int NUMBER_OF_PAGES = 5;
    /** Names of pages. */
    private static final String[] TABS_NAMES = {"All", "SMS", "Contacts", "Images", "Videos"};

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // All
            case 0:
                return new PageAll();

            // Sms and contact
            case 1:
            case 2:
                return PageSmsContacts.init(position);

            // Image and video
            case 3:
            case 4:
                return PageImageVideo.init(position);

            default:
                return new PageAll();
        }

    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS_NAMES[position];
    }



}
