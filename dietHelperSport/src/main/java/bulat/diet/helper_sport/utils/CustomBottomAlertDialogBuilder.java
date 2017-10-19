package bulat.diet.helper_sport.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import bulat.diet.helper_sport.R;

/**
 * Alert dialog builder for custom application dialogs
 */
public class CustomBottomAlertDialogBuilder extends CustomAlertDialogBuilder {
    @SuppressWarnings("unused")
    private static final String TAG = "TradeDialogBuilder";

    private final Activity context;
    NumberPicker picker1;
    NumberPicker picker2;
    /**
     * Class constructor.
     *
     * @param context
     *            Parent activity for dialog
     */
    public CustomBottomAlertDialogBuilder(Activity context) {
        super(context, R.style.TradeDialogTheme);
        this.context = context;
    }

    @Override
    public AlertDialog show() {
        dialog = super.show();
        if (dialog != null) {
            initDialogSize(dialog);
        }
        return dialog;
    }

    private void initDialogSize(AlertDialog dialog) {

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setWindowAnimations(R.style.DialogAnimation);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // retrieve display dimensions
            setWindowWidth(window, 100);
        } else {
                // retrieve display dimensions
            setWindowWidth(window, 55);
        }
    }

    private void setWindowWidth(Window window, int percetageOfWidth) {
        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = displayRectangle.width() * percetageOfWidth/100;
        window.setAttributes(lp);
    }

    public void refresh() {
        initDialogSize(dialog);
    }

    public AlertDialog getDialog() {
        return dialog;
    }


    /**
     * Set text on negative button from resource with given Id
     *
     * @param negativeButtonTextId
     * @return
     */
    public CustomBottomAlertDialogBuilder setNegativeButtonText(int negativeButtonTextId) {
        this.negativeButtonText = context.getResources().getString(
                negativeButtonTextId);
        return this;
    }



    /**
     * Set layout used for inflating in dialog
     *
     * @param layoutId
     * @return
     */
    public CustomBottomAlertDialogBuilder setLayout(int layoutId) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(layoutId,
                (ViewGroup) context.findViewById(R.id.dialogRoot));
        dialogView.setPadding(0, 0, 0, 0);
        setView(dialogView);
        return this;
    }

    public CustomBottomAlertDialogBuilder setDimensionLabel(String label) {
        TextView demensionLabel = (TextView) rootView.findViewById(R.id.demensionLabel);
        demensionLabel.setText(label);
        return this;
    }

    public CustomBottomAlertDialogBuilder setFirstPicker(int min, int max, int value) {
        picker1 = (NumberPicker) rootView.findViewById(R.id.spinner1);
        picker1.setValue(value);
        picker1.setMinValue(min);
        picker1.setMaxValue(max);
        if (picker2 == null ) {
            dialogValue.put(FIRST_VALUE, "" + picker1.getValue());
        } else {
            dialogValue.put(FIRST_VALUE, "" + picker1.getValue());
            dialogValue.put(SECOND_VALUE, "" + picker2.getValue());
        }
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (picker2 == null ) {
                    dialogValue.put(FIRST_VALUE, "" + newVal);
                } else {
                    dialogValue.put(FIRST_VALUE, "" + newVal);
                    dialogValue.put(SECOND_VALUE, "" + picker2.getValue());
                }
            }
        });
        return this;
    }


    public CustomBottomAlertDialogBuilder setSecondPicker(int min, int max, int value) {
        picker2 = (NumberPicker) rootView.findViewById(R.id.spinner2);
        picker2.setValue(value);
        if (picker1 == null ) {
            dialogValue.put(SECOND_VALUE, "" + picker2.getValue());
        } else {
            dialogValue.put(FIRST_VALUE, "" + picker1.getValue());
            dialogValue.put(SECOND_VALUE, "" + picker2.getValue());
        }
        picker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    dialogValue.put(FIRST_VALUE, "" + picker1.getValue());
                    dialogValue.put(SECOND_VALUE, "" + newVal);
            }
        });
        return this;
    }


}
