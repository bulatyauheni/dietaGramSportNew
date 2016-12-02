package bulat.diet.helper_sport.db;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import bulat.diet.helper_sport.item.MessageItem;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.utils.Constants;

public class MessagesHelper {

	public static int addNewMessage(MessageItem mess, Context context) {
		
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();

			//values.put(DishProvider.DISH_ID, couponId);
			values.put(DishProvider.USER_NAME, mess.getUserName());
			values.put(DishProvider.USER_ID, mess.getUserId());
			values.put(DishProvider.USER_ID_FROM, mess.getUserIdFrom());
			values.put(DishProvider.MESSAGE_TEXT, mess.getMessage());
			values.put(DishProvider.DATE, mess.getDate());
			values.put(DishProvider.DATE_INT, mess.getDateInt());	
			Uri uri = cr.insert(DishProvider.MESSAGES_CONTENT_URI, values);
			long id = ContentUris.parseId(uri);
			return (int)id;		
	}

	public static Cursor getMessagesByUser(Context context, String userId) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.USER_ID + " = " + userId;
		Cursor c = cr
				.query(DishProvider.MESSAGES_CONTENT_URI, null, selection, null, DishProvider.DATE_INT + " DESC");
		return c;
	}
	
	public static Cursor getUsers(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.USER_ID + "<>0" + ") GROUP BY (" + DishProvider.USER_ID;
		 String[] columns = new String[] {"_id", DishProvider.USER_NAME, DishProvider.USER_ID, DishProvider.MESSAGE_TEXT, DishProvider.DATE};
		 Cursor c = cr.query(DishProvider.MESSAGES_CONTENT_URI, columns, selection, null, DishProvider.DATE_INT + " DESC");

		return c;
	}
	
	public static ArrayList<String> getUsersList(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.USER_ID + "<>0" + ") GROUP BY (" + DishProvider.USER_ID;
		 String[] columns = new String[] {"_id", DishProvider.USER_NAME, DishProvider.USER_ID};
		 Cursor c = cr.query(DishProvider.MESSAGES_CONTENT_URI, columns, selection, null, DishProvider.DATE_INT);
		 ArrayList<String> result = new ArrayList<String>();
			if (c!=null){
				try {
					String res;
					while (c.moveToNext())
			        {															
						res = String.valueOf(c.getInt(2));
						result.add(res);					
			        }
				}catch (Exception e) {
					e.printStackTrace();
				}finally{
					c.close();
				}
			}
			return result;
	}


	public static void clearAll(Context context) {
		ContentResolver cr = context.getContentResolver();
		cr.delete(DishProvider.MESSAGES_CONTENT_URI, null, null);
	}
	public static void clearConversation(Context context, String userId) {
		ContentResolver cr = context.getContentResolver();		
		String where = DishProvider.USER_ID + " = " + String.valueOf(userId );
		//String[] val = new String[] { String.valueOf(id )};
		
		cr.delete(DishProvider.MESSAGES_CONTENT_URI, where, null);		
	}
	public static void removeFollowing(MessageItem messageItem,Context context) {
		ContentResolver cr = context.getContentResolver();		
		String where = DishProvider.USER_ID + " = " + String.valueOf(messageItem.getUserId() + " and " + DishProvider.DATA + " = '" + Constants.IFOLLOW +"'");
		//String[] val = new String[] { String.valueOf(id )};
		
		cr.delete(DishProvider.MESSAGES_CONTENT_URI, where, null);	
	}

	public static boolean amIFollow(String userId, Context context) {
		// TODO Auto-generated method stub
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.USER_ID + "=" + userId + " and " + DishProvider.DATA + "='" + Constants.IFOLLOW +"'";
		 String[] columns = new String[] {"_id", DishProvider.USER_NAME, DishProvider.USER_ID};
		 Cursor c = cr.query(DishProvider.MESSAGES_CONTENT_URI, columns, selection, null, DishProvider.DATE_INT);
		 ArrayList<String> result = new ArrayList<String>();
			if (c!=null){
				try {
					String res;
					while (c.moveToNext())
			        {															
						res = String.valueOf(c.getInt(2));
						result.add(res);					
			        }
				}catch (Exception e) {
					e.printStackTrace();
				}finally{
					c.close();
				}
			}
			if(result.size()>0){
				return true;
			}
			return false;
	}
	public static Cursor getFavoriteUsers(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.USER_ID + "<>0" + " and "+ DishProvider.DATA +"='" + Constants.IFOLLOW +"') GROUP BY (" + DishProvider.USER_ID;
		 String[] columns = new String[] {"_id", DishProvider.USER_NAME, DishProvider.USER_ID, DishProvider.MESSAGE_TEXT, DishProvider.DATE};
		 Cursor c = cr.query(DishProvider.MESSAGES_CONTENT_URI, columns, selection, null, DishProvider.DATE_INT + " DESC");

		return c;
	}
	
	public static Cursor getFollowers(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.USER_ID + "<>0 and " + DishProvider.DATA + " = " + Constants.FOLLOWER + " ) GROUP BY (" + DishProvider.USER_ID;
		 String[] columns = new String[] {"_id", DishProvider.USER_NAME, DishProvider.USER_ID, DishProvider.MESSAGE_TEXT, DishProvider.DATE};
		 Cursor c = cr.query(DishProvider.MESSAGES_CONTENT_URI, columns, selection, null, DishProvider.DATE_INT + " DESC");

		return c;
	}
	
	public static Cursor getIFollow(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.USER_ID + "<>0 and " + DishProvider.DATA + " = " + Constants.IFOLLOW + " ) GROUP BY (" + DishProvider.USER_ID;
		 String[] columns = new String[] {"_id", DishProvider.USER_NAME, DishProvider.USER_ID, DishProvider.MESSAGE_TEXT, DishProvider.DATE};
		 Cursor c = cr.query(DishProvider.MESSAGES_CONTENT_URI, columns, selection, null, DishProvider.DATE_INT + " DESC");

		return c;
	}

}

