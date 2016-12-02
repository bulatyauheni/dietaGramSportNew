package bulat.diet.helper_sport.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.DaysAdapter;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.ServerUpdater;

public class CalendarActivity extends BaseActivity {
	protected static final int DIALOG_CHART = 0;
	protected static final int DIALOG_WEIGHT = 1;
	TextView header;
	ListView daysList;
	Cursor c;
	int sum;
	private TextView avgFatView;
	private TextView avgProteinView;
	private TextView avgCarbonView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//TodayDish td = new TodayDish("", "", 12, "", 0, 0, "",0, 0, "");
		//td.setServerId(11);
		//TodayDishHelper.addNewDish(td,this);
		TodayDishHelper.removeSinchDish(this);
		//new ServerUpdater(CalendarActivity.this).execute(); 	
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.days_list, null);
		header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);

		Button templateTab = (Button) viewToLoad.findViewById(R.id.templateTab);
		templateTab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent = new Intent();
					intent.setClass(getParent(), TemplateActivity.class);
					CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
					activityStack.push("TemplateActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


		Button resetButton = (Button) viewToLoad.findViewById(R.id.buttonReset);
		resetButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = null;

				builder = new AlertDialog.Builder(CalendarActivity.this
						.getParent());
				builder.setMessage(R.string.reset_dialog)
						.setPositiveButton(getString(R.string.yes),
								dialogClickListener)
						.setNegativeButton(getString(R.string.no),
								dialogClickListener).show();
			}
		});
		Button chartButton = (Button) viewToLoad.findViewById(R.id.buttonChart);
		chartButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showDialog(DIALOG_CHART);
				Intent intent = new Intent();
				intent.setClass(getParent(), WeightChartActivity.class);
				CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
				activityStack.push("WeightChartActivity", intent);
			}
		});
		setContentView(viewToLoad);
		LinearLayout graphHeader = (LinearLayout) findViewById(R.id.linearLayoutChart);
		graphHeader.setOnClickListener(showChartListener);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (c != null)
			c.close();
	}

	
	
	private ArrayList<DishType> getChartTypes(CalendarActivity calendarActivity) {
		ArrayList<DishType> types = new ArrayList<DishType>();
		types.add(new DishType(1,
				getString(R.string.change_weight_dialog_title)));
		if (SaveUtils.getChestEnbl(CalendarActivity.this))
			types.add(new DishType(2, getString(R.string.volume_chest)));
		if (SaveUtils.getBicepsEnbl(CalendarActivity.this))
			types.add(new DishType(3, getString(R.string.volume_biceps)));
		if (SaveUtils.getForearmEnbl(CalendarActivity.this))
			types.add(new DishType(4, getString(R.string.volume_forearm)));
		if (SaveUtils.getHipEnbl(CalendarActivity.this))
			types.add(new DishType(5, getString(R.string.volume_hip)));
		if (SaveUtils.getNeckEnbl(CalendarActivity.this))
			types.add(new DishType(6, getString(R.string.volume_neck)));
		if (SaveUtils.getShinEnbl(CalendarActivity.this))
			types.add(new DishType(7, getString(R.string.volume_shin)));
		if (SaveUtils.getPelvisEnbl(CalendarActivity.this))
			types.add(new DishType(8, getString(R.string.volume_pelvis)));
		if (SaveUtils.getWaistEnbl(CalendarActivity.this))
			types.add(new DishType(9, getString(R.string.volume_waist)));
		return types;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (c != null)
			c.close();
	}

	DaysAdapter da;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// TODO Auto-generated method stub
		avgCalorisityView = (TextView) findViewById(R.id.textViewAVGValue);
		avgFatView = (TextView) findViewById(R.id.textViewFat);
		avgProteinView = (TextView) findViewById(R.id.textViewProtein);
		avgCarbonView = (TextView) findViewById(R.id.textViewCarbon);
		String[] avgVals = TodayDishHelper.getAvgDishCalorisity(this);
		TextView tvFp = (TextView) findViewById(R.id.textViewFatPercent);
		TextView tvCp = (TextView) findViewById(R.id.textViewCarbonPercent);
		TextView tvPp = (TextView) findViewById(R.id.textViewProteinPercent);

		avgCalorisityView.setText(avgVals[0] + " " + getString(R.string.kcal));

		avgFatView.setText(avgVals[1]);
		avgCarbonView.setText(avgVals[2]);
		avgProteinView.setText(avgVals[3]);

		DecimalFormat df = new DecimalFormat("###.#");

		float sum = Float.valueOf(avgVals[1].replace(",", ".")) + Float.valueOf(avgVals[2].replace(",", "."))
				+ Float.valueOf(avgVals[3].replace(",", "."));
		tvFp.setText("(" + df.format(Float.valueOf(avgVals[1].replace(",", ".")) * 100 / sum)
				+ "%)");
		tvCp.setText("(" + df.format(Float.valueOf(avgVals[2].replace(",", ".")) * 100 / sum)
				+ "%)");
		tvPp.setText("(" + df.format(Float.valueOf(avgVals[3].replace(",", ".")) * 100 / sum)
				+ "%)");

		checkLimit(Integer.valueOf(avgVals[0]));
		header.setText(getString(R.string.tab_calendar_header));
		c = TodayDishHelper.getDaysNew(getApplicationContext());
		if (c != null) {
			try {
				int customLimit = SaveUtils.getCustomLimit(this);
				if (customLimit > 0) {
					SaveUtils.saveBMR(String.valueOf(customLimit), this);
					SaveUtils.saveMETA(String.valueOf(customLimit), this);
				}
				da = new DaysAdapter(this, getApplicationContext(), c,
						(CalendarActivityGroup) getParent());
				// TodayDishAdapter da = new
				// TodayDishAdapter(getApplicationContext(),
				// c,(DishActivityGroup) getParent());
				daysList = (ListView) findViewById(R.id.listViewDays);
				daysList.setAdapter(da);
				daysList.setItemsCanFocus(true);

				daysList.setOnItemClickListener(daysListOnItemClickListener);

				/*
				 * if (c.getCount() > 0){ c.moveToFirst(); sum = sum +
				 * Integer.parseInt
				 * (c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY
				 * ))); while(c.moveToNext()){
				 * 
				 * sum = sum +
				 * Integer.parseInt(c.getString(c.getColumnIndex(DishProvider
				 * .TODAY_DISH_CALORICITY))); } TextView tv = (TextView)
				 * findViewById(R.id.textViewTotalValue);
				 * tv.setText(String.valueOf(sum)); }else{ TextView tv =
				 * (TextView) findViewById(R.id.textViewTotalValue);
				 * tv.setText(String.valueOf(0)); }
				 */
			} catch (Exception e) {
				if (c != null)
					c.close();
			} finally {
				// c.close();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (c != null)
			c.close();
	}

	private OnItemClickListener daysListOnItemClickListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
			// switchTabInActivity(1)
			Intent intent = new Intent();
			TextView day = (TextView) v.findViewById(R.id.textViewDay);

			intent.putExtra(AddTodayDishActivity.TITLE,
					getString(R.string.edit_today_dish));
			intent.putExtra(DishActivity.DATE, day.getTag().toString());
			intent.putExtra(DishActivity.BACKBTN, true);
			intent.putExtra(DishActivity.PARENT_NAME,
					CalendarActivityGroup.class.toString());
			intent.setClass(getParent(), DishActivity.class);
			CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
			activityStack.push("DishDayActivity", intent);
		}
	};
	private TextView avgCalorisityView;

	public void switchTabInActivity(int indexTabToSwitchTo) {
		DietHelperActivity parentActivity;
		parentActivity = (DietHelperActivity) this.getParent();
		parentActivity.switchTab(indexTabToSwitchTo);
	}

	public void checkLimit(int sum) {
		int mode = SaveUtils.getMode(this);
		int customLimit = SaveUtils.getCustomLimit(this);
		if (customLimit > 0) {
			SaveUtils.saveBMR(String.valueOf(customLimit), this);
			SaveUtils.saveMETA(String.valueOf(customLimit), this);
		}
		try {
			switch (mode) {
			case 0:
				if (sum > Integer.parseInt(SaveUtils.getBMR(this))) {
					LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
					totalLayout.setBackgroundResource(R.color.light_red);
				} else {
					LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
					totalLayout.setBackgroundResource(R.color.light_green);
				}

				break;
			case 1:
				if (sum > Integer.parseInt(SaveUtils.getMETA(this))) {
					LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
					totalLayout.setBackgroundResource(R.color.light_red);
				} else {
					LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
					totalLayout.setBackgroundResource(R.color.light_green);
				}
				break;
			case 2:
				if (sum < Integer.parseInt(SaveUtils.getMETA(this))) {
					LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
					totalLayout.setBackgroundResource(R.color.light_red);

				} else {
					LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
					totalLayout.setBackgroundResource(R.color.light_green);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener showChartListener = new OnClickListener() {

		public void onClick(View v) {
			showDialog(DIALOG_CHART);
		}
	};
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					TodayDishHelper.clearAll(CalendarActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	public void resume() {
		c.requery();
		da.notifyDataSetChanged();
	}
}
