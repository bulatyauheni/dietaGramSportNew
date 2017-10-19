package bulat.diet.helper_sport.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import bulat.diet.helper_sport.R;

public class DataBaseImportActivity extends BaseImportActivity {
	public static final String FILENAME_PATTERN = "dishbase";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				bulat.diet.helper_sport.R.layout.statistics_import, null);
		setContentView(viewToLoad);	
		ctx = this;
		///
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);				
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});	
		
		dialogClickListener = new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					try {
						new ImportDataBaseCSVTask().execute("");
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					selectedItemId = null;
					break;
				}
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();		
		initView(FILENAME_PATTERN);		
	}
	
}