package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.BaseLoader;
import bulat.diet.helper_sport.adapter.LocalsArrayAdapter;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.NotificationDishHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.NotificationDish;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.ServerUpdater;
import bulat.diet.helper_sport.utils.SocialUpdater;

public class StartActivity extends BasePayActivity{
	private static final int DIALOG_DOWNLOAD_PROGRESS = 0;	
	static final String SKU_SUBSCRIPTION = "monthdhsport";
	String lang = "";
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		try{
			sliptask.cancel(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//http://calorii.ru/classes/ajax/search.php?query=%D0%B1%D0%BE%D1%80%D1%89
	//http://www.ambal.ru/calories.php?do=3&st=%20%D1%82%D1%83%D1%88%D0%B5%D0%BD%D0%B0%D1%8F
	private final long SLEEP_TIME = 500;
	private static final long SLEEP_TIME_SHOT = 10;
	SleepTask sliptask;
	private ProgressDialog mProgressDialog;	
	ListView localList = null;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DietHelperActivity.dayscount = TodayDishHelper.getDaysCount(this);
		
		if(SaveUtils.getEndPDate(this) == 0){
			Date currDate = new Date();
			SaveUtils.setEndPDate(currDate.getTime() + 7 * DateUtils.DAY_IN_MILLIS, this);
		}
		
		// Create the helper, passing it our context and the public key to verify signatures with      
		   
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.start, null);
		setContentView(viewToLoad);
			//BaseLoader.LoadBase(StartActivity.this);
		Date currDate = new Date();
		SaveUtils.setLastVisitTime(currDate.getTime(),this);
		if(SaveUtils.isFirstTime(StartActivity.this)){			
			List<DishType> locations = new ArrayList<DishType>();
			locations.add(new DishType(R.drawable.ru, getString(R.string.russian)));
			locations.add(new DishType(R.drawable.es, getString(R.string.espanol)));
			//locations.add(new DishType(R.drawable.pl, getString(R.string.polish)));
			//locations.add(new DishType(R.drawable.en, getString(R.string.english)));
			//locations.add(new DishType(R.drawable.by, getString(R.string.belarusian)));
			LocalsArrayAdapter da = new LocalsArrayAdapter(getApplicationContext(),
						R.layout.locations_list_row, locations);
			localList = (ListView)viewToLoad.findViewById(R.id.ListViewLocation);			
			localList.setAdapter(da);
			localList.setOnItemClickListener(listener);								
			//lang = "";
			//SaveUtils.setLang(lang, StartActivity.this);
			//Locale locale = new Locale(lang);
			//Locale.setDefault(locale);
			//Configuration config = new Configuration();
			//config.locale = locale;
			//getBaseContext().getResources().updateConfiguration(config,
			//      getBaseContext().getResources().getDisplayMetrics());
			//try {
			//	if(!SaveUtils.getNotificationLoded(StartActivity.this)){
			//		createNotifications();
			//	}
			//	sliptask = new SleepTask();	
			//	sliptask.execute();
				
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}
		}else{
			if(SaveUtils.isReseted(this) == 0){
				DishListHelper.resetPopularity(this);
				SaveUtils.setReseted(1,this);
			}
			Locale locale = new Locale(SaveUtils.getLang(this));
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
			      getBaseContext().getResources().getDisplayMetrics());
			sliptask = new SleepTask();	
			sliptask.execute();
			SaveUtils.setLastVisitTime(currDate.getTime(),this);
			
