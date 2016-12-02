package bulat.diet.helper_sport.httprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import bulat.diet.helper_sport.utils.Constants;

public class RequestHelper {
	public static String postRequestJson(String url, String json ) throws ClientProtocolException, IOException {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(url);
	    StringEntity se = new StringEntity(json.toString());  //new ByteArrayEntity(json.toString().getBytes(            "UTF8"))
	    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	    post.setHeader("Accept", "application/json");
	    post.setHeader("Content-type", "application/json");
	    post.setEntity(se);
	    HttpResponse response = client.execute(post);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} else {
			return "httpError: "+ statusCode;
		}
		return builder.toString().trim();
	}
}
