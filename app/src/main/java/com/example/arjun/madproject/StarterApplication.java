
package com.example.arjun.madproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;
import com.parse.twitter.Twitter;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Parse.enableLocalDatastore(this);

    Parse.initialize(this, "JUsZihp54u72sZjSaKTjAnKVs7OW7AJXyqgzOD0i", "Lya1kgKy9GROgieeiy8iUGQH7wTbo8XSauPurdgq");
    ParseTwitterUtils.initialize("aQ05c6bsu3cUgz2oVdf0UTjFq", "7xrNyqO3TLuaShgMfTBcFBwJfeuEBMdegx4oizM21oox4F7aEn");
    ParseInstallation.getCurrentInstallation().saveInBackground();

    ParseFacebookUtils.initialize(this);
    FacebookSdk.sdkInitialize(getApplicationContext());

  }
}
