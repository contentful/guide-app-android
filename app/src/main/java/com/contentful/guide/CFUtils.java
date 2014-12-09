package com.contentful.guide;

import android.content.Context;

import com.contentful.guide.model.Place;
import com.contentful.java.cda.CDAClient;
import java.util.HashMap;

/**
 * Utilities.
 */
public class CFUtils {
  private CFUtils() {
  }

  private static CDAClient client;

  public synchronized static CDAClient getClient(Context context) {
    if (client == null) {
      // Create the client
      HashMap<String, Class<?>> classMap = new HashMap<>();
      classMap.put(context.getString(R.string.cda_place_content_type_id), Place.class);

      client = new CDAClient.Builder().setSpaceKey(context.getString(R.string.cda_space_key))
          .setAccessToken(context.getString(R.string.cda_access_token))
          .setCustomClasses(classMap)
          .build();
    }

    return client;
  }
}
