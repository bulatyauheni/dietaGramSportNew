package bulat.diet.helper_sport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.DishListActivity;
import bulat.diet.helper_sport.activity.DishListActivityGroup;
import bulat.diet.helper_sport.db.DishProvider;

public class DishAdapter extends CursorAdapter {

	private Context context;
	private DishListActivityGroup parent = null;
	DishListActivity page;
	protected boolean remove;

	
	String itemName;
	String itemCaloricity;
	String itemId;
	String type;

	TextView nameTextView;
	TextView caloricityView;
	
	TextView fatView;
	TextView carbonView;
	TextView proteinView;
	TextView typeView;
	TextView caloricityLabelView;

	
	public DishAdapter(DishListActivity page, Context context, Cursor c, DishListActivityGroup parent) {
		super(context, c);
		this.page = page;
		this.context = context;
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}	

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dish_list_row, parent, false);
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
		 itemName = c.getString(c.getColumnIndex(DishProvider.NAME));
		 itemCaloricity = c.getString(c
				.getColumnIndex(DishProvider.CALORICITY));
		 itemId = c.getString(c.getColumnIndex("_id"));
		 type = c.getString(c.getColumnIndex(DishProvider.TYPE));

		 nameTextView = (TextView) v
				.findViewById(R.id.textViewDishName);

		nameTextView.setText(itemName);

		 caloricityView = (TextView) v
				.findViewById(R.id.textViewCaloricity);
		
		 fatView = (TextView) v
				.findViewById(R.id.textViewFat);
		fatView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.FAT))));

		 carbonView = (TextView) v
				.findViewById(R.id.textViewCarbon);
		carbonView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.CARBON))));

		 proteinView = (TextView) v
				.findViewById(R.id.textViewProtein);
		proteinView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.PROTEIN))));


		caloricityView.setText(itemCaloricity);
		
		typeView = (TextView) v
				.findViewById(R.id.textViewDishType);
		typeView.setText(type);

		 caloricityLabelView = (TextView) v
				.findViewById(R.id.textViewCaloricityLabel);

		caloricityLabelView.setText(context.getString(R.string.kcal));

		TextView idView = (TextView) v.findViewById(R.id.textViewId);

		idView.setText(itemId);

		if (parent != null) {

			Button remButton = (Button) v.findViewById(R.id.buttonDell);

			if (remButton != null) {

				remButton.setVisibility(View.VISIBLE);
				remButton.setId(Integer.parseInt(itemId));
				remButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Button rbut = (Button) v;
						TextView tvi = (TextView) ((View) rbut.getParent())
									.findViewById(R.id.textViewId);										
						page.showingDialogCancel(tvi.getText().toString());
					}
				});
				remButton.setId(Integer.parseInt(itemId));
			}
		}
	}

	

}
