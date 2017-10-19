package bulat.diet.helper_sport.service;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.DietHelperActivity;
import bulat.diet.helper_sport.activity.NotificationActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AlarmService extends Service{
	public static final int NOTIFICATION_ID2 = 5;
	@Override

	public void onCreate() {

	//Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();

	}



	@Override

	public IBinder onBind(Intent intent) {

	// TODO Auto-generated method stub

	//Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();

	return null;

	}


    @Override

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);	
		if(intent!=null){
		Bundle extras = intent.getExtras();
		if(extras!=null){
			NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification();
			notification.icon = R.drawable.icon_m;
			notification.tickerText =extras.getString(NotificationActivity.TITLE);
			notification.sound = Uri.parse("android.resource://"
		            + this.getPackageName() + "/" + R.raw.eye);
			notification.when = System.currentTimeMillis();
			Intent notificationIntent = new Intent(this, DietHelperActivity.class);
		    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			try {
				Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
				deprecatedMethod.invoke(notification, this, extras.getString(NotificationActivity.TITLE), this.getString(R.string.reminder), contentIntent);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {

			}
		    //notification.setLatestEventInfo(this, extras.getString(NotificationActivity.TITLE), this.getString(R.string.reminder) , contentIntent);
		    notification.flags |= Notification.FLAG_AUTO_CANCEL;


		    manager.notify(NOTIFICATION_ID2, notification);
		}
		}
	}



	@Override

	public boolean onUnbind(Intent intent) {

	// TODO Auto-generated method stub

	//Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

	return super.onUnbind(intent);

	}
}
