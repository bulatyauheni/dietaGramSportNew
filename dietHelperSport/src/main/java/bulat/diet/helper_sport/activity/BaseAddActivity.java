package bulat.diet.helper_sport.activity;

import android.os.Bundle;
import android.widget.TextView;

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
