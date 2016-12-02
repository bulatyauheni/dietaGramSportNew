package bulat.diet.helper_sport.utils;


import java.util.Random;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.VolumeInfo;

public class SaveUtils {
	
	public static final String BMI = "BMI";
	public static final String BMR = "BMR";
	public static final String META = "META";
	public static final String HEIGHT = "HIGHT";
	public static final String WEIGHT = "WEIGHT";
	public static final String AGE = "AGE";
	public static final String SEX = "SEX";
	public static final String ACTIVITY = "ACTIVITY";
	public static final String SEARCH_HEIGHT = "SEARCH_HIGHT";
	public static final String SEARCH_WEIGHT = "SEARCH_WEIGHT";
	public static final String SEARCH_AGE = "SEARCH_AGE";
	public static final String SEARCH_SEX = "SEARCH_SEX";
	public static final String SEARCH_ACTIVITY = "SEARCH_ACTIVITY";

	public static final String FIRSTTIME = "FIRSTTIMERun";
	public static final String ACTIVATED = "activated";
	private static final String MODE = "mode";
	private static final String EXPMODE = "expmode";
	private static final String SEARCH_WEIGHT_ENBL = "weight_chec";
	private static final String SEARCH_SEX_ENBL = "sex_chec";
	private static final String SEARCH_HEIGHT_ENBL = "height_chec";
	private static final String SEARCH_ACTIVITY_ENBL = "activity_chec";
	private static final String SEARCH_AGE_ENBL = "age_chec";
	private static final String NIKNAME = "nikname";
	private static final String SEARCH_NAME_ENBL = "name_chec";
	private static final String SEARCH_NAME = "searchname";
	private static final String USERUNICID = "userunicid";
	private static final String REQUEST = "requestque";
	public static final String SEPARATOR = "separator";
	private static final String REQUESTDISH = "requestdish";
	private static final String MESSAGESCOUNT = "messages_count";
	private static final String USERUNICIDFAKE = "fakenum";
	private static final String SCROLLPOSS = "scrollposs";
	private static final String WEIGHT_DEC = "weight_dec";
	private static final String NUMB_DEC = "numb_dec";
	private static final String LIFESTYLE = "LIFESTYLE";
	private static final String LASTTIME = "LASTTIME";
	private static final String NEXTINT = "nextint";
	private static final String TYPE = "type";
	private static final String LASTVISITTIME = "LASTVISITTIME";
	private static final String LANG = "LANG";
	private static final String EXPFILENAME = "EXPFILENAME";
	private static final String RESET = "RESET";
	public static final String LIMIT = "LIMIT";
	private static final String NOTIF = "NOTIF";
	private static final String CHESTDEC = "CHESTDEC";
	private static final String CHEST = "CHEST";
	private static final String SHINDEC = "SHINDEC";
	private static final String SHIN = "SHIN";
	private static final String HIPDEC = "HIPDEC";
	private static final String HIP = "HIP";
	private static final String WAISTDEC = "WAISTDEC";
	private static final String WAIST = "WAIST";
	private static final String FOREARMDEC = "FOREARMDEC";
	private static final String FOREARM = "FOREARM";
	private static final String BICEPSDEC = "BICEPSDEC";
	private static final String BICEPS = "BICEPS";
	private static final String NECKDEC = "NECKDEC";
	private static final String NECK = "NECK";
	private static final String PELVISDEC = "PELVISDEC";
	private static final String PELVIS = "PELVIS";
	private static final String EXPMODEVALUE = "EXPMODEVALUE";
	private static final String CHESTENBL = "CHESTENBL";
	private static final String SHINENBL = "SHINENBL";
	private static final String NECENBL = "NECENBL";
	private static final String WAISTENBL = "WAISTENBL";
	private static final String FORENBL = "FORENBL";
	private static final String BITKAENBL = "BITKAENBL";
	private static final String HIPENBL = "HIPENBL";
	private static final String PELVISENBL = "PELVISENBL";
	private static final String PAYKEY = "PAYKEY";
	private static final String PAYDDAYS = "PAYDDAYS";
	private static final String FREEDAYSCOUNT = "FREEDAYSCOUNT";
	private static final String USERADVICID = "USERADVICID";
	private static final String LASTADVUPDATETIME = "LASTADVUPDATETIME";
	private static final String ENDPDATE = "ENDPDATE";
	private static final String LASTRATIONID = "LASTRATIONID";
	public static final String LIMIT_PROTEIN = "LIMIT_PROTEIN";
	public static final String LIMIT_CARBON = "LIMIT_CARBON";
	public static final String LIMIT_FAT = "LIMIT_FAT";
	public static final String EXPDATABASEFILENAME = "EXPDATABASEFILENAME";
	public static final String USER_IDEAL_WEIGHT = "USER_IDEAL_WEIGHT";
	public static final String USER_IDEAL_CHEST = "USER_IDEAL_CHEST";
	public static final String USER_IDEAL_HIP = "USER_IDEAL_HIP";
	public static final String USER_IDEAL_FOREARM = "USER_IDEAL_FOREARM";
	public static final String USER_IDEAL_BICEPS = "USER_IDEAL_BICEPS";
	public static final String USER_IDEAL_NECK = "USER_IDEAL_NECK";
	public static final String USER_IDEAL_SHIN = "USER_IDEAL_SHIN";
	public static final String USER_IDEAL_PELVIS = "USER_IDEAL_PELVIS";
	public static final String USER_IDEAL_WAIST = "USER_IDEAL_WAIST";
	private static final String USER_RATE_APP = "USER_RATE_APP";
	private static final String USER_USE_FREE_ABONEMENT = "USER_USE_FREE_ABONEMENT";

	
	public static void saveScrollPosition(int pos, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(SCROLLPOSS, pos);
		
		editor.commit();
	}
	public static int loadScrollPosition(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(SCROLLPOSS, 0);
	}
	public static int getNextInt(Context context){
		
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		int res =preferences.getInt(NEXTINT, 0);
		editor.putInt(NEXTINT, res+1);
		
		editor.commit();
		
		return preferences.getInt(NEXTINT, 0);
	}
	public static void saveRequestBody(String text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();

		editor.putString(REQUEST, text);		
		editor.commit();
	}
	public static String getRequestBody(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(REQUEST, "");
	}

