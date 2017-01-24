package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Purchase;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.IabHelper;
import bulat.diet.helper_sport.utils.IabResult;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class PaymentsListActivity extends BasePayActivity {
	
	public static final String SKU_YEAR_VIP = "vip_abonement";

	public static final String SKU_YEAR_2017 = "dg_sport_1year_2017";
	public static final String SKU_HALFYEAR_2017 = "dg_sport_halfyear_2017";
	public static final String SKU_MUUNTH_2017 = "dg_sport_month_2017";

	public static final String SKU_YEAR_NEW = "2016_year_ordinary";
	public static final String SKU_HALFYEAR = "2016_halfyear_ordinary";
	public static final String SKU_MUUNTH_NEW = "2016_month_ordinary";
	
	public static final String SKU_MUNTH_OLD = "calorycounter_sport1";
	public static final String SKU_YEAR_OLD = "yearsubs_diethelper";
	public static final String SKU_MUNTH = "munth_plan";
	protected static final int DIALOG_EMAIL = 2;
	// static final String SKU_HALFYEAR = "halfyearsubs_diethelper";
	public static final String SKU_YEAR = "year_plan";
	static final int RC_REQUEST = 10001;
	private static final String PAYMENT_ERROR_REPORT = "PAYMENT_ERROR_REPORT";
	private static final String ABONEMENT = "ABONEMENT";
	private static final String ABONEMENT_VIP = "ABONEMENT_VIP";
	private static final String ABONEMENT_YEAR = "ABONEMENT_YEAR";
	private static final String ABONEMENT_HALFYEAR = "ABONEMENT_HALFYEAR";
	private static final String ABONEMENT_MONTH = "ABONEMENT_MONTH";
	private static final String ABONEMENT_FREE = "ABONEMENT_FREE";
	Context ctx = null;
	private int selectedInemId;
	StatisticActivityGroup parent;
	// The helper object
	int test = 0;
	String TAG = "PaymentsListActivity";
	private EditText userNameET;
	private Button nobutton;
	private TextView textViewIdValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.payment, null);
		setContentView(viewToLoad);
		ctx = this;
		
		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		textViewIdValue = (TextView) viewToLoad
				.findViewById(R.id.textViewIdValue);
		Date cuDate = new Date();
		int daysCount = (int) ((SaveUtils.getEndPDate(this) - cuDate.getTime()) / DateUtils.DAY_IN_MILLIS);
		if (daysCount >= 0) {
			textViewIdValue.setText(getString(R.string.days_limit) + ": "
					+ daysCount);
		}

		parent = (StatisticActivityGroup) PaymentsListActivity.this.getParent();
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		String[] list = { getString(R.string.paymentvipy),
				getString(R.string.paymenty)+ " " + String.format(getString(R.string.paymentbenefit), "70%"),
				getString(R.string.paymenthy)+ " " + String.format(getString(R.string.paymentbenefit), "58%"),
				getString(R.string.paymentm),
				getString(R.string.paymentspec),
				getString(R.string.paymenterror) };

		ListView listView = (ListView) findViewById(R.id.listViewStatistics);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.payment_row, list) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);
				if (position == 0) {
					textView.setBackgroundColor(Color.WHITE);
				} else {				
					textView.setTextColor(Color.DKGRAY);
				}

				return view;
			}
		};
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectedInemId = arg2;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PaymentsListActivity.this.getParent().getParent());
				if (arg2 == 0) {
					GATraker.sendEvent(ABONEMENT, ABONEMENT_VIP);
					builder.setMessage(R.string.payment_dialog_vipyear);
				} else
				if (arg2 == 1) {
					GATraker.sendEvent(ABONEMENT, ABONEMENT_YEAR);
					builder.setMessage(R.string.payment_dialog_year);
				} else
				if (arg2 == 2) {
					GATraker.sendEvent(ABONEMENT, ABONEMENT_HALFYEAR);
					builder.setMessage(R.string.payment_dialog_halfyear);
				} else
				if (arg2 == 3) {
					GATraker.sendEvent(ABONEMENT, ABONEMENT_MONTH);
					builder.setMessage(R.string.payment_dialog_month);
				} else
				if (arg2 == 4) {
					GATraker.sendEvent(ABONEMENT, ABONEMENT_FREE);
					PaymentsListActivity.this.showDialog(DIALOG_EMAIL);
				} else
				if (arg2 == 5) {
					GATraker.sendEvent(ABONEMENT, PAYMENT_ERROR_REPORT);
					CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(PaymentsListActivity.this.getParent().getParent());
					bld.setLayout(R.layout.section_alert_dialog_two_buttons)
					.setMessage(PaymentsListActivity.this.getParent().getString(R.string.complain_for_payment))			
					.setPositiveButton(R.id.dialogButtonOk, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
						            "mailto","bulat.yauheni@gmail.com", null));
							emailIntent.putExtra(Intent.EXTRA_SUBJECT, PaymentsListActivity.this.getParent().getString(R.string.app_name));
							emailIntent.putExtra(Intent.EXTRA_TEXT, "");
							PaymentsListActivity.this.getParent().getParent().startActivity((Intent.createChooser(emailIntent, "Send email...")));
						}
					})
					.setPositiveButtonText(R.string.agree)
					.setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {
	
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					})
					.setNegativeButtonText(R.string.disagree);
					bld.show();
				}
				if (arg2 !=5 && arg2 != 4){
					builder.setPositiveButton(getString(R.string.yes),
							dialogClickListener)
							.setNegativeButton(getString(R.string.no),
									dialogClickListener).show();
				}

			}
		});
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					Bundle buyIntentBundle;
					PendingIntent pendingIntent = null;
					if (selectedInemId == 0) {
						if (!mHelper.subscriptionsSupported()) {
							complain("Subscriptions not supported on your device yet. Sorry!");
							return;
						}

						String payload = ""
								+ SaveUtils
										.getUserAdvId(PaymentsListActivity.this);

						Log.d(TAG,
								"Launching purchase flow for infinite gas subscription.");
						mHelper.launchPurchaseFlow(PaymentsListActivity.this,
								SKU_YEAR_VIP, IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
								mPurchaseFinishedListener, payload);
					} else if (selectedInemId == 1) {
						if (!mHelper.subscriptionsSupported()) {
							complain("Subscriptions not supported on your device yet. Sorry!");
							return;
						}

						String payload = ""
								+ SaveUtils
										.getUserAdvId(PaymentsListActivity.this);

						Log.d(TAG,
								"Launching purchase flow for infinite gas subscription.");
						mHelper.launchPurchaseFlow(PaymentsListActivity.this,
								SKU_YEAR_2017, IabHelper.ITEM_TYPE_SUBS,
								RC_REQUEST, mPurchaseFinishedListener, payload);
					} else if (selectedInemId == 2) {
						if (!mHelper.subscriptionsSupported()) {
							complain("Subscriptions not supported on your device yet. Sorry!");
							return;
						}

						String payload = ""
								+ SaveUtils
										.getUserAdvId(PaymentsListActivity.this);

						Log.d(TAG,
								"Launching purchase flow for infinite gas subscription.");
						mHelper.launchPurchaseFlow(PaymentsListActivity.this,
								SKU_HALFYEAR_2017, IabHelper.ITEM_TYPE_SUBS,
								RC_REQUEST, mPurchaseFinishedListener, payload);
					} else if (selectedInemId == 3) {
						if (!mHelper.subscriptionsSupported()) {
							complain("Subscriptions not supported on your device yet. Sorry!");
							return;
						}

						String payload = ""
								+ SaveUtils
										.getUserAdvId(PaymentsListActivity.this);

						Log.d(TAG,
								"Launching purchase flow for infinite gas subscription.");
						mHelper.launchPurchaseFlow(PaymentsListActivity.this,
								SKU_MUUNTH_2017, IabHelper.ITEM_TYPE_SUBS,
								RC_REQUEST, mPurchaseFinishedListener, payload);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				selectedInemId = 10;
				break;
			}
		}
	};

	private class UpdatePaymentTask extends AsyncTask<Void, Void, Void> {
		String email = "";
		Boolean allOk = false;

		public UpdatePaymentTask(String email) {
			this.email = email;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (NetworkState.isOnline(PaymentsListActivity.this)) {
				StringBuilder builder = new StringBuilder();
				HttpParams httpParameters = new BasicHttpParams();

				int timeoutConnection = 5000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				int timeoutSocket = 5000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				HttpClient client = new DefaultHttpClient(httpParameters);
				// searchString = searchString.replaceAll(" ", "%20");
				Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				Account[] accounts = AccountManager.get(
						PaymentsListActivity.this).getAccountsByType(
						"com.google");

				StringBuffer parametersb = new StringBuffer("");
				parametersb.append("?updatePurchase=" + 1);
				parametersb.append("&userEmail=" + email);
				parametersb.append("&userAdvId="
						+ SaveUtils.getUserAdvId(PaymentsListActivity.this));
				HttpGet httpGet = new HttpGet(Constants.URL_PAY + parametersb);
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
							if (jsonObject.getString("user_productId") != null) {
								SaveUtils.setPaymentType(
										jsonObject.getString("user_productId"),
										PaymentsListActivity.this);
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyyMMdd");
								Date date = format.parse(jsonObject
										.getString("user_paymentDate"));
								Date currDate = new Date();
								// if(((currDate.getTime()-date.getTime())/DateFormat.DAY)<Integer.parseInt(jsonObject.getString("user_expdate"))){
								SaveUtils
										.setEndPDate(
												date.getTime()
														+ DateUtils.DAY_IN_MILLIS
														* Integer
																.parseInt(jsonObject
																		.getString("user_expdate")),
												PaymentsListActivity.this);
								// }
								if (currDate.getTime() < (date.getTime() + DateUtils.DAY_IN_MILLIS
										* Integer.parseInt(jsonObject
												.getString("user_expdate")))) {
									allOk = true;
								}
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!allOk) {
				Toast.makeText(PaymentsListActivity.this,
						getString(R.string.user_abonement_empty),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(PaymentsListActivity.this,
						getString(R.string.user_abonement_ok),
						Toast.LENGTH_LONG).show();
				Date cuDate = new Date();
				int daysCount = (int) ((SaveUtils
						.getEndPDate(PaymentsListActivity.this) - cuDate
						.getTime()) / DateUtils.DAY_IN_MILLIS);
				if (daysCount >= 0) {
					textViewIdValue.setText(getString(R.string.days_limit)
							+ ": " + daysCount);
				}
			}
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	   if (requestCode == 1001) {           
	      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	        
	      if (resultCode == RESULT_OK) {
	         try {
	            JSONObject jo = new JSONObject(purchaseData);
	            String sku = jo.getString("productId");
	            Date date = new Date();
	            if(sku.equals(SKU_YEAR)){
	            	SaveUtils.setEndPDate(date.getTime() + 367*DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
	            }
	            if(sku.equals(SKU_MUNTH)){
	            	SaveUtils.setEndPDate(date.getTime() + 32*DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
	            }
	            if (sku.equals(SKU_YEAR_NEW)) {
					SaveUtils.setEndPDate(date.getTime() + 367
							* DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				}
	            if (sku.equals(SKU_YEAR_VIP)) {
					SaveUtils.setEndPDate(date.getTime() + 367
							* DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				} 
	            if (sku.equals(SKU_HALFYEAR)) {
					SaveUtils.setEndPDate(date.getTime() + 190
							* DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				}          
	            if (sku.equals(SKU_MUUNTH_NEW)) {
					SaveUtils.setEndPDate(date.getTime() + 32
							* DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				}
				 if (sku.equals(SKU_YEAR_2017)) {
					 SaveUtils.setEndPDate(date.getTime() + 367
							 * DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				 }
				 if (sku.equals(SKU_HALFYEAR_2017)) {
					 SaveUtils.setEndPDate(date.getTime() + 190
							 * DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				 }
				 if (sku.equals(SKU_MUUNTH_2017)) {
					 SaveUtils.setEndPDate(date.getTime() + 32
							 * DateUtils.DAY_IN_MILLIS, PaymentsListActivity.this);
				 }
	            //alert("You have bought the " + sku + ". Excellent choice, adventurer!");
	          }
	          catch (JSONException e) {

	           //  alert("Failed to parse purchase data.");
	             e.printStackTrace();
	          }
	      }
	   }
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		Button buttonOk;
		switch (id) {
		case DIALOG_EMAIL:
			// graph with custom labels and drawBackground
			dialog = new Dialog(this.getParent().getParent());

			dialog.setContentView(R.layout.user_email_dialog);
			dialog.setTitle(R.string.user_abonement_title);

			TextView label = (TextView) dialog.findViewById(R.id.textViewLabel);
			label.setText(R.string.user_abonement_label);
			TextView idView2 = (TextView) dialog.findViewById(R.id.textViewId);

			idView2.setText("id"
					+ SaveUtils.getUserAdvId(PaymentsListActivity.this));

			userNameET = (EditText) dialog.findViewById(R.id.editTextUserEmail);
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					if (userNameET.getText().toString().length() < 1) {
						userNameET.setBackgroundColor(Color.RED);
					} else {
						dialog.cancel();
						try {
							new UpdatePaymentTask(userNameET.getText()
									.toString().trim()).execute();
						} catch (Exception e) {
							e.printStackTrace();
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

}
