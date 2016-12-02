package bulat.diet.helper_sport.reciver;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.AddTodayDishActivity;
import bulat.diet.helper_sport.activity.StartActivity;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.utils.SaveUtils;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;

public class CaloryAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		
		Locale locale = new Locale(SaveUtils.getLang(context));
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews remoteViews;
		View viewToLoad = LayoutInflater.from(context).inflate(
				R.layout.widjet, null);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet);
		remoteViews.reapply(context, viewToLoad);
		ComponentName thiswidget = new ComponentName(context,
				CaloryAppWidgetProvider.class);
		
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

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thiswidget, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//
		//
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
				R.layout.widjet, null);
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widjet);
		remoteViews.reapply(context, viewToLoad);
		remoteViews.setOnClickPendingIntent(R.id.buttonAdd, pendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.buttonFitnes, pendingIntent);
		remoteViews.setTextViewText(R.id.widget_inlabel, context.getString(R.string.total));
		remoteViews.setTextViewText(R.id.widget_surpluslabel, context.getString(R.string.surplus));
		remoteViews.setTextViewText(R.id.widget_carbon_label, context.getString(R.string.widjet_carb));
		remoteViews.setTextViewText(R.id.widget_limitlabel, context.getString(R.string.limit));
		remoteViews.setTextViewText(R.id.widget_prot_label, context.getString(R.string.widjet_prot));
		remoteViews.setTextViewText(R.id.widget_fat_label, context.getString(R.string.widjet_fat));
		remoteViews.setTextViewText(R.id.widget_limit_kkal, context.getString(R.string.kcal));
		remoteViews.setTextViewText(R.id.widget_in_kkal, context.getString(R.string.kcal));
		remoteViews.setTextViewText(R.id.widget_surplus_kkal, context.getString(R.string.kcal));
		remoteViews.setTextViewText(R.id.widget_weight_label, context.getString(R.string.kgram));
		remoteViews.setTextViewText(R.id.widget_totalweight_label, context.getString(R.string.kgram));
		
		remoteViews.setTextViewText(R.id.widget_limit,
				String.valueOf(SaveUtils.getBMR(context)));
		super.onReceive(context, intent);
		ComponentName thiswidget = new ComponentName(context,
				CaloryAppWidgetProvider.class);
		
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
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(thiswidget, remoteViews);
	}
	public void checkLimit(int sum, float sumf, float sump,float sumc, RemoteViews remoteViews, Context context){
		int mode = SaveUtils.getMode(context);		
		List<Day> weights = TodayDishHelper.getDaysStat(context);
		Float dif = 0f;
		int  totalDif = 0;
		DecimalFormat df = new DecimalFormat("###.#");
		
		remoteViews.setTextViewText(R.id.widget_fat,
				df.format(sumf));
		remoteViews.setTextViewText(R.id.widget_prot,
				df.format(sump));
		remoteViews.setTextViewText(R.id.widget_carb,
				df.format(sumc));
		if(weights.size()>2){
			dif = Float.valueOf(weights.get(0).getBodyWeight() - weights.get(1).getBodyWeight());
			totalDif = (int)(weights.get(0).getBodyWeight() - weights.get(weights.size()-1).getBodyWeight());
		}
		
		
		
		if(dif > 0){
			remoteViews.setImageViewResource(R.id.widget_dif_img, R.drawable.dn);
		}else{
			remoteViews.setImageViewResource(R.id.widget_dif_img, R.drawable.up);
		}
		
		remoteViews.setTextViewText(R.id.widget_weight_dif, df.format(dif));
		String totalDifstr = "";
		if(totalDif>0){
			totalDifstr =" " + "+" + totalDif;
			totalDifstr.trim();
			if(mode==2){
				remoteViews.setTextColor(R.id.widget_totalweight_dif, context.getResources().getColor(R.color.widget_green));
			}else{
				remoteViews.setTextColor(R.id.widget_totalweight_dif, context.getResources().getColor(R.color.red));
			}
			
		}else if(totalDif<0){
			totalDifstr = "" + totalDif;
			if(mode==2){
				remoteViews.setTextColor(R.id.widget_totalweight_dif, context.getResources().getColor(R.color.red));
			}else{
				remoteViews.setTextColor(R.id.widget_totalweight_dif, context.getResources().getColor(R.color.widget_green));
			}			
		}
		remoteViews.setTextViewText(R.id.widget_totalweight_dif, totalDifstr);	
		int bmr = Integer.parseInt(SaveUtils.getBMR(context));
		int meta = Integer.parseInt(SaveUtils.getMETA(context));
		switch (mode) {
		case 0:
			if(sum > bmr){
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr, bmr, false);
				remoteViews.setImageViewResource(R.id.widget_man, R.drawable.fat_man);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				
				remoteViews.setTextColor(R.id.widget_in, context.getResources().getColor(R.color.red));
				remoteViews.setTextColor(R.id.widget_in_kkal,context.getResources().getColor(R.color.red));
				remoteViews.setTextColor(R.id.widget_inlabel, context.getResources().getColor(R.color.red));

			}else{
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr, sum, false);

				remoteViews.setImageViewResource(R.id.widget_man, R.drawable.halth_man);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				
				remoteViews.setTextColor(R.id.widget_in, Color.GREEN);
				remoteViews.setTextColor(R.id.widget_in_kkal, Color.GREEN);
				remoteViews.setTextColor(R.id.widget_inlabel, Color.GREEN);
				
			
			}	
			remoteViews.setTextViewText(R.id.surplus_limit, String.valueOf(bmr - sum));
			break;
		case 1:
			if(sum >meta){
				remoteViews.setProgressBar(R.id.progressBarWidget, meta, meta, false);
				remoteViews.setImageViewResource(R.id.widget_man, R.drawable.fat_man);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getMETA(context))) );
				
				remoteViews.setTextColor(R.id.widget_in, context.getResources().getColor(R.color.red));
				remoteViews.setTextColor(R.id.widget_in_kkal, context.getResources().getColor(R.color.red));
				remoteViews.setTextColor(R.id.widget_inlabel, context.getResources().getColor(R.color.red));
				
			}else{
				remoteViews.setProgressBar(R.id.progressBarWidget, meta, sum, false);
				remoteViews.setImageViewResource(R.id.widget_man, R.drawable.halth_man);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getMETA(context))) );
				
				remoteViews.setTextColor(R.id.widget_in, Color.GREEN);
				remoteViews.setTextColor(R.id.widget_in_kkal, Color.GREEN);
				remoteViews.setTextColor(R.id.widget_inlabel, Color.GREEN);
			}	
			remoteViews.setTextViewText(R.id.surplus_limit, String.valueOf(meta - sum));
			break;
		case 2:			
			if(sum < meta){
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr, sum, false);
				remoteViews.setImageViewResource(R.id.widget_man, R.drawable.thin_man);	
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
				
				remoteViews.setTextColor(R.id.widget_in, context.getResources().getColor(R.color.red));
				remoteViews.setTextColor(R.id.widget_in_kkal, context.getResources().getColor(R.color.red));
				remoteViews.setTextColor(R.id.widget_inlabel, context.getResources().getColor(R.color.red));
			
			}else{
				remoteViews.setProgressBar(R.id.progressBarWidget, bmr,  bmr, false);
				remoteViews.setImageViewResource(R.id.widget_man, R.drawable.halth_man);
				remoteViews.setTextViewText(R.id.widget_limit,
						String.valueOf(Integer.valueOf(SaveUtils.getBMR(context))) );
			
				remoteViews.setTextColor(R.id.widget_in, Color.GREEN);
				remoteViews.setTextColor(R.id.widget_in_kkal, Color.GREEN);
				remoteViews.setTextColor(R.id.widget_inlabel, Color.GREEN);
			
			}
			remoteViews.setTextViewText(R.id.surplus_limit, String.valueOf(meta - sum));
			break;
		default:
			break;
		}
		
	}
}
