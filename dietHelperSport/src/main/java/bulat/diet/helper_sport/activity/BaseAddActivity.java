package bulat.diet.helper_sport.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.utils.MessagesUpdater;
import bulat.diet.helper_sport.utils.SaveUtils;

public class BaseAddActivity extends BaseActivity {
	protected Integer timeMMValue;
	protected Integer timeHHValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void setTime(TextView timeTW) {
		timeTW.setText(timeHHValue +  ":" + ((timeMMValue > 9) ? timeMMValue : "0" +timeMMValue));
	}
}
