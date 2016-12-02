package bulat.diet.helper_sport.item;

public class User {
	private String id;
	private String userName;
	private int age;
	private String description;
	private int caloricity;
	private int weightOfFood;
	private float weightOfBody;
	private int sex;
	private int height;
	private int activity;
	
	public User( String name, String userid, float weight, int height, int activity,int sex, int age) {
		// TODO Auto-generated constructor stub
		this.id = userid;
		this.userName = name;
		this.weightOfBody = weight;
		this.sex = sex;
		this.setHeight(height);
		this.setActivity(activity);
		this.setAge(age);
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public int getWeightOfFood() {
		return weightOfFood;
	}
	public void setWeightOfFood(int weightOfFood) {
		this.weightOfFood = weightOfFood;
	}
	public float getWeightOfBody() {
		return weightOfBody;
	}
	public void setWeightOfBody(float weightOfBody) {
		this.weightOfBody = weightOfBody;
	}
	public int getIsFoloved() {
		return isFoloved;
	}
	public void setIsFoloved(int isFoloved) {
		this.isFoloved = isFoloved;
	}
	private int isFoloved;
	public User() {

	}


	public int getSex() {
		// TODO Auto-generated method stub
		return sex;
	}


	public void setSex(int sex) {
		this.sex = sex;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public int getActivity() {
		return activity;
	}


	public void setActivity(int activity) {
		this.activity = activity;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


}
