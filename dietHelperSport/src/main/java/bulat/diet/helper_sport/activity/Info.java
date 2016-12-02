package bulat.diet.helper_sport.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.component.PrefixedEditText;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;
import bulat.diet.helper_sport.utils.TextWatcherWithParameter;

public class Info extends Activity {

	private Spinner modeSpinner;
	private TextView BMItextView;
	private TextView BMRtextView;
	private TextView MetatextView;

	public static final int MIN_WEIGHT = 30;
	public static final int MAX_WEIGHT = 200;
	public static final int MIN_HEIGHT = 140;
	public static final int MAX_HEIGHT = 210;
	public static final int MIN_AGE = 18;
	public static final int MAX_AGE = 90;
	public static final double INDEX_FAT = 0.2/9;
	public static final double INDEX_CARBON = 0.5/4;
	public static final double INDEX_PROTEIN = 0.3/4;
	
	private Spinner ageSpinner;
	private Spinner sexSpinner;
	private Spinner weightSpinner;
	private Spinner weightSpinnerDec;
	private Spinner highSpinner;
	private Spinner activitySpinner;

	double BMI;
	int BMR;
	int META;
	private CheckBox chkIos;
	EditText limitET;
	LinearLayout castomLimitLayout;
	LinearLayout castomLimitLayoutBSU;
	PrefixedEditText edtProtein;
	PrefixedEditText edtFat;
	PrefixedEditText edtCarbon;
	private SimpleDateFormat sdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.settings, null);
		setContentView(viewToLoad);	
		chkIos = (CheckBox)  viewToLoad.findViewById(R.id.cbLimit);
		limitET = (EditText) viewToLoad.findViewById(R.id.editTextLimitValue);
		edtProtein = (PrefixedEditText) findViewById(R.id.editTextProtein);
		edtFat = (PrefixedEditText) findViewById(R.id.editTextFat);
		edtCarbon = (PrefixedEditText) findViewById(R.id.editTextCarbon);
		
		edtProtein.setPrefix(getString(R.string.widjet_prot));        
		edtFat.setPrefix(getString(R.string.widjet_fat));       
		edtCarbon.setPrefix(getString(R.string.widjet_carb));
		
		edtProtein.addTextChangedListener(new TextWatcherWithParameter( limitET, edtProtein, edtFat, edtCarbon));
		edtCarbon.addTextChangedListener(new TextWatcherWithParameter( limitET, edtProtein, edtFat, edtCarbon));
		edtFat.addTextChangedListener(new TextWatcherWithParameter( limitET, edtProtein, edtFat, edtCarbon));
		
		int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, 0, this);
		int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, 0, this);
		int limitCarbon = SaveUtils.readInt(SaveUtils.LIMIT_CARBON, 0, this);
		int limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, 0, this);
		if(limitKkal>0){
			chkIos.setChecked(true);
			limitET.setText(String.valueOf(limitKkal));
			edtProtein.setText(String.valueOf(limitProtein));
			edtCarbon.setText(String.valueOf(limitCarbon));
			edtFat.setText(String.valueOf(limitFat));
			LinearLayout limitsLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutLimits);
			limitsLayout.setVisibility(View.GONE);
			castomLimitLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutCastomLimit);
		    castomLimitLayoutBSU = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutCastomLimitBSU);
			castomLimitLayout.setVisibility(View.VISIBLE);
			castomLimitLayoutBSU.setVisibility(View.VISIBLE);
		}
		 
		chkIos.setOnClickListener(new OnClickListener() {

		  public void onClick(View v) {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) {
				int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, META, Info.this);
				int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, (int)(INDEX_PROTEIN*META), Info.this);
				int limitCarbon = SaveUtils.readInt(SaveUtils.LIMIT_CARBON, (int)(INDEX_CARBON*META), Info.this);
				int limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, (int)(INDEX_FAT*META), Info.this);
				edtProtein.setText(String.valueOf(limitProtein));
				edtCarbon.setText(String.valueOf(limitCarbon));
				edtFat.setText(String.valueOf(limitFat));
				
				SaveUtils.setCustomLimit(0, Info.this);
				LinearLayout limitsLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutLimits);
				limitsLayout.setVisibility(View.GONE);
				castomLimitLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutCastomLimit);
			    castomLimitLayoutBSU = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutCastomLimitBSU);
				castomLimitLayout.setVisibility(View.VISIBLE);
				castomLimitLayoutBSU.setVisibility(View.VISIBLE);
			}else{
				SaveUtils.setCustomLimit(0, Info.this);
				LinearLayout limitsLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutLimits);
				limitsLayout.setVisibility(View.VISIBLE);
				SaveUtils.saveLifeStyle(1, Info.this);
				castomLimitLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutCastomLimit);
			    castomLimitLayoutBSU = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutCastomLimitBSU);
				castomLimitLayout.setVisibility(View.GONE);
				castomLimitLayoutBSU.setVisibility(View.GONE);
			}
	 
		  }
		});
		Button saveLimitButton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		saveLimitButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				limitET.setBackgroundColor(Color.WHITE);
				if(limitET.getText().length()<3){
					limitET.setBackgroundColor(Color.RED);
				}else{
					try{
						SaveUtils.writeInt(SaveUtils.LIMIT, Integer.valueOf(limitET.getText().toString()), Info.this);
						SaveUtils.writeInt(SaveUtils.LIMIT_PROTEIN ,Integer.valueOf(edtProtein.getText().toString()), Info.this);
						SaveUtils.writeInt(SaveUtils.LIMIT_CARBON ,Integer.valueOf(edtCarbon.getText().toString()), Info.this);
						SaveUtils.writeInt(SaveUtils.LIMIT_FAT ,Integer.valueOf(edtFat.getText().toString()), Info.this);

						Toast.makeText(Info.this, getString(R.string.save_limit),
								Toast.LENGTH_LONG).show();
					}catch (Exception e) {
						limitET.setBackgroundColor(Color.RED);
						e.printStackTrace();
					}
				}
			}
		});
		ageSpinner = (Spinner) findViewById(R.id.SpinnerAge);
		sexSpinner = (Spinner) findViewById(R.id.SpinnerSex);
		weightSpinner = (Spinner) findViewById(R.id.SpinnerWeight);
		weightSpinnerDec = (Spinner) findViewById(R.id.SpinnerWeightDecimal);
		highSpinner = (Spinner) findViewById(R.id.SpinnerHeight);
		activitySpinner = (Spinner) findViewById(R.id.SpinnerActivity);
		
		BMItextView = (TextView) findViewById(R.id.textViewBMI);
		BMRtextView = (TextView) findViewById(R.id.textViewMeta);
		MetatextView = (TextView) findViewById(R.id.textViewBMR);
		
		modeSpinner = (Spinner) findViewById(R.id.SpinnerMode);
		ArrayList<DishType> mode = new ArrayList<DishType>();
		mode.add(new DishType(0, getString(R.string.losing_weight)));
		mode.add(new DishType(1, getString(R.string.keeping_weight)));
		mode.add(new DishType(2, getString(R.string.gaining_weight)));
		ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
				android.R.layout.simple_spinner_item, mode);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modeSpinner.setAdapter(adapter);		
		modeSpinner.setSelection(SaveUtils.getMode(this));	
		modeSpinner.setOnItemSelectedListener(spinnerModeListener);
	////
			
			
		setSpinnerValues();
		Locale locale;
		if("".endsWith(SaveUtils.getLang(this))){
			locale = new Locale("ru");
		}else{
			locale = new Locale(SaveUtils.getLang(this));
		}
		sdf = new SimpleDateFormat("EEE dd MMMM",locale);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void setSpinnerValues() {
		try {
			DialogUtils.setArraySpinnerValues(ageSpinner, MIN_AGE, MAX_AGE,
					this);
			ageSpinner.setSelection(SaveUtils.getAge(this));
			ageSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(weightSpinner, MIN_WEIGHT,
					MAX_WEIGHT, this);
			weightSpinner.setSelection(SaveUtils.getWeight(this));
			weightSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(weightSpinnerDec, 0, 10, this);
			weightSpinnerDec.setSelection(SaveUtils.getWeightDec(this));
			weightSpinnerDec.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(highSpinner, MIN_HEIGHT,
					MAX_HEIGHT, this);
			highSpinner.setSelection(SaveUtils.getHeight(this));
			highSpinner.setOnItemSelectedListener(spinnerListener);

			ArrayList<DishType> genders = new ArrayList<DishType>();
			genders.add(new DishType(166, getString(R.string.male)));
			genders.add(new DishType(0, getString(R.string.female)));
			ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
					android.R.layout.simple_spinner_item, genders);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sexSpinner.setAdapter(adapter);
			sexSpinner.setSelection(SaveUtils.getSex(this));
			sexSpinner.setOnItemSelectedListener(spinnerListener);

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

			activitySpinner.setSelection(SaveUtils.getActivity(this));
			activitySpinner.setOnItemSelectedListener(spinnerListener);

		} catch (Exception e) {
			e.printStackTrace();
			SaveUtils.saveAge(0, Info.this);
			SaveUtils.saveActivity(0, Info.this);
			SaveUtils.saveHeight(0, Info.this);
			SaveUtils.saveSex(0, Info.this);
			SaveUtils.saveWeight(0, Info.this);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			BMI = Double.parseDouble(SaveUtils.getBMI(this));
			String addText = "";
			if (BMI < 18.5) {
				addText = "(" + getString(R.string.underweight) + ")";

			} else if (BMI < 24.9) {
				addText = "(" + getString(R.string.normal_weight) + ")";

			} else if (BMI < 29.9) {
				addText = "(" + getString(R.string.overweight) + ")";

			} else {
				addText = "(" + getString(R.string.obese) + ")";

			}
			BMItextView.setText(BMI + addText);
			BMRtextView.setText(SaveUtils.getBMR(this) + "("
					+ getString(R.string.kcal_day) + ")");
			MetatextView.setText(SaveUtils.getMETA(this) + "("
					+ getString(R.string.kcal_day) + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setSpinnerValues();
	}

	private OnItemSelectedListener spinnerModeListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			SaveUtils.saveMode(arg2, Info.this);
			// DialogUtils.showDialog(Info.this.getParent(),
			// getString(R.string.save_prove));
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// BMI Weight Status
			// Below 18.5 Underweight
			// 18.5 - 24.9 Normal
			// 25 - 29.9 Overweight
			// 30.0 & Above Obese
			// Ð”Ð»Ñ� Ð¼ÑƒÐ¶Ñ‡Ð¸Ð½: BMR = [9.99 x
			// Ð²ÐµÑ� (ÐºÐ³)] + [6.25 x Ñ€Ð¾Ñ�Ñ‚
			// (Ñ�Ð¼)] - [4.92 x Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚ (Ð²
			// Ð³Ð¾Ð´Ð°Ñ…)] + 5
			// Ð”Ð»Ñ� Ð¶ÐµÐ½Ñ‰Ð¸Ð½: BMR = [9.99 x
			// Ð²ÐµÑ� (ÐºÐ³)] + [6.25 x Ñ€Ð¾Ñ�Ñ‚
			// (Ñ�Ð¼)] - [4.92 x Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚ (Ð²
			// Ð³Ð¾Ð´Ð°Ñ…)] -161

			BMI = (((DishType) weightSpinner.getSelectedItem()).getTypeKey() + ((DishType) weightSpinnerDec
					.getSelectedItem()).getTypeKey() / 10)
					/ (0.01 * ((DishType) highSpinner.getSelectedItem())
							.getTypeKey() * 0.01 * ((DishType) highSpinner
							.getSelectedItem()).getTypeKey());
			BMI = Math.round(BMI * 10.0) / 10.0;
			String addText = "";
			if (BMI < 18.5) {
				addText = getString(R.string.underweight);
			} else if (BMI < 24.9) {
				addText = getString(R.string.normal_weight);
			} else if (BMI < 29.9) {
				addText = getString(R.string.overweight);
			} else {
				addText = getString(R.string.obese);
			}

			BMItextView.setText(String.valueOf(BMI) + " " + addText);// new
																		// BigDecimal(BMI).round(new
																		// MathContext(1,
																		// RoundingMode.HALF_EVEN)))
																		// + " "
																		// +
																		// addText);

			BMR = (int) ((10 * (((DishType) weightSpinner.getSelectedItem())
					.getTypeKey() + ((DishType) weightSpinnerDec
					.getSelectedItem()).getTypeKey() / 10))
					+ (6.25 * ((DishType) highSpinner.getSelectedItem())
							.getTypeKey())
					- (5 * ((DishType) ageSpinner.getSelectedItem())
							.getTypeKey()) - 161 + ((DishType) sexSpinner
					.getSelectedItem()).getTypeKey());
			BMR = (int) BMR;
			if (((DishType) activitySpinner.getSelectedItem()).getTypeKey() == 1) {
				META = (int) (BMR * 1.2);

			} else if (((DishType) activitySpinner.getSelectedItem())
					.getTypeKey() == 2) {
				META = (int) (BMR * 1.35);
			} else if (((DishType) activitySpinner.getSelectedItem())
					.getTypeKey() == 3) {
				META = (int) (BMR * 1.55);
			} else if (((DishType) activitySpinner.getSelectedItem())
					.getTypeKey() == 4) {
				META = (int) (BMR * 1.75);
			} else if (((DishType) activitySpinner.getSelectedItem())
					.getTypeKey() == 5) {
				META = (int) (BMR * 1.92);
			}
			BMRtextView.setText(String.valueOf((int) (META * 0.8)) + " "
					+ getString(R.string.calorie_day));// new
														// BigDecimal(BMR).round(new
														// MathContext(1,
														// RoundingMode.HALF_EVEN))));

			MetatextView.setText(String.valueOf(META) + " "
					+ getString(R.string.calorie_day));// new
														// BigDecimal(BMR).round(new
														// MathContext(1,
														// RoundingMode.HALF_EVEN))));

			saveAll();

			// ageSpinner;
			// sexSpinner;
			// weightSpinner;
			// highSpinner;
			// activitySpinner

		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	public void saveAll() {

		try {

			SaveUtils.saveBMI(String.valueOf(BMI), Info.this);
			SaveUtils.saveBMR(String.valueOf((int) (META * 0.8)), Info.this);
			SaveUtils.saveMETA(String.valueOf(META), Info.this);

			SaveUtils.saveAge((int) ageSpinner.getSelectedItemId(), Info.this);
			SaveUtils.saveActivity((int) activitySpinner.getSelectedItemId(),
					Info.this);
			SaveUtils.saveHeight((int) highSpinner.getSelectedItemId(),
					Info.this);
			SaveUtils.saveSex((int) sexSpinner.getSelectedItemId(), Info.this);
			SaveUtils.saveWeight((int) weightSpinner.getSelectedItemId(),
					Info.this);
			SaveUtils.saveWeightDec((int) weightSpinnerDec.getSelectedItemId(),
					Info.this);
			Date curentDateandTime = new Date();
			Date start = new Date();
			
			try {
				start = sdf.parse(sdf.format(new Date(curentDateandTime.getTime())));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			start.setYear(curentDateandTime.getYear());
			TodayDishHelper.updateBobyParams(
					Info.this,
					String.valueOf(start.getTime()),
					String.valueOf(SaveUtils.getRealWeight(Info.this)),
					new BodyParams(String.valueOf((float) SaveUtils.getChest(Info.this)+VolumeInfo.MIN_CHEST + (float) SaveUtils.getChestDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getBiceps(Info.this)+VolumeInfo.MIN_BICEPS + (float) SaveUtils.getBicepsDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getPelvis(Info.this)+VolumeInfo.MIN_PELVIS + (float) SaveUtils.getPelvisDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getNeck(Info.this)+VolumeInfo.MIN_NECK + (float) SaveUtils.getNeckDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getWaist(Info.this)+VolumeInfo.MIN_WAIST + (float) SaveUtils.getWaistDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getForearm(Info.this)+VolumeInfo.MIN_FOREARM + (float) SaveUtils.getForearmDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getHip(Info.this)+VolumeInfo.MIN_HIP + (float) SaveUtils.getHipDec(Info.this)/10), 
							String.valueOf((float) SaveUtils.getShin(Info.this)+VolumeInfo.MIN_SHIN + (float) SaveUtils.getShinDec(Info.this)/10)));
			
			// update social profile
			if (SaveUtils.getUserUnicId(this) != 0) {
				new SocialUpdater(this).execute();
			}
			/*
			 * BMI = Double.parseDouble(SaveUtils.getBMI(Info.this)); if(BMI <
			 * 18.5 ){ SaveUtils.saveMode(2,Info.this); } else if(BMI < 24.9 ){
			 * SaveUtils.saveMode(1,Info.this); } else if(BMI < 29.9 ){
			 * SaveUtils.saveMode(0,Info.this); } else {
			 * SaveUtils.saveMode(0,Info.this); }
			 */
			SaveUtils.setActivated(Info.this, true);
			// DialogUtils.showDialog(Info.this.getParent(),
			// getString(R.string.save_prove));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
