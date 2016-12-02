package bulat.diet.helper_sport.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.utils.SaveUtils;


public class BaseExportActivity extends Activity {
	TextView filename;
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		dialog = new ProgressDialog(
				BaseExportActivity.this.getParent().getParent());
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	void showDialog(boolean flag) {
		if (flag) {
			this.dialog.setMessage("Exporting database...");
			this.dialog.show();
		} else {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}
	
	protected class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
		
		String mfileNamePattern;
		public ExportDatabaseCSVTask(String fileNamePattern) {
			mfileNamePattern = fileNamePattern;
		}

		// can use UI thread here

		@Override
		protected void onPreExecute(){
			showDialog(true);
		}

		// automatically done on worker thread (separate from UI thread)

		protected Boolean doInBackground(final String... args)	{
			File exportDir = new File(
					Environment.getExternalStorageDirectory(), "");
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM", new Locale(
					getString(R.string.locale)));
			String date = sdf.format(new Date());
			String filename = mfileNamePattern + "_" + date + ".csv";
			File file = new File(exportDir, filename);
			try {
				file.createNewFile();
				CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
				Cursor curCSV = DishListHelper.getAllDishes(BaseExportActivity.this);
				csvWrite.writeNext(curCSV.getColumnNames());
				while (curCSV.moveToNext()) {
					String arrStr[] = { curCSV.getString(0),
							curCSV.getString(1), curCSV.getString(2),
							curCSV.getString(3), curCSV.getString(4),
							curCSV.getString(5), curCSV.getString(6),
							curCSV.getString(7), curCSV.getString(8),
							curCSV.getString(9), curCSV.getString(10),
							curCSV.getString(11), curCSV.getString(12),};
					csvWrite.writeNext(arrStr);
				}
				csvWrite.close();
				curCSV.close();
				SaveUtils.setLastExportedDataBaseFileName(filename,
						BaseExportActivity.this);
				return true;

			} catch (IOException e) {
				return false;
			}
		}

		// can use UI thread here

		@Override
		protected void onPostExecute(final Boolean success)	{
			showDialog(false);

			if (success) {
				Toast.makeText(BaseExportActivity.this, "Export successful!", Toast.LENGTH_SHORT)
						.show();
				filename.setText(getString(R.string.saved_history)
						+ " "
						+ SaveUtils.getLastExportedDataBaseFileName(BaseExportActivity.this));
			} else {
				Toast.makeText(BaseExportActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	protected class ExportHistoryCSVTask extends AsyncTask<String, Void, Boolean> {
		String mfileNamePattern;
		public ExportHistoryCSVTask(String fileNamePattern) {
			mfileNamePattern = fileNamePattern;
		}

		private final ProgressDialog dialog = new ProgressDialog(
				BaseExportActivity.this.getParent().getParent());

		// can use UI thread here

		@Override
		protected void onPreExecute(){
			showDialog(true);
		}

		// automatically done on worker thread (separate from UI thread)

		protected Boolean doInBackground(final String... args)	{
			File exportDir = new File(
					Environment.getExternalStorageDirectory(), "");
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM", new Locale(
					getString(R.string.locale)));
			String date = sdf.format(new Date());
			String filename = mfileNamePattern + "_" + date + ".csv";
			File file = new File(exportDir, filename);
			try {
				file.createNewFile();
				CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
				Cursor curCSV = TodayDishHelper.getAllDishes(
						BaseExportActivity.this,
						String.valueOf((new Date()).getTime() - 30
								* DateUtils.DAY_IN_MILLIS));
				csvWrite.writeNext(curCSV.getColumnNames());
				while (curCSV.moveToNext()) {
					String arrStr[] = { curCSV.getString(0),
							curCSV.getString(1), curCSV.getString(2),
							curCSV.getString(3), curCSV.getString(4),
							curCSV.getString(5), curCSV.getString(6),
							curCSV.getString(7), curCSV.getString(8),
							curCSV.getString(9), curCSV.getString(10),
							curCSV.getString(11), curCSV.getString(12),
							curCSV.getString(13), curCSV.getString(14),
							curCSV.getString(15), curCSV.getString(16),
							curCSV.getString(17), curCSV.getString(18),
							curCSV.getString(19), };
					csvWrite.writeNext(arrStr);
				}
				csvWrite.close();
				curCSV.close();
				SaveUtils.setLastExportedHistoryFileName(filename,
						BaseExportActivity.this);
				return true;

			} catch (IOException e) {
				return false;
			}
		}

		// can use UI thread here

		@Override
		protected void onPostExecute(final Boolean success)	{
			showDialog(false);

			if (success) {
				Toast.makeText(BaseExportActivity.this, "Export successful!", Toast.LENGTH_SHORT)
						.show();
				filename.setText(getString(R.string.saved_history)
						+ " "
						+ SaveUtils
								.getLastExportedHistoryFileName(BaseExportActivity.this));
			} else {
				Toast.makeText(BaseExportActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
