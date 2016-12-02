package bulat.diet.helper_sport.item;

public class News extends User {
	private String newsId;
	private String news;
	private Boolean isComent;
	private String newsDate;
	
	
	
	public News(String name, String userid, float weight, int height,
			int activity, int sex, int age, String newsId, String news,	String newsDate, String iscoment) {
		super(name, userid, weight, height, activity, sex, age);
		this.newsId = newsId;
		this.news = news;
		this.newsDate = newsDate;
		if(iscoment.equals("1")){
			this.isComent=true;
		}else {
			this.isComent=false;
		}
	}
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getNews() {
		return news;
	}
	public void setNews(String news) {
		this.news = news;
	}
	public String getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}
	public Boolean getIsComent() {
		return isComent;
	}
	public void setIsComent(Boolean isComent) {
		this.isComent = isComent;
	}
	
	

}
