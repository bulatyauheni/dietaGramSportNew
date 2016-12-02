package bulat.diet.helper_sport.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.CalendarActivityGroup;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TemplateDishHelper;
import bulat.diet.helper_sport.utils.SaveUtils;

public class TemplatesAdapter extends CursorAdapter {

	private Context ctx;
	private CalendarActivityGroup parent;
	protected String selectedInemId;

	public TemplatesAdapter(Context context, Cursor c,
			CalendarActivityGroup calendarActivityGroup) {
		super(context, c);
		ctx = context;
		this.parent = calendarActivityGroup;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.temlpate_list_row, parent, false);
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
		String itemName = c.getString(c
				.getColumnIndex(DishProvider.TODAY_DISH_DATE));
		String itemCaloricity = c.getString(c.getColumnIndex("val"));
		
		String itemWeight = c.getString(c.getColumnIndex("weight"));
		String itemBodyWeight = "";
		if (c.getInt(c.getColumnIndex("bodyweight")) > 0) {

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

		} else {
			itemBodyWeight = String.valueOf(SaveUtils.getWeight(context)
					+ Info.MIN_WEIGHT);			
		}
		TextView nameTextView = (TextView) v.findViewById(R.id.textViewDay);
		nameTextView.setText(itemName);
		LinearLayout le = (LinearLayout) v.findViewById(R.id.layoutEmpty);
		LinearLayout lf = (LinearLayout) v.findViewById(R.id.layoutParams);
		if ("0".equals(itemWeight) && "0".equals(itemCaloricity)) {
			lf.setVisibility(View.GONE);
			le.setVisibility(View.VISIBLE);

		} else {
			lf.setVisibility(View.VISIBLE);
			le.setVisibility(View.GONE);

			TextView fatView = (TextView) v.findViewById(R.id.textViewFat);
			fatView.setText(String.valueOf(c.getString(c.getColumnIndex("fat"))));

			TextView carbonView = (TextView) v
					.findViewById(R.id.textViewCarbon);
			carbonView.setText(String.valueOf(c.getString(c.getColumnIndex("carbon"))));

			TextView proteinView = (TextView) v
					.findViewById(R.id.textViewProtein);
			proteinView.setText(String.valueOf(c.getString(c.getColumnIndex("protein"))));

			TextView caloricityView = (TextView) v
					.findViewById(R.id.textViewCaloricity);

			caloricityView.setText(itemCaloricity + " "
					+ context.getString(R.string.kcal));
			TextView weightView = (TextView) v
					.findViewById(R.id.textViewWeightTotal);

			weightView.setText(itemWeight + " "
					+ context.getString(R.string.gram));

			
			float currWeight = SaveUtils.getRealWeight(ctx);

			if (itemBodyWeight == null) {
				itemBodyWeight = "" + currWeight;
			}		
		}

		Button dellButton = (Button) v.findViewById(R.id.buttonDell);
		if (dellButton != null) {
			dellButton.setId(c.getInt(c.getColumnIndex("_id")));
		}
		TextView tvi = (TextView) v.findViewById(R.id.textViewId);
		tvi.setText(itemName);
		if (dellButton != null) {

			// weightButton.setId(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_DATE_LONG))));
			dellButton.setOnClickListener(new View.OnClickListener() {

				

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button rbut = (Button) v;
					TextView tvi = (TextView) ((View) rbut.getParent())
							.findViewById(R.id.textViewId);
					selectedInemId = tvi.getText().toString();
					AlertDialog.Builder builder = null;
					
					builder = new AlertDialog.Builder(parent);
					

					builder.setMessage(R.string.remove_dialog)
							.setPositiveButton(ctx.getString(R.string.yes),
									dialogClickListener)
							.setNegativeButton(ctx.getString(R.string.no),
									dialogClickListener).show();
				}
			});
		}

	}

	public boolean checkLimit(int sum) {
		int mode = SaveUtils.getMode(ctx);
		try {
			switch (mode) {
			case 0:
				if (sum > Integer.parseInt(SaveUtils.getBMR(ctx))) {
					return false;
				} else {
					return true;
				}
			case 1:
				if (sum > Integer.parseInt(SaveUtils.getMETA(ctx))) {
					return false;
				} else {
					return true;
				}
			case 2:
				if (sum < Integer.parseInt(SaveUtils.getMETA(ctx))) {
					return false;
				} else {
					return true;
				}
			default:
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					TemplateDishHelper.removeDishesByDay(selectedInemId, ctx);					
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

}
