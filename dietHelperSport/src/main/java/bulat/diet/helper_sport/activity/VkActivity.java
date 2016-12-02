package bulat.diet.helper_sport.activity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Account;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.SaveUtils;

import com.perm.kate.api.Api;
import com.perm.kate.api.Photo;

public class VkActivity extends Activity{
	
	private String vkUrl = "https://oauth.vk.com/authorize?client_id=3583596&scope=wall,offline&redirect_uri=oauth.vk.com/blank.html&display=touch&response_type=token";
    private String postUrl = "https://oauth.vk.com/method/wall.post?message=%s&attachment=%s&access_token=%s";
	private final String ACCESS_TOKEN = "blank.html#access_token";
	private final String SEND = "{\"response\":{\"processing\":1}}";
	public static final String MESSAGE = "MESSAGE";
	public static final String LINK = "LINK";
	public static final String IMAGE_DESK = "IMAGE_DESK";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	 private final int REQUEST_LOGIN=1;
	    
	    Button authorizeButton;
	    Button logoutButton;
	    Button postButton;
	    EditText messageEditText;
	    
	    Account account=new Account();
	    Api api;
		private String pathImage;
		private String imageDesckription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startLoginActivity();
		pathImage = getIntent().getStringExtra(IMAGE_PATH);
		imageDesckription = getIntent().getStringExtra(IMAGE_DESK);
		
	}
	  private void startLoginActivity() {
	        Intent intent = new Intent();
	        intent.setClass(this, LoginActivity.class);
	        startActivityForResult(intent, REQUEST_LOGIN);
	    }
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == REQUEST_LOGIN) {
	            if (resultCode == RESULT_OK) {
	                //авторизовались успешно 
	                account.access_token=data.getStringExtra("token");
	                account.user_id=data.getLongExtra("user_id", 0);
	                account.save(VkActivity.this);
	                api=new Api(account.access_token, Constants.API_ID);
	                postToWall();
	                joinToGroup();
	               
	            }
	        }
	    } 

