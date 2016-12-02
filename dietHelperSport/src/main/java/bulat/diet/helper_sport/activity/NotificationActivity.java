package bulat.diet.helper_sport.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.NotificationsAdapter;
import bulat.diet.helper_sport.db.NotificationDishHelper;

public class NotificationActivity extends Activity {
	public static final String TITLE = "TITLE";
	public static final String TYPE = "type";
	protected static final int DIALOG_CHART = 0;
	protected static final int DIALOG_WEIGHT = 1;
	TextView header;
	ListView notificationList;
	Cursor c ;
	int sum;
	Activity parent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);					
		View viewToLoad = LayoutInflater.from(this).inflate(R.layout.notification_list, null);
		//header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			String type = extras.getString(TYPE);
			if(type!=null && "calendar".equals(type)){
				parent = this.getParent();
			}else{
				parent = this;
			}
		}else{
			parent = this;
		}
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {				
				try{
					NotificationActivity.this.onBackPressed();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}			
		});	
		setContentView(viewToLoad);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(c!=null)
			c.close();
	}
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		Button buttonOk;
		Button nobutton;
		switch (id) {
		
		case DIALOG_CHART:		

			break;
		
		default:
			dialog = null;
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		Button buttonOk;
		Button nobutton;
		switch (id) {
		case DIALOG_CHART:
			dialog = new Dialog(this.getParent());
			dialog.setContentView(R.layout.user_chart_dialog);
			dialog.setTitle(R.string.user_chart_dialog);
			break;
		
		default:
			dialog = null;
		}

		return dialog;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(c!=null)
			c.close();
	}
	NotificationsAdapter da;
	@Override
	protected void onResume() {
		super.onResume();				
		c = NotificationDishHelper.getAllNotifications(getApplicationContext());
		if (c!=null){
			
			try {	
							
				da = new NotificationsAdapter(this, getApplicationContext(), c, parent);
				
				notificationList = (ListView) findViewById(R.id.listViewNotifications);
				notificationList.setAdapter(da);
				notificationList.setItemsCanFocus(true);								
				notificationList.setOnItemClickListener(notificationListOnItemClickListener);
	
			}catch (Exception e) {
				if(c!=null)
					c.close();
			}finally{
				//c.close();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(c!=null)
			c.close();
	}
	
	private OnItemClickListener notificationListOnItemClickListener= new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			//switchTabInActivity(1)
			Intent intent = new Intent();
			TextView day = (TextView) v.findViewById(R.id.textViewNotification);
			
			//todo	
		}
	};

	public void resume(){
		c.requery();
		da.notifyDataSetChanged();
	}
}
