
package com.example.arjun.madproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
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
//    ParseTwitterUtils.initialize("ElbJBZBtmS2Dtv5HBxTSMYOTx", "Bf0aVR9vqLkShR9eYE2xU8r1cNb5G0Xxq8aV1lxxIt7qZVzF7k");
    FacebookSdk.sdkInitialize(this);
  }
}
