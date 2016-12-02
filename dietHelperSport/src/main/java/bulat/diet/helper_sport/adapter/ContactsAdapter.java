package bulat.diet.helper_sport.adapter;



import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.ChatActivity;
import bulat.diet.helper_sport.activity.SocialActivityGroup;
import bulat.diet.helper_sport.activity.SocialDishActivity;
import bulat.diet.helper_sport.activity.SocialUserListActivity;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.SaveUtils;


public class ContactsAdapter extends CursorAdapter{
	SocialActivityGroup parent;
	private Context ctx;
	private TreeMap<String,DishType> newMessUsersId;
	public ContactsAdapter(Context context, Cursor c, TreeMap<String,DishType> newMessUsersId, SocialActivityGroup parent) {
		super(context, c);
		ctx = context;
		this.parent = parent;
		this.newMessUsersId = newMessUsersId;
		// TODO Auto-generated constructor stub
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.social_contacts_list_row, parent, false);
		Button messageButton = (Button) v.findViewById(R.id.buttonMessage);
		LinearLayout ll =  (LinearLayout) v.findViewById(R.id.lastMessageLayout);
		if (messageButton != null) {
			messageButton.setOnClickListener(listener);
			messageButton.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DishProvider.USER_ID))));
		}
		if (ll != null) {
			ll.setOnClickListener(listener);
			ll.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DishProvider.USER_ID))));
		}
		
		return v;
		//return inflater.inflate(R.layout.item_list_row, null);
	}


    @Override
    public int getCount() {
        if (super.getCount() != 0){
            return super.getCount();
        }
        return 0;
    }


	@Override
	public void bindView(View v, Context context, Cursor c) {		  
		try{ 
		String itemUserName = c.getString(c.getColumnIndex(DishProvider.USER_NAME));	   
	      String itemUserId = c.getString(c.getColumnIndex(DishProvider.USER_ID));
	      TextView nameTextView = (TextView)v.findViewById(R.id.textViewName);	     
	      nameTextView.setText(itemUserName);
	      TextView idView = (TextView)v.findViewById(R.id.textViewUserId);
	      idView.setText(itemUserId);
	      if(newMessUsersId != null){
	    	  if(newMessUsersId.containsKey(itemUserId)){
	    		  TextView newMessageView = (TextView)v.findViewById(R.id.textViewNewMessage);	      
	    		  newMessageView.setVisibility(View.VISIBLE);
	    		  TextView newMessageViewValue = (TextView)v.findViewById(R.id.textViewNewMessageValue);	      
	    		  newMessageViewValue.setVisibility(View.VISIBLE);
	    		  newMessageViewValue.setText(String.valueOf(newMessUsersId.get(itemUserId).getTypeKey()));
	    		  TextView mv = (TextView)v.findViewById(R.id.textViewMessage);	      
	    		  mv.setVisibility(View.GONE);
	    		  TextView md = (TextView)v.findViewById(R.id.textViewDate);	      
	    		  md.setVisibility(View.GONE);
	    		  v.setBackgroundColor(R.color.light_red);
	    	  }
	      }
	      TextView text = (TextView)v.findViewById(R.id.textViewMessage);
	      text.setText(c.getString(c.getColumnIndex(DishProvider.MESSAGE_TEXT)));
	      TextView textViewDate = (TextView)v.findViewById(R.id.textViewDate);
	      textViewDate.setText( c.getString(c.getColumnIndex(DishProvider.DATE)));
	 	}catch (Exception e) {
			// TODO: handle exception
	 		e.printStackTrace();
		}
	}
	
	private View.OnClickListener listener =  new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(SaveUtils.getUserUnicId(ctx)!=0){
				
				TextView tvi = (TextView) ((View) v.getParent())
						.findViewById(R.id.textViewUserId);
				TextView nameTextView = (TextView) ((View) v.getParent()).findViewById(R.id.textViewName);

				String selectedInemName = nameTextView.getText().toString();
				String selectedInemId = tvi.getText().toString();
				Intent intent = new Intent();
				intent.putExtra(SocialDishActivity.USERID, selectedInemId);
				intent.putExtra(SocialDishActivity.USERNAME, selectedInemName);

				try {					
					intent.setClass(ctx, ChatActivity.class);
					SocialActivityGroup activityStack = (SocialActivityGroup) ContactsAdapter.this.parent;
					activityStack.push("ChatActivity", intent);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else{
				Intent intent = new Intent();
				intent.setClass(ctx, SocialUserListActivity.class);
				SocialActivityGroup activityStack = (SocialActivityGroup) ContactsAdapter.this.parent;
				activityStack.push("UserListActivity", intent);
			}
			
		}
	};
	
}

