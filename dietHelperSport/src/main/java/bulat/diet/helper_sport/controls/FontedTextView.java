package bulat.diet.helper_sport.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontedTextView extends TextView {
	public FontedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface face = Typeface.createFromAsset(getContext().getAssets(),
				"font/helvetica-light.OTF");
		setTypeface(face);
	}
}