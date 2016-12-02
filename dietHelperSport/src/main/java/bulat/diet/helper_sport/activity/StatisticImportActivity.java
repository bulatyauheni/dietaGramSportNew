package bulat.diet.helper_sport.activity;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DatabaseHelper;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.TodayDish;

public class StatisticImportActivity extends Activity {
	Context ctx = null;
	private String selectedInemId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.statistics_import, null);
		setContentView(viewToLoad);
		ctx = this;
		// /
		Button buttonFind = (Button) viewToLoad.findViewById(R.id.buttonFind);
		buttonFind.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					new ImportDatabaseTask().execute("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});

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

		File filePath = Environment.getExternalStorageDirectory();
		String[] list;
		ArrayList<String> files = new ArrayList<String>();
		list = filePath.list(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".db");
			}
		});

		if (list != null) {
			Arrays.sort(list, new AlphabeticComparator());
			ListView listView = (ListView) findViewById(R.id.listViewStatistics);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_single_choice, list) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View view = super.getView(position, convertView, parent);

					TextView textView = (TextView) view
							.findViewById(android.R.id.text1);

					/* YOUR CHOICE OF COLOR */
					textView.setTextColor(Color.DKGRAY);

					return view;
				}
			};
			listView.setAdapter(adapter);
			listView.setTextFilterEnabled(true);

			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					// selectedInemId = arg1
					CheckedTextView textView = (CheckedTextView) arg1;
					selectedInemId = textView.getText().toString();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							StatisticImportActivity.this.getParent()
									.getParent());
					builder.setMessage(R.string.history_download)
							.setPositiveButton(getString(R.string.yes),
									dialogClickListener)
							.setNegativeButton(getString(R.string.no),
									dialogClickListener).show();

				}
			});
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

	class ImportDatabaseTask extends AsyncTask<String, Void, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(
				StatisticImportActivity.this.getParent().getParent());

		// can use UI thread here
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Importing database...");
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)

		protected Boolean doInBackground(final String... args) {
			try {
				DatabaseHelper dbhelp = new DatabaseHelper(
						StatisticImportActivity.this);
				dbhelp.downloadDBFromCloud(StatisticExportActivity.BUCKUP_FILENAME);
				dbhelp.restoreDB(StatisticExportActivity.BUCKUP_FILENAME,
						StatisticImportActivity.this.getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		// can use UI thread here

		@Override
		protected void onPostExecute(final Boolean success) {
			if (this.dialog.isShowing()) {

				this.dialog.dismiss();
			}
			if (success) {
				Toast.makeText(ctx, "Import successful!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(ctx, "Import failed", Toast.LENGTH_SHORT).show();
			}
		}
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					new ImportDatabaseTask().execute("");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				selectedInemId = null;
				break;
			}
		}
	};
}

class DirFilter implements FilenameFilter {
	private Pattern pattern;

	public DirFilter(String reg) {
		pattern = Pattern.compile(reg);
	}

	public boolean accept(File dir, String name) {
		// Strip path information, search for regex:
		return pattern.matcher(new File(name).getName()).matches();
	}
}

class AlphabeticComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		String str1 = (String) o1;
		String str2 = (String) o2;
		return str1.toLowerCase().compareTo(str2.toLowerCase());
	}
}
