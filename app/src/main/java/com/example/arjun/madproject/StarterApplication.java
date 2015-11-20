
package com.example.arjun.madproject;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    Parse.initialize(this, "JUsZihp54u72sZjSaKTjAnKVs7OW7AJXyqgzOD0i", "Lya1kgKy9GROgieeiy8iUGQH7wTbo8XSauPurdgq");



    //ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
    defaultACL.setPublicReadAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
