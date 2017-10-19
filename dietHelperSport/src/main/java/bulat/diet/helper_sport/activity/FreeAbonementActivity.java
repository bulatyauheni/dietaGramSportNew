package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.github.mikephil.charting.charts.PieChart;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.PricesAdapter;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.IabHelper;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FreeAbonementActivity extends StatisticFCPActivity {

	private LinearLayout freeBlock;
	private LinearLayout payBlock;
	private LinearLayout infoBlock;
	private int selectedInemId;
	String TAG = "FreeAbonementActivity";
	private Button buttonGetFree;
	private Button buttonBay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.free_abonement,
				null);
		setContentView(viewToLoad);
		mParties = new String[] { getString(R.string.protein),
				getString(R.string.carbon), getString(R.string.fat) };
		warning = (TextView) viewToLoad.findViewById(R.id.warning);

		infoBlock = (LinearLayout) viewToLoad.findViewById(R.id.info_block);
		freeBlock = (LinearLayout) viewToLoad.findViewById(R.id.free_block);
		payBlock = (LinearLayout) viewToLoad.findViewById(R.id.pay_block);

		buttonGetFree = (Button) viewToLoad.findViewById(R.id.buttonGetFree);
		buttonGetFree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				infoBlock.setVisibility(View.GONE);
				freeBlock.setVisibility(View.VISIBLE);
			}
		});
		buttonBay = (Button) viewToLoad.findViewById(R.id.buttonBay);
		buttonBay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				infoBlock.setVisibility(View.GONE);
				payBlock.setVisibility(View.VISIBLE);
			}
		});
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
				
		initDietTypeSpinner();
		chartsLayout = (LinearLayout) 	findViewById (R.id.chartsLayout);

		ImageButton vkButton = (ImageButton) findViewById(R.id.buttonVKChart);
		vkButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {						
				Intent i = new Intent(getApplicationContext(), VkActivity.class);
				i.putExtra(VkActivity.IMAGE_PATH, getBitmapFromView(chartsLayout));
				i.putExtra(VkActivity.IMAGE_DESK, successInPercentageTV.getText().toString());
				startActivityForResult(i, 1);

			}
		});
		if (!getString(R.string.locale).equals("ru")){
			vkButton.setVisibility(View.GONE);
		}

		ImageButton fbButton = (ImageButton) findViewById(R.id.buttonFBChart);
		fbButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), SharingActivity.class);
				i.putExtra(VkActivity.IMAGE_PATH, getBitmapFromView(chartsLayout));
				i.putExtra(VkActivity.IMAGE_DESK, successInPercentageTV.getText().toString());
				startActivityForResult(i, 1);
			}
		});

		
		mChartIdeal = (PieChart) findViewById(R.id.chart1);
		initChart(mChartIdeal);
		mChartIdeal.setCenterText(getString(R.string.idealCheet));
		
		mChartClient = (PieChart) findViewById(R.id.chart2);
		initChart(mChartClient);
		mChartClient.setCenterText(getString(R.string.yourCheet));
		
		initTimeIntervalSelector();

		successInPercentageTV = (TextView) findViewById(R.id.successInPercentageTV);
		

	}
	@Override
	protected void onResume() {
		super.onResume();
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<String, String>(getString(R.string.paymentvipy) , BasePayActivity.prices.get(PaymentsListActivity.SKU_YEAR_VIP)));
        list.add(new Pair<String, String>(getString(R.string.paymenty)+ " " + String.format(getString(R.string.paymentbenefit), "70%") , BasePayActivity.prices.get(PaymentsListActivity.SKU_YEAR_2017)));
        list.add(new Pair<String, String>(getString(R.string.paymenthy)+ " " + String.format(getString(R.string.paymentbenefit), "58%"),  BasePayActivity.prices.get(PaymentsListActivity.SKU_HALFYEAR_2017)));
        list.add(new Pair<String, String>(getString(R.string.paymentm) ,  BasePayActivity.prices.get(PaymentsListActivity.SKU_MUUNTH_2017)));
		list.add(new Pair<String, String>(getString(R.string.paymentCripto),"-10%"));
        list.add(new Pair<String, String>(getString(R.string.paymentspec),""));
        list.add(new Pair<String, String>(getString(R.string.paymenterror), ""));

        ListView listView = (ListView) findViewById(R.id.listViewStatistics);
        PricesAdapter adapter = new PricesAdapter(this, R.layout.payment_row, list);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				selectedInemId = arg2;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FreeAbonementActivity.this);
				if (arg2 == 0) {

					builder.setMessage(R.string.payment_dialog_vipyear);
				} else
				if (arg2 == 1) {

					builder.setMessage(R.string.payment_dialog_year);
				} else
				if (arg2 == 2) {

					builder.setMessage(R.string.payment_dialog_halfyear);
				} else
				if (arg2 == 3) {

					builder.setMessage(R.string.payment_dialog_month);
				} else
				if (arg2 == 4) {
					GATraker.sendEvent(PaymentsListActivity.ABONEMENT, PaymentsListActivity.ABONEMENT_CRIPTO);
					CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(FreeAbonementActivity.this);
					bld.setLayout(R.layout.section_alert_dialog_two_buttons)
							.setTitle(FreeAbonementActivity.this.getString(R.string.paymentCripto))
							.setMessage(FreeAbonementActivity.this.getString(R.string.cripto_for_payment))
							.setPositiveButton(R.id.dialogButtonOk, null, new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
											"mailto","bulat.yauheni@gmail.com", null));
									emailIntent.putExtra(Intent.EXTRA_SUBJECT, FreeAbonementActivity.this.getString(R.string.app_name));
									emailIntent.putExtra(Intent.EXTRA_TEXT, "");
									FreeAbonementActivity.this.startActivity((Intent.createChooser(emailIntent, "Send email...")));
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
				if (arg2 == 5) {
					GATraker.sendEvent(PaymentsListActivity.ABONEMENT, PaymentsListActivity.ABONEMENT_FREE);
					FreeAbonementActivity.this.showDialog(PaymentsListActivity.DIALOG_EMAIL);
				} else
				if (arg2 == 6) {
					GATraker.sendEvent(PaymentsListActivity.ABONEMENT, PaymentsListActivity.PAYMENT_ERROR_REPORT);
					CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(FreeAbonementActivity.this);
					bld.setLayout(R.layout.section_alert_dialog_two_buttons)
							.setMessage(FreeAbonementActivity.this.getString(R.string.complain_for_payment))
							.setPositiveButton(R.id.dialogButtonOk, null, new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
											"mailto","bulat.yauheni@gmail.com", null));
									emailIntent.putExtra(Intent.EXTRA_SUBJECT, FreeAbonementActivity.this.getString(R.string.app_name));
									emailIntent.putExtra(Intent.EXTRA_TEXT, "");
									FreeAbonementActivity.this.startActivity((Intent.createChooser(emailIntent, "Send email...")));
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
				if (arg2 !=4 &&arg2 !=5 && arg2 != 6){
					builder.setPositiveButton(getString(R.string.yes),
							dialogClickListener)
							.setNegativeButton(getString(R.string.no),
									dialogClickListener).show();
				}

			}
		});
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
									.getUserAdvId(FreeAbonementActivity.this);

							Log.d(TAG,
									"Launching purchase flow for infinite gas subscription.");
							mHelper.launchPurchaseFlow(FreeAbonementActivity.this,
									PaymentsListActivity.SKU_YEAR_VIP, IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
									mPurchaseFinishedListener, payload);
							GATraker.sendEvent(PaymentsListActivity.ABONEMENT_VIP,
									"0".equals( SaveUtils.getSex(FreeAbonementActivity.this))? "Woman":"Man",
									"" + (SaveUtils.getAge(FreeAbonementActivity.this) + Info.MIN_AGE),
									(long) SaveUtils.getRealWeight(FreeAbonementActivity.this));
						} else if (selectedInemId == 1) {
							if (!mHelper.subscriptionsSupported()) {
								complain("Subscriptions not supported on your device yet. Sorry!");
								return;
							}

							String payload = ""
									+ SaveUtils
									.getUserAdvId(FreeAbonementActivity.this);

							Log.d(TAG,
									"Launching purchase flow for infinite gas subscription.");
							mHelper.launchPurchaseFlow(FreeAbonementActivity.this,
									PaymentsListActivity.SKU_YEAR_2017, IabHelper.ITEM_TYPE_SUBS,
									RC_REQUEST, mPurchaseFinishedListener, payload);
							GATraker.sendEvent(PaymentsListActivity.ABONEMENT_YEAR,
									"0".equals( SaveUtils.getSex(FreeAbonementActivity.this))? "Woman":"Man",
									"" + (SaveUtils.getAge(FreeAbonementActivity.this) + Info.MIN_AGE),
									(long) SaveUtils.getRealWeight(FreeAbonementActivity.this));
						} else if (selectedInemId == 2) {
							if (!mHelper.subscriptionsSupported()) {
								complain("Subscriptions not supported on your device yet. Sorry!");
								return;
							}

							String payload = ""
									+ SaveUtils
									.getUserAdvId(FreeAbonementActivity.this);

							Log.d(TAG,
									"Launching purchase flow for infinite gas subscription.");
							mHelper.launchPurchaseFlow(FreeAbonementActivity.this,
									PaymentsListActivity.SKU_HALFYEAR_2017, IabHelper.ITEM_TYPE_SUBS,
									RC_REQUEST, mPurchaseFinishedListener, payload);
							GATraker.sendEvent(PaymentsListActivity.ABONEMENT_HALFYEAR,
									"0".equals( SaveUtils.getSex(FreeAbonementActivity.this))? "Woman":"Man",
									"" + (SaveUtils.getAge(FreeAbonementActivity.this) + Info.MIN_AGE),
									(long) SaveUtils.getRealWeight(FreeAbonementActivity.this));
						} else if (selectedInemId == 3) {
							if (!mHelper.subscriptionsSupported()) {
								complain("Subscriptions not supported on your device yet. Sorry!");
								return;
							}

							String payload = ""
									+ SaveUtils
									.getUserAdvId(FreeAbonementActivity.this);

							Log.d(TAG,
									"Launching purchase flow for infinite gas subscription.");
							mHelper.launchPurchaseFlow(FreeAbonementActivity.this,
									PaymentsListActivity.SKU_MUUNTH_2017, IabHelper.ITEM_TYPE_SUBS,
									RC_REQUEST, mPurchaseFinishedListener, payload);
							GATraker.sendEvent(PaymentsListActivity.ABONEMENT_MONTH,
									"0".equals( SaveUtils.getSex(FreeAbonementActivity.this))? "Woman":"Man",
									"" + (SaveUtils.getAge(FreeAbonementActivity.this) + Info.MIN_AGE),
									(long) SaveUtils.getRealWeight(FreeAbonementActivity.this));
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
					if(sku.equals(PaymentsListActivity.SKU_YEAR)){
						SaveUtils.setEndPDate(date.getTime() + 367* DateUtils.DAY_IN_MILLIS, this);
					}
					if(sku.equals(PaymentsListActivity.SKU_MUNTH)){
						SaveUtils.setEndPDate(date.getTime() + 32*DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_YEAR_NEW)) {
						SaveUtils.setEndPDate(date.getTime() + 367
								* DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_YEAR_VIP)) {
						SaveUtils.setEndPDate(date.getTime() + 367
								* DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_HALFYEAR)) {
						SaveUtils.setEndPDate(date.getTime() + 190
								* DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_MUUNTH_NEW)) {
						SaveUtils.setEndPDate(date.getTime() + 32
								* DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_YEAR_2017)) {
						SaveUtils.setEndPDate(date.getTime() + 367
								* DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_HALFYEAR_2017)) {
						SaveUtils.setEndPDate(date.getTime() + 190
								* DateUtils.DAY_IN_MILLIS, this);
					}
					if (sku.equals(PaymentsListActivity.SKU_MUUNTH_2017)) {
						SaveUtils.setEndPDate(date.getTime() + 32
								* DateUtils.DAY_IN_MILLIS, this);
					}
					SaveUtils.setSKU(jo.toString(), this);
					//alert("You have bought the " + sku + ". Excellent choice, adventurer!");
				}
				catch (JSONException e) {

					//  alert("Failed to parse purchase data.");
					e.printStackTrace();
				}
			}
		} else {
			onBackPressed();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		Button buttonOk;
		switch (id) {
			case PaymentsListActivity.DIALOG_EMAIL:
				// graph with custom labels and drawBackground
				dialog = new Dialog(this);

				dialog.setContentView(R.layout.user_email_dialog);
				dialog.setTitle(R.string.user_abonement_title);

				TextView label = (TextView) dialog.findViewById(R.id.textViewLabel);
				label.setText(R.string.user_abonement_label);
				TextView idView2 = (TextView) dialog.findViewById(R.id.textViewId);

				idView2.setText("id"
						+ SaveUtils.getUserAdvId(FreeAbonementActivity.this));

				final EditText userNameET = (EditText) dialog.findViewById(R.id.editTextUserEmail);
				buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
				buttonOk.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						if (userNameET.getText().toString().length() < 1) {
							userNameET.setBackgroundColor(Color.RED);
						} else {
							dialog.cancel();
							try {
								new FreeAbonementActivity.UpdatePaymentTask(userNameET.getText()
										.toString().trim()).execute();
							} catch (Exception e) {
								e.printStackTrace();
							}
							onResume();
						}
					}

				});
				Button nobutton = (Button) dialog.findViewById(R.id.buttonNo);
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

	private class UpdatePaymentTask extends AsyncTask<Void, Void, Void> {
		String email = "";
		Boolean allOk = false;

		public UpdatePaymentTask(String email) {
			this.email = email;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (NetworkState.isOnline(FreeAbonementActivity.this)) {
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
						FreeAbonementActivity.this).getAccountsByType(
						"com.google");

				StringBuffer parametersb = new StringBuffer("");
				parametersb.append("?updatePurchase=" + 1);
				parametersb.append("&userEmail=" + email);
				parametersb.append("&userAdvId="
						+ SaveUtils.getUserAdvId(FreeAbonementActivity.this));
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
										FreeAbonementActivity.this);
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
												FreeAbonementActivity.this);
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
				Toast.makeText(FreeAbonementActivity.this,
						getString(R.string.user_abonement_empty),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(FreeAbonementActivity.this,
						getString(R.string.user_abonement_ok),
						Toast.LENGTH_LONG).show();
				Date cuDate = new Date();
				int daysCount = (int) ((SaveUtils
						.getEndPDate(FreeAbonementActivity.this) - cuDate
						.getTime()) / DateUtils.DAY_IN_MILLIS);
				onBackPressed();
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (infoBlock.getVisibility() == View.VISIBLE) {
			CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(FreeAbonementActivity.this);
			bld.setLayout(R.layout.section_alert_dialog_two_buttons)
					.setMessage(FreeAbonementActivity.this.getString(R.string.do_you_really_want_to_exit))
					.setPositiveButton(R.id.dialogButtonOk, null, new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								FreeAbonementActivity.this.finishAffinity();
							}
							finish();
							System.exit(0);
						}
					})
					.setPositiveButtonText(R.string.agree)
					.setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					})
					.setNegativeButtonText(R.string.disagree);
			bld.show();

		} else {
			super.onBackPressed();
		}

	}

}
