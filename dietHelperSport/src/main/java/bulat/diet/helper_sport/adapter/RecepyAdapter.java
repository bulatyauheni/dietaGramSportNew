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
import bulat.diet.helper_sport.activity.RecepyListActivity;
import bulat.diet.helper_sport.db.DishProvider;

public class RecepyAdapter extends CursorAdapter {

	private Context context;
	private DishListActivityGroup parent = null;
	RecepyListActivity page;
	protected boolean remove;

	public RecepyAdapter(RecepyListActivity page,
			Context context, Cursor c, DishListActivityGroup parent2) {
		super(context, c);
		this.page = page;
		this.context = context;
		this.parent = parent2;
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
		String itemName = c.getString(c.getColumnIndex(DishProvider.NAME));
		String itemCaloricity = c.getString(c
				.getColumnIndex(DishProvider.CALORICITY));
		String itemId = c.getString(c.getColumnIndex("_id"));
		String type = c.getString(c.getColumnIndex(DishProvider.TYPE));

		TextView nameTextView = (TextView) v
				.findViewById(R.id.textViewDishName);

		nameTextView.setText(itemName);

		TextView caloricityView = (TextView) v
				.findViewById(R.id.textViewCaloricity);
		
		TextView fatView = (TextView) v
				.findViewById(R.id.textViewFat);
		fatView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.FAT))));

		TextView carbonView = (TextView) v
				.findViewById(R.id.textViewCarbon);
		carbonView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.CARBON))));

		TextView proteinView = (TextView) v
				.findViewById(R.id.textViewProtein);
		proteinView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.PROTEIN))));


		caloricityView.setText(itemCaloricity);
		
		TextView typeView = (TextView) v
				.findViewById(R.id.textViewDishType);
		typeView.setText(type);

		TextView caloricityLabelView = (TextView) v
				.findViewById(R.id.textViewCaloricityLabel);

		caloricityLabelView.setText(context.getString(R.string.kcal));

		TextView idView = (TextView) v.findViewById(R.id.textViewId);

		idView.setText(itemId);

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
