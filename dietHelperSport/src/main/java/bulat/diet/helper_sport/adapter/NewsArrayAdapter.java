package bulat.diet.helper_sport.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.SocialActivityGroup;
import bulat.diet.helper_sport.activity.SocialDishActivity;
import bulat.diet.helper_sport.activity.SocialNewsListActivity;
import bulat.diet.helper_sport.activity.SocialUserNewsActivity;
import bulat.diet.helper_sport.item.News;
import bulat.diet.helper_sport.item.User;
import bulat.diet.helper_sport.utils.SaveUtils;

public class NewsArrayAdapter extends ArrayAdapter<User> {

	private Context context;	
	int layoutResourceId;
	protected boolean remove;
	List<News> list;
	SocialActivityGroup parent;
	private String myId;
	SocialNewsListActivity page;
	
	public NewsArrayAdapter(SocialNewsListActivity socialNewsListActivity, Context context, int layoutResourceId, ArrayList<News> list2,  SocialActivityGroup parent) {
		super(context, layoutResourceId);
		this.layoutResourceId=layoutResourceId;
		this.context = context;
		this.list = list2;
		this.parent = parent;
		myId = String.valueOf(SaveUtils.getUserUnicId(context));
		page = socialNewsListActivity;
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
		News user = null;
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
			String news = user.getNews();
			int userAge = user.getAge();
			int userHeight = user.getHeight();
			String userWeight = String.valueOf(user.getWeightOfBody());
			String userId = "0";
			int activity_level = user.getActivity();
			if(user.getUserName() != null){			
				userId = user.getId();
			}
			TextView newsIdTextView = (TextView) rowView
					.findViewById(R.id.textViewNewsId);
			newsIdTextView.setText( user.getNewsId());
			TextView ownerIdTextView = (TextView) rowView
					.findViewById(R.id.ownerNewsId);
			ownerIdTextView.setText(user.getId());
			// delete news
			Button removeButton = (Button) rowView.findViewById(R.id.buttonRemove);
			if(userId.equals(myId)){
				removeButton.setVisibility(View.VISIBLE);
			}else{
				removeButton.setVisibility(View.GONE);
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
		    
		    TextView nameReplicaTextView = (TextView) rowView
					.findViewById(R.id.textViewUserName_replica2);
			
			nameReplicaTextView.setText(userName);
			
			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.textViewName2);

			nameTextView.setText(userName);			
			nameTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
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
			
			TextView newsView = (TextView) rowView.findViewById(R.id.textViewNewsText);

			newsView.setText(news);
			
			
			
			nameTextView.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					TextView rbut = (TextView) v;
					
					Intent intent = new Intent();
					TextView idView = (TextView) ((View) rbut.getParent()).findViewById(R.id.textViewUserId);
					TextView nameView = (TextView) ((View) rbut.getParent()).findViewById(R.id.textViewName2);
					TextView currAgeView = (TextView) ((View) rbut.getParent()).findViewById(R.id.textViewUserAge);
					TextView currWeightView = (TextView) ((View) rbut.getParent()).findViewById(R.id.textViewUserWeight);
					TextView currHeightView = (TextView) ((View) rbut.getParent()).findViewById(R.id.textViewUserHeight);
					TextView currSexView = (TextView) ((View) rbut.getParent()).findViewById(R.id.textViewUserSex);
					ImageView currSportView = (ImageView) ((View) rbut.getParent()).findViewById(R.id.imageViewActivity);
					
					intent.putExtra(SocialDishActivity.USERID, idView.getText()
							.toString());
					intent.putExtra(SocialDishActivity.USERNAME, nameView.getText()
							.toString());
					intent.putExtra(SocialDishActivity.USERWEIGHT, currWeightView.getText()
							.toString());
					intent.putExtra(SocialDishActivity.USERHEIGHT, currHeightView.getText()
							.toString());
					if("0".equals( currSexView.getText().toString())){
						intent.putExtra(SocialDishActivity.USERSEX, page.getString(R.string.male));
					}else{
						intent.putExtra(SocialDishActivity.USERSEX, page.getString(R.string.female));
					}
					intent.putExtra(SocialDishActivity.USERAGE, currAgeView.getText()
							.toString());
					intent.putExtra(SocialDishActivity.USERSPORTING, currSportView.getHorizontalFadingEdgeLength());

										intent.setClass(page.getParent(), SocialUserNewsActivity.class);
							SocialActivityGroup activityStack = (SocialActivityGroup) page.getParent();
							if(activityStack.getStack().contains("SocialUserNewsActivity")){
								activityStack.pushInstead2("SocialUserNewsActivity", intent);		
							}else{
								activityStack.push("SocialUserNewsActivity", intent);		
							}
										
				}
			});

		
			//coment button
			Button messageButton = (Button) rowView.findViewById(R.id.buttonMessage);

			if (messageButton != null) {
				messageButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Button rbut = (Button) v;
						TextView tvi = (TextView) ((View) rbut.getParent())
									.findViewById(R.id.textViewNewsId);	
						TextView tvoi = (TextView) ((View) rbut.getParent())
								.findViewById(R.id.ownerNewsId);		
						page.showingComentDialog(tvi.getText().toString(), tvoi.getText().toString());						
					}
				});
				messageButton.setId(Integer.parseInt(userId));
			}
			

			if (removeButton != null) {
				removeButton.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						Button rbut = (Button) v;
						TextView tvi = (TextView) ((View) rbut.getParent())
									.findViewById(R.id.textViewNewsId);	
						TextView tvui = (TextView) ((View) rbut.getParent())
								.findViewById(R.id.textViewUserId);	
						if(String.valueOf(SaveUtils.getUserUnicId(context)).equals(tvui.getText().toString())){
							page.showingDialogCancel(tvi.getText().toString());
						}
					}
				});
			
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
			ImageView comentsmallicon = (ImageView) rowView.findViewById(R.id.comenticon);
			if (user.getIsComent()) {
				newsView.setTextColor(Color.BLACK);
				newsView.setTextColor(Color.BLACK);
				rowView.setPadding(10, 0, 0, 0);
				if (messageButton != null) {
					messageButton.setVisibility(View.GONE);
				}
				comentsmallicon.setVisibility(View.VISIBLE);
				
			}else{
				newsView.setTextColor(Color.RED);
				newsView.setTextColor(Color.RED);
				rowView.setBackgroundColor(Color.WHITE);
				rowView.findViewById(R.id.mainLayout).setBackgroundColor(Color.WHITE);
				rowView.setPadding(0, 0, 0, 0);
				if (messageButton != null) {
					messageButton.setVisibility(View.VISIBLE);
				}
				comentsmallicon.setVisibility(View.GONE);
			}
		}
		
		return rowView;
	}
		
	
}
