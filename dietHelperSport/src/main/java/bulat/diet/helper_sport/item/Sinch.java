package bulat.diet.helper_sport.item;

public class Sinch {

	private String id;
	private String url;
	private String params;
	private String type;
				
	public Sinch(String url, String params, String type) {
		super();
		this.url = url;
		this.params = params;
		this.type = type;
	}
	
	public Sinch() {
		super();		
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	

}
