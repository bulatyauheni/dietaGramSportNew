package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.AddFitnesActivity;
import bulat.diet.helper_sport.activity.AddTodayDishActivity;
import bulat.diet.helper_sport.activity.AddTodayFitnesActivity;
import bulat.diet.helper_sport.activity.CalendarActivityGroup;
import bulat.diet.helper_sport.activity.DishActivity;
import bulat.diet.helper_sport.activity.DishActivityGroup;
import bulat.diet.helper_sport.activity.NewTemplateActivity;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TemplateDishHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.SocialUpdater;

public class TemplateDishAdapter extends CursorAdapter {
	// We have two list item view types
		private static final int VIEW_TYPE_GROUP_START = 0;
		private static final int VIEW_TYPE_GROUP_CONT = 1;
		private static final int VIEW_TYPE_COUNT = 2;
		private Context ctx;
		private DishActivityGroup parentDishActivity;
		private CalendarActivityGroup parentCalendarActivity;
		String parentName = null;
		DishActivity page;
		protected String selectedInemId;

		String lastIndex = "";
		private int sectionNumber;
		private int caloryCount;
		private TextView currHeader = null;
		private int oldPosition;
		TreeMap<String, Dish> headers = new TreeMap<String, Dish>();

		protected boolean newCursor = false;

		public TemplateDishAdapter(Context context, Cursor c, DishActivityGroup parent) {
			super(context, c);

			this.page = page;
			ctx = context;
			setHeaders(c);
			this.parentDishActivity = parent;
			parentName = DishActivityGroup.class.toString();
			// TODO Auto-generated constructor stub
		}

		public TemplateDishAdapter(Context context, Cursor c,
				CalendarActivityGroup parent) {
			super(context, c);
			ctx = context;

			setHeaders(c);
			this.parentCalendarActivity = parent;
			parentName = CalendarActivityGroup.class.toString();
			// TODO Auto-generated constructor stub
		}

