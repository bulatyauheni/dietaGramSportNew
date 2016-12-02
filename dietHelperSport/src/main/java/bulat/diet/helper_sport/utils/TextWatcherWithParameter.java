package bulat.diet.helper_sport.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherWithParameter implements TextWatcher {

	EditText mLimitKK;
	EditText mProt;
	EditText mFat;
	EditText mCarb;
	String oldElementLimit;
	
	
	public TextWatcherWithParameter (EditText limitKK, EditText prot, EditText fat, EditText carb) {
		mLimitKK = limitKK;
		mProt = prot;
		mFat = fat;
		mCarb = carb;
	}

	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
		oldElementLimit = s.toString();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		String elementLimit = s.toString();
		String limit = mLimitKK.getText().toString();
		int currLimit = recalculate();
		mLimitKK.setText(String.valueOf(currLimit));
	}
	
	private int recalculate() {
		int currLimit = 0;
		
		if (!TextUtils.isEmpty(mProt.getText().toString())) {
			currLimit = 4 * Integer.parseInt(mProt.getText().toString());
		}
		
		if (!TextUtils.isEmpty(mFat.getText().toString())) {
			currLimit = currLimit + 9 * Integer.parseInt(mFat.getText().toString());
		}
		
		if (!TextUtils.isEmpty(mCarb.getText().toString())) {
			currLimit = currLimit + 4 * Integer.parseInt(mCarb.getText().toString());
		}
		return currLimit;
	}
}
