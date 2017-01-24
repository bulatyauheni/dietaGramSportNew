package bulat.diet.helper_sport.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.github.mikephil.charting.charts.PieChart;
import com.perm.kate.api.Api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;

public class SharingActivity extends StatisticFCPActivity {

    private CallbackManager callbackManager;
    private LoginManager manager;
    private static final String FCP_FB_SHARE = "FCP_FB_SHARE";
    private static final String FCP_FB_SHARE_CLICK = "FCP_FB_SHARE_CLICK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fb_empty);

            FacebookSdk.sdkInitialize(getApplicationContext());
            GATraker.sendEvent("FacebookActivity", FCP_FB_SHARE, FCP_FB_SHARE_CLICK, 1L);
            callbackManager = CallbackManager.Factory.create();

            List<String> permissionNeeds = Arrays.asList("publish_actions");

            //this loginManager helps you eliminate adding a LoginButton to your UI
            manager = LoginManager.getInstance();

            manager.logInWithPublishPermissions(this, permissionNeeds);

            manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    sharePhotoToFacebook();
                }

                @Override
                public void onCancel() {
                    System.out.println("onCancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    System.out.println("onError");
                }
            });
    }

    private void sharePhotoToFacebook() {

        String pathImage = getIntent().getStringExtra(VkActivity.IMAGE_PATH);
        String imageDesckription = getIntent().getStringExtra(VkActivity.IMAGE_DESK);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap image = BitmapFactory.decodeFile(pathImage, options);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(imageDesckription + " " + "https://play.google.com/store/apps/details?id=bulat.diet.helper_sport&referrer=utm_source%3DDG_FB_WALL_WC")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
        runOnUiThread(successRunnable);
        onBackPressed();
    }
    Runnable successRunnable = new Runnable() {
        public void run() {
            Toast.makeText(getApplicationContext(), getString(R.string.vk_toast), Toast.LENGTH_LONG).show();
            Date currDate = new Date();
            if (currDate.getTime() > SaveUtils.getEndPDate(getApplicationContext())) {
                SaveUtils.setEndPDate(currDate.getTime() + 7 * DateUtils.DAY_IN_MILLIS, getApplicationContext());
                SaveUtils.setUseFreeAbonement(true, getApplicationContext());
            }
            onBackPressed();
        }
    };
}
