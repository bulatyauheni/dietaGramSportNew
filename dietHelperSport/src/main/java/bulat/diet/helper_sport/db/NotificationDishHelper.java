package bulat.diet.helper_sport.db;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;
import bulat.diet.helper_sport.db.DishProvider.DatabaseHelper;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.NotificationDish;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;

public class NotificationDishHelper {
	
	public static Cursor getAllNotifications(Context context) {
		ContentResolver cr = context.getContentResolver();
		//String selection =  DishProvider.TODAY_IS_DAY + " <> " + "1 and "  + DishProvider.TODAY_DISH_DATE_LONG + " > " + "?";
		Cursor c = cr.query(DishProvider.NOTIFICATION_CONTENT_URI, null, null, null, DishProvider.N_TIME_ID);
		return c;
	}
	
	public static ArrayList<NotificationDish> getAllNotificationsList(Context context) {
		ContentResolver cr = context.getContentResolver();
		//String selection =  DishProvider.N_ENABLED + "=" + "1 ";
		Cursor c = cr.query(DishProvider.NOTIFICATION_CONTENT_URI, null, null, null, DishProvider.N_TIME_ID);		
		ArrayList<NotificationDish> reslist = new ArrayList<NotificationDish>();
			
		if (c!=null){
			try {
				
				while (c.moveToNext())
		        {	
					NotificationDish res = new NotificationDish(
							c.getString(c.getColumnIndex(DishProvider.N_TIME_ID)), 
							c.getString(c
							.getColumnIndex(DishProvider.N_NAME_NOTIFICATION)),
							c.getString(c.getColumnIndex(DishProvider.N_NOTIFICATION_TIME_HH)),
							c.getString(c.getColumnIndex(DishProvider.N_NOTIFICATION_TIME_MM)),
							c.getInt(c.getColumnIndex(DishProvider.N_ENABLED)),
							c.getInt(c.getColumnIndex(DishProvider.N_NOTIF_ENABLED)));					
					reslist.add(res);
		        }
				return reslist;
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				c.close();
			}
		}
		return reslist;
	}
	
	public static ArrayList<NotificationDish> getEnabledNotificationsList(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.N_ENABLED + "=" + "1 ";
		Cursor c = cr.query(DishProvider.NOTIFICATION_CONTENT_URI, null, selection, null, DishProvider.N_TIME_ID);		
		ArrayList<NotificationDish> reslist = new ArrayList<NotificationDish>();
			
		if (c!=null){
			try {
				
				while (c.moveToNext())
		        {	
					NotificationDish res = new NotificationDish(
							c.getString(c.getColumnIndex(DishProvider.N_TIME_ID)), 
							c.getString(c
							.getColumnIndex(DishProvider.N_NAME_NOTIFICATION)),
							c.getString(c.getColumnIndex(DishProvider.N_NOTIFICATION_TIME_HH)),
							c.getString(c.getColumnIndex(DishProvider.N_NOTIFICATION_TIME_MM)),
							c.getInt(c.getColumnIndex(DishProvider.N_ENABLED)),
							c.getInt(c.getColumnIndex(DishProvider.N_NOTIF_ENABLED)));					
					reslist.add(res);
		        }
				return reslist;
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				c.close();
			}
		}
		return reslist;
	}	
	public static TodayDish getNotificationById(String id, Context context) {
		
		ContentResolver cr = context.getContentResolver();
		String selection = "_id" + "=" + "?" ;
		String[] columns = new String[] { DishProvider.TODAY_NAME, DishProvider.TODAY_CALORICITY, 
				DishProvider.TODAY_DISH_WEIGHT, "_id", DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TYPE };
		String[] val = new String[] { id };
		Cursor c = cr.query(DishProvider.NOTIFICATION_CONTENT_URI, columns, selection, val,null);
		TodayDish res = new TodayDish();
		if (c!=null){
			try {
				
				while (c.moveToNext())
		        {					
					res.setName(c.getString(0));
					res.setCaloricity(c.getInt(1));
					res.setWeight(c.getInt(2));
					res.setId(c.getString(3));
					res.setDateTime(4);
					res.setType(c.getString(5));
					return res;
		        }
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				c.close();
			}
		}
		return null;	
	}

	public static String addNewNotification(NotificationDish notification, Context context) {
		
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();

			//values.put(DishProvider.DISH_ID, couponId);

			values.put(DishProvider.N_TIME_ID, notification.getId());
			values.put(DishProvider.N_ENABLED, notification.getEnabled());
			values.put(DishProvider.N_NOTIF_ENABLED, notification.getEnabledNotification());
			values.put(DishProvider.N_NAME_NOTIFICATION, notification.getName());
			values.put(DishProvider.N_NOTIFICATION_TIME_HH, notification.getTimeHH());
			values.put(DishProvider.N_NOTIFICATION_TIME_MM, notification.getTimeMM());
		
			long id = ContentUris.parseId(cr.insert(DishProvider.NOTIFICATION_CONTENT_URI, values));
			String f = String.valueOf(id);
			return f;		
	}
	
	public static boolean updateDish(TodayDish dish, Context context) {
		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();

		//values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.TODAY_NAME, dish.getName());
		values.put(DishProvider.TODAY_DESCRIPTION, dish.getDescription());
		values.put(DishProvider.TODAY_CALORICITY, dish.getCaloricity());
		values.put(DishProvider.TODAY_DISH_CALORICITY, dish.getAbsolutCaloricity());
		values.put(DishProvider.TODAY_FAT, dish.getFat());
		values.put(DishProvider.TODAY_CARBON, dish.getCarbon());
		values.put(DishProvider.TODAY_PROTEIN, dish.getProtein());
		values.put(DishProvider.TODAY_DISH_FAT, dish.getAbsFat());
		values.put(DishProvider.TODAY_DISH_CARBON, dish.getAbsCarbon());
		values.put(DishProvider.TODAY_DISH_PROTEIN, dish.getAbsProtein());
		values.put(DishProvider.TODAY_CATEGORY, dish.getCategory());
		values.put(DishProvider.TODAY_DISH_WEIGHT, dish.getWeight());
		values.put(DishProvider.TODAY_DISH_TIME_HH, dish.getDateTimeHH());
		values.put(DishProvider.TODAY_DISH_TIME_MM, dish.getDateTimeMM());
		String where = "_id" + " = " + String.valueOf(dish.getId());
		cr.update(DishProvider.NOTIFICATION_CONTENT_URI, values, where, null);
		return true;		
	}
	//int en   0-off or 1-on
	public static boolean updateNotifTime( Context context, String hh, String mm, String en, String title, String nId) {
		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();

		values.put(DishProvider.N_NOTIF_ENABLED, en);
		values.put(DishProvider.N_NAME_NOTIFICATION, title);
		values.put(DishProvider.N_NOTIFICATION_TIME_HH, hh);
		values.put(DishProvider.N_NOTIFICATION_TIME_MM, mm);	
		String where = DishProvider.N_TIME_ID + " = " + nId;
		cr.update(DishProvider.NOTIFICATION_CONTENT_URI, values, where, null);
		return true;		
	}

	public static boolean setNotifEnabled( Context context, int en, String nId) {
		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();

		values.put(DishProvider.N_ENABLED, en);	
		String where = DishProvider.N_TIME_ID + " = " + nId;
		cr.update(DishProvider.NOTIFICATION_CONTENT_URI, values, where, null);
		return true;		
	}
	

	

	
	public static void clearAll(Context context) {
		ContentResolver cr = context.getContentResolver();
		cr.delete(DishProvider.NOTIFICATION_CONTENT_URI, null, null);
	}

}
