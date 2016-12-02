package bulat.diet.helper_sport.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.Constants;

public class SocialDishListArrayAdapter extends ArrayAdapter<TodayDish> {
	
	private static final int VIEW_TYPE_GROUP_START = 0;
	private static final int VIEW_TYPE_GROUP_CONT = 1;
	private static final int VIEW_TYPE_COUNT = 2;

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<TodayDish> list;
	
	String lastIndex = "";
	private int sectionNumber;
	private int caloryCount;
	private TextView currHeader = null;
	private int oldPosition;
	TreeMap<String,Integer> headers = new TreeMap<String,Integer>();
	private int headerNum=0;
	
	public SocialDishListArrayAdapter(Context context, int layoutResourceId, ArrayList<TodayDish> list2) {
		super(context, layoutResourceId);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list2;
		String curDayTime = "";
		int tempCount = 0;
		for (TodayDish todayDish : list2) {
			if("".equals(curDayTime)){
				curDayTime = todayDish.getDescription();
			}
			if(curDayTime.equals( todayDish.getDescription())){
				tempCount = tempCount + todayDish.getCaloricity();
			}else{				
				headers.put(curDayTime,Integer.valueOf(tempCount));
				curDayTime = todayDish.getDescription();
				tempCount = todayDish.getCaloricity();
			}		
		}
		headers.put(curDayTime, Integer.valueOf(tempCount));
		tempCount=0;
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
		  View v;
          if (convertView == null) {
              v = newView( parent, position);
          } else {
        	  v = convertView;
          }
          bindView(v,position);
          return v;
	}
	
	public View newView(ViewGroup parent, int position) {

		LayoutInflater mInflater = LayoutInflater.from(context);
		int nViewType;

		if (position == 0) {
			// Group header for position 0
			nViewType = VIEW_TYPE_GROUP_START;
		} else {
			// For other positions, decide based on data
			boolean newGroup = isNewGroup( position);
			if (newGroup) {
				nViewType = VIEW_TYPE_GROUP_START;
			} else {
				nViewType = VIEW_TYPE_GROUP_CONT;
			}
		}

		View v;
		if (nViewType == VIEW_TYPE_GROUP_START) {
			headerNum++;
			// Inflate a layout to start a new group
			v = mInflater.inflate(R.layout.social_header_dish_list_row, parent,
					false);
			// Ignore clicks on the list header
			View vHeader = v.findViewById(R.id.message_item_when_header);
			vHeader.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				}
			});
		} else {
			// Inflate a layout for "regular" items
			v = mInflater.inflate(R.layout.social_today_dish_list_row, parent, false);
		}
		v.setClickable(true);
		v.setFocusable(true);
		v.setBackgroundResource(android.R.drawable.menuitem_background);

		return v;
		// return inflater.inflate(R.layout.item_list_row, null);
	}

	public void bindView(View v, int position) {
		
		TodayDish td = list.get(position);
		int itemTime = Integer.parseInt(td.getDescription());
		String itemCaloricity = String.valueOf(td.getCaloricity());
		// counting calories for sections headers
		int itemCaloricityInt = Integer.valueOf(itemCaloricity);
		
			// //header
			TextView tv = (TextView) v.findViewById(R.id.message_item_when);
			TextView tvCount = (TextView) v.findViewById(R.id.message_item_in_value);
			TextView tvLabel = (TextView) v.findViewById(R.id.message_item_in_label);

			/*if (tv != null) {
				currHeader = tvCount;
				sectionNumber = itemTime;
				if (itemTime == Constants.BREAKFAST) {
					tv.setText(R.string.time_breakfast);
				}
				if (itemTime == Constants.BRANCH) {
					tv.setText(R.string.time_branch);
				}
				if (itemTime == Constants.DINER) {
					tv.setText(R.string.time_dinner);
				}
				if (itemTime == Constants.LANCH) {
					tv.setText(R.string.time_lanch);
				}
				if (itemTime == Constants.SUPPER) {
					tv.setText(R.string.time_supper);
				}				
				if (itemTime == Constants.ACTIVITY) {
					tv.setText(R.string.time_active);
					tvLabel.setText(context.getString(R.string.header_out_label));
				}
				tvCount.setText(String.valueOf(headers.get(td.getDescription())));
			}	*/		

		// rows
		String itemName = td.getName();
		String itemAbsCaloricity = String.valueOf(td.getCaloricity());
		
		

		String itemWeight = String.valueOf(td.getWeight());
		String itemId = td.getId();
		// String itemcategory =
		// c.getString(c.getColumnIndex(DishProvider.TODAY_CATEGORY));
		TextView time = (TextView) v.findViewById(R.id.textViewTime);
		time.setText(String.valueOf(itemTime));
		TextView nameTextView = (TextView) v
				.findViewById(R.id.textViewDishName);
		nameTextView.setText(itemName);
		TextView caloricityView = (TextView) v
				.findViewById(R.id.textViewCaloricity);
		caloricityView.setText(itemCaloricity);
		ImageView img_icon = (ImageView) v.findViewById(R.id.imageKindViewId);
		TextView weightView = (TextView) v.findViewById(R.id.textViewWeight);
		if (Integer.valueOf(itemCaloricity.toString()) >= 0) {
			weightView.setText(itemWeight);
			img_icon.setImageResource(R.drawable.food_icon);
		} else {
			weightView.setText("");
			img_icon.setImageResource(R.drawable.fitnes_icon);
			// v.setBackgroundResource(R.color.light_green);
		}

		TextView idView = (TextView) v.findViewById(R.id.textViewId);
		idView.setText(itemId);

		TextView absCalView = (TextView) v
				.findViewById(R.id.textViewAbsCaloricity);
		absCalView.setText(itemAbsCaloricity);		
	}

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
		boolean newGroup = isNewGroup( position);

		// Check item grouping

		if (newGroup) {
			return VIEW_TYPE_GROUP_START;
		} else {
			return VIEW_TYPE_GROUP_CONT;
		}
	}

	private boolean isNewGroup( int position) {
		// Get date values for current and previous data items

		String nWhenThis = list.get(position).getDescription();

		
		String nWhenPrev = list.get(position - 1).getDescription();

		// Compare date values, ignore time values

		if (!nWhenThis.equals(nWhenPrev)) {
			return true;
		}

		return false;
	}


		

}
