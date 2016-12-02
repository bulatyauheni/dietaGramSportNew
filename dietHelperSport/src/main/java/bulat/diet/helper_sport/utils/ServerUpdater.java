package bulat.diet.helper_sport.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.db.SinchHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.httprequest.RequestHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.item.TodayDish;

public class ServerUpdater extends AsyncTask<Void, Void, Void> {

	private Context context;
	private TodayDish todayDish = null;
	private Boolean isDishUpdate = false;
	private String removeId = null;
	
	List<TodayDish> toSincList;
	
	public ServerUpdater(Context context) {
		this.context = context;
		toSincList = TodayDishHelper.getUnsincDishes(context);
		
	}

	// constructor for insert/update dish
	public ServerUpdater(Context context, TodayDish dish, Boolean isUpdate) {
		this.context = context;
		this.todayDish = dish;
		this.isDishUpdate = isUpdate;
	}

	// constructor for delete dish
	public ServerUpdater(Context context, String id) {
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
        StringBuilder builder2 = new StringBuilder();
        ArrayList<String> ids = new ArrayList<String>();
        try {
 
        	    JSONObject jsonPost = new JSONObject();
        	    //array with unsinc dishes from mobile
        	    JSONArray arrayForServer = createArray(toSincList);
        	    //the greatest dish server id in mobile base.
        	    JSONObject lastLocalDishId = new JSONObject();
        	    lastLocalDishId.put("lastid", TodayDishHelper.getLastServerId(context));
        	    for (TodayDish dish : toSincList) {        	        
        	        ids.add(dish.getId());
        	    }
        	    jsonPost.put("data", arrayForServer);       
        	    jsonPost.put("version", lastLocalDishId);
        	    
        	    //sent data to server and get response with new dishes from server and map idMobile:idServer
        	    String resultString = RequestHelper.postRequestJson(Constants.URL_DISHBASE + "serversinc.php", jsonPost.toString());
        	    
				JSONArray jsonArray = new JSONArray();
				try {
					JSONObject jsonRoot = new JSONObject(resultString);	
					//array for new dishes from server
					jsonArray = new JSONObject(jsonRoot.getString("update")).getJSONArray("dishes");			
					//array for map of ids
					JSONArray mapArray = new JSONArray();
					try{
						//get map of ids
						mapArray = new JSONObject(jsonRoot.getString("update")).getJSONArray("map");	
					}catch(Exception e){
						e.printStackTrace();
					}
										
					//json with ids of dishes. For aprowing sinchronization and making server dishes active.
					JSONObject jsonIds = new JSONObject();
					JSONArray data = new JSONArray();
					 					
	        	    //get map of ids
					for (int i = 0; i < ids.size(); i++) {
						int key = 0;
						JSONObject jsonObject = mapArray.getJSONObject(i);
						Iterator<?> iterator = jsonObject.keys();
						while (iterator.hasNext()) {
							   key = Integer.parseInt((String)iterator.next());							                  
						} 
						int val = jsonObject.getInt(""+ key);
						
						//set server id for sinchronozed dishes
						TodayDishHelper.sincDish(key, val, context);
						
						//add id into json
	        	        JSONObject jsonObj = new JSONObject();
	        	        jsonObj.put("id", val);
	        	        data.put(jsonObj);
					}
	        
					jsonIds.put("data", data);
					// update active flag for saved records
					String resultString2 = RequestHelper.postRequestJson(Constants.URL_DISHBASE + "serveractiv.php",jsonIds.toString());
					if(!"1".equals(resultString2)){
						SinchHelper.addNewSinch(Constants.URL_DISHBASE + "serveractiv.php", jsonIds.toString(), "post", context);
					}

					
					// add dishes from server side
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
					for (TodayDish todayDish : newDishes) {
						TodayDishHelper.addNewDish(todayDish, context);
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

	
	private JSONArray createArray(List<TodayDish> toSincList2) throws JSONException, UnsupportedEncodingException {
		JSONArray array = new JSONArray();	    	   
	    for (TodayDish dish : toSincList) {
	        JSONObject jsonObj = new JSONObject();	       
	        jsonObj.put("id", dish.getId());
	        jsonObj.put("bodyweight", dish.getBodyweight());
	        jsonObj.put("name", URLEncoder.encode(dish.getName(), "UTF-8"));
	        jsonObj.put("daytime", dish.getDescription());
	        jsonObj.put("calorisity", dish.getCaloricity());
	        jsonObj.put("calorie", dish.getAbsolutCaloricity());
	        jsonObj.put("category", dish.getCategory());
	        jsonObj.put("dishweight", dish.getWeight());
	        jsonObj.put("datetext", URLEncoder.encode(dish.getDate(), "UTF-8"));
	        jsonObj.put("datelong", dish.getDateTime());
	        jsonObj.put("fat", dish.getFat());
	        jsonObj.put("carbon", dish.getCarbon());
	        jsonObj.put("protein", dish.getProtein());
	        jsonObj.put("fatabs", dish.getAbsFat());        	        
	        jsonObj.put("carbonabs", dish.getAbsCarbon());
	        jsonObj.put("proteinabs", dish.getAbsProtein());
	        jsonObj.put("type", dish.getType());
	        jsonObj.put("hh", dish.getDateTimeHH());
	        jsonObj.put("mm", dish.getDateTimeMM());
	        jsonObj.put("issport", dish.getCaloricity()>=0?0:1);
	        //jsonObj.put("chest", dish.get);       	        
	        //jsonObj.put("pelvis", dish.get);
	       // jsonObj.put("biceps", dish.get);
	       // jsonObj.put("shin", dish.get);
	       // jsonObj.put("neck", dish.get);
	       // jsonObj.put("waist", dish.get);
	       // jsonObj.put("forearm", dish.get);
	       // jsonObj.put("hip", dish.get);
	       array.put(jsonObj);
	    } 
		return array;
	}

	protected void onPostExecute(String unused) {
	}




}