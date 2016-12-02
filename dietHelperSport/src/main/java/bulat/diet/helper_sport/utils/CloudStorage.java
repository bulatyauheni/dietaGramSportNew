package bulat.diet.helper_sport.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import bulat.diet.helper_sport.R;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

/**
 * Simple wrapper around the Google Cloud Storage API
 */
public class CloudStorage {

	private static Properties properties;
	private static Storage storage;

	private static final String PROJECT_ID_PROPERTY = "project.id";
	private static final String APPLICATION_NAME_PROPERTY = "application.name";
	private static final String ACCOUNT_ID_PROPERTY = "account.id";
	private static final String PRIVATE_KEY_PATH_PROPERTY = "private.key.path";

	/**
	 * Uploads a file to a bucket. Filename and content type will be based on
	 * the original file.
	 * 
	 * @param bucketName
	 *            Bucket where file will be uploaded
	 * @param filePath
	 *            Absolute path of the file to upload
	 * @throws Exception
	 */
	public static void uploadFile(String bucketName, String filePath, Context context)
			throws Exception {

		Storage storage = getStorage(context);

		StorageObject object = new StorageObject();
		object.setBucket(bucketName);
		
		File sd = Environment.getExternalStorageDirectory();
		File file = new File(sd, filePath);
		
		InputStream stream = new FileInputStream(file);
		try {
			String contentType = URLConnection
					.guessContentTypeFromStream(stream);
			InputStreamContent content = new InputStreamContent(contentType,
					stream);

			Storage.Objects.Insert insert = storage.objects().insert(
					bucketName, null, content);
			insert.setName(file.getName());

			insert.execute();
		} finally {
			stream.close();
		}
	}
	
	public static void downloadFile(String bucketName, String fileName, String destinationDirectory, Context context) throws Exception {		
		File sd = Environment.getExternalStorageDirectory();
		File file = new File(sd, fileName);
		
		Storage storage = getStorage(context);

		InputStream mInput = ObjectsDownloadExample.download(storage, bucketName, fileName);

			String outFileName = fileName;
		    Log.d("Ahtumg Minen","Output:"+outFileName);
		    File createOutFile = new File(sd, outFileName);
		    OutputStream mOutput = new FileOutputStream(createOutFile);
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

	/**
	 * Deletes a file within a bucket
	 * 
	 * @param bucketName
	 *            Name of bucket that contains the file
	 * @param fileName
	 *            The file to delete
	 * @throws Exception
	 */
	public static void deleteFile(String bucketName, String fileName, Context context)
			throws Exception {

		Storage storage = getStorage(context);
		
		storage.objects().delete(bucketName, fileName).execute();
	}

	/**
	 * Creates a bucket
	 * 
	 * @param bucketName
	 *            Name of bucket to create
	 * @throws Exception
	 */
	public static void createBucket(String bucketName, Context context) throws Exception {

		Storage storage = getStorage(context);

		Bucket bucket = new Bucket();
		bucket.setName(bucketName);
		//getProperties().getProperty(PROJECT_ID_PROPERTY), bucket
		storage.buckets().insert(
				getProperties(context).getProperty(PROJECT_ID_PROPERTY), 
				bucket
				).execute();
	}
	
	/**
	 * Deletes a bucket
	 * 
	 * @param bucketName
	 *            Name of bucket to delete
	 * @throws Exception
	 */
	public static void deleteBucket(String bucketName, Context context) throws Exception {

		Storage storage = getStorage(context);
		
		storage.buckets().delete(bucketName).execute();
	}
	
	/**
	 * Lists the objects in a bucket
	 * 
	 * @param bucketName bucket name to list
	 * @return Array of object names
	 * @throws Exception
	 */
	public static List<String> listBucket(String bucketName, Context context) throws Exception {
		
		Storage storage = getStorage(context);
		
		List<String> list = new ArrayList<String>();
		
		List<StorageObject> objects = storage.objects().list(bucketName).execute().getItems();
		if(objects != null) {
			for(StorageObject o : objects) {
				list.add(o.getName());
			}
		}
		
		return list;
	}
	
	/**
	 * List the buckets with the project
	 * (Project is configured in properties)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> listBuckets(Context context) throws Exception {
		
		Storage storage = getStorage(context);
		
		List<String> list = new ArrayList<String>();
		
		List<Bucket> buckets = storage.buckets().list(getProperties(context).getProperty(PROJECT_ID_PROPERTY)).execute().getItems();
		if(buckets != null) {
			for(Bucket b : buckets) {
				list.add(b.getId());
			}
		}
		
		return list;
	}

	private static Properties getProperties(Context context) throws Exception {

		if (properties == null) {
			// Read from the /res/raw directory
			try {
				Resources resources = context.getResources();
			    InputStream rawResource = resources.openRawResource(R.raw.cloudstorage);
			    properties = new Properties();
			    properties.load(rawResource);
			    System.out.println("The properties are now loaded");
			    System.out.println("properties: " + properties);
			} catch (IOException e) {
			    System.err.println("Failed to open microlog property file");
			} 
		}
		return properties;
	}

	private static Storage getStorage(Context context) throws Exception {

		if (storage == null) {

			HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();

			List<String> scopes = new ArrayList<String>();
			scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
		//	Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.dietagrame9d2f531f351);
			File p12 = InputStreamToFileApp.convert(context.getResources().openRawResource(R.raw.dietagrame9d2f531f351));
			Credential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(jsonFactory)
					.setServiceAccountId(
							getProperties(context).getProperty(ACCOUNT_ID_PROPERTY))
					.setServiceAccountPrivateKeyFromP12File(p12)
					.setServiceAccountScopes(scopes).build();
			Uri url = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.dietagrame9d2f531f351);
			File file = new File(url.toString());
			storage = new Storage.Builder(httpTransport, jsonFactory,
					credential).setApplicationName(
					getProperties(context).getProperty(APPLICATION_NAME_PROPERTY))
					.build();
		}

		return storage;
	}
}
