package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.CalendarActivity;
import bulat.diet.helper_sport.activity.CalendarActivityGroup;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.VolumeInfo;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.utils.DialogUtils;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;


public class TopCursorAdapter extends CursorAdapter {
	DecimalFormat df = new DecimalFormat("###.#");
	private Context ctx;

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
	public TopCursorAdapter(Context context, Cursor c) {
		super(context, c);
		ctx = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.top_dish_list_row, parent, false);
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
				.getColumnIndex(DishProvider.TODAY_DISH_CALORICITY));
		 		
		 nameTextView = (TextView) v
				.findViewById(R.id.textViewDishName);

		nameTextView.setText(itemName);

		 caloricityView = (TextView) v
				.findViewById(R.id.textViewCaloricity);
		
		 fatView = (TextView) v
				.findViewById(R.id.textViewFat);
		fatView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.TODAY_DISH_FAT))));

		 carbonView = (TextView) v
				.findViewById(R.id.textViewCarbon);
		carbonView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.TODAY_DISH_CARBON))));

		 proteinView = (TextView) v
				.findViewById(R.id.textViewProtein);
		proteinView.setText(String.valueOf(c.getFloat(c
				.getColumnIndex(DishProvider.TODAY_DISH_PROTEIN))));


		caloricityView.setText(itemCaloricity);
				
		 caloricityLabelView = (TextView) v
				.findViewById(R.id.textViewCaloricityLabel);

		caloricityLabelView.setText(context.getString(R.string.kcal));		
		
	}

	

}
