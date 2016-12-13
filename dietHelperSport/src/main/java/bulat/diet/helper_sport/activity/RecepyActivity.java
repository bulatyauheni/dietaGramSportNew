package bulat.diet.helper_sport.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.RecepyDishAdapter;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TemplateDishHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;

public class RecepyActivity extends BaseActivity {
	public static final String NAME = "name_r";
	public static final String ID = "id_r";
	public static final String DATE = "date";
	public static final String PARENT_NAME = "parentname";
	public static final String BACKBTN = "backbtn";
	private static final int DIALOG_TEMPLATE_NAME = 0;
	public static final String TEMPLATE = "TEMPLATE";
	private Spinner templateSpinner;
	String curentDateandTime;
	ListView dishesList;
	EditText realWeight;
	Cursor c;
	TextView header;
	String parentName = null;
	int sum;
	float sumF;
	float sumC;
	float sumP;
	private DishActivityGroup parentDishActivity;
	private CalendarActivityGroup parentCalendarActivity;
	private boolean flagLoad = false;
	private static String id;
	String date = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.new_template_list, null);
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);

		
		realWeight = (EditText)viewToLoad.findViewById(R.id.editTextrealWeight);
		Button backButton = (Button) viewToLoad
						.findViewById(R.id.buttonBack);
		backButton.setVisibility(View.VISIBLE);

		backButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					onBackPressed();
				}
			});
		TextView tvl = (TextView) viewToLoad.findViewById(R.id.textViewTotalLabel);
		tvl.setVisibility(View.GONE);
		TextView tvc = (TextView) viewToLoad.findViewById(R.id.textViewCaloricityLabel);
		tvc.setVisibility(View.VISIBLE);
		TextView tvll = (TextView) viewToLoad.findViewById(R.id.textViewLimitLabel);
		tvll.setVisibility(View.GONE);
		TextView tvlv = (TextView) viewToLoad.findViewById(R.id.textViewLimitValue);
		tvlv.setVisibility(View.GONE);
		
		LinearLayout lHeader = (LinearLayout)viewToLoad.findViewById(R.id.relativeLayoutHeader);
	    lHeader.setBackgroundResource(R.color.yelow);
		LinearLayout ltw = (LinearLayout)viewToLoad.findViewById(R.id.linearLayoutTotalWeight);
		ltw.setVisibility(View.VISIBLE);
		Button addButton = (Button) viewToLoad.findViewById(R.id.buttonAdd);
		addButton.setOnClickListener(addTodayDishListener);
		Button okButton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		okButton.setOnClickListener(okButtonListener);
		Button saveButton = (Button) viewToLoad.findViewById(R.id.savetemplate);
		saveButton.setOnClickListener(saveListener);
		Button loadButton = (Button) viewToLoad.findViewById(R.id.loadtemplate);
		loadButton.setOnClickListener(loadListener);
		LinearLayout linearLayoutBtnLoadTempl = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutBtnLoadTempl);
		linearLayoutBtnLoadTempl.setVisibility(View.GONE);
		Button buttonFitnes = (Button) viewToLoad.findViewById(R.id.buttonFitnes);
		buttonFitnes.setVisibility(View.GONE);
		parentName = DishActivityGroup.class.toString();
		if (extras != null) {
			date = extras.getString(NAME);
			id = extras.getString(ID);
			parentName = extras.getString(PARENT_NAME);
		}		
		
		setContentView(viewToLoad);
	}
	
	@Override
	protected void onResume() {
		//colors
		//main FFF0E5
		//header FF9730
		//title FFF6EF
		// TODO Auto-generated method stub
		super.onResume();
		sum = 0;
		sumF = 0;
		sumC = 0;
		sumP = 0;
		templateSpinner = (Spinner) findViewById(R.id.template_spinner);
		loadTemplates();
	
		
		header.setText(date);
		
		setAdapter();
	}

	

	private void setAdapter() {
		c = TemplateDishHelper.getDishesByID(getApplicationContext(),
				id);
		//if its new or empty template then create fish for  template
		
		if (c != null) {							
			try {
				RecepyDishAdapter da;				
				da = new RecepyDishAdapter(getApplicationContext(), c,
						(DishListActivityGroup) getParent());

				
				dishesList = (ListView) findViewById(R.id.listViewTodayDishes);
				dishesList.setAdapter(da);
				dishesList.setItemsCanFocus(true);

				da.registerDataSetObserver(new DataSetObserver() {
					@Override
					public void onChanged() {
						sum = 0;
						sumF = 0;
						sumC = 0;
						sumP = 0;
						initDishTable();									
					}
					
				});
				dishesList.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
					}
				});
				initDishTable();
				
			} catch (Exception e) {
				e.printStackTrace();
				if (c != null)
					c.close();
			} finally {
				// c.close();
			}
		}
	}

	private void loadTemplates() {
		ArrayList<DishType> types = new ArrayList<DishType>();
		types.add(new DishType(0, getString(R.string.choosetemplate)));
		types.addAll(TemplateDishHelper.getDaysArray(RecepyActivity.this));		
		
		ArrayAdapter<DishType> adapter2 = new ArrayAdapter<DishType>(RecepyActivity.this, android.R.layout.simple_spinner_item, types);
		
		((ArrayAdapter<DishType>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_item);
		templateSpinner.setAdapter(adapter2);
		templateSpinner.setOnItemSelectedListener(spinnerListener);
		
	}



	
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (c != null)
			c.close();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (c != null)
			c.close();

	}
	
	private OnClickListener addTodayDishListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(RecepyActivity.NAME, date);
			intent.putExtra(RecepyActivity.ID, id);
			intent.putExtra(NewTemplateActivity.TEMPLATE, true);
			intent.putExtra(PARENT_NAME, "Recepy");
			intent.setClass(getParent(), DishListActivity.class);			
			DishListActivityGroup activityStack = (DishListActivityGroup) getParent();
			activityStack.push("DishListActivityGroup", intent);

		}
	};

	private OnClickListener okButtonListener = new OnClickListener() {
		
		public void onClick(View v) {
			int realw = Integer.valueOf(realWeight.getText().toString());
			if(realw <= 0){
				realw = 1;
			}
			TextView tv = (TextView) findViewById(R.id.textViewTotalValue);			
			TextView tvF = (TextView) findViewById(R.id.textViewFatTotal);			
			TextView tvC = (TextView) findViewById(R.id.textViewCarbonTotal);			
			TextView tvP = (TextView) findViewById(R.id.textViewProteinTotal);
			tv.setText(String.valueOf(sum*100/realw));
			DecimalFormat df = new DecimalFormat("###.#");
			tvF.setText(df.format(Float.valueOf(sumF*100/realw)));
			tvC.setText(df.format(Float.valueOf(sumC*100/realw)));
			tvP.setText(df.format(Float.valueOf(sumP*100/realw)));
			Dish dish = DishListHelper.getDishById(id, RecepyActivity.this);
			dish.setCaloricity((int)(sum*100/realw));
			dish.setDescription(DishProvider.ISRECEPY + "_" + realWeight.getText().toString());		
			dish.setCarbonStr(df.format(Float.valueOf(sumC*100/realw)).replace(',', '.'));
			dish.setProteinStr(df.format(Float.valueOf(sumP*100/realw)).replace(',', '.'));
			dish.setFatStr(df.format(Float.valueOf(sumF*100/realw)).replace(',', '.'));
			DishListHelper.updateDish(dish, RecepyActivity.this);
			
		}
	};
