package bulat.diet.helper_sport.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class SinchProvider extends ContentProvider{
	
	private static final String SINCHLIST_URI_STRING = "content://bulat.diet.helper_sport.db.SinchProvider/list_items";	
	public static final Uri SINCHLIST_CONTENT_URI = Uri.parse(SINCHLIST_URI_STRING);	

	private static final String DATABASE_NAME = "sinch.db";
	private static final int DATABASE_VERSION = 1;
		
	private static final String SINCHS_TABLE_NAME = "sinchs";

	//sinchs table
	public static final String SINCH_ID = "_id";
	public static final String URL = "url";
	public static final String PARAMS = "params";
	public static final String TYPE = "type";

	private static final int SINCHLIST = 28;
	private static final UriMatcher uriMatcher;
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("bulat.diet.helper_sport.db.SinchProvider", "list_items", SINCHLIST);       		
	}

	private DatabaseHelper dbHelper;
	private SQLiteDatabase sinchsDB;
	
	public static class DatabaseHelper extends SQLiteOpenHelper {
		
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + SINCHS_TABLE_NAME + " (_id INTEGER PRIMARY KEY," + URL
					+ " TEXT," + PARAMS + " TEXT,"  + TYPE + " TEXT" + ");");
			}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}						
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (uriMatcher.match(uri)) {

		case SINCHLIST:
			qb.setTables(SINCHS_TABLE_NAME);
			break;		
		}

		// If no sort order is specified sort by date / time
		String orderBy;
		if (TextUtils.isEmpty(sort)) {
			orderBy = "_id";
		} else {
			orderBy = sort;
		}

		// Apply the query to the underlying database.
		Cursor c = qb.query(sinchsDB, projection, selection, selectionArgs,
				null, null, orderBy);
		// Register the contexts ContentResolver to be notified if
		// the cursor result set changes.
		c.setNotificationUri(getContext().getContentResolver(), uri);
		// Return a cursor to the query result.
		return c;
	}

	@Override
	public Uri insert(Uri _uri, ContentValues _initialValues) {
		// Insert the new row, will return the row number if
		// successful.
		final int match = uriMatcher.match(_uri);
        switch (match) {
        	case SINCHLIST: {
        		long rowID = sinchsDB.insert(SINCHS_TABLE_NAME, "quake",
        				_initialValues);
        		// Return a URI to the newly inserted row on success.
        		if (rowID > 0) {
        			Uri uri = ContentUris.withAppendedId(SINCHLIST_CONTENT_URI, rowID);
        			getContext().getContentResolver().notifyChange(uri, null);
        			return uri;
        		}
            }
            
        }
		
		throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
		case SINCHLIST:
			count = sinchsDB.delete(SINCHS_TABLE_NAME, where, whereArgs);
			break;
		
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
		
		case SINCHLIST:
			count = sinchsDB.update(SINCHS_TABLE_NAME, values, where,
					whereArgs);
			break;		
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri _uri) {
		switch (uriMatcher.match(_uri)) {
		
		
		case SINCHLIST:
			return "vnd.SinchProvider.cursor.item/itemscontent";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + _uri);
		}
	}

	
	
	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		sinchsDB = dbHelper.getWritableDatabase();
		return (sinchsDB != null);
	}

}
