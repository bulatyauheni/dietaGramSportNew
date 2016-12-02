package bulat.diet.helper_sport.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DatabaseHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.utils.CloudStorage;
import bulat.diet.helper_sport.utils.SaveUtils;

public class StatisticExportActivity extends Activity {
	public static final String BUCKUP_FILENAME = "backupDietagram.db";
	Context ctx = null;
	TextView filename;

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
				new ExportDatabaseTask().execute("");
			}
		});

		// requestBackup();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (SaveUtils.getLastExportedFileName(StatisticExportActivity.this)
				.length() > 1) {
			filename.setText(getString(R.string.saved_history)
					+ " "
					+ SaveUtils
							.getLastExportedFileName(StatisticExportActivity.this));
		}
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

	class ExportDatabaseTask extends AsyncTask<String, Void, Boolean>

	{

		private final ProgressDialog dialog = new ProgressDialog(
				StatisticExportActivity.this.getParent().getParent());

		// can use UI thread here

		@Override
		protected void onPreExecute()

		{

			this.dialog.setMessage("Exporting database...");

			this.dialog.show();

		}

		// automatically done on worker thread (separate from UI thread)

		protected Boolean doInBackground(final String... args)

		{

			try {
				DatabaseHelper dbhelp = new DatabaseHelper(
						StatisticExportActivity.this);
				dbhelp.getBase(BUCKUP_FILENAME);

				dbhelp.uploadDBOnCloud(BUCKUP_FILENAME);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		// can use UI thread here

		@Override
		protected void onPostExecute(final Boolean success)

		{

			if (this.dialog.isShowing())

			{

				this.dialog.dismiss();

			}

			if (success)

			{

				Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT)
						.show();

				filename.setText(getString(R.string.saved_history)
						+ " "
						+ SaveUtils
								.getLastExportedFileName(StatisticExportActivity.this));
			}

			else

			{

				Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();

			}

		}

	}

}
