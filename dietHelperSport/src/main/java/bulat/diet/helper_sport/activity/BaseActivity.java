package bulat.diet.helper_sport.activity;

import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.MessagesUpdater;
import bulat.diet.helper_sport.utils.SaveUtils;

public class BaseActivity extends AppCompatActivity {
    Handler myHandler;
    public static final String CUSTOM_INTENT = "bulatplus.intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SaveUtils.getLang(this).length() < 1) {
            SaveUtils.setLang("ru", this);
        }
        Locale locale = new Locale(SaveUtils.getLang(this));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                String message = b.getString("message");
                updateMessageCount(message);
            }
        };
        GATraker.sendScreen(this);
        super.onCreate(savedInstanceState);


    }

    protected void updateMessageCount(String message) {
        // TODO Auto-generated method stub
        DietHelperActivity parentActivity;
        parentActivity = (DietHelperActivity) this.getParent().getParent();
        parentActivity.changeSocialTabIndicator(3, message);
        TextView tv = (TextView) findViewById(R.id.textViewMessNumber);
        if (tv != null) {
            if (!"0".equals(message)) {
                tv.setText(" (" + message + ")");
            } else {
                tv.setText("");
            }
        }
    }

    @Override
    protected void onResume() {
        Locale locale = new Locale(SaveUtils.getLang(this));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        // TODO Auto-generated method stub
        super.onResume();
        // TODO Auto-generated method stub
        new MessagesUpdater(this, myHandler).execute();
        Intent i = new Intent();
        i.setAction(CUSTOM_INTENT);
        this.sendBroadcast(i);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Intent i = new Intent();
        i.setAction(CUSTOM_INTENT);
        this.sendBroadcast(i);
    }

    public void checkLimit(int sum) {
        int mode = SaveUtils.getMode(this);
        int customLimit = SaveUtils.getCustomLimit(this);
        if (customLimit > 0) {
            SaveUtils.saveBMR(String.valueOf(customLimit), this);
            SaveUtils.saveMETA(String.valueOf(customLimit), this);
        }
        //TextView tvSurplus = (TextView) findViewById(R.id.textViewSurplusValue);
        ImageView ivIcon = (ImageView) findViewById(R.id.statusIcon);
        switch (mode) {
            case 0:

                TextView tvLimit = (TextView) findViewById(R.id.textViewLimitValue);

                tvLimit.setText(String.valueOf(SaveUtils.getBMR(this)) + " " + getString(R.string.kcal));
                //if (tvSurplus != null) {
                //    tvSurplus.setText(String.valueOf(Integer.valueOf(SaveUtils.getBMR(this)) - sum));
                //}

                if (ivIcon != null) {
                    if (sum > Integer.parseInt(SaveUtils.getBMR(this))) {
                        ivIcon.setImageResource(R.drawable.icon_status_bad);
                    } else {
                        ivIcon.setImageResource(R.drawable.icon_status_best);
                    }
                }
                break;
            case 1:
                if (ivIcon != null) {
                    if (sum > Integer.parseInt(SaveUtils.getMETA(this))) {
                        ivIcon.setImageResource(R.drawable.icon_status_bad);
                    } else {
                        ivIcon.setImageResource(R.drawable.icon_status_best);
                    }
                }
                TextView tvLimit2 = (TextView) findViewById(R.id.textViewLimitValue);
                tvLimit2.setText(String.valueOf(SaveUtils.getMETA(this)) + " " + getString(R.string.kcal));
                //if (tvSurplus != null) {
                 //   tvSurplus.setText(String.valueOf(Integer.valueOf(SaveUtils.getMETA(this)) - sum));
                //}
                break;
            case 2:
                if (ivIcon != null) {
                    if (sum < Integer.parseInt(SaveUtils.getBMR(this))) {
                        ivIcon.setImageResource(R.drawable.icon_status_bad);
                    } else {
                        ivIcon.setImageResource(R.drawable.icon_status_best);
                    }
                }

                TextView tvLimit3 = (TextView) findViewById(R.id.textViewLimitValue);
                tvLimit3.setText(String.valueOf(SaveUtils.getMETA(this)) + " " + getString(R.string.kcal));

                //if (tvSurplus != null) {
                //    tvSurplus.setText(String.valueOf(Integer.valueOf(SaveUtils.getMETA(this)) - sum));
                //}
                break;
            default:
                break;
        }

    }
}
