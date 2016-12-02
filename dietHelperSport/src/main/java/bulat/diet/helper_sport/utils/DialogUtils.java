package bulat.diet.helper_sport.utils;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.DishType;

public class DialogUtils {
		
	public static void showDialog(Context context, String text){
		DialogInterface.OnClickListener doNothing = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(text)
				.setNeutralButton(R.string.dialog_ok, doNothing).create();
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}
	
	public static void showDialog(Context context, int text){
		DialogInterface.OnClickListener doNothing = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(text)
				.setNeutralButton(R.string.dialog_ok, doNothing).create();
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}
	
	public static void setArraySpinnerValues(Spinner s, int minval, int maxval, Context context) {
		ArrayList<DishType> values = new ArrayList<DishType>();
		for (int i = minval; i < maxval; i++) {
			values.add(new DishType( i, String.valueOf(i)));
		}
		
		ArrayAdapter<DishType>  adapter = new ArrayAdapter<DishType>(context, android.R.layout.simple_spinner_item, values);		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);	
	
	}
}
