package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

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
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.SocialCalendarAdapter;
import bulat.diet.helper_sport.db.MessagesHelper;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.MessageItem;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;


public class SocialCalendarActivity extends Activity {
	protected static final int DIALOG_CHART = 0;
	private static final int DIALOG_CHANGE_USER_NAME = 3;
	TextView header;
	ListView daysList;
	int sum;
	EditText userNameET;
	String userId = null;
	String userName = "";
	private TextView loadingView;
	ArrayList<Day> list = new ArrayList<Day>();
	private TextView badSearchView;
	private View emptyLayout;
	private TextView ageView;
	private TextView nameView;
	private ImageView sportingView;
	private TextView sexView;
	private TextView weightView;
	private TextView heightView;
	private String userWeight;
	private String userHeight;
	private String userAge;
	private int userSporting;
	private String userSex;
	private Button followButton;
	private Button unfollowButton;  
	int ownId ;
	private Button settingsButton;
	private Button nobutton;
	private Button buttonMessages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		ownId = SaveUtils.getUserUnicId(SocialCalendarActivity.this);
		super.onCreate(savedInstanceState);		
		Bundle extras = getIntent().getExtras();	
		
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.social_calendar_list, null);
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		emptyLayout =  viewToLoad.findViewById(R.id.emptyLayoutCalendar);
		badSearchView = (TextView) viewToLoad.findViewById(R.id.textViewBadSearch);
		
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);

		
		followButton = (Button) viewToLoad.findViewById(R.id.buttonFollow);
		followButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new FollowTask().execute();
			}
		});
		unfollowButton = (Button) viewToLoad.findViewById(R.id.buttonUnfollow);
		unfollowButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new UnfollowTask().execute();
			}
		});
		daysList = (ListView) viewToLoad.findViewById(R.id.listViewUser);
		daysList.setOnItemClickListener(daysListOnItemClickListener);
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				onBackPressed();
			}			
		});	
		if (extras != null) {
			userName = extras.getString(SocialDishActivity.USERNAME);
			userWeight = extras.getString(SocialDishActivity.USERWEIGHT);
			userHeight = extras.getString(SocialDishActivity.USERHEIGHT);
			userAge = extras.getString(SocialDishActivity.USERAGE);
			userSporting = extras.getInt(SocialDishActivity.USERSPORTING);
			userSex = extras.getString(SocialDishActivity.USERSEX);

			userId = extras.getString(SocialDishActivity.USERID);
			
			if(userId != null){
				if (userId.equals(String.valueOf(SaveUtils.getUserUnicId(this)))){
					followButton.setVisibility(View.GONE);
					unfollowButton.setVisibility(View.GONE);
				}
				new LoadCalendarTask().execute();
			}
		}	
		Button newsTab= (Button) viewToLoad.findViewById(R.id.newsTab);
		newsTab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();	
					intent.putExtra(SocialDishActivity.USERID, userId);
					intent.putExtra(SocialDishActivity.USERNAME, userName);
					intent.putExtra(SocialDishActivity.USERWEIGHT, userWeight);
					intent.putExtra(SocialDishActivity.USERHEIGHT, userHeight);					
					intent.putExtra(SocialDishActivity.USERSEX, userSex);					
					intent.putExtra(SocialDishActivity.USERAGE, userAge);
					intent.putExtra(SocialDishActivity.USERSPORTING, userSporting);	
					
					intent.setClass(getParent(), SocialUserNewsActivity.class);					
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.pushInstead("SocialUserNewsActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		settingsButton = (Button) viewToLoad.findViewById(R.id.buttonSettings);
		settingsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_CHANGE_USER_NAME);
			}
		});
	    buttonMessages = (Button) viewToLoad.findViewById(R.id.buttonMessages);
		buttonMessages.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {	
				if(SaveUtils.getUserUnicId(SocialCalendarActivity.this)!=0){
					Intent intent = new Intent();
					intent.putExtra(SocialDishActivity.USERID, userId);
					intent.putExtra(SocialDishActivity.USERNAME, userName);
	
					try {					
						intent.setClass(getParent(), ChatActivity.class);
						SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
						activityStack.push("ChatActivity", intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Intent intent = new Intent();
					intent.setClass(SocialCalendarActivity.this, SocialUserListActivity.class);
					SocialActivityGroup activityStack = (SocialActivityGroup) SocialCalendarActivity.this.getParent();
					activityStack.push("UserListActivity", intent);
				}
			}			
		});	
		ageView = (TextView) viewToLoad.findViewById(R.id.textViewAgeValue);
		nameView = (TextView) viewToLoad.findViewById(R.id.textViewNameValue);
		sportingView = (ImageView) viewToLoad.findViewById(R.id.imageViewActivity);
		sexView = (TextView) viewToLoad.findViewById(R.id.textViewSexValue);
		weightView = (TextView) viewToLoad
				.findViewById(R.id.textViewWeightValue);
		heightView = (TextView) viewToLoad
				.findViewById(R.id.textViewHeightValue);
		
		setContentView(viewToLoad);
		LinearLayout graphHeader = (LinearLayout)findViewById(R.id.linearLayoutChart);
		graphHeader.setOnClickListener(showChartListener);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		header.setText(userName);
		setHeader();
		if(MessagesHelper.amIFollow( userId, this)){
			followButton.setVisibility(View.GONE);
			unfollowButton.setVisibility(View.VISIBLE);
		}else{
			unfollowButton.setVisibility(View.GONE);
			followButton.setVisibility(View.VISIBLE);
		}
		if(userId != null){
			if (userId.equals(String.valueOf(SaveUtils.getUserUnicId(this)))){
				followButton.setVisibility(View.GONE);
				unfollowButton.setVisibility(View.GONE);
				settingsButton.setVisibility(View.VISIBLE);
				buttonMessages.setVisibility(View.GONE);
			}
		}
	}

	private void setHeader() {
		// TODO Auto-generated method stub
		ageView.setText(userAge);
		if("0".equals(userSex)){
			sexView.setText(getString(R.string.male));
		}else if("1".equals(userSex)){
			sexView.setText(getString(R.string.female));
		}else{		
			sexView.setText(userSex);
		}
		nameView.setText(userName);
		weightView.setText(userWeight);
		heightView.setText(userHeight);
		
		
		
		switch (userSporting) {
		case 0:
			sportingView.setImageResource(R.drawable.activity_level1);
			break;
		case 1:
			sportingView.setImageResource(R.drawable.activity_level2);
			break;
		case 2:
			sportingView.setImageResource(R.drawable.activity_level3);
			break;
		case 3:
			sportingView.setImageResource(R.drawable.activity_level4);
			break;
		case 4:
			sportingView.setImageResource(R.drawable.activity_level5);
			break;

		default:
			sportingView.setImageResource(R.drawable.activity_level1);
			break;
		}

	}


	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		Button buttonOk;
		switch (id) {

		case DIALOG_CHANGE_USER_NAME:
			dialog = new Dialog(this.getParent());
			if(SaveUtils.getUserUnicId(this)!=0){
				dialog.setContentView(R.layout.user_name_change_dialog);
				dialog.setTitle(R.string.username_change_dialog_title);
				TextView idView2 = (TextView) dialog.findViewById(R.id.textViewId);
				
				idView2.setText("id" + SaveUtils.getUserUnicIdFake(SocialCalendarActivity.this));
			}else{
				dialog.setContentView(R.layout.user_name_dialog);
				dialog.setTitle(R.string.username_dialog_title);
			}

			userNameET = (EditText) dialog.findViewById(R.id.editTextUserName);
			userNameET.setText(SaveUtils.getNikName(this));
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					if(userNameET.getText().length()<1){
						userNameET.setBackgroundColor(Color.RED);
					}else{
						SaveUtils.setNikName(SocialCalendarActivity.this, userNameET.getText().toString());
						userName =  userNameET.getText().toString();
						dialog.cancel();
						//update social profile
						if(SaveUtils.getUserUnicId(SocialCalendarActivity.this) != 0){
							new SocialUpdater(SocialCalendarActivity.this).execute();
						}
						onResume();
					}
					
				}
			});
			nobutton = (Button) dialog.findViewById(R.id.buttonNo);
			nobutton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.cancel();
				}
			});

			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	private OnItemClickListener daysListOnItemClickListener= new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			//switchTabInActivity(1)
			Intent intent = new Intent();
			TextView idView = (TextView) v.findViewById(R.id.textViewUserId);
			TextView dateView = (TextView) v.findViewById(R.id.textViewName);
			intent.putExtra(SocialDishActivity.DATEINT, idView.getText().toString());
			intent.putExtra(SocialDishActivity.DATE, dateView.getText().toString());
			intent.putExtra(SocialDishActivity.USERID, userId);
			
				try {					
					intent.setClass(getParent(), SocialDishActivity.class);
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.push("SocialDishActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		}
	};

	
	public void switchTabInActivity(int indexTabToSwitchTo){
        DietHelperActivity parentActivity;
        parentActivity = (DietHelperActivity) this.getParent();
        parentActivity.switchTab(indexTabToSwitchTo);
	}

	private class FollowTask extends AsyncTask<Void, Void, Void>{


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
			//	searchString = searchString.replaceAll(" ", "%20");
				HttpGet httpGet = new HttpGet(
						Constants.URL_SOCIAL_NEWS + "?id="+ownId+"&friend_id=" + userId +"&friend_add=" + userId);

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
						if (Integer.valueOf(resultString.trim()) > 0){
							Date d = new Date();
							MessagesHelper.addNewMessage(
									new MessageItem(0,
											String.valueOf(userId),
											String.valueOf(ownId),
											"",
											userName,
											String.valueOf(d.getTime()),
											"",
											Constants.IFOLLOW),
									SocialCalendarActivity.this);
							
							
						}else{
							//done = false;
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
			setHeader();
			setAdapter();
			onResume();
			Toast.makeText(SocialCalendarActivity.this, getString(R.string.soc_start_following),
					Toast.LENGTH_LONG).show();
		}

	}
	private class LoadCalendarTask extends AsyncTask<Void, Void, Void>{


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
			//	searchString = searchString.replaceAll(" ", "%20");
				HttpGet httpGet = new HttpGet(
						Constants.URL_SOCIAL + "?userid=" + userId);

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
								jsonRoot.getString("days"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							list.add(new Day(jsonObject.getString("name"), 
									Long.parseLong(jsonObject.getString("date_int")),
									Integer.parseInt(jsonObject.getString("caloricity")),
									Integer.parseInt(jsonObject.getString("weight")),
									Float.parseFloat(jsonObject.getString("body_weight")),
									(int)Float.parseFloat(jsonObject.getString("limit"))));								
							userWeight = jsonObject.getString("curr_weight");
							userHeight = jsonObject.getString("height");
							userAge = jsonObject.getString("age");
							userSporting = Integer.parseInt(jsonObject.getString("activity"));
							userSex = jsonObject.getString("sex");
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
			setHeader();
			setAdapter();
		}

	}
	private class UnfollowTask extends AsyncTask<Void, Void, Void>{



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
					int ownId = SaveUtils.getUserUnicId(SocialCalendarActivity.this);
					StringBuilder builder = new StringBuilder();
					HttpClient client = new DefaultHttpClient();
				//	searchString = searchString.replaceAll(" ", "%20");
					HttpGet httpGet = new HttpGet(
							Constants.URL_SOCIAL_NEWS + "?id="+ownId+"&friend_id=" + userId +"&friend_remove=" + userId);

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
							if (Integer.valueOf(resultString.trim()) > 0){
								Date d = new Date();
								MessagesHelper.removeFollowing(
										new MessageItem(0,
												String.valueOf(userId),
												String.valueOf(ownId),
												"",
												userName,
												String.valueOf(d.getTime()),
												"",
												Constants.IFOLLOW),
										SocialCalendarActivity.this);
		
								
							}else{
								//done = false;
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
				setHeader();
				setAdapter();
				onResume();
				Toast.makeText(SocialCalendarActivity.this, getString(R.string.soc_stop_following),
						Toast.LENGTH_LONG).show();
			}
			
		}
	private void setAdapter() {
		if (!NetworkState.isOnline(getApplicationContext())) {

			Toast.makeText(SocialCalendarActivity.this, getString(R.string.disonect),
					Toast.LENGTH_LONG).show();
		}
		emptyLayout.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		if (list.size() > 0) {
			badSearchView.setVisibility(View.GONE);

		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.VISIBLE);
		}
		try {
			SocialCalendarAdapter da = new SocialCalendarAdapter(getApplicationContext(),
					R.layout.social_calendar_list_row, list);
			daysList.setAdapter(da);
			da.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
private OnClickListener showChartListener = new OnClickListener() {
		
		public void onClick(View v) {
			showDialog(DIALOG_CHART);
		}
	};
	
}
