package bulat.diet.helper_sport.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.SaveUtils;

public class CalcActivity  extends BaseActivity{
	public static final int MIN_WEIGHT = 30;
	public static final int MAX_WEIGHT = 200;
	private static final int MIN_HEIGHT = 140;
	private static final int MAX_HEIGHT = 210;
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 90;
	private Spinner ageSpinner;
	private Spinner sexSpinner;
	private Spinner weightSpinner;
	private Spinner highSpinner;
	private Spinner activitySpinner;
	private TextView BMItextView;
	private TextView BMRtextView;
	private TextView MetatextView;
	private Button saveBtn;
	
	double BMI;
	int BMR;
	int META;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.calculator, null);
		setContentView(viewToLoad);
		ageSpinner = (Spinner) findViewById(R.id.SpinnerAge);
		sexSpinner = (Spinner) findViewById(R.id.SpinnerSex);
		weightSpinner = (Spinner) findViewById(R.id.SpinnerWeight);
		highSpinner = (Spinner) findViewById(R.id.SpinnerHeight);
		activitySpinner = (Spinner) findViewById(R.id.SpinnerActivity);
		
		BMItextView = (TextView) findViewById(R.id.textViewBMI);
		BMRtextView = (TextView) findViewById(R.id.textViewBMR);
		MetatextView = (TextView) findViewById(R.id.textViewMeta);

		saveBtn = (Button) findViewById(R.id.buttonSave);
		saveBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {				
				
					try{
						
						SaveUtils.saveBMI(String.valueOf(BMI), CalcActivity.this);
						SaveUtils.saveBMR(String.valueOf(BMR), CalcActivity.this);
						SaveUtils.saveMETA(String.valueOf(META), CalcActivity.this);
						
						SaveUtils.saveAge((int)ageSpinner.getSelectedItemId(), CalcActivity.this);
						SaveUtils.saveActivity((int)activitySpinner.getSelectedItemId(), CalcActivity.this);
						SaveUtils.saveHeight((int)highSpinner.getSelectedItemId(), CalcActivity.this);
						SaveUtils.saveSex((int)sexSpinner.getSelectedItemId(), CalcActivity.this);
						SaveUtils.saveWeight((int)weightSpinner.getSelectedItemId(), CalcActivity.this);
						BMI = Double.parseDouble(SaveUtils.getBMI(CalcActivity.this));
						if(BMI < 18.5 ){
							SaveUtils.saveMode(2,CalcActivity.this);		
						} else
						if(BMI < 24.9 ){
							SaveUtils.saveMode(1,CalcActivity.this);
						} else
						if(BMI < 29.9 ){
							SaveUtils.saveMode(0,CalcActivity.this);
						} else {
							SaveUtils.saveMode(0,CalcActivity.this);
						}
						SaveUtils.setActivated(CalcActivity.this, true);
						DialogUtils.showDialog(CalcActivity.this.getParent(), getString(R.string.save_prove));
					}catch (Exception e) {
						e.printStackTrace();
					}
			}
		});	
		setSpinnerValues();
		
	}

	private void setSpinnerValues() {
	try{
		DialogUtils.setArraySpinnerValues(ageSpinner,MIN_AGE,MAX_AGE,this);				
		ageSpinner.setOnItemSelectedListener(spinnerListener);
		ageSpinner.setSelection(SaveUtils.getAge(this));	
		
		DialogUtils.setArraySpinnerValues(weightSpinner,MIN_WEIGHT,MAX_WEIGHT,this);				
		weightSpinner.setOnItemSelectedListener(spinnerListener);
		weightSpinner.setSelection(SaveUtils.getWeight(this));	
		
		DialogUtils.setArraySpinnerValues(highSpinner,MIN_HEIGHT,MAX_HEIGHT,this);			
		highSpinner.setOnItemSelectedListener(spinnerListener);
		highSpinner.setSelection(SaveUtils.getHeight(this));	
		
		ArrayList<DishType> genders = new ArrayList<DishType>();
		genders.add(new DishType( 166, getString(R.string.male)));
		genders.add(new DishType( 0, getString(R.string.female)));		
		ArrayAdapter<DishType>  adapter = new ArrayAdapter<DishType>(this, android.R.layout.simple_spinner_item, genders);		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sexSpinner.setAdapter(adapter);	
		sexSpinner.setOnItemSelectedListener(spinnerListener);
		sexSpinner.setSelection(SaveUtils.getSex(this));	
		

		ArrayList<DishType> activity = new ArrayList<DishType>();
		activity.add(new DishType( 1, getString(R.string.level_1)));
		activity.add(new DishType( 2, getString(R.string.level_2)));
		activity.add(new DishType( 3, getString(R.string.level_3)));
		activity.add(new DishType( 4, getString(R.string.level_4)));
		activity.add(new DishType( 5, getString(R.string.level_5)));		
		ArrayAdapter<DishType>  adapter2 = new ArrayAdapter<DishType>(this, android.R.layout.simple_spinner_item, activity);		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		activitySpinner.setAdapter(adapter2);	
		activitySpinner.setOnItemSelectedListener(spinnerListener);
		activitySpinner.setSelection(SaveUtils.getActivity(this));	
		
	}catch (Exception e) {
		e.printStackTrace();
		SaveUtils.saveAge(0, CalcActivity.this);
		SaveUtils.saveActivity(0, CalcActivity.this);
		SaveUtils.saveHeight(0, CalcActivity.this);
		SaveUtils.saveSex(0, CalcActivity.this);
		SaveUtils.saveWeight(0, CalcActivity.this);
	}
		
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener(){

		

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//BMI 	Weight Status
			//Below 18.5 	Underweight
			//18.5 - 24.9 	Normal
			//25 - 29.9 	Overweight
			//30.0 & Above 	Obese
			//Ð”Ð»Ñ� Ð¼ÑƒÐ¶Ñ‡Ð¸Ð½: BMR = [9.99 x Ð²ÐµÑ� (ÐºÐ³)] + [6.25 x Ñ€Ð¾Ñ�Ñ‚ (Ñ�Ð¼)] - [4.92 x Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚ (Ð² Ð³Ð¾Ð´Ð°Ñ…)] + 5
			//Ð”Ð»Ñ� Ð¶ÐµÐ½Ñ‰Ð¸Ð½: BMR = [9.99 x Ð²ÐµÑ� (ÐºÐ³)] + [6.25 x Ñ€Ð¾Ñ�Ñ‚ (Ñ�Ð¼)] - [4.92 x Ð²Ð¾Ð·Ñ€Ð°Ñ�Ñ‚ (Ð² Ð³Ð¾Ð´Ð°Ñ…)] -161
			
			BMI = (((DishType)weightSpinner.getSelectedItem()).getTypeKey())/ 
					(0.01*((DishType)highSpinner.getSelectedItem()).getTypeKey()*0.01*((DishType)highSpinner.getSelectedItem()).getTypeKey());
			BMI = Math.round(BMI * 10.0) / 10.0;
			String addText = "";
			if(BMI < 18.5 ){
				addText = getString(R.string.underweight);
			} else
			if(BMI < 24.9 ){
				addText = getString(R.string.normal_weight);
			} else
			if(BMI < 29.9 ){
				addText = getString(R.string.overweight);
			} else {
				addText = getString(R.string.obese);
			}
			
			BMItextView.setText(String.valueOf(BMI) + " " + addText);//new BigDecimal(BMI).round(new MathContext(1, RoundingMode.HALF_EVEN))) + " " + addText);

			
			BMR = (int)((9.99*((DishType)weightSpinner.getSelectedItem()).getTypeKey()) + 
					(6.25*((DishType)highSpinner.getSelectedItem()).getTypeKey()) - 
					(4.92*((DishType)ageSpinner.getSelectedItem()).getTypeKey()) - 161 + 
					((DishType)sexSpinner.getSelectedItem()).getTypeKey());
			BMR = (int)BMR;
			MetatextView.setText(String.valueOf(BMR) + " " + getString(R.string.calorie_day));//new BigDecimal(BMR).round(new MathContext(1, RoundingMode.HALF_EVEN))));
					if(((DishType)activitySpinner.getSelectedItem()).getTypeKey()==1){
						META = (int)(BMR*1.2);
						
					}else if(((DishType)activitySpinner.getSelectedItem()).getTypeKey()==2){
						META =(int)( BMR*1.35);
					}else if(((DishType)activitySpinner.getSelectedItem()).getTypeKey()==3){
						META = (int)(BMR*1.55);
					}else if(((DishType)activitySpinner.getSelectedItem()).getTypeKey()==3){
						META = (int)(BMR*1.75); 
					}else if(((DishType)activitySpinner.getSelectedItem()).getTypeKey()==3){
						META = (int)(BMR*1.92);
					}
			BMRtextView.setText(String.valueOf(META) + " " + getString(R.string.calorie_day));//new BigDecimal(BMR).round(new MathContext(1, RoundingMode.HALF_EVEN))));		
					
					
			
			//ageSpinner;
			//sexSpinner;
			//weightSpinner;
			//highSpinner;
			//activitySpinner
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}		
	};

}
