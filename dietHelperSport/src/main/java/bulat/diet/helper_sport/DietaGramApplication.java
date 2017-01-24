package bulat.diet.helper_sport;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.facebook.FacebookSdk;

import bulat.diet.helper_sport.utils.GATraker;

/**
 * Created by Yauheni.Bulat on 19.12.2016.
 */

public class DietaGramApplication extends Application {

    @Override
    public void onCreate() {
        GATraker.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        super.onCreate();
    }
}
