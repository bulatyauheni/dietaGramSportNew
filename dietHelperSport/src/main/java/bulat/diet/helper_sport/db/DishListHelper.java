package bulat.diet.helper_sport.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.FitnesType;
import bulat.diet.helper_sport.utils.SaveUtils;

public class DishListHelper {

	public static int addNewDish(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();

		// values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.NAME, dish.getName());
		values.put(DishProvider.SEARCHNAME, dish.getName().toLowerCase());
		values.put(DishProvider.DESCRIPTION, dish.getDescription());
		values.put(DishProvider.CALORICITY, dish.getCaloricity());
		values.put(DishProvider.CATEGORY, dish.getCategory());
		values.put(DishProvider.CATEGORY_NAME, dish.getCategoryName());
		values.put(DishProvider.FAT, dish.getFatStr());
		values.put(DishProvider.CARBON, dish.getCarbonStr());
		values.put(DishProvider.PROTEIN, dish.getProteinStr());
		values.put(DishProvider.BARCODE, dish.getBarcode());

		// values.put(DishProvider.ISCATEGORY, dish.getIscategory());
		// values.put(DishProvider.POPULARITY, dish.getPopularity());
		values.put(DishProvider.TYPE, dish.getType());
		Uri uri = cr.insert(DishProvider.DISHLIST_CONTENT_URI, values);
		long id = ContentUris.parseId(uri);
		return (int) id;
	}
	public static int addType(String name, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		// values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.CATEGORY_NAME, name);		
		values.put(DishProvider.CALORICITY, -100);	
		values.put(DishProvider.CATEGORY, SaveUtils.getLastType(context));	
		Uri uri = cr.insert(DishProvider.DISHLIST_CONTENT_URI, values);
		long id = ContentUris.parseId(uri);
		return (int) id;
	}

	public static int addNewDishFullParams(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		Log.e("New ", dish.getName());
		// values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.NAME, dish.getName());
		values.put(DishProvider.SEARCHNAME, dish.getName().toLowerCase());
		values.put(DishProvider.DESCRIPTION, dish.getDescription());
		values.put(DishProvider.CALORICITY, dish.getCaloricity());
		values.put(DishProvider.CATEGORY, dish.getCategory());
		values.put(DishProvider.CATEGORY_NAME, dish.getCategoryName());
		values.put(DishProvider.FAT, dish.getFatStr());
		values.put(DishProvider.CARBON, dish.getCarbonStr());
		values.put(DishProvider.PROTEIN, dish.getProteinStr());

		// values.put(DishProvider.ISCATEGORY, dish.getIscategory());
		// values.put(DishProvider.POPULARITY, dish.getPopularity());
		values.put(DishProvider.TYPE, dish.getType());
		Uri uri = cr.insert(DishProvider.DISHLIST_CONTENT_URI, values);
		long id = ContentUris.parseId(uri);
		return (int) id;
	}

