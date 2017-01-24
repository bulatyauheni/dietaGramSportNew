package bulat.diet.helper_sport.reciver;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.AddTodayDishActivity;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.StartActivity;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RemoteViews;

public class CaloryAppBigWidgetProvider extends AppWidgetProvider {
	protected static final String Fat = "f";
	protected static final String Carbon = "c";
	protected static final String Protein = "p";
	protected String[] mParties;
	DecimalFormat df = new DecimalFormat("###.#");
	protected float valuesCustom[] = { 0, 0, 0 };
	protected float valuesNormal[] = { 1, 4, 1 };
	protected float valuesFith[] = { 1, 5, 1 };
	protected float valuesBrain[] = { 1, 3, (float) 0.8 };
	private float values[] = { 0, 0, 0 };
	int limit = 0;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		mParties = new String[] { context.getString(R.string.protein),
				context.getString(R.string.carbon), context.getString(R.string.fat) };
		Locale locale = new Locale(SaveUtils.getLang(context));
		Locale.setDefault(locale);
		GATraker.initialize(context.getApplicationContext());
		GATraker.sendEvent("WIDGET", "BIG_WIDGET_UPDATE");
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews remoteViews;
		View viewToLoad = LayoutInflater.from(context).inflate(
				R.layout.widjet_xxl, null);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet_xxl);
		remoteViews.reapply(context, viewToLoad);
		ComponentName thiswidget = new ComponentName(context,
				CaloryAppBigWidgetProvider.class);
		
		Intent intent2 = new Intent(context, StartActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
		// Get the layout for the App Widget and attach an on-click listener to the button
		
		remoteViews.setOnClickPendingIntent(R.id.buttonAdd, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.buttonFitnes, pendingIntent);	
		
		remoteViews.setTextViewText(R.id.widget_limit,
				String.valueOf(SaveUtils.getBMR(context)));
		int sum = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
				 SaveUtils.getLang(context)));
		String curentDateandTime = sdf.format(new Date());

		Cursor c = TodayDishHelper.getDishesByDate(context, curentDateandTime);
		if (c.getCount() > 0) {
			c.moveToFirst();
			if (c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)) != null) {
				sum = sum
						+ Integer
								.parseInt(c.getString(c
										.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)));
			}
			while (c.moveToNext()) {

				sum = sum
						+ Integer
								.parseInt(c.getString(c
										.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)));
			}
			remoteViews.setTextViewText(R.id.widget_in, String.valueOf(sum));
		} else {
			remoteViews.setTextViewText(R.id.widget_in, String.valueOf(0));
		}

				PieChart chart = initChart(context);

				chart.measure(View.MeasureSpec.makeMeasureSpec(convertDpToPixel(200),View.MeasureSpec.EXACTLY),
				              View.MeasureSpec.makeMeasureSpec(500,View.MeasureSpec.EXACTLY));
				chart.layout(0, 0, chart.getMeasuredWidth(), chart.getMeasuredHeight());

				Bitmap chartBitmap = chart.getChartBitmap();
				setBitmap(remoteViews, R.id.graph , chartBitmap);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thiswidget, remoteViews);
	}
	public static int convertDpToPixel(float dp){
	    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return Math.round(px);
    }
	protected PieChart initChart(Context context) {
		PieChart mChart = new PieChart(context);
		// TODO Auto-generated method stub
				mChart.setUsePercentValues(true);
				mChart.setDescription("");
				mChart.setExtraOffsets(5, 10, 5, 5);

				mChart.setDragDecelerationFrictionCoef(0.95f);
				mChart.setCenterText("");
				mChart.setBackgroundColor(Color.TRANSPARENT);
				mChart.setExtraOffsets(10.f, 0.f, 10.f, 0.f);

				mChart.setDrawHoleEnabled(true);
				mChart.setHoleColor(Color.TRANSPARENT);

				mChart.setTransparentCircleColor(Color.WHITE);
				mChart.setTransparentCircleAlpha(110);

				mChart.setHoleRadius(49f);
				mChart.setTransparentCircleRadius(53f);
			//	mChart.set
				mChart.setDrawCenterText(true);
				mChart.setCenterTextColor(Color.WHITE);
				mChart.setRotationAngle(0);
				// enable rotation of the chart by touch
				mChart.setRotationEnabled(true);
				mChart.setHighlightPerTapEnabled(true);


				mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
				//mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
				// mChart.spin(2000, 0, 360);
				Legend l = mChart.getLegend();
				l.setPosition(LegendPosition.RIGHT_OF_CHART);
				l.setEnabled(false);
				setData(mChart, 3, getValues(TodayDishHelper.getStatisticFCP(context, 1)));
				return mChart;
	}
	
	private float[] getValues(Map<String, Float> statistic) {
		float res[] = { 0, 0, 0 };
		if (statistic == null) {
			return res;
		}
		try {
			if (statistic.get(Fat) != null) {
				res[0] = statistic.get(Protein);
			}
			if (statistic.get(Carbon) != null) {
				res[1] = statistic.get(Carbon);
			}
			if (statistic.get(Protein) != null) {
				res[2] = statistic.get(Fat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	
	private void setData(PieChart mChart, int count, float[] data) {

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		for (int i = 0; i < count; i++) {
			yVals1.add(new Entry(data[i], i));
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count + 1; i++)
			xVals.add(mParties[i % mParties.length]);

		PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
		dataSet.setSliceSpace(3f);
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		//for (int c : ColorTemplate.VORDIPLOM_COLORS)
		//	colors.add(c);

		//for (int c : ColorTemplate.JOYFUL_COLORS)
		//	colors.add(c);

		//for (int c : ColorTemplate.COLORFUL_COLORS)
		//	colors.add(c);

		for (int c : ColorTemplate.LIBERTY_COLORS)
			colors.add(c);

		//for (int c : ColorTemplate.PASTEL_COLORS)
		//	colors.add(c);

		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		// dataSet.setSelectionShift(0f);

		 dataSet.setValueLinePart1OffsetPercentage(80.f);
		 dataSet.setValueLinePart1Length(0.2f);
		 dataSet.setValueLinePart2Length(0.4f);

		PieData pieData = new PieData(xVals, dataSet);
		pieData.setValueTextColor(Color.WHITE);
		pieData.setValueFormatter(new PercentFormatter());
		pieData.setValueTextSize(11f);
		mChart.setData(pieData);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

	private void setBitmap(RemoteViews views, int resId, Bitmap bitmap){
	    Bitmap proxy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(proxy);
	    c.drawBitmap(bitmap, new Matrix(), null);
	    views.setImageViewBitmap(resId, proxy);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//
		//
		mParties = new String[] { context.getString(R.string.protein),
				context.getString(R.string.carbon), context.getString(R.string.fat) };
		int customLimit = SaveUtils.getCustomLimit(context);
		if(customLimit>0){
			SaveUtils.saveBMR(String.valueOf(customLimit), context);
			SaveUtils.saveMETA(String.valueOf(customLimit), context);
		}
		Locale locale = new Locale(SaveUtils.getLang(context));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
		Intent intent2 = new Intent(context, StartActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
		// Get the layout for the App Widget and attach an on-click listener to the button
		RemoteViews remoteViews;
		View viewToLoad = LayoutInflater.from(context).inflate(
				R.layout.widjet_xxl, null);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet_xxl);
		remoteViews.reapply(context, viewToLoad);
		remoteViews.setOnClickPendingIntent(R.id.buttonAdd, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.buttonFitnes, pendingIntent);
		remoteViews.setTextViewText(R.id.widget_inlabel, context.getString(R.string.header_in_label));
		remoteViews.setTextViewText(R.id.widget_surpluslabel, context.getString(R.string.need_to_consume));

		remoteViews.setTextViewText(R.id.widget_prot_norma_label, context.getString(R.string.norma));
		remoteViews.setTextViewText(R.id.widget_fat_norma_label, context.getString(R.string.norma));
		remoteViews.setTextViewText(R.id.widget_carbon_norma_label, context.getString(R.string.norma));

		remoteViews.setTextViewText(R.id.widget_prot_gramm, context.getString(R.string.gram));
		remoteViews.setTextViewText(R.id.widget_carb_gramm, context.getString(R.string.gram));
		remoteViews.setTextViewText(R.id.widget_fat_gramm, context.getString(R.string.gram));

		remoteViews.setTextViewText(R.id.widget_currentweightlabel, context.getString(R.string.weight_widget));
		remoteViews.setTextViewText(R.id.widget_currentweight_kg, context.getString(R.string.kgram));

		remoteViews.setTextViewText(R.id.widget_carbon_label, context.getString(R.string.carbon));
		remoteViews.setTextViewText(R.id.widget_limitlabel, context.getString(R.string.limit));
		remoteViews.setTextViewText(R.id.widget_prot_label, context.getString(R.string.protein));
		remoteViews.setTextViewText(R.id.widget_fat_label, context.getString(R.string.fat));
		remoteViews.setTextViewText(R.id.widget_limit_kkal, context.getString(R.string.kcal));
		remoteViews.setTextViewText(R.id.widget_in_kkal, context.getString(R.string.kcal));
		remoteViews.setTextViewText(R.id.widget_surplus_kkal, context.getString(R.string.kcal));
		remoteViews.setTextViewText(R.id.widget_weight_label, context.getString(R.string.kgram));
		remoteViews.setTextViewText(R.id.widget_totalweight_label, context.getString(R.string.kgram));
		remoteViews.setTextViewText(R.id.widget_currentweight, df.format(SaveUtils.getRealWeight(context)));
		remoteViews.setTextViewText(R.id.widget_limit,
				String.valueOf(SaveUtils.getBMR(context)));
		super.onReceive(context, intent);
		ComponentName thiswidget = new ComponentName(context,
				CaloryAppBigWidgetProvider.class);
		
		int sum = 0;
		float sumF = 0;
		float sumC= 0;
		float sumP= 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
				SaveUtils.getLang(context)));
		String curentDateandTime = sdf.format(new Date());

		Cursor c = TodayDishHelper.getDishesByDate(context, curentDateandTime);
		if(c!=null){
		if (c.getCount() > 0) {
			c.moveToFirst();
			if (c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY)) != null) {
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
		}
			remoteViews.setTextViewText(R.id.widget_in, String.valueOf(sum));
			
		} else {
			remoteViews.setTextViewText(R.id.widget_in, String.valueOf(0));
		}
		try{
			checkLimit(sum, sumF, sumP, sumC, remoteViews, context);
		}catch (NumberFormatException e) {
			e.printStackTrace();		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		initIdealFCP(context);
		remoteViews.setTextViewText(R.id.widget_prot_norma_value, "" + values[0]);
		remoteViews.setTextViewText(R.id.widget_carbon_norma_value, "" + values[1]);
		remoteViews.setTextViewText(R.id.widget_fat_norma_value, "" + values[2]);
		PieChart chart = initChart(context);

		chart.measure(View.MeasureSpec.makeMeasureSpec(convertDpToPixel(200),View.MeasureSpec.EXACTLY),
		              View.MeasureSpec.makeMeasureSpec(500,View.MeasureSpec.EXACTLY));
		chart.layout(0, 0, chart.getMeasuredWidth(), chart.getMeasuredHeight());

		Bitmap chartBitmap = chart.getChartBitmap();
		setBitmap(remoteViews, R.id.graph , chartBitmap);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thiswidget, remoteViews);
	}
	
	private void initIdealFCP(Context context) {
		int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, 0, context);
		if (limitKkal > 0) {
			int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, 0,
					context);
			int limitCarbon = SaveUtils
					.readInt(SaveUtils.LIMIT_CARBON, 0, context);
			int limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, 0, context);
			int sum = limitProtein + limitCarbon + limitFat;
			valuesCustom[0] = (float) limitProtein / sum;
			valuesCustom[1] = (float) limitCarbon / sum;
			valuesCustom[2] = (float) limitFat / sum;
		}
		
		int lifestyle = SaveUtils.getLifeStyle(context);

		switch (lifestyle) {
		case 0:
			values[0] = (int)(limit/29);
			values[1] = (int)(4 * limit/29);
			values[2] = (int)(limit/29);
			break;
		case 1:
			values[0] = (int)(limit/33);
			values[1] = (int)(5 * limit/33);
			values[2] = (int)(limit/33);
			break;
		case 2: 
			values[0] = (int)(limit/23.2);
			values[1] = (int)(3*limit/23.2);
			values[2] = (int)(0.8*limit/23.2);
			break;
		case 3:
			values = valuesCustom;
			break;
		default:
			values[0] = (int)(limit/29);
			values[1] = (int)(4 * limit/29);
			values[2] = (int)(limit/29);
			break;
		}
	}
	public void checkLimit(int sum, float sumf, float sump,float sumc, RemoteViews remoteViews, Context context){
		int mode = SaveUtils.getMode(context);		
		List<Day> weights = TodayDishHelper.getDaysStat(context);
		Float dif = 0f;
		float  totalDif = 0;
	
		remoteViews.setTextViewText(R.id.widget_fat,
				df.format(sumf));
		remoteViews.setTextViewText(R.id.widget_prot,
				df.format(sump));
		remoteViews.setTextViewText(R.id.widget_carb,
				df.format(sumc));
		if(weights.size()>2){
			dif = Float.valueOf(weights.get(0).getBodyWeight() - weights.get(1).getBodyWeight());
			totalDif = (weights.get(0).getBodyWeight() - weights.get(weights.size()-1).getBodyWeight());
		}
				
		if(dif > 0){
			remoteViews.setImageViewResource(R.id.widget_dif_img, R.drawable.dn);
		}else{
			remoteViews.setImageViewResource(R.id.widget_dif_img, R.drawable.up);
		}
		
		remoteViews.setTextViewText(R.id.widget_weight_dif, df.format(dif));
		String totalDifstr = "-0";
		if(totalDif>0){
			totalDifstr =" " + "+" + df.format(totalDif);
			totalDifstr.trim();
			
			
		}else if(totalDif<0){
			totalDifstr = "" + df.format(totalDif);
					
		}
		remoteViews.setTextViewText(R.id.widget_totalweight_dif, totalDifstr);	
		int bmr = Integer.parseInt(SaveUtils.getBMR(context));
		int meta = Integer.parseInt(SaveUtils.getMETA(context));
		
		if (0 == SaveUtils.getSex(context)) {
			remoteViews.setImageViewResource(R.id.widget_man, R.drawable.man_widget);
		} else {
			remoteViews.setImageViewResource(R.id.widget_man, R.drawable.woman_widget);
		}
		
		
		
		switch (mode) {
		case 0:
			if(sum > bmr){
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr, bmr, false);
				remoteViews.setInt(R.id.indicatorLayout, "setBackgroundResource", R.drawable.background_red);
				remoteViews.setTextViewText(R.id.widget_surpluslabel, context.getString(R.string.need_to_luse));
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				limit = Integer.valueOf(SaveUtils.getBMR(context));
				//remoteViews.setTextColor(R.id.widget_in, context.getResources().getColor(R.color.red));
				//remoteViews.setTextColor(R.id.widget_in_kkal,context.getResources().getColor(R.color.red));
				//remoteViews.setTextColor(R.id.widget_inlabel, context.getResources().getColor(R.color.red));

			}else{
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr, sum, false);
				remoteViews.setInt(R.id.indicatorLayout, "setBackgroundResource", R.drawable.background_green);			
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				limit = Integer.valueOf(SaveUtils.getBMR(context));
				remoteViews.setTextViewText(R.id.widget_surpluslabel, context.getString(R.string.need_to_consume));
				//remoteViews.setTextColor(R.id.widget_in, Color.GREEN);
				//remoteViews.setTextColor(R.id.widget_in_kkal, Color.GREEN);
				//remoteViews.setTextColor(R.id.widget_inlabel, Color.GREEN);
				
			
			}	
			remoteViews.setTextViewText(R.id.surplus_limit, String.valueOf(bmr - sum));
			break;
		case 1:
			if(sum >meta){
				remoteViews.setProgressBar(R.id.progressBarWidget, meta, meta, false);
				
				remoteViews.setInt(R.id.indicatorLayout, "setBackgroundResource", R.drawable.background_red);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getMETA(context))) );
				limit = Integer.valueOf(SaveUtils.getMETA(context));
				remoteViews.setTextViewText(R.id.widget_surpluslabel, context.getString(R.string.need_to_luse));
				//remoteViews.setTextColor(R.id.widget_in, context.getResources().getColor(R.color.red));
				//remoteViews.setTextColor(R.id.widget_in_kkal, context.getResources().getColor(R.color.red));
				//remoteViews.setTextColor(R.id.widget_inlabel, context.getResources().getColor(R.color.red));
				
			}else{
				remoteViews.setProgressBar(R.id.progressBarWidget, meta, sum, false);
				
				remoteViews.setTextViewText(R.id.widget_surpluslabel, context.getString(R.string.need_to_consume));
				remoteViews.setInt(R.id.indicatorLayout, "setBackgroundResource", R.drawable.background_green);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getMETA(context))) );
				limit = Integer.valueOf(SaveUtils.getMETA(context));
				//remoteViews.setTextColor(R.id.widget_in, Color.GREEN);
				//remoteViews.setTextColor(R.id.widget_in_kkal, Color.GREEN);
				//remoteViews.setTextColor(R.id.widget_inlabel, Color.GREEN);
			}	
			remoteViews.setTextViewText(R.id.surplus_limit, String.valueOf(meta - sum));
			break;
		case 2:			
			if(sum < meta){
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr, sum, false);
				remoteViews.setTextViewText(R.id.widget_limit,	String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				limit = Integer.valueOf(SaveUtils.getBMR(context));
				//remoteViews.setTextColor(R.id.widget_in, context.getResources().getColor(R.color.red));
				//remoteViews.setTextColor(R.id.widget_in_kkal, context.getResources().getColor(R.color.red));
				//remoteViews.setTextColor(R.id.widget_inlabel, context.getResources().getColor(R.color.red));
			
			}else{
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr,  bmr, false);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				limit = Integer.valueOf(SaveUtils.getBMR(context));
				//remoteViews.setTextColor(R.id.widget_in, Color.GREEN);
				//remoteViews.setTextColor(R.id.widget_in_kkal, Color.GREEN);
				//remoteViews.setTextColor(R.id.widget_inlabel, Color.GREEN);
			
			}
			remoteViews.setTextViewText(R.id.surplus_limit, String.valueOf(meta - sum));
			break;
		default:
			break;
		}
		
	}
}