		//register user	
		 if(0!=SaveUtils.getUserAdvId(this)){			 
			 SaveUtils.setLastVisitTime(currDate.getTime(),this);
			 if(currDate.getTime() - SaveUtils.getLastAdvUpdateTime(this) > 10*DateUtils.DAY_IN_MILLIS){
		    		  new UpdateTask().execute();		    		 
		    		  SaveUtils.setLastAdvUpdateTime(currDate.getTime(),this);
			 }
	      }else{
	    	  new RegisterTask().execute();	    	 
	      }
		}
	}
	
	 private void createNotifications() {
		 NotificationDishHelper.clearAll(this);
		 String name = null;
		 int enabled = 1;
		 String defName = getString(R.string.time_def);
		 for (int i = 0; i < 10; i++) {
			 name = null;
			 try{
				 if(i<5){
					 name = getString(R.string.time_1+i);
				 }
			 }catch (Exception e) {
				// TODO: handle exception
			 }
			 if(name==null){
				 name= defName+i;
				 enabled = 0;
			 }
			 NotificationDishHelper.addNewNotification(new NotificationDish(String.valueOf(i), name, "0", "0", enabled, 0), this);		
			 }		 
		SaveUtils.setNotificationLoded(true, this);
	}



	@Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DIALOG_DOWNLOAD_PROGRESS:
	            mProgressDialog = new ProgressDialog(this);
	            if(SaveUtils.isFirstTime(this)){
	            	mProgressDialog.setMessage(getString(R.string.loading_base));
	            }else{
	            	mProgressDialog.setMessage(getString(R.string.update_base));
	            }
	            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	            mProgressDialog.setCancelable(false);
	            mProgressDialog.show();
	            return mProgressDialog;
	        default:
	            return null;
	        }
	    }
	
	private class SleepTask extends AsyncTask<Void, Void, Void>{

		int progress;
		@Override
		protected void onPreExecute() {
			if(SaveUtils.isFirstTime(StartActivity.this) || DishListHelper.getCount(StartActivity.this)<10){
				showDialog(DIALOG_DOWNLOAD_PROGRESS);
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {
				//fill db by dishes during first time runing	
				//SaveUtils.setFirstTime(this, true);
				if(SaveUtils.isFirstTime(StartActivity.this)){
					new LoadTask().execute();
					Thread.sleep(SLEEP_TIME_SHOT);
					int perc = 0;
					int kVal = 27;
					if(SaveUtils.getLang(StartActivity.this).equals(getString(R.string.localebg))){
						kVal = 13;
					}
					if(SaveUtils.getLang(StartActivity.this).equals(getString(R.string.localepl))){
						kVal = 25;
					}
					if(SaveUtils.getLang(StartActivity.this).equals(getString(R.string.localeen))){
						kVal = 10;
					}
					if(SaveUtils.getLang(StartActivity.this).equals(getString(R.string.localeby))){
						kVal = 26;
					}
					if(SaveUtils.getLang(StartActivity.this).equals(getString(R.string.localees))){
						kVal = 20;
					}
					while(perc<95){
						perc = SaveUtils.getNumOfRows(StartActivity.this)/kVal;
						progress = perc;
						publishProgress();
						Thread.sleep(SLEEP_TIME);
					}
				}else{
					if(DishListHelper.getCount(StartActivity.this)<10){
						SaveUtils.setNumOfRows(0, StartActivity.this);
						new LoadTask().execute();
						Thread.sleep(SLEEP_TIME);
						int perc = 0;
						while(perc<90){							
							perc = SaveUtils.getNumOfRows(StartActivity.this)/27;
							progress = perc;
							publishProgress();	
							Thread.sleep(SLEEP_TIME);
						}
					}
					
					new SocialUpdater(StartActivity.this, true).execute();
					
					checkCalendar(StartActivity.this);
					Thread.sleep(SLEEP_TIME_SHOT);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	        
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			mProgressDialog.setProgress(progress);
		}
		@Override
		protected void onPostExecute(Void result) {
			if(SaveUtils.isFirstTime(StartActivity.this) || DishListHelper.getCount(StartActivity.this)<10){
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			}
			
			Intent intent;			
				intent = new Intent(StartActivity.this, DietHelperActivity.class);
			startActivity(intent);			
			finish();
		}
		
	}
	
	private class LoadTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			//fill db by dishes during first time runing	
			//SaveUtils.setFirstTime(this, true);
			
				//DishListHelper.clearAll(getApplicationContext());
				//TodayDishHelper.clearAll(StartActivity.this);
				BaseLoader.LoadFitnesBase(StartActivity.this);
				BaseLoader.LoadBase(StartActivity.this);
				//BaseLoader.LoadText(R.raw.base, StartActivity.this);
				SaveUtils.setFirstTime(StartActivity.this, false);
				checkCalendar(StartActivity.this);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
					
			finish();
		}
		
	}
	public static void checkCalendar(Context ctx ) {
		// TODO Auto-generated method stub
		Locale locale;
		if("".endsWith(SaveUtils.getLang(ctx))){
			locale = new Locale("ru");
		}else{
			locale = new Locale(SaveUtils.getLang(ctx));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM",locale);

		String lastDate = TodayDishHelper.getLastDate(ctx);
		Date curentDateandTime = new Date();
		if(lastDate != null){
			try {
				curentDateandTime = sdf.parse(lastDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date nowDate = new Date();
			curentDateandTime.setYear(nowDate.getYear());
			//curentDateandTime = new Date(curentDateandTime.getTime() - 5 * DateUtils.DAY_IN_MILLIS);			
			if(nowDate.getTime() - curentDateandTime.getTime() > DateUtils.DAY_IN_MILLIS){
				while(nowDate.getTime() - curentDateandTime.getTime() > DateUtils.DAY_IN_MILLIS){
					curentDateandTime = new Date(curentDateandTime.getTime() + DateUtils.DAY_IN_MILLIS);					
					TodayDishHelper.addNewDish(new TodayDish("", "", 0, "", 0, 0, sdf.format(curentDateandTime), curentDateandTime.getTime() ,1, "", SaveUtils.getRealWeight(ctx)), ctx);
					TodayDishHelper.updateBobyParams(
							ctx,
							String.valueOf(sdf.format(curentDateandTime)),
							String.valueOf(SaveUtils.getRealWeight(ctx)),
							new BodyParams(String.valueOf((float) SaveUtils.getChest(ctx)+VolumeInfo.MIN_CHEST + (float) SaveUtils.getChestDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getBiceps(ctx)+VolumeInfo.MIN_BICEPS + (float) SaveUtils.getBicepsDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getPelvis(ctx)+VolumeInfo.MIN_PELVIS + (float) SaveUtils.getPelvisDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getNeck(ctx)+VolumeInfo.MIN_NECK + (float) SaveUtils.getNeckDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getWaist(ctx)+VolumeInfo.MIN_WAIST + (float) SaveUtils.getWaistDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getForearm(ctx)+VolumeInfo.MIN_FOREARM + (float) SaveUtils.getForearmDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getHip(ctx)+VolumeInfo.MIN_HIP + (float) SaveUtils.getHipDec(ctx)/10), 
									String.valueOf((float) SaveUtils.getShin(ctx)+VolumeInfo.MIN_SHIN + (float) SaveUtils.getShinDec(ctx)/10)));
				}
			}
		}else{
			curentDateandTime = new Date();
			Date start = new Date();
			try {
				start = sdf.parse(sdf.format(new Date(curentDateandTime.getTime())));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			start.setYear(curentDateandTime.getYear());

			TodayDishHelper.addNewDish(new TodayDish("", "", 0, "", 0, 0, sdf.format(new Date(curentDateandTime.getTime())), start.getTime()  ,1, "", SaveUtils.getRealWeight(ctx)), ctx);
			TodayDishHelper.updateBobyParams(
					ctx,
					String.valueOf(start.getTime()),
					String.valueOf(SaveUtils.getRealWeight(ctx)),
					new BodyParams(String.valueOf((float) SaveUtils.getChest(ctx)+VolumeInfo.MIN_CHEST + (float) SaveUtils.getChestDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getBiceps(ctx)+VolumeInfo.MIN_BICEPS + (float) SaveUtils.getBicepsDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getPelvis(ctx)+VolumeInfo.MIN_PELVIS + (float) SaveUtils.getPelvisDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getNeck(ctx)+VolumeInfo.MIN_NECK + (float) SaveUtils.getNeckDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getWaist(ctx)+VolumeInfo.MIN_WAIST + (float) SaveUtils.getWaistDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getForearm(ctx)+VolumeInfo.MIN_FOREARM + (float) SaveUtils.getForearmDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getHip(ctx)+VolumeInfo.MIN_HIP + (float) SaveUtils.getHipDec(ctx)/10), 
							String.valueOf((float) SaveUtils.getShin(ctx)+VolumeInfo.MIN_SHIN + (float) SaveUtils.getShinDec(ctx)/10)));
		}
	}
	private OnItemClickListener listener = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			
			TextView idView = (TextView) v.findViewById(R.id.textViewName);	
			if(idView.getText().toString().equals(getString(R.string.russian))){
				lang = "ru";
			}else if(idView.getText().toString().equals(getString(R.string.bulgarian))){
				lang = "bg";
			}else if(idView.getText().toString().equals(getString(R.string.polish))){
				lang = "pl";
			}else if(idView.getText().toString().equals(getString(R.string.english))){
				lang = "en";
			}else if(idView.getText().toString().equals(getString(R.string.belarusian))){
				lang = "by";
			}else if(idView.getText().toString().equals(getString(R.string.espanol))){
				lang = "es";
			}			
			SaveUtils.setLang(lang, StartActivity.this);
			Locale locale = new Locale(lang);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
			      getBaseContext().getResources().getDisplayMetrics());
			AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
			builder.setMessage(R.string.remove_dialog)
					.setPositiveButton(getString(R.string.yes), dialogClickListener)
					.setNegativeButton(getString(R.string.no), dialogClickListener)
					.show();
			
		}
	};
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					if(!SaveUtils.getNotificationLoded(StartActivity.this)){
						createNotifications();
					}

					sliptask = new SleepTask();	
					sliptask.execute();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				
				break;
			}
		}
	};

	private class RegisterTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (NetworkState.isOnline(StartActivity.this)) {
				StringBuilder builder = new StringBuilder();
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				int timeoutConnection = 5000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				HttpClient client = new DefaultHttpClient(httpParameters);
				// searchString = searchString.replaceAll(" ", "%20");
				StringBuffer parametersb = new StringBuffer("");
				parametersb.append("?updateadv=" + 1);			

								
					parametersb.append("&weight="
							+(SaveUtils
							.getWeight(StartActivity.this) + Info.MIN_WEIGHT));
					parametersb.append("&high="
							+(SaveUtils
							.getHeight(StartActivity.this) + Info.MIN_HEIGHT));
					parametersb.append("&age="
							+(SaveUtils
							.getAge(StartActivity.this) + Info.MIN_AGE));
					parametersb.append("&sex="
							+(SaveUtils
							.getSex(StartActivity.this)));
					parametersb.append("&activity="
							+(SaveUtils
							.getActivity(StartActivity.this)));
					
				HttpGet httpGet = new HttpGet(Constants.URL_ADVERT + parametersb);
				try {
					HttpResponse response = client.execute(httpGet);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(content));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}
					String resultString = builder.toString().trim();
										
						try{
							if(Integer.valueOf(resultString) > 0)
								SaveUtils.setUserAdvId(StartActivity.this, Integer.valueOf(resultString));
						}catch (Exception e) {
								e.printStackTrace();
						}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

	}
	private class UpdateTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (NetworkState.isOnline(StartActivity.this)) {
				StringBuilder builder = new StringBuilder();
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				int timeoutConnection = 5000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				HttpClient client = new DefaultHttpClient(httpParameters);
				// searchString = searchString.replaceAll(" ", "%20");
				StringBuffer parametersb = new StringBuffer("");
				parametersb.append("?id=" + SaveUtils.getUserAdvId(StartActivity.this));
				parametersb.append("?updateadv=" + "&id="
						+ SaveUtils.getUserAdvId(StartActivity.this));
				parametersb.append("&age="
						+ (SaveUtils.getAge(StartActivity.this) + Info.MIN_AGE));
				parametersb.append("&sex=" + (SaveUtils.getSex(StartActivity.this)));
				
				parametersb.append("&activity="
						+ (SaveUtils.getActivity(StartActivity.this)));
				parametersb.append("&version="+Constants.VERSION);
				parametersb.append("&socid=" + (SaveUtils.getUserUnicId(StartActivity.this)));
				
				HttpGet httpGet = new HttpGet(Constants.URL_ADVERT
						+ parametersb);
				try {
					HttpResponse response = client.execute(httpGet);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(content));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}
					String resultString = builder.toString().trim();
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

		}

	}
}
