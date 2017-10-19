package bulat.diet.helper_sport.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.VolumeInfo;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;

/**
 * Alert dialog builder for custom application dialogs
 */
public class ChangeBodyParamsDialogBuilder extends CustomAlertDialogBuilder {
    @SuppressWarnings("unused")
    private static final String TAG = "TradeDialogBuilder";

    private final Activity context;
    private Map<String, String> weightValue;
    private Map<String, String> chestValue;
    private Map<String, String> bicepsValue;
    private Map<String, String> neckValue;
    private Map<String, String> pelvisValue;
    private Map<String, String> forearmValue;
    private Map<String, String> hipValue;
    private Map<String, String> waistValue;
    private Map<String, String> shinValue;

    /**
     * Class constructor.
     *
     * @param context Parent activity for dialog
     */
    public ChangeBodyParamsDialogBuilder(Activity context) {
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
        wlp.gravity = Gravity.CENTER_VERTICAL;
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
        lp.width = displayRectangle.width() * percetageOfWidth / 100;
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
    public ChangeBodyParamsDialogBuilder setNegativeButtonText(int negativeButtonTextId) {
        this.negativeButtonText = context.getResources().getString(
                negativeButtonTextId);
        return this;
    }

    @Override
    protected void setButton(int id, final View.OnClickListener listener, final DialogValueListener valueListener, String text, final boolean isAutoClose) {
            final Button button = (Button) rootView.findViewById(id);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        saveValues();
                        if (valueListener != null) {
                            valueListener.onNewDialogValue(dialogValue);
                        }
                        if (listener != null) {
                            listener.onClick(v);
                        }
                        if (isAutoClose) {
                            dialog.dismiss();
                        }
                    }
                });
            }

    }

    private void saveValues() {
        if (weightValue != null) {
            SaveUtils.saveWeight(Integer.parseInt(weightValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - Info.MIN_WEIGHT, context);
            SaveUtils.saveWeightDec(Integer.parseInt(weightValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (chestValue != null) {
            SaveUtils.saveChest(Integer.parseInt(chestValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_CHEST, context);
            SaveUtils.saveChestDec(Integer.parseInt(chestValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (neckValue != null) {
            SaveUtils.saveNeck(Integer.parseInt(neckValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_NECK, context);
            SaveUtils.saveNeckDec(Integer.parseInt(neckValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (bicepsValue != null) {
            SaveUtils.saveBiceps(Integer.parseInt(bicepsValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_BICEPS, context);
            SaveUtils.saveBicepsDec(Integer.parseInt(bicepsValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (pelvisValue != null) {
            SaveUtils.savePelvis(Integer.parseInt(pelvisValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_PELVIS, context);
            SaveUtils.savePelvisDec(Integer.parseInt(pelvisValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (forearmValue != null) {
            SaveUtils.saveForearm(Integer.parseInt(forearmValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_FOREARM, context);
            SaveUtils.saveForearmDec(Integer.parseInt(forearmValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (hipValue != null) {
            SaveUtils.saveHip(Integer.parseInt(hipValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_HIP, context);
            SaveUtils.saveHipDec(Integer.parseInt(hipValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (waistValue != null) {
            SaveUtils.saveWaist(Integer.parseInt(waistValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_WAIST, context);
            SaveUtils.saveWaistDec(Integer.parseInt(waistValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        if (shinValue != null) {
            SaveUtils.saveShin(Integer.parseInt(shinValue.get(CustomAlertDialogBuilder.FIRST_VALUE)) - VolumeInfo.MIN_SHIN, context);
            SaveUtils.saveShinDec(Integer.parseInt(shinValue.get(CustomAlertDialogBuilder.SECOND_VALUE)), context);
        }
        Date curentDateandTime = new Date();
        Date start = new Date();
        Locale locale;
        if ("".endsWith(SaveUtils.getLang(context))) {
            locale = new Locale("ru");
        } else {
            locale = new Locale(SaveUtils.getLang(context));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", locale);
        try {
            start = sdf.parse(sdf.format(new Date(curentDateandTime.getTime())));
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        start.setYear(curentDateandTime.getYear());
        TodayDishHelper.updateBobyParams(
                context,
                String.valueOf(sdf.format(new Date(curentDateandTime.getTime()))),
                String.valueOf(SaveUtils.getRealWeight(context)),
                new BodyParams(String.valueOf((float) SaveUtils.getChest(context) + VolumeInfo.MIN_CHEST + (float) SaveUtils.getChestDec(context) / 10),
                        String.valueOf((float) SaveUtils.getBiceps(context) + VolumeInfo.MIN_BICEPS + (float) SaveUtils.getBicepsDec(context) / 10),
                        String.valueOf((float) SaveUtils.getPelvis(context) + VolumeInfo.MIN_PELVIS + (float) SaveUtils.getPelvisDec(context) / 10),
                        String.valueOf((float) SaveUtils.getNeck(context) + VolumeInfo.MIN_NECK + (float) SaveUtils.getNeckDec(context) / 10),
                        String.valueOf((float) SaveUtils.getWaist(context) + VolumeInfo.MIN_WAIST + (float) SaveUtils.getWaistDec(context) / 10),
                        String.valueOf((float) SaveUtils.getForearm(context) + VolumeInfo.MIN_FOREARM + (float) SaveUtils.getForearmDec(context) / 10),
                        String.valueOf((float) SaveUtils.getHip(context) + VolumeInfo.MIN_HIP + (float) SaveUtils.getHipDec(context) / 10),
                        String.valueOf((float) SaveUtils.getShin(context) + VolumeInfo.MIN_SHIN + (float) SaveUtils.getShinDec(context) / 10)));

    }


    /**
     * Set layout used for inflating in dialog
     *
     * @param layoutId
     * @return
     */
    public ChangeBodyParamsDialogBuilder setLayout(int layoutId) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(layoutId,
                (ViewGroup) context.findViewById(R.id.dialogRoot));
        dialogView.setPadding(0, 0, 0, 0);

        LinearLayout l1 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutForearm);
        if (SaveUtils.getForearmEnbl(context)) l1.setVisibility(View.VISIBLE);
        LinearLayout l2 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutWaist);
        if (SaveUtils.getWaistEnbl(context)) l2.setVisibility(View.VISIBLE);
        LinearLayout l3 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutChest);
        if (SaveUtils.getChestEnbl(context)) l3.setVisibility(View.VISIBLE);
        LinearLayout l4 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutNeck);
        if (SaveUtils.getNeckEnbl(context)) l4.setVisibility(View.VISIBLE);
        LinearLayout l5 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutShin);
        if (SaveUtils.getShinEnbl(context)) l5.setVisibility(View.VISIBLE);
        LinearLayout l6 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutBiceps);
        if (SaveUtils.getBicepsEnbl(context)) l6.setVisibility(View.VISIBLE);
        LinearLayout l7 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutPelvis);
        if (SaveUtils.getPelvisEnbl(context)) l7.setVisibility(View.VISIBLE);
        LinearLayout l8 = (LinearLayout) dialogView.findViewById(R.id.linearLayoutHip);
        if (SaveUtils.getHipEnbl(context)) l8.setVisibility(View.VISIBLE);
        //StringUtils.setSpinnerValues(dialogView, context);
        TextView buttonAdd = (TextView) dialogView.findViewById(R.id.add_body_param_btn);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(context, VolumeInfo.class);
                context.startActivity(intent);
            }
        });
        final TextView weightSpinner = (TextView) dialogView.findViewById(R.id.weight);
        weightSpinner.setText("" + SaveUtils.getRealWeight(context));
        weightSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(Info.MIN_WEIGHT, Info.MAX_WEIGHT, Info.MIN_WEIGHT + SaveUtils.getWeight(context))
                        .setSecondPicker(0, 9, SaveUtils.getWeightDec(context))
                        .setMessage(context.getString(R.string.body_weight))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                weightValue = value;
                                weightSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });


        final TextView chestSpinner = (TextView) dialogView.findViewById(R.id.chest);
        chestSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.CHEST, SaveUtils.CHESTDEC, VolumeInfo.MIN_CHEST));
        chestSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_CHEST,180, VolumeInfo.MIN_CHEST + SaveUtils.getChest(context))
                        .setSecondPicker(0, 9, SaveUtils.getChestDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_chest))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                chestValue = value;
                                chestSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        final TextView bicepsSpinner = (TextView) dialogView.findViewById(R.id.biceps);
        bicepsSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.BICEPS, SaveUtils.BICEPSDEC, VolumeInfo.MIN_BICEPS));
        bicepsSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_BICEPS,80, VolumeInfo.MIN_BICEPS + SaveUtils.getBiceps(context))
                        .setSecondPicker(0, 9, SaveUtils.getBicepsDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_biceps))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                bicepsValue = value;
                                bicepsSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });
        final TextView neckSpinner = (TextView) dialogView.findViewById(R.id.neck);
        neckSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.NECK, SaveUtils.NECKDEC, VolumeInfo.MIN_NECK));
        neckSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_NECK,80, VolumeInfo.MIN_NECK + SaveUtils.getNeck(context))
                        .setSecondPicker(0, 9, SaveUtils.getNeckDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_neck))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                neckValue = value;
                                neckSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        final TextView pelvisSpinner = (TextView) dialogView.findViewById(R.id.pelvis);
        pelvisSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.PELVIS, SaveUtils.PELVISDEC, VolumeInfo.MIN_PELVIS));
        pelvisSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_PELVIS,200, VolumeInfo.MIN_PELVIS + SaveUtils.getPelvis(context))
                        .setSecondPicker(0, 9, SaveUtils.getPelvisDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_pelvis))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                pelvisValue = value;
                                pelvisSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        final TextView forearmSpinner = (TextView) dialogView.findViewById(R.id.forearm);
        forearmSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.FOREARM, SaveUtils.FOREARMDEC, VolumeInfo.MIN_FOREARM));
        forearmSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_FOREARM,60, VolumeInfo.MIN_FOREARM + SaveUtils.getForearm(context))
                        .setSecondPicker(0, 9, SaveUtils.getForearmDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_forearm))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                forearmValue = value;
                                forearmSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        final TextView hipSpinner = (TextView) dialogView.findViewById(R.id.hip);
        hipSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.HIP, SaveUtils.HIPDEC, VolumeInfo.MIN_HIP));
        hipSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_HIP,100, VolumeInfo.MIN_HIP + SaveUtils.getHip(context))
                        .setSecondPicker(0, 9, SaveUtils.getHipDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_hip))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                hipValue = value;
                                hipSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        final TextView waistSpinner = (TextView) dialogView.findViewById(R.id.waist);
        waistSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.WAIST, SaveUtils.WAISTDEC, VolumeInfo.MIN_WAIST));
        waistSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_WAIST,300, VolumeInfo.MIN_WAIST + SaveUtils.getWaist(context))
                        .setSecondPicker(0, 9, SaveUtils.getWaistDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_waist))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                waistValue = value;
                                waistSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        final TextView shinSpinner = (TextView) dialogView.findViewById(R.id.shin);
        shinSpinner.setText("" + SaveUtils.getRealValue(context, SaveUtils.SHIN, SaveUtils.SHINDEC, VolumeInfo.MIN_SHIN));
        shinSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(context);
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(VolumeInfo.MIN_SHIN,70, VolumeInfo.MIN_SHIN + SaveUtils.getShin(context))
                        .setSecondPicker(0, 9, SaveUtils.getShinDec(context))
                        .setDimensionLabel(context.getString(R.string.santimetr))
                        .setMessage(context.getString(R.string.volume_shin))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                shinValue = value;
                                shinSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });
        setView(dialogView);
        return this;
    }

}
