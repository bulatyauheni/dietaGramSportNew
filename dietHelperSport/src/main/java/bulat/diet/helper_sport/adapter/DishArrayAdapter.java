package bulat.diet.helper_sport.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Dish;

public class DishArrayAdapter extends ArrayAdapter<Dish> {

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<Dish> list;

	public DishArrayAdapter(Context context, int layoutResourceId, List<Dish> list) {
		super(context, layoutResourceId, list);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowView;
		Dish item = null;
		try{
			item = getItem(position);
		}catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		if (convertView == null) {
			rowView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(layoutResourceId, rowView, true);
		} else {
			rowView = (LinearLayout) convertView;
		}
		
		
		if (item != null){
			String itemName = item.getName();
			String itemCaloricity = String.valueOf(item.getCaloricity());
			String itemId = "0";
			if(item.getId() != null){			
				itemId = item.getId();
			}

			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textViewDishName);

			nameTextView.setText(itemName);

			TextView caloricityView = (TextView) rowView
					.findViewById(R.id.textViewCaloricity);

			caloricityView.setText(itemCaloricity);

			TextView caloricityLabelView = (TextView) rowView
					.findViewById(R.id.textViewCaloricityLabel);

			caloricityLabelView.setText(context.getString(R.string.kcal));
			
			TextView fatView = (TextView) rowView
					.findViewById(R.id.textViewFat);
			fatView.setText(String.valueOf(item.getFatStr()));
			
			TextView typeView = (TextView) rowView
					.findViewById(R.id.textViewDishType);
			typeView.setText(String.valueOf(item.getType()));


			TextView carbonView = (TextView) rowView
					.findViewById(R.id.textViewCarbon);
			carbonView.setText(String.valueOf(item.getCarbonStr()));

			TextView proteinView = (TextView) rowView
					.findViewById(R.id.textViewProtein);
			proteinView.setText(String.valueOf(item.getProteinStr()));


			TextView idView = (TextView) rowView.findViewById(R.id.textViewId);

			idView.setText(itemId);
			// ToDo - alcohol does not match for this rule 
			//if (!item.isValid()) {
			//	rowView.setBackgroundColor(context.getResources().getColor(R.color.red));				
			//} else {
				rowView.setBackgroundColor(context.getResources().getColor(R.color.main_color));	
			//}
		}
		
		return rowView;

	}

	
	

}
