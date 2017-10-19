package bulat.diet.helper_sport.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.StringUtils;

public class AddRecepyActivity extends BaseActivity{

	
	private Spinner typeSpinner;
	private Spinner balanceTypeSpinner;
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
	
	InputMethodManager imm;
	private String id;
	private int typeKey;
	protected String type;
	
	private LinearLayout balanseLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		final View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.add_recepy, null);
		balanseLayout = (LinearLayout)viewToLoad.findViewById(R.id.typeLayout);
		balanceTypeSpinner = (Spinner) viewToLoad.findViewById(R.id.balance_type_spinner);
		Bundle extras = getIntent().getExtras();
		nameView = (EditText)viewToLoad.findViewById(R.id.editTextDishName);
		//caloricityView = (EditText)viewToLoad.findViewById(R.id.editTextDishCaloricity);
		//fatView = (EditText)viewToLoad.findViewById(R.id.editTextFat);
		//carbonView = (EditText)viewToLoad.findViewById(R.id.editTextCarbon);
		//proteinView = (EditText)viewToLoad.findViewById(R.id.editTextProtein);
		String title = "";
		if(extras!=null){
			typeKey = extras.getInt(DISH_TYPE, 0);
			flag_add = extras.getInt(ADD);
			String name = extras.getString(AddTodayDishActivity.DISH_NAME);
			nameView.setText(name);	
			String caloricity = String.valueOf(extras.getInt(AddTodayDishActivity.DISH_CALORISITY));
			String fat = extras.getString(AddTodayDishActivity.DISH_FAT);
			String carbon = extras.getString(AddTodayDishActivity.DISH_CARBON);
			String protein = extras.getString(AddTodayDishActivity.DISH_PROTEIN);			
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
					activityStack.pop();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}			
		});	
		
		okbutton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		
		nameView.setOnEditorActionListener(onEditListener);
		okbutton.setOnClickListener(new OnClickListener() {
		

		

			public void onClick(View v) {
				if(!"".endsWith(nameView.getText().toString())){
					if(flag_add == 1){
						addString = nameView.getText().toString();
						calorycity = "0";
						fat = "0";
						carbon = "0";
						protein = "0";
						
						category =  ((DishType)typeSpinner.getSelectedItem()).getTypeKey();
						type = DishListHelper.getDishesTypeByCategory(AddRecepyActivity.this,((DishType)typeSpinner.getSelectedItem()).getTypeKey());						
						id = String.valueOf(DishListHelper.addNewDish(
								new Dish(nameView.getText().toString(),
										DishProvider.ISRECEPY + "_0",
										Integer.valueOf(calorycity),
										((DishType)typeSpinner.getSelectedItem()).getTypeKey(),
										0,
										0,
										type,
										fat,
										carbon,
										protein,
										((DishType)typeSpinner.getSelectedItem()).getDescription()),
								AddRecepyActivity.this));
						String ids = SaveUtils.readString(DishListActivity.POPULAR, "-1", AddRecepyActivity.this);
						TreeSet<String> newIds = new TreeSet<String>(Arrays.asList(ids.split(",")));
						newIds.add(id);
						String [] idsUpdated = newIds.toArray(new String[newIds.size()]);
						SaveUtils.writeString(DishListActivity.POPULAR, StringUtils.toSingleString(idsUpdated), AddRecepyActivity.this);
					}									
					try{
						if(getParent() instanceof DishListActivityGroup ){
							//DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
							//activityStack.getFirst();
							Intent intent = new Intent();
							intent.putExtra(RecepyActivity.NAME, nameView.getText().toString());
							if(id!=null){
								intent.putExtra(RecepyActivity.ID, id);
							}
							intent.setClass(getParent(), RecepyActivity.class);							
							DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
							activityStack.push("RecepyActivity", intent);

						}
						if(getParent() instanceof DishActivityGroup ){
							//DishActivityGroup activityStack = (DishActivityGroup) getParent();
							//activityStack.pop();
							Intent intent = new Intent();
							intent.putExtra(RecepyActivity.NAME, nameView.getText().toString());
							if(id!=null){
								intent.putExtra(RecepyActivity.ID, id);
							}
							intent.setClass(getParent(), RecepyActivity.class);							
						    DishActivityGroup activityStack = (DishActivityGroup) getParent();
							activityStack.push("DishListActivity", intent);
						}
						
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					nameView.setBackgroundColor(Color.RED);					
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
	}

	@Override
	protected void onResume() {		
		super.onResume();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		imm = (InputMethodManager)AddRecepyActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		typeSpinner = (Spinner) findViewById(R.id.dihs_type_spinner);
		
		addString = "";
		calorycity = "";
		fat = "";
		carbon= "";
		protein = "";
		ArrayList<DishType> types = new ArrayList<DishType>();
		/*if ( DishListHelper.getPopularDishesCount(this)>7){
			if(typeKey>0){
				--typeKey;
			}
		}		*/
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
		if(((DishType)typeSpinner.getSelectedItem()).getTypeKey()>100){
			balanseLayout.setVisibility(View.VISIBLE);
		typeSpinner.setOnItemSelectedListener(spinnerListener);
		
		DishType[] balanceTypes = {new DishType(0, getString(R.string.Meat)),new DishType(1, getString(R.string.Diery)),new DishType(2, getString(R.string.Sweet)),new DishType(3, getString(R.string.Bakery)),new DishType(4, getString(R.string.Fruit))};
		ArrayAdapter<DishType> adapterBalance = new ArrayAdapter<DishType>(this, android.R.layout.simple_spinner_item, balanceTypes);
		
		adapterBalance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		balanceTypeSpinner.setAdapter(adapterBalance);
		
		balanceTypeSpinner.setOnItemSelectedListener(spinnerListener);
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
	
}
