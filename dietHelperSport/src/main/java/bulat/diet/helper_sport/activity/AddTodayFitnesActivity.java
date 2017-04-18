package bulat.diet.helper_sport.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.TemplateDishHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.FitnesType;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;

public class AddTodayFitnesActivity extends BaseAddActivity implements TimePickerDialog.OnTimeSetListener{
	public static final String DISH_NAME = "dish_name";
	public static final String CALORYBURN = "CALORYBURN";
	public static final String DISH_CALORISITY = "dish_calorisity";
	public static final String DISH_CATEGORY = "dish_category";
	public static final String DISH_ABSOLUTE_CALORISITY = "dish_absolute_calorisity";
	public static final String TITLE = "title_header";
	public static final String DISH_WEIGHT = "dish_weight";
	public static final String DISH_TYPE = "dish_type";
	public static final String ID = "id";
	public static final String ADD = "add_dish";
	TextView dishCaloricityVTW;
	boolean templateFlag = false;
	EditText weightView;
	Button okbutton;
	Button nobutton;
	int dc = 0;
	String id = null;
	int flag_add = 0;
	Integer category;
	String currDate;
	InputMethodManager imm;
	private String parentName;
	
	Spinner weightSpinner;
	private String activitiName;
	private int selection;
	private String sportName;
	private String calotyBurn="0";
	private String fitnesWay;
	private String fitnesWeight;
	private LinearLayout fitnes;
	private LinearLayout gym;
	private Spinner fitWeightSpinner;
	private Spinner fitCountSpinner;
	private Spinner fitWeightDecSpinner;
	private int fitnesCountSelection;
	private int fitApproachCountSelection;
	private int fitnesWeightSelection;
	private int fitnesWeightDecSelection;
	private int weight;
	private TextView timeTW;
	private Spinner fitApproachCountSpinner;

	private void initData(Bundle extras) {

		//
		currDate = extras.getString(DishActivity.DATE);
		fitnesWay = extras.getString(AddFitnesActivity.FITNES_WEY);
		fitnesWeight = extras.getString(AddFitnesActivity.FITNES_WEIGHT);
		fitnesCountSelection = extras.getInt(AddFitnesActivity.FITNES_COUNT_SELECTION);
		fitnesWeightSelection = extras.getInt(AddFitnesActivity.FITNES_WEIGHT_SELECTION);
		fitnesWeightDecSelection = extras.getInt(AddFitnesActivity.FITNES_WEIGHTDEC_SELECTION);
		calotyBurn = extras.getString(CALORYBURN);
		sportName = extras.getString(DISH_NAME);
		parentName = extras.getString(DishActivity.PARENT_NAME);
		weight = extras.getInt(DISH_WEIGHT);
		dc = extras.getInt(DISH_ABSOLUTE_CALORISITY);
		templateFlag = extras.getBoolean(NewTemplateActivity.TEMPLATE);
		category = extras.getInt(DISH_CATEGORY);
		flag_add = extras.getInt(ADD);
		timeHHValue = Integer.getInteger(extras.getString(AddTodayDishActivity.DISH_TIME_HH));
		timeMMValue = Integer.getInteger(extras.getString(AddTodayDishActivity.DISH_TIME_MM));
		//
		parentName = extras.getString(DishActivity.PARENT_NAME);
		templateFlag = extras.getBoolean(NewTemplateActivity.TEMPLATE);
		flag_add = extras.getInt(ADD);

		//necessary field if edit dish
		id = extras.getString(ID);


		//necessary field if edit dish
		id = extras.getString(ID);

		if (id != null) {
			TodayDish dish;

			if (templateFlag) {
				dish = TemplateDishHelper.getDishById(id, this);
			} else {
				dish = TodayDishHelper.getDishById(id, this);
			}

			dc = dish.getCaloricity();
			category = Integer.valueOf(dish.getCategory());
			fitnesWeight = "" + dish.getFat();
			fitnesWay = dish.getCarbon() != 0 ? "" + dish.getCarbon() : null;
			fitnesCountSelection = (int)dish.getAbsFat();
			fitnesWeightSelection = (int)dish.getAbsCarbon();
			fitnesWeightDecSelection = (int)dish.getProtein();
			fitApproachCountSelection = (int)dish.getAbsProtein();
			calotyBurn = "0";
			sportName = dish.getName();
			weight = dish.getWeight();
			timeHHValue = dish.getDateTimeHH();
			timeMMValue = dish.getDateTimeMM();
			currDate = dish.getDate();

			activitiName = dish.getName();
		}

		Calendar now = Calendar.getInstance();
		timeHHValue = timeHHValue != null ? timeHHValue : now.getTime().getHours();
		timeMMValue = timeMMValue != null ? timeMMValue : now.getTime().getMinutes();
	}

