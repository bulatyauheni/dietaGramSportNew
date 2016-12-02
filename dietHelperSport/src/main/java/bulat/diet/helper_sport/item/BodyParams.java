package bulat.diet.helper_sport.item;

public class BodyParams {
	private String chest;
	private String biceps;
	private String pelvis;
	private String neck;
	private String waist;
	private String forearm;
	private String hip;
	private String shin;
				
	public BodyParams(String chest, String biceps, String pelvis, String neck,
			String waist, String forearm, String hip, String shin) {
		super();
		this.chest = chest;
		this.biceps = biceps;
		this.pelvis = pelvis;
		this.neck = neck;
		this.waist = waist;
		this.forearm = forearm;
		this.hip = hip;
		this.shin = shin;
	}
	
	public String getChest() {
		return chest;
	}
	public void setChest(String chest) {
		this.chest = chest;
	}
	public String getBiceps() {
		return biceps;
	}
	public void setBiceps(String biceps) {
		this.biceps = biceps;
	}
	public String getPelvis() {
		return pelvis;
	}
	public void setPelvis(String pelvis) {
		this.pelvis = pelvis;
	}
	public String getNeck() {
		return neck;
	}
	public void setNeck(String neck) {
		this.neck = neck;
	}
	public String getWaist() {
		return waist;
	}
	public void setWaist(String waist) {
		this.waist = waist;
	}
	public String getForearm() {
		return forearm;
	}
	public void setForearm(String forearm) {
		this.forearm = forearm;
	}
	public String getHip() {
		return hip;
	}
	public void setHip(String hip) {
		this.hip = hip;
	}
	public String getShin() {
		return shin;
	}
	public void setShin(String shin) {
		this.shin = shin;
	}

}
