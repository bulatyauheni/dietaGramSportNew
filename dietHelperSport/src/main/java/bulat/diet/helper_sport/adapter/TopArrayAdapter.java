package bulat.diet.helper_sport.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.TodayDish;

public class TopArrayAdapter extends ArrayAdapter<TodayDish> {

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<TodayDish> list;

	public TopArrayAdapter(Context context, int layoutResourceId, List<TodayDish> list) {
		super(context, layoutResourceId, list);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount(){
		if( list != null ){
			return list.size();
		}
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowView;
		TodayDish item = null;
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


			TextView caloricityView = (TextView) rowView
					.findViewById(R.id.textViewCaloricity);

			caloricityView.setText(itemCaloricity);

			TextView caloricityLabelView = (TextView) rowView
					.findViewById(R.id.textViewCaloricityLabel);

			caloricityLabelView.setText(context.getString(R.string.kcal));
			
			TextView fatView = (TextView) rowView
					.findViewById(R.id.textViewFat);
			fatView.setText(String.valueOf(item.getFat()));
			



			TextView carbonView = (TextView) rowView
					.findViewById(R.id.textViewCarbon);
			carbonView.setText(String.valueOf(item.getCarbon()));

			TextView proteinView = (TextView) rowView
					.findViewById(R.id.textViewProtein);
			proteinView.setText(String.valueOf(item.getProtein()));



			if (parent != null) {

				
			}
		}
		
		return rowView;

	}

	
	

}