	public static int getLastType(Context context){		
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		int res =preferences.getInt(TYPE, 100);
		editor.putInt(TYPE, res+1);
			
		editor.commit();
			
		return preferences.getInt(TYPE, 100);
	}
	
	public static void saveRequestDish(String text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();

		editor.putString(REQUESTDISH, text + SEPARATOR);		
		editor.commit();
	}
	public static String getRequestDish(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(REQUESTDISH, "");
	}
	public static void saveNewMessagesCount(int count, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();

		editor.putInt(MESSAGESCOUNT, count);		
		editor.commit();
	}
	public static int getNewMessagesCount(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(MESSAGESCOUNT, 0);
	}
	
	
	public static void saveBMI(String text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putString(BMI, text);		
		editor.commit();
	}
	
	public static String getBMI(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(BMI, "");
	}
	
	public static void saveBMR(String text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putString(BMR, text);		
		editor.commit();
	}
	
	public static String getBMR(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(BMR, "");
	}
	
	public static void saveMETA(String text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putString(META, text);		
		editor.commit();
	}
	
	public static String getMETA(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(META, "");
	}
	
	public static void saveHeight(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(HEIGHT, text);		
		editor.commit();
	}
	
	public static int getHeight(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(HEIGHT, 25);
	}
	
	public static void saveWeight(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(WEIGHT, text);		
		editor.commit();
	}
	
	public static int getWeight(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(WEIGHT, 25);
	}
	
	public static float getRealWeight(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return (float)preferences.getInt(WEIGHT, 25) + (float)preferences.getInt(WEIGHT_DEC, 5)/10 + Info.MIN_WEIGHT;
	}
	
	
	public static void saveAge(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(AGE, text);		
		editor.commit();
	}
	
	public static int getAge(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(AGE, 5);
	}
	
	
	public static void saveLifeStyle(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(LIFESTYLE, text);		
		editor.commit();
	}
	
	public static int getLifeStyle(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(LIFESTYLE, 0);
	}
	
	public static void saveLastTime(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(LASTTIME, text);		
		editor.commit();
	}
	
	public static int getLastTime(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(LASTTIME, 0);
	}
	
