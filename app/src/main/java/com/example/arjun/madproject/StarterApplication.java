
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

    Parse.initialize(this, "JUsZihp54u72sZjSaKTjAnKVs7OW7AJXyqgzOD0i", "Lya1kgKy9GROgieeiy8iUGQH7wTbo8XSauPurdgq");
    ParseInstallation.getCurrentInstallation().saveInBackground();

    ParseFacebookUtils.initialize(getApplicationContext());
    FacebookSdk.sdkInitialize(getApplicationContext());

  }
}