/*	    private void postToWall() {
	        //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
	        new Thread(){
	            @Override
	            public void run(){
	                try {
	                    String text=getString(R.string.vk_recomend);
	                    ArrayList<String> attach = new ArrayList<String>();
	                    attach.add("https://play.google.com/store/apps/details?id=bulat.diet.helper_ru");
	                    api.createWallPost(account.user_id, text, attach, null, false, false, false, null, null, null, null);
	                    //Показать сообщение в UI потоке 
	                    runOnUiThread(successRunnable);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }.start();
	    }*/
	    private void postToWall() {
	        new Thread() {
	          @Override
	          public void run() {
	            try {
	              String loadServer = api.photosGetWallUploadServer(
	                  account.user_id, null);
	              File file = new File(pathImage);
	              Bundle params = new Bundle();
	              try {
	                byte[] data = getBytesFromFile(file);
	                params.putByteArray("photo", data);
	              } catch (IOException e) {
	                e.printStackTrace();
	              }

	              JSONObject jsonObj = new JSONObject(openUrl(loadServer,
	                  "POST", params, SaveUtils.getUserAdvId(VkActivity.this)));
	              String photo = jsonObj.getString("photo");
	              String server = jsonObj.getString("server");
	              String hash = jsonObj.getString("hash");
	              api.saveWallPhoto(server, photo, hash, account.user_id,
	                  null);

	              ArrayList<Photo> photoS;
	              photoS = api.saveWallPhoto(server, photo, hash, null, null);
	              String userID = null;
	              String ownID = null;
	              for (int i = 0; i < photoS.size(); i++) {
	                userID = String.valueOf(photoS.get(i).pid);
	                ownID = photoS.get(i).owner_id;
	              }
	              final String pid = userID;
	              final String owner_id = ownID;
	              Collection<String> attachments = new ArrayList<String>() {
	                {
	                  add("photo" + owner_id + "_" + pid);
	                }
	              };
	              attachments.add("photo_" + userID);
	              attachments.add("https://play.google.com/store/apps/details?id=bulat.diet.helper_sport&referrer=utm_source%3DDG_VK_WALL_WC");
	              api.createWallPost(account.user_id,
	                  getString(R.string.app_name) + " - " + imageDesckription , attachments, null,
	                  false, false, false, null, null, null, null);
	              runOnUiThread(successRunnable);
	            } catch (Exception e) {
	              e.printStackTrace();
	            }
	          }
	        }.start();
	      }
	    
	    public byte[] getBytesFromFile(File file) throws IOException {
	        InputStream is = new FileInputStream(file);
	        long length = file.length();
	        if (length > Integer.MAX_VALUE) {
	          return null;
	        }
	        byte[] bytes = new byte[(int) length];
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length
	            && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
	          offset += numRead;
	        }
	        if (offset < bytes.length) {
	          throw new IOException("Could not completely read file "
	              + file.getName());
	        }
	        is.close();
	        return bytes;
	      }
	    public static String openUrl(String url, String method, Bundle params, int userId)
	    	      throws MalformedURLException, IOException, JSONException {
	    	    String boundary = "Asrf456BGe4h";
	    	    String endLine = "\r\n";
	    	    String twoHyphens = "--";
	    	    OutputStream os;
	    	    HttpURLConnection connection = (HttpURLConnection) new URL(url)
	    	        .openConnection();
	    	    if (!method.equals("GET")) {
	    	      Bundle dataparams = new Bundle();
	    	      for (String key : params.keySet()) {
	    	        Object parameter = params.get(key);
	    	        if (parameter instanceof byte[]) {
	    	          dataparams.putByteArray(key, (byte[]) parameter);
	    	        }
	    	      }
	    	      connection.setRequestMethod("POST");
	    	      connection.setRequestProperty("Content-Type",
	    	          "multipart/form-data; boundary=" + boundary);
	    	      connection.setRequestProperty("Connection", "keep-alive");
	    	      connection.setDoOutput(true);
	    	      connection.setDoInput(true);
	    	      connection.connect();
	    	      os = new BufferedOutputStream(connection.getOutputStream());
	    	      os.write((twoHyphens + boundary + endLine).getBytes());
	    	      if (!dataparams.isEmpty()) {
	    	        for (String key : dataparams.keySet()) {
	    	          os.write(("Content-Disposition: form-data; name=\"photo\"; filename=\"photo"+ userId +".jpg\"")
	    	              .getBytes());
	    	          os.write(("Content-Type: image/jpeg" + endLine + endLine)
	    	              .getBytes());
	    	          os.write(dataparams.getByteArray(key));
	    	          os.write((endLine + twoHyphens + boundary + twoHyphens + endLine)
	    	              .getBytes());
	    	        }
	    	      }
	    	      os.flush();
	    	      os.close();
	    	    }
	    	    String response = read(connection.getInputStream());
	    	    return response;
	    	  }
	    
	    private static String read(InputStream in) throws IOException {
	        StringBuilder sb = new StringBuilder();
	        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
	        for (String line = r.readLine(); line != null; line = r.readLine()) {
	          sb.append(line);
	        }
	        in.close();
	        return sb.toString();
	      }
	  private void joinToGroup() {
	        //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
	        new Thread(){
	            @Override
	            public void run(){
	                try {
	                    String text=getString(R.string.vk_recomend);
	                    ArrayList<String> attach = new ArrayList<String>();
	                    attach.add("https://play.google.com/store/apps/details?id=bulat.diet.helper_ru");
	                    api.joinGroup(47759767, null, null);
	                   // api.createWallPost(account.user_id, text, attach, null, false, false, false, null, null, null, null);
	                    //Показать сообщение в UI потоке                
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }.start();
	    }
	    
	    Runnable successRunnable=new Runnable(){
	        public void run() {
	            Toast.makeText(getApplicationContext(), getString(R.string.vk_toast), Toast.LENGTH_LONG).show();
	            Date currDate = new Date();
	            if(currDate.getTime()>SaveUtils.getEndPDate(getApplicationContext())){
	            	SaveUtils.setEndPDate(currDate.getTime() + 30 * DateUtils.DAY_IN_MILLIS, getApplicationContext());
	            	SaveUtils.setUseFreeAbonement(true, getApplicationContext());
	    		}
	            onBackPressed();
	        }
	    };
	    	
	
}
