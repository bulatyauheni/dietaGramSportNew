package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.utils.SaveUtils;

public class SportArrayAdapter extends ArrayAdapter<Dish> {

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<Dish> list;
	int weight = 0;
	public SportArrayAdapter(Context context, int layoutResourceId, List<Dish> list) {
		super(context, layoutResourceId, list);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list;
		weight = (int)SaveUtils.getRealWeight(context);
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
			String itemId =	item.getId();
			String type = item.getType();

			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textViewDishName);
			TextView wayTextView = (TextView) rowView
					.findViewById(R.id.textViewWayHide);
			TextView weightTextView = (TextView) rowView
					.findViewById(R.id.textViewWeightHide);
			TextView textViewProteinLabel = (TextView) rowView
					.findViewById(R.id.textViewProteinLabel);
			
			nameTextView.setText(itemName);

			DecimalFormat df = new DecimalFormat("###.#");

			TextView textViewСonsumption = (TextView) rowView
					.findViewById(R.id.textViewCons);
			if(item.getProtein()!=0){
				textViewСonsumption.setText(df.format(item.getProtein()*60*weight));
			}else{
				wayTextView.setText(df.format(item.getCarbon()));
				weightTextView.setText(df.format(item.getFat()));
				textViewСonsumption.setVisibility(View.GONE);
				textViewProteinLabel.setVisibility(View.GONE);
			}
		
			TextView typeView = (TextView) rowView
					.findViewById(R.id.textViewDishType);
			typeView.setText(type);

			TextView idView = (TextView) rowView.findViewById(R.id.textViewId);

			idView.setText(itemId);

			if (parent != null) {

			/*	Button remButton = (Button) v.findViewById(R.id.buttonDell);

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
				}*/
			}
		}
		return rowView;
	}
}
