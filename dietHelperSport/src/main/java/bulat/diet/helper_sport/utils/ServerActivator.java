package bulat.diet.helper_sport.utils;

import java.util.List;
import android.content.Context;
import android.os.AsyncTask;
import bulat.diet.helper_sport.db.SinchHelper;
import bulat.diet.helper_sport.httprequest.RequestHelper;
import bulat.diet.helper_sport.item.Sinch;

public class ServerActivator extends AsyncTask<Void, Void, Void> {

	private Context context;
	List<Sinch> toSincList;
	
	public ServerActivator(Context context) {
		this.context = context;
		toSincList = SinchHelper.getSinchArray(context);
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {

        try {
        	    for (Sinch sinch : toSincList) {        	        
        	               	    
					String resultString2 = RequestHelper.postRequestJson(Constants.URL_DISHBASE + "serveractiv.php",sinch.getUrl());
					if("1".equals(resultString2)){
						SinchHelper.removeSinch(sinch.getId(), context);
					}

				}					
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	protected void onPostExecute(String unused) {
	}

}