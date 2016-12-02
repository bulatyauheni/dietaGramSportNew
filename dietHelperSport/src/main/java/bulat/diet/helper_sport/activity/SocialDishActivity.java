package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.SocialDishListArrayAdapter;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class SocialDishActivity extends Activity {
	public static final String DATE = "date";
	public static final String PARENT_NAME = "parentname";
	public static final String BACKBTN = "backbtn";
	public static final String URL = Constants.URL_SOCIAL;
	public static final String USERID = "userid";
	public static final String USERNAME = "username";
	protected static final String DATEINT = "date_int";
	public static final String USERWEIGHT = "USERWEIGHT";
	public static final String USERHEIGHT = "USERHEIGHT";
	public static final String USERAGE = "USERAGE";
	public static final String USERSEX = "USERSEX";
	public static final String USERSPORTING = "USERSPORTING";
	String curentDateandTime;
	ListView dishesList;
	String userId = null;
	private TextView loadingView;
	ArrayList<TodayDish> list = new ArrayList<TodayDish>();
	private TextView badSearchView;
	private View emptyLayout;  
	TextView header;
	String parentName = null;
	int sum;
	private String userName;
	private String dateint;

	@Override
	protected void onResume() {
	
		// TODO Auto-generated method stub
		super.onResume();	
		Bundle extras = getIntent().getExtras();
		String date = null;
		parentName = DishActivityGroup.class.toString();
		if (extras != null) {
			date = extras.getString(DATE);
			dateint = extras.getString(DATEINT);
			parentName = extras.getString(PARENT_NAME);
			userId = extras.getString(USERID);
			userName = extras.getString(USERNAME);
			if(userId != null && list.size() < 1){
				new LoadDishesTask().execute();
			}
		}
		if (date == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM",new Locale( SaveUtils.getLang(SocialDishActivity.this)));
			curentDateandTime = sdf.format(new Date());
		} else {
			curentDateandTime = date;
		}
		header.setText(curentDateandTime);

	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.social_today_list, null);
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		dishesList = (ListView) viewToLoad.findViewById(R.id.listViewTodayDishes);
		emptyLayout = viewToLoad.findViewById(R.id.emptyLayout);
		badSearchView = (TextView) viewToLoad
				.findViewById(R.id.textViewBadSearch);
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);
		if (extras != null) {
			Boolean backb = extras.getBoolean(BACKBTN);
			if (backb) {
				Button backButton = (Button) viewToLoad
						.findViewById(R.id.buttonBack);
				backButton.setVisibility(View.VISIBLE);
				backButton.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						onBackPressed();
					}
				});
			}
		}

		Button addButton = (Button) viewToLoad.findViewById(R.id.buttonAdd);
		addButton.setOnClickListener(addTodayDishListener);
		Button fitesButton = (Button) viewToLoad.findViewById(R.id.buttonFitnes);
		fitesButton.setOnClickListener(addTodayFitnesListener);
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				onBackPressed();
			}			
		});	

		setContentView(viewToLoad);
	}
	
	private OnClickListener addTodayDishListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(SocialDishActivity.DATE, curentDateandTime);
			intent.putExtra(PARENT_NAME, parentName);
			intent.setClass(getParent(), DishListActivity.class);
			if (CalendarActivityGroup.class.toString().equals(parentName)) {
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("DishListActivityCalendar", intent);
			} else {
				DishActivityGroup activityStack = (DishActivityGroup) getParent();
				activityStack.push("DishListActivity", intent);
			}
		}
	};
	

	private OnClickListener addTodayFitnesListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(SocialDishActivity.DATE, curentDateandTime);
			intent.putExtra(PARENT_NAME, parentName);
			intent.putExtra(AddTodayDishActivity.ADD, 1);
			intent.setClass(getParent(), AddTodayFitnesActivity.class);
			if (CalendarActivityGroup.class.toString().equals(parentName)) {
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("FitnesActivityCalendar", intent);
			} else {
				DishActivityGroup activityStack = (DishActivityGroup) getParent();
				activityStack.push("FitnesActivity", intent);
			}
		}
	};
	
	private class LoadDishesTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			emptyLayout.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			if (NetworkState.isOnline(getApplicationContext())) {
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(
						URL + "?userid=" + userId + "&date_int=" + dateint);
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
					} else {

					}
					String resultString = builder.toString();
					try {
						JSONObject jsonRoot = new JSONObject(resultString);
						JSONArray jsonArray = new JSONArray(
								jsonRoot.getString("dishes"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							list.add(new TodayDish(jsonObject.getString("name"),
									jsonObject.getString("day_time"),									
									Integer.parseInt(jsonObject.getString("caloricity")),
									"",
									Integer.parseInt(jsonObject.getString("weight")),
									0,
									"",
									new Long(0),
									0,
									""));
					
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// return builder.toString();
			}
			return null;

			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			setAdapter();
		}

	}

	private void setAdapter() {
		if (!NetworkState.isOnline(getApplicationContext())) {

			Toast.makeText(this, getString(R.string.disonect),
					Toast.LENGTH_LONG).show();
		}
		emptyLayout.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		if (list.size() > 0) {
			badSearchView.setVisibility(View.GONE);
			try {
				SocialDishListArrayAdapter da = new SocialDishListArrayAdapter(getApplicationContext(),
						R.layout.social_today_dish_list_row, list);
				dishesList.setAdapter(da);
				da.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
			initBottomHeader(list);
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.VISIBLE);
		}
		
	}
	
	
	private void initBottomHeader(ArrayList<TodayDish> list2) {
		int sum2=0;
		// TODO Auto-generated method stub
		for (TodayDish todayDish : list2) {
			sum2 = sum2 + todayDish.getCaloricity();

		}
		
		TextView tv = (TextView) findViewById(R.id.textViewTotalValue);
		tv.setText(String.valueOf(sum2) + getString(R.string.kcal));
		TextView tv2 = (TextView) findViewById(R.id.textViewLimitValue);
		tv2.setText(String.valueOf(SaveUtils.getBMR(this)) + getString(R.string.kcal));


	}



	public void checkLimit(int sum){
		int mode = SaveUtils.getMode(this);
		switch (mode) {
		case 0:
			if(sum > Integer.parseInt(SaveUtils.getBMR(SocialDishActivity.this))){
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_red);
			}else{
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_green);
			}
			TextView tvLimit = (TextView) findViewById(R.id.textViewLimitValue);
			tvLimit.setText(String.valueOf(SaveUtils.getBMR(SocialDishActivity.this)) + getString(R.string.kcal));
			break;
		case 1:
			if(sum > Integer.parseInt(SaveUtils.getMETA(SocialDishActivity.this))){
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_red);
			}else{
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_green);
			}	
			TextView tvLimit2 = (TextView) findViewById(R.id.textViewLimitValue);
			tvLimit2.setText(String.valueOf(SaveUtils.getMETA(SocialDishActivity.this)) + getString(R.string.kcal));
			break;
		case 2:
			TextView tvLimit3 = (TextView) findViewById(R.id.textViewLimitValue);
			tvLimit3.setText(String.valueOf(SaveUtils.getBMR(SocialDishActivity.this)) + getString(R.string.kcal) );
			if(sum < Integer.parseInt(SaveUtils.getBMR(SocialDishActivity.this))){
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_red);
								
			}else{
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_green);
			}
			break;
		default:
			break;
		}
		
	}
	
}
