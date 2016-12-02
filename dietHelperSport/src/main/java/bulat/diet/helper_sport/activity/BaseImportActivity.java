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

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.TodayDish;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;

public class BaseImportActivity extends Activity {
    Context ctx = null;
    protected String selectedItemId;
    private ProgressDialog dialog;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		ctx = this;
		dialog = new ProgressDialog(
				this.getParent().getParent());
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
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	
	protected void initView(String fileNamePattern) {
		File filePath = Environment.getExternalStorageDirectory();
		String[] list;
		list = filePath.list(new DirFilter(fileNamePattern + ".+"));
		if(list != null){
			Arrays.sort(list, new AlphabeticComparator());		
			ListView listView = (ListView) findViewById(R.id.listViewStatistics);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		            this,android.R.layout.simple_list_item_single_choice, list){
				 @Override
			        public View getView(int position, View convertView,ViewGroup parent) {
			            View view =super.getView(position, convertView, parent);
	
			            TextView textView=(TextView) view.findViewById(android.R.id.text1);
	
			            /*YOUR CHOICE OF COLOR*/
			            textView.setTextColor(Color.DKGRAY);
	
			            return view;
			        }
		    };		 
			listView.setAdapter(adapter);
			listView.setTextFilterEnabled(true);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
	
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					//selectedInemId = arg1
					CheckedTextView textView = (CheckedTextView) arg1;
					selectedItemId = textView.getText().toString();
					AlertDialog.Builder builder = new AlertDialog.Builder(BaseImportActivity.this.getParent().getParent());
					builder.setMessage(R.string.history_download)
							.setPositiveButton(getString(R.string.yes), dialogClickListener)
							.setNegativeButton(getString(R.string.no), dialogClickListener)
							.show();
										
				}
			});
		}
	}
	
	void showDialog(boolean flag) {
		if (flag) {
			this.dialog.setMessage("Importing database...");
			this.dialog.show();
		} else {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}
	
	protected class ImportHistoryCSVTask extends AsyncTask<String, Void, Boolean> {
        // can use UI thread here
        @Override
        protected void onPreExecute() {
        	showDialog(true);
        }   

        // automatically done on worker thread (separate from UI thread)


        protected Boolean doInBackground(final String... args) 
        {                      
        	String next[] = {};
            List<TodayDish> list = new ArrayList<TodayDish>();
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");       
            if (!exportDir.exists()) 
            {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, selectedItemId);
            boolean first = true;
            Map<String, Integer> map = new TreeMap<String, Integer>();
            try {
                CSVReader reader = new CSVReader(new FileReader(file));
                for(;;) {
                	
                    next = reader.readNext();
                    if(next != null) {
                    	if(first){
                    		for (int i = 0; i < next.length; i++) {
                    			map.put(next[i], i);
                    			first=false;
							}                   		
                    	}else{
                    		list.add(new TodayDish(Float.valueOf(next[map.get(DishProvider.TODAY_DISH_ID)]), 
                    				next[map.get(DishProvider.TODAY_NAME)], 
                    				next[map.get(DishProvider.TODAY_DESCRIPTION)], 
                    				Integer.valueOf(next[map.get(DishProvider.TODAY_CALORICITY)]),
                    				next[map.get(DishProvider.TODAY_CATEGORY)], 
                    				Integer.valueOf(next[map.get(DishProvider.TODAY_DISH_WEIGHT)]), 
                    				Integer.valueOf(next[map.get(DishProvider.TODAY_DISH_CALORICITY)]), 
                    				next[map.get(DishProvider.TODAY_DISH_DATE)], 
                    				Long.valueOf(next[map.get(DishProvider.TODAY_DISH_DATE_LONG)]), 
                    				Integer.valueOf(next[map.get(DishProvider.TODAY_IS_DAY)]), 
                    				next[map.get(DishProvider.TODAY_TYPE)], 
                    				(map.get(DishProvider.TODAY_FAT) != null) ? Float.valueOf(next[map.get(DishProvider.TODAY_FAT)]) : 0, 
                    				(map.get(DishProvider.TODAY_FAT) != null) ?  Float.valueOf(next[map.get(DishProvider.TODAY_DISH_FAT)]) : 0 , 
                    				(map.get(DishProvider.TODAY_FAT) != null) ?  Float.valueOf(next[map.get(DishProvider.TODAY_CARBON)]) : 0 , 
                    				(map.get(DishProvider.TODAY_FAT) != null) ?  Float.valueOf(next[map.get(DishProvider.TODAY_DISH_CARBON)]) : 0, 
                    				(map.get(DishProvider.TODAY_FAT) != null) ?  Float.valueOf(next[map.get(DishProvider.TODAY_PROTEIN)]) : 0, 
                    				(map.get(DishProvider.TODAY_FAT) != null) ?  Float.valueOf(next[map.get(DishProvider.TODAY_DISH_PROTEIN)]) : 0, 
                    				(map.get(DishProvider.TODAY_FAT) != null) ?  Integer.valueOf(next[map.get(DishProvider.TODAY_DISH_TIME_HH)]) : 0, 
               						(map.get(DishProvider.TODAY_FAT) != null) ?  Integer.valueOf(next[map.get(DishProvider.TODAY_DISH_TIME_MM)]) : 0));
                    	}                       
                    } else {
                    	TodayDishHelper.importDays( list,  BaseImportActivity.this);
                        break;
                    }
                }
                reader.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;       
        }

        @Override
        protected void onPostExecute(final Boolean success) 
        {
        	showDialog(false);
            if (success) 
            {
                Toast.makeText(ctx, "Import successful!", Toast.LENGTH_SHORT).show();
            } 
            else 
            {
                Toast.makeText(ctx, "Import failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

	protected DialogInterface.OnClickListener dialogClickListener;
	
	protected class ImportDataBaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(BaseImportActivity.this.getParent().getParent());
        // can use UI thread here
        @Override
        protected void onPreExecute() 
        {
            this.dialog.setMessage("Importing database...");
            this.dialog.show();
        }   

        // automatically done on worker thread (separate from UI thread)


        protected Boolean doInBackground(final String... args) 
        {                      
        	String next[] = {};
            List<Dish> list = new ArrayList<Dish>();
            File exportDir = new File(Environment.getExternalStorageDirectory(), "");       
            if (!exportDir.exists()) 
            {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, selectedItemId);
            boolean first = true;
            Map<String, Integer> map = new TreeMap<String, Integer>();
            try {
                CSVReader reader = new CSVReader(new FileReader(file));
                for(;;) {
                	
                    next = reader.readNext();
                    if(next != null) {
                    	if(first){
                    		for (int i = 0; i < next.length; i++) {
                    			map.put(next[i], i);
                    			first=false;
							}                   		
                    	}else{
                    		list.add(new Dish( 
                    				next[map.get(DishProvider.NAME)],                 				
                    				next[map.get(DishProvider.DESCRIPTION)],
                    				Integer.valueOf(next[map.get(DishProvider.CALORICITY)]),
                    				Integer.valueOf(next[map.get(DishProvider.CATEGORY)]),
                    				1,
                    				map.get(DishProvider.POPULARITY) != null ? Integer.valueOf(next[map.get(DishProvider.POPULARITY)]) : 0, 
                    				next[map.get(DishProvider.TYPE)],
                    				next[map.get(DishProvider.FAT)],
                    				next[map.get(DishProvider.CARBON)],
                    				next[map.get(DishProvider.PROTEIN)],
                    				next[map.get(DishProvider.CATEGORY_NAME)], 
                    				next[map.get(DishProvider.BARCODE)],
                    				next[map.get(DishProvider.DISH_ID)])
                    		);
                    	}                       
                    } else {
                    	DishListHelper.importDays( list,  BaseImportActivity.this);
                        break;
                    }
                }
                reader.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;       
        }

        @Override
        protected void onPostExecute(final Boolean success) 
        {
            if (this.dialog.isShowing()) 
            {

                this.dialog.dismiss();
            }
            if (success) 
            {
                Toast.makeText(ctx, "Import successful!", Toast.LENGTH_SHORT).show();
            } 
            else 
            {
                Toast.makeText(ctx, "Import failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
