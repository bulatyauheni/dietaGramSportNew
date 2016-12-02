package bulat.diet.helper_sport.utils;


import java.util.Random;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.VolumeInfo;

public class SaveUtilsServer {
	
	private static final String LASTSERVERID = "LASTSERVERID";

	public static void setLastServerID(int id, Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		
		editor.putInt(LASTSERVERID, id);
		
		editor.commit();
	}
	public static int getLastServerID(Context context){
		SharedPreferences preferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		return preferences.getInt(LASTSERVERID, 0);
	}
	
}
