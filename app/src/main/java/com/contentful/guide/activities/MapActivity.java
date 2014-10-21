package com.contentful.guide.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.contentful.guide.R;
import com.contentful.guide.fragments.GuideMapFragment;
import com.contentful.guide.model.Place;

/**
 * Map Activity class.
 */
public class MapActivity extends FragmentActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_map);

    Place place = (Place) getIntent().getSerializableExtra(InfoActivity.EXTRA_PLACE);

    getSupportFragmentManager().beginTransaction()
        .add(R.id.root_view, GuideMapFragment.newInstance(place), GuideMapFragment.class.getName())
        .commit();
  }
}
