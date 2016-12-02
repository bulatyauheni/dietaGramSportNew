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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.UserArrayAdapter;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.User;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;

public class SocialFollowersListActivity extends BaseActivity {
	private static final String URL = Constants.URL_SOCIAL_NEWS;
	private static final int DIALOG_USER_OPTIONS = 1;
	private static final int DIALOG_USER_NAME = 2;
	private static final int DIALOG_CHANGE_USER_NAME = 3;
	ListView userList;
	int sum;
	private TextView loadingView;
	ArrayList<User> list = new ArrayList<User>();
	private LinearLayout badSearchView;
	private View emptyLayout;
	private Button changebutton;

	private Spinner ageSpinner;
	private Spinner sexSpinner;
	private Spinner weightSpinner;
	private Spinner highSpinner;
	private Spinner activitySpinner;
	private TextView sexView;
	private TextView weightView;
	private TextView heightView;
	private Button settingsButton;

	private TextView ageView;
	private TextView nameView;
	private ImageView sportingView;
	private int needPostion;
	private Button followersButton;
	private Button contactsButton;
	private Button favoriteButton;
	@Override
	protected void onPause() {
		SaveUtils.saveScrollPosition(userList.getFirstVisiblePosition(),
				SocialFollowersListActivity.this);
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
       
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.social_contact_followers_list, null);
		Button folButton = (Button) viewToLoad.findViewById(R.id.FoloversUsersButton);
		folButton.setTextColor(Color.WHITE);
		setContentView(viewToLoad);
		//tabs

				Button messagesTab= (Button) viewToLoad.findViewById(R.id.messagesTab);
				messagesTab.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent();					
							intent.setClass(getParent(), MessagesActivity.class);					
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
				Button buttonProfile = (Button) viewToLoad.findViewById(R.id.buttonProfile);
				buttonProfile.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						
						Intent intent = new Intent();
						
						
						intent.putExtra(SocialDishActivity.USERID,String.valueOf(SaveUtils.getUserUnicId(SocialFollowersListActivity.this)));
						intent.putExtra(SocialDishActivity.USERNAME, SaveUtils.getNikName(SocialFollowersListActivity.this));
						intent.putExtra(SocialDishActivity.USERWEIGHT, String.valueOf(SaveUtils.getRealWeight(SocialFollowersListActivity.this)));
						intent.putExtra(SocialDishActivity.USERHEIGHT, String.valueOf((SaveUtils.getHeight(SocialFollowersListActivity.this) + Info.MIN_HEIGHT)));
						if(!"0".equals( SaveUtils.getSex(SocialFollowersListActivity.this))){
							intent.putExtra(SocialDishActivity.USERSEX, SocialFollowersListActivity.this.getString(R.string.male));
						}else{
							intent.putExtra(SocialDishActivity.USERSEX, SocialFollowersListActivity.this.getString(R.string.female));
						}
						intent.putExtra(SocialDishActivity.USERAGE, String.valueOf((SaveUtils.getAge(SocialFollowersListActivity.this) + Info.MIN_AGE)));
						intent.putExtra(SocialDishActivity.USERSPORTING, SaveUtils.getActivity(SocialFollowersListActivity.this));
						
