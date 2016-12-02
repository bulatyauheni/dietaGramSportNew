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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
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
import bulat.diet.helper_sport.utils.SocialUpdater;

public class SocialNewsListActivity extends BaseActivity {
	private static final String URL = Constants.URL_SOCIAL_NEWS;
	private static final int DIALOG_SEARCH_OPTIONS = 0;
	private static final int DIALOG_USER_OPTIONS = 1;
	private static final int DIALOG_USER_NAME = 2;
	private static final int DIALOG_CHANGE_USER_NAME = 3;
	public static final int DIALOG_COMMENT = 4;
	ListView newsList;
	int sum;
	private TextView loadingView;
	ArrayList<News> list = new ArrayList<News>();
	private LinearLayout badSearchView;
	private View emptyLayout;
	private Button changebutton;
	private String removedNewsId;
	private String commentNewsId;
	private String ownerNewsId;
	private Button settingsButton;


	private int needPostion;
	private TextView news;
	@Override
	protected void onPause() {
		SaveUtils.saveScrollPosition(newsList.getFirstVisiblePosition(),
				SocialNewsListActivity.this);
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
       
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.social_news_list, null);
		setContentView(viewToLoad);
		news = (TextView) viewToLoad.findViewById(R.id.EditTextNews);
		//tabs
				Button messagesTab= (Button) viewToLoad.findViewById(R.id.messagesTab);
				messagesTab.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
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
							activityStack.push("searchTab", intent);
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
		

		////
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		// search string
		
		
		emptyLayout = viewToLoad.findViewById(R.id.emptyLayout);
		badSearchView = (LinearLayout)findViewById(R.id.linearLayoutEmptyListHeader);
		badSearchView.setOnClickListener(addTodayDishListener);
		//badSearchView = (TextView) viewToLoad
		//		.findViewById(R.id.textViewBadSearch);
		newsList = (ListView) viewToLoad.findViewById(R.id.listViewUser);
		//newsList.setOnItemClickListener(newsListOnItemClickListener);

		//changebutton = (Button) viewToLoad.findViewById(R.id.buttonChangeDish);
		//changebutton.setOnClickListener(changeButtonListener);
		Button buttonProfile = (Button) viewToLoad.findViewById(R.id.buttonProfile);
		buttonProfile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent();
				
				
				intent.putExtra(SocialDishActivity.USERID,String.valueOf(SaveUtils.getUserUnicId(SocialNewsListActivity.this)));
				intent.putExtra(SocialDishActivity.USERNAME, SaveUtils.getNikName(SocialNewsListActivity.this));
				intent.putExtra(SocialDishActivity.USERWEIGHT, String.valueOf(SaveUtils.getRealWeight(SocialNewsListActivity.this)));
				intent.putExtra(SocialDishActivity.USERHEIGHT, String.valueOf((SaveUtils.getHeight(SocialNewsListActivity.this) + Info.MIN_HEIGHT)));
				if(!"0".equals( SaveUtils.getSex(SocialNewsListActivity.this))){
					intent.putExtra(SocialDishActivity.USERSEX, SocialNewsListActivity.this.getString(R.string.male));
				}else{
					intent.putExtra(SocialDishActivity.USERSEX, SocialNewsListActivity.this.getString(R.string.female));
				}
				intent.putExtra(SocialDishActivity.USERAGE, String.valueOf((SaveUtils.getAge(SocialNewsListActivity.this) + Info.MIN_AGE)));
				intent.putExtra(SocialDishActivity.USERSPORTING, SaveUtils.getActivity(SocialNewsListActivity.this));
				
