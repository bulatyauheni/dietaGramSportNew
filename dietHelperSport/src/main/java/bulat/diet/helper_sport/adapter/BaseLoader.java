package bulat.diet.helper_sport.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.DishListHelper;
import bulat.diet.helper_sport.db.SportListHelper;
import bulat.diet.helper_sport.item.Dish;
import bulat.diet.helper_sport.utils.SaveUtils;

import com.google.gson.stream.JsonReader;

public class BaseLoader {

	public static void LoadBase(Context c) {
		try {
			InputStream is = c.getResources().openRawResource(R.raw.full_base);
			JsonReader reader = new JsonReader(new InputStreamReader(is,
					"UTF-8"));
			reader.beginArray();
			Dish dish = null;
			int count = 0;
			int lastRow = SaveUtils.getNumOfRows(c);
			if (lastRow == 0) {
				DishListHelper.clearAll(c);
			}
			String msg = c.getString(R.string.loading_social);
			while (reader.hasNext()) {
				reader.beginObject();
				count++;

				dish = new Dish();
				while (reader.hasNext()) {

					String name = reader.nextName();
					if (name.equals("name")) {
						dish.setName(reader.nextString());
					} else if (name.equals("caloric")) {
						dish.setCaloricity(reader.nextInt());
					} else if (name.equals("category_id")) {
						dish.setCategory(reader.nextInt());
					} else if (name.equals("type")) {
						dish.setType(reader.nextString());
					} else if (name.equals("fat")) {
						dish.setFatStr(String.valueOf(reader.nextDouble()));
					} else if (name.equals("carbon")) {
						dish.setCarbonStr(reader.nextString());
					} else if (name.equals("protein")) {
						dish.setProteinStr(reader.nextString());
					} else if (name.equals("category")) {
						dish.setCategoryName(reader.nextString());
					} else {
						reader.skipValue();
					}

				}
				if (count > lastRow) {
					DishListHelper.addNewDishFullParams(dish, c);
				}

				reader.endObject();

				if (count % 5 == 0) {
					SaveUtils.setNumOfRows(count, c);
				}
			}
			reader.endArray();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void LoadFitnesBase(Context c) {

		InputStream is2 = c.getResources().openRawResource(R.raw.fitnes);
		BufferedReader br = new BufferedReader(new InputStreamReader(is2));
		String readLine = null;

		List<String[]> base = new ArrayList<String[]>();

		try {
			SportListHelper.clearAll(c);
			String[] mass; // While the BufferedReader readLine is not null
			while ((readLine = br.readLine()) != null) {
				mass = readLine.split("\\t");
				base.add(mass);
			}
			for (String[] dishStr : base) {
				if(dishStr.length==3){
					SportListHelper.addNewSport(
						new Dish(dishStr[0].trim(), dishStr[0].trim().toLowerCase(), 0, Integer.parseInt(dishStr[2].trim()), 0,
								0, "","0", "0", String.valueOf(Float.valueOf(dishStr[1].trim())/60)), c);
				}else if(dishStr.length==4){
					SportListHelper.addNewSport(
							new Dish(dishStr[0].trim(), dishStr[0].trim().toLowerCase(), 0, Integer.parseInt(dishStr[3].trim()), 0,
									0, "", String.valueOf(Float.valueOf(dishStr[1].trim())), String.valueOf(Float.valueOf(dishStr[2].trim())), "0"), c);
				}else{
					SportListHelper.addType(dishStr[0].trim(), dishStr[1].trim(), c);										}
			}
			// Close the InputStream and BufferedReader is.close(); br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void LoadText(int resourceId, Context c) { // The
	 * InputStream opens the resourceId and sends it to the buffer InputStream
	 * is = c.getResources().openRawResource(resourceId); BufferedReader br =
	 * new BufferedReader(new InputStreamReader(is)); String readLine = null;
	 * Map<Integer, String> types = LoadCategoryTypeMap(R.raw.category, c);
	 * Map<Integer, String> categories = LoadCategoryMap(R.raw.category, c);
	 * List<String[]> base = new ArrayList<String[]>(); try { String[] mass; //
	 * While the BufferedReader readLine is not null while ((readLine =
	 * br.readLine()) != null) { mass = readLine.split("\\t"); base.add(mass); }
	 * String category = ""; String type = ""; for (String[] dishStr : base) {
	 * if (dishStr.length == 1){ category = dishStr[0]; if(!"".equals(category)
	 * && category != null){ type = types.get(Integer.valueOf(category.trim()));
	 * } if(type == null){ type = ""; } }else{ if(category != null){
	 * DishListHelper.addNewDish(new Dish( dishStr[0].trim(), dishStr[0].trim(),
	 * Integer.parseInt(dishStr[1].trim()), Integer.parseInt(category.trim()),
	 * 0, 0, type), c); } } } for (Integer key : categories.keySet()) {
	 * DishListHelper.addNewDish(new Dish( categories.get(key),
	 * categories.get(key), key, key, 1, 0, ""), c); } // Close the InputStream
	 * and BufferedReader is.close(); br.close();
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } }
	 */
	public static String getStringFromFile(int resourceId, Context c) {
		// The InputStream opens the resourceId and sends it to the buffer
		InputStream is = c.getResources().openRawResource(resourceId);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String readLine = null;
		StringBuffer base = new StringBuffer();
		try {
			// While the BufferedReader readLine is not null
			while ((readLine = br.readLine()) != null) {
				base.append(readLine);

			}

			// Close the InputStream and BufferedReader
			is.close();
			br.close();
			return base.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Map<String, Float> LoadFitnesMap(int resourceId, Context c) {
		InputStream is = c.getResources().openRawResource(resourceId);
		Map<String, Float> categories = new TreeMap<String, Float>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String readLine = null;

			while ((readLine = br.readLine()) != null) {
				String[] mass = readLine.split("\\t");

				categories
						.put(mass[0].trim(), Float.parseFloat(mass[1].trim().replace(",", ".")));
			}
			// Close the InputStream and BufferedReader
			is.close();
			br.close();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return categories;
	}

	/*
	 * public static Map<Integer, String> LoadCategoryMap(int resourceId,
	 * Context c) { InputStream is =
	 * c.getResources().openRawResource(resourceId); Map<Integer, String>
	 * categories = new TreeMap<Integer, String>(); try { BufferedReader br =
	 * new BufferedReader(new InputStreamReader(is));
	 * 
	 * String readLine = null;
	 * 
	 * while ((readLine = br.readLine()) != null) { String[] mass =
	 * readLine.split("\\t");
	 * 
	 * categories.put(Integer.parseInt(mass[1].trim()), mass[0].trim()); } //
	 * Close the InputStream and BufferedReader is.close(); br.close(); } catch
	 * (UnsupportedEncodingException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * return categories; } public static Map<Integer, String>
	 * LoadCategoryTypeMap(int resourceId, Context c) { InputStream is =
	 * c.getResources().openRawResource(resourceId); Map<Integer, String>
	 * categories = new TreeMap<Integer, String>(); try { BufferedReader br =
	 * new BufferedReader(new InputStreamReader(is));
	 * 
	 * String readLine = null;
	 * 
	 * while ((readLine = br.readLine()) != null) { String[] mass =
	 * readLine.split("\\t"); if(mass.length == 3){
	 * categories.put(Integer.parseInt(mass[1].trim()), mass[2].trim()); } } //
	 * Close the InputStream and BufferedReader is.close(); br.close(); } catch
	 * (UnsupportedEncodingException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * return categories; }
	 * 
	 * public static String getType(int category, Context ctx) { Map<Integer,
	 * String> types = LoadCategoryTypeMap(R.raw.category, ctx); return
	 * types.get(category); }
	 */

}