						intent.setClass(getParent(), SocialCalendarActivity.class);
						SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
						if(activityStack.getStack().contains("SocialCalendarActivity")){
									activityStack.pushInstead2("SocialCalendarActivity", intent);		
						}else{
									activityStack.push("SocialCalendarActivity", intent);		
						}
											
					}
				});

				contactsButton= (Button) viewToLoad.findViewById(R.id.ConversationUsersButton);
				contactsButton.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent();					
							intent.setClass(getParent(), MessagesActivity.class);					
							SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
							activityStack.push("searchTab", intent);
						} catch (Exception e) {
							e.printStackTrace();
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
		

		////
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		// search string
		/*ageView = (TextView) viewToLoad.findViewById(R.id.textViewAgeValue);
		nameView = (TextView) viewToLoad.findViewById(R.id.textViewNameValue);
		sportingView = (ImageView) viewToLoad.findViewById(R.id.imageViewActivity);
		sexView = (TextView) viewToLoad.findViewById(R.id.textViewSexValue);
		heightView = (TextView) viewToLoad
				.findViewById(R.id.textViewHeightValue);
		weightView = (TextView) viewToLoad
				.findViewById(R.id.textViewWeightValue);
*/
		emptyLayout = viewToLoad.findViewById(R.id.emptyLayout);
		badSearchView = (LinearLayout)findViewById(R.id.linearLayoutEmptyListHeader);
		
		//badSearchView = (TextView) viewToLoad
		//		.findViewById(R.id.textViewBadSearch);
		userList = (ListView) viewToLoad.findViewById(R.id.listViewUser);
		userList.setOnItemClickListener(userListOnItemClickListener);

		//changebutton = (Button) viewToLoad.findViewById(R.id.buttonChangeDish);
		//changebutton.setOnClickListener(changeButtonListener);				
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void onResume() {
		needPostion = SaveUtils.loadScrollPosition(this);
		// TODO Auto-generated method stub
		super.onResume();
		// TODO Auto-generated method stub		
		//setSearchLine();

		if (NetworkState.isOnline(getApplicationContext())) {
			if (0==SaveUtils.getUserUnicId(this)) {
				showDialog(DIALOG_USER_NAME);
			}
			new LoadUsersTask().execute();
		} else {
			DialogUtils.showDialog(SocialFollowersListActivity.this.getParent(),
					R.string.disonect);
		}
	}

	private OnItemClickListener userListOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			// switchTabInActivity(1)
			
			Intent intent = new Intent();
			TextView idView = (TextView) v.findViewById(R.id.textViewUserId);
			TextView nameView = (TextView) v.findViewById(R.id.textViewName);
			TextView currAgeView = (TextView) v.findViewById(R.id.textViewUserAge);
			TextView currWeightView = (TextView) v.findViewById(R.id.textViewUserWeight);
			TextView currHeightView = (TextView) v.findViewById(R.id.textViewUserHeight);
			TextView currSexView = (TextView) v.findViewById(R.id.textViewUserSex);
			ImageView currSportView = (ImageView) v.findViewById(R.id.imageViewActivity);
			
			intent.putExtra(SocialDishActivity.USERID, idView.getText()
					.toString());
			intent.putExtra(SocialDishActivity.USERNAME, nameView.getText()
					.toString());
			intent.putExtra(SocialDishActivity.USERWEIGHT, currWeightView.getText()
					.toString());
			intent.putExtra(SocialDishActivity.USERHEIGHT, currHeightView.getText()
					.toString());
			if("0".equals( currSexView.getText().toString())){
				intent.putExtra(SocialDishActivity.USERSEX, getString(R.string.male));
			}else{
				intent.putExtra(SocialDishActivity.USERSEX, getString(R.string.female));
			}
			intent.putExtra(SocialDishActivity.USERAGE, currAgeView.getText()
					.toString());
			intent.putExtra(SocialDishActivity.USERSPORTING, currSportView.getHorizontalFadingEdgeLength());

								intent.setClass(getParent(), SocialCalendarActivity.class);
					SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
					activityStack.push("SocialCalendarActivity", intent);
		}
	};
	private Button buttonOk;
	private CheckBox sexCheckbox;
	private CheckBox ageCheckbox;
	private CheckBox weightCheckbox;
	private CheckBox highCheckbox;
	private CheckBox activityCheckbox;
	private Button nobutton;
	private EditText userName;
	private EditText nameSearch;
	private CheckBox nameCheckbox;

	public void switchTabInActivity(int indexTabToSwitchTo) {
		DietHelperActivity parentActivity;
		parentActivity = (DietHelperActivity) this.getParent();
		parentActivity.switchTab(indexTabToSwitchTo);
	}

	private class LoadUsersTask extends AsyncTask<Void, Void, Void> {

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
				parameters.append("?followers_select=1&id="+SaveUtils.getUserUnicId(SocialFollowersListActivity.this));
				
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
					list = new ArrayList<User>();
					try {
						JSONObject jsonRoot = new JSONObject(resultString);
						JSONArray jsonArray = new JSONArray(
								jsonRoot.getString("users"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							if(!jsonObject.getString("user").equals(
									String.valueOf(SaveUtils.getUserUnicId(SocialFollowersListActivity.this)))){
							list.add(new User(jsonObject.getString("name"),
									jsonObject.getString("user"), 
									Float.parseFloat(jsonObject.getString("weight")),
									Integer.parseInt(jsonObject.getString("height")), 
									Integer.parseInt(jsonObject.getString("activity")),
									Integer.parseInt(jsonObject.getString("sex")),
									Integer.parseInt(jsonObject.getString("age"))));
							}
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
												.getNikName(SocialFollowersListActivity.this), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					parameters.append("&weight="
							+(SaveUtils
							.getWeight(SocialFollowersListActivity.this) + Info.MIN_WEIGHT));
					parameters.append("&high="
							+(SaveUtils
							.getHeight(SocialFollowersListActivity.this) + Info.MIN_HEIGHT));
					parameters.append("&age="
							+(SaveUtils
							.getAge(SocialFollowersListActivity.this) + Info.MIN_AGE));
					parameters.append("&sex="
							+(SaveUtils
							.getSex(SocialFollowersListActivity.this)));
					parameters.append("&activity="
							+(SaveUtils
							.getActivity(SocialFollowersListActivity.this)));
					
					
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
							SaveUtils.setUserUnicId(SocialFollowersListActivity.this, Integer.valueOf(resultString));
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
			UserArrayAdapter da = new UserArrayAdapter(getApplicationContext(),
					R.layout.social_user_list_row, list,
					(SocialActivityGroup)SocialFollowersListActivity.this.getParent());
			userList.setAdapter(da);
			if(list.size() > needPostion){
				userList.setSelection(needPostion);
			}
			da.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
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
						SaveUtils.setNikName(SocialFollowersListActivity.this, userName.getText().toString());
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
						SaveUtils.setNikName(SocialFollowersListActivity.this, userName.getText().toString());
						dialog.cancel();
						//update social profile
						if(SaveUtils.getUserUnicId(SocialFollowersListActivity.this) != 0){
							new SocialUpdater(SocialFollowersListActivity.this).execute();
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

	private void setSpinnerValues() {
		try {
			DialogUtils.setArraySpinnerValues(ageSpinner, Info.MIN_AGE, Info.MAX_AGE,this);
			ageSpinner.setOnItemSelectedListener(spinnerListener);
			ageSpinner.setSelection(SaveUtils.getSearchAge(this));
			ageSpinner.setEnabled(SaveUtils.getSearchAgeEnbl(this));

			DialogUtils.setArraySpinnerValues(weightSpinner, Info.MIN_WEIGHT,
					Info.MAX_WEIGHT,this);
			weightSpinner.setOnItemSelectedListener(spinnerListener);
			weightSpinner.setSelection(SaveUtils.getSearchWeight(this));
			weightSpinner.setEnabled(SaveUtils.getSearchWeightEnbl(this));

			DialogUtils.setArraySpinnerValues(highSpinner, Info.MIN_HEIGHT, Info.MAX_HEIGHT,this);
			highSpinner.setOnItemSelectedListener(spinnerListener);
			highSpinner.setSelection(SaveUtils.getSearchHeight(this));
			highSpinner.setEnabled(SaveUtils.getSearchHeightEnbl(this));
			ArrayList<DishType> genders = new ArrayList<DishType>();
			genders.add(new DishType(166, getString(R.string.male)));
			genders.add(new DishType(0, getString(R.string.female)));
			ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
					android.R.layout.simple_spinner_item, genders);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sexSpinner.setAdapter(adapter);
			sexSpinner.setOnItemSelectedListener(spinnerListener);
			sexSpinner.setSelection(SaveUtils.getSearchSex(this));
			sexSpinner.setEnabled(SaveUtils.getSearchSexEnbl(this));

			ArrayList<DishType> activity = new ArrayList<DishType>();
			activity.add(new DishType(1, getString(R.string.level_1)));
			activity.add(new DishType(2, getString(R.string.level_2)));
			activity.add(new DishType(3, getString(R.string.level_3)));
			activity.add(new DishType(4, getString(R.string.level_4)));
			activity.add(new DishType(5, getString(R.string.level_5)));
			ArrayAdapter<DishType> adapter2 = new ArrayAdapter<DishType>(this,
					android.R.layout.simple_spinner_item, activity);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			activitySpinner.setAdapter(adapter2);
			activitySpinner.setOnItemSelectedListener(spinnerListener);
			activitySpinner.setSelection(SaveUtils.getSearchActivity(this));
			activitySpinner.setEnabled(SaveUtils.getSearchActivityEnbl(this));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setSearchLine() {
		if (SaveUtils.getSearchSexEnbl(this)) {
			if (SaveUtils.getSearchSex(this) == 0)
				sexView.setText(String.valueOf(getString(R.string.male)
						.toCharArray()[0]));
			else
				sexView.setText(String.valueOf(getString(R.string.female)
						.toCharArray()[0]));
		} else {
			sexView.setText(getString(R.string.any));
		}
		if (SaveUtils.getSearchWeightEnbl(this)) {
			weightView.setText(Integer.toString(SaveUtils.getSearchWeight(this)
					+ Info.MIN_WEIGHT));
		} else {
			weightView.setText(R.string.any);
		}
		if (SaveUtils.getSearchHeightEnbl(this)) {
			heightView.setText(Integer.toString(SaveUtils.getSearchHeight(this)
					+ Info.MIN_HEIGHT));
		} else {
			heightView.setText(R.string.any);
		}
		if (SaveUtils.getSearchAgeEnbl(this)) {
			ageView.setText(Integer.toString(SaveUtils.getSearchAge(this)
					+ Info.MIN_AGE));
		} else {
			ageView.setText(R.string.any);
		}
		if (SaveUtils.getSearchNameEnbl(this)) {
			nameView.setText(SaveUtils.getSearchName(this));
		} else {
			nameView.setText(R.string.any_midle);
		}
		if(SaveUtils.getSearchActivityEnbl(this)){
			sportingView.setAlpha(255);
			switch (SaveUtils.getSearchActivity(this)) {
		
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
		}else{
			sportingView.setAlpha(100);
		}
		
	}

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// saveAll();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	public void saveAll() {
		try {
			SaveUtils.saveSearchNameEnbl(nameSearch.isEnabled(), this);
			SaveUtils
					.saveSearchActivityEnbl(activityCheckbox.isChecked(), this);
			SaveUtils.saveSearchAgeEnbl(ageCheckbox.isChecked(), this);
			SaveUtils.saveSearchHeightEnbl(highCheckbox.isChecked(), this);
			SaveUtils.saveSearchSexEnbl(sexCheckbox.isChecked(), this);
			SaveUtils.saveSearchWeightEnbl(weightCheckbox.isChecked(), this);

			String name = nameSearch.getText().toString().trim();
			try {
				name = URLEncoder.encode(name, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			SaveUtils.saveSearchName(name, this);
			SaveUtils.saveSearchAge((int) ageSpinner.getSelectedItemId(), this);
			SaveUtils.saveSearchActivity(
					(int) activitySpinner.getSelectedItemId(), this);
			SaveUtils.saveSearchHeight((int) highSpinner.getSelectedItemId(),
					this);
			SaveUtils.saveSearchSex((int) sexSpinner.getSelectedItemId(), this);
			SaveUtils.saveSearchWeight((int) weightSpinner.getSelectedItemId(),
					this);
			// DialogUtils.showDialog(Info.this.getParent(),
			// getString(R.string.save_prove));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
}
