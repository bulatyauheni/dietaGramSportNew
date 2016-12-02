package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.NewsArrayAdapter;
import bulat.diet.helper_sport.item.News;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class SocialUserNewsActivity extends SocialNewsListActivity {
	protected static final int DIALOG_CHART = 0;
	private static final int DIALOG_CHANGE_USER_NAME = 3;
	private static final String URL = Constants.URL_SOCIAL_NEWS;
	TextView header;
	ListView daysList;
	int sum;
	String userId = null;
	String userName = "";
	private TextView loadingView;
	ArrayList<News> list = new ArrayList<News>();
	private TextView badSearchView;
	private View emptyLayout;
	private TextView ageView;
	private EditText coment;
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
	private Button buttonOk;
	private Button nobutton;
	private Button followButton;
	private Button unfollowButton; 
	private String commentNewsId;
	private TextView news;
	int ownId ;
	ListView newsList;
	private Button settingsButton;
	private Button buttonMessages;
	private LinearLayout layoutNewsbox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		ownId = SaveUtils.getUserUnicId(SocialUserNewsActivity.this);
		super.onCreate(savedInstanceState);		
		Bundle extras = getIntent().getExtras();	
		
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.social_user_news_list, null);
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		emptyLayout =  viewToLoad.findViewById(R.id.emptyLayoutCalendar);
		badSearchView = (TextView) viewToLoad.findViewById(R.id.textViewBadSearch);
		
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);

		if (extras != null) {
			userName = extras.getString(SocialDishActivity.USERNAME);
			userWeight = extras.getString(SocialDishActivity.USERWEIGHT);
			userHeight = extras.getString(SocialDishActivity.USERHEIGHT);
			userAge = extras.getString(SocialDishActivity.USERAGE);
			userSporting = extras.getInt(SocialDishActivity.USERSPORTING);
			userSex = extras.getString(SocialDishActivity.USERSEX);

			userId = extras.getString(SocialDishActivity.USERID);
			
			
		}	
		Button buttonNewsSend = (Button) viewToLoad.findViewById(R.id.buttonSendNews);
		buttonNewsSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(news.getText().toString().length()>0){
					new SendNewsTask().execute();
				}
			}
		});
		settingsButton = (Button) viewToLoad.findViewById(R.id.buttonSettings);
		settingsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_CHANGE_USER_NAME);
			}
		});
		Button calendarTab= (Button) viewToLoad.findViewById(R.id.calendarTab);
		calendarTab.setOnClickListener(new OnClickListener() {

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
					intent.setClass(getParent(), SocialCalendarActivity.class);					
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.pushInstead("SocialCalendarActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		news = (TextView) viewToLoad.findViewById(R.id.EditTextNews);
		newsList = (ListView) viewToLoad.findViewById(R.id.listViewUser);
		

		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				onBackPressed();
			}			
		});	
		
		buttonMessages = (Button) viewToLoad.findViewById(R.id.buttonMessages);
		buttonMessages.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {	
				if(SaveUtils.getUserUnicId(SocialUserNewsActivity.this)!=0){
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
					intent.setClass(SocialUserNewsActivity.this, SocialUserListActivity.class);
					SocialActivityGroup activityStack = (SocialActivityGroup) SocialUserNewsActivity.this.getParent();
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
		layoutNewsbox = (LinearLayout) viewToLoad
				.findViewById(R.id.LayoutNewsbox);
		setContentView(viewToLoad);
		
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub	
		header.setText(userName);
		setHeader();
		news.setText("");
		
		// TODO Auto-generated method stub
		super.onResume();
		// TODO Auto-generated method stub		
		
		if (NetworkState.isOnline(getApplicationContext())) {
			
		new LoadNewsTask().execute();
		} else {
			DialogUtils.showDialog(SocialUserNewsActivity.this.getParent(),
					R.string.disonect);
		}
		if(userId != null){
			if (userId.equals(String.valueOf(SaveUtils.getUserUnicId(this)))){	
				layoutNewsbox.setVisibility(View.VISIBLE);
				settingsButton.setVisibility(View.VISIBLE);
				buttonMessages.setVisibility(View.GONE);
			}
		}
	}
	private class LoadNewsTask extends AsyncTask<Void, Void, Void> {

		private String searchString;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			emptyLayout.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext())) {
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				// searchString = searchString.replaceAll(" ", "%20");
				StringBuffer parameters = new StringBuffer("");
				parameters.append("?mynews_select=1&id="+userId);
				HttpGet httpGet = new HttpGet(URL + parameters);
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
					list = new ArrayList<News>();
					try {
						JSONObject jsonRoot = new JSONObject(resultString);
						JSONArray jsonArray = new JSONArray(
								jsonRoot.getString("news"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							
							list.add(new News(jsonObject.getString("name"),
									jsonObject.getString("user"), 
									Float.parseFloat(jsonObject.getString("weight")),
									Integer.parseInt(jsonObject.getString("height")), 
									Integer.parseInt(jsonObject.getString("activity")),
									Integer.parseInt(jsonObject.getString("sex")),
									Integer.parseInt(jsonObject.getString("age")),
									jsonObject.getString("news_id"),
									jsonObject.getString("news"),
									jsonObject.getString("news_date"),
									jsonObject.getString("news_iscoment")));
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
	private class SendNewsTask extends AsyncTask<Void, Void, Void> {

		private String searchString;
		private String newstext;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			emptyLayout.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext())) {
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				try {
					newstext = URLEncoder.encode(news.getText().toString(), "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				StringBuffer parameters = new StringBuffer("");
				parameters.append("?news="+newstext+"&id="+SaveUtils.getUserUnicId(SocialUserNewsActivity.this));
				HttpGet httpGet = new HttpGet(URL + parameters);
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
					String resultString = builder.toString().trim();
					
					
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
			onResume();
		}

	}
	
	private class AddComentTask extends AsyncTask<Void, Void, Void> {

		private String searchString;
		private String newstext;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			emptyLayout.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext())) {
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				String comentStr = "";
				try {
					comentStr = URLEncoder.encode(coment.getText().toString(), "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				StringBuffer parameters = new StringBuffer("");
				parameters.append("?coment="+comentStr+"&master_news_id="+ commentNewsId +"&id="+SaveUtils.getUserUnicId(SocialUserNewsActivity.this));
				HttpGet httpGet = new HttpGet(URL + parameters);
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
					String resultString = builder.toString().trim();
					
					
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
			commentNewsId = null;
			onResume();
			
		}

	}


	private void setHeader() {
		// TODO Auto-generated method stub		
		/*nameView.setText(userName);
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
*/
	}


	
	
	
	public void switchTabInActivity(int indexTabToSwitchTo){
        DietHelperActivity parentActivity;
        parentActivity = (DietHelperActivity) this.getParent();
        parentActivity.switchTab(indexTabToSwitchTo);
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

		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.VISIBLE);
		}
		try {
			NewsArrayAdapter da = new NewsArrayAdapter(this, getApplicationContext(),
					R.layout.social_news_row, list,
					(SocialActivityGroup)SocialUserNewsActivity.this.getParent());
			newsList.setAdapter(da);		
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
	public void showingComentDialog(String id) {
		commentNewsId = id;
		showDialog(SocialNewsListActivity.DIALOG_COMMENT);
		
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		switch (id) {
		case SocialNewsListActivity.DIALOG_COMMENT:
			dialog = new Dialog(this.getParent());
			dialog.setContentView(R.layout.add_coment_dialog);
			dialog.setTitle(R.string.soc_coment);

			coment = (EditText) dialog.findViewById(R.id.editTextComent);
			coment.setText("");
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					if(coment.getText().length()<1){
						coment.setBackgroundColor(Color.RED);
					}else{
						dialog.cancel();
						new SocialUserNewsActivity.AddComentTask().execute();
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
	
}
