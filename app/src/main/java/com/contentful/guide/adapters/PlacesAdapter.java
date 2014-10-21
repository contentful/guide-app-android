package com.contentful.guide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.contentful.guide.CircleTransform;
import com.contentful.guide.R;
import com.contentful.guide.model.Place;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAResource;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Places Adapter class.
 */
public class PlacesAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<Place> data;

  public PlacesAdapter(Context context) {
    this.context = context;
    this.data = new ArrayList<Place>();
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public Place getItem(int position) {
    return data.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder vh;

    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.view_place, parent, false);
      vh = new ViewHolder(convertView);
      vh.ratingBar.setMax(5);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    // Grab Place item according to position
    Place item = getItem(position);

    // Display image
    Picasso.with(context)
        .load(item.getPictures().get(0).getUrl())
        .fit()
        .centerCrop()
        .transform(new CircleTransform())
        .into(vh.ivPhoto);

    // Set text fields
    vh.tvName.setText(item.getName());
    vh.tvType.setText(item.getType());

    // Set rating
    vh.ratingBar.setProgress(item.getRating());

    return convertView;
  }

  /**
   * Adds all {@code Place} Entries from a {@link CDAArray} object to the Adapter.
   *
   * @param array {@link CDAArray} instance.
   * @return Boolean indicating whether the contents of the Adapter changed.
   */
  public boolean addFromArray(CDAArray array) {
    boolean changed = false;

    for (CDAResource resource : array.getItems()) {
      if (resource instanceof Place) {
        data.add((Place) resource);
        changed = true;
      }
    }

    return changed;
  }

  /**
   * View Holder
   */
  private class ViewHolder {
    ImageView ivPhoto;
    TextView tvName;
    TextView tvType;
    RatingBar ratingBar;

    ViewHolder(View v) {
      ivPhoto = (ImageView) v.findViewById(R.id.iv_photo);
      tvName = (TextView) v.findViewById(R.id.tv_name);
      tvType = (TextView) v.findViewById(R.id.tv_type);
      ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
    }
  }
}
