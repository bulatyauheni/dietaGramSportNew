package bulat.diet.helper_sport.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.TodayDish;

public class RecepyUpdater extends AsyncTask<Void, Void, Void> {

	private Context context;
	private TodayDish todayDish = null;
	private Boolean isDishUpdate = false;
	private String removeId = null;
	private Boolean isClearing = false;
	
	List<TodayDish> toSincList;
	
	public RecepyUpdater(Context context) {
		this.context = context;
		toSincList = TodayDishHelper.getUnsincDishes(context);
		
	}

	// constructor for clearing request base
	public RecepyUpdater(Context context, Boolean isClearing) {
		this.context = context;
		this.isClearing = isClearing;
	}

	// constructor for insert/update dish
	public RecepyUpdater(Context context, TodayDish dish, Boolean isUpdate) {
		this.context = context;
		this.todayDish = dish;
		this.isDishUpdate = isUpdate;
	}

	// constructor for delete dish
	public RecepyUpdater(Context context, String id) {
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
		InputStream inputStream = null;
        String result = "";
        StringBuilder builder = new StringBuilder();
        try {
 
        	    JSONObject Parent = new JSONObject();
        	    JSONArray array = new JSONArray();
        	    JSONObject version = new JSONObject();
        	    version.put("lastid", 0);  
        	    JSONObject userid = new JSONObject();
        	    userid.put("userid", SaveUtils.getUserUnicId(context));
        	    Parent.put("data", array);       
        	    Parent.put("version", version);
        	    Parent.put("userid", userid);
        	    
        	    HttpClient client = new DefaultHttpClient();
        	    HttpPost post = new HttpPost("http://calorycman.net/server.php");
        	    StringEntity se = new StringEntity(Parent.toString());  //new ByteArrayEntity(json.toString().getBytes(            "UTF8"))
        	    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        	    post.setHeader("Accept", "application/json");
        	    post.setHeader("Content-type", "application/json");
        	    post.setEntity(se);
        	    HttpResponse response = client.execute(post);
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
				}
				String resultString = builder.toString().trim();
				try {
					JSONObject jsonRoot = new JSONObject(resultString);
					JSONArray jsonArray = new JSONObject(jsonRoot.getString("update")).getJSONArray("dishes");			
					
					JSONArray mapArray = new JSONObject(jsonRoot.getString("update")).getJSONArray("map");				
					
					ArrayList<TodayDish> newDishes = new ArrayList<TodayDish>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						TodayDish dish = new TodayDish();
						
						JSONObject jsonObj = new JSONObject(); //jsonObject.getString("name");
						dish.setServerId(jsonObject.getInt("id"));
						dish.setBodyweight(Float.parseFloat(jsonObject.getString("bodyweight")));
						dish.setName(jsonObject.getString("name"));
						dish.setDescription(jsonObject.getString("daytime"));
						dish.setCaloricity(jsonObject.getInt("calorisity"));
						dish.setAbsolutCaloricity(jsonObject.getInt("calorie"));
						dish.setCategory(jsonObject.getString("category"));
						dish.setWeight(jsonObject.getInt("dishweight"));
						dish.setDate(jsonObject.getString("datetext"));
						dish.setDateTime(jsonObject.getLong("datelong"));
						dish.setFat(Float.parseFloat(jsonObject.getString("fat")));
						dish.setCarbon(Float.parseFloat(jsonObject.getString("carbon")));
						dish.setProtein(Float.parseFloat(jsonObject.getString("protein")));
						dish.setAbsFat(Float.parseFloat(jsonObject.getString("fatabs")));        	        
						dish.setAbsCarbon(Float.parseFloat(jsonObject.getString("carbonabs")));
						dish.setAbsProtein(Float.parseFloat(jsonObject.getString("proteinabs")));
						dish.setType(jsonObject.getString("type"));
						dish.setDateTimeHH(jsonObject.getInt("hh"));
						dish.setDateTimeMM(jsonObject.getInt("mm"));
						
						newDishes.add(dish);
					}
					//list = new ArrayList<Dish>(unicDish);
				} catch (Exception e) {
					e.printStackTrace();
				}
 
        } catch (Exception e) {
            e.printStackTrace();
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