package bulat.diet.helper_sport.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Day;

public class SocialCalendarAdapter extends ArrayAdapter<Day> {

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<Day> list;

	public SocialCalendarAdapter(Context context, int layoutResourceId, ArrayList<Day> list2) {
		super(context, layoutResourceId);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list2;
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
	public View  getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout rowView;
		Day day = null;
		try{
			day = list.get(position);
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
		
		
		if (day != null){
			String name = day.getName();
			String weight = String.valueOf(day.getWeight());
			String caloricity = String.valueOf(day.getCaloricity());
			String bodyweight = String.valueOf(day.getBodyWeight());
			String date_int = String.valueOf(day.getDateInt());
			
			
			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textViewName);

			nameTextView.setText(name);
			

			TextView weightView = (TextView) rowView
					.findViewById(R.id.textViewWeight);

			weightView.setText(weight  + " " + context.getString(R.string.gram));

			TextView caloricityView = (TextView) rowView
					.findViewById(R.id.textViewCaloricity);

			caloricityView.setText(caloricity + " " + context.getString(R.string.kcal));
			
			TextView bodyweightView = (TextView) rowView
					.findViewById(R.id.textViewWeightBodyTotal);

			bodyweightView.setText(bodyweight + " " + context.getString(R.string.kgram));

			TextView idView = (TextView) rowView.findViewById(R.id.textViewUserId);

			idView.setText(date_int);

			ImageView iv = (ImageView) rowView.findViewById(R.id.imageViewDay);
			if(day.getCaloricity() > day.getLimit()){
				 iv.setImageResource(R.drawable.fat_man);
		    }else{
		    	  iv.setImageResource(R.drawable.halth_man);
			}
			
		}
		
		return rowView;
	}
		

}
