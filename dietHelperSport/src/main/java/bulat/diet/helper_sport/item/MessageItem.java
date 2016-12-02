package bulat.diet.helper_sport.item;

public class MessageItem {
	private int id;
	private String userId;
	private String userIdFrom;
	private String date;
	private String userName;
	private String dateInt;
	private String message;
	private String data;
	
	
	
	public MessageItem(int id, String userId, String userIdFrom, String date, String userName,
			String dateInt, String message, String data) {
		super();
		this.id = id;
		this.userId = userId;
		this.userIdFrom = userIdFrom;
		this.date = date;
		this.userName = userName;
		this.dateInt = dateInt;
		this.message = message;
		this.data = data;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDateInt() {
		return dateInt;
	}
	public void setDateInt(String dateInt) {
		this.dateInt = dateInt;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserIdFrom() {
		return userIdFrom;
	}
	public void setUserIdFrom(String userIdFrom) {
		this.userIdFrom = userIdFrom;
	}
	public String getData() {
		// TODO Auto-generated method stub
		return data;
	}
	public void setData(String data) {
		// TODO Auto-generated method stub
		this.data = data;
	}
}