	// public static final String DISH_NAME = "dish_name";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.add_today_fitnes, null);
		TextView header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);
		weightView = (EditText) viewToLoad.findViewById(R.id.timepicker_input_fit);
		dishCaloricityVTW = (TextView) viewToLoad.findViewById(R.id.textCaloricityValue);

		initData(getIntent().getExtras());
		activitiName = "";


		timeTW = (TextView) viewToLoad.findViewById(R.id.textViewTime);
		setTime(timeTW);
		timeTW.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				TimePickerDialog tpd = TimePickerDialog.newInstance(
						(TimePickerDialog.OnTimeSetListener) AddTodayFitnesActivity.this,
						timeHHValue,
						timeMMValue,
						true
				);

				tpd.setThemeDark(true);
				tpd.vibrate(true);
				tpd.dismissOnPause(true);
				tpd.enableSeconds(false);
				tpd.setVersion(TimePickerDialog.Version.VERSION_2 );// TimePickerDialog.Version.VERSION_1
				tpd.setAccentColor(Color.parseColor("#4981A0"));
				tpd.setTitle("TimePicker Title");
				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {

					}
				});
				tpd.show(AddTodayFitnesActivity.this.getParent().getFragmentManager(), "Datepickerdialog");
			}
		});


		//spinnerTimeMM.setOnItemSelectedListener(spinnerListener);

		if (flag_add == 0) {
			header.setText(getString(R.string.edit_today_fitnes));
		}

		TextView sportTitle = (TextView) viewToLoad
				.findViewById(R.id.sportTitle);
		sportTitle.setText(sportName);
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					((DishActivityGroup) AddTodayFitnesActivity.this
							.getParent()).getFirst();
				} catch (Exception e) {
					((CalendarActivityGroup) AddTodayFitnesActivity.this
							.getParent()).pop();
				}
			}
		});
		
		// set weight
		weightView.addTextChangedListener(searchEditTextWatcher);
		weightView.setOnEditorActionListener(onEditListener);
		if (weight == 0) {
			weightView.setText(String.valueOf(10));
		} else {
			weightView.setText(String.valueOf(weight));
		}
		// set name of dish
				if(("0".equals(calotyBurn) || null == calotyBurn) && weight!=0){
					//get count of burned calories per hour
					calotyBurn="" + dc*60/weight;
				}
		// set caloriity

		okbutton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		okbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				StartActivity.checkCalendar(AddTodayFitnesActivity.this);
				if (!"".endsWith(weightView.getText().toString())) {
					if (flag_add == 1) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"EEE dd MMMM", new Locale(
										 SaveUtils.getLang(AddTodayFitnesActivity.this)));
						Date curentDateandTime = null;

						try {
							if (templateFlag) {
								curentDateandTime = new Date();
							} else {
								curentDateandTime = sdf.parse(currDate);

								Date nowDate = new Date();
								curentDateandTime.setYear(nowDate.getYear());
							}

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						TodayDish td = new TodayDish(SaveUtils
								.getRealWeight(AddTodayFitnesActivity.this),
								sportName,
								String.valueOf(Constants.ACTIVITY),
								Integer.valueOf(dishCaloricityVTW.getText().toString())>0?-Integer.valueOf(dishCaloricityVTW.getText().toString()): -1,
								category.toString(),
								Integer.valueOf(weightView.getText().toString()),
								Integer.valueOf(dishCaloricityVTW.getText().toString())>0?-Integer.valueOf(dishCaloricityVTW.getText().toString()): -1,
								currDate,
								curentDateandTime.getTime(),
								0,
								"",
								Float.parseFloat(fitnesWeight.replace(",", ".")),
								fitCountSpinner.getSelectedItemId()>=0?fitCountSpinner.getSelectedItemId():0,
								Float.parseFloat(fitnesWay.replace(",", ".")),
								fitWeightSpinner.getSelectedItemId()>=0?fitWeightSpinner.getSelectedItemId():0,
								fitWeightDecSpinner.getSelectedItemId()>=0?fitWeightDecSpinner.getSelectedItemId():0,
								fitApproachCountSpinner.getSelectedItemId()>=0?fitApproachCountSpinner.getSelectedItemId():0,
								timeHHValue,
								timeMMValue);
						if (templateFlag) {
							td.setId(TemplateDishHelper.addNewDish(td,
									AddTodayFitnesActivity.this));
						} else {
							td.setId(TodayDishHelper.addNewDish(td,
									AddTodayFitnesActivity.this));
							if (0 != SaveUtils
									.getUserUnicId(AddTodayFitnesActivity.this)) {
								new SocialUpdater(AddTodayFitnesActivity.this,
										td, false).execute();
							}
						}

					} else {
						if (id != null) {
							fitnesWay = (fitnesWay == null) ? "0": fitnesWay;
							TodayDish td = new TodayDish(id,
									sportName, 
									String.valueOf(Constants.ACTIVITY), dc,
									"", 
									Integer.parseInt(weightView.getText()
											.toString()),
									Integer.valueOf(dishCaloricityVTW.getText().toString())>0?-Integer.valueOf(dishCaloricityVTW.getText().toString()): -1,
									currDate,Float.parseFloat(fitnesWeight),
									fitCountSpinner.getSelectedItemId()>=0?fitCountSpinner.getSelectedItemId():0,
									Float.parseFloat(fitnesWay),
									fitWeightSpinner.getSelectedItemId()>=0?fitWeightSpinner.getSelectedItemId():0,
									fitWeightDecSpinner.getSelectedItemId()>=0?fitWeightDecSpinner.getSelectedItemId():0,
									fitApproachCountSpinner.getSelectedItemId()>=0?fitApproachCountSpinner.getSelectedItemId():0,
									timeHHValue,
									timeMMValue);

							if (templateFlag) {
								TemplateDishHelper.updateDish(td,
										AddTodayFitnesActivity.this);
							} else {
								TodayDishHelper.updateDish(td,
										AddTodayFitnesActivity.this);
								if (0 != SaveUtils
										.getUserUnicId(AddTodayFitnesActivity.this)) {
									new SocialUpdater(
											AddTodayFitnesActivity.this, td,
											true).execute();
								}
							}
						}
					}
					if (CalendarActivityGroup.class.toString().equals(
							parentName)) {
						CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
						if(id!=null){
							activityStack.pop(2);
						}else{
							activityStack.pop(3);
						}
					} else {
						try {
							DishActivityGroup activityStack = (DishActivityGroup) getParent();
							activityStack.getFirst();
						} catch (Exception e) {
							CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
							if(id!=null){
								activityStack.pop(2);
							}else{
								activityStack.pop(3);
							}
						}
					}

				} else {
					// weightView.setBackgroundColor(Color.RED);
				}
				Intent i = new Intent();
				i.setAction(BaseActivity.CUSTOM_INTENT);
				AddTodayFitnesActivity.this.sendBroadcast(i);
			}
		});

		// DialogUtils.setArraySpinnerValues(weightSpinner,CalcActivity.MIN_WEIGHT,CalcActivity.MAX_WEIGHT,
		// AddTodayFitnesActivity.this);
		// weightSpinner.setOnItemSelectedListener(spinnerListener);
		// weightSpinner.setSelection(SaveUtils.getWeight(this));
		 fitnes = (LinearLayout)viewToLoad.findViewById(R.id.linearLayoutFitnes);
		 gym = (LinearLayout)viewToLoad.findViewById(R.id.linearLayoutGym);
		
		fitWeightSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerADDWeight);
		fitCountSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerCount);
		fitApproachCountSpinner = (Spinner) viewToLoad.findViewById(R.id.approachCount);
		fitWeightDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerADDWeightDecimal);
		setFitnesOrGymView();
		setContentView(viewToLoad);
	}

	private void setFitnesOrGymView() {
		// TODO Auto-generated method stub
		if(fitnesWay!=null && fitnesWeight!=null){
			if(!"0".equals(fitnesWay)||!"0".equals(fitnesWeight)){
				gym.setVisibility(View.VISIBLE);
				fitnes.setVisibility(View.GONE);
				setSpinnerValues();
			}else{
				gym.setVisibility(View.GONE);
				fitnes.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {

		super.onPause();
		imm.hideSoftInputFromWindow(weightView.getWindowToken(), 0);
	}

	@Override
	protected void onResume() {

		ArrayList<FitnesType> types = DishListHelper.LoadFitnesMap(this);
		selection = 0;
		for (FitnesType fitnesType : types) {
			if(fitnesType.getDescription().equals(activitiName)){
				selection = types.indexOf(fitnesType);
			}
		}

		imm = (InputMethodManager) AddTodayFitnesActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		setCaloryLoosingView();

		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private OnEditorActionListener onEditListener = new OnEditorActionListener() {

		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			try {
				setCaloryLoosingView();
			} catch (Exception e) {
				dishCaloricityVTW.setText("0");
				e.printStackTrace();
			}
			return false;
		}
	};

	private TextWatcher searchEditTextWatcher = new TextWatcher() {

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			try {
				if (!"".endsWith(weightView.getText().toString())) {
					setCaloryLoosingView();
					// weightView.setBackgroundColor(Color.WHITE);
				} else {
					// weightView.setBackgroundColor(Color.RED);
				}
			} catch (Exception e) {
				dishCaloricityVTW.setText("0");
			}
		}
	};
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			setCaloryLoosingView();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	protected void setCaloryLoosingView() {
		// TODO Auto-generated method stub
		if(fitnesWay!=null && fitnesWeight!=null){
			if(!"0".equals(fitnesWay)||!"0".equals(fitnesWeight)){
//$_POST['razA11']*19.6*($_POST['vesaA11']+$ves_tela)
				float job =(float) (
						(float) Float.parseFloat(fitnesWay.replace(",", "."))*(
								((fitCountSpinner.getSelectedItemId()+1)*19.6*(fitApproachCountSpinner.getSelectedItemId()+1)*
										(SaveUtils.getRealWeight(this)*Float.parseFloat(fitnesWeight.replace(",", ".")) + fitWeightSpinner.getSelectedItemId() + fitWeightDecSpinner.getSelectedItemId()/10)
										*0.239/1000)/(SaveUtils.getExpModeValue(this)*0.01)));
				job = job + job/100* ((SaveUtils.getHeight(this) + Info.MIN_HEIGHT - 175)/2);
				job = job + job/100* ((SaveUtils.getWeight(this) + Info.MIN_WEIGHT - 75)/2);
				DecimalFormat df = new DecimalFormat("###");
				dishCaloricityVTW.setText(df.format(job));
			}else{
				dishCaloricityVTW
				.setText(String.valueOf((int)Float.parseFloat(calotyBurn.replace(",", "."))
						 * Integer.valueOf(weightView.getText().toString())/60));	
			}
		}else{
			dishCaloricityVTW
			.setText(String.valueOf((int)Float.parseFloat(calotyBurn)
					 * Integer.valueOf(weightView.getText().toString())/60));
		}

	}
	
	private void setSpinnerValues() {
		try{

			DialogUtils.setArraySpinnerValues(fitWeightSpinner,0,200,this);	
			
			fitWeightSpinner.setSelection(fitnesWeightSelection>0?fitnesWeightSelection:11);
			fitWeightSpinner.setOnItemSelectedListener(spinnerListener);
			
			DialogUtils.setArraySpinnerValues(fitWeightDecSpinner,0,10,this);						
			fitWeightDecSpinner.setSelection(fitnesWeightDecSelection);	
			fitWeightDecSpinner.setOnItemSelectedListener(spinnerListener);
			
			DialogUtils.setArraySpinnerValues(fitCountSpinner,1,100,this);
			DialogUtils.setArraySpinnerValues(fitApproachCountSpinner, 1, 20, this);
			fitApproachCountSpinner.setSelection(fitApproachCountSelection >0? fitApproachCountSelection:0);
			fitApproachCountSpinner.setOnItemSelectedListener(spinnerListener);
			fitCountSpinner.setSelection(fitnesCountSelection>0?fitnesCountSelection:11);
			fitCountSpinner.setOnItemSelectedListener(spinnerListener);									
		}catch (Exception e) {
			e.printStackTrace();			
		}
			
		}

	@Override
	public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
		timeHHValue = hourOfDay;
		timeMMValue = minute;
		setTime(timeTW);
	}

}
