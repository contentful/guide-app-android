package com.contentful.guide.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contentful.guide.CFUtils;
import com.contentful.guide.CustomCallback;
import com.contentful.guide.R;
import com.contentful.guide.activities.InfoActivity;
import com.contentful.guide.adapters.PlacesAdapter;
import com.contentful.guide.model.Place;
import com.contentful.java.cda.model.CDAArray;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Places Fragment class, holds a layout consisting of a {@link android.widget.ListView}.
 * Upon creation, an instance of this class will make a request to the Delivery API in order
 * to fetch all Entries associated with the current Space (defined in {@link CFUtils} class).
 * This also makes use of the {@link CustomCallback} class to illustrate how one could
 * provide a generic way to deal with common errors.
 */
public class PlacesFragment extends Fragment {
  // Views
  private ListView listView;

  // Adapter
  private PlacesAdapter adapter;

  // Callback reference
  private CustomCallback<CDAArray> cb;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (adapter == null) {
      // Create an adapter
      adapter = new PlacesAdapter(getActivity());

      // Make a request to the Delivery API and attempt to fetch all Entries of a specific
      // Content Type, ordered by rating. We also keep a reference to the callback instance,
      // which makes it possible to cancel the request in the future if necessary.
      HashMap<String, String> query = new HashMap<>();
      query.put("content_type", getString(R.string.cda_place_content_type_id));
      query.put("order", "-fields.rating");

      CFUtils.getClient(getActivity())
          .entries().async().fetchAll(query,
          cb = new CustomCallback<CDAArray>(new WeakReference<Activity>(getActivity())) {
            @Override
            protected void onSuccess(CDAArray array) {
              // Request was successful, add all result items to the adapter and
              // notify any observers.
              if (adapter.addFromArray(array)) {
                adapter.notifyDataSetChanged();
              }
            }
          });
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    super.onCreateView(inflater, container, savedInstanceState);

    // Inflate the places layout.
    return inflater.inflate(R.layout.fragment_places, null);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Bind views
    listView = (ListView) view.findViewById(R.id.list);

    // Attach listeners
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Grab the item according to position
        Place place = adapter.getItem(position);

        // Show InfoActivity
        startActivity(
            new Intent(getActivity(), InfoActivity.class).putExtra(InfoActivity.EXTRA_PLACE,
                place));
      }
    });

    // Setup ListView
    listView.setAdapter(adapter);
  }

  @Override public void onDestroy() {
    // Cancel the callback if it's still alive
    if (cb != null) {
      cb.cancel();
    }

    super.onDestroy();
  }
}
