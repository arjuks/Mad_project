package com.example.arjun.madproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Parse.enableLocalDatastore(this);

    Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    ParseInstallation.getCurrentInstallation().saveInBackground();

    ParseFacebookUtils.initialize(this);
    FacebookSdk.sdkInitialize(this);
  }
}
