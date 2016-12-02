package bulat.diet.helper_sport.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

import android.content.Context;
import android.os.AsyncTask;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.item.TodayDish;

public class SocialUpdater extends AsyncTask<Void, Void, Void> {

	private Context context;
	private TodayDish todayDish = null;
	private Boolean isDishUpdate = false;
	private String removeId = null;
	private Boolean isClearing = false;;

	public SocialUpdater(Context context) {
		this.context = context;
	}

	// constructor for clearing request base
	public SocialUpdater(Context context, Boolean isClearing) {
		this.context = context;
		this.isClearing = isClearing;
	}

	// constructor for insert/update dish
	public SocialUpdater(Context context, TodayDish dish, Boolean isUpdate) {
		this.context = context;
		this.todayDish = dish;
		this.isDishUpdate = isUpdate;
	}

	// constructor for delete dish
	public SocialUpdater(Context context, String id) {
		this.context = context;
		this.removeId = id;
		todayDish = new TodayDish();
		todayDish.setId(removeId);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (!isClearing) {
			// clearing)
			fullUpdate();
			if (todayDish == null) {
				if (UpdateBodyPrams()) {
					// body params updated
					SaveUtils.saveRequestBody("", context);
				} else {
					// update failed - saving request
					SaveUtils.saveRequestBody("text", context);
				}
			} else {
				if (!UpdateCalendar(null)) {
					// update failed - saving request
					SaveUtils.saveRequestDish(SaveUtils.getRequestDish(context)
							+ parameters.toString(), context);
				}
			}
		} else {
			// clearing)
			fullUpdate();
		}
		return null;
	}

	private void fullUpdate() {
		if (!"".equals(SaveUtils.getRequestBody(context))) {
			if (UpdateBodyPrams()) {
				// body params updated
				SaveUtils.saveRequestBody("", context);
			} else {
				SaveUtils.saveRequestBody("text", context);
			}
		}
		if (!"".equals(SaveUtils.getRequestDish(context))) {
			String template = SaveUtils.getRequestDish(context);
			SaveUtils.saveRequestDish("", context);
			String[] queries = template.split(SaveUtils.SEPARATOR);
			int customLimit = SaveUtils.getCustomLimit(context);
			if(customLimit>0){
				SaveUtils.saveBMR(String.valueOf(customLimit), context);
				SaveUtils.saveMETA(String.valueOf(customLimit), context);
			}
			for (int i = 0; i < queries.length; i++) {
				if (!UpdateCalendar(queries[i])) {
					for (int j = i; j < queries.length; j++) {
						SaveUtils.saveRequestDish(queries[j], context);
					}
					i = queries.length;
				} 
			}
		}
		
	}

	protected void onPostExecute(String unused) {
	}

	protected boolean UpdateBodyPrams() {
		if (NetworkState.isOnline(context)) {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			// searchString = searchString.replaceAll(" ", "%20");
			StringBuffer parametersb = new StringBuffer("");
			parametersb.append("?updateId=" + SaveUtils.getUserUnicId(context));
			parametersb.append("&weight="
					+ (SaveUtils.getRealWeight(context)));
			parametersb.append("&high="
					+ (SaveUtils.getHeight(context) + Info.MIN_HEIGHT));
			parametersb.append("&age="
					+ (SaveUtils.getAge(context) + Info.MIN_AGE));
			parametersb.append("&sex=" + (SaveUtils.getSex(context)));
			parametersb.append("&activity=" + (SaveUtils.getActivity(context)));
			try {
				parametersb.append("&version=" + URLEncoder.encode(Constants.VERSION, "UTF-8"));
			
				parametersb.append("&username=" + URLEncoder.encode(SaveUtils
						.getNikName(context), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			
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
					if (Integer.valueOf(resultString) > 0) {

					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			// set flag that app need update profile when network will
			// be available
			return false;
		}
		return true;
	}

	StringBuffer parameters = new StringBuffer("");

	protected boolean UpdateCalendar(String params) {
		parameters = new StringBuffer("");
		StringBuilder builder = new StringBuilder();
		
		// searchString = searchString.replaceAll(" ", "%20");
		if (params == null) {
			parameters.append("?user_dish_id="
					+ SaveUtils.getUserUnicId(context));
			if (removeId != null) {
				parameters.append("&remove_dish=1&" + todayDish.toString());
			} else if (isDishUpdate) {
				parameters.append("&update_dish=1&" + todayDish.toString());
			} else {
				parameters.append("&update_dish=&" + todayDish.toString() + "&limit="+SaveUtils.getBMR(context));
			}
		} else {
			parameters = new StringBuffer(params);
		}
		if (NetworkState.isOnline(context)) {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(Constants.URL_SOCIAL + parameters+ "&lang=" + SaveUtils.getLang(context));
			
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
					if (Integer.valueOf(resultString) > 0) {

					}
				} catch (Exception e) {
					e.printStackTrace();
					// save request for best times
					return false;
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		} else {
			// save request for best times
			return false;
		}
		return true;
	}
}