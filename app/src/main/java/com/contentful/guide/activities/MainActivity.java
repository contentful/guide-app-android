package com.contentful.guide.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.contentful.guide.R;
import com.contentful.guide.adapters.MainAdapter;

/**
 * Main Activity class, holds a layout consisting of a {@link com.astuetz.PagerSlidingTabStrip}
 * View and a {@link android.support.v4.view.ViewPager}, to navigate between Fragments.
 */
public class MainActivity extends FragmentActivity {
    // Views
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // Init ViewPager & tabs
        viewPager.setAdapter(new MainAdapter(this, getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }
}
