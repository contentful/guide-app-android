package com.contentful.guide;

import android.content.Context;

import com.contentful.guide.model.Place;
import com.contentful.java.api.CDAClient;

/**
 * Utilities.
 */
public class CFUtils {
    private static CDAClient sClient;

    public synchronized static CDAClient getClient(Context context) {
        if (sClient == null) {
            // Create the client
            sClient = new CDAClient.Builder()
                    .setSpaceKey(context.getString(R.string.cda_space_key))
                    .setAccessToken(context.getString(R.string.cda_access_token))
                    .build();

            // Register the Place class
            sClient.registerCustomClass(
                    context.getString(R.string.cda_place_content_type_id),
                    Place.class);
        }

        return sClient;
    }
}
