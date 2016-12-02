package bulat.diet.helper_sport.item;

public class NotificationDish {
	private String id;
	private String name;
	private String timeHH;
	private String timeMM;
	private int enabled;
	private int enabledNotification;
	
	public NotificationDish(String id, String name, String timeHH,
			String timeMM, int enabled, int enabledNotif) {
		super();
		this.id = id;
		this.name = name;
		this.timeHH = timeHH;
		this.timeMM = timeMM;
		this.enabled = enabled;
		this.enabledNotification = enabledNotif;
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
	public String getTimeHH() {
		return timeHH;
	}
	public void setTimeHH(String timeHH) {
		this.timeHH = timeHH;
	}
	public String getTimeMM() {
		return timeMM;
	}
	public void setTimeMM(String timeMM) {
		this.timeMM = timeMM;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	//is budilnik enabled
	public int getEnabledNotification() {
		return enabledNotification;
	}
	public void setEnabledNotification(int enabledNotification) {
		this.enabledNotification = enabledNotification;
	}
	
}
