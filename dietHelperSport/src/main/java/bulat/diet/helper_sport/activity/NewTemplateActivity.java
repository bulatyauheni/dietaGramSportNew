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
import android.widget.AdapterView.OnItemLongClickListener;
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
import bulat.diet.helper_sport.adapter.TemplateDishAdapter;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TemplateDishHelper;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;

public class NewTemplateActivity extends BaseActivity {
	public static final String DATE = "date";
	public static final String PARENT_NAME = "parentname";
	public static final String BACKBTN = "backbtn";
	private static final int DIALOG_TEMPLATE_NAME = 0;
	public static final String TEMPLATE = "TEMPLATE";
	private Spinner templateSpinner;
	String curentDateandTime;
	ListView dishesList;
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
	protected Intent copyIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.new_template_list, null);
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);

		if (extras != null) {
			Boolean backb = extras.getBoolean(BACKBTN);
			if (backb) {
				Button backButton = (Button) viewToLoad
						.findViewById(R.id.buttonBack);
				backButton.setVisibility(View.VISIBLE);
				backButton.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						onBackPressed();
					}
				});
			}
		}

		Button addButton = (Button) viewToLoad.findViewById(R.id.buttonAdd);
		addButton.setOnClickListener(addTodayDishListener);
		Button fitesButton = (Button) viewToLoad.findViewById(R.id.buttonFitnes);
		fitesButton.setOnClickListener(addTodayFitnesListener);
		Button saveButton = (Button) viewToLoad.findViewById(R.id.savetemplate);
		saveButton.setOnClickListener(saveListener);
		Button loadButton = (Button) viewToLoad.findViewById(R.id.loadtemplate);
		loadButton.setOnClickListener(loadListener);
		
		String date = null;
		parentName = DishActivityGroup.class.toString();
		if (extras != null) {
			date = extras.getString(DATE);
			parentName = extras.getString(PARENT_NAME);
		}
		if (date == null) {
			curentDateandTime = getString(R.string.new_template_header) + SaveUtils.getNextInt(this);
		} else {
			curentDateandTime = date;
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
		templateSpinner = (Spinner) findViewById(R.id.template_spinner);
		loadTemplates();
	
		
		header.setText(curentDateandTime);
		
		setAdapter();
	}

	

	private void setAdapter() {
		c = TemplateDishHelper.getDishesByDate(getApplicationContext(),
				curentDateandTime);
		//if its new or empty template then create fish for  template
		if(c.getCount() == 0){
			TemplateDishHelper.addNewDish(new TodayDish("", "", 0, "", 0, 0, curentDateandTime, (new Date()).getTime() ,1, ""), this);
		}
		if (c != null) {							
			try {
				TemplateDishAdapter da;
				if (CalendarActivityGroup.class.toString().equals(parentName)) {
					da = new TemplateDishAdapter(getApplicationContext(), c,
							(CalendarActivityGroup) getParent());
				} else {
					da = new TemplateDishAdapter(getApplicationContext(), c,
							(DishActivityGroup) getParent());
				}
				
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
				dishesList.setOnItemLongClickListener(new OnItemLongClickListener() {

		            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		                    int pos, long id2) {
		            	try{
		            	copyIntent = new Intent();						
		                TextView name = (TextView) arg1
								.findViewById(R.id.textViewDishName);
						TextView time = (TextView) arg1.findViewById(R.id.textViewTime);
						TextView timeHH = (TextView) arg1.findViewById(R.id.textViewTimeHH);
						TextView timeMM = (TextView) arg1.findViewById(R.id.textViewTimeMM);
						TextView fat = (TextView) arg1
								.findViewById(R.id.textViewFatTrue);
						TextView carbon = (TextView) arg1
								.findViewById(R.id.textViewCarbonTrue);
						TextView protein = (TextView) arg1
								.findViewById(R.id.textViewProteinTrue);
						TextView calorisity = (TextView) arg1
								.findViewById(R.id.textViewCaloricity);
						TextView absCalorisity = (TextView) arg1
								.findViewById(R.id.textViewAbsCaloricity);
						TextView fatAbsView = (TextView) arg1
								.findViewById(R.id.textViewFat);
						TextView weight = (TextView) arg1
								.findViewById(R.id.textViewWeight);
						TextView carbonAbsView = (TextView) arg1
								.findViewById(R.id.textViewCarbon);
						TextView id = (TextView) arg1.findViewById(R.id.textViewId);
						copyIntent.putExtra(AddTodayDishActivity.TITLE,
								R.string.add_today_dish);
						copyIntent.putExtra(NewTemplateActivity.TEMPLATE, true);
						copyIntent.putExtra(AddTodayFitnesActivity.ADD, 1);
						copyIntent.putExtra(DishActivity.DATE, curentDateandTime);
						copyIntent.putExtra(AddTodayDishActivity.DISH_NAME, name.getText().toString());
						copyIntent.putExtra(AddTodayDishActivity.DISH_CALORISITY,
								Integer.valueOf(calorisity.getText().toString()));
						copyIntent.putExtra(AddTodayDishActivity.DISH_FAT,
								fat.getText().toString());
						copyIntent.putExtra(AddTodayDishActivity.DISH_CARBON,
								carbon.getText().toString());
						copyIntent.putExtra(AddTodayDishActivity.DISH_PROTEIN,
								protein.getText().toString());
						copyIntent.putExtra(AddTodayDishActivity.DISH_ABSOLUTE_CALORISITY,
								Integer.valueOf(absCalorisity.getText().toString()));
						copyIntent.putExtra(AddTodayDishActivity.ID, id.getText());
						if (time.getText() != null) {
							copyIntent.putExtra(AddTodayDishActivity.DISH_TIME,
									time.getText());
						}
						if (timeHH.getText() != null) {
							copyIntent.putExtra(AddTodayDishActivity.DISH_TIME_HH,
									timeHH.getText());
						}
						if (timeMM.getText() != null) {
							copyIntent.putExtra(AddTodayDishActivity.DISH_TIME_MM,
									timeMM.getText());
						}
						if (Integer.valueOf(absCalorisity.getText().toString()) >= 0) {
							//jast > 0  couse 0 - is the sport category
							copyIntent.putExtra(AddTodayDishActivity.DISH_CATEGORY,	40);
							copyIntent.setClass(NewTemplateActivity.this, AddTodayDishActivity.class);
							copyIntent.putExtra(AddTodayDishActivity.DISH_WEIGHT,
									Integer.valueOf(weight.getText().toString()));
						} else {
							copyIntent.putExtra(AddFitnesActivity.FITNES_WEY,
									carbon.getText().toString());
							copyIntent.putExtra(AddFitnesActivity.FITNES_WEIGHT,
									fat.getText().toString());
							copyIntent.putExtra(AddFitnesActivity.FITNES_COUNT_SELECTION,
									Integer.valueOf(fatAbsView.getText().toString()));
							copyIntent.putExtra(AddFitnesActivity.FITNES_WEIGHT_SELECTION,
									Integer.valueOf(carbonAbsView.getText().toString()));
							copyIntent.putExtra(AddFitnesActivity.FITNES_WEIGHTDEC_SELECTION,
									Integer.valueOf(protein.getText().toString()));
							
							
							copyIntent.putExtra(AddTodayFitnesActivity.CALORYBURN,
									"0");
							copyIntent.setClass(NewTemplateActivity.this, AddTodayFitnesActivity.class);
						}

						copyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
		            	}catch (Exception e) {
							e.printStackTrace();
						}
						
		            	// TODO Auto-generated method stub		           
		            	//((TextView)arg1.findViewById(R.id.textViewDishName)).getText()
		            	
		            	AlertDialog.Builder builder = null;

						builder = new AlertDialog.Builder(
								NewTemplateActivity.this.getParent());
						
						builder.setMessage(R.string.copy_dialog)
								.setPositiveButton(NewTemplateActivity.this.getString(R.string.yes),
										new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										if (CalendarActivityGroup.class.toString().equals(parentName)) {
											CalendarActivityGroup activityStack = (CalendarActivityGroup)NewTemplateActivity.this.getParent();
											activityStack.push("AddTodayDishActivityCalendar", copyIntent);
										} else {
											DishActivityGroup activityStack = (DishActivityGroup)NewTemplateActivity.this.getParent();
											activityStack.push("AddTodayDishActivity", copyIntent);
										}	
									}})
								.setNegativeButton(NewTemplateActivity.this.getString(R.string.no),
										null).show();
						return true;		            							

						
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
		types.addAll(TemplateDishHelper.getDaysArray(NewTemplateActivity.this));		
		
		ArrayAdapter<DishType> adapter2 = new ArrayAdapter<DishType>(NewTemplateActivity.this, android.R.layout.simple_spinner_item, types);
		
		((ArrayAdapter<DishType>) adapter2).setDropDownViewResource(android.R.layout.simple_spinner_item);
		templateSpinner.setAdapter(adapter2);
		templateSpinner.setOnItemSelectedListener(spinnerListener);
		
	}



	/*public void checkLimit(int sum){
		int mode = SaveUtils.getMode(this);
		switch (mode) {
		case 0:
			if(sum > Integer.parseInt(SaveUtils.getBMR(NewTemplateActivity.this))){
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_red);
			}else{
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_green);
			}
			TextView tvLimit = (TextView) findViewById(R.id.textViewLimitValue);
			tvLimit.setText(String.valueOf(SaveUtils.getBMR(NewTemplateActivity.this)) + getString(R.string.kcal));
			break;
		case 1:
			if(sum > Integer.parseInt(SaveUtils.getMETA(NewTemplateActivity.this))){
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_red);
			}else{
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_green);
			}	
			TextView tvLimit2 = (TextView) findViewById(R.id.textViewLimitValue);
			tvLimit2.setText(String.valueOf(SaveUtils.getMETA(NewTemplateActivity.this)) + getString(R.string.kcal));
			break;
		case 2:
			TextView tvLimit3 = (TextView) findViewById(R.id.textViewLimitValue);
			tvLimit3.setText(String.valueOf(SaveUtils.getMETA(NewTemplateActivity.this)) + getString(R.string.kcal) );
			if(sum < Integer.parseInt(SaveUtils.getMETA(NewTemplateActivity.this))){
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_red);
								
			}else{
				LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutTotal);
				totalLayout.setBackgroundResource(R.color.light_green);
			}
			break;
		default:
			break;
		}
		
	}
	*/
	

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
			intent.putExtra(NewTemplateActivity.DATE, curentDateandTime);
			intent.putExtra(NewTemplateActivity.TEMPLATE, true);
			intent.putExtra(PARENT_NAME, parentName);
			intent.setClass(getParent(), DishListActivity.class);
			if (CalendarActivityGroup.class.toString().equals(parentName)) {
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("DishListActivityCalendar", intent);
			} else {
				DishActivityGroup activityStack = (DishActivityGroup) getParent();
				activityStack.push("DishListActivity", intent);
			}
		}
	};
	

	private OnClickListener addTodayFitnesListener = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(DishActivity.DATE, curentDateandTime);
			intent.putExtra(PARENT_NAME, parentName);
			intent.putExtra(AddTodayFitnesActivity.ADD, 1);
			intent.putExtra(NewTemplateActivity.TEMPLATE, true);
			intent.setClass(getParent(), SportListActivity.class);
			if (CalendarActivityGroup.class.toString().equals(parentName)) {
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("FitnesActivityCalendar", intent);
			} else {
				DishActivityGroup activityStack = (DishActivityGroup) getParent();
				activityStack.push("FitnesActivity", intent);
			}
			///
			/*Intent intent = new Intent();
			intent.putExtra(NewTemplateActivity.DATE, curentDateandTime);
			intent.putExtra(PARENT_NAME, parentName);
			intent.putExtra(AddTodayDishActivity.ADD, 1);
			intent.putExtra(NewTemplateActivity.TEMPLATE, true);
			intent.setClass(getParent(), AddTodayFitnesActivity.class);
			if (CalendarActivityGroup.class.toString().equals(parentName)) {
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("FitnesActivityCalendar", intent);
			} else {
				DishActivityGroup activityStack = (DishActivityGroup) getParent();
				activityStack.push("FitnesActivity", intent);
			}*/
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
	

	private void initDishTable(){
		if (c.getCount() > 0) {
			c.moveToFirst();
			if (c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)) != null) {
				int val = Integer.parseInt(c.getString(c
						.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)));
				if (val > 0) {
					sum = sum + val;
				}
				if (!c.getString(c.getColumnIndex(DishProvider.TODAY_CATEGORY))
						.equals("0")) {
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
			}
			while (c.moveToNext()) {
				int val = Integer.parseInt(c.getString(c
						.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)));
				if (val > 0) {
					sum = sum + val;
				}

				if (val >= 0) {
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
			}
			TextView tv = (TextView) findViewById(R.id.textViewTotalValue);			
			TextView tvF = (TextView) findViewById(R.id.textViewFatTotal);			
			TextView tvC = (TextView) findViewById(R.id.textViewCarbonTotal);			
			TextView tvP = (TextView) findViewById(R.id.textViewProteinTotal);
			tv.setText(String.valueOf(sum) + getString(R.string.kcal));
			DecimalFormat df = new DecimalFormat("###.#");
			tvF.setText(df.format(Float.valueOf(sumF)));
			tvC.setText(df.format(Float.valueOf(sumC)));
			tvP.setText(df.format(Float.valueOf(sumP)));
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
		
		checkLimit(sum);
	}
	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(flagLoad){
				flagLoad=false;
				AlertDialog.Builder builder = null;
				
				builder = new AlertDialog.Builder(
							NewTemplateActivity.this.getParent());
				

				builder.setMessage(R.string.template_download)
						.setPositiveButton(NewTemplateActivity.this.getString(R.string.yes),
								dialogClickListener)
						.setNegativeButton(NewTemplateActivity.this.getString(R.string.no),
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
							"EEE dd MMMM",new Locale( SaveUtils.getLang(NewTemplateActivity.this)));
					Date curentDateandTimeLong = null;			
					curentDateandTimeLong = new Date();					
					try{
					cTemp = TemplateDishHelper.getDishesByDate( NewTemplateActivity.this, ((DishType) templateSpinner.getSelectedItem()).getDescription());
					if(cTemp.getCount()>0){						
						TemplateDishHelper.removeDishesByDay(curentDateandTime, NewTemplateActivity.this);
					}else{
						Toast.makeText(NewTemplateActivity.this, getString(R.string.loadtemplateempty),
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
											.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM))), NewTemplateActivity.this);					
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
				Toast.makeText(NewTemplateActivity.this, getString(R.string.loadtemplate),
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
			dialog.setContentView(R.layout.user_name_dialog);
			dialog.setTitle(R.string.templates);

			templateName = (EditText) dialog.findViewById(R.id.editTextUserName);
			templateName.setText(curentDateandTime);
			TextView maintext = (TextView) dialog.findViewById(R.id.textViewSerchActivityLevelLabel);
			maintext.setText(getText(R.string.template_save));
			TextView nametext = (TextView) dialog.findViewById(R.id.textViewNameLabel);
			nametext.setText(getText(R.string.template_name));
			buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
			buttonOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					
					if(templateName.getText().length()<1){
						templateName.setBackgroundColor(Color.RED);
					}else{
						
						try{
							TemplateDishHelper.updateName(curentDateandTime, templateName.getText().toString(), NewTemplateActivity.this);			        
							curentDateandTime = templateName.getText().toString();
							header.setText(templateName.getText().toString());
						}catch (Exception e) {
							e.printStackTrace();
						}
						Toast.makeText(NewTemplateActivity.this, getString(R.string.savetemplate),
								Toast.LENGTH_LONG).show();
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
