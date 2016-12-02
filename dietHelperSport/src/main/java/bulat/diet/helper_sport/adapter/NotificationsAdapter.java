package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.R.color;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.NotificationActivity;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.NotificationDishHelper;
import bulat.diet.helper_sport.item.NotificationDish;
import bulat.diet.helper_sport.service.AlarmService;

public class NotificationsAdapter extends CursorAdapter {
	DecimalFormat df = new DecimalFormat("###.#");
	private Context ctx;
	private Activity parentVal;
	List<NotificationDish> list;
	NotificationActivity page;
	private int layoutResourceId;

	public NotificationsAdapter(NotificationActivity page, Context context,
			Cursor c, Activity calendarActivityGroup) {
		super(context, c);
		this.page = page;
		ctx = context;
		this.parentVal = calendarActivityGroup;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater
				.inflate(R.layout.notification_list_row, parent, false);
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

	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	@Override
	public void bindView(View v, Context context, Cursor c) {

		NotificationDish notif = new NotificationDish(c.getString(c
				.getColumnIndex(DishProvider.N_TIME_ID)), c.getString(c
				.getColumnIndex(DishProvider.N_NAME_NOTIFICATION)),
				c.getString(c
						.getColumnIndex(DishProvider.N_NOTIFICATION_TIME_HH)),
				c.getString(c
						.getColumnIndex(DishProvider.N_NOTIFICATION_TIME_MM)),
				c.getInt(c.getColumnIndex(DishProvider.N_ENABLED)), c.getInt(c
						.getColumnIndex(DishProvider.N_NOTIF_ENABLED)));

		if (notif != null) {

			int itemEnabled = notif.getEnabled();
			CheckBox enabedCB = (CheckBox) v.findViewById(R.id.enabledCB);

			// duplicate of name, for alert title.
			TextView titleTextView = (TextView) v
					.findViewById(R.id.textViewTitle);
			LinearLayout row = (LinearLayout)v.findViewById(R.id.notifLL);
			TextView nameTextView = (TextView) v
					.findViewById(R.id.textViewNotification);
			TextView nameTextViewOn = (TextView) v
					.findViewById(R.id.textViewNotificationTimeLabel);
			TextView nameTextViewOff = (TextView) v
					.findViewById(R.id.textViewNotificationTimeLabelOff);
			TextView timeTextView = (TextView) v
					.findViewById(R.id.textViewNotificationTime);
			nameTextView.setText(notif.getName());
			titleTextView.setText(notif.getName());
			TextView tvi = (TextView) v.findViewById(R.id.textViewId);
			final TextView tvi2 = (TextView) v.findViewById(R.id.textViewId2);
			TextView tvENFLAG = (TextView) v
					.findViewById(R.id.textViewNotifEnabled);
			TextView tviHH = (TextView) v.findViewById(R.id.textViewHH);
			TextView tviMM = (TextView) v.findViewById(R.id.textViewMM);
			//Button nButton = (Button) v.findViewById(R.id.b_notif);
			tvi.setText(notif.getId());
			tvi2.setText(notif.getId());
			tviHH.setText(notif.getTimeHH());
			tviMM.setText(notif.getTimeMM());
			tvENFLAG.setText("" + notif.getEnabledNotification());
			Date date;
			try {
				date = sdf.parse(notif.getTimeHH() + ":" + notif.getTimeMM());
				timeTextView.setText(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (notif.getEnabledNotification() == 1) {
				nameTextViewOn.setVisibility(View.VISIBLE);
				nameTextViewOff.setVisibility(View.GONE);
				timeTextView.setVisibility(View.VISIBLE);
			} else {				
				nameTextViewOff.setVisibility(View.VISIBLE);
				nameTextViewOn.setVisibility(View.GONE);
				timeTextView.setVisibility(View.GONE);
			}
			if (itemEnabled == 1) {
				enabedCB.setChecked(true);
				//nButton.setEnabled(true);
				row.setBackgroundResource(R.color.main_color);
			} else {
				row.setBackgroundColor(Color.GRAY);
				enabedCB.setChecked(false);
				//nButton.setEnabled(false);

			}
			// enabedCB.setId(Integer.parseInt(notif.getId()));
			// weightButton.setId(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_DATE_LONG))));
			enabedCB.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					TextView titleTextView = (TextView) ((View) arg0
							.getParent().getParent()).findViewById(R.id.textViewTitle);
					TextView IDTextView = (TextView) ((View) arg0
							.getParent().getParent()).findViewById(R.id.textViewId2);
					TextView ifEnabled = (TextView) ((View) arg0.getParent()
							.getParent())
							.findViewById(R.id.textViewNotifEnabled);
					TextView tviHH = (TextView) ((View) arg0
							.getParent().getParent())
							.findViewById(R.id.textViewHH);
					TextView tviMM = (TextView) ((View) arg0
							.getParent().getParent())
							.findViewById(R.id.textViewMM);
					if (((CheckBox) arg0).isChecked()) {
						NotificationDishHelper.setNotifEnabled(ctx, 1, tvi2
								.getText().toString());
						
						setAlert(titleTextView.getText().toString(),
								IDTextView.getText().toString(),
								ifEnabled.getText().toString(),
								tviHH.getText().toString(),
								tviMM.getText().toString(),								
								true);
					} else {
						if(NotificationDishHelper.getEnabledNotificationsList(ctx).size()<2){
							Toast.makeText(ctx, ctx.getString(R.string.last_notif_alert),
									Toast.LENGTH_LONG).show();
						}else{
							NotificationDishHelper.setNotifEnabled(ctx, 0, tvi2
									.getText().toString());	
							setAlert(titleTextView.getText().toString(),
									IDTextView.getText().toString(),
									ifEnabled.getText().toString(),
									tviHH.getText().toString(),
									tviMM.getText().toString(),								
									false);
						}
					}

					page.resume();
				}
			});
			if(notif.getEnabled()==1){
				v.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						CheckBox enabedCB = (CheckBox) v.findViewById(R.id.enabledCB);
						if (enabedCB.isChecked()){
						LinearLayout mbut = (LinearLayout) v;
						final TextView tvi2 = (TextView) ((View) mbut)
								.findViewById(R.id.textViewId2);
						final Dialog dialog = new Dialog(parentVal);
						dialog.setContentView(R.layout.set_notification_dialog);
						dialog.setTitle(R.string.set_notif_dialog_title);
						TextView titleTextView = (TextView) ((View) mbut).findViewById(R.id.textViewTitle);
						
						final TextView tviHH = (TextView) ((View) mbut)
								.findViewById(R.id.textViewHH);
						final TextView tviMM = (TextView) ((View) mbut)
								.findViewById(R.id.textViewMM);
						EditText title = (EditText) dialog.findViewById(R.id.editTextNotifTitle);
						title.setText(titleTextView.getText().toString());
						final TimePicker time = (TimePicker) dialog
								.findViewById(R.id.timePickerNotif);
						final TextView id = (TextView) dialog
								.findViewById(R.id.textViewIdTemp);
						final TextView tvENFLAG = (TextView) ((View) mbut)
								.findViewById(R.id.textViewNotifEnabled);
						final CheckBox chb = (CheckBox) dialog
								.findViewById(R.id.enabledNotifCB);
						id.setText(tvi2.getText());
						
						if ("0".equals(tvENFLAG.getText().toString())) {
							chb.setChecked(false);
							time.setEnabled(false);
						} else {
							chb.setChecked(true);
							time.setEnabled(true);
						}
						time.setEnabled(chb.isChecked());
						chb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							public void onCheckedChanged(CompoundButton v,
									boolean isChecked) {
								time.setEnabled(isChecked);
							}
						});
						// set current time into timepicker
						time.setCurrentHour(Integer.valueOf(tviHH.getText()
								.toString()));
						time.setCurrentMinute(Integer.valueOf(tviMM.getText()
								.toString()));
	
						Button buttonOk = (Button) dialog
								.findViewById(R.id.buttonYes);
						buttonOk.setOnClickListener(new OnClickListener() {
	
							public void onClick(View v) {
								final TimePicker time = (TimePicker) ((View) v
										.getParent().getParent().getParent())
										.findViewById(R.id.timePickerNotif);
								final TextView tviTemp = (TextView) ((View) v
										.getParent().getParent().getParent())
										.findViewById(R.id.textViewIdTemp);
								final CheckBox chb = (CheckBox) ((View)v.getParent().getParent().getParent())
										.findViewById(R.id.enabledNotifCB);
								EditText title = (EditText) dialog.findViewById(R.id.editTextNotifTitle);
								if(title.getText().length()<1){
									title.setBackgroundColor(Color.RED);
								}else{								
									NotificationDishHelper.updateNotifTime(ctx, ""+ time.getCurrentHour(),
										"" + time.getCurrentMinute(), chb.isChecked()?"1":"0", title.getText().toString(), tviTemp.getText().toString());
										setAlert(title.getText().toString(), 
										tviTemp.getText().toString(), 
										chb.isChecked()?"1":"0", 
										""+time.getCurrentHour(), 
										""+time.getCurrentMinute(), 
										chb.isChecked());
	
									dialog.cancel();
									page.resume();
								}
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
					}
				});
			}
		}
	}
	//set and cancel alert
	public void setAlert(String title, String id, String ifEnabled, String timeHH, String timmeMM, boolean flag){
		PendingIntent pendingIntent;
		Intent myIntent = new Intent(ctx, AlarmService.class);		
		if(flag){			
			myIntent.putExtra(NotificationActivity.TITLE, title);
			pendingIntent = PendingIntent.getService(ctx, id.hashCode(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			if ("1".equals(ifEnabled)) {				
				Date date = new Date(System.currentTimeMillis());	
				if((date.getHours()>Integer.parseInt(timeHH))||(date.getHours()==Integer.parseInt(timeHH) && date.getMinutes()>Integer.parseInt(timmeMM))){
					date.setTime(date.getTime() + DateUtils.DAY_IN_MILLIS);
				}
				date.setHours(Integer.parseInt(timeHH));
				date.setMinutes(Integer.parseInt(timmeMM));	
				AlarmManager alarmManager = (AlarmManager) ctx
						.getSystemService(ctx.ALARM_SERVICE);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				calendar.add(Calendar.SECOND, 10);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(),
						DateUtils.DAY_IN_MILLIS, pendingIntent);
				
				//alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				//		calendar.getTimeInMillis(),
				 //       AlarmManager.INTERVAL_DAY, pendingIntent);
				
				Toast.makeText(ctx, ctx.getText(R.string.set_notiftime_toast_on),
						Toast.LENGTH_LONG).show();
			}else{
				AlarmManager alarmManager = (AlarmManager) ctx
						.getSystemService(ctx.ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);
			}
		}
	}
}
