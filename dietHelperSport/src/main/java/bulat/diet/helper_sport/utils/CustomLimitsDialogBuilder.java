package bulat.diet.helper_sport.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.component.PrefixedEditText;
import io.apptik.widget.MultiSlider;

/**
 * Alert dialog builder for custom application dialogs
 */
public class CustomLimitsDialogBuilder extends CustomAlertDialogBuilder {
    @SuppressWarnings("unused")
    private static final String TAG = "TradeDialogBuilder";
    PrefixedEditText limitET;
    LinearLayout castomLimitLayout;
    LinearLayout castomLimitLayoutBSU;

    PrefixedEditText edtProtein;
    PrefixedEditText edtFat;
    PrefixedEditText edtCarbon;
    MultiSlider multiSlider;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbon;
    private final Activity context;
    private CheckBox chkIos;
    TextWatcherTotalCall textWatcherTotal = new TextWatcherTotalCall();
    private TextWatcherWithParameter textWatcherPFC;

    /**
     * Class constructor.
     *
     * @param context
     *            Parent activity for dialog
     */
    public CustomLimitsDialogBuilder(Activity context) {
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
    public CustomLimitsDialogBuilder setNegativeButtonText(int negativeButtonTextId) {
        this.negativeButtonText = context.getResources().getString(
                negativeButtonTextId);
        return this;
    }

    @Override
    protected void setButton(int id, final View.OnClickListener listener, final DialogValueListener valueListener, String text, final boolean isAutoClose) {
        if (listener != null) {
            final Button button = (Button) rootView.findViewById(id);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (validateDialog()) {

                            if (valueListener != null) {
                                valueListener.onNewDialogValue(dialogValue);
                            }
                            listener.onClick(v);

                            if (isAutoClose) {
                                dialog.dismiss();
                            }
                        }
                    }
                });
                if (text != null) {
                    button.setText(text);
                }
            }
        }
    }

    private boolean validateDialog() {
           limitET.setBackgroundColor(Color.WHITE);
                if (limitET.getText().length() < 3) {
                    limitET.setBackgroundColor(Color.RED);
                    return false;
                } else {
                    try {
                        SaveUtils.writeInt(SaveUtils.LIMIT, Integer.valueOf(limitET.getText().toString()), context);
                        SaveUtils.writeInt(SaveUtils.LIMIT_PROTEIN, Integer.valueOf(tvProtein.getText().toString()), context);
                        SaveUtils.writeInt(SaveUtils.LIMIT_CARBON, Integer.valueOf(tvCarbon.getText().toString()), context);
                        SaveUtils.writeInt(SaveUtils.LIMIT_FAT, Integer.valueOf(tvFat.getText().toString()), context);

                        Toast.makeText(context, context.getString(R.string.save_limit),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        limitET.setBackgroundColor(Color.RED);
                        e.printStackTrace();
                    }
                    return true;
                }
    }

    /**
     * Set layout used for inflating in dialog
     *
     * @param layoutId
     * @return
     */
    public CustomLimitsDialogBuilder setLayout(int layoutId) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(layoutId,
                (ViewGroup) context.findViewById(R.id.dialogRoot));
        dialogView.setPadding(0, 0, 0, 0);
        limitET = (PrefixedEditText) dialogView.findViewById(R.id.editTextLimitValue);
        edtProtein = (PrefixedEditText) dialogView.findViewById(R.id.editTextProtein);
        edtFat = (PrefixedEditText) dialogView.findViewById(R.id.editTextFat);
        edtCarbon = (PrefixedEditText) dialogView.findViewById(R.id.editTextCarbon);
        chkIos = (CheckBox) dialogView.findViewById(R.id.cbLimit);
        tvProtein = (TextView) dialogView.findViewById(R.id.textViewProtein);
        tvFat = (TextView) dialogView.findViewById(R.id.textViewFat);
        tvCarbon = (TextView) dialogView.findViewById(R.id.textViewCarbon);

        limitET.setPrefix(context.getString(R.string.kcal) + ":");
        limitET.addTextChangedListener(textWatcherTotal);
        textWatcherPFC = new TextWatcherWithParameter(limitET, edtProtein, edtFat, edtCarbon);
        edtProtein.addTextChangedListener(textWatcherPFC);
        edtCarbon.addTextChangedListener(textWatcherPFC);
        edtFat.addTextChangedListener(textWatcherPFC);

        edtProtein.setPrefix(context.getString(R.string.gram));
        edtFat.setPrefix(context.getString(R.string.gram));
        edtCarbon.setPrefix(context.getString(R.string.gram));
        multiSlider = (MultiSlider) dialogView.findViewById(R.id.range_slider5);
        int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, Integer.valueOf(SaveUtils.getMETA(context)), context);
        int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, 17, context);
        int limitCarbon = SaveUtils.readInt(SaveUtils.LIMIT_CARBON, 67, context);
        int limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, 16, context);
        multiSlider.getThumb(0).setValue(limitProtein);
        multiSlider.getThumb(1).setValue(limitFat + limitProtein);

        multiSlider.setOnThumbValueChangeListener(slideListener);
        if (limitKkal > 0) {
            limitET.setText(String.valueOf(limitKkal));
            castomLimitLayout = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCastomLimit);
            castomLimitLayoutBSU = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCastomLimitBSU);
            castomLimitLayout.setVisibility(View.VISIBLE);
            castomLimitLayoutBSU.setVisibility(View.VISIBLE);
            multiSlider.setVisibility(View.VISIBLE);
        }
        setView(dialogView);
        return this;
    }

    private class TextWatcherWithParameter implements TextWatcher {

        EditText mLimitKK;
        EditText mProt;
        EditText mFat;
        EditText mCarb;
        String oldElementLimit;


        public TextWatcherWithParameter(EditText limitKK, EditText prot, EditText fat, EditText carb) {
            mLimitKK = limitKK;
            mProt = prot;
            mFat = fat;
            mCarb = carb;
        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            oldElementLimit = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            mLimitKK.removeTextChangedListener(textWatcherTotal);
            int currLimit = recalculate();
            mLimitKK.setText(String.valueOf(currLimit));
            mLimitKK.addTextChangedListener(textWatcherTotal);

            if (currLimit == 0) {
                currLimit = 1;
            }
            multiSlider.setOnThumbValueChangeListener(null);
            multiSlider.getThumb(0).setValue((int) getInt(mProt.getText().toString()) * 4 * 100 / currLimit);
            multiSlider.getThumb(1).setValue((int) (getInt(mProt.getText().toString()) * 4 + getInt(mFat.getText().toString()) * 9) * 100 / currLimit);
            tvProtein.setText("" + multiSlider.getThumb(0).getValue());
            tvFat.setText("" + (multiSlider.getThumb(1).getValue() - multiSlider.getThumb(0).getValue()));
            tvCarbon.setText("" + (100 - Integer.valueOf(tvFat.getText().toString()) - Integer.valueOf(tvProtein.getText().toString())));
            multiSlider.setOnThumbValueChangeListener(slideListener);
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
    private MultiSlider.OnThumbValueChangeListener slideListener = new MultiSlider.OnThumbValueChangeListener() {
        @Override
        public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {

            tvProtein.setText("" + CustomLimitsDialogBuilder.this.multiSlider.getThumb(0).getValue());

            tvFat.setText("" + (CustomLimitsDialogBuilder.this.multiSlider.getThumb(1).getValue() - CustomLimitsDialogBuilder.this.multiSlider.getThumb(0).getValue()));

            tvCarbon.setText("" + (100 - Integer.valueOf(tvFat.getText().toString()) - Integer.valueOf(tvProtein.getText().toString())));

            updatePFC();

        }
    };
    private int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private void updateLimits() {
        try {
            tvProtein.setText("" + multiSlider.getThumb(0).getValue());
            tvFat.setText("" + (multiSlider.getThumb(1).getValue() - multiSlider.getThumb(0).getValue()));
            tvCarbon.setText("" + (100 - multiSlider.getThumb(1).getValue()));
            multiSlider.getThumb(0).setValue(Integer.valueOf(tvProtein.getText().toString()));
            multiSlider.getThumb(1).setValue(Integer.valueOf(tvFat.getText().toString()) + Integer.valueOf(tvProtein.getText().toString()));
            multiSlider.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePFC() {
        edtFat.removeTextChangedListener(textWatcherPFC);
        edtCarbon.removeTextChangedListener(textWatcherPFC);
        edtProtein.removeTextChangedListener(textWatcherPFC);

        int limit = TextUtils.isEmpty(limitET.getText().toString()) ? 1 : (int) (Integer.valueOf(limitET.getText().toString()));
        edtCarbon.setText("" + (int) (limit * (100 - Integer.valueOf(tvFat.getText().toString()) - Integer.valueOf(tvProtein.getText().toString())) / 100) / 4);
        edtProtein.setText("" + (int) (limit * multiSlider.getThumb(0).getValue() / 100) / 4);
        edtFat.setText("" + (int) (limit * (multiSlider.getThumb(1).getValue() - multiSlider.getThumb(0).getValue()) / 100) / 9);
        edtProtein.addTextChangedListener(textWatcherPFC);
        edtCarbon.addTextChangedListener(textWatcherPFC);
        edtFat.addTextChangedListener(textWatcherPFC);
    }

    private class TextWatcherTotalCall implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateLimits();
        }
    }

}