		private void setHeaders(Cursor c) {
			// TODO Auto-generated method stub
			headers = new TreeMap<String, Dish>();
			String curDayTime = "";
			int tempCount = 0;
			float tempFat = 0;
			float tempCarbon = 0;
			float tempProtein = 0;
			
			if (!c.isBeforeFirst()) {
				c.moveToFirst();
				c.moveToPrevious();
			}

			if (c != null) {
				try {
					Dish dish = new Dish();
					while (c.moveToNext()) {
						if ("".equals(curDayTime)) {
							curDayTime = c
									.getString(c
											.getColumnIndex(DishProvider.TODAY_DESCRIPTION));
						}
						if (curDayTime.equals(c.getString(c
								.getColumnIndex(DishProvider.TODAY_DESCRIPTION)))) {
							tempCount = tempCount
									+ c.getInt(c
											.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY));
							tempFat = tempFat
									+ c.getFloat(c
											.getColumnIndex(DishProvider.TODAY_DISH_FAT));
							tempCarbon = tempCarbon
									+ c.getFloat(c
											.getColumnIndex(DishProvider.TODAY_DISH_CARBON));
							tempProtein = tempProtein
									+ c.getFloat(c
											.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN));
						} else {
							
							headers.put(curDayTime, new Dish("", "", Integer.valueOf(tempCount), 0, 0, 0, "", String.valueOf(tempFat), String.valueOf(tempCarbon), String.valueOf(tempProtein)));
							curDayTime = c
									.getString(c
											.getColumnIndex(DishProvider.TODAY_DESCRIPTION));
							tempCount = c
									.getInt(c
											.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY));
							tempFat = c.getFloat(c
											.getColumnIndex(DishProvider.TODAY_DISH_FAT));
							tempCarbon = c.getFloat(c
											.getColumnIndex(DishProvider.TODAY_DISH_CARBON));
							tempProtein = c.getFloat(c
											.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN));
						}
					}
					DecimalFormat df = new DecimalFormat("###.#");
					headers.put(curDayTime, new Dish("", "", Integer.valueOf(tempCount), 0, 0, 0, "", df.format(tempFat), df.format(tempCarbon), df.format(tempProtein)));
					tempCount = 0;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					c.moveToFirst();
				}
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			LayoutInflater mInflater = LayoutInflater.from(context);

			int position = cursor.getPosition();
			int nViewType;

			if (position == 0) {
				// Group header for position 0

				nViewType = VIEW_TYPE_GROUP_START;
			} else {
				// For other positions, decide based on data

				boolean newGroup = isNewGroup(cursor, position);
				if (newGroup) {
					nViewType = VIEW_TYPE_GROUP_START;
				} else {
					nViewType = VIEW_TYPE_GROUP_CONT;
				}
			}

			View v;
			if (nViewType == VIEW_TYPE_GROUP_START) {
				// Inflate a layout to start a new group

				v = mInflater.inflate(R.layout.today_header_dish_list_row, parent,
						false);

				// Ignore clicks on the list header

				View vHeader = v.findViewById(R.id.message_item_when_header);
				vHeader.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
					}
				});
			} else {
				// Inflate a layout for "regular" items
				v = mInflater.inflate(R.layout.today_dish_list_row, parent, false);
			}
			v.setClickable(true);
			v.setFocusable(true);
			v.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					Intent intent = new Intent();
					TextView name = (TextView) v
							.findViewById(R.id.textViewDishName);
					TextView time = (TextView) v.findViewById(R.id.textViewTime);
					TextView timeHH = (TextView) v.findViewById(R.id.textViewTimeHH);
					TextView timeMM = (TextView) v.findViewById(R.id.textViewTimeMM);
					TextView fat = (TextView) v
							.findViewById(R.id.textViewFatTrue);
					TextView carbon = (TextView) v
							.findViewById(R.id.textViewCarbonTrue);
					TextView protein = (TextView) v
							.findViewById(R.id.textViewProteinTrue);
					TextView fatAbsView = (TextView) v
							.findViewById(R.id.textViewFat);
					TextView carbonAbsView = (TextView) v
							.findViewById(R.id.textViewCarbon);
					TextView calorisity = (TextView) v
							.findViewById(R.id.textViewCaloricity);
					TextView absCalorisity = (TextView) v
							.findViewById(R.id.textViewAbsCaloricity);
					TextView weight = (TextView) v
							.findViewById(R.id.textViewWeight);
					TextView id = (TextView) v.findViewById(R.id.textViewId);
					intent.putExtra(AddTodayDishActivity.TITLE,
							R.string.edit_today_dish);
					intent.putExtra(AddTodayDishActivity.DISH_NAME, name.getText().toString());
					intent.putExtra(AddTodayDishActivity.DISH_CALORISITY,
							Integer.valueOf(calorisity.getText().toString()));
					intent.putExtra(AddTodayDishActivity.DISH_FAT,
							fat.getText().toString());
					intent.putExtra(AddTodayDishActivity.DISH_CARBON,
							carbon.getText().toString());
					intent.putExtra(AddTodayDishActivity.DISH_PROTEIN,
							protein.getText().toString());
					intent.putExtra(AddTodayDishActivity.DISH_ABSOLUTE_CALORISITY,
							Integer.valueOf(absCalorisity.getText().toString()));
					intent.putExtra(AddTodayDishActivity.ID, id.getText());
					if (time.getText() != null) {
						intent.putExtra(AddTodayDishActivity.DISH_TIME,
								time.getText());
					}
					if (timeHH.getText() != null) {
						intent.putExtra(AddTodayDishActivity.DISH_TIME_HH,
								timeHH.getText());
					}
					if (timeMM.getText() != null) {
						intent.putExtra(AddTodayDishActivity.DISH_TIME_MM,
								timeMM.getText());
					}
					if (Integer.valueOf(calorisity.getText().toString()) >= 0) {
						intent.putExtra(NewTemplateActivity.TEMPLATE, true);
						intent.setClass(ctx, AddTodayDishActivity.class);
						intent.putExtra(AddTodayDishActivity.DISH_WEIGHT,
								Integer.valueOf(weight.getText().toString()));
					} else {
						intent.putExtra(NewTemplateActivity.TEMPLATE, true);
						intent.putExtra(AddFitnesActivity.FITNES_WEY,
								carbon.getText().toString());
						intent.putExtra(AddFitnesActivity.FITNES_WEIGHT,
								fat.getText().toString());
						intent.putExtra(AddFitnesActivity.FITNES_COUNT_SELECTION,
								Integer.valueOf(fatAbsView.getText().toString()));
						intent.putExtra(AddFitnesActivity.FITNES_WEIGHT_SELECTION,
								Integer.valueOf(carbonAbsView.getText().toString()));
						intent.putExtra(AddFitnesActivity.FITNES_WEIGHTDEC_SELECTION,
								Integer.valueOf(protein.getText().toString()));
						
						
						intent.putExtra(AddTodayFitnesActivity.CALORYBURN,
								"0");
						intent.setClass(ctx, AddTodayFitnesActivity.class);
					}

					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					if (CalendarActivityGroup.class.toString().equals(parentName)) {
						CalendarActivityGroup activityStack = TemplateDishAdapter.this.parentCalendarActivity;
						activityStack.push("AddTodayDishActivityCalendar", intent);
					} else {
						DishActivityGroup activityStack = TemplateDishAdapter.this.parentDishActivity;
						activityStack.push("AddTodayDishActivity", intent);
					}

				}

			});
			v.setBackgroundResource(android.R.drawable.menuitem_background);

			return v;
			// return inflater.inflate(R.layout.item_list_row, null);
		}

		@Override
		public void bindView(View v, Context context, Cursor c) {
			if (newCursor) {
				setHeaders(c);
				newCursor = false;
			}
			int itemTime = c.getInt(c
					.getColumnIndex(DishProvider.TODAY_DESCRIPTION));
			String timeHH = String.valueOf(c.getInt(c
					.getColumnIndex(DishProvider.TODAY_DISH_TIME_HH)));
			String timeMM = c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_TIME_MM));
			
			// get food params
			String itemCaloricity = c.getString(c
					.getColumnIndex(DishProvider.TODAY_CALORICITY));
			String itemAbsCaloricity = c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY));
			String itemFat = c.getString(c.getColumnIndex(DishProvider.TODAY_FAT));
			String itemCarbon = c.getString(c
					.getColumnIndex(DishProvider.TODAY_CARBON));
			String itemProtein = c.getString(c
					.getColumnIndex(DishProvider.TODAY_PROTEIN));
			String itemAbsFat = c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_FAT));
			String itemAbsCarbon = c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_CARBON));
			String itemAbsProtein = c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN));

			int itemCaloricityInt = Integer.valueOf(itemAbsCaloricity);

			if (itemTime == sectionNumber) {
				caloryCount = caloryCount + itemCaloricityInt;
			} else {
				caloryCount = itemCaloricityInt;
			}
			
			//time HH
					TextView timeHHView = (TextView) v
							.findViewById(R.id.textViewTimeHH);
					timeHHView.setText(timeHH);
					
					TextView timeMMView = (TextView) v
							.findViewById(R.id.textViewTimeMM);
					timeMMView.setText(timeMM);
					
					
			TextView fatView = (TextView) v
					.findViewById(R.id.textViewFat);
			fatView.setText(itemAbsFat);

			TextView carbonView = (TextView) v
					.findViewById(R.id.textViewCarbon);
			carbonView.setText(itemAbsCarbon);

			TextView proteinView = (TextView) v
					.findViewById(R.id.textViewProtein);
			proteinView.setText(itemAbsProtein);
			// //header
			TextView tv = (TextView) v.findViewById(R.id.message_item_when);
			TextView tvCount = (TextView) v
					.findViewById(R.id.item_in_value);
			TextView tvFat = (TextView) v
					.findViewById(R.id.textViewFatTotal);
			TextView tvCarb = (TextView) v
					.findViewById(R.id.textViewCarbonTotal);
			TextView tvProt = (TextView) v
					.findViewById(R.id.textViewProteinTotal);
	TextView tvFatPercent = (TextView) v
					.findViewById(R.id.textViewFatPercent);
			TextView tvCarbPercent = (TextView) v
					.findViewById(R.id.textViewCarbonPercent);
			TextView tvProtPercent = (TextView) v
					.findViewById(R.id.textViewProteinPercent);
			TextView tvLabel = (TextView) v
					.findViewById(R.id.item_in_label);

			if (tv != null) {
				currHeader = tvCount;
				sectionNumber = itemTime;
				LinearLayout hedLine = (LinearLayout) v.findViewById(R.id.linearLayoutFCPline);
				hedLine.setVisibility(View.VISIBLE);

				if (itemTime == Constants.ACTIVITY) {
					tv.setText(R.string.time_active);
					tvLabel.setText(ctx.getString(R.string.header_out_label));				
					hedLine.setVisibility(View.GONE);
				}
				
				try{
				tvCount.setText(String.valueOf(headers.get(String.valueOf(itemTime)).getCaloricity()));
				
				tvFat.setText(headers.get(String.valueOf(itemTime)).getFatStr());
				tvCarb.setText(headers.get(String.valueOf(itemTime)).getCarbonStr());
				tvProt.setText(headers.get(String.valueOf(itemTime)).getProteinStr());
				float sum = Float.valueOf(headers.get(String.valueOf(itemTime)).getFatStr()) + 
						Float.valueOf(headers.get(String.valueOf(itemTime)).getCarbonStr())+
						Float.valueOf(headers.get(String.valueOf(itemTime)).getProteinStr());
				
			
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			// rows
			String itemName = c
					.getString(c.getColumnIndex(DishProvider.TODAY_NAME));

			String itemWeight = c.getString(c
					.getColumnIndex(DishProvider.TODAY_DISH_WEIGHT));
			String itemId = c.getString(c.getColumnIndex("_id"));
			// String itemcategory =
			// c.getString(c.getColumnIndex(DishProvider.TODAY_CATEGORY));
			TextView time = (TextView) v.findViewById(R.id.textViewTime);
			time.setText(String.valueOf(itemTime));
			TextView nameTextView = (TextView) v
					.findViewById(R.id.textViewDishName);
			nameTextView.setText(itemName);
			TextView fat = (TextView) v
					.findViewById(R.id.textViewFatTrue);
			fat.setText(itemFat);
			TextView carbon = (TextView) v
					.findViewById(R.id.textViewCarbonTrue);
			carbon.setText(itemCarbon);
			TextView protein = (TextView) v
					.findViewById(R.id.textViewProteinTrue);
			protein.setText(itemProtein);
			TextView caloricityView = (TextView) v
					.findViewById(R.id.textViewCaloricity);
			TextView wayAndWeight = (TextView) v
					.findViewById(R.id.textViewWayAdneWeight);
			caloricityView.setText(itemCaloricity);
			ImageView img_icon = (ImageView) v.findViewById(R.id.imageKindViewId);
			TextView weightView = (TextView) v.findViewById(R.id.textViewWeight);
			LinearLayout weightLine = (LinearLayout) v.findViewById(R.id.linearLayoutWeightValue);
			weightLine.setVisibility(View.VISIBLE);
			LinearLayout linearLayoutFCP = (LinearLayout) v.findViewById(R.id.linearLayoutFCP);
			linearLayoutFCP.setVisibility(View.VISIBLE);
			if (Integer.valueOf(itemCaloricity.toString()) >= 0) {
				weightView.setText(itemWeight);
				wayAndWeight.setVisibility(View.GONE);
				// img_icon.setImageResource(R.drawable.food_icon);
			} else {
				if(Integer.valueOf(itemAbsCarbon)>0 && Integer.valueOf(itemAbsFat)>0){
					wayAndWeight.setText(itemAbsCarbon + " " + ctx.getString(R.string.kgram) +  "/" + (Integer.valueOf(itemAbsFat)+1) + " " + ctx.getString(R.string.count_num));
					wayAndWeight.setVisibility(View.VISIBLE);
				}
				else
					if(Integer.valueOf(itemAbsFat)>0 ){
						wayAndWeight.setText((Integer.valueOf(itemAbsFat)+1)+ " " + ctx.getString(R.string.count_num));
						wayAndWeight.setVisibility(View.VISIBLE);
					}
				weightLine.setVisibility(View.GONE);
				weightView.setText("");
				linearLayoutFCP.setVisibility(View.GONE);
				// img_icon.setImageResource(R.drawable.fitnes_icon);
				// v.setBackgroundResource(R.color.light_green);
			}

			TextView idView = (TextView) v.findViewById(R.id.textViewId);

			idView.setText(itemId);

			TextView absCalView = (TextView) v
					.findViewById(R.id.textViewAbsCaloricity);

			absCalView.setText(itemAbsCaloricity);

			Button remButton = (Button) v.findViewById(R.id.buttonDell);

			if (remButton != null) {
				remButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Button rbut = (Button) v;
						TextView tvi = (TextView) ((View) rbut.getParent())
								.findViewById(R.id.textViewId);
						selectedInemId = tvi.getText().toString();
						AlertDialog.Builder builder = null;
						if (CalendarActivityGroup.class.toString().equals(
								parentName)) {
							builder = new AlertDialog.Builder(
									TemplateDishAdapter.this.parentCalendarActivity);
						} else {
							builder = new AlertDialog.Builder(
									TemplateDishAdapter.this.parentDishActivity);
						}

						builder.setMessage(R.string.remove_dialog)
								.setPositiveButton(ctx.getString(R.string.yes),
										dialogClickListener)
								.setNegativeButton(ctx.getString(R.string.no),
										dialogClickListener).show();
					}
				});
				remButton.setId(Integer.parseInt(itemId));
			}
		}

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					try {
						TemplateDishHelper.removeDish(selectedInemId, ctx);					
						// need update headers values after removing
						newCursor = true;
						selectedInemId = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					selectedInemId = null;
					break;
				}
			}
		};

		public int getViewTypeCount() {
			return VIEW_TYPE_COUNT;
		};

		@Override
		public int getItemViewType(int position) {
			// There is always a group header for the first data item
			if (position == 0) {
				return VIEW_TYPE_GROUP_START;
			}

			// For other items, decide based on current data

			Cursor cursor = getCursor();
			cursor.moveToPosition(position);
			boolean newGroup = isNewGroup(cursor, position);

			// Check item grouping

			if (newGroup) {
				return VIEW_TYPE_GROUP_START;
			} else {
				return VIEW_TYPE_GROUP_CONT;
			}
		}

		private boolean isNewGroup(Cursor cursor, int position) {
			// Get date values for current and previous data items

			String nWhenThis = cursor.getString(cursor
					.getColumnIndex(DishProvider.TODAY_DESCRIPTION));

			cursor.moveToPosition(position - 1);
			String nWhenPrev = cursor.getString(cursor
					.getColumnIndex(DishProvider.TODAY_DESCRIPTION));

			// Restore cursor position

			cursor.moveToPosition(position);

			// Compare date values, ignore time values

			if (!nWhenThis.equals(nWhenPrev)) {
				return true;
			}

			return false;
		}

	}
