package bulat.diet.helper_sport.db;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.Constants;




public class TemplateDishHelper {
	
	public static Map<String, Integer> getStatistic(Context context) {		
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<> 1" + ") GROUP BY (" + DishProvider.TODAY_TYPE;
		 String[] columns = new String[] { DishProvider.TODAY_TYPE, " sum("+DishProvider.TODAY_DISH_WEIGHT +") as weight"};
		 Long date = ((new Date()).getTime() - 30*DateUtils.DAY_IN_MILLIS) ;
		 String[] val = new String[] {date.toString()};
		 Cursor c  = null;
		 try {
			 c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");

			Map<String, Integer> result = new TreeMap<String, Integer>();
			if (c!=null){
					
					while (c.moveToNext())
			        {	
						int temp = c.getInt(1);
						try{
							if(c.getString(0)!=null){
								result.put(c.getString(0), temp);
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
			        }
					return result;
			}
		} catch (Exception e) {
				e.printStackTrace();
		}finally{
			if(c!= null){
				c.close();
			}
		}
		return null;		
}
	public static Map<String, Float> getStatisticFCP(Context context) {		
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<> 1" + ") GROUP BY (" + DishProvider.TODAY_TYPE;
		 String[] columns = new String[] { DishProvider.TODAY_TYPE, " sum("+DishProvider.TODAY_DISH_FAT +") as fat"," sum("+DishProvider.TODAY_DISH_PROTEIN +") as protein"," sum("+DishProvider.TODAY_DISH_CARBON +") as carbon"};
		 Long date = ((new Date()).getTime() - 30*DateUtils.DAY_IN_MILLIS) ;
		 String[] val = new String[] {date.toString()};
		 Cursor c  = null;
		 try {
			 c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");

			Map<String, Float> result = new TreeMap<String, Float>();
			if (c!=null){
					
					while (c.moveToNext())
			        {	
						int temp = c.getInt(1);
						try{
							if(c.getString(0)!=null){
								result.put("f", c.getFloat(1));
								result.put("p", c.getFloat(2));
								result.put("c", c.getFloat(3));
							}
							
							return result;
						}catch (Exception e) {
							e.printStackTrace();
						}
			        }
					return result;
			}
		} catch (Exception e) {
				e.printStackTrace();
		}finally{
			if(c!= null){
				c.close();
			}
		}
		return null;		
}	
public static TodayDish getDishById(String id, Context context) {
		
		ContentResolver cr = context.getContentResolver();
		String selection = "_id" + "=" + "?" ;
		String[] columns = new String[] { DishProvider.TODAY_NAME, DishProvider.TODAY_CALORICITY, 
				DishProvider.TODAY_DISH_WEIGHT, "_id", DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TYPE };
		String[] val = new String[] { id };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val,null);
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


	public static TodayDish getFitnessById(String id, Context context) {

		ContentResolver cr = context.getContentResolver();
		String selection = "_id" + "=" + "?" ;
		String[] columns = new String[] { DishProvider.TODAY_NAME, DishProvider.TODAY_CALORICITY,
				DishProvider.TODAY_DISH_WEIGHT, "_id", DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TYPE };
		String[] val = new String[] { id };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val,null);
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


	public static String addNewDish(TodayDish dish, Context context) {
		
			ContentResolver cr = context.getContentResolver();
			ContentValues values = new ContentValues();

			//values.put(DishProvider.DISH_ID, couponId);

			values.put(DishProvider.TODAY_DISH_ID, dish.getBodyweight());
			values.put(DishProvider.TODAY_NAME, dish.getName());
			values.put(DishProvider.TODAY_DESCRIPTION, dish.getDescription());
			values.put(DishProvider.TODAY_CALORICITY, dish.getCaloricity());
			values.put(DishProvider.TODAY_DISH_CALORICITY, dish.getAbsolutCaloricity());
			values.put(DishProvider.TODAY_CATEGORY, dish.getCategory());
			values.put(DishProvider.TODAY_DISH_DATE, dish.getDate());
			values.put(DishProvider.TODAY_DISH_WEIGHT, dish.getWeight());
			values.put(DishProvider.TODAY_DISH_DATE_LONG, dish.getDateTime());
			values.put(DishProvider.TODAY_IS_DAY, dish.getIsdate());
			values.put(DishProvider.TODAY_TYPE, dish.getType());
			values.put(DishProvider.TODAY_FAT, dish.getFat());
			values.put(DishProvider.TODAY_CARBON, dish.getCarbon());
			values.put(DishProvider.TODAY_PROTEIN, dish.getProtein());
			values.put(DishProvider.TODAY_DISH_FAT, dish.getAbsFat());
			values.put(DishProvider.TODAY_DISH_CARBON, dish.getAbsCarbon());
			values.put(DishProvider.TODAY_DISH_PROTEIN, dish.getAbsProtein());
			values.put(DishProvider.TODAY_DISH_TIME_HH, dish.getDateTimeHH());
			values.put(DishProvider.TODAY_DISH_TIME_MM, dish.getDateTimeMM());
			long id = ContentUris.parseId(cr.insert(DishProvider.TEMPLATE_CONTENT_URI, values));
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
		cr.update(DishProvider.TEMPLATE_CONTENT_URI, values, where, null);
		return true;		
	}
	
	public static boolean updateName(String oldName, String newName, Context context) {		
		ContentResolver cr = context.getContentResolver();	
		ContentValues values = new ContentValues();
		//values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.TODAY_DISH_DATE, newName);
		String where = DishProvider.TODAY_DISH_DATE + " = '" + oldName+ "'";
		cr.update(DishProvider.TEMPLATE_CONTENT_URI, values, where, null);
		return true;		
	}
	
	public static boolean updateDayWeight( Context context, String date_long, String weight) {
		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();

		//values.put(DishProvider.DISH_ID, couponId);
		values.put(DishProvider.TODAY_DISH_ID, weight);		
		String where = DishProvider.TODAY_DISH_DATE_LONG + " = " + date_long + " and " + DishProvider.TODAY_IS_DAY + "<> 1";
		cr.update(DishProvider.TEMPLATE_CONTENT_URI, values, where, null);
		return true;		
	}

	public static boolean removeDish(String id, Context context) {
		
		ContentResolver cr = context.getContentResolver();
		
		String where = "_id" + " = " + String.valueOf(id );
		//String[] val = new String[] { String.valueOf(id )};
		
		cr.delete(DishProvider.TEMPLATE_CONTENT_URI, where, null);
		return true;		
	}

	
	public static Cursor getDishesByDate(Context context, String date) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.TODAY_IS_DAY + " <> " + "1 and "  + DishProvider.TODAY_DISH_DATE + " = " + "?";
		String[] val = new String[] { date };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, null, selection, val, DishProvider.TODAY_DESCRIPTION);
		return c;
	}
	public static Cursor getDishesByID(Context context, String id) {
		ContentResolver cr = context.getContentResolver();
		String selection =  DishProvider.TODAY_IS_DAY + " <> " + "1 and "  + DishProvider.TODAY_DISH_DATE + " = " + "?";
		String[] val = new String[] { id };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, null, selection, val, DishProvider.TODAY_DESCRIPTION);
		return c;
	}

	//0-caloricity 1-fat 2-carbon 3-protein
	public static String[] getAvgDishCalorisity(Context context) {		
		Cursor c = getDays(context);
		int sumCal = 0;
		float sumFat = 0;
		float sumCarb = 0;
		float sumProt = 0;
		int daysCount = 0;
		
		if (c!=null){
			try {
				
				while (c.moveToNext())
		        {	
					int tempCal = c.getInt(c.getColumnIndex("val"));	
					float tempFat = c.getFloat(c.getColumnIndex("fat"));	
					float tempCarb = c.getFloat(c.getColumnIndex("carbon"));	
					float tempProt = c.getFloat(c.getColumnIndex("protein"));	
					if(tempCal > 0){
						sumCal = sumCal + tempCal;
						sumFat = sumFat + tempFat;
						sumCarb = sumCarb + tempCarb;
						sumProt = sumProt + tempProt;
						daysCount++;
					}
		        }
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				c.close();
			}
	 }
		if(daysCount == 0){
			daysCount++;
		}
		DecimalFormat df = new DecimalFormat("###.#");

		return new String[]{String.valueOf(sumCal/daysCount),df.format(sumFat/daysCount),df.format(sumCarb/daysCount),df.format(sumProt/daysCount)};
	
	}
	
	public static Cursor getDays(Context context) {
		
		//'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';
		
		ContentResolver cr = context.getContentResolver();
		//need to change DishProvider.TODAY_DISH_DATE to DishProvider.TODAY_DISH_DATE_LONG in other version
		String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "0 and " + DishProvider.TODAY_IS_DAY + "<>" + "? and " + DishProvider.TODAY_DESCRIPTION + " <>" + Constants.ACTIVITY + ") GROUP BY (" + DishProvider.TODAY_DISH_DATE;
		String[] columns = new String[] { DishProvider.TODAY_DISH_DATE, "sum("+DishProvider.TODAY_DISH_FAT +") as fat, sum("+DishProvider.TODAY_DISH_CARBON +") as carbon, sum("+DishProvider.TODAY_DISH_PROTEIN +") as protein, sum("+DishProvider.TODAY_DISH_CALORICITY +") as val, sum("+DishProvider.TODAY_DISH_WEIGHT +") as weight, _id, "+DishProvider.TODAY_DISH_ID+" as bodyweight, count(*) as count" , DishProvider.TODAY_DISH_DATE_LONG};
		String[] val = new String[] { "test" };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");
		
		return c;
	}
	
	public static ArrayList<DishType> getDaysArray(Context context) {
		
		//'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';
		
		ContentResolver cr = context.getContentResolver();
		//need to change DishProvider.TODAY_DISH_DATE to DishProvider.TODAY_DISH_DATE_LONG in other version
		String selection = DishProvider.TODAY_IS_DAY + "<>" + "? and " + DishProvider.TODAY_DESCRIPTION + " <>" + Constants.ACTIVITY + ") GROUP BY (" + DishProvider.TODAY_DISH_DATE;
		String[] columns = new String[] { DishProvider.TODAY_DISH_DATE, DishProvider.TODAY_DISH_DATE_LONG};
		String[] val = new String[] { "test" };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");
		
		ArrayList<DishType> list = new ArrayList<DishType>();
		if (c != null) {
			try {

				while (c.moveToNext()) {
					String day = c.getString(0);
					int time = c.getInt(1);
					if (day != null && time != 0) {
						list.add(new DishType(c.getInt(1), day));
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
	
	public static List<Day> getDaysStat(Context context) {
		
		//'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';
		
		ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.TODAY_IS_DAY + "<>" + "?" + ") GROUP BY (" + DishProvider.TODAY_DISH_DATE;
		 String[] columns = new String[] { DishProvider.TODAY_DISH_DATE, "sum("+DishProvider.TODAY_DISH_CALORICITY +") as val, sum("+DishProvider.TODAY_DISH_WEIGHT +") as weight, _id, "+DishProvider.TODAY_DISH_ID+" as bodyweight" , DishProvider.TODAY_DISH_DATE_LONG};
		 String[] val = new String[] { "1" };
		Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC LIMIT 30");
		ArrayList<Day> dayList = new ArrayList<Day>();
		Day d = new Day();
		try {
			
			while (c.moveToNext())
	        {
				d = new Day();
				d.setBodyWeight(Float.parseFloat(c.getString(4).replace(",", ".")));
				d.setDateInt(Long.parseLong(c.getString(5)));
				dayList.add(d);
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			c.close();
		}
		return dayList;
	}
	
	public static void clearAll(Context context) {
		ContentResolver cr = context.getContentResolver();
		cr.delete(DishProvider.TEMPLATE_CONTENT_URI, null, null);
	}

	public static String getLastDate(Context context) {
		ContentResolver cr = context.getContentResolver();		
		 String[] columns = new String[] { DishProvider.TODAY_DISH_DATE, DishProvider.TODAY_DISH_DATE_LONG  };
		 Cursor c = cr.query(DishProvider.TEMPLATE_CONTENT_URI, columns, null, null, DishProvider.TODAY_DISH_DATE_LONG +" DESC");
		 if (c!=null){
				try {
					
					while (c.moveToNext())
			        {	
						return c.getString(0);	
			        }
				}catch (Exception e) {
				}finally{
					c.close();
				}
		 }
		return null;
	}
	public static boolean removeDishesByDay(String day, Context context) {
			
			ContentResolver cr = context.getContentResolver();
			
			String where = DishProvider.TODAY_DISH_DATE + " = " + "'" + String.valueOf(day) + "'" ;
			//String[] val = new String[] { String.valueOf(id )};
			
			cr.delete(DishProvider.TEMPLATE_CONTENT_URI, where, null);
			return true;		
		}
	}
