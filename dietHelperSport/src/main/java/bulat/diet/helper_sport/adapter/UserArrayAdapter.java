package bulat.diet.helper_sport.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.ChatActivity;
import bulat.diet.helper_sport.activity.SocialActivityGroup;
import bulat.diet.helper_sport.activity.SocialDishActivity;
import bulat.diet.helper_sport.activity.SocialUserListActivity;
import bulat.diet.helper_sport.item.User;
import bulat.diet.helper_sport.utils.SaveUtils;

public class UserArrayAdapter extends ArrayAdapter<User> {

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<User> list;
	SocialActivityGroup parent;

	public UserArrayAdapter(Context context, int layoutResourceId, ArrayList<User> list2,  SocialActivityGroup parent) {
		super(context, layoutResourceId);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list2;
		this.parent = parent;
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
		User user = null;
		try{
			user = list.get(position);
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
		
		
		if (user != null){
			String userName = user.getUserName();
			int sex = user.getSex();
			int userAge = user.getAge();
			int userHeight = user.getHeight();
			String userWeight = String.valueOf(user.getWeightOfBody());
			String userId = "0";
			int activity_level = user.getActivity();
			if(user.getUserName() != null){			
				userId = user.getId();
			}
			ImageView iv = (ImageView) rowView.findViewById(R.id.imageViewDay);
			TextView sexTextView = (TextView) rowView
					.findViewById(R.id.textViewUserSex);

		    if(sex == 1){
		    	sexTextView.setText("1");
		    	  iv.setImageResource(R.drawable.weman);
		    }else{
		    	sexTextView.setText("0");
		    	  iv.setImageResource(R.drawable.man);
		    }
		      
			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textViewName);

			nameTextView.setText(userName);
			
			TextView nameReplicaTextView = (TextView) rowView
					.findViewById(R.id.textViewUserName_replica);
			
			nameReplicaTextView.setText(userName);
			
			TextView weightView = (TextView) rowView
					.findViewById(R.id.textViewUserWeight);

			weightView.setText(userWeight);
			
			TextView heightView = (TextView) rowView
					.findViewById(R.id.textViewUserHeight);

			heightView.setText(String.valueOf(userHeight));
			
			TextView ageView = (TextView) rowView
					.findViewById(R.id.textViewUserAge);

			ageView.setText(String.valueOf(userAge));

			TextView idView = (TextView) rowView.findViewById(R.id.textViewUserId);

			idView.setText(userId);
			
			Button messageButton = (Button) rowView.findViewById(R.id.buttonMessage);

			if (messageButton != null) {
				messageButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(SaveUtils.getUserUnicId(context)!=0){
							Button mbut = (Button) v;
							TextView tvi = (TextView) ((View) mbut.getParent())
									.findViewById(R.id.textViewUserId);
							TextView nameTextView = (TextView) ((View) mbut.getParent()).findViewById(R.id.textViewUserName_replica);
	
							String selectedInemName = nameTextView.getText().toString();
							String selectedInemId = tvi.getText().toString();
							Intent intent = new Intent();
							intent.putExtra(SocialDishActivity.USERID, selectedInemId);
							intent.putExtra(SocialDishActivity.USERNAME, selectedInemName);
	
							try {					
								intent.setClass(context, ChatActivity.class);
								SocialActivityGroup activityStack = (SocialActivityGroup) UserArrayAdapter.this.parent;
								activityStack.push("ChatActivity", intent);
							} catch (Exception e) {
								e.printStackTrace();
							}	
						}else{
							Intent intent = new Intent();
							intent.setClass(context, SocialUserListActivity.class);
							SocialActivityGroup activityStack = (SocialActivityGroup) UserArrayAdapter.this.parent;
							activityStack.push("UserListActivity", intent);
						}
						
					}
				});
				messageButton.setId(Integer.parseInt(userId));
			}
			
			ImageView ivActivity = (ImageView) rowView.findViewById(R.id.imageViewActivity);

			switch (activity_level) {
			case 0:
				ivActivity.setImageResource(R.drawable.activity_level1);
				break;
			case 1:
				ivActivity.setImageResource(R.drawable.activity_level2);
				break;
			case 2:
				ivActivity.setImageResource(R.drawable.activity_level3);
				break;
			case 3:
				ivActivity.setImageResource(R.drawable.activity_level4);
				break;
			case 4:
				ivActivity.setImageResource(R.drawable.activity_level5);
				break;

			default:
				ivActivity.setImageResource(R.drawable.activity_level1);
				break;
			}
			ivActivity.setFadingEdgeLength(activity_level);
			if (parent != null) {

				
			}
		}
		
		return rowView;
	}
		

}
