package bulat.diet.helper_sport.utils;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import bulat.diet.helper_sport.R;

/**
 * Created by Yauheni.Bulat on 19.12.2016.
 */

public class GATraker {
    private static Tracker mTracker;

    public static void initialize(Context app) {

        GoogleAnalytics.getInstance(app);
        prepareTrackers(app);
    }

    private static void prepareTrackers(Context app) {
        if (mTracker == null) {
            mTracker = GoogleAnalytics.getInstance(app).newTracker(R.xml.analytics_config);
        }
    }

    public static void sendScreen(Object obj) {
        final String clsName = obj.getClass().getCanonicalName();
        mTracker.setScreenName(clsName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendEvent(String category, String action, String label, Long value) {
        final HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder(category, action).setLabel(label);
        if (value != null) {
            builder.setValue(value);
        }
        getTracker().send(builder.build());
    }

    public static void sendEvent(String category, String action) {
        getTracker().send(new HitBuilders.EventBuilder(category, action).build());
    }

    public static Tracker getTracker() {
        return mTracker;
    }
}
