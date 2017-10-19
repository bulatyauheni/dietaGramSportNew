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
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import bulat.diet.helper_sport.activity.VolumeInfo;
import bulat.diet.helper_sport.db.DishProvider.DatabaseHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;

public class TodayDishHelper {

    public static Cursor getAllDishes(Context context, String date) {
        ContentResolver cr = context.getContentResolver();
        //String selection =  DishProvider.TODAY_IS_DAY + " <> " + "1 and "  + DishProvider.TODAY_DISH_DATE_LONG + " > " + "?";
        String[] val = new String[]{date};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, null, null, null, null);
        return c;
    }


    public static Map<String, Integer> getStatistic(Context context) {
        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<> 1 and " + DishProvider.TODAY_CALORICITY + "> 1" + ") GROUP BY (" + DishProvider.TODAY_TYPE;
        String[] columns = new String[]{DishProvider.TODAY_TYPE, " sum(" + DishProvider.TODAY_DISH_WEIGHT + ") as weight"};
        Long date = ((new Date()).getTime() - 30 * DateUtils.DAY_IN_MILLIS);
        String[] val = new String[]{date.toString()};
        Cursor c = null;
        try {
            c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");

            Map<String, Integer> result = new TreeMap<String, Integer>();
            if (c != null) {

                while (c.moveToNext()) {
                    int temp = c.getInt(1);
                    try {
                        if (c.getString(0) != null) {
                            result.put(c.getString(0), temp);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    public static Map<String, Float> getStatisticFCP(Context context, int days) {
        return getStatisticFCP(context, 0, days);
    }

    public static Map<String, Float> getStatisticFCP(Context context, int dayFrom, int dayTo) {
        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_IS_DAY + " <> " + "1 and " + DishProvider.TODAY_DISH_DATE_LONG + "<" + "? and " + DishProvider.TODAY_DISH_DATE_LONG + ">" + "? ";
        Long dateTo = ((new Date()).getTime() - dayTo * DateUtils.DAY_IN_MILLIS);
        Long dateFrom = ((new Date()).getTime() - dayFrom * DateUtils.DAY_IN_MILLIS);
        String[] val = new String[]{dateFrom.toString(), dateTo.toString()};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, null, selection, val, DishProvider.TODAY_DESCRIPTION);
        ArrayList<TodayDish> dayList = new ArrayList<TodayDish>();
        TodayDish d = new TodayDish();
        try {
            while (c.moveToNext()) {
                d = new TodayDish();
                d.setId(c.getString(c.getColumnIndex("_id")));
                d.setName(c.getString(c.getColumnIndex(DishProvider.TODAY_NAME)));
                d.setDayTyme(c.getString(c.getColumnIndex(DishProvider.TODAY_DESCRIPTION)));
                d.setCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                d.setProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                d.setFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_FAT))));
                d.setAbsCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_CARBON))));
                d.setAbsProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_PROTEIN))));
                d.setAbsFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_FAT))));
                d.setCaloricity(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY))));
                d.setWeight(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                d.setDateTimeHH(c.getInt(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH)));
                d.setDateTimeMM(c.getInt(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM)));
                dayList.add(d);
            }
            float sumF = 0;
            float sumC = 0;
            float sumP = 0;
            int sum = 0;
            int sumMinus = 0;
            for (TodayDish dish : dayList) {
                if (dish.getCaloricity()>=0) {
                    sumC = sumC + dish.getCarbon();
                    sumF = sumF + dish.getFat();
                    sumP = sumP + dish.getProtein();
                    sum = sum + dish.getCaloricity();
                } else {
                    sumMinus = sumMinus + dish.getCaloricity();
                }
            }
            Map<String, Float> result = new TreeMap<String, Float>();
            result.put("p", sumP);
            result.put("c", sumC);
            result.put("f", sumF);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return null;
        /////
        /*ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE_LONG + "<" + "? and " + DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and CAST(" + DishProvider.TODAY_DISH_CALORICITY + "value AS INTEGER) >= 0";
        String[] columns = new String[]{DishProvider.TODAY_TYPE, " sum(" + DishProvider.TODAY_DISH_FAT + ") as fat", " sum(" + DishProvider.TODAY_DISH_PROTEIN + ") as protein", " sum(" + DishProvider.TODAY_DISH_CARBON + ") as carbon"};
        Long dateTo = ((new Date()).getTime() - dayTo * DateUtils.DAY_IN_MILLIS);
        Long dateFrom = ((new Date()).getTime() - dayFrom * DateUtils.DAY_IN_MILLIS);
        String[] val = new String[]{dateFrom.toString(), dateTo.toString()};
        Cursor c = null;
        try {
            c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");

            Map<String, Float> result = new TreeMap<String, Float>();
            if (c != null) {

                while (c.moveToNext()) {
                    int temp = c.getInt(1);
                    try {
                        if (c.getString(0) != null) {
                            result.put("p", c.getFloat(2));
                            result.put("c", c.getFloat(3));
                            result.put("f", c.getFloat(1));
                        }

                        return result;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;*/
    }

    public static Map<String, Float> getStatisticFCP(Context context, String date) {
        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_IS_DAY + " <> " + "1 and " +  DishProvider.TODAY_DISH_DATE + "=" + "'" + String.valueOf(date) +"' "  ;
        String[] val = new String[]{};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, null, selection, val, DishProvider.TODAY_DESCRIPTION);
        ArrayList<TodayDish> dayList = new ArrayList<TodayDish>();
        TodayDish d = new TodayDish();
        try {
            while (c.moveToNext()) {
                d = new TodayDish();
                d.setId(c.getString(c.getColumnIndex("_id")));
                d.setName(c.getString(c.getColumnIndex(DishProvider.TODAY_NAME)));
                d.setDayTyme(c.getString(c.getColumnIndex(DishProvider.TODAY_DESCRIPTION)));
                d.setCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                d.setProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                d.setFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_FAT))));
                d.setAbsCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_CARBON))));
                d.setAbsProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_PROTEIN))));
                d.setAbsFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_FAT))));
                d.setCaloricity(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY))));
                d.setWeight(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                d.setDateTimeHH(c.getInt(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH)));
                d.setDateTimeMM(c.getInt(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM)));
                dayList.add(d);
            }
            float sumF = 0;
            float sumC = 0;
            float sumP = 0;
            int sum = 0;
            int sumMinus = 0;
            for (TodayDish dish : dayList) {
                if (dish.getCaloricity()>=0) {
                    sumC = sumC + dish.getCarbon();
                    sumF = sumF + dish.getFat();
                    sumP = sumP + dish.getProtein();
                    sum = sum + dish.getCaloricity();
                } else {
                    sumMinus = sumMinus + dish.getCaloricity();
                }
            }
            Map<String, Float> result = new TreeMap<String, Float>();
            result.put("p", sumP);
            result.put("c", sumC);
            result.put("f", sumF);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return null;
        /*ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE + "=" + "'" + String.valueOf(date) + "' and " + DishProvider.TODAY_IS_DAY + "<> 1 " + ") GROUP BY (" + DishProvider.TODAY_DISH_DATE;
        String[] columns = new String[]{DishProvider.TODAY_TYPE, " sum(" + DishProvider.TODAY_DISH_FAT + ") as fat", " sum(" + DishProvider.TODAY_DISH_PROTEIN + ") as protein", " sum(" + DishProvider.TODAY_DISH_CARBON + ") as carbon"};
        String[] val = new String[]{};
        Cursor c = null;

        try {
            c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");

            Map<String, Float> result = new TreeMap<String, Float>();
            if (c != null) {

                while (c.moveToNext()) {
                    int temp = c.getInt(1);
                    try {
                        if (c.getString(0) != null) {
                            result.put("f", c.getFloat(1));
                            result.put("p", c.getFloat(2));
                            result.put("c", c.getFloat(3));
                        }

                        return result;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;*/
    }

    public static TodayDish getDishById(String id, Context context) {

        ContentResolver cr = context.getContentResolver();
        String selection = "_id" + "=" + "?";
        String[] columns = new String[]{DishProvider.TODAY_NAME, DishProvider.TODAY_CALORICITY, DishProvider.TODAY_DESCRIPTION,
                DishProvider.TODAY_DISH_WEIGHT, "_id", DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TYPE, DishProvider.TODAY_CATEGORY, DishProvider.TODAY_FAT,
                DishProvider.TODAY_PROTEIN, DishProvider.TODAY_CARBON, DishProvider.TODAY_DISH_FAT,
                DishProvider.TODAY_DISH_PROTEIN, DishProvider.TODAY_DISH_CARBON, DishProvider.TODAY_CALORICITY, DishProvider.TODAY_DISH_TIME_HH, DishProvider.TODAY_DISH_TIME_MM};
        String[] val = new String[]{id};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, null);
        TodayDish res = new TodayDish();
        if (c != null) {
            try {

                while (c.moveToNext()) {

                    res.setId(c.getString(c.getColumnIndex("_id")));
                    res.setName(c.getString(c.getColumnIndex(DishProvider.TODAY_NAME)));
                    res.setDayTyme(String.valueOf(c.getInt(c.getColumnIndex(DishProvider.TODAY_DESCRIPTION))));
                    res.setCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_CARBON))));
                    res.setProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_PROTEIN))));
                    res.setFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_FAT))));

                    res.setAbsCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                    res.setAbsProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                    res.setAbsFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_FAT))));

                    res.setCaloricity(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_CALORICITY))));
                    res.setWeight(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                    res.setDateTimeHH(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH))));
                    res.setDateTimeMM(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM))));
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

    public static TodayDish getDishByName(String name, Context context) {

        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_NAME + "=" + "?";
        String[] columns = new String[]{DishProvider.TODAY_NAME, DishProvider.TODAY_CALORICITY, DishProvider.TODAY_DESCRIPTION,
                DishProvider.TODAY_DISH_WEIGHT, "_id", DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TYPE, DishProvider.TODAY_CATEGORY, DishProvider.TODAY_FAT,
                DishProvider.TODAY_PROTEIN, DishProvider.TODAY_CARBON, DishProvider.TODAY_DISH_FAT,
                DishProvider.TODAY_DISH_PROTEIN, DishProvider.TODAY_DISH_CARBON, DishProvider.TODAY_CALORICITY, DishProvider.TODAY_DISH_TIME_HH, DishProvider.TODAY_DISH_TIME_MM};
        String[] val = new String[]{name};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, "_id" + " DESC");
        TodayDish res = new TodayDish();
        if (c != null) {
            try {

                while (c.moveToNext()) {

                    res.setId(c.getString(c.getColumnIndex("_id")));
                    res.setName(c.getString(c.getColumnIndex(DishProvider.TODAY_NAME)));
                    res.setDayTyme(String.valueOf(c.getInt(c.getColumnIndex(DishProvider.TODAY_DESCRIPTION))));
                    res.setCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_CARBON))));
                    res.setProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_PROTEIN))));
                    res.setFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_FAT))));

                    res.setAbsCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                    res.setAbsProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                    res.setAbsFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_FAT))));

                    res.setCaloricity(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_CALORICITY))));
                    res.setWeight(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                    res.setDateTimeHH(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH))));
                    res.setDateTimeMM(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM))));
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

    public static TodayDish getFitnessById(String id, Context context) {

        ContentResolver cr = context.getContentResolver();
        String selection = "_id" + "=" + "?";
        String[] columns = new String[]{DishProvider.TODAY_NAME, DishProvider.TODAY_CALORICITY, DishProvider.TODAY_DESCRIPTION,
                DishProvider.TODAY_DISH_WEIGHT, "_id", DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TYPE, DishProvider.TODAY_CATEGORY, DishProvider.TODAY_DISH_FAT,
                DishProvider.TODAY_DISH_PROTEIN, DishProvider.TODAY_DISH_CARBON, DishProvider.TODAY_DISH_CALORICITY, DishProvider.TODAY_DISH_TIME_HH, DishProvider.TODAY_DISH_TIME_MM};
        String[] val = new String[]{id};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, null);
        TodayDish res = new TodayDish();
        if (c != null) {
            try {

                while (c.moveToNext()) {

                    res.setId(c.getString(c.getColumnIndex("_id")));
                    res.setName(c.getString(c.getColumnIndex(DishProvider.TODAY_NAME)));
                    res.setDayTyme(String.valueOf(c.getInt(c.getColumnIndex(DishProvider.TODAY_DESCRIPTION))));
                    res.setCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                    res.setProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                    res.setFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_FAT))));
                    res.setCaloricity(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY))));
                    res.setWeight(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                    res.setDateTimeHH(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH))));
                    res.setDateTimeMM(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM))));
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

    public static float getBodyWeightByDate(long date, Context context) {

        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE_LONG + "=" + "?";
        String[] columns = new String[]{DishProvider.TODAY_DISH_ID};
        String[] val = new String[]{String.valueOf(date)};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, null);
        TodayDish res = new TodayDish();
        if (c != null) {
            try {

                while (c.moveToNext()) {
                    if (c.getFloat(0) > 0) {
                        return c.getFloat(0);
                    }
                }
                return SaveUtils.getRealWeight(context);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
        }
        return SaveUtils.getRealWeight(context);
    }

    public static float getBodyWeightByDate(String date, Context context) {

        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE + "=" + "?";
        String[] columns = new String[]{DishProvider.TODAY_DISH_ID};
        String[] val = new String[]{String.valueOf(date)};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, null);
        TodayDish res = new TodayDish();
        if (c != null) {
            try {

                while (c.moveToNext()) {
                    if (c.getFloat(0) > 0) {
                        return c.getFloat(0);
                    }
                }
                return SaveUtils.getRealWeight(context);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
        }
        return SaveUtils.getRealWeight(context);
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
        if (dish.getServerId() > 0) {
            values.put(DishProvider.TODAY_DISH_SERVER_ID, dish.getServerId());
        }

        long id = ContentUris.parseId(cr.insert(DishProvider.TODAYDISH_CONTENT_URI, values));
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
        cr.update(DishProvider.TODAYDISH_CONTENT_URI, values, where, null);
        return true;
    }

    public static boolean updateBobyParams(Context context, String date, String weight, BodyParams bp) {

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        //values.put(DishProvider.DISH_ID, couponId);
        values.put(DishProvider.TODAY_DISH_ID, weight);
        values.put(DishProvider.TODAY_CHEST, bp.getChest());
        values.put(DishProvider.TODAY_BICEPS, bp.getBiceps());
        values.put(DishProvider.TODAY_PELVIS, bp.getPelvis());
        values.put(DishProvider.TODAY_WAIST, bp.getWaist());
        values.put(DishProvider.TODAY_SHIN, bp.getShin());
        values.put(DishProvider.TODAY_NECK, bp.getNeck());
        values.put(DishProvider.TODAY_FOREARM, bp.getForearm());
        values.put(DishProvider.TODAY_HIP, bp.getHip());

        String where = DishProvider.TODAY_DISH_DATE + " = " + "'" + String.valueOf(date) + "'";
        cr.update(DishProvider.TODAYDISH_CONTENT_URI, values, where, null);
        return true;
    }

    public static boolean removeDish(String id, Context context) {

        ContentResolver cr = context.getContentResolver();

        String where = "_id" + " = " + String.valueOf(id);
        //String[] val = new String[] { String.valueOf(id )};

        cr.delete(DishProvider.TODAYDISH_CONTENT_URI, where, null);
        return true;
    }

    public static boolean removeSinchDish(Context context) {

        ContentResolver cr = context.getContentResolver();

        String where = DishProvider.TODAY_DISH_SERVER_ID + " > " + 0;
        //String[] val = new String[] { String.valueOf(id )};

        cr.delete(DishProvider.TODAYDISH_CONTENT_URI, where, null);
        return true;
    }

    public static boolean removeDishesByDay(String day, Context context) {

        ContentResolver cr = context.getContentResolver();

        String where = DishProvider.TODAY_DISH_DATE + " = " + "'" + String.valueOf(day) + "'";
        //String[] val = new String[] { String.valueOf(id )};

        cr.delete(DishProvider.TODAYDISH_CONTENT_URI, where, null);
        return true;
    }


    public static Cursor getDishesByDate(Context context, String date) {
        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_IS_DAY + " <> " + "1 and " + DishProvider.TODAY_DISH_DATE + " = " + "?";
        String[] val = new String[]{date};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, null, selection, val, DishProvider.TODAY_DESCRIPTION);
        return c;
    }

    public static ArrayList<TodayDish> getArrayDishesByDate(Context context, String date) {
        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_IS_DAY + " <> " + "1 and " + DishProvider.TODAY_DISH_DATE + " = " + "?";
        String[] val = new String[]{date};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, null, selection, val, DishProvider.TODAY_DESCRIPTION);
        ArrayList<TodayDish> dayList = new ArrayList<TodayDish>();
        TodayDish d = new TodayDish();
        try {
            while (c.moveToNext()) {
                d = new TodayDish();
                d.setId(c.getString(c.getColumnIndex("_id")));
                d.setName(c.getString(c.getColumnIndex(DishProvider.TODAY_NAME)));
                d.setDayTyme(c.getString(c.getColumnIndex(DishProvider.TODAY_DESCRIPTION)));
                d.setCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                d.setProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                d.setFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_FAT))));
                d.setAbsCarbon(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_CARBON))));
                d.setAbsProtein(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_PROTEIN))));
                d.setAbsFat(Float.parseFloat(c.getString(c.getColumnIndex(DishProvider.TODAY_FAT))));
                d.setCaloricity(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY))));
                d.setWeight(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                d.setDateTimeHH(c.getInt(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH)));
                d.setDateTimeMM(c.getInt(c.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM)));
                dayList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return dayList;
    }

    public static int getAllCouponsCount(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr
                .query(DishProvider.TODAYDISH_CONTENT_URI, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    //0-caloricity 1-fat 2-carbon 3-protein
    public static String[] getAvgDishCalorisity(Context context) {
        Cursor c = getDaysNew(context);
        int sumCal = 0;
        float sumFat = 0;
        float sumCarb = 0;
        float sumProt = 0;
        int daysCount = 0;

        if (c != null) {
            try {

                while (c.moveToNext()) {
                    int tempCal = c.getInt(c.getColumnIndex("val"));
                    float tempFat = c.getFloat(c.getColumnIndex("fat"));//?0:c.getFloat(c.getColumnIndex("fat"));
                    float tempCarb = c.getFloat(c.getColumnIndex("carbon"));
                    float tempProt = c.getFloat(c.getColumnIndex("protein"));
                    if (tempCal != 0) {
                        sumCal = sumCal + tempCal;
                        sumFat = sumFat + tempFat;
                        sumCarb = sumCarb + tempCarb;
                        sumProt = sumProt + tempProt;
                        daysCount++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
        }
        if (daysCount == 0) {
            daysCount++;
        }
        DecimalFormat df = new DecimalFormat("###.#");

        return new String[]{String.valueOf(sumCal / daysCount), df.format(sumFat / daysCount), df.format(sumCarb / daysCount), df.format(sumProt / daysCount)};

    }

    public static Cursor getDays(Context context) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';

        ContentResolver cr = context.getContentResolver();
        //need to change DishProvider.TODAY_DISH_DATE to DishProvider.TODAY_DISH_DATE_LONG in other version
        String selection = DishProvider.TODAY_TYPE + "<> '' and " + DishProvider.TODAY_IS_DAY + "<>" + "? ) GROUP BY (" + DishProvider.TODAY_DISH_DATE;
        String[] columns = new String[]{DishProvider.TODAY_DISH_DATE, "sum(" + DishProvider.TODAY_DISH_FAT + ") as fat, sum(" + DishProvider.TODAY_DISH_CARBON + ") as carbon, sum(" + DishProvider.TODAY_DISH_PROTEIN + ") as protein, sum(" + DishProvider.TODAY_DISH_CALORICITY + ") as val, sum(" + DishProvider.TODAY_DISH_WEIGHT + ") as weight, _id, " + DishProvider.TODAY_DISH_ID + " as bodyweight, count(*) as count", DishProvider.TODAY_DISH_DATE_LONG};
        String[] val = new String[]{"test"};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, DishProvider.TODAY_DISH_DATE_LONG + " DESC");

        return c;
    }

    public static int getDaysCount(Context context) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';
        int res = 0;
        Cursor c = getDaysNew(context);
        try {
            if (c != null) res = c.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return res;
    }

    static SQLiteDatabase allDishesDB = null;

    public static Cursor getDaysNew(Context context) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        if (allDishesDB == null) {
            allDishesDB = dbHelper.getWritableDatabase();
        }

        ContentResolver cr = context.getContentResolver();
        //need to change DishProvider.TODAY_DISH_DATE to DishProvider.TODAY_DISH_DATE_LONG in other version

        Cursor c = allDishesDB.rawQuery(
                "Select " + DishProvider.TODAY_DISH_DATE + " , sum(" + DishProvider.TODAY_DISH_CALORICITY + ") as val, sum(fatw) as fat, sum(carbw) as carbon, sum(protw) as protein, " +
                        " sum(clearweight) as weight, sum(waterweight) as woterweight, _id, " + DishProvider.TODAY_DISH_ID + " as bodyweight, count(_id) as count ," + DishProvider.TODAY_DISH_DATE_LONG +
                        "," + DishProvider.TODAY_BICEPS + "," + DishProvider.TODAY_CHEST + "," + DishProvider.TODAY_NECK + "," + DishProvider.TODAY_FOREARM + "," + DishProvider.TODAY_PELVIS +
                        "," + DishProvider.TODAY_SHIN + "," + DishProvider.TODAY_WAIST + "," + DishProvider.TODAY_HIP + "," + DishProvider.TODAY_DISH_SERVER_ID +
                        " from day_dishes as a " +
                        "left JOIN (SELECT _id as _id2, " + DishProvider.TODAY_DISH_WEIGHT + " as clearweight , " + DishProvider.TODAY_DISH_FAT + " as fatw, " + DishProvider.TODAY_DISH_CARBON + " as carbw, " + DishProvider.TODAY_DISH_PROTEIN + " as protw" +
                        " FROM    day_dishes" +
                        " WHERE " + DishProvider.TODAY_CATEGORY + " <> 0 and " + DishProvider.TODAY_CALORICITY + " <> 0) as b ON a._id=b._id2 " +
                        "left JOIN (SELECT _id as _id3, " + DishProvider.TODAY_DISH_WEIGHT + " as waterweight " +
                        "FROM  day_dishes " +
                        "WHERE " + DishProvider.TODAY_DISH_CALORICITY + " = 0 ) as c ON a._id=c._id3 " +
                        "GROUP BY " + DishProvider.TODAY_DISH_DATE_LONG + " order by " + DishProvider.TODAY_DISH_DATE_LONG + " DESC", null);
        return c;
    }

    public static List<Day> getDaysStat(Context context) {
        return getDaysStat(context, 30);
    }

    public static String getTotalWeightLoose(Context context) {
        List<Day> weights = getDaysStat(context, 366);
        if (weights != null && weights.size() > 2) {
            return String.valueOf(weights.get(1).getBodyWeight() - weights.get(weights.size() - 1).getBodyWeight());
        } else {
            return "";
        }
    }

    public static List<Day> getDaysStat(Context context, int days) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';
        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_ID + " <> 0 and " + DishProvider.TODAY_CHEST + " <> 0 " + ") GROUP BY (" + DishProvider.TODAY_DISH_DATE;
        String[] columns = new String[]{DishProvider.TODAY_DISH_DATE, "sum(" + DishProvider.TODAY_DISH_CALORICITY + ") as val, sum(" + DishProvider.TODAY_DISH_WEIGHT + ") as bodyweight, _id, " + DishProvider.TODAY_DISH_ID + " as bodyweight, " + DishProvider.TODAY_DISH_DATE_LONG, DishProvider.TODAY_FOREARM, DishProvider.TODAY_WAIST, DishProvider.TODAY_HIP, DishProvider.TODAY_NECK, DishProvider.TODAY_SHIN, DishProvider.TODAY_PELVIS, DishProvider.TODAY_BICEPS, DishProvider.TODAY_CHEST};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, null, DishProvider.TODAY_DISH_DATE_LONG + " DESC LIMIT " + days + " "); //getDaysNew(context);
        ArrayList<Day> dayList = new ArrayList<Day>();
        Day d = new Day();
        try {

            while (c.moveToNext()) {
                d = new Day();
                d.setBodyWeight(Float.parseFloat(c.getString(c
                        .getColumnIndex("bodyweight")).replace(",", ".")));
                d.setDateInt(Long.parseLong(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_DATE_LONG))));

                d.setDateStr(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_DATE)));

                d.setBiceps(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_BICEPS))) + VolumeInfo.MIN_CHEST);
                d.setChest(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_CHEST))) + VolumeInfo.MIN_CHEST);
                d.setNeck(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_NECK))) + VolumeInfo.MIN_NECK);
                d.setForearm(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_FOREARM))) + VolumeInfo.MIN_FOREARM);
                d.setPelvis(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_PELVIS))) + VolumeInfo.MIN_PELVIS);
                d.setShin(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_SHIN))) + VolumeInfo.MIN_SHIN);
                d.setWaist(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_WAIST))) + VolumeInfo.MIN_WAIST);
                d.setHip(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_HIP))) + VolumeInfo.MIN_HIP);

                dayList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return dayList;
    }

    public static List<TodayDish> getTopDishesForPeriod(Context context, int days) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';

        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_CALORICITY + ">=" + "0 and " + DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<>" + "1" + ") GROUP BY (" + DishProvider.TODAY_NAME;
        String[] columns = new String[]{"count(" + DishProvider.TODAY_NAME + ") as popindex ", DishProvider.TODAY_NAME, DishProvider.TODAY_TYPE, DishProvider.TODAY_CATEGORY, DishProvider.TODAY_CALORICITY, DishProvider.TODAY_DISH_WEIGHT,
                DishProvider.TODAY_PROTEIN, DishProvider.TODAY_CARBON, DishProvider.TODAY_FAT};

        Long date = ((new Date()).getTime() - days * DateUtils.DAY_IN_MILLIS);
        String[] val = new String[]{date.toString()};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, "popindex" + " DESC LIMIT 15");
        ArrayList<TodayDish> dayList = new ArrayList<TodayDish>();
        TodayDish d = new TodayDish();
        try {

            while (c.moveToNext()) {
                d = new TodayDish();

                d.setName(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_NAME)));
                d.setCarbon(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_CARBON))));
                d.setProtein(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_PROTEIN))));
                d.setFat(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_FAT))));
                d.setCaloricity(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_CALORICITY))));
                d.setType(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_TYPE)));
                d.setCategory(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_CATEGORY)));
                d.setWeight(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));
                dayList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return dayList;
    }

    public static Cursor getTopDishesCursor(Context context) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';

        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<>" + "1" + ") GROUP BY (" + DishProvider.TODAY_NAME;
        String[] columns = new String[]{"_id", DishProvider.TODAY_NAME, "sum(" + DishProvider.TODAY_DISH_CALORICITY + ") as t_absolutecaloricity, sum(" + DishProvider.TODAY_DISH_WEIGHT + ") as t_weight, sum(" + DishProvider.TODAY_DISH_PROTEIN + ") as t_absprotein, sum(" + DishProvider.TODAY_DISH_CARBON + ") as t_abscarbon,sum(" + DishProvider.TODAY_DISH_FAT + ") as t_absfat"};

        Long date = ((new Date()).getTime() - 30 * DateUtils.DAY_IN_MILLIS);
        String[] val = new String[]{date.toString()};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, "t_absolutecaloricity" + " DESC LIMIT 10");
        /*ContentResolver cr = context.getContentResolver();
		String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<>" + "1" + ") GROUP BY (" + DishProvider.TODAY_NAME;
		String[] columns = new String[] {"_id" , DishProvider.TODAY_NAME, "sum("+DishProvider.TODAY_DISH_CALORICITY +") as t_absolutecaloricity, sum("+DishProvider.TODAY_DISH_WEIGHT +") as t_weight, sum("+DishProvider.TODAY_DISH_PROTEIN +") as t_absprotein, sum("+DishProvider.TODAY_DISH_CARBON +") as t_abscarbon,sum("+DishProvider.TODAY_DISH_FAT +") as t_absfat"};

		Long date = ((new Date()).getTime() - 30*DateUtils.DAY_IN_MILLIS) ;
		String[] val = new String[] {date.toString()};
		Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, "t_absolutecaloricity" + " DESC LIMIT 10");*/
        return c;
    }

    public static ArrayList<TodayDish> getTopDishesList(Context context, String orderby, int days) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';

        ContentResolver cr = context.getContentResolver();
        String selection = DishProvider.TODAY_DISH_DATE_LONG + ">" + "? and " + DishProvider.TODAY_IS_DAY + "<>" + "1" + ") GROUP BY (" + DishProvider.TODAY_NAME;
        String[] columns = new String[]{"_id", DishProvider.TODAY_NAME, "sum(" + DishProvider.TODAY_DISH_CALORICITY + ") as t_absolutecaloricity, sum(" + DishProvider.TODAY_DISH_WEIGHT + ") as t_weight, sum(" + DishProvider.TODAY_DISH_PROTEIN + ") as t_absprotein, sum(" + DishProvider.TODAY_DISH_CARBON + ") as t_abscarbon,sum(" + DishProvider.TODAY_DISH_FAT + ") as t_absfat"};

        Long date = ((new Date()).getTime() - days * DateUtils.DAY_IN_MILLIS);
        String[] val = new String[]{date.toString()};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, selection, val, orderby + " DESC LIMIT 10");

        ArrayList<TodayDish> dayList = new ArrayList<TodayDish>();
        TodayDish d = new TodayDish();
        try {

            while (c.moveToNext()) {
                d = new TodayDish();
                d.setId(c.getString(c
                        .getColumnIndex("_id")));
                d.setName(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_NAME)));

                d.setAbsCarbon(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                d.setAbsFat(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_FAT))));
                d.setAbsProtein(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                d.setAbsolutCaloricity(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_CALORICITY))));
                dayList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return dayList;
    }


    public static void clearAll(Context context) {
        ContentResolver cr = context.getContentResolver();
        cr.delete(DishProvider.TODAYDISH_CONTENT_URI, null, null);
    }

    public static String getLastDate(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] columns = new String[]{DishProvider.TODAY_DISH_DATE, DishProvider.TODAY_DISH_DATE_LONG};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns, null, null, DishProvider.TODAY_DISH_DATE_LONG + " DESC");
        if (c != null) {
            try {

                while (c.moveToNext()) {
                    return c.getString(0);
                }
            } catch (Exception e) {
            } finally {
                c.close();
            }
        }
        return null;


    }

    public static void importDays(List<TodayDish> list, Context ctx) {
        clearAll(ctx);
        for (TodayDish todayDish : list) {
            addNewDish(todayDish, ctx);
        }

    }

    //unsichronized dishes (withiout server eqvivalent id)
    public static List<TodayDish> getUnsincDishes(Context context) {

        //'SELECT date, sum(value) as value  FROM dishs GROUP BY date ORDER BY id';

        ContentResolver cr = context.getContentResolver();
        String selection = "ifnull(" + DishProvider.TODAY_DISH_SERVER_ID + ", '') = '' and " + DishProvider.TODAY_IS_DAY + "<>" + "1";

        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, null, selection, null, null);
        ArrayList<TodayDish> dayList = new ArrayList<TodayDish>();
        TodayDish d = new TodayDish();
        try {

            while (c.moveToNext()) {
                d = new TodayDish();
                d.setId(c.getString(c
                        .getColumnIndex("_id")));
                d.setName(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_NAME)));
                d.setCarbon(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_CARBON))));
                d.setDescription(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DESCRIPTION)));
                d.setProtein(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_PROTEIN))));
                d.setFat(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_FAT))));
                d.setWeight(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_WEIGHT))));

                d.setBodyweight(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_ID))));
                d.setAbsCarbon(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_CARBON))));
                d.setAbsFat(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_FAT))));
                d.setAbsProtein(Float.parseFloat(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));
                d.setAbsolutCaloricity(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_CALORICITY))));
                d.setCaloricity(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_CALORICITY))));
                d.setDate(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_DATE)));
                d.setDateTime(c.getLong(c
                        .getColumnIndex(DishProvider.TODAY_DISH_DATE_LONG)));
                d.setDateTimeHH(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_TIME_HH))));
                d.setDateTimeMM(Integer.parseInt(c.getString(c
                        .getColumnIndex(DishProvider.TODAY_DISH_TIME_MM))));
                dayList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return dayList;
    }

    public static boolean sincDish(int id, int serverId, Context context) {

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(DishProvider.TODAY_DISH_SERVER_ID, serverId);
        String where = "_id" + " = " + String.valueOf(id);
        cr.update(DishProvider.TODAYDISH_CONTENT_URI, values, where, null);
        return true;
    }

    public static String getLastServerId(Context context) {

        ContentResolver cr = context.getContentResolver();
        String[] columns = new String[]{DishProvider.TODAY_DISH_SERVER_ID};
        Cursor c = cr.query(DishProvider.TODAYDISH_CONTENT_URI, columns,
                null, null, DishProvider.TODAY_DISH_SERVER_ID + " desc");
        if (c != null) {
            try {

                while (c.moveToNext()) {
                    String id = String.valueOf(c.getInt(0));
                    return id;
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
}
