package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.MessagesAdapter;
import bulat.diet.helper_sport.db.MessagesHelper;
import bulat.diet.helper_sport.item.MessageItem;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class ChatActivity extends BaseActivity {
	ListView messageList;
	int sum;
	String userId = null;
	String userName = "";
	private TextView loadingView;
	private TextView badSearchView;
	private View emptyLayout;
	private EditText message;
	InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.social_messages_list, null);
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		emptyLayout = viewToLoad.findViewById(R.id.emptyLayoutCalendar);
		badSearchView = (TextView) viewToLoad
				.findViewById(R.id.textViewBadSearch);

		messageList = (ListView) viewToLoad.findViewById(R.id.listViewMessages);
		Button removeButton = (Button) viewToLoad.findViewById(R.id.buttonRemove);
		removeButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = null;
				
				builder = new AlertDialog.Builder(ChatActivity.this.getParent());
				builder.setMessage(R.string.remove_chat_dialog)
						.setPositiveButton(getString(R.string.yes),
								dialogClickListener)
						.setNegativeButton(getString(R.string.no),
								dialogClickListener).show();		
			}
		});
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setContentView(viewToLoad);
		if (extras != null) {
			userName = extras.getString(SocialDishActivity.USERNAME);

			userId = extras.getString(SocialDishActivity.USERID);
		}
		message = (EditText) viewToLoad.findViewById(R.id.EditTextMessage);
		message.setOnEditorActionListener(messageEditorListener);
		Button buttonSend = (Button) viewToLoad
				.findViewById(R.id.buttonSendMessage);
		buttonSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (message.getText().toString().trim().length() > 0) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					buffer = message.getText().toString().trim();
					 message.setText("");
					new SendMessageTask().execute();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		super.onResume();
		if (userId != null) {
			new UpdateMessgesTask().execute();
		}
		setAdapter();
	}

	private OnItemClickListener messageListOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			// switchTabInActivity(1)
		}
	};
	private String buffer;

	private void setAdapter() {
		Cursor c = MessagesHelper.getMessagesByUser(getApplicationContext(),
				userId);
		if (c != null) {
			try {

				MessagesAdapter da = new MessagesAdapter(
						getApplicationContext(), c);
				messageList.setAdapter(da);
				messageList.setItemsCanFocus(true);
				messageList
						.setOnItemClickListener(messageListOnItemClickListener);
			} catch (Exception e) {
				if (c != null)
					c.close();
			} finally {
				// c.close();
			}
		}
	}

	private class SendMessageTask extends AsyncTask<Void, Void, Void> {
		boolean done = false;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext()))
				try {
					{

						StringBuilder builder = new StringBuilder();
						HttpClient client = new DefaultHttpClient();
						SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM",new Locale( SaveUtils.getLang(ChatActivity.this)));
						String date = sdf.format(new Date());
						try {
							String dateTemp = URLEncoder.encode(date, "UTF-8");

							StringBuffer parameters = new StringBuffer("");
							parameters.append("?user_from_id=" +  SaveUtils
									.getUserUnicId(ChatActivity.this));
							parameters.append("&user_to_id="
									+userId);
							parameters.append("&date=" + dateTemp);
							parameters.append("&date_int="
									+ new Date().getTime());
							parameters.append("&username="
									+ URLEncoder.encode(SaveUtils.getNikName(ChatActivity.this), "UTF-8"));
							parameters.append("&text="
									+ URLEncoder.encode(buffer, "UTF-8"));

							HttpGet httpGet = new HttpGet(Constants.URL_SOCIAL
									+ parameters);

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
							try {
								if (Integer.valueOf(resultString) > 0){
									done = true;
									
									MessagesHelper
											.addNewMessage(
													new MessageItem(
															0,
															userId,
															String.valueOf(SaveUtils
																	.getUserUnicId(ChatActivity.this)),
															date,
															userName,
															String.valueOf(new Date()
																	.getTime()),
															buffer, ""),
													ChatActivity.this);
								}else{
									done = false;
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
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			checkTask(done);
		}

		

	}
	private void checkTask(boolean done2) {
		if(done2){
			message.setText("");
		}else{
			message.setText(buffer);
			Toast.makeText(this, getString(R.string.disonect),
					Toast.LENGTH_LONG).show();
		}
		
	}
	private class UpdateMessgesTask extends AsyncTask<Void, Void, Void> {

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
				HttpGet httpGet = new HttpGet(Constants.URL_SOCIAL
						+ "?user_from_id=" + userId + "&user_to_id="
						+ SaveUtils.getUserUnicId(ChatActivity.this));
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
								jsonRoot.getString("messages"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							MessagesHelper.addNewMessage(
									new MessageItem(0, 
											jsonObject.getString("user_from_id"),
											jsonObject.getString("user_from_id"),
											jsonObject.getString("date"),
											jsonObject.getString("name"),
											jsonObject.getString("date_int"),
											jsonObject.getString("text"),""),
									ChatActivity.this);
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
	private OnEditorActionListener messageEditorListener = new OnEditorActionListener() {

		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return true;
			}
			return false;
		}

	};

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					MessagesHelper.clearConversation(ChatActivity.this, userId);	
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:				
				break;
			}
		}
	};
}
