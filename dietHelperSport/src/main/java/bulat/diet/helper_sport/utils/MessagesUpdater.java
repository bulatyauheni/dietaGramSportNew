package bulat.diet.helper_sport.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import bulat.diet.helper_sport.activity.DishListActivity;
import bulat.diet.helper_sport.item.DishType;

public class MessagesUpdater extends AsyncTask<Void, Void, Void> {

	private Context context;
	int fullCount;
	private Handler handler;

	TreeMap<String,DishType> map = new TreeMap<String, DishType>();

	// constructor for clearing request base
	public MessagesUpdater(Context context, Handler handler){
        this.handler = handler;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {

		// clearing)
		fullUpdate();

		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		 Message m = new Message();
		 Bundle b = new Bundle();
		    b.putString("message",String.valueOf(fullCount));
		    m.setData(b);
		 handler.sendMessage(m);
	}

	protected boolean fullUpdate() {
		if (NetworkState.isOnline(context)) {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			// searchString = searchString.replaceAll(" ", "%20");
			StringBuffer parametersb = new StringBuffer("");
			parametersb.append("?check_new_messages=" + SaveUtils.getUserUnicId(context));
			HttpGet httpGet = new HttpGet(Constants.URL_SOCIAL + parametersb+ "&lang=" + SaveUtils.getLang(context));
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
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} else {
					return false;
				}
				String resultString = builder.toString().trim();
				try {
					
					JSONObject jsonRoot = new JSONObject(resultString);
					JSONArray jsonArray = new JSONArray(
							jsonRoot.getString("updates"));
					
					for (int i = 0; i < jsonArray.length(); i++) {						
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						fullCount = fullCount +	Integer.parseInt(jsonObject.getString("count"));
						map.put(jsonObject.getString("user_from_id"),
								new DishType(
										Integer.parseInt(jsonObject.getString("count")), 
										jsonObject.getString("user_name")));
					}
					//SaveUtils.saveNewMessagesCount();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// set flag that app need update profile when network will
			// be available
			return false;
		}
		return true;
	}	
}