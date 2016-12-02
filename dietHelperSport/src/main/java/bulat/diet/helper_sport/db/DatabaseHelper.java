package bulat.diet.helper_sport.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.regex.Pattern;

import bulat.diet.helper_sport.activity.StatisticExportActivity;
import bulat.diet.helper_sport.utils.CloudStorage;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static String TAG = "TAG";
	private static String DB_PATH = "/data/data/bulat.diet.helper_sport/databases/";
	private static String DB_NAME = "dishessport.db";
	private SQLiteDatabase mDataBase; 
	private final Context mContext;

	public DatabaseHelper(Context context) 
	{
	    super(context, DB_NAME, null, 1);
	    DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	    this.mContext = context;
	}   
	
	public void requestBackup() {
		try {
			//CloudStorage.createBucket("diate-87", this);

			CloudStorage.downloadFile("diate-87", "backupname.db", "", mContext);

			List<String> buckets = CloudStorage.listBuckets(mContext);

			List<String> files = CloudStorage.listBucket("my-bucket", mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadDBOnCloud(String fileName) {
		try {
			//create buccket is not exist
			CloudStorage.deleteFile(getEmail(), fileName, mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//create buccket is not exist
			CloudStorage.createBucket(getEmail(), mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			CloudStorage.uploadFile(getEmail(), fileName, mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadDBFromCloud( String fileName) {	
		try {
			CloudStorage.downloadFile(getEmail(), fileName, "", mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void createDataBase() throws IOException
	{
	    boolean mDataBaseExist = checkDataBase();
	    Log.d(TAG,"create DB in Helper. Data exists?"+mDataBaseExist);
	    if(!mDataBaseExist)
	    {
	        Log.d(TAG,"get Writable in DatabaseHelper");
	        this.getWritableDatabase();
	        try 
	        {
	            Log.d(TAG,"copy Database");
	            copyDataBase();
	        } 
	        catch (IOException mIOException) 
	        {Log.d(TAG,"copy not succeed");
	            throw new Error("ErrorCopyingDataBase");

	        }
	    }
	}

	private boolean checkDataBase()
	{
	    SQLiteDatabase mCheckDataBase = null;
	    try
	    {
	        String myPath = DB_PATH + DB_NAME;
	        mCheckDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	    }
	    catch(SQLiteException mSQLiteException)
	    {
	        Log.e(TAG, "DatabaseNotFound " + mSQLiteException.toString());
	    }

	    if(mCheckDataBase != null)
	    {
	        mCheckDataBase.close();
	    }
	    return mCheckDataBase != null;
	}

	private void copyDataBase() throws IOException
	{
	    Log.d(TAG,"copy");
	    InputStream mInput = mContext.getResources().getAssets().open(DB_NAME);

	    String outFileName = DB_PATH + DB_NAME;
	    Log.d(TAG,"Output:"+outFileName);
	    File createOutFile = new File(outFileName);
	    if(!createOutFile.exists()){
	        createOutFile.mkdir();
	    }
	    OutputStream mOutput = new FileOutputStream(outFileName);
	    byte[] mBuffer = new byte[1024];
	    int mLength;
	    while ((mLength = mInput.read(mBuffer))>0)
	    {
	        mOutput.write(mBuffer, 0, mLength);
	    }
	    mOutput.flush();
	    mOutput.close();
	    mInput.close();
	}

	public boolean openDataBase() throws SQLException
	{
	    String mPath = DB_PATH + DB_NAME;
	    mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	    return mDataBase != null;
	}

	@Override
	public synchronized void close() 
	{
	    if(mDataBase != null)
	        mDataBase.close();
	    super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{ }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	    Log.v(TAG, "UpgradingDatabase, This will drop current database and will recreate it");
	}
	
	public String getEmail() {	
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(mContext).getAccountsByType("com.google");
		String email = null;
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        email = account.name;
		    }
		}
		return email.split("@")[0].replaceAll("\\.", "");
	}
	
	public void getBase(String backupDBPath) throws IOException {	
		     File sd = Environment.getExternalStorageDirectory();
		     File data = Environment.getDataDirectory();
		     if (sd.canWrite()) {
		         String currentDBPath = DB_PATH + DB_NAME;
		         File currentDB = new File(currentDBPath);
		         File backupDB = new File(sd, backupDBPath);

		         if (currentDB.exists()) {
		             FileChannel src = new FileInputStream(currentDB).getChannel();
		             FileChannel dst = new FileOutputStream(backupDB).getChannel();
		             dst.transferFrom(src, 0, src.size());
		             src.close();
		             dst.close();
		         }
		     }
		
	}
	
	public void restoreDB(String backupDBPath, Context context) throws IOException {
			  File sd = Environment.getExternalStorageDirectory();
			  File data = Environment.getDataDirectory();
			  if (sd.canWrite()) {
			    String currentDBPath = DB_PATH + DB_NAME;
		        File currentDB = new File(currentDBPath);
		        File backupDB = new File(sd, backupDBPath);

		        if (currentDB.exists()) {
		            FileChannel src = new FileInputStream(backupDB).getChannel();
		            FileChannel dst = new FileOutputStream(currentDB).getChannel();
		            dst.transferFrom(src, 0, src.size());
		            src.close();
		            dst.close();
		        }
		    }
	}
}