	public static int addNewSport(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();

		// values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.NAME, dish.getName());
		values.put(DishProvider.DESCRIPTION, dish.getDescription());
		values.put(DishProvider.CALORICITY, dish.getCaloricity());
		values.put(DishProvider.CATEGORY, dish.getCategory());
		values.put(DishProvider.FAT, dish.getFatStr());
		values.put(DishProvider.CARBON, dish.getCarbonStr());
		values.put(DishProvider.PROTEIN, dish.getProteinStr());
		values.put(DishProvider.DISH_ID, 0);

		// values.put(DishProvider.ISCATEGORY, dish.getIscategory());
		// values.put(DishProvider.POPULARITY, dish.getPopularity());
		values.put(DishProvider.TYPE, dish.getType());
		Uri uri = cr.insert(DishProvider.DISHLIST_CONTENT_URI, values);
		long id = ContentUris.parseId(uri);
		return (int) id;
	}
	public static Cursor searchInAll(Context context, String searchString) {
		searchString = searchString.trim();
		ArrayList<String> searchStrings = new ArrayList<String>();
		String searchStringsTemp[] = searchString.split(" ");
		for (int i = 0; i < searchStringsTemp.length; i++) {
			if(searchStringsTemp[i].length()>2){
				searchStrings.add(searchStringsTemp[i]);
			}
		}
		
		ContentResolver cr = context.getContentResolver();
		String selection="";
		if(searchStrings.size()>0){
			selection = "LOWER(" + DishProvider.SEARCHNAME + ") like '%" + searchString
					+ "%' or LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(0)
						+ "%'";
		}
		if(searchStrings.size()==2){
			selection = "LOWER(" + DishProvider.SEARCHNAME + ") like '%" + searchString
					+ "%' or (LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(1)
					+ "%' and LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(0)
					+ "%')";
		}
		if(searchStrings.size()==3){
			selection = "LOWER(" + DishProvider.SEARCHNAME + ") like '%" + searchString
					+ "%' or (LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(2)
					+ "%' and LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(1)
					+ "%' and LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(0)
					+ "%')";
		}
			Log.e("asdas", selection);
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null,
				selection, null, DishProvider.NAME + " LIMIT 20");		
		return c;
	}

	public static ArrayList<Dish> searchInAll(String searchString,
			Context context) {
		searchString = searchString.trim();
		ArrayList<String> searchStrings = new ArrayList<String>();
		String searchStringsTemp[] = searchString.split(" ");
		for (int i = 0; i < searchStringsTemp.length; i++) {
			if(searchStringsTemp[i].length()>2){
				searchStrings.add(searchStringsTemp[i]);
			}
		}
		
		ContentResolver cr = context.getContentResolver();
		String selection="";
		if(searchStrings.size()>0){
			selection = "LOWER(" + DishProvider.SEARCHNAME + ") like '%" + searchString
					+ "%' or LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(0)
						+ "%'";
		}
		if(searchStrings.size()==2){
			selection = "LOWER(" + DishProvider.SEARCHNAME + ") like '%" + searchString
					+ "%' or (LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(1)
					+ "%' and LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(0)
					+ "%')";
		}
		if(searchStrings.size()==3){
			selection = "LOWER(" + DishProvider.SEARCHNAME + ") like '%" + searchString
					+ "%' or (LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(2)
					+ "%' and LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(1)
					+ "%' and LOWER("  + DishProvider.SEARCHNAME + ") like '%" + searchStrings.get(0)
					+ "%')";
		}
			Log.e("asdas", selection);
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null,
				selection, null, DishProvider.DISH_ID + " DESC "  + " LIMIT 20");
		ArrayList<Dish> result = new ArrayList<Dish>();
		if (c != null) {
			try {
				Dish res;
				while (c.moveToNext()) {
					res = new Dish();
					res.setName(c.getString(c.getColumnIndex(DishProvider.NAME)));
					res.setCategory(c.getInt(c.getColumnIndex(DishProvider.CATEGORY)));
					res.setCaloricity(c.getInt(c.getColumnIndex(DishProvider.CALORICITY)));
					// res.setPopularity(c.getInt(3));
					res.setId(c.getString(c.getColumnIndex("_id")));
					res.setIscategory(0);
					res.setType(c.getString(c.getColumnIndex(DishProvider.TYPE)));
					if(c.getString(c.getColumnIndex(DishProvider.CARBON)) == null){
						break;
					}					
					res.setCarbonStr(c.getString(c.getColumnIndex(DishProvider.CARBON)));
					res.setProteinStr(c.getString(c.getColumnIndex(DishProvider.PROTEIN)));
					res.setFatStr(c.getString(c.getColumnIndex(DishProvider.FAT)));
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

	/*
	 * private static boolean alreadyExists(int couponId, Context context) {
	 * ContentResolver cr = context.getContentResolver(); String selection =
	 * CouponsProvider.DEAL_ID + " = " + couponId; Cursor c =
	 * cr.query(CouponsProvider.CONTENT_URI, null, selection, null, null);
	 * boolean exists = c.moveToFirst(); c.close(); return exists; }
	 */

	public static boolean resetPopularity(String id, Context context) {

		Dish dish = getDishById(id, context);
		if (dish != null) {
			dish.setPopularity(0);
			updateDish(dish, context);
		}
		return true;
	}

	/*
	 * public static boolean incDishPopularity(String id, Context context) {
	 * 
	 * Dish dish = getDishById(id, context); if(dish != null){
	 * dish.setPopularity(dish.getPopularity() + 1); updateDish(dish, context);
	 * } addToPopularGroup(dish, context); return true; }
	 * 
	 * private static void addToPopularGroup(Dish dish, Context context) {
	 * if(dish.getPopularity() > 3 && dish.getCategory()!=0){
	 * dish.setPopularity(0); updateDish(dish, context); dish.setCategory(0);
	 * if(!isAlreadyExist(dish, context)){ addNewDish(dish, context); } } }
	 */
	private static boolean isAlreadyExist(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.NAME + " = ?" + " and "
				+ DishProvider.CATEGORY + " = " + "0";
		String[] val = new String[] { dish.getName() };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
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

	public static Dish getDishById(String id, Context context) {

		ContentResolver cr = context.getContentResolver();
		String selection = "_id" + "=" + "?";
		String[] columns = new String[] { DishProvider.NAME,
				DishProvider.CATEGORY, DishProvider.CALORICITY, "_id",
				DishProvider.TYPE, DishProvider.DISH_ID , DishProvider.CARBON , DishProvider.FAT , DishProvider.PROTEIN, DishProvider.DESCRIPTION };
		String[] val = new String[] { id };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, val, DishProvider.CATEGORY);
		Dish res = new Dish();
		if (c != null) {
			try {

				while (c.moveToNext()) {
					res.setName(c.getString(0));
					res.setCategory(c.getInt(1));
					res.setCaloricity(c.getInt(2));
					res.setPopularity(c.getInt(5));
					res.setCarbonStr(c.getString(6));
					res.setFatStr(c.getString(7));
					res.setProteinStr(c.getString(8));
					res.setId(c.getString(3));
					res.setIscategory(0);
					res.setType(c.getString(4));
					res.setDescription(c.getString(9));
					return res;
				}
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return null;
	}

	public static Dish getIdByDish(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.NAME + "=" + "? , "
				+ DishProvider.CATEGORY + "=" + "? , "
				+ DishProvider.CALORICITY + "=" + "? , ";
		String[] columns = new String[] { "_id" };
		String[] val = new String[] { dish.getName(),
				String.valueOf(dish.getCategory()),
				String.valueOf(dish.getCaloricity()) };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, val, DishProvider.CATEGORY);
		if (c != null) {
			try {

				while (c.moveToNext()) {
					dish.setId(c.getString(0));
					return dish;
				}
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return null;
	}

	public static boolean updateDish(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(DishProvider.NAME, dish.getName());
		values.put(DishProvider.SEARCHNAME, dish.getName().toLowerCase());
		values.put(DishProvider.DESCRIPTION, dish.getDescription());
		values.put(DishProvider.CALORICITY, dish.getCaloricity());
		values.put(DishProvider.CATEGORY, dish.getCategory());
		values.put(DishProvider.FAT, dish.getFatStr());
		values.put(DishProvider.CARBON, dish.getCarbonStr());
		values.put(DishProvider.PROTEIN, dish.getProteinStr());
	    values.put(DishProvider.DISH_ID, dish.getPopularity());
	    values.put(DishProvider.CATEGORY_NAME, dish.getCategoryName());
		String where = "_id" + " = " + String.valueOf(dish.getId());
		cr.update(DishProvider.DISHLIST_CONTENT_URI, values, where, null);
		return true;
	}

	public static boolean resetPopularity(Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put(DishProvider.DISH_ID, 1);
		// values.put(DishProvider.POPULARITY, dish.getPopularity());
		String where = DishProvider.DISH_ID + " <> 0";
		cr.update(DishProvider.DISHLIST_CONTENT_URI, values, where, null);
		return true;
	}
	public static boolean updateSport(Dish dish, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(DishProvider.NAME, dish.getName());
		values.put(DishProvider.DESCRIPTION, dish.getDescription());
		values.put(DishProvider.CATEGORY, dish.getCategory());
		values.put(DishProvider.PROTEIN, dish.getProteinStr());
		// values.put(DishProvider.POPULARITY, dish.getPopularity());
		String where = "_id" + " = " + String.valueOf(dish.getId());
		cr.update(DishProvider.DISHLIST_CONTENT_URI, values, where, null);
		return true;
	}
	public static Cursor getDishesByCategory(Context context, int category) {
		
		ContentResolver cr = context.getContentResolver();
		if(category != 1000){
		String selection = DishProvider.CATEGORY + " = " + category + " AND " + DishProvider.NAME + " > " + "''" ;

		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, DishProvider.NAME);
		return c;
		}else{
			return getPopularDishes(context);
		}
		
	}
	
	public static Cursor getPopularDishes(Context context) {
		
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.DISH_ID + " > " + 1;

		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, DishProvider.DISH_ID + " DESC "+  " LIMIT 30 ");
		return c;
	}
	
	public static int getPopularDishesCount(Context context) {

		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.DISH_ID + " > " + 0;

		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, DishProvider.DISH_ID + " DESC ");
		int res=0;
		try {
			
			res = c.getCount();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return res;
	}

	public static void increaseCatrgoryPopularity(int category, Context context) {
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put(DishProvider.DISH_ID, getCategoryPopularity(context, category) + 1);		
		// values.put(DishProvider.POPULARITY, dish.getPopularity());
		String where =DishProvider.CATEGORY + " = " + String.valueOf(category);
		cr.update(DishProvider.DISHLIST_CONTENT_URI, values, where, null);		
	}
	
	public static boolean incDishPopularity(String id, Context context) {
		
		Dish dish = getDishById(id, context);
		if(dish != null){
			dish.setPopularity(dish.getPopularity() + 1);
			updateDish(dish, context);
		}		
		return true;		
	}
	
	public static Cursor getFitnes(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + " = " + 0;

		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, null);
		return c;
	}

	public static String getDishesTypeByCategory(Context context, int category) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + "=" + category
				+ ") GROUP BY (" + DishProvider.CATEGORY_NAME;
		String res = "";
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, null);
		try {
			while (c.moveToNext()) {
				res = c.getString(c.getColumnIndex(DishProvider.TYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return res;
	}

	public static ArrayList<DishType> getUsingDishCategories(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + "<>" + "?" + ") GROUP BY ("
				+ DishProvider.CATEGORY_NAME;
		String[] columns = new String[] { DishProvider.CATEGORY_NAME,
				DishProvider.CATEGORY,  DishProvider.DISH_ID };
		String[] val = new String[] { "test" };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, val, DishProvider.CATEGORY + " DESC");
		ArrayList<DishType> list = new ArrayList<DishType>();
		if (c != null) {
			try {

				while (c.moveToNext()) {
					String category = c.getString(0);
					if (category != null) {
						list.add(new DishType(c.getInt(1), category));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return list;
	}

	public static ArrayList<DishType> getUnpopularDishCategories(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + "<>" + "?) GROUP BY ("
				+ DishProvider.CATEGORY_NAME;
		String[] columns = new String[] { DishProvider.CATEGORY_NAME,
				DishProvider.CATEGORY, DishProvider.DISH_ID };
		String[] val = new String[] { "test" };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, val, DishProvider.CATEGORY + " DESC");
		ArrayList<DishType> list = new ArrayList<DishType>();
		if (c != null) {
			try {

				while (c.moveToNext()) {
					String category = c.getString(0);
					int category_id = c.getInt(1);
					if (category != null && category_id != 0) {
						list.add(new DishType(c.getInt(1), category));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return list;
	}

	public static ArrayList<DishType> getAllDishCategories(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + "<>" + "?) GROUP BY ("
				+ DishProvider.CATEGORY_NAME;
		String[] columns = new String[] { DishProvider.CATEGORY_NAME,
				DishProvider.CATEGORY,  DishProvider.DISH_ID };
		String[] val = new String[] { "test" };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, val, DishProvider.CATEGORY + " DESC");
		ArrayList<DishType> list = new ArrayList<DishType>();
		if (c != null) {
			try {

				while (c.moveToNext()) {
					String value = c.getString(0);
					if (value != null) {
						if (c.getInt(1) != 0) {
							list.add(new DishType(c.getInt(1), value));
						} else {
							if (getDishesCount("0", context) > 1) {
								list.add(new DishType(c.getInt(1), value));
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return list;
	}
	public static int getCategoryPopularity(Context context, int category) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + "=" + category +") GROUP BY ("
				+ DishProvider.CATEGORY_NAME;
		String[] columns = new String[] { DishProvider.DISH_ID,
				DishProvider.CATEGORY };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, null, DishProvider.CATEGORY);
		if (c != null) {
			try {

				while (c.moveToNext()) {
					int value = c.getInt(0);
					if (value != 0) {
						return value;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return 0;
	}

	public static int getDishesCount(String categiryId, Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + "=" + categiryId;
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, null);
		int count = 0;
		try {
			count = c.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return count;
	}

	public static void clearAll(Context context) {
		ContentResolver cr = context.getContentResolver();
		cr.delete(DishProvider.DISHLIST_CONTENT_URI, null, null);
	}

	public static void removeDish(String id, Context context) {
		ContentResolver cr = context.getContentResolver();

		String where = "_id" + " = " + String.valueOf(id);
		// String[] val = new String[] { String.valueOf(id )};

		cr.delete(DishProvider.DISHLIST_CONTENT_URI, where, null);

	}

	public static ArrayList<FitnesType> LoadFitnesMap(Context context) {
		ArrayList<FitnesType> categories = new ArrayList<FitnesType>();
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.CATEGORY + " = " + 0;
		String[] columns = new String[] { DishProvider.NAME,
				DishProvider.PROTEIN };
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns,
				selection, null, null);
		ArrayList<Dish> result = new ArrayList<Dish>();
		if (c != null) {
			try {
				Dish res;
				while (c.moveToNext()) {
					categories.add(new FitnesType(c.getFloat(1),c.getString(0)));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return categories;
	}

	public static int getCount(Context context) {		
		ContentResolver cr = context.getContentResolver();
		String[] columns = new String[] {"count(*) as c"};
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, columns, null
				, null, null);
		
		if (c != null) {
			try {
				Dish res;
				while (c.moveToNext()) {
					return(c.getInt(0));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				c.close();
			}
		}
		return 0;
		
	}
	public static Cursor getRecepyes(Context context) {
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.DESCRIPTION + " LIKE '%recepy_1000%' ";

		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, selection,
				null, null);
		return c;
	}
	public static ArrayList<Dish> searchInAllBar(String searchString,	Context context) {
		searchString = searchString.trim();
		
		
		ContentResolver cr = context.getContentResolver();
		String selection="";
		
		selection =  DishProvider.BARCODE+ " = " + searchString;

		Log.e("AllBar", selection);
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null,
				selection, null, DishProvider.DISH_ID + " DESC "  + " LIMIT 20");
		ArrayList<Dish> result = new ArrayList<Dish>();
		if (c != null) {
			try {
				Dish res;
				while (c.moveToNext()) {
					res = new Dish();
					res.setName(c.getString(c.getColumnIndex(DishProvider.NAME)));
					res.setCategory(c.getInt(c.getColumnIndex(DishProvider.CATEGORY)));
					res.setCaloricity(c.getInt(c.getColumnIndex(DishProvider.CALORICITY)));
					// res.setPopularity(c.getInt(3));
					res.setId(c.getString(c.getColumnIndex("_id")));
					res.setIscategory(0);
					res.setType(c.getString(c.getColumnIndex(DishProvider.TYPE)));
					if(c.getString(c.getColumnIndex(DishProvider.CARBON)) == null){
						break;
					}					
					res.setCarbonStr(c.getString(c.getColumnIndex(DishProvider.CARBON)));
					res.setProteinStr(c.getString(c.getColumnIndex(DishProvider.PROTEIN)));
					res.setFatStr(c.getString(c.getColumnIndex(DishProvider.FAT)));
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
	
	public static Cursor getAllDishes(Context context) {
		ContentResolver cr = context.getContentResolver();
		//String selection =  DishProvider.TODAY_IS_DAY + " <> " + "1 and "  + DishProvider.TODAY_DISH_DATE_LONG + " > " + "?";
		Cursor c = cr.query(DishProvider.DISHLIST_CONTENT_URI, null, null, null, null);
		return c;
	}
	
	public static void importDays(List<Dish> list, Context ctx) {
		clearAll(ctx);
		for (Dish dish : list) {
			addNewDish(dish, ctx);
		}
		
	}
	
}
