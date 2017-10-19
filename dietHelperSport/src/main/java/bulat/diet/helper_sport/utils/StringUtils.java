package bulat.diet.helper_sport.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.util.Patterns;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.VolumeInfo;

public class StringUtils {

	public static String getEmail(Context context) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		String email = null;
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				email = account.name;
			}
		}
		return email;
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
	
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString().trim();
		} else {       
			return "";
		}
	}

	public static String toSingleString(String[] idsUpdated) {
		if (idsUpdated.length > 0) {
			StringBuilder nameBuilder = new StringBuilder();

			for (String n : idsUpdated) {
				nameBuilder.append(n).append(",");
			}
			nameBuilder.deleteCharAt(nameBuilder.length() - 1);
			return nameBuilder.toString();
		} else {
			return "";
		}
	}


	public static void setSpinnerValues(Dialog dialog, Context ctx) {
		try{
			Spinner chestSpinner = (Spinner) dialog.findViewById(R.id.SpinnerChest);
			Spinner chestDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerChestDecimal);

			Spinner pelvisSpinner = (Spinner) dialog.findViewById(R.id.SpinnerPelvis);
			Spinner pelvisDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerPelvisDecimal);

			Spinner neckSpinner = (Spinner) dialog.findViewById(R.id.SpinnerNeck);
			Spinner neckDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerNeckDecimal);

			Spinner bicepsSpinner = (Spinner) dialog.findViewById(R.id.SpinnerBiceps);
			Spinner bicepsDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerBicepsDecimal);

			Spinner forearmSpinner = (Spinner) dialog.findViewById(R.id.SpinnerForearm);
			Spinner forearmDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerForearmDecimal);

			Spinner waistSpinner = (Spinner) dialog.findViewById(R.id.SpinnerWaist);
			Spinner waistDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerWaistDecimal);

			Spinner hipSpinner = (Spinner) dialog.findViewById(R.id.SpinnerHip);
			Spinner hipDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerHipDecimal);

			Spinner shinSpinner = (Spinner) dialog.findViewById(R.id.SpinnerShin);
			Spinner shinDecSpinner = (Spinner) dialog.findViewById(R.id.SpinnerShinDecimal);

			DialogUtils.setArraySpinnerValues(chestSpinner, VolumeInfo.MIN_CHEST,180,ctx);
			chestSpinner.setSelection(SaveUtils.getChest(ctx));
			//chestSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(chestDecSpinner,0,9,ctx);
			chestDecSpinner.setSelection(SaveUtils.getChestDec(ctx));
			//chestDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(pelvisSpinner,VolumeInfo.MIN_PELVIS,200,ctx);
			pelvisSpinner.setSelection(SaveUtils.getPelvis(ctx));
			//pelvisSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(pelvisDecSpinner,0,9,ctx);
			pelvisDecSpinner.setSelection(SaveUtils.getPelvisDec(ctx));
			//pelvisDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(neckSpinner,VolumeInfo.MIN_NECK,70,ctx);
			neckSpinner.setSelection(SaveUtils.getNeck(ctx));
			//neckSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(neckDecSpinner,0,9,ctx);
			neckDecSpinner.setSelection(SaveUtils.getNeckDec(ctx));
			//neckDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(bicepsSpinner,VolumeInfo.MIN_BICEPS,78,ctx);
			bicepsSpinner.setSelection(SaveUtils.getBiceps(ctx));
			//bicepsSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(bicepsDecSpinner,0,9,ctx);
			bicepsDecSpinner.setSelection(SaveUtils.getBicepsDec(ctx));
			//bicepsDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(forearmSpinner,VolumeInfo.MIN_FOREARM,60,ctx);
			forearmSpinner.setSelection(SaveUtils.getForearm(ctx));
			//forearmSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(forearmDecSpinner,0,9,ctx);
			forearmDecSpinner.setSelection(SaveUtils.getForearmDec(ctx));
			//forearmDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(waistSpinner,VolumeInfo.MIN_WAIST,300,ctx);
			waistSpinner.setSelection(SaveUtils.getWaist(ctx));
			//waistSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(waistDecSpinner,0,9,ctx);
			waistDecSpinner.setSelection(SaveUtils.getWaistDec(ctx));
			//waistDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(hipSpinner,VolumeInfo.MIN_HIP,100,ctx);
			hipSpinner.setSelection(SaveUtils.getHip(ctx));
			//hipSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(hipDecSpinner,0,9,ctx);
			hipDecSpinner.setSelection(SaveUtils.getHipDec(ctx));
			//hipDecSpinner.setOnItemSelectedListener(spinnerListener);

			DialogUtils.setArraySpinnerValues(shinSpinner,VolumeInfo.MIN_SHIN,70,ctx);
			shinSpinner.setSelection(SaveUtils.getShin(ctx));
			//shinSpinner.setOnItemSelectedListener(spinnerListener);
			DialogUtils.setArraySpinnerValues(shinDecSpinner,0,9,ctx);
			shinDecSpinner.setSelection(SaveUtils.getShinDec(ctx));
			//shinDecSpinner.setOnItemSelectedListener(spinnerListener);

		}catch (Exception e) {
			e.printStackTrace();
		}

	}
}
