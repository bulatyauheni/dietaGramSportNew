package bulat.diet.helper_sport.activity;

import java.util.Date;
import java.util.Locale;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.utils.SaveUtils;

public class DietHelperActivity extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;
	public static DietHelperActivity ctx;
	public static int dayscount;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 if(!SaveUtils.isFirstTime(DietHelperActivity.this)){ 
         	Locale locale = new Locale(SaveUtils.getLang(this));
 			Locale.setDefault(locale);
 			Configuration config = new Configuration();
 			config.locale = locale;
 			getBaseContext().getResources().updateConfiguration(config,
 			      getBaseContext().getResources().getDisplayMetrics());
         }
    	 super.onCreate(savedInstanceState);
    	 dayscount = TodayDishHelper.getDaysCount(this);
         setContentView(R.layout.main);
       
 		Resources res = getResources();
 		tabHost = getTabHost();
 		
 		TabHost.TabSpec spec;
 		Intent intent;
 		tabHost.setOnTabChangedListener(this);
 		intent = new Intent().setClass(this, DishActivityGroup.class);
 		//getString(R.string.tab_dish),res.getDrawable(R.drawable.today_selector)
 		
 		spec = tabHost.newTabSpec("today")
 				.setIndicator(getString(R.string.tab_dish),res.getDrawable(R.drawable.today_selector))
 				.setContent(intent);
 		tabHost.addTab(spec);

 		intent = new Intent().setClass(this, CalendarActivityGroup.class);
 		spec = tabHost
 				.newTabSpec("calendar")
 				.setIndicator(getString(R.string.tab_calendar),
 						res.getDrawable(R.drawable.calendar_selector))
 				.setContent(intent);
 		tabHost.addTab(spec);

 		intent = new Intent().setClass(this, DishListActivityGroup.class);
 		spec = tabHost.newTabSpec("list")
 				.setIndicator(getString(R.string.tab_dishlist),
 						res.getDrawable(R.drawable.list_selector)).setContent(intent);
 		tabHost.addTab(spec);

 		intent = new Intent().setClass(this, SocialActivityGroup.class);
 		spec = tabHost.newTabSpec("calc")
 				.setIndicator(getString(R.string.tab_calc),
 						res.getDrawable(R.drawable.calculator_selector)).setContent(intent);
 		tabHost.addTab(spec);
 		intent = new Intent().setClass(this, SettingsActivity.class);
 		spec = tabHost.newTabSpec("settings")
 				.setIndicator(getString(R.string.tab_setting),
 						res.getDrawable(R.drawable.settings_selector)).setContent(intent);
 		tabHost.addTab(spec);

 		if(!SaveUtils.isActivated(this)){
 			tabHost.setCurrentTab(4);
 		}else{
 			tabHost.setCurrentTab(0);
 		}
 		if(!validatePayment()){
 			tabHost.setCurrentTab(4);
		}
 		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
			tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.tab_color);
        } 
					
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.color.tab_selected_color);
		ctx = this;
		
		
    }
   
    private void setTab() {		
 		tabHost.setCurrentTab(4); 		
	}
    
	public void switchTab(int tab){
	        tabHost.setCurrentTab(tab);
	        
	        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
		    {
		        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
		        tv.setTextColor(Color.WHITE);
		    } 
	}
	
	public void changeSocialTabIndicator(int tab, String additionText){
	/*	TextView title = (TextView) tabHost.getTabWidget().getChildAt(tab).findViewById(android.R.id.title);
		if(!"0".equals(additionText)){
			title.setText(getString(R.string.tab_calc) + " (" +additionText+ ")");
		}else{
			title.setText(getString(R.string.tab_calc));
		}
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
	    {
	        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        tv.setTextColor(Color.WHITE);
	    } */
	}
	 
	 
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		if(!validatePayment()){
			setTab();
		}
  	    int tab = tabHost.getCurrentTab();
  	    tabHost.getTabWidget().getChildAt(tab).setBackgroundColor(Color.CYAN);
  	
		if(tabHost != null){
			for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
	        {
				tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.tab_color);
	        } 
						
			tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.color.tab_selected_color);
			
		}
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
	    {
	        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        //tv.setTextColor(Color.WHITE);
	    } 
	}
	Date currDate = new Date();
	private boolean validatePayment() {
		if(currDate.getTime()>SaveUtils.getEndPDate(this)){
			return false;
		}
		//if((dayscount - SaveUtils.getStartDaysCount(this))> getPaymentLimit(SaveUtils.getPaymentType(this))){	
		///	return false;
		//}		
		return true;
	}

	public static int getPaymentLimit(String paymentType) {
		if("calorycounter_sport1".endsWith(paymentType)){
			return 32;
		}else if("yearsubs_diethelper".endsWith(paymentType)){
			return 365;
		}else{
			return 7;
		}
	}	

}