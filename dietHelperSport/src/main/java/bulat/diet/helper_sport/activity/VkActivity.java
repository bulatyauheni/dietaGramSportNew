package bulat.diet.helper_sport.activity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Account;
import bulat.diet.helper_sport.item.VKFriends;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;

import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

public class VkActivity extends Activity {

    private static final String FCP_VK_FRIEND_INVITE_SUCCESS = "FCP_VK_FRIEND_INVITE_SUCCESS";
    private static final String FCP_VK_WALL_POST_SUCCESS = "FCP_VK_WALL_POST_SUCCESS";
    private static final String FCP_VK_USER_JOIN_SUCCESS = "FCP_VK_USER_JOIN_SUCCESS";
    private static final String FCP_VK_USER_JOIN_ERROR = "FCP_VK_USER_JOIN_ERROR";
    private static final String FCP_VK_WALL_POST_ERROR = "FCP_VK_WALL_POST_ERROR";
    private static final String FCP_VK_FRIEND_INVITE_ERROR = "FCP_VK_FRIEND_INVITE_ERROR";
    private String vkUrl = "https://oauth.vk.com/authorize?client_id=3583596&scope=wall,offline&redirect_uri=oauth.vk.com/blank.html&display=touch&response_type=token";
    private String postUrl = "https://oauth.vk.com/method/wall.post?message=%s&attachment=%s&access_token=%s";
    private final String ACCESS_TOKEN = "blank.html#access_token";
    private final String SEND = "{\"response\":{\"processing\":1}}";
    public static final String MESSAGE = "MESSAGE";
    public static final String LINK = "LINK";
    public static final String IMAGE_DESK = "IMAGE_DESK";
    public static final String IMAGE_PATH = "IMAGE_PATH";
    private final int REQUEST_LOGIN = 1;
    private static final String FCP_VK_SHARE = "FCP_VK_SHARE";
    private static final String FCP_VK_SHARE_CLICK = "FCP_VK_SHARE_CLICK";

    Button authorizeButton;
    Button logoutButton;
    Button postButton;
    EditText messageEditText;

    Account account = new Account();
    private String pathImage;
    private String imageDesckription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vk_share);
        GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_SHARE_CLICK, 1L);
        startLoginActivity();
        pathImage = getIntent().getStringExtra(IMAGE_PATH);
        imageDesckription = getIntent().getStringExtra(IMAGE_DESK);

    }

    private void startLoginActivity() {
        VKSdk.login(this, VKScope.WALL, VKScope.PHOTOS, VKScope.GROUPS, VKScope.FRIENDS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                File file = new File(pathImage);
/*                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.wall);*/
                final Bitmap photo = BitmapFactory.decodeFile(file.getPath());
                VKRequest request = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), 0, 60479154);
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                        makePost(new VKAttachments(photoModel), getString(R.string.app_name) + " - " + imageDesckription + " #Dietagram https://play.google.com/store/apps/details?id=bulat.diet.helper_sport&referrer=utm_source%3DDG_VK_WALL_WC", getMyId());
                    }

                    @Override
                    public void onError(VKError error) {
                        Toast.makeText(VkActivity.this, "onError1" + error.errorMessage, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                        Toast.makeText(VkActivity.this, "attemptFailed1", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(VkActivity.this, "onError2" + error.errorMessage, Toast.LENGTH_LONG).show();
            }

        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void makePost(VKAttachments att, String msg, final int ownerId) {
        VKParameters parameters = new VKParameters();
        parameters.put(VKApiConst.OWNER_ID, String.valueOf(ownerId));
        parameters.put(VKApiConst.ATTACHMENTS, att);
        parameters.put(VKApiConst.MESSAGE, msg);
        VKRequest post = VKApi.wall().post(parameters);
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_WALL_POST_SUCCESS, 1L);
                VKRequest request = VKApi.groups().join(VKParameters.from(VKApiConst.GROUP_ID, 47759767));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_USER_JOIN_SUCCESS, 1L);
                        runOnUiThread(successRunnable);
                        inviteFriends();
                    }
                    @Override
                    public void onError(VKError error) {
                        GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_USER_JOIN_ERROR, 1L);
                        Toast.makeText(VkActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }

                });
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(VkActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_WALL_POST_ERROR, 1L);
                onBackPressed();
            }
        });
    }

    int getMyId() {
        final VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        return vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
    }


    /*  private void joinToGroup() {
          //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
          new Thread() {
              @Override
              public void run() {
                  try {
                      String text = getString(R.string.vk_recomend);
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
  */
    Runnable successRunnable = new Runnable() {
        public void run() {
            Toast.makeText(getApplicationContext(), getString(R.string.vk_toast), Toast.LENGTH_LONG).show();
            Date currDate = new Date();
            if (currDate.getTime() > SaveUtils.getEndPDate(getApplicationContext())) {
                SaveUtils.setEndPDate(currDate.getTime() + 7 * DateUtils.DAY_IN_MILLIS, getApplicationContext());
                SaveUtils.setUseFreeAbonement(true, getApplicationContext());
                Toast.makeText(getApplicationContext(), getString(R.string.user_abonement_ok), Toast.LENGTH_LONG).show();
            }
        }
    };

    public void inviteFriends() {
        VKRequest request = VKApi.friends().get(VKParameters.from("order", "mobile" ));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Gson g = new Gson();
                VKFriends friends = g.fromJson(response.json.toString(), VKFriends.class);
                sendInvitations(friends.getResponse().getItems(), 0);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(VkActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Toast.makeText(VkActivity.this, "attemptFailed", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
    }
    Handler handler = new Handler();
    private void sendInvitations(final List<Integer> items, final int num) {
        if (num < (items.size() > 15 ? 15 : items.size())) {
            Class[] cArg = new Class[2];
            cArg[0] = String.class;
            cArg[1] = VKParameters.class;
            Method method = null;
            try {
                method = VKApi.groups().getClass().getSuperclass().getDeclaredMethod("prepareRequest", cArg);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            method.setAccessible(true);

            try {
                VKRequest request2 = (VKRequest) method.invoke(VKApi.groups(), "invite", VKParameters.from(VKApiConst.GROUP_ID, 47759767, "user_id", items.get(num)));
                request2.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        Log.e("Item n",  "" + num);
                        GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_FRIEND_INVITE_SUCCESS, 1L);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendInvitations(items, num + 1);
                            }
                        }, 500);
                    }

                    @Override
                    public void onError(VKError error) {
                        GATraker.sendEvent("VkActivity", FCP_VK_SHARE, FCP_VK_FRIEND_INVITE_ERROR, 1L);
                        Log.e("Item n",  "" + num +  " " + error.errorCode);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendInvitations(items, num + 1);
                            }
                        }, 500);
                    }

                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            onBackPressed();
        }

    }
}
