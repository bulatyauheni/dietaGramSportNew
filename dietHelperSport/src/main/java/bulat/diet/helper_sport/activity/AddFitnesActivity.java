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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.SportListHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class AddFitnesActivity extends BaseActivity{

	
	private Button okbutton;
	EditText nameView;
	private Button nobutton;
	private int flag_add = 0;
	int category;
	
	public static final String DISH_NAME = "name";
	public static final String DISH_TYPE = "type";
	public static final String DISH_CALORICITY = "caloricity";
	public static final String ID = "id";
	public static final String ADD = "add_dish";
	public static final String FITNES_WEY = "FITNES_WEY";
	public static final String FITNES_WEIGHT = "FITNES_WEIGHT";
	public static final String FITNES_COUNT_SELECTION = "FITNES_COUNT_SELECTION";
	public static final String FITNES_WEIGHT_SELECTION = "FITNES_WEIGHT_SELECTION";
	public static final String FITNES_WEIGHTDEC_SELECTION = "FITNES_WEIGHTDEC_SELECTION";
	protected static final String TYPE = "TYPE";
	protected static final String TYPE_KEY = "TYPE_KEY";
	
	InputMethodManager imm;
	private String id;
	private int typeKey;
	protected String type;
	private EditText proteinView;
	private TextView typeView;
	private String typeName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		final View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.add_fitnes, null);
		Bundle extras = getIntent().getExtras();
		
		typeView = (TextView)viewToLoad.findViewById(R.id.textViewTypeName);
		nameView = (EditText)viewToLoad.findViewById(R.id.editTextDishName);		
		proteinView = (EditText)viewToLoad.findViewById(R.id.editTextFitnesCaloricity);
		TextView label = (TextView)viewToLoad.findViewById(R.id.textViewDishCaloricity);
		String title = "";
		if(extras!=null){
			typeKey = extras.getInt(TYPE_KEY, 0);
			flag_add = extras.getInt(ADD);
			typeName = extras.getString(TYPE);
			typeView.setText(typeName);
			String name = extras.getString(AddTodayDishActivity.DISH_NAME);
			nameView.setText(name);	
			String protein = extras.getString(AddTodayDishActivity.DISH_PROTEIN);	
			String way = extras.getString(FITNES_WEY);
			String weight = extras.getString(FITNES_WEIGHT);
			if(way!=null){
				if(!"0".equals(way)||!"0".equals(weight)){
					proteinView.setVisibility(View.GONE);
					label.setVisibility(View.GONE);
				}
			}
			proteinView.setText(protein);
			id = extras.getString(ID);
			title = extras.getString(AddTodayDishActivity.TITLE);
		}
		//TextView header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);
		
		//header.setText(title);
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				try{
					DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
					activityStack.pop();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}			
		});	
		
		okbutton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		
		nameView.setOnEditorActionListener(onEditListener);
		proteinView.setOnEditorActionListener(onEditListener);
		okbutton.setOnClickListener(new OnClickListener() {
		

		

			public void onClick(View v) {
				String desk = nameView.getText().toString().toLowerCase();
				if(!"".equals(nameView.getText().toString()) && !"".equals(proteinView.getText().toString())){
					if(flag_add == 1){
						addString = nameView.getText().toString();
						protein = proteinView.getText().toString();
																		
						SportListHelper.addNewSport(
								new Dish(nameView.getText().toString(),
										desk,
										0,
										typeKey,
										0,
										0,
										type,
										"0",
										"0",
										String.valueOf(Float.valueOf(proteinView.getText().toString().replace(',', '.'))/(60*(int)SaveUtils.getRealWeight(AddFitnesActivity.this)))),
								AddFitnesActivity.this);

						if(category != 0){
							new AddTask().execute();
						}
					
					}else{
						if(id!=null){
							SportListHelper.updateSport( 
									new Dish(id, 
											nameView.getText().toString(),
											desk,
											0,
											typeKey,
											"0",
											"0",
											String.valueOf(Float.valueOf(proteinView.getText().toString().replace(',', '.'))/(60*(int)SaveUtils.getRealWeight(AddFitnesActivity.this))), ""),
									AddFitnesActivity.this);
							
						}
					}				
					try{
						DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
						activityStack.pop();
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					nameView.setBackgroundColor(Color.RED);					
					proteinView.setBackgroundColor(Color.RED);
				}
			}
		});	
		nobutton = (Button) viewToLoad.findViewById(R.id.buttonNo);
		nobutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				
					try{
						DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
						activityStack.getFirst();
					}catch (Exception e) {
						e.printStackTrace();
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
		imm.hideSoftInputFromWindow(proteinView.getWindowToken(), 0);
	}

	@Override
	protected void onResume() {		
		super.onResume();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		imm = (InputMethodManager)AddFitnesActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		
		addString = "";
		calorycity = "";
		fat = "";
		carbon= "";
		protein = "";
		
		ArrayList<DishType> types = DishListHelper.getAllDishCategories(this);
		if(id==null){
			types = DishListHelper.getUnpopularDishCategories(this);	
		}

		
		
	}
	
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
				
				
				HttpGet httpGet = new HttpGet(
						Constants.URL_DISHBASE + "?dish=" + addString + "&calory=" + calorycity + "&category=" + category + "&type=" + type+ "&lang=" + SaveUtils.getLang(AddFitnesActivity.this));
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
