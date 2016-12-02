package bulat.diet.helper_sport.adapter;



import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.utils.SaveUtils;


public class MessagesAdapter extends CursorAdapter{
	String id = "";
	private Context ctx;
	public MessagesAdapter(Context context, Cursor c) {
		super(context, c);
		ctx = context;
		id = String.valueOf(SaveUtils.getUserUnicId(context));
		// TODO Auto-generated constructor stub
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.social_messages_list_row, parent, false);
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
		String date = c.getString(c.getColumnIndex(DishProvider.DATE));	 
		String itemUserName = c.getString(c.getColumnIndex(DishProvider.USER_NAME));
		String text = c.getString(c.getColumnIndex(DishProvider.MESSAGE_TEXT));	 
		
	    //  String itemUserId = c.getString(c.getColumnIndex(DishProvider.USER_ID));
	      TextView nameTextView = (TextView)v.findViewById(R.id.textViewName);	     
	      nameTextView.setText(itemUserName);
	      TextView dateView = (TextView)v.findViewById(R.id.textViewDate);
	      dateView.setText(date.toLowerCase());
	      TextView textTextView = (TextView)v.findViewById(R.id.textViewMessage);	     
	      textTextView.setText(text);
	      if(id.equals(c.getString(c.getColumnIndex(DishProvider.USER_ID_FROM)))){	    	       
		      nameTextView.setText(SaveUtils.getNikName(context));
	    	  v.setBackgroundResource(R.color.light_grey);	    	  
	      }else{
	    	  v.setBackgroundColor(Color.WHITE);
	      }
	 	}
	
}

