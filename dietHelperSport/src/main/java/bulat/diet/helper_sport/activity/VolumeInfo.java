package bulat.diet.helper_sport.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.SaveUtils;

public class VolumeInfo extends Activity {
	
	public static final int MIN_CHEST = 40;
	public static final int MIN_PELVIS = 30;
	public static final int MIN_NECK = 15;
	public static final int MIN_BICEPS = 10;
	public static final int MIN_FOREARM = 10;
	public static final int MIN_WAIST = 30;
	public static final int MIN_HIP = 10;
	public static final int MIN_SHIN = 8;
	
	private Spinner chestSpinner;
	private Spinner chestDecSpinner;
	
	private Spinner pelvisSpinner;
	private Spinner pelvisDecSpinner;
	
	private Spinner neckSpinner;
	private Spinner neckDecSpinner;
	
	private Spinner bicepsSpinner;
	private Spinner bicepsDecSpinner;
	
	private Spinner forearmSpinner;
	private Spinner forearmDecSpinner;
	
	private Spinner waistSpinner;
	private Spinner waistDecSpinner;
	
	private Spinner hipSpinner;
	private Spinner hipDecSpinner;
	
	private Spinner shinSpinner;
	private Spinner shinDecSpinner;
	
	private Spinner expirienceSpinner;
	private CheckBox chestCB;
	private CheckBox pelvisCB;
	private CheckBox neckCB;
	private CheckBox bicepsCB;
	private CheckBox forearmCB;
	private CheckBox waistCBr;
	private CheckBox hipCB;
	private CheckBox shinCB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.extrasettings, null);
		setContentView(viewToLoad);					
		
		chestSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerChest);
		chestDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerChestDecimal);
		
		pelvisSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerPelvis);
		pelvisDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerPelvisDecimal);
		
		neckSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerNeck);
		neckDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerNeckDecimal);
		
		bicepsSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerBiceps);
		bicepsDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerBicepsDecimal);
		
		forearmSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerForearm);
		forearmDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerForearmDecimal);
		
		waistSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerWaist);
		waistDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerWaistDecimal);
		
		hipSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerHip);
		hipDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerHipDecimal);
		
		shinSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerShin);
		shinDecSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerShinDecimal);
		
		
		chestCB = (CheckBox) viewToLoad.findViewById(R.id.enabledChest);
		chestCB.setChecked(SaveUtils.getChestEnbl(this));
		chestSpinner.setEnabled(SaveUtils.getChestEnbl(this));
		chestDecSpinner.setEnabled(SaveUtils.getChestEnbl(this));
		chestCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
				if (((CheckBox) v).isChecked()) {
					SaveUtils.setChestEnbl(true, VolumeInfo.this);
					chestSpinner.setEnabled(true);
					chestDecSpinner.setEnabled(true);
				}else{
					SaveUtils.setChestEnbl(false, VolumeInfo.this);
					chestSpinner.setEnabled(false);
					chestDecSpinner.setEnabled(false);
				}
		 
			  }
			});
		pelvisCB = (CheckBox) viewToLoad.findViewById(R.id.enabledPelvis);
		pelvisCB.setChecked(SaveUtils.getPelvisEnbl(this));
		pelvisSpinner.setEnabled(SaveUtils.getPelvisEnbl(this));
		pelvisDecSpinner.setEnabled(SaveUtils.getPelvisEnbl(this));
		pelvisCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
				if (((CheckBox) v).isChecked()) {
					SaveUtils.setPelvisEnbl(true, VolumeInfo.this);
					pelvisSpinner.setEnabled(true);
					pelvisDecSpinner.setEnabled(true);
				}else{
					SaveUtils.setPelvisEnbl(false, VolumeInfo.this);
					pelvisSpinner.setEnabled(false);
					pelvisDecSpinner.setEnabled(false);
				}
		 
			  }
			});
		neckCB = (CheckBox) viewToLoad.findViewById(R.id.enabledNeck);
		neckCB.setChecked(SaveUtils.getNeckEnbl(this));
		neckSpinner.setEnabled(SaveUtils.getNeckEnbl(this));
		neckDecSpinner.setEnabled(SaveUtils.getNeckEnbl(this));
		neckCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
					SaveUtils.setNeckEnbl(((CheckBox) v).isChecked(), VolumeInfo.this);
					neckSpinner.setEnabled(((CheckBox) v).isChecked());
					neckDecSpinner.setEnabled(((CheckBox) v).isChecked());						 
			  }
			});
		bicepsCB = (CheckBox) viewToLoad.findViewById(R.id.enabledBiceps);
		bicepsCB.setChecked(SaveUtils.getBicepsEnbl(this));
		bicepsSpinner.setEnabled(SaveUtils.getBicepsEnbl(this));
		bicepsDecSpinner.setEnabled(SaveUtils.getBicepsEnbl(this));
		bicepsCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
					SaveUtils.setBicepsEnbl(((CheckBox) v).isChecked(), VolumeInfo.this);
					bicepsSpinner.setEnabled(((CheckBox) v).isChecked());
					bicepsDecSpinner.setEnabled(((CheckBox) v).isChecked());						 
			  }
			});
		forearmCB = (CheckBox) viewToLoad.findViewById(R.id.enabledForearm);
		forearmCB.setChecked(SaveUtils.getForearmEnbl(this));
		forearmSpinner.setEnabled(SaveUtils.getForearmEnbl(this));
		forearmDecSpinner.setEnabled(SaveUtils.getForearmEnbl(this));
		forearmCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
					SaveUtils.setForearmEnbl(((CheckBox) v).isChecked(), VolumeInfo.this);
					forearmSpinner.setEnabled(((CheckBox) v).isChecked());
					forearmDecSpinner.setEnabled(((CheckBox) v).isChecked());						 
			  }
			});
		waistCBr = (CheckBox) viewToLoad.findViewById(R.id.enabledWaist);
		waistCBr.setChecked(SaveUtils.getWaistEnbl(this));
		waistSpinner.setEnabled(SaveUtils.getWaistEnbl(this));
		waistDecSpinner.setEnabled(SaveUtils.getWaistEnbl(this));
		waistCBr.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
					SaveUtils.setWaistEnbl(((CheckBox) v).isChecked(), VolumeInfo.this);
					waistSpinner.setEnabled(((CheckBox) v).isChecked());
					waistDecSpinner.setEnabled(((CheckBox) v).isChecked());						 
			  }
			});
		hipCB = (CheckBox) viewToLoad.findViewById(R.id.enabledHip);
		hipCB.setChecked(SaveUtils.getHipEnbl(this));
		hipSpinner.setEnabled(SaveUtils.getHipEnbl(this));
		hipDecSpinner.setEnabled(SaveUtils.getHipEnbl(this));
		hipCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
					SaveUtils.setHipEnbl(((CheckBox) v).isChecked(), VolumeInfo.this);
					hipSpinner.setEnabled(((CheckBox) v).isChecked());
					hipDecSpinner.setEnabled(((CheckBox) v).isChecked());						 
			  }
			});
		shinCB = (CheckBox) viewToLoad.findViewById(R.id.enabledShin);
		shinCB.setChecked(SaveUtils.getShinEnbl(this));
		shinSpinner.setEnabled(SaveUtils.getShinEnbl(this));
		shinDecSpinner.setEnabled(SaveUtils.getShinEnbl(this));
		shinCB.setOnClickListener(new OnClickListener() {

			  public void onClick(View v) {
		                //is chkIos checked?
					SaveUtils.setShinEnbl(((CheckBox) v).isChecked(), VolumeInfo.this);
					shinSpinner.setEnabled(((CheckBox) v).isChecked());
					shinDecSpinner.setEnabled(((CheckBox) v).isChecked());						 
			  }
			});
		
		expirienceSpinner = (Spinner) viewToLoad.findViewById(R.id.SpinnerExpirienceActivity);
			
		
		ArrayList<DishType> mode = new ArrayList<DishType>();
		mode.add(new DishType(20, getString(R.string.exp1)));
		mode.add(new DishType(24, getString(R.string.exp2)));
		mode.add(new DishType(27, getString(R.string.exp3)));
		mode.add(new DishType(30, getString(R.string.exp4)));
		ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
				android.R.layout.simple_spinner_item, mode);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		expirienceSpinner.setAdapter(adapter);		
		expirienceSpinner.setSelection(SaveUtils.getExpModeSelection(this));	
		expirienceSpinner.setOnItemSelectedListener(spinnerModeListener);
	////
			
			
		setSpinnerValues();

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
		try{
			
			DialogUtils.setArraySpinnerValues(chestSpinner,MIN_CHEST,180,this);							
			chestSpinner.setSelection(SaveUtils.getChest(this));
			chestSpinner.setOnItemSelectedListener(spinnerListener);			
			DialogUtils.setArraySpinnerValues(chestDecSpinner,0,9,this);						
			chestDecSpinner.setSelection(SaveUtils.getChestDec(this));	
			chestDecSpinner.setOnItemSelectedListener(spinnerListener);
			
			DialogUtils.setArraySpinnerValues(pelvisSpinner,MIN_PELVIS,200,this);							
			pelvisSpinner.setSelection(SaveUtils.getPelvis(this));	
			pelvisSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(pelvisDecSpinner,0,9,this);						
			pelvisDecSpinner.setSelection(SaveUtils.getPelvisDec(this));
			pelvisDecSpinner.setOnItemSelectedListener(spinnerListener);
						
			DialogUtils.setArraySpinnerValues(neckSpinner,MIN_NECK,70,this);							
			neckSpinner.setSelection(SaveUtils.getNeck(this));	
			neckSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(neckDecSpinner,0,9,this);						
			neckDecSpinner.setSelection(SaveUtils.getNeckDec(this));
			neckDecSpinner.setOnItemSelectedListener(spinnerListener);
			
			DialogUtils.setArraySpinnerValues(bicepsSpinner,MIN_BICEPS,78,this);							
			bicepsSpinner.setSelection(SaveUtils.getBiceps(this));	
			bicepsSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(bicepsDecSpinner,0,9,this);						
			bicepsDecSpinner.setSelection(SaveUtils.getBicepsDec(this));
			bicepsDecSpinner.setOnItemSelectedListener(spinnerListener);
			
			DialogUtils.setArraySpinnerValues(forearmSpinner,MIN_FOREARM,60,this);							
			forearmSpinner.setSelection(SaveUtils.getForearm(this));	
			forearmSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(forearmDecSpinner,0,9,this);						
			forearmDecSpinner.setSelection(SaveUtils.getForearmDec(this));
			forearmDecSpinner.setOnItemSelectedListener(spinnerListener);
			
			DialogUtils.setArraySpinnerValues(waistSpinner,MIN_WAIST,300,this);							
			waistSpinner.setSelection(SaveUtils.getWaist(this));	
			waistSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(waistDecSpinner,0,9,this);						
			waistDecSpinner.setSelection(SaveUtils.getWaistDec(this));
			waistDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(hipSpinner,MIN_HIP,100,this);							
			hipSpinner.setSelection(SaveUtils.getHip(this));	
			hipSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(hipDecSpinner,0,9,this);						
			hipDecSpinner.setSelection(SaveUtils.getHipDec(this));
			hipDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(shinSpinner,MIN_SHIN,70,this);							
			shinSpinner.setSelection(SaveUtils.getShin(this));	
			shinSpinner.setOnItemSelectedListener(spinnerListener);		
			DialogUtils.setArraySpinnerValues(shinDecSpinner,0,9,this);						
			shinDecSpinner.setSelection(SaveUtils.getShinDec(this));
			shinDecSpinner.setOnItemSelectedListener(spinnerListener);
												
		}catch (Exception e) {
			e.printStackTrace();			
		}
			
		}	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	private OnItemSelectedListener spinnerModeListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			SaveUtils.saveExpModeSelection(arg2, VolumeInfo.this);
			SaveUtils.saveExpModeValue(((DishType)expirienceSpinner.getItemAtPosition(arg2)).getTypeKey(), VolumeInfo.this);
			//DialogUtils.showDialog(Info.this.getParent(), getString(R.string.save_prove));
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};
	
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener(){

		

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			saveAll();		
			

			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}		
	};


	public void saveAll() {				
		
		try{
						
			SaveUtils.saveChest((int)chestSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveChestDec((int)chestDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.savePelvis((int)pelvisSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.savePelvisDec((int)pelvisDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.saveNeck((int)neckSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveNeckDec((int)neckDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.saveBiceps((int)bicepsSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveBicepsDec((int)bicepsDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.saveForearm((int)forearmSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveForearmDec((int)forearmDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.saveWaist((int)waistSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveWaistDec((int)waistDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.saveHip((int)hipSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveHipDec((int)hipDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.saveShin((int)shinSpinner.getSelectedItemId(),VolumeInfo.this);
			SaveUtils.saveShinDec((int)shinDecSpinner.getSelectedItemId(),VolumeInfo.this);
			
			SaveUtils.setActivated(VolumeInfo.this, true);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
}

}