	public static void saveSex(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(SEX, text);		
		editor.commit();
	}
	
	public static int getSex(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(SEX, 0);
	}
	
	public static void saveActivity(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(ACTIVITY, text);		
		editor.commit();
	}
	
	public static int getActivity(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(ACTIVITY, 0);
	}

	public static void saveMode(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(MODE, text);		
		editor.commit();
	}
	
	public static int getMode(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(MODE, 1);
	}
	
	public static void saveExpModeSelection(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(EXPMODE, text);		
		editor.commit();
	}
	public static void saveExpModeValue(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(EXPMODEVALUE, text);		
		editor.commit();
	}
	
	public static int getExpModeSelection(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(EXPMODE, 1);
	}
	public static int getExpModeValue(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(EXPMODEVALUE, 20);
	}
	
	public static boolean isFirstTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(FIRSTTIME, true);
	}

	public static void setFirstTime(Context context, boolean b) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(FIRSTTIME, b);		
				editor.commit();
	}
	
	public static void setUserUnicId(Context context, int b) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				   Random ran = new Random();

				   int x = ran.nextInt(400000) + 500000;
				   while (x%b == 0) {
					x++;
				   }
				   
				editor.putInt(USERUNICID, b);	
				editor.putString(USERUNICIDFAKE, String.valueOf(x));	
				editor.commit();
	}
	
	public static int getUserUnicId(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(USERUNICID, 0);
	
	}
	
	public static boolean isActivated(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(ACTIVATED, false);
	}

	public static void setActivated(Context context, boolean b) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(ACTIVATED, b);		
				editor.commit();
	}
	
	public static void saveSearchName(String selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putString(SEARCH_NAME, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchAge(int selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(SEARCH_AGE, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchActivity(int selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(SEARCH_ACTIVITY, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchHeight(int selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(SEARCH_HEIGHT, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchSex(int selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(SEARCH_SEX, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchWeight(int selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(SEARCH_WEIGHT, selectedItemId);		
				editor.commit();
	}

	public static String getSearchName(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getString(SEARCH_NAME, "");
	}

	public static int getSearchAge(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(SEARCH_AGE, getAge(context));
	}

	public static int getSearchActivity(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(SEARCH_ACTIVITY, getActivity(context));
	}

	public static int getSearchHeight(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(SEARCH_HEIGHT, getHeight(context));
	}

	public static int getSearchSex(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(SEARCH_SEX, getSex(context));
	}

	public static int getSearchWeight(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(SEARCH_WEIGHT, getWeight(context));
	}
	
	///////
	
	public static void saveSearchNameEnbl(Boolean selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SEARCH_NAME_ENBL, selectedItemId);		
				editor.commit();
	}

	
	public static void saveSearchAgeEnbl(Boolean selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SEARCH_AGE_ENBL, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchActivityEnbl(Boolean selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SEARCH_ACTIVITY_ENBL, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchHeightEnbl(Boolean selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SEARCH_HEIGHT_ENBL, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchSexEnbl(Boolean selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SEARCH_SEX_ENBL, selectedItemId);		
				editor.commit();
	}

	public static void saveSearchWeightEnbl(Boolean selectedItemId,
			Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SEARCH_WEIGHT_ENBL, selectedItemId);		
				editor.commit();
	}

	public static boolean getSearchNameEnbl(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(SEARCH_NAME_ENBL, false);
	}

	public static boolean getSearchAgeEnbl(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(SEARCH_AGE_ENBL, true);
	}

	public static Boolean getSearchActivityEnbl(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(SEARCH_ACTIVITY_ENBL, true);
	}

	public static Boolean getSearchHeightEnbl(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(SEARCH_HEIGHT_ENBL, true);
	}

	public static Boolean getSearchSexEnbl(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(SEARCH_SEX_ENBL, true);
	}

	public static Boolean getSearchWeightEnbl(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getBoolean(SEARCH_WEIGHT_ENBL, true);
	}
	
	//network name
	public static void setNikName(Context context, String b) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putString(NIKNAME, b);		
				editor.commit();
	}
	
	public static String getNikName(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getString(NIKNAME, "");
	}
	public static String getUserUnicIdFake(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getString(USERUNICIDFAKE, "") + preferences.getInt(USERUNICID, 0);
	}
	
	public static void saveWeightDec(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(WEIGHT_DEC, text);		
		editor.commit();
	}
	public static int getWeightDec(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(WEIGHT_DEC, 0);
	}

	public static void setNumOfRows(int text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(NUMB_DEC, text);		
		editor.commit();
	}
	public static int getNumOfRows(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getInt(NUMB_DEC, 0);
	}

	public static void setLastVisitTime(long text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putLong(LASTVISITTIME, text);		
		editor.commit();
	}
	
	public static long getLastVisitTime(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getLong(LASTVISITTIME, 0);
	}
	public static void setLang(String lang, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putString(LANG, lang);		
		editor.commit();		
	}
	public static String getLang(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(LANG, "ru");
	}
	public static void setLastExportedFileName(String name, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putString(EXPFILENAME, name);		
		editor.commit();		
	}
	public static String getLastExportedFileName(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(EXPFILENAME, "");
	}
	public static int isReseted(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(RESET, 0);
	}
	public static void setReseted(int flag, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putInt(RESET, flag);		
		editor.commit();		
	}
	public static void setCustomLimit(int limit, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putInt(LIMIT, limit);		
		editor.commit();		
	}
	public static int getCustomLimit(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(LIMIT, 0);
	}
	
	public static void setNotificationLoded(Boolean flg, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putBoolean(NOTIF, flg);		
		editor.commit();		
	}
	public static Boolean getNotificationLoded(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getBoolean(NOTIF, false);
	}
	public static int getChest(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(CHEST, 60);
	}
	public static int getChestDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(CHESTDEC,0);
	}
	
	public static int getPelvis(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(PELVIS,10);
	}
	public static int getPelvisDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(PELVISDEC,0);
	}
	
	public static int getNeck(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(NECK,10);
	}
	public static int getNeckDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(NECKDEC,0);
	}
	
	public static int getBiceps(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(BICEPS,10);
	}
	public static int getBicepsDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(BICEPSDEC,0);
	}
	
	public static int getForearm(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(FOREARM,10);
	}
	public static int getForearmDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(FOREARMDEC,0);
	}
	
	public static int getWaist(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(WAIST,30);
	}
	public static int getWaistDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(WAISTDEC,0);
	}
	
	public static int getHip(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(HIP,10);
	}
	public static int getHipDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(HIPDEC,0);
	}
	
	public static int getShin(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(SHIN,10);
	}
	public static int getShinDec(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(SHINDEC,0);
	}
	public static void saveChest(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(CHEST, text);		
				editor.commit();
		
	}
	public static void saveChestDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(CHESTDEC, text);		
				editor.commit();
		
	}
	public static void savePelvis(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(PELVIS, text);		
				editor.commit();
		
	}
	public static void savePelvisDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( PELVISDEC, text);		
				editor.commit();
		
	}
	public static void saveNeck(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( NECK, text);		
				editor.commit();
		
	}
	public static void saveNeckDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( NECKDEC, text);		
				editor.commit();
		
	}
	public static void saveBiceps(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( BICEPS, text);		
				editor.commit();
		
	}
	public static void saveBicepsDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( BICEPSDEC, text);		
				editor.commit();
		
	}
	public static void saveForearm(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( FOREARM, text);		
				editor.commit();
		
	}
	public static void saveForearmDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(FOREARMDEC , text);		
				editor.commit();
		
	}
	public static void saveWaist(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( WAIST, text);		
				editor.commit();
		
	}
	public static void saveWaistDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( WAISTDEC, text);		
				editor.commit();
		
	}
	public static void saveHip(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( HIP, text);		
				editor.commit();
		
	}
	public static void saveHipDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( HIPDEC, text);		
				editor.commit();
		
	}
	public static void saveShin(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( SHIN, text);		
				editor.commit();
		
	}
	public static void saveShinDec(int text, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt( SHINDEC, text);		
				editor.commit();
		
	}
	public static boolean getChestEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(CHESTENBL, true);
	}
	public static void setChestEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(CHESTENBL, b);		
				editor.commit();
	}
	public static boolean getPelvisEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(PELVISENBL, false);
	}
	public static void setPelvisEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(PELVISENBL, b);		
				editor.commit();
	}
	public static boolean getNeckEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(NECENBL, false);
	}
	public static void setNeckEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(NECENBL, b);		
				editor.commit();
	}
	public static boolean getShinEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(SHINENBL, false);
	}
	public static void setShinEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(SHINENBL, b);		
				editor.commit();
	}
	public static boolean getBicepsEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(BITKAENBL, true);
	}
	public static void setBicepsEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(BITKAENBL, b);		
				editor.commit();
	}
	public static boolean getForearmEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(FORENBL, false);
	}
	public static void setForearmEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(FORENBL, b);		
				editor.commit();
	}
	
	public static boolean getWaistEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(WAISTENBL, false);
	}
	public static void setWaistEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(WAISTENBL, b);		
				editor.commit();
	}
	public static boolean getHipEnbl(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getBoolean(HIPENBL, false);
	}
	public static void setHipEnbl(boolean b, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putBoolean(HIPENBL, b);		
				editor.commit();
	}
	public static void setPaymentType(String resultString, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putString(PAYKEY, resultString);		
				editor.commit();
	}
	public static String getPaymentType(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getString(PAYKEY, "0");
	}
	public static void setPaydDays(int resultString, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(PAYDDAYS, resultString);		
				editor.commit();
	}
	
	public static int getPaydDays(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(PAYDDAYS, 0);
	}
	
	
	public static void setStartDaysCount(int resultString, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();
				
				editor.putInt(FREEDAYSCOUNT, resultString);		
				editor.commit();
	}
	
	public static int getStartDaysCount(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
		return preferences.getInt(FREEDAYSCOUNT, 0);
	}
	
	public static void setUserAdvId(Context context, int b) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();   
				editor.putInt(USERADVICID, b);	
				editor.commit();
	}
	
	public static int getUserAdvId(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getInt(USERADVICID, 0);
	
	}
	public static void setLastAdvUpdateTime(long text, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putLong(LASTADVUPDATETIME, text);		
		editor.commit();
	}
	
	public static long getLastAdvUpdateTime(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getLong(LASTADVUPDATETIME, 0);
	}
	
	public static void setEndPDate(long date, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putLong(ENDPDATE, date);		
		editor.commit();
	}
	//end payment date in mls
	public static long getEndPDate(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getLong(ENDPDATE, 0);
	}
	public static void setLastRationId(Context context, int b) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();   
				editor.putInt(LASTRATIONID, b);	
				editor.commit();
	}
	
	public static int getLastRationId(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getInt(LASTRATIONID, 0);
	
	}
	public static void writeInt(String name, Integer valueOf, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();   
				editor.putInt(name, valueOf);	
				editor.commit();		
	}
	
	public static int readInt(String name, Integer deffValueOf, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getInt(name, deffValueOf);
	
	}
	
	public static void writeFloat(String name, Float valueOf, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				Editor editor = preferences.edit();   
				editor.putFloat(name, valueOf);	
				editor.commit();		
	}
	
	public static Float readFloat(String name, Float deffValueOf, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getFloat(name, deffValueOf);
	
	}
	
	public static void setLastExportedDataBaseFileName(String name, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putString(EXPDATABASEFILENAME, name);		
		editor.commit();		
	}
	public static String getLastExportedDataBaseFileName(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(EXPDATABASEFILENAME, "");
	}
	public static void setLastExportedHistoryFileName(String name, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
				
		editor.putString(EXPFILENAME, name);		
		editor.commit();		
	}
	public static String getLastExportedHistoryFileName(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getString(EXPFILENAME, "");
	}
	public static float getIdealWeight(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
				
				return preferences.getFloat(USER_IDEAL_WEIGHT, (float) 65.1);
	}
	public static boolean isAllreadyRate(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getBoolean(USER_RATE_APP, false);
	}
	
	public static void setAllreadyRate(Boolean value, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();				
		editor.putBoolean(USER_RATE_APP, value);		
		editor.commit();		
	}
	public static void setUseFreeAbonement(boolean value, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();				
		editor.putBoolean(USER_USE_FREE_ABONEMENT, value);		
		editor.commit();
	}
	
	public static boolean isUseFreeAbonement(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);				
				return preferences.getBoolean(USER_USE_FREE_ABONEMENT, false);
	}
	
}
