package com.contentful.guide;

import android.app.Activity;
import android.widget.Toast;

import com.contentful.java.api.CDACallback;

import java.lang.ref.WeakReference;

import retrofit.RetrofitError;

/**
 * An extension of the {@link com.contentful.java.api.CDACallback} class which keeps
 * a {@link java.lang.ref.WeakReference} to an {@link android.app.Activity} instance,
 * and displays a simple {@link android.widget.Toast} in case the request fails for any reason.
 */
public abstract class CustomCallback<T> extends CDACallback<T> {
  private WeakReference<Activity> weakActivity;

  public CustomCallback(WeakReference<Activity> weakActivity) {
    this.weakActivity = weakActivity;
  }

  @Override protected void onFailure(RetrofitError retrofitError) {
    super.onFailure(retrofitError);

    Activity activity = weakActivity.get();

    if (activity != null) {
      Toast.makeText(activity, R.string.error_request_failed, Toast.LENGTH_LONG).show();
    }
  }
}
