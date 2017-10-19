package bulat.diet.helper_sport;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookSdk;
import com.vk.sdk.VKSdk;

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
        VKSdk.initialize(this);
        super.onCreate();
    }
}
