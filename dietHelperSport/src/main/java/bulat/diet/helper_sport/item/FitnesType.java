package bulat.diet.helper_sport.item;

public class FitnesType {
	private float typeKey;
	private String description;
	public float getTypeKey() {
		return typeKey;
	}
	public void setTypeKey(int typeKey) {
		this.typeKey = typeKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getDescription();
	}
	public FitnesType(float typeKey, String description) {
		super();
		this.typeKey = typeKey;
		this.description = description;
	}
	

}
