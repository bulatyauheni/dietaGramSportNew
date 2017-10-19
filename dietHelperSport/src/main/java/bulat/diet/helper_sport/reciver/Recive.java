package bulat.diet.helper_sport.reciver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.TreeMap;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.DietHelperActivity;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.SaveUtils;


public class Recive extends BroadcastReceiver {
	public NotificationManager myNotificationManager;
	public static final int NOTIFICATION_ID = 1;
	public static final int NOTIFICATION_ID2 = 2;
	TreeMap<String,DishType> map = new TreeMap<String, DishType>();
	private boolean flagNotFirstTimeAdv=true;
	@Override
	public void onReceive(Context context, Intent intent) {
	
	    myNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

	      CharSequence NotificationTicket = context.getString(R.string.newmessage);
	      CharSequence NotificationTitle = context.getString(R.string.newmessage_title);
	     // CharSequence NotificationContent = "USB is Connected!";
	     
	      Date currDate = new Date();
    	  if(currDate.getTime() - SaveUtils.getLastVisitTime(context) > DateUtils.DAY_IN_MILLIS && currDate.getTime() - SaveUtils.getLastVisitTime(context) < 3 * DateUtils.DAY_IN_MILLIS  ){
    		  Notification notification = new Notification(R.drawable.icon_m, NotificationTicket, 0);
		      Intent notificationIntent = new Intent(context, DietHelperActivity.class);
		      PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		     // notification.setLatestEventInfo(context, context.getString(R.string.reminder), context.getString(R.string.reminder) , contentIntent);
			  try {
				  Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
				  deprecatedMethod.invoke(notification, this, context.getString(R.string.reminder), context.getString(R.string.reminder), contentIntent);
			  } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {

			  }
		      notification.flags |= Notification.FLAG_AUTO_CANCEL;
		      myNotificationManager.notify(NOTIFICATION_ID2, notification);
    	  }
    	  if(currDate.getTime() - SaveUtils.getLastVisitTime(context) > 30*DateUtils.DAY_IN_MILLIS && currDate.getTime() - SaveUtils.getLastVisitTime(context) < 33 * DateUtils.DAY_IN_MILLIS  ){
    		  Notification notification = new Notification(R.drawable.icon_m, NotificationTicket, 0);
		      Intent notificationIntent = new Intent(context, DietHelperActivity.class);
		      PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		     // notification.setLatestEventInfo(context, context.getString(R.string.reminder), context.getString(R.string.reminder) , contentIntent);
		      notification.flags |= Notification.FLAG_AUTO_CANCEL;
		      myNotificationManager.notify(NOTIFICATION_ID2, notification);
    	  }
	      
	}	
}