private OnClickListener saveListener = new OnClickListener() {
		
		public void onClick(View v) {			
			showDialog(DIALOG_TEMPLATE_NAME);
		}
	};
	
	private OnClickListener loadListener = new OnClickListener() {
		public void onClick(View v) {			
			templateSpinner.setSelection(0);
			flagLoad = true;
			templateSpinner.performClick();
		}
	};
	private int weight;
	

	private void initDishTable(){
		weight = 0;
		if (c.getCount() > 0) {
			c.moveToFirst();
			if (c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)) != null) {
				weight = weight + Integer
						.parseInt(c.getString(c
								.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT)));

				sum = sum
						+ Integer
								.parseInt(c.getString(c
										.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)));

				sumF = sumF
						+ c.getFloat(c
								.getColumnIndex(DishProvider.TODAY_DISH_FAT));
				sumC = sumC
						+ c.getFloat(c
								.getColumnIndex(DishProvider.TODAY_DISH_CARBON));
				sumP = sumP
						+ c.getFloat(c
								.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN));
			}
			while (c.moveToNext()) {
				weight = weight + Integer
						.parseInt(c.getString(c
								.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT)));
				
				sum = sum
						+ Integer
								.parseInt(c.getString(c
										.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)));
				sumF = sumF
						+ c.getFloat(c
								.getColumnIndex(DishProvider.TODAY_DISH_FAT));
				sumC = sumC
						+ c.getFloat(c
								.getColumnIndex(DishProvider.TODAY_DISH_CARBON));
				sumP = sumP
						+ c.getFloat(c
								.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN));
			}
			Dish dish = DishListHelper.getDishById(id, RecepyActivity.this);
			String[] rec_meta = dish.getDescription().split("_");
			if(rec_meta.length==3){
				realWeight.setText(rec_meta[2]);
			}else{
				realWeight.setText("0");
			}
			TextView tv = (TextView) findViewById(R.id.textViewTotalValue);		
			TextView tW = (TextView) findViewById(R.id.textViewTotalWeight);	
			TextView tvF = (TextView) findViewById(R.id.textViewFatTotal);			
			TextView tvC = (TextView) findViewById(R.id.textViewCarbonTotal);			
			TextView tvP = (TextView) findViewById(R.id.textViewProteinTotal);
			tW.setText(String.valueOf(weight));
			int realw = Integer.valueOf(realWeight.getText().toString());
			if(realw == 0){
				realWeight.setText(String.valueOf(weight));
				realw = weight;
			}
			tv.setText(String.valueOf(sum*100/realw));
			DecimalFormat df = new DecimalFormat("###.#");
			tvF.setText(df.format(Float.valueOf(sumF*100/realw)));
			tvC.setText(df.format(Float.valueOf(sumC*100/realw)));
			tvP.setText(df.format(Float.valueOf(sumP*100/realw)));
			
			dish.setCaloricity((int)(sum*100/realw));		
			dish.setCarbonStr(df.format(Float.valueOf(sumC*100/realw)).replace(',', '.'));
			dish.setProteinStr(df.format(Float.valueOf(sumP*100/realw)).replace(',', '.'));
			dish.setFatStr(df.format(Float.valueOf(sumF*100/realw)).replace(',', '.'));
			DishListHelper.updateDish(dish, RecepyActivity.this);
		} else {
			LinearLayout emptyHeader = (LinearLayout)findViewById(R.id.linearLayoutEmptyListHeader);
			emptyHeader.setVisibility(View.VISIBLE);
			emptyHeader.setOnClickListener(addTodayDishListener);
			TextView tv = (TextView) findViewById(R.id.textViewTotalValue);
			TextView tvF = (TextView) findViewById(R.id.textViewFatTotal);
			TextView tvC = (TextView) findViewById(R.id.textViewCarbonTotal);
			TextView tvP = (TextView) findViewById(R.id.textViewProteinTotal);
			tv.setText(String.valueOf(0) + getString(R.string.kcal));
			DecimalFormat df = new DecimalFormat("###.#");
			tvF.setText(df.format(Float.valueOf(sumF)));
			tvC.setText(df.format(Float.valueOf(sumC)));
			tvP.setText(df.format(Float.valueOf(sumP)));
		}
		
	}
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(flagLoad){
				flagLoad=false;
				AlertDialog.Builder builder = null;
				
				builder = new AlertDialog.Builder(
						RecepyActivity.this.getParent());
				

				builder.setMessage(R.string.template_download)
						.setPositiveButton(RecepyActivity.this.getString(R.string.yes),
								dialogClickListener)
						.setNegativeButton(RecepyActivity.this.getString(R.string.no),
								dialogClickListener).show();
				
			
			}

			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					LinearLayout emptyHeader = (LinearLayout)findViewById(R.id.linearLayoutEmptyListHeader);
					emptyHeader.setVisibility(View.GONE);
					Cursor cTemp=null;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"EEE dd MMMM",new Locale( SaveUtils.getLang(RecepyActivity.this)));
					Date curentDateandTimeLong = null;			
					curentDateandTimeLong = new Date();					
					try{
					cTemp = TemplateDishHelper.getDishesByDate( RecepyActivity.this, ((DishType) templateSpinner.getSelectedItem()).getDescription());
					if(cTemp.getCount()>0){						
						TemplateDishHelper.removeDishesByDay(curentDateandTime, RecepyActivity.this);
					}else{
						Toast.makeText(RecepyActivity.this, getString(R.string.loadtemplateempty),
								Toast.LENGTH_LONG).show();
					}
					while (cTemp.moveToNext())
			        {	
						try{
							TemplateDishHelper.addNewDish(new TodayDish(
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_ID)),
									cTemp.getString(cTemp
											.getColumnIndex(DishProvider.TODAY_NAME)),
									cTemp.getString(cTemp
											.getColumnIndex(DishProvider.TODAY_DESCRIPTION)),
									cTemp.getInt(cTemp
											.getColumnIndex(DishProvider.TODAY_CALORICITY)),
									cTemp.getString(cTemp
											.getColumnIndex(DishProvider.TODAY_CATEGORY)),
									cTemp.getInt(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT)),
									cTemp.getInt(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)),
									curentDateandTime,
									curentDateandTimeLong.getTime(),
									cTemp.getInt(cTemp
											.getColumnIndex(DishProvider.TODAY_IS_DAY)),
									cTemp.getString(cTemp
											.getColumnIndex(DishProvider.TODAY_TYPE)),
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_FAT)),
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_FAT)),
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_CARBON)),
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_CARBON)),
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_PROTEIN)),
									cTemp.getFloat(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN)),
									cTemp.getInt(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH)),
									cTemp.getInt(cTemp
											.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM))), RecepyActivity.this);					
						}catch (Exception e) {
							e.printStackTrace();
						}
			        }
					}catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(cTemp!= null){
							cTemp.close();
						}
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
				templateSpinner.setSelection(0);
				Toast.makeText(RecepyActivity.this, getString(R.string.loadtemplate),
						Toast.LENGTH_LONG).show();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				templateSpinner.setSelection(0);
				break;
			}
		}
	};
	private EditText templateName;
	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		Button buttonOk;
		Button nobutton;
		switch (id) {
		
		case DIALOG_TEMPLATE_NAME:
			dialog = new Dialog(this.getParent());
			dialog.setContentView(R.layout.recepy_name_dialog);
			dialog.setTitle(R.string.edit_dish);

			templateName = (EditText) dialog.findViewById(R.id.editTextUserName);
			templateName.setText(date);			
			TextView nametext = (TextView) dialog.findViewById(R.id.textViewNameLabel);
			nametext.setText(getText(R.string.dish_name));
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					if(templateName.getText().length()<1){
						templateName.setBackgroundColor(Color.RED);
					}else{
						
						try{
							Dish dish = DishListHelper.getDishById(RecepyActivity.id, RecepyActivity.this);
							dish.setName(templateName.getText().toString());
							DishListHelper.updateDish(dish, RecepyActivity.this);			        
							curentDateandTime = templateName.getText().toString();
							header.setText(templateName.getText().toString());
						}catch (Exception e) {
							e.printStackTrace();
						}						
						setAdapter();
						dialog.cancel();
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
