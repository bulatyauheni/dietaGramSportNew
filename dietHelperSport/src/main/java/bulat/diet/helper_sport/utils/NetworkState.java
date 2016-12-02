package bulat.diet.helper_sport.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkState {
	public static boolean isOnline(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	    if (activeNetInfo == null)
	        return false;
	      if (!activeNetInfo.isConnected())
	        return false;
	      if (!activeNetInfo.isAvailable())
	        return false;
	      return true;
	}
}
