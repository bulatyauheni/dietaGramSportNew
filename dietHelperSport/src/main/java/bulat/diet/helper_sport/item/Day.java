package bulat.diet.helper_sport.item;

public class Day {
	private String name;
	private String dateStr;
	private long dateInt;
	private float bodyWeight;
	private float chest;
	private float biceps;
	private float pelvis;
	private float neck;
	private float waist;
	private float forearm;
	private float hip;
	private float shin;
	private int limit;
	
	public Day(String name, long dateInt, float bodyWeight, float chest,
			float biceps, float pelvis, float neck, float waist, float forearm,
			float hip, float shin, int limit, int caloricity, int weight) {
		super();
		this.name = name;
		this.dateInt = dateInt;
		this.bodyWeight = bodyWeight;
		this.chest = chest;
		this.biceps = biceps;
		this.pelvis = pelvis;
		this.neck = neck;
		this.waist = waist;
		this.forearm = forearm;
		this.hip = hip;
		this.shin = shin;
		this.limit = limit;
		this.caloricity = caloricity;
		this.weight = weight;
		this.setLimit(limit);
	}
	public Day(String name, long dateInt, int caloricity, int weight, float bodyweight, int limit) {
		super();
		this.name = name;
		this.dateInt = dateInt;
		this.caloricity = caloricity;
		this.weight = weight;
		this.bodyWeight = bodyweight;
		this.setLimit(limit);
	}
	public Day() {
		super();		
	}
	
	
	public float getChest() {
		return chest;
	}
	public void setChest(float chest) {
		this.chest = chest;
	}
	public float getBiceps() {
		return biceps;
	}
	public void setBiceps(float biceps) {
		this.biceps = biceps;
	}
	public float getPelvis() {
		return pelvis;
	}
	public void setPelvis(float pelvis) {
		this.pelvis = pelvis;
	}
	public float getNeck() {
		return neck;
	}
	public void setNeck(float neck) {
		this.neck = neck;
	}
	public float getWaist() {
		return waist;
	}
	public void setWaist(float waist) {
		this.waist = waist;
	}
	public float getForearm() {
		return forearm;
	}
	public void setForearm(float forearm) {
		this.forearm = forearm;
	}
	public float getHip() {
		return hip;
	}
	public void setHip(float hip) {
		this.hip = hip;
	}
	public float getShin() {
		return shin;
	}
	public void setShin(float shin) {
		this.shin = shin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDateInt() {
		return dateInt;
	}
	public void setDateInt(long description) {
		this.dateInt = description;
	}
	public int getCaloricity() {
		return caloricity;
	}
	public void setCaloricity(int caloricity) {
		this.caloricity = caloricity;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public float getBodyWeight() {
		return bodyWeight;
	}
	public void setBodyWeight(float bodyWeight) {
		this.bodyWeight = bodyWeight;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	private int caloricity;
	private int weight;

	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
}
