package bulat.diet.helper_sport.db;

import java.util.ArrayList;

import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.Sinch;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class SinchHelper {

	public static ArrayList<Sinch> getSinchArray(Context context) {
		ContentResolver cr = context.getContentResolver();
		Cursor c = cr.query(SinchProvider.SINCHLIST_CONTENT_URI, null, null,
				null, null);
		ArrayList<Sinch> result = new ArrayList<Sinch>();
		if (c != null) {
			try {
				Sinch res;
				while (c.moveToNext()) {
					res = new Sinch();
					res.setId(String.valueOf(c.getInt(c.getColumnIndex(SinchProvider.SINCH_ID))));
					res.setUrl(c.getString(c.getColumnIndex(SinchProvider.URL)));
					res.setParams(c.getString(c.getColumnIndex(SinchProvider.PARAMS)));
					res.setType(c.getString(c.getColumnIndex(SinchProvider.TYPE)));					
					result.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return result;
	}

	public static int addNewSinch(String url, String params, String type, Context context) {
		
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();

			// values.put(DishProvider.DISH_ID, couponId);
			values.put(SinchProvider.URL, url);
			values.put(SinchProvider.PARAMS, params);
			//values.put(SinchProvider.USER_ID, user.uid);
			values.put(SinchProvider.TYPE, type);

			Uri uri = cr.insert(SinchProvider.SINCHLIST_CONTENT_URI, values);
			long id = ContentUris.parseId(uri);
			return (int) id;
	}
	
	public static void removeSinch(String id, Context context) {
		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		String where = "_id" + " = " + id ;
		// values.put(DishProvider.DISH_ID, couponId);
		values.put(SinchProvider.SINCH_ID, id);		
		cr.delete(SinchProvider.SINCHLIST_CONTENT_URI, where, null);		
	}

	public static void clear(Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		cr.delete(SinchProvider.SINCHLIST_CONTENT_URI, null, null);
	}

	private static boolean isAlreadyExist(String id, Context context) {

		ContentResolver cr = context.getContentResolver();
		String selection = SinchProvider.SINCH_ID + " = ?";
		String[] val = new String[] { "" + id };
		Cursor c = cr.query(SinchProvider.SINCHLIST_CONTENT_URI, null, selection,
				val, null);
		try {
			return c.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return false;

	}
}
