package com.contentful.guide.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.contentful.guide.CFUtils;
import com.contentful.guide.CustomCallback;
import com.contentful.guide.R;
import com.contentful.guide.activities.InfoActivity;
import com.contentful.guide.model.Place;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAResource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import retrofit.client.Response;

/**
 * An extension to the {@link com.google.android.gms.maps.SupportMapFragment} class.
 * <p/>
 * This Fragment is currently used in two different places in the app:
 * <p/>
 * 1. Inflated by {@link com.contentful.guide.adapters.MainAdapter} and displayed
 * in {@link com.contentful.guide.activities.MainActivity}, representing all available
 * {@code Place}s over the map.
 * <p/>
 * 2. Inflated and displayed by {@link InfoActivity}, representing a single {@code Place} over the map.
 */
public class GuideMapFragment extends SupportMapFragment implements GoogleMap.OnInfoWindowClickListener {
    // Mapping of Markers to Places
    private HashMap<Marker, Place> markers;

    // Callback reference
    private CustomCallback<CDAArray> cb;

    // Factory
    public static GuideMapFragment newInstance(Place place) {
        GuideMapFragment fragment = new GuideMapFragment();

        Bundle b = new Bundle();
        b.putSerializable(InfoActivity.EXTRA_PLACE, place);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GoogleMap map = getMap();

        // Enable current user location on the map
        map.setMyLocationEnabled(true);

        // Attach click listeners
        map.setOnInfoWindowClickListener(this);

        // Add Markers to the map
        showMarkers();
    }

    /**
     * Adds Markers to the map.
     * <p/>
     * In case this Fragment has a single {@code Place} associated to it
     * via {@link #setArguments(android.os.Bundle)}), a single {@code Marker} will be added.
     * Otherwise, this will issue a request to the Delivery API to fetch all available Entries,
     * upon success a {@code Marker} will be added for every {@code Place} Entry contained within
     * the result.
     */
    private void showMarkers() {
        // Initialize markers mapping
        markers = new HashMap<Marker, Place>();

        // Extract attached Place if there is one.
        Bundle b = getArguments();
        Place place = b == null ? null : (Place) b.getSerializable(InfoActivity.EXTRA_PLACE);

        if (place == null) {
            // No Place attached, fetch and display all.
            CFUtils.getClient(getActivity()).fetchEntries(cb =
                    new CustomCallback<CDAArray>(new WeakReference<Activity>(getActivity())) {
                        @Override
                        protected void onSuccess(CDAArray array, Response response) {
                            // Request was successful, add Markers for all result items. This also
                            // creates a LatLngBounds to help center the map while displaying all
                            // newly created Markers.
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            // Iterate through the result
                            for (CDAResource resource : array.getItems()) {
                                if (resource instanceof Place) {
                                    Place p = (Place) resource;

                                    // Create a new Marker and keep a reference to it in our map.
                                    markers.put(addMarker(p), p);

                                    // Add place LatLng coordinates to our LatLngBounds builder
                                    builder.include(p.getPosition());
                                }
                            }

                            // Padding to be used when zooming over our new LatLngBounds
                            int padding = getResources().getInteger(R.integer.map_padding);

                            // Animate the map
                            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(
                                    builder.build(), padding));
                        }
                    });
        } else {
            // Create a new Marker and keep a reference to it in our map.
            markers.put(addMarker(place), place);

            // Animate the map
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(place.getPosition(),
                    (float) getResources().getInteger(R.integer.map_default_zoom)));
        }
    }

    /**
     * Adds a single {@code Marker} to the map from the supplied {@code place} argument.
     *
     * @param place {@link Place} to extract lat/lon coordinates from.
     * @return The newly created {@link com.google.android.gms.maps.model.Marker} instance.
     */
    private Marker addMarker(Place place) {
        LatLng position = place.getPosition();

        return getMap().addMarker(new MarkerOptions()
                .position(position)
                .title(place.getName()));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Since a Marker has no convenient way of attaching metadata to it, we have
        // to keep a reference to it in our map, for this kind of use case.
        Place place = markers.get(marker);

        if (place != null) {
            // Start InfoActivity, closing any activities that are on top it.
            startActivity(new Intent(getActivity(), InfoActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(InfoActivity.EXTRA_PLACE, place));
        }
    }

    @Override
    public void onDestroy() {
        // Cancel the callback if it's still alive
        if (cb != null) {
            cb.cancel();
        }

        super.onDestroy();
    }
}
