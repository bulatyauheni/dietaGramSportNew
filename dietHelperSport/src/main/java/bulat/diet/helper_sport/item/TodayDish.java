package bulat.diet.helper_sport.item;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TodayDish {
	
	private String id;
	private float bodyweight;
	private String name="";
	private String description;
	private int caloricity;
	private String category;
	private int weight;
	private int absolutCaloricity;
	private String date="";
	private long dateTime;
	private int isdate;
	private String type;
	private float fat;
	private float carbon;
	private float protein;
	private float absFat;
	private float absCarbon;
	private float absProtein;
	private int dateTimeHH;
	private int dateTimeMM;
	private int serverId;
	private String dayTyme; // breakfast or lunch:)
	
	public TodayDish(String id2, String name, String description, int caloricity,
			String category, int weight, int absolutCaloricity, String date,  float fat, float absFat, float carbon, float absCarbon, float protein, float absProtein, int timeHH, int timeMM) {
		super();
		this.id = id2;
		this.name = name;
		this.description = description;
		this.caloricity = caloricity;
		this.category = category;
		this.weight = weight;
		this.absolutCaloricity = absolutCaloricity;
		this.date = date;
		this.fat = fat;
		this.carbon = carbon;
		this.protein = protein;
		this.absFat = absFat;
		this.absCarbon = absCarbon;
		this.absProtein = absProtein;
		this.dateTimeMM = timeMM;
		this.dateTimeHH = timeHH;
	}
	
	public TodayDish(float bodyweight, String name, String description, int caloricity,
			String category, int weight, int absolutCaloricity, String date, long dateTime, 
			int isdate, String type, float fat, float absFat, float carbon, float absCarbon, float protein, float absProtein, int timeHH, int timeMM) {
		super();
		this.setBodyweight(bodyweight);
		this.name = name;
		this.description = description;
		this.caloricity = caloricity;
		this.category = category;
		this.weight = weight;
		this.absolutCaloricity = absolutCaloricity;
		this.date = date;
		this.isdate = isdate;
		this.dateTime = dateTime;
		this.type = type;
		this.fat = fat;
		this.carbon = carbon;
		this.protein = protein;
		this.absFat = absFat;
		this.absCarbon = absCarbon;
		this.absProtein = absProtein;
		this.dateTimeMM = timeMM;
		this.dateTimeHH = timeHH;
	}
	
	public TodayDish(String name, String description, int caloricity,
			String category, int weight, int absolutCaloricity, String date, long dateTime,  int isdate, String type) {
		super();
		this.name = name;
		this.description = description;
		this.caloricity = caloricity;
		this.category = category;
		this.weight = weight;
		this.absolutCaloricity = absolutCaloricity;
		this.date = date;
		this.isdate = isdate;
		this.dateTime = dateTime;
		this.type = type;
	}
	
	public TodayDish(String name, String description, int caloricity,
			String category, int weight, int absolutCaloricity, String date, long dateTime,  int isdate, String type, float bodyweight) {
		super();
		this.name = name;
		this.description = description;
		this.caloricity = caloricity;
		this.category = category;
		this.weight = weight;
		this.absolutCaloricity = absolutCaloricity;
		this.date = date;
		this.isdate = isdate;
		this.dateTime = dateTime;
		this.type = type;
	}
	
	public int getDateTimeHH() {
		return dateTimeHH;
	}

	public void setDateTimeHH(int dateTimeHH) {
		this.dateTimeHH = dateTimeHH;
	}

	public int getDateTimeMM() {
		return dateTimeMM;
	}

	public void setDateTimeMM(int dateTimeMM) {
		this.dateTimeMM = dateTimeMM;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsdate() {
		return isdate;
	}
	public void setIsdate(int isdate) {
		this.isdate = isdate;
	}
	
	public TodayDish() {
		// TODO Auto-generated constructor stub
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCaloricity() {
		return caloricity;
	}
	public void setCaloricity(int caloricity) {
		this.caloricity = caloricity;
	}
	public String getCategory() {
		if(category==null || category.length()==0)category = "1";
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getAbsolutCaloricity() {
		return absolutCaloricity;
	}
	public void setAbsolutCaloricity(int absolutCaloricity) {
		this.absolutCaloricity = absolutCaloricity;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public float getBodyweight() {
		return bodyweight;
	}

	public void setBodyweight(float bodyweight) {
		this.bodyweight = bodyweight;
	}

	@Override
	public String toString() {
		String id_l = "";
		if(id != null){
			id_l = id;
		}
		if(date==null){
			date = "";
		}
		try {
			return "id=" + id_l +"&body_weight="+bodyweight+"&dish_name="+URLEncoder.encode(name.toString(),"UTF-8")+"&day_time="+description+
			"&caloricity="+caloricity+"&weight="+weight+
			"&date="+URLEncoder.encode(date.toString(),"UTF-8")+"&date_int="+dateTime+"&fat="+fat+"&carbon="+carbon+"&protein="+protein ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public float getFat() {
		return fat;
	}

	public void setFat(float fat) {
		this.fat = fat;
	}

	public float getCarbon() {
		return carbon;
	}

	public void setCarbon(float carbon) {
		this.carbon = carbon;
	}

	public float getProtein() {
		return protein;
	}

	public void setProtein(float protein) {
		this.protein = protein;
	}

	public float getAbsFat() {
		return absFat;
	}

	public void setAbsFat(float absFat) {
		this.absFat = absFat;
	}

	public float getAbsCarbon() {
		return absCarbon;
	}

	public void setAbsCarbon(float absCarbon) {
		this.absCarbon = absCarbon;
	}

	public float getAbsProtein() {
		return absProtein;
	}

	public void setAbsProtein(float absProtein) {
		this.absProtein = absProtein;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getDayTyme() {
		return dayTyme;
	}

	public void setDayTyme(String dayTyme) {
		this.dayTyme = dayTyme;
	}
	
	

}
