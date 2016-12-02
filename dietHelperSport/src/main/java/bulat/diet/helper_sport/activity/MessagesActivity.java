package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

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
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.ContactsAdapter;
import bulat.diet.helper_sport.db.MessagesHelper;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.MessageItem;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;

public class MessagesActivity extends BaseActivity {
	private static final int DIALOG_CHANGE_USER_NAME = 3;
	private static final int DIALOG_USER_NAME = 2;
	private static final String URL = Constants.URL_SOCIAL;
	ListView usersList;
	int sum;
	String userId = null;
	private TextView loadingView;
	private TextView badSearchView;
	private View emptyLayout;
	private Handler myHandler;
	private Button settingsButton;
	private Button changebutton;
	private Button conversationButton;
	private Button favoriteButton;
	private Button followersButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		Bundle extras = getIntent().getExtras();

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.social_contact_list, null);
		//tabs

		Button searchTab= (Button) viewToLoad.findViewById(R.id.searchTab);
		searchTab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();					
					intent.setClass(getParent(), SocialUserListActivity.class);					
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.push("searchTab", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Button newsTab= (Button) viewToLoad.findViewById(R.id.newsTab);
		newsTab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();					
					intent.setClass(getParent(), SocialNewsListActivity.class);					
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.push("SocialNewsListActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		Button buttonProfile = (Button) viewToLoad.findViewById(R.id.buttonProfile);
		buttonProfile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent();
				
				
				intent.putExtra(SocialDishActivity.USERID,String.valueOf(SaveUtils.getUserUnicId(MessagesActivity.this)));
				intent.putExtra(SocialDishActivity.USERNAME, SaveUtils.getNikName(MessagesActivity.this));
				intent.putExtra(SocialDishActivity.USERWEIGHT, String.valueOf(SaveUtils.getRealWeight(MessagesActivity.this)));
				intent.putExtra(SocialDishActivity.USERHEIGHT, String.valueOf((SaveUtils.getHeight(MessagesActivity.this) + Info.MIN_HEIGHT)));
				if(!"0".equals( SaveUtils.getSex(MessagesActivity.this))){
					intent.putExtra(SocialDishActivity.USERSEX, MessagesActivity.this.getString(R.string.male));
				}else{
					intent.putExtra(SocialDishActivity.USERSEX, MessagesActivity.this.getString(R.string.female));
				}
				intent.putExtra(SocialDishActivity.USERAGE, String.valueOf((SaveUtils.getAge(MessagesActivity.this) + Info.MIN_AGE)));
				intent.putExtra(SocialDishActivity.USERSPORTING, SaveUtils.getActivity(MessagesActivity.this));
				
				intent.setClass(getParent(), SocialCalendarActivity.class);
				SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
				if(activityStack.getStack().contains("SocialCalendarActivity")){
							activityStack.pushInstead2("SocialCalendarActivity", intent);		
				}else{
							activityStack.push("SocialCalendarActivity", intent);		
				}
									
			}
		});

		favoriteButton= (Button) viewToLoad.findViewById(R.id.favoriteUsersButton);
		favoriteButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();					
					intent.setClass(getParent(), SocialFavoriteUserListActivity.class);					
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.push("SocialFavoriteListActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		followersButton= (Button) viewToLoad.findViewById(R.id.FoloversUsersButton);
		followersButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();					
					intent.setClass(getParent(), SocialFollowersListActivity.class);					
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.push("SocialFollowersListActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		emptyLayout = viewToLoad.findViewById(R.id.emptyLayout);
		badSearchView = (TextView) viewToLoad
				.findViewById(R.id.textViewBadSearch);

		usersList = (ListView) viewToLoad.findViewById(R.id.listViewUser);
		changebutton = (Button) viewToLoad.findViewById(R.id.buttonChangeDish);
		changebutton.setOnClickListener(listener);
		LinearLayout searchLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutAVG);
		searchLayout.setOnClickListener(listener);

		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setContentView(viewToLoad);
		if (extras != null) {
			userId = extras.getString(SocialDishActivity.USERID);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (NetworkState.isOnline(getApplicationContext())) {
			if (0==SaveUtils.getUserUnicId(this)) {
				showDialog(DIALOG_USER_NAME);
			}
			new MessagesUpdaterTask().execute();
		} else {
			DialogUtils.showDialog(MessagesActivity.this.getParent(),
					R.string.disonect);
		}
		
	}

	protected void updateMessageCount(String message) {
		// TODO Auto-generated method stub
		DietHelperActivity parentActivity;
		parentActivity = (DietHelperActivity) this.getParent().getParent();
		parentActivity.changeSocialTabIndicator(3, message);
	}

	private OnItemClickListener usersListOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			// switchTabInActivity(1)
			Intent intent = new Intent();
			TextView idView = (TextView) v.findViewById(R.id.textViewUserId);
			TextView nameView = (TextView) v.findViewById(R.id.textViewName);
			
			intent.putExtra(SocialDishActivity.USERID, idView.getText()
					.toString());
			intent.putExtra(SocialDishActivity.USERNAME, nameView.getText()
					.toString());
			
			intent.setClass(getParent(), SocialCalendarActivity.class);
			SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
			activityStack.push("SocialCalendarActivity", intent);
		}
	};
	Cursor c = null;
	private int flag; 
	private void setAdapter(TreeMap<String,DishType> map) {
		//add hello message for new user		 
		 emptyLayout.setVisibility(View.GONE);
		 loadingView.setVisibility(View.GONE);
		 //set adapter
		 //conversation shows		 
		 if(flag == 0){
			 checkNewUser(map);
			 c = MessagesHelper.getUsers(getApplicationContext());
		 }else{
			 c = MessagesHelper.getFavoriteUsers(getApplicationContext());
		 }
		 

			 if (c != null) {
		 
		
		
			try {
				ContactsAdapter da = new ContactsAdapter(
						getApplicationContext(), c, map, (SocialActivityGroup)MessagesActivity.this.getParent());
				usersList.setAdapter(da);
				usersList.setItemsCanFocus(true);
				usersList.setOnItemClickListener(usersListOnItemClickListener);
			} catch (Exception e) {
				e.printStackTrace();
				if (c != null)
					c.close();
			} finally {
				// c.close();
			}
		}
	}

	private void checkNewUser(TreeMap<String,DishType> map) {
		ArrayList<String> users = MessagesHelper.getUsersList(this);
		Set<String> ids = map.keySet();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM",new Locale( SaveUtils.getLang(this)));
		String curentDateandTime = sdf.format(new Date());
		
		for (String id : ids) {
			if(!users.contains(id)){
				
				MessagesHelper.addNewMessage(
						new MessageItem(
								0,
								id,
								id,
								curentDateandTime,
								map.get(id).getDescription(),
								String.valueOf(0),
								getString(R.string.newContactMessage),""),
						this);	
						
			}			
		}
		
	}

	private class MessagesUpdaterTask extends AsyncTask<Void, Void, Void> {

		
		int fullCount;
		TreeMap<String, DishType> map = new TreeMap<String, DishType>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			emptyLayout.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (NetworkState.isOnline(getApplicationContext())) {
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				// searchString = searchString.replaceAll(" ", "%20");
				StringBuffer parametersb = new StringBuffer("");
				parametersb.append("?check_new_messages="
						+ SaveUtils.getUserUnicId(MessagesActivity.this));
				HttpGet httpGet = new HttpGet(Constants.URL_SOCIAL
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
					try {

						JSONObject jsonRoot = new JSONObject(resultString);
						JSONArray jsonArray = new JSONArray(
								jsonRoot.getString("updates"));

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							fullCount = fullCount
									+ Integer.parseInt(jsonObject
											.getString("count"));
							map.put(jsonObject.getString("user_from_id"),
									new DishType(Integer.parseInt(jsonObject
											.getString("count")), jsonObject
											.getString("user_name")));
						}
						// SaveUtils.saveNewMessagesCount();
					} catch (Exception e) {
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
			// TODO Auto-generated method stub
			updateMessageCount(String.valueOf(fullCount));
			setAdapter(map);
		}
	}
	private Button buttonOk;
	
	private Button nobutton;
	private EditText userName;

	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		switch (id) {
		
		case DIALOG_USER_NAME:
			dialog = new Dialog(this.getParent());
			dialog.setContentView(R.layout.user_name_dialog);
			dialog.setTitle(R.string.username_dialog_title);

			userName = (EditText) dialog.findViewById(R.id.editTextUserName);
			userName.setText(SaveUtils.getNikName(this));
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					if(userName.getText().length()<1){
						userName.setBackgroundColor(Color.RED);
					}else{
						SaveUtils.setNikName(MessagesActivity.this, userName.getText().toString());
						dialog.cancel();
						new RegisterUserTask().execute();
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
		case DIALOG_CHANGE_USER_NAME:
			dialog = new Dialog(this.getParent());
			if(SaveUtils.getUserUnicId(this)!=0){
				dialog.setContentView(R.layout.user_name_change_dialog);
				dialog.setTitle(R.string.username_change_dialog_title);
				TextView idView2 = (TextView) dialog.findViewById(R.id.textViewId);
				
				idView2.setText("id" + SaveUtils.getUserUnicIdFake(MessagesActivity.this));
			}else{
				dialog.setContentView(R.layout.user_name_dialog);
				dialog.setTitle(R.string.username_dialog_title);
			}

			userName = (EditText) dialog.findViewById(R.id.editTextUserName);
			userName.setText(SaveUtils.getNikName(this));
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					if(userName.getText().length()<1){
						userName.setBackgroundColor(Color.RED);
					}else{
						SaveUtils.setNikName(MessagesActivity.this, userName.getText().toString());
						dialog.cancel();
						//update social profile
						if(SaveUtils.getUserUnicId(MessagesActivity.this) != 0){
							new SocialUpdater(MessagesActivity.this).execute();
						}
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
	private class RegisterUserTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext())) {
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				// searchString = searchString.replaceAll(" ", "%20");
				StringBuffer parameters = new StringBuffer("");
					try {
						parameters.append("?username="
										 + URLEncoder.encode(SaveUtils
												.getNikName(MessagesActivity.this), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					parameters.append("&weight="
							+(SaveUtils
							.getWeight(MessagesActivity.this) + Info.MIN_WEIGHT));
					parameters.append("&high="
							+(SaveUtils
							.getHeight(MessagesActivity.this) + Info.MIN_HEIGHT));
					parameters.append("&age="
							+(SaveUtils
							.getAge(MessagesActivity.this) + Info.MIN_AGE));
					parameters.append("&sex="
							+(SaveUtils
							.getSex(MessagesActivity.this)));
					parameters.append("&activity="
							+(SaveUtils
							.getActivity(MessagesActivity.this)));
					
					
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
						try{
						if(Integer.valueOf(resultString) > 0)
							SaveUtils.setUserUnicId(MessagesActivity.this, Integer.valueOf(resultString));
						}catch (Exception e) {
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
			showTip();
		}
	}
	private void showTip(){
		if(SaveUtils.getUserUnicId(this) != 0){
			Toast.makeText(this, getString(R.string.username_dialog_registred),
					Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, getString(R.string.username_dialog_notregistred),
					Toast.LENGTH_LONG).show();
		}
	}
	private View.OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			SaveUtils.saveScrollPosition(0,
					MessagesActivity.this);
			intent.setClass(getParent(), SocialUserListActivity.class);
			SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
			activityStack.push("SocialUserListActivity", intent);
		}
	};
}