				intent.setClass(getParent(), SocialCalendarActivity.class);
				SocialActivityGroup activityStack = (SocialActivityGroup) getParent();
				if(activityStack.getStack().contains("SocialCalendarActivity")){
							activityStack.pushInstead2("SocialCalendarActivity", intent);		
				}else{
							activityStack.push("SocialCalendarActivity", intent);		
				}
									
			}
		});

		Button buttonNewsSend = (Button) viewToLoad.findViewById(R.id.buttonSendNews);
		buttonNewsSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(news.getText().toString().length()>0){
					new SendNewsTask().execute();
				}
			}
		});

		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
	}

	@Override
	protected void onResume() {
		news.setText("");
		needPostion = SaveUtils.loadScrollPosition(this);
		// TODO Auto-generated method stub
		super.onResume();
		// TODO Auto-generated method stub		
		
		if (NetworkState.isOnline(getApplicationContext())) {
			if (0==SaveUtils.getUserUnicId(this)) {
				showDialog(DIALOG_USER_NAME);
			}
		new LoadNewsTask().execute();
		} else {
			DialogUtils.showDialog(SocialNewsListActivity.this.getParent(),
					R.string.disonect);
		}
	}

	private OnItemClickListener newsListOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			// switchTabInActivity(1)
			try{
			Intent intent = new Intent();
			TextView idView = (TextView) v.findViewById(R.id.textViewUserId);
			TextView nameView = (TextView) v.findViewById(R.id.textViewName2);
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
			}catch (Exception e) {
				e.printStackTrace();
			}
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
	private EditText coment;
	private EditText nameSearch;
	private CheckBox nameCheckbox;

	public void switchTabInActivity(int indexTabToSwitchTo) {
		DietHelperActivity parentActivity;
		parentActivity = (DietHelperActivity) this.getParent();
		parentActivity.switchTab(indexTabToSwitchTo);
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
				parameters.append("?news_select=1&id="+SaveUtils.getUserUnicId(SocialNewsListActivity.this));
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
				parameters.append("?news="+newstext+"&id="+SaveUtils.getUserUnicId(SocialNewsListActivity.this));
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
	private class RemoveNewsTask extends AsyncTask<Void, Void, Void> {

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
				
				StringBuffer parameters = new StringBuffer("");
				if(removedNewsId!= null){
					parameters.append("?news_rem="+removedNewsId+"&id="+SaveUtils.getUserUnicId(SocialNewsListActivity.this));
				}
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
			removedNewsId = null;
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
				parameters.append("?coment="+comentStr+"&owner_news_id="+ ownerNewsId +"&master_news_id="+ commentNewsId +"&id="+SaveUtils.getUserUnicId(SocialNewsListActivity.this));
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
			ownerNewsId = null;
			onResume();
			
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
												.getNikName(SocialNewsListActivity.this), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					parameters.append("&weight="
							+(SaveUtils
							.getWeight(SocialNewsListActivity.this) + Info.MIN_WEIGHT));
					parameters.append("&high="
							+(SaveUtils
							.getHeight(SocialNewsListActivity.this) + Info.MIN_HEIGHT));
					parameters.append("&age="
							+(SaveUtils
							.getAge(SocialNewsListActivity.this) + Info.MIN_AGE));
					parameters.append("&sex="
							+(SaveUtils
							.getSex(SocialNewsListActivity.this)));
					parameters.append("&activity="
							+(SaveUtils
							.getActivity(SocialNewsListActivity.this)));
					
					
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
							SaveUtils.setUserUnicId(SocialNewsListActivity.this, Integer.valueOf(resultString));
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
			NewsArrayAdapter da = new NewsArrayAdapter(this, getApplicationContext(),
					R.layout.social_news_row, list,
					(SocialActivityGroup)SocialNewsListActivity.this.getParent());
			newsList.setAdapter(da);
			if(list.size() > needPostion){
				newsList.setSelection(needPostion);
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
						SaveUtils.setNikName(SocialNewsListActivity.this, userName.getText().toString());
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
						SaveUtils.setNikName(SocialNewsListActivity.this, userName.getText().toString());
						dialog.cancel();
						//update social profile
						if(SaveUtils.getUserUnicId(SocialNewsListActivity.this) != 0){
							new SocialUpdater(SocialNewsListActivity.this).execute();
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
		case DIALOG_COMMENT:
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
						new AddComentTask().execute();
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
			// DialogUtils.showDialog(Info.this.getParent(),
			// getString(R.string.save_prove));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener changeButtonListener = new OnClickListener() {

		public void onClick(View v) {
			showDialog(DIALOG_SEARCH_OPTIONS);
		}
	};
	private OnClickListener addTodayDishListener = new OnClickListener() {
		
		public void onClick(View v) {showDialog(DIALOG_SEARCH_OPTIONS);}
	};
	public void showingDialogCancel(String id) {
		removedNewsId = id;
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setMessage(R.string.remove_dialog)
				.setPositiveButton(getString(R.string.yes), dialogClickListener)
				.setNegativeButton(getString(R.string.no), dialogClickListener)
				.show();
	}
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					new RemoveNewsTask().execute();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				removedNewsId = null;
				break;
			}
		}
	};

	
	public void showingComentDialog(String id, String ownerId) {
		commentNewsId = id;
		ownerNewsId = ownerId;
		showDialog(DIALOG_COMMENT);
		
	}
	

	
}
