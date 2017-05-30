package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.AddTodayDishActivity;
import bulat.diet.helper_sport.activity.CalendarActivity;
import bulat.diet.helper_sport.activity.CalendarActivityGroup;
import bulat.diet.helper_sport.activity.DishActivity;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.VolumeInfo;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;
import bulat.diet.helper_sport.utils.StringUtils;


public class DaysAdapter extends CursorAdapter {
	private static final String CALENDAR_WEIGHT_BUTTON_CLICK = "CALENDAR_WEIGHT_BUTTON_CLICK";
	public static final String WEIGHT_BTN = "WEIGHT_BTN";
	DecimalFormat df = new DecimalFormat("###.#");
	private Context ctx;
	private CalendarActivityGroup parent;
	CalendarActivity page;
	int height = 0;
	int age = 0;
	int sex = 0;
	int activity = 0;
	private String[] mParties;
	
	public DaysAdapter(CalendarActivity page, Context context, Cursor c,
			CalendarActivityGroup calendarActivityGroup) {
		super(context, c);
		mParties = new String[] { "","","" };
		this.page = page;
		ctx = context;
		height = SaveUtils.getHeight(ctx) + Info.MIN_HEIGHT;
		age = SaveUtils.getAge(ctx)+ Info.MIN_AGE;
		sex = SaveUtils.getSex(ctx);
		activity = SaveUtils.getActivity(ctx)+1;
		this.parent = calendarActivityGroup;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.days_list_row, parent, false);
		return v;
		// return inflater.inflate(R.layout.item_list_row, null);
	}

	@Override
	public int getCount() {
		if (super.getCount() != 0) {
			return super.getCount();
		}
		return 0;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		String dayDate = c.getString(c
				.getColumnIndex(DishProvider.TODAY_DISH_DATE));
		String itemCaloricity = c.getString(c.getColumnIndex("val"));
		String itemWoterWeight = c.getString(c.getColumnIndex("woterweight"));
		String itemWeight = c.getString(c.getColumnIndex("weight"));
		String itemBodyWeight = "";
		//if (Integer.parseInt(itemCaloricity) > 0) {

			try {
				if ((c.getInt(c.getColumnIndex("count")) - 1) > 0) {
					itemBodyWeight = String.valueOf(c.getFloat(c
							.getColumnIndex("bodyweight")));
					v.setBackgroundResource(R.color.main_color);
				} else {
					itemBodyWeight = String.valueOf(c.getFloat(c
							.getColumnIndex("bodyweight")));
					v.setBackgroundResource(R.color.main_color);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		//} else {
		//	itemBodyWeight = String.valueOf(SaveUtils.getWeight(context)
		//			+ Info.MIN_WEIGHT);
			//v.setBackgroundResource(R.color.light_broun);
		//}
		TextView dateTextView = (TextView) v.findViewById(R.id.textViewDay);

		dateTextView.setText(dayDate.split(" ")[1]);
		dateTextView.setTag(dayDate);
		SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEE", new Locale(
				SaveUtils.getLang(context)));
		SimpleDateFormat month = new SimpleDateFormat("MMM", new Locale(
				SaveUtils.getLang(context)));
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
				SaveUtils.getLang(context)));
		TextView monthTextView = (TextView) v.findViewById(R.id.textViewMonth);

		try {
			monthTextView.setText(month.format(sdf.parse(dayDate)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		TextView dayOfWeekTextView = (TextView) v.findViewById(R.id.textViewDayOFWeek);
		dayOfWeekTextView.setText(dayDate.split(" ")[0]);

		View iv = (View) v.findViewById(R.id.imageViewDay);
		int flag = checkLimit(Integer.parseInt(itemCaloricity), Float.valueOf(itemBodyWeight));
		if (0==flag) {
			iv.setBackgroundResource(R.color.today_green);
		} else if (1==flag){
			iv.setBackgroundResource(R.color.red);
		} else{
			iv.setBackgroundResource(R.color.red);
		}
		LinearLayout le = (LinearLayout) v.findViewById(R.id.layoutEmpty);
		LinearLayout lf = (LinearLayout) v.findViewById(R.id.layoutParams);
		if ("0".equals(itemWeight) && "0".equals(itemCaloricity)) {
			lf.setVisibility(View.GONE);
			le.setVisibility(View.VISIBLE);

		} else {
			lf.setVisibility(View.VISIBLE);
			le.setVisibility(View.GONE);

			TextView fatView = (TextView) v.findViewById(R.id.textViewFat);
			fatView.setText(c.getString(c.getColumnIndex("fat"))==null?"0":c.getString(c.getColumnIndex("fat")));

			TextView carbonView = (TextView) v
					.findViewById(R.id.textViewCarbon);
			carbonView.setText(c.getString(c.getColumnIndex("carbon"))==null?"0":c.getString(c.getColumnIndex("carbon")));

			TextView proteinView = (TextView) v
					.findViewById(R.id.textViewProtein);
			proteinView.setText(c.getString(c.getColumnIndex("protein"))==null?"0":c.getString(c.getColumnIndex("protein")));
			
			TextView tvFatPercent = (TextView) v
					.findViewById(R.id.textViewFatPercent);
			TextView tvCarbPercent = (TextView) v
					.findViewById(R.id.textViewCarbonPercent);
			TextView tvProtPercent = (TextView) v
					.findViewById(R.id.textViewProteinPercent);
			
			float protein=  Float.valueOf(c.getString(c.getColumnIndex("protein"))==null?"0":c.getString(c.getColumnIndex("protein")));
			float fat=  Float.valueOf(c.getString(c.getColumnIndex("fat"))==null?"0":c.getString(c.getColumnIndex("fat")));
			float carbon=  Float.valueOf(c.getString(c.getColumnIndex("carbon"))==null?"0":c.getString(c.getColumnIndex("carbon")));
			
			float sum = protein + fat+ carbon;
			
			tvFatPercent.setText("("+df.format(fat*100/sum)+"%)");
			tvCarbPercent.setText("("+df.format(carbon*100/sum)+"%)");
			tvProtPercent.setText("("+df.format(protein*100/sum)+"%)");

			if (sum > 0) {
				tvFatPercent.setText("(" + df.format(Float.valueOf(fat) * 100 / sum)
						+ "%)");
				tvCarbPercent.setText("(" + df.format(Float.valueOf(carbon) * 100 / sum)
						+ "%)");
				tvProtPercent.setText("(" + df.format(Float.valueOf(protein) * 100 / sum)
						+ "%)");
			} else {
				tvFatPercent.setText("(0%)");
				tvCarbPercent.setText("(0%)");
				tvProtPercent.setText("(0%)");
			}

			TextView waterweightView = (TextView)v.findViewById(R.id.textViewWoter);
		      waterweightView.setText(itemWoterWeight + " " + context.getString(R.string.gram));
			TextView caloricityView = (TextView) v
					.findViewById(R.id.textViewCaloricity);

			caloricityView.setText(itemCaloricity + " "
					+ context.getString(R.string.kcal));
			TextView weightView = (TextView) v
					.findViewById(R.id.textViewWeightTotal);

			weightView.setText(itemWeight + " "
					+ context.getString(R.string.gram));

			TextView bodyweightView = (TextView) v
					.findViewById(R.id.textViewWeightBodyTotal);
			
			

			float currWeight = SaveUtils.getRealWeight(ctx);

			if (itemBodyWeight == null) {
				itemBodyWeight = "" + currWeight;
			}
			bodyweightView.setText(itemBodyWeight + " "
					+ context.getString(R.string.kgram));
		}
		

		Button weightButton = (Button) v.findViewById(R.id.buttonWeight);
		if (weightButton != null) {
			weightButton.setId(c.getInt(c.getColumnIndex("_id")));
		}
		TextView tvi = (TextView) v.findViewById(R.id.textViewId);
		tvi.setText(c.getString(c
				.getColumnIndex(DishProvider.TODAY_DISH_DATE)));
		if (weightButton != null) {

			// weightButton.setId(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_DATE_LONG))));
			weightButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Button mbut = (Button) v;
					GATraker.sendEvent(WEIGHT_BTN, CALENDAR_WEIGHT_BUTTON_CLICK);
					final TextView tvi2 = (TextView) ((View) mbut.getParent())
							.findViewById(R.id.textViewId);
					final Dialog dialog = new Dialog(parent);
					dialog.setContentView(R.layout.update_weight_dialog);
					dialog.setTitle(R.string.change_weight_dialog_title);
					LinearLayout l1 = (LinearLayout) dialog.findViewById(R.id.linearLayoutForearm);
					if(SaveUtils.getForearmEnbl(ctx))l1.setVisibility(View.VISIBLE);
					LinearLayout l2 = (LinearLayout) dialog.findViewById(R.id.linearLayoutWaist);
					if(SaveUtils.getWaistEnbl(ctx))l2.setVisibility(View.VISIBLE);
					LinearLayout l3 = (LinearLayout) dialog.findViewById(R.id.linearLayoutChest);
					if(SaveUtils.getChestEnbl(ctx))l3.setVisibility(View.VISIBLE);
					LinearLayout l4 = (LinearLayout) dialog.findViewById(R.id.linearLayoutNeck);
					if(SaveUtils.getNeckEnbl(ctx))l4.setVisibility(View.VISIBLE);
					LinearLayout l5 = (LinearLayout) dialog.findViewById(R.id.linearLayoutShin);
					if(SaveUtils.getShinEnbl(ctx))l5.setVisibility(View.VISIBLE);
					LinearLayout l6 = (LinearLayout) dialog.findViewById(R.id.linearLayoutBiceps);
					if(SaveUtils.getBicepsEnbl(ctx))l6.setVisibility(View.VISIBLE);
					LinearLayout l7 = (LinearLayout) dialog.findViewById(R.id.linearLayoutPelvis);
					if(SaveUtils.getPelvisEnbl(ctx))l7.setVisibility(View.VISIBLE);
					LinearLayout l8 = (LinearLayout) dialog.findViewById(R.id.linearLayoutHip);
					if(SaveUtils.getHipEnbl(ctx))l8.setVisibility(View.VISIBLE);
					StringUtils.setSpinnerValues(dialog, ctx);
					final Spinner weightSpinner = (Spinner) dialog
							.findViewById(R.id.search_weight);
					final Spinner weightSpinnerDec = (Spinner) dialog
							.findViewById(R.id.search_weight_decimal);
					DialogUtils.setArraySpinnerValues(weightSpinner,
							Info.MIN_WEIGHT, Info.MAX_WEIGHT, ctx);
					DialogUtils.setArraySpinnerValues(weightSpinnerDec, 0, 10,
							ctx);
					weightSpinner.setSelection(SaveUtils.getWeight(ctx));
					weightSpinnerDec.setSelection(SaveUtils.getWeightDec(ctx));
					final Spinner chestSpinner = (Spinner) dialog.findViewById(R.id.SpinnerChest);
					final Spinner chestDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerChestDecimal);
					
					final Spinner pelvisSpinner = (Spinner) dialog.findViewById(R.id.SpinnerPelvis);
					final Spinner pelvisDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerPelvisDecimal);
					
					final Spinner neckSpinner = (Spinner) dialog.findViewById(R.id.SpinnerNeck);
					final Spinner neckDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerNeckDecimal);
					
					final Spinner bicepsSpinner = (Spinner) dialog.findViewById(R.id.SpinnerBiceps);
					final Spinner bicepsDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerBicepsDecimal);
					
					final Spinner forearmSpinner = (Spinner) dialog.findViewById(R.id.SpinnerForearm);
					final Spinner forearmDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerForearmDecimal);
					
					final Spinner waistSpinner = (Spinner) dialog.findViewById(R.id.SpinnerWaist);
					final Spinner waistDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerWaistDecimal);
					
					final Spinner hipSpinner = (Spinner) dialog.findViewById(R.id.SpinnerHip);
					final Spinner hipDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerHipDecimal);
					
					final Spinner shinSpinner = (Spinner) dialog.findViewById(R.id.SpinnerShin);
					final Spinner shinDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerShinDecimal);
					TextView buttonAdd = (TextView) dialog.findViewById(R.id.add_body_param_btn);
					buttonAdd.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent().setClass(ctx, VolumeInfo.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							ctx.startActivity(intent);
						}
					});
					Button buttonOk = (Button) dialog
							.findViewById(R.id.buttonYes);
					buttonOk.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {


								String lastDate = TodayDishHelper.getLastDate(ctx);

								if (tvi2.getText().toString().equals(lastDate)) {
									SaveUtils.saveWeight((int) weightSpinner
											.getSelectedItemId(), ctx);
									SaveUtils.saveWeightDec(
											(int) weightSpinnerDec
													.getSelectedItemId(), ctx);
									SaveUtils.saveChest((int)chestSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveChestDec((int)chestDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.savePelvis((int)pelvisSpinner.getSelectedItemId(),ctx);
									SaveUtils.savePelvisDec((int)pelvisDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.saveNeck((int)neckSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveNeckDec((int)neckDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.saveBiceps((int)bicepsSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveBicepsDec((int)bicepsDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.saveForearm((int)forearmSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveForearmDec((int)forearmDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.saveWaist((int)waistSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveWaistDec((int)waistDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.saveHip((int)hipSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveHipDec((int)hipDecSpinner.getSelectedItemId(),ctx);
									
									SaveUtils.saveShin((int)shinSpinner.getSelectedItemId(),ctx);
									SaveUtils.saveShinDec((int)shinDecSpinner.getSelectedItemId(),ctx);
									if (SaveUtils.getUserUnicId(ctx) != 0) {
										new SocialUpdater(ctx).execute();
									}
								}
								TodayDishHelper.updateBobyParams(
										ctx,
										tvi2.getText().toString(),
										String.valueOf(((float) weightSpinner
												.getSelectedItemId() + Info.MIN_WEIGHT)
												+ (float) weightSpinnerDec
														.getSelectedItemId()
												/ 10),
										new BodyParams(String.valueOf((float) chestSpinner.getSelectedItemId() +VolumeInfo.MIN_CHEST + (float) chestDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) bicepsSpinner.getSelectedItemId()+VolumeInfo.MIN_BICEPS + (float) bicepsDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) pelvisSpinner.getSelectedItemId()+VolumeInfo.MIN_PELVIS + (float) pelvisDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) neckSpinner.getSelectedItemId()+VolumeInfo.MIN_NECK + (float) neckDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) waistSpinner.getSelectedItemId()+VolumeInfo.MIN_WAIST + (float) waistDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) forearmSpinner.getSelectedItemId()+VolumeInfo.MIN_FOREARM + (float) forearmDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) hipSpinner.getSelectedItemId()+VolumeInfo.MIN_HIP + (float) hipDecSpinner.getSelectedItemId()/10), 
													String.valueOf((float) shinSpinner.getSelectedItemId()+VolumeInfo.MIN_SHIN + (float) shinDecSpinner.getSelectedItemId()/10)));
								dialog.cancel();
								page.resume();
							}
					});
					Button nobutton = (Button) dialog
							.findViewById(R.id.buttonNo);
					nobutton.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.cancel();
						}
					});
					dialog.show();
					
				}
			});
		}

	}

	
	public int checkLimit(int sum, float bodyweight){
		int BMR = getBMR(bodyweight);
		int META = getMeta(bodyweight);
		int mode = SaveUtils.getMode(ctx);
		try{
		switch (mode) {
		case 0:
			if(sum > BMR){
				return 1;
			}else{
				return 0;
			}			
		case 1:
			if(sum > META){
				return 1;
			}else{
				return 0;
			}				
		case 2:			
			if(sum < BMR){
				return 2;								
			}else{
				return 0;
			}
		default:
			return 0;
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;	
	}
	private int getMeta(float bobyweight){
		int sexValue = 0;
		if (sex == 0){
			sexValue = 166;
		}
		int BMR = (int)(9.99*(bobyweight) + 
				6.25*(height) - 
				4.92*(age) - 161 + 
				sexValue);
		BMR = (int)BMR;
		int META = 0;
		if(activity ==1){
			META = (int)(BMR*1.2);
					
		}else if(activity==2){
			META =(int)( BMR*1.35);
		}else if(activity==3){
			META = (int)(BMR*1.55);
		}else if(activity==4){
			META = (int)(BMR*1.75); 
		}else if(activity==5){
			META = (int)(BMR*1.92);
		}
		return META;			
	}
	private int getBMR(float bobyweight){
						
		return (int) (getMeta(bobyweight) * 0.8);
		
		
	}
	public int checkLimit(int sum) {
		int mode = SaveUtils.getMode(ctx);
		try {
			switch (mode) {
			case 0:
				if (sum > Integer.parseInt(SaveUtils.getBMR(ctx))) {
					return 1;
				} else {
					return 0;
				}
			case 1:
				if (sum > Integer.parseInt(SaveUtils.getMETA(ctx))) {
					return 1;
				} else {
					return 0;
				}
			case 2:
				if (sum < Integer.parseInt(SaveUtils.getMETA(ctx))) {
					return 2;
				} else {
					return 0;
				}
			default:
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
