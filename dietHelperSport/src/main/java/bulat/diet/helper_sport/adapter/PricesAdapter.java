package bulat.diet.helper_sport.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Dish;

public class PricesAdapter extends ArrayAdapter<Pair<String, String>> {

	private Context context;
	int layoutResourceId;
	protected boolean remove;
	List<Pair<String, String>> list;

	public PricesAdapter(Context context, int layoutResourceId, List<Pair<String, String>> list) {
		super(context, layoutResourceId, list);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowView;
		Pair<String, String> item = null;
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
			String itemName = item.first;
			String itemPrice = item.second;

			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textPriceName);

			nameTextView.setText(itemName);

			TextView price = (TextView) rowView
					.findViewById(R.id.price);
			price.setText(itemPrice);
		}
		
		return rowView;

	}

	
	

}
