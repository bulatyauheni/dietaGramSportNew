package bulat.diet.helper_sport.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.TopCursorAdapter;
import bulat.diet.helper_sport.db.TodayDishHelper;

public class Top10 extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.top10, null);
		
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);				
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setContentView(viewToLoad);	
		}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cursor c = TodayDishHelper.getTopDishesCursor(this);
		
		ListView listView = (ListView) findViewById(R.id.listViewTopDishes);
		
		TopCursorAdapter adapter = new TopCursorAdapter(this, c);//new TopArrayAdapter(getApplicationContext(),R.layout.dish_list_row, items);		 
		listView.setAdapter(adapter);
		listView.setTextFilterEnabled(true);	
		adapter.notifyDataSetChanged();
		}

}
