package bulat.diet.helper_sport.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class AddDishActivity extends BaseActivity{

	
	private Spinner typeSpinner;
	private Spinner balanceTypeSpinner;
	private Button okbutton;
	EditText nameView;
	EditText caloricityView;
	private Button nobutton;
	private int flag_add = 0;
	int category;
	
	public static final String DISH_NAME = "name";
	public static final String DISH_TYPE = "type";
	public static final String DISH_CALORICITY = "caloricity";
	public static final String ID = "id";
	public static final String ADD = "add_dish";
	protected static final String DISH_BALANCETYPE = "DISH_BALANCETYPE";
	
	InputMethodManager imm;
	private String id;
	private int typeKey;
	protected String type;
	private EditText fatView;
	private EditText carbonView;
	private EditText proteinView;
	private LinearLayout balanseLayout;
	private String balanceTypeKey = "m";
	private EditText dishBarView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		final View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.add_dish, null);
		balanseLayout = (LinearLayout)viewToLoad.findViewById(R.id.typeLayout);
		balanceTypeSpinner = (Spinner) viewToLoad.findViewById(R.id.balance_type_spinner);
		Bundle extras = getIntent().getExtras();
		nameView = (EditText)viewToLoad.findViewById(R.id.editTextDishName);
		caloricityView = (EditText)viewToLoad.findViewById(R.id.editTextDishCaloricity);
		fatView = (EditText)viewToLoad.findViewById(R.id.editTextFat);
		carbonView = (EditText)viewToLoad.findViewById(R.id.editTextCarbon);
		proteinView = (EditText)viewToLoad.findViewById(R.id.editTextProtein);
		dishBarView = (EditText)viewToLoad.findViewById(R.id.dihsBar);
		String title = "";
		if(extras!=null){
			typeKey = extras.getInt(DISH_TYPE, 0);
			balanceTypeKey  = extras.getString(DISH_BALANCETYPE);
			flag_add = extras.getInt(ADD);
			String name = extras.getString(AddTodayDishActivity.DISH_NAME);
			nameView.setText(name);	
			String caloricity = String.valueOf(extras.getInt(AddTodayDishActivity.DISH_CALORISITY));
			String fat = extras.getString(AddTodayDishActivity.DISH_FAT);
			String barcode = extras.getString(AddTodayDishActivity.BARCODE);
			dishBarView.setText(barcode);
			String carbon = extras.getString(AddTodayDishActivity.DISH_CARBON);
			String protein = extras.getString(AddTodayDishActivity.DISH_PROTEIN);
			caloricityView.setText(caloricity);
			fatView.setText(fat);
			carbonView.setText(carbon);
			proteinView.setText(protein);
			id = extras.getString(ID);
			title = extras.getString(AddTodayDishActivity.TITLE);
		}
		TextView header = (TextView) viewToLoad
				.findViewById(R.id.textViewTitle);
		
		header.setText(title);
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				try{
					DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
					activityStack.getFirst();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}			
		});	
		
		okbutton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		
		nameView.setOnEditorActionListener(onEditListener);
		caloricityView.setOnEditorActionListener(onEditListener);
		fatView.setOnEditorActionListener(onEditListener);
		carbonView.setOnEditorActionListener(onEditListener);
		proteinView.setOnEditorActionListener(onEditListener);
		okbutton.setOnClickListener(new OnClickListener() {
		

		

			public void onClick(View v) {
				if(!"".endsWith(nameView.getText().toString()) &&  !"".equals(caloricityView.getText().toString())&&  !"".equals(fatView.getText().toString())&&  !"".equals(carbonView.getText().toString())&&  !"".equals(proteinView.getText().toString())){
					if(flag_add == 1){
						addString = nameView.getText().toString();
						calorycity = caloricityView.getText().toString();
						fat = fatView.getText().toString();
						carbon = carbonView.getText().toString();
						protein = proteinView.getText().toString();
						barcode = dishBarView.getText().toString();
						
						category =  ((DishType)typeSpinner.getSelectedItem()).getTypeKey();
						type = DishListHelper.getDishesTypeByCategory(AddDishActivity.this,((DishType)typeSpinner.getSelectedItem()).getTypeKey());
						DishListHelper.addNewDish(
								new Dish(nameView.getText().toString(),
										nameView.getText().toString(),
										Integer.valueOf(calorycity),
										((DishType)typeSpinner.getSelectedItem()).getTypeKey(),
										0,
										0,
										type,
										fatView.getText().toString().replace(',', '.'),
										carbonView.getText().toString().replace(',', '.'),
										proteinView.getText().toString().replace(',', '.'),
										((DishType)typeSpinner.getSelectedItem()).getDescription(), barcode),
								AddDishActivity.this);
						
						if(category != 0){
							new AddTask().execute();
						}
					
					}else{
						if(id!=null){
							barcode = dishBarView.getText().toString();
							DishListHelper.updateDish( 
									new Dish(id, 
											nameView.getText().toString(),
											"",
											Integer.valueOf(caloricityView.getText().toString()),
											((DishType)typeSpinner.getSelectedItem()).getTypeKey(),
											fatView.getText().toString(),
											carbonView.getText().toString(),
											proteinView.getText().toString(),
											((DishType)typeSpinner.getSelectedItem()).getDescription(), barcode),

									AddDishActivity.this);
							
						}
					}				
					try{
						if(getParent() instanceof DishListActivityGroup ){
							DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
							activityStack.getFirst();
						}
						if(getParent() instanceof DishActivityGroup ){
							DishActivityGroup activityStack = (DishActivityGroup) getParent();
							activityStack.pop();
						}
						
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					nameView.setBackgroundColor(Color.RED);
					caloricityView.setBackgroundColor(Color.RED);
					fatView.setBackgroundColor(Color.RED);
					carbonView.setBackgroundColor(Color.RED);
					proteinView.setBackgroundColor(Color.RED);
				}
			}
		});	
	
		setContentView(viewToLoad);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		imm.hideSoftInputFromWindow(nameView.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(caloricityView.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(fatView.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(carbonView.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(proteinView.getWindowToken(), 0);
	}

	@Override
	protected void onResume() {		
		super.onResume();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		imm = (InputMethodManager)AddDishActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		typeSpinner = (Spinner) findViewById(R.id.dihs_type_spinner);
		
		addString = "";
		calorycity = "";
		fat = "";
		carbon= "";
		protein = "";
		ArrayList<DishType> types = new ArrayList<DishType>();

		if(id==null){
			types.addAll(DishListHelper.getUsingDishCategories(this));	
		}else{
			types.addAll(DishListHelper.getAllDishCategories(this));
		}

		ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this, android.R.layout.simple_spinner_item, types);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(adapter);
		try{
			if((types.size()-1) < typeKey){
				typeSpinner.setSelection(0);
			}else{
				typeSpinner.setSelection(typeKey);
			}
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
		balanseLayout.setVisibility(View.GONE);
		typeSpinner.setOnItemSelectedListener(spinnerListener);
		
		DishType[] balanceTypes = {new DishType(0, getString(R.string.Meat)),new DishType(1, getString(R.string.Diery)),new DishType(2, getString(R.string.Sweet)),new DishType(3, getString(R.string.Bakery)),new DishType(4, getString(R.string.Fruit))};
		ArrayAdapter<DishType> adapterBalance = new ArrayAdapter<DishType>(this, android.R.layout.simple_spinner_item, balanceTypes);
		
		adapterBalance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		balanceTypeSpinner.setAdapter(adapterBalance);
		
		balanceTypeSpinner.setOnItemSelectedListener(spinnerListener);
		if("m".equals(balanceTypeKey)){
			balanceTypeSpinner.setSelection(0);
		}else if("d".equals(balanceTypeKey)){
			balanceTypeSpinner.setSelection(1);
		}else if("s".equals(balanceTypeKey)){
			balanceTypeSpinner.setSelection(2);
		}else if("b".equals(balanceTypeKey)){
			balanceTypeSpinner.setSelection(3);
		}else {
			balanceTypeSpinner.setSelection(4);
		}

		
	}	
	
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener(){

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			 ((DishType)typeSpinner.getSelectedItem()).getTypeKey();
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}		
	};
	private OnEditorActionListener onEditListener = new OnEditorActionListener(){
		
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			//dishCaloricityVTW.setText(String.valueOf(dc*Integer.valueOf(weightView.getText().toString())/100));
			return false;
		}
	};
	
	public String addString;
	public String calorycity;
	private String fat;
	private String carbon;
	private String protein;
	private String barcode;
	private class AddTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext())) {
				HttpClient client = new DefaultHttpClient();
				try {
					addString = URLEncoder.encode(addString,"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				
				category =  ((DishType)typeSpinner.getSelectedItem()).getTypeKey();
				HttpGet httpGet ;
				if(barcode.length()<5){
					httpGet = new HttpGet(
						Constants.URL_DISHBASE + "?dish=" + addString + "&calory=" + calorycity + "&category=" + category + "&type=" + type + "&fat=" + fat+ "&carbon=" + carbon+ "&protein=" + protein+ "&lang=" + SaveUtils.getLang(AddDishActivity.this));
				}else{
					httpGet = new HttpGet(
							Constants.URL_BARCOD + "?dish=" + addString + "&calory=" + calorycity + "&category=" + category + "&type=" + type + "&fat=" + fat+ "&carbon=" + carbon+ "&protein=" + protein+ "&lang=" + SaveUtils.getLang(AddDishActivity.this)+ "&barcode=" + barcode);

				}
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
						
					} else {

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
		}

	}	
}
