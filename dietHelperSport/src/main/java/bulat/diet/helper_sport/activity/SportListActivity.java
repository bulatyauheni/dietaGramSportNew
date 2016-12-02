package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.SportAdapter;
import bulat.diet.helper_sport.adapter.SportArrayAdapter;
import bulat.diet.helper_sport.db.SportListHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class SportListActivity extends Activity{

	private static final String URL = Constants.URL_SPORTBASE;
	protected String selectedInemId;
	ListView dishesList;
	String currDate;
	Spinner typeSpinner;
	String id = null;
	TextView header;
	Cursor c;
	DishListActivityGroup parent = null;
	private String parentName;
	private EditText searchEditText;
	private String searchString;
	private Button clearButton;
	private LinearLayout spinerLayot;
	private InputMethodManager imm;
	ArrayList<Dish> list = new ArrayList<Dish>();
	View viewToLoad;
	private RelativeLayout serchLayout;
	private TextView loadingView;
	private TextView badSearchView;
	private LinearLayout emptyLayout;
	private LinearLayout tabLayout;
	private boolean templateFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.fitnes_list, null);
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);
		spinerLayot = (LinearLayout) viewToLoad.findViewById(R.id.spinerLayout);
		serchLayout = (RelativeLayout) viewToLoad
				.findViewById(R.id.searchLayout);
		tabLayout = (LinearLayout) viewToLoad
				.findViewById(R.id.tabLayout);
		emptyLayout = (LinearLayout) viewToLoad.findViewById(R.id.emptyLayout);
		loadingView = (TextView) viewToLoad.findViewById(R.id.textViewLoading);
		badSearchView = (TextView) viewToLoad
				.findViewById(R.id.textViewBadSearch);
		searchEditText = (EditText) viewToLoad
				.findViewById(R.id.editTextSearch);
		searchEditText.setOnEditorActionListener(searchEditorListener);
		searchEditText.addTextChangedListener(searchEditTextWatcher);
		clearButton = (Button) viewToLoad.findViewById(R.id.buttonClear);
		clearButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				searchEditText.setText("");
			}
		});
		Bundle extras = getIntent().getExtras();
		parentName = null;
		if (extras != null) {
			currDate = extras.getString(DishActivity.DATE);
			id = extras.getString(AddTodayDishActivity.ID);
			parentName = extras.getString(DishActivity.PARENT_NAME);
			templateFlag = extras.getBoolean(NewTemplateActivity.TEMPLATE);
			
		}
		if (parentName == null) {
			if (DishListActivityGroup.class.getName().equals(
					getParent().getClass().getName())) {
				try {
					parent = (DishListActivityGroup) getParent();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		Button dishTab= (Button) viewToLoad.findViewById(R.id.dishTab);
		dishTab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();					
					intent.setClass(getParent(), DishListActivity.class);					
					DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
					activityStack.push("DishListActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Button recepyTab= (Button) viewToLoad.findViewById(R.id.myDishTab);
		recepyTab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();					
					intent.setClass(getParent(), RecepyListActivity.class);					
					DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
					activityStack.push("recepyListActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		
		Button addButton = (Button) viewToLoad.findViewById(R.id.buttonAdd);
		if (parent != null) {
			addButton.setVisibility(View.VISIBLE);
			tabLayout.setVisibility(View.VISIBLE);
		} else {
			backButton.setVisibility(View.VISIBLE);

			serchLayout.setVisibility(View.VISIBLE);
		}
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

		addButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				try {
					intent.putExtra(AddTodayDishActivity.ADD, 1);
					intent.putExtra(AddFitnesActivity.TYPE, typeSpinner.getSelectedItem().toString());
					intent.putExtra(AddFitnesActivity.TYPE_KEY, ((DishType)typeSpinner.getSelectedItem()).getTypeKey());
					intent.setClass(getParent(), AddFitnesActivity.class);
					intent.putExtra(AddTodayDishActivity.TITLE,
							getString(R.string.add_fitnes));

					DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
					activityStack.push("AddFitnesActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		setContentView(viewToLoad);
	}

	public void showingDialogCancel(String id) {
		selectedInemId = id;
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setMessage(R.string.remove_dialog)
				.setPositiveButton(getString(R.string.yes), dialogClickListener)
				.setNegativeButton(getString(R.string.no), dialogClickListener)
				.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (c != null)
			c.close();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (c != null)
			c.close();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		setSpiner();		
		imm = (InputMethodManager) SportListActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		//String header_text = getString(R.string.dish_list);

		//header.setText(header_text);
		if (typeSpinner.getSelectedItem() != null) {
			c = SportListHelper.getFitnesByCategory(getApplicationContext(), 
					((DishType) typeSpinner.getSelectedItem()).getTypeKey());
			if (c != null) {
				try {
					SportAdapter da = new SportAdapter(this,
							getApplicationContext(), c, parent);
					dishesList = (ListView) findViewById(R.id.listViewDishes);
					dishesList.setAdapter(da);
				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					c.close();
				}
			}
			dishesList.setOnItemClickListener(dishesListOnItemClickListener);
		}					
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private OnItemClickListener dishesListOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			Dish temp = null;
			Intent intent = new Intent();
			TextView name = (TextView) v.findViewById(R.id.textViewDishName);
			TextView idView = (TextView) v.findViewById(R.id.textViewId);
			
			TextView wayTextView = (TextView) v
					.findViewById(R.id.textViewWayHide);
			TextView weightTextView = (TextView) v
					.findViewById(R.id.textViewWeightHide);
			TextView textViewConsumption = (TextView) v
					.findViewById(R.id.textViewCons);
			TextView typeView = (TextView) v
					.findViewById(R.id.textViewDishType);

			intent.putExtra(AddTodayDishActivity.DISH_NAME, name.getText().toString());
			// adding in local db
			if ("0".equals(idView.getText())) {
				temp = (Dish) dishesList.getAdapter().getItem(arg2);
				temp.setId(String.valueOf(SportListHelper.addNewSport(temp,
						SportListActivity.this)));
			}
			if (id != null) {
				intent.putExtra(AddTodayDishActivity.TITLE,
						getString(R.string.edit_today_fitnes));
				intent.putExtra(AddTodayDishActivity.ID, id);
			} else {
				intent.putExtra(AddTodayDishActivity.TITLE,
						getString(R.string.add_today_fitnes));
				intent.putExtra(AddTodayDishActivity.ADD, 1);
			}	
			if(templateFlag){
				intent.putExtra(NewTemplateActivity.TEMPLATE, true);
			}
			intent.putExtra(AddTodayDishActivity.DISH_PROTEIN,
					textViewConsumption.getText().toString());	
			intent.putExtra(AddFitnesActivity.FITNES_WEY,
					wayTextView.getText().toString());
			intent.putExtra(AddFitnesActivity.TYPE_KEY, ((DishType)typeSpinner.getSelectedItem()).getTypeKey());
			intent.putExtra(AddFitnesActivity.FITNES_WEIGHT,
					weightTextView.getText().toString());
			intent.putExtra(AddTodayDishActivity.DISH_STAT_TYPE,
					typeView.getText().toString());
			intent.putExtra(DishActivity.DATE, currDate);
			intent.putExtra(DishActivity.PARENT_NAME, parentName);
			
			if (CalendarActivityGroup.class.getName().equals(
					getParent().getClass().getName())) {
				intent.putExtra(AddTodayFitnesActivity.CALORYBURN,
						textViewConsumption.getText().toString());	
				intent.setClass(getParent(), AddTodayFitnesActivity.class);
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("AddTodayFitnesActivityCalendar", intent);
			} else if (DishActivityGroup.class.getName().equals(
					getParent().getClass().getName())) {
				try {
					/*if (!"0".equals(idView.getText().toString())) {
						DishListHelper.incDishPopularity(idView.getText()
								.toString(), DishListActivity.this);
					} else {
						if (temp != null) {
							DishListHelper.incDishPopularity(temp.getId(),
									DishListActivity.this);
						}
					}*/
					intent.putExtra(AddTodayFitnesActivity.CALORYBURN,
							textViewConsumption.getText().toString());	
					intent.setClass(getParent(), AddTodayFitnesActivity.class);
					DishActivityGroup activityStack = (DishActivityGroup) getParent();
					activityStack.push("AddTodayFitnesActivityDish", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (DishListActivityGroup.class.getName().equals(
					getParent().getClass().getName())) {
				try {

					intent.putExtra(AddTodayDishActivity.ID, idView.getText()
							.toString());
					intent.putExtra(AddTodayDishActivity.ADD, 0);
					intent.putExtra(AddTodayDishActivity.TITLE,
							getString(R.string.edit_dish));					
					intent.putExtra(AddTodayFitnesActivity.CALORYBURN,
							textViewConsumption.getText().toString());				
					intent.setClass(getParent(), AddFitnesActivity.class);
					DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
					activityStack.push("AddFitnesActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	};

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					SportListHelper.removeDish(selectedInemId,
							SportListActivity.this);
					selectedInemId = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				selectedInemId = null;
				break;
			}
		}
	};

	private OnEditorActionListener searchEditorListener = new OnEditorActionListener() {

		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				searchString = searchEditText.getText().toString().trim();
				new SetFoundTask().execute();

				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return true;
			}
			return false;
		}

	};
	protected int couter_2;
	protected Object lastSearchString;
	private TextWatcher searchEditTextWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			searchString = searchEditText.getText().toString().trim();
			if (!searchString.equals(lastSearchString)) {
				lastSearchString = searchString;
				if (searchString.length() == 0) {
					emptyLayout.setVisibility(View.GONE);
					badSearchView.setVisibility(View.GONE);
					loadingView.setVisibility(View.GONE);
					spinerLayot.setVisibility(View.VISIBLE);					
					clearButton.setVisibility(View.INVISIBLE);
					clear();
				} else {
					spinerLayot.setVisibility(View.GONE);
					clearButton.setVisibility(View.VISIBLE);
				}
				if (searchString.length() >= 3
						&& searchString.length() % 2 == 1) {
					new SetFoundTask().execute();
					couter_2++;
				}
			}
		}
	};
	public int count;

	private void clear() {
		searchString = "";
	}

	private class SetFoundTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			emptyLayout.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String searchString2 = searchString;
			try {
				searchString=URLEncoder.encode(searchString,"UTF-8");
				 
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			list = SportListHelper.searchInAll(searchString2, getParent());
			Set<Dish> unicDish = new TreeSet<Dish>();
			unicDish.addAll(list);
			
			if (NetworkState.isOnline(getApplicationContext())) {
				count++;

				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				searchString = searchString.replaceAll(" ", "%20");
				HttpGet httpGet = new HttpGet(
						URL + "?name=" + searchString+ "&lang=" + SaveUtils.getLang(SportListActivity.this));
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				int timeoutConnection = 3000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
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
							unicDish.add(new Dish(jsonObject.getString("name"),
									jsonObject.getString("name"), Integer
											.parseInt(jsonObject
													.getString("caloric")),
									Integer.parseInt(jsonObject
											.getString("category_id")), 0, 0,
									jsonObject.getString("type")));
						}
						list = new ArrayList<Dish>(unicDish);
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

			Toast.makeText(SportListActivity.this, getString(R.string.disonect),
					Toast.LENGTH_LONG).show();
		}
		emptyLayout.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		if (list.size() > 0) {
			badSearchView.setVisibility(View.GONE);
			try {
				SportArrayAdapter da = new SportArrayAdapter(getApplicationContext(),
						R.layout.fitnes_list_row, list);
				dishesList.setAdapter(da);
				da.notifyDataSetChanged();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			emptyLayout.setVisibility(View.VISIBLE);
			badSearchView.setVisibility(View.VISIBLE);
		}
		
	}
	private void setSpiner() {
		// TODO Auto-generated method stub
		typeSpinner = (Spinner) findViewById(R.id.dihs_type_spinner);
		ArrayList<DishType> types = new ArrayList<DishType>();
		if ( SportListHelper.getPopularFitnesCount(this)>5){
			types.add(new DishType(1000, getString(R.string.popular)));
		}
		
		types.addAll(SportListHelper.getAllFitnesCategories(this));		


		ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
				android.R.layout.simple_spinner_item, types);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(adapter);
		typeSpinner.setSelection(0);
		typeSpinner.setOnItemSelectedListener(spinnerListener);
	}
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			c.close();
			c = SportListHelper.getFitnesByCategory(getApplicationContext(),
					((DishType) typeSpinner.getSelectedItem()).getTypeKey());
			if (c != null) {
				try {
					SportAdapter da = new SportAdapter(SportListActivity.this,
							getApplicationContext(), c, parent);
					// dishesList = (ListView)
					// viewToLoad.findViewById(R.id.listViewDishes);
					dishesList.setAdapter(da);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

}
