package com.contentful.guide.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contentful.guide.R;
import com.contentful.guide.model.Place;
import com.squareup.picasso.Picasso;

/**
 * Info Activity class.
 * Displays general information about a specific {@code Place}.
 */
public class InfoActivity extends Activity implements View.OnClickListener {
    public static final String EXTRA_PLACE = "com.contentful.guide.EXTRA_PLACE";

    private Place place;

    // Views
    private ImageView ivPhoto;
    private TextView tvDescription;
    private TextView tvHours;
    private LinearLayout actionWrapper;
    private ImageView ivEmail;
    private ImageView ivMap;
    private ImageView ivCall;
    private ImageView ivWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);

        // Bind views
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvHours = (TextView) findViewById(R.id.tv_hours);
        actionWrapper = (LinearLayout) findViewById(R.id.action_wrapper);
        ivEmail = (ImageView) findViewById(R.id.iv_email);
        ivMap = (ImageView) findViewById(R.id.iv_map);
        ivCall = (ImageView) findViewById(R.id.iv_call);
        ivWebsite = (ImageView) findViewById(R.id.iv_website);

        // Attach listeners
        ivEmail.setOnClickListener(this);
        ivMap.setOnClickListener(this);
        ivCall.setOnClickListener(this);
        ivWebsite.setOnClickListener(this);

        // Populate the UI
        place = (Place) getIntent().getSerializableExtra(EXTRA_PLACE);
        setTitle(place.getName());

        Picasso.with(this)
                .load(place.getPictures().get(0).getUrl())
                .fit()
                .centerCrop()
                .into(ivPhoto);

        tvDescription.setText(place.getDescription());

        tvHours.setText(place.getOpeningTimes());

        if (place.getPhoneNumber() == null) {
            ivCall.setVisibility(View.GONE);
            actionWrapper.setWeightSum(actionWrapper.getWeightSum() - 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_email:
                onEmailClicked();
                break;

            case R.id.iv_map:
                onMapClicked();
                break;

            case R.id.iv_call:
                onCallClicked();
                break;

            case R.id.iv_website:
                onWebsiteClicked();
                break;
        }
    }

    /**
     * Fires an implicit {@link android.content.Intent} to send an email to the email address
     * of the current {@code Place}.
     */
    private void onEmailClicked() {
        startActivity(new Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", place.getEmail(), null)));
    }

    /**
     * Fires an implicit {@link android.content.Intent} to dial the phone number of
     * the current {@code Place}.
     */
    private void onCallClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.fromParts("tel", place.getPhoneNumber(), null)));
    }

    /**
     * Launches a new instance of the {@link MapActivity} class to display the current {@code Place}
     * in a map view.
     */
    private void onMapClicked() {
        startActivity(new Intent(this, MapActivity.class)
                .putExtra(InfoActivity.EXTRA_PLACE, place));
    }

    /**
     * Launches an implicit {@link android.content.Intent} to view the website of the
     * current {@code Place}.
     */
    private void onWebsiteClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(place.getUrl())));
    }
}
