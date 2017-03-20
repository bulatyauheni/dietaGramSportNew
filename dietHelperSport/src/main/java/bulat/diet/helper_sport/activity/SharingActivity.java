package bulat.diet.helper_sport.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.api.client.http.HttpResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.StringUtils;

public class SharingActivity extends StatisticFCPActivity {

    private CallbackManager callbackManager;
    private LoginManager manager;
    private static final String FCP_FB_SHARE = "FCP_FB_SHARE";
    private static final String FCP_FB_SHARE_ERROR = "FCP_FB_SHARE_ERROR";
    private static final String FCP_FB_SHARE_CLICK = "FCP_FB_SHARE_CLICK";


    byte[] ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_empty);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "bulat.diet.helper_sport",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        /*List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        manager = LoginManager.getInstance();

        manager.logInWithPublishPermissions(this, permissionNeeds);

        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sharePhotoToFacebook();
                Profile profile = Profile.getCurrentProfile();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                sharePhotoToFacebook();
                Profile profile = Profile.getCurrentProfile();
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                GATraker.sendEvent("FacebookActivity", FCP_FB_SHARE_ERROR, exception.getMessage(), 1L);
            }
        });*/
        sharePhotoToFacebook();
    }

    private void sharePhotoToFacebook() {
        String pathImage = getIntent().getStringExtra(VkActivity.IMAGE_PATH);
        final String imageDesckription = getIntent().getStringExtra(VkActivity.IMAGE_DESK);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap image = BitmapFactory.decodeFile(pathImage, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        ba = bao.toByteArray();
        image.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        ba = bao.toByteArray();
        new uploadToServer().execute();
        ShareDialog shareDialog = new ShareDialog(SharingActivity.this);
        if (!shareDialog.canShow(SharePhotoContent.class)) {
            CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(SharingActivity.this);
            bld.setLayout(R.layout.section_alert_dialog_two_buttons)
                    .setMessage(SharingActivity.this.getString(R.string.info_install_fb))
                    .setPositiveButton(R.id.dialogButtonOk, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            final String appPackageName = "com.facebook.katana"; // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    })
                    .setPositiveButtonText(R.string.agree)
                    .setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    })
                    .setNegativeButtonText(R.string.disagree);
            bld.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
        // onBackPressed();
    }

    Runnable successRunnable = new Runnable() {
        public void run() {
            Toast.makeText(getApplicationContext(), getString(R.string.fb_toast), Toast.LENGTH_LONG).show();
            Date currDate = new Date();
            if (currDate.getTime() > SaveUtils.getEndPDate(getApplicationContext())) {
                SaveUtils.setEndPDate(currDate.getTime() + 7 * DateUtils.DAY_IN_MILLIS, getApplicationContext());
                SaveUtils.setUseFreeAbonement(true, getApplicationContext());
            }
            onBackPressed();
        }
    };
    String ba1;

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(SharingActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String filename = "" + System.currentTimeMillis() + StringUtils.getEmail(SharingActivity.this) + ".jpg";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("base64", com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64String(ba)));
            nameValuePairs.add(new BasicNameValuePair("ImageName", filename));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.api.dietagram.ru/upload.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);
                ShareDialog shareDialog = new ShareDialog(SharingActivity.this);
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(SharingActivity.this, "Share Success", Toast.LENGTH_SHORT).show();
                        GATraker.sendEvent("FacebookActivity", FCP_FB_SHARE, FCP_FB_SHARE_CLICK, 1L);
                        runOnUiThread(successRunnable);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SharingActivity.this, "Share Cancelled", Toast.LENGTH_SHORT).show();
                        GATraker.sendEvent("FacebookActivity", FCP_FB_SHARE, FCP_FB_SHARE_CLICK, 1L);
                        runOnUiThread(successRunnable);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SharingActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("SharingActivity", "Share: " + exception.getMessage());
                        GATraker.sendEvent("FacebookActivity", FCP_FB_SHARE, FCP_FB_SHARE_CLICK, 1L);
                        runOnUiThread(successRunnable);
                        exception.printStackTrace();
                    }
                });
                if (shareDialog.canShow(SharePhotoContent.class)) {

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=bulat.diet.helper_sport&referrer=utm_source%3DDG_FB_WALL_WC"))
                            .setImageUrl(Uri.parse("http://www.api.dietagram.ru/upload/" + filename))
                            .build();
                    shareDialog.show(SharingActivity.this, content);
                }
            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
}
