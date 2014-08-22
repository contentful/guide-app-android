package com.contentful.guide.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.contentful.guide.R;
import com.contentful.guide.fragments.GuideMapFragment;
import com.contentful.guide.fragments.PlacesFragment;

/**
 * A simple {@link android.support.v4.app.FragmentPagerAdapter} which currently
 * holds two items ({@link PlacesFragment}, {@link GuideMapFragment}).
 */
public class MainAdapter extends FragmentPagerAdapter {
    private final Context context;

    public MainAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PlacesFragment();

            case 1:
                return new GuideMapFragment();

            default:
                throw new IllegalStateException("Invalid adapter count");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_places);

            case 1:
                return context.getString(R.string.tab_map);

            default:
                throw new IllegalStateException("Invalid adapter count");
        }
    }
}
