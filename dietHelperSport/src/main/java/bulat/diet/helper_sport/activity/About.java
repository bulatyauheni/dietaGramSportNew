package bulat.diet.helper_sport.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.NetworkState;
import bulat.diet.helper_sport.utils.SaveUtils;

public class About extends Activity {

		private EditText anwer;
		private Button okbutton;

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.about, null);
		setContentView(viewToLoad);	
		//anwer = (EditText) viewToLoad.findViewById(R.id.answer);
		ImageView email = (ImageView)viewToLoad.findViewById(R.id.imageViewEmail);
		email.setOnClickListener(new OnClickListener() {
		

			public void onClick(View v) {
				 	Intent emailIntent = new Intent(Intent.ACTION_SEND);
				    emailIntent.putExtra(Intent.EXTRA_EMAIL, "email: bulat.yauheni@gmail.com");
				    emailIntent.setType("plain/text");
				    final PackageManager pm = getPackageManager();
				    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
				    ResolveInfo best = null;
				    for(final ResolveInfo info : matches)
				        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
				            best = info;
				    if (best != null)
				        emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
				    try{
				    startActivity(emailIntent);
				    }catch(Exception e){
				    	e.printStackTrace();
				    }
			}
		});	

		ImageView textViewFacebook = (ImageView)viewToLoad.findViewById(R.id.imageViewFacebook);
		textViewFacebook.setOnClickListener(new OnClickListener() {
		

			public void onClick(View v) {
				Uri uri = Uri.parse("https://www.facebook.com/yauheni.bulat");
				 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				 getParent().startActivity(intent);
			}
		});	
		/*okbutton = (Button) viewToLoad.findViewById(R.id.buttonYes);
		okbutton.setOnClickListener(new OnClickListener() {
		

			public void onClick(View v) {
				if(anwer.getText().length() > 5){
					new AddTask().execute();
				}
			}
		});	*/

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
		}

	private class AddTask extends AsyncTask<Void, Void, Void> {

		private String addString;

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected Void doInBackground(Void... params) {

			if (NetworkState.isOnline(getApplicationContext())) {
				HttpClient client = new DefaultHttpClient();
				addString = anwer.getText().toString();
				addString = addString.replaceAll(" ", "%20");
				try {
					addString = URLEncoder.encode(addString,"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HttpGet httpGet = new HttpGet(
						Constants.URL_DISHBASE + "?answer=" + addString + "&users_ansver_id=" + SaveUtils.getUserUnicId(About.this));
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is established.
				// The default value is zero, that means the timeout is not used. 
				int timeoutConnection = 3000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT) 
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				try {
					HttpResponse response = client.execute(httpGet);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						
					} else {

					}					
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// return builder.toString();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			anwer.setText("");
		}

	}

}
