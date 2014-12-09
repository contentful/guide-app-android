package com.contentful.guide.model;

import com.contentful.java.cda.model.CDAAsset;
import com.contentful.java.cda.model.CDAEntry;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Place.
 */
public class Place extends CDAEntry {
  private String name;
  private String type;
  private Integer rating;
  private ArrayList<CDAAsset> pictures;
  private String description;
  private String openingTimes;
  private String email;
  private String phoneNumber;
  private String url;
  private transient LatLng position;

  public Place() {
  }

  public String getName() {
    if (name == null) {
      name = (String) getFields().get("name");
    }

    return name;
  }

  public String getType() {
    if (type == null) {
      type = (String) getFields().get("type");
    }

    return type;
  }

  public Integer getRating() {
    if (rating == null) {
      rating = ((Double) getFields().get("rating")).intValue();
    }

    return rating;
  }

  @SuppressWarnings("unchecked")
  public List<CDAAsset> getPictures() {
    if (pictures == null) {
      pictures = (ArrayList<CDAAsset>) getFields().get("pictures");
    }

    return pictures;
  }

  public String getDescription() {
    if (description == null) {
      description = (String) getFields().get("description");
    }

    return description;
  }

  public String getOpeningTimes() {
    if (openingTimes == null) {
      openingTimes = (String) getFields().get("openingTimes");
    }

    return openingTimes;
  }

  public String getEmail() {
    if (email == null) {
      email = (String) getFields().get("email");
    }

    return email;
  }

  public String getPhoneNumber() {
    if (phoneNumber == null) {
      phoneNumber = (String) getFields().get("phoneNumber");
    }

    return phoneNumber;
  }

  public String getUrl() {
    if (url == null) {
      url = (String) getFields().get("url");
    }

    return url;
  }

  public LatLng getPosition() {
    if (position == null) {
      Map address = (Map) getFields().get("address");

      position = new LatLng((Double) address.get("lat"), (Double) address.get("lon"));
    }

    return position;
  }
}
