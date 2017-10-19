package bulat.diet.helper_sport.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.SaveUtils;

public class DataBaseExportActivity extends BaseExportActivity {
	Context ctx = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.statistics_export, null);
		setContentView(viewToLoad);
		ctx = this;
		// /
		filename = (TextView) viewToLoad.findViewById(R.id.textViewExportData);
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		Button saveButton = (Button) viewToLoad.findViewById(R.id.buttonSave);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new ExportDatabaseCSVTask(DataBaseImportActivity.FILENAME_PATTERN).execute("");
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (SaveUtils.getLastExportedDataBaseFileName(DataBaseExportActivity.this)
				.length() > 1) {
			filename.setText(getString(R.string.saved_history)
					+ " "
					+ SaveUtils
							.getLastExportedDataBaseFileName(DataBaseExportActivity.this));
		}
	}

}
