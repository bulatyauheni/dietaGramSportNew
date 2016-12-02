package bulat.diet.helper_sport.activity;

import java.util.Date;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.SaveUtils;

public class SettingsActivity extends TabActivity implements OnTabChangeListener {

	private TabHost tabHost;
	private TextView tview;
	private TextView tview3;
	private TextView tview2;
	public static SettingsActivity ctx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.setting_main, null);
		setContentView(viewToLoad);	


		Button notification = (Button) viewToLoad.findViewById(R.id.buttonNotification);
		notification.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Intent intent = new Intent();					
				intent.setClass(getParent(), NotificationActivity.class);				
				startActivity(intent);
					
			}
		});
		
		tview=new TextView(this); 
		tview.setText(getString(R.string.tab_mode));
		tview.setTextColor(Color.WHITE);
		tview.setWidth(LayoutParams.FILL_PARENT);
		tview.setGravity(Gravity.CENTER_HORIZONTAL);
		tview3=new TextView(this); 
		tview3.setText(getString(R.string.tab_statistic));
		tview3.setTextColor(Color.WHITE);
		tview3.setWidth(LayoutParams.FILL_PARENT);
		tview3.setGravity(Gravity.CENTER_HORIZONTAL);
		
		tview2=new TextView(this); 
		tview2.setText(getString(R.string.tab_add_params));
		tview2.setTextColor(Color.WHITE);
		tview2.setWidth(LayoutParams.FILL_PARENT);
		tview2.setGravity(Gravity.CENTER_HORIZONTAL);
		
 		tabHost = getTabHost();
 		TabHost.TabSpec spec;
 		Intent intent;
 		tabHost.setOnTabChangedListener(this);
 		intent = new Intent().setClass(this, Info.class);
 		spec = tabHost.newTabSpec("mod")
 				.setIndicator(tview)
 				.setContent(intent);
 		tabHost.addTab(spec);
 		intent = new Intent().setClass(this, StatisticActivityGroup.class);
 		spec = tabHost
 				.newTabSpec("stat")
 				.setIndicator(tview3)
 				.setContent(intent);
 		tabHost.addTab(spec);
 		
 		intent = new Intent().setClass(this, VolumeInfo.class);
 		spec = tabHost
 				.newTabSpec("about")
 				.setIndicator(tview2)
 				.setContent(intent);
 		tabHost.addTab(spec);
 		
 		if(!validatePayment()){
 			tabHost.setCurrentTab(1);
		}
 		
 		tabHost.setCurrentTab(0);
 		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.color.tab_selected_color);
 		if(tabHost != null){
			for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
	        {
				tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.noselected_tab);
				//tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.sec_tab_color);
	        } 
						
			//tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.color.tab_selected_color);
			tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.selected_tab);
			
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
		ctx = this;
		}

	public void onTabChanged(String tabId) {
		if(!validatePayment()){
 			tabHost.setCurrentTab(1);
		}
		// TODO Auto-generated method stub
		if(tabHost != null){
			for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
	        {
				tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.noselected_tab);
				//tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.sec_tab_color);
	        } 
						
			//tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.color.tab_selected_color);
			tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.selected_tab);
		}

	}
	private boolean validatePayment() {
		Date currDate = new Date();
		if(currDate.getTime()>SaveUtils.getEndPDate(this)){	
			return false;
		}		
		return true;
	}

	private int getPaymentLimit(String paymentType) {
		if("calorycounter_sport1".endsWith(paymentType)){
			return 32;
		}else if("yearsubs_diethelper".endsWith(paymentType)){
			return 365;
		}else{
			return 7;
		}
	}	
}
