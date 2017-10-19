package bulat.diet.helper_sport.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.controls.ImagedSelector;
import bulat.diet.helper_sport.controls.TitledSwitch;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.CustomBottomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.CustomLimitsDialogBuilder;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;

public class Info extends AppCompatActivity {
    public static final int MIN_WEIGHT = 30;
    public static final int MAX_WEIGHT = 200;
    public static final int MIN_HEIGHT = 140;
    public static final int MAX_HEIGHT = 210;
    public static final int MIN_AGE = 18;
    public static final int MAX_AGE = 90;
    public static final double INDEX_FAT = 0.2 / 9;
    public static final double INDEX_CARBON = 0.5 / 4;
    public static final double INDEX_PROTEIN = 0.3 / 4;

    private TextView ageSpinner;
    private TextView highSpinner;

    double BMI;
    int BMR;
    int META;

    private SimpleDateFormat sdf;

    private TextView weightSelector;
    private TitledSwitch sexSelector;
    private TextView textViewActivity;
    private TextView textViewMode;
    private TextView limitTextView;
    private TextView BMIDiagnoz;
    private TextView BMILabel;
    private CheckBox cbLimit;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
                R.layout.settings, null);
        setContentView(viewToLoad);

        final ArrayList<String> activity = new ArrayList<String>();
        activity.add(getString(R.string.level_1));
        activity.add(getString(R.string.level_2));
        activity.add(getString(R.string.level_3));
        activity.add(getString(R.string.level_4));
        activity.add(getString(R.string.level_5));


        final ArrayList<String> mode = new ArrayList<String>();
        mode.add(getString(R.string.losing_weight));
        mode.add(getString(R.string.keeping_weight));
        mode.add(getString(R.string.gaining_weight));



        textViewActivity = (TextView) findViewById(R.id.textViewActivity);

        ImagedSelector activityLevelSelector = (ImagedSelector) findViewById(R.id.activityLevelSelector);
        activityLevelSelector.setOnItemSelectListener(new ImagedSelector.ItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                SaveUtils.saveActivity((int) position, Info.this);
                onValuesUpdated();
                textViewActivity.setText(activity.get(position - 1));
            }
        });

        textViewMode = (TextView) findViewById(R.id.textViewMode);
        ImagedSelector modeSelector = (ImagedSelector) findViewById(R.id.dietaModeSelector);
        modeSelector.setOnItemSelectListener(new ImagedSelector.ItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                SaveUtils.saveMode(position-1, Info.this);
                onValuesUpdated();
                textViewMode.setText(mode.get(position - 1));
            }
        });

        initLimits();

        limitTextView = (TextView) findViewById(R.id.textViewLimit);
        limitTextView.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                CustomLimitsDialogBuilder bld = new CustomLimitsDialogBuilder(Info.this.getParent().getParent());
                bld.setLayout(R.layout.section_custom_limits_alert_dialog)
                        .setMessage(getBaseContext().getString(R.string.castom_limit))
                        .setPositiveButton(R.id.dialogButtonOk, null, new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                onValuesUpdated();
                            }
                        });
                bld.show();

                //is chkIos checked?
               /*
*/
            }
        });
        cbLimit = (CheckBox) findViewById(R.id.cbLimit);
        cbLimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if (((CheckBox) v).isChecked()) {
                    int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, META, Info.this);
                    if (SaveUtils.getCustomLimit(Info.this) == 0 ) {
                        SaveUtils.setCustomLimit(Integer.valueOf(SaveUtils.getLimit(Info.this)), Info.this);
                    }
                    limitTextView.setEnabled(true);
                    //limitTextView.setText(limitKkal > 0 ? String.valueOf(limitKkal) : String.valueOf(META));
                } else {
                    SaveUtils.setCustomLimit(0, Info.this);
                    limitTextView.setEnabled(false);
                    LinearLayout limitsLayout = (LinearLayout) viewToLoad.findViewById(R.id.linearLayoutLimits);
                    limitsLayout.setVisibility(View.VISIBLE);
                }
                onValuesUpdated();
            }
        });
        ageSpinner = (TextView) findViewById(R.id.SpinnerAge);
        ageSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(Info.this.getParent().getParent());
                bld.setLayout(R.layout.section_alert_dialog_single_picker_one_button)
                        .setFirstPicker(MIN_AGE, MAX_AGE, MIN_AGE + SaveUtils.getAge(Info.this))
                        .setDimensionLabel(getBaseContext().getString(R.string.years))
                        .setMessage(getBaseContext().getString(R.string.age_colon))
                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                SaveUtils.saveAge(Integer.parseInt(value.get(CustomAlertDialogBuilder.FIRST_VALUE)) - MIN_AGE, Info.this);
                                ageSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE));
                                onValuesUpdated();
                            }
                        }, new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });
        sexSelector = (TitledSwitch) findViewById(R.id.sex_mode);
        sexSelector.setChecked(SaveUtils.getSex(getBaseContext()) == 1);
        sexSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                   @Override
                                                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                       if (isChecked) {
                                                           SaveUtils.saveSex(1, getBaseContext());
                                                       } else {
                                                           SaveUtils.saveSex(0, getBaseContext());
                                                       }
                                                       onValuesUpdated();
                                                   }
                                               }
        );

        weightSelector = (TextView) findViewById(R.id.SpinnerWeight);
        weightSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(Info.this.getParent().getParent());
                bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                        .setFirstPicker(MIN_WEIGHT, MAX_WEIGHT, MIN_WEIGHT + SaveUtils.getWeight(Info.this))
                        .setSecondPicker(0, 9, SaveUtils.getWeightDec(Info.this))
                        .setDimensionLabel(getBaseContext().getString(R.string.kgram))
                        .setMessage(Info.this.getParent().getParent().getString(R.string.body_weight))

                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                SaveUtils.saveWeight(Integer.parseInt(value.get(CustomAlertDialogBuilder.FIRST_VALUE)) - MIN_WEIGHT, Info.this);
                                SaveUtils.saveWeightDec(Integer.parseInt(value.get(CustomAlertDialogBuilder.SECOND_VALUE)), Info.this);
                                weightSelector.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE) + ", " + value.get(CustomAlertDialogBuilder.SECOND_VALUE));
                                onValuesUpdated();
                            }
                        }, new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });
        highSpinner = (TextView) findViewById(R.id.SpinnerHeight);
        highSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(Info.this.getParent().getParent());
                bld.setLayout(R.layout.section_alert_dialog_single_picker_one_button)
                        .setFirstPicker(MIN_HEIGHT, MAX_HEIGHT, MIN_HEIGHT + SaveUtils.getHeight(Info.this))
                        .setDimensionLabel(getBaseContext().getString(R.string.santimetr))
                        .setMessage(Info.this.getParent().getParent().getString(R.string.body_weight))

                        .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                            @Override
                            public void onNewDialogValue(Map<String, String> value) {
                                SaveUtils.saveHeight(Integer.parseInt(value.get(CustomAlertDialogBuilder.FIRST_VALUE)) - MIN_HEIGHT, Info.this);
                                highSpinner.setText(value.get(CustomAlertDialogBuilder.FIRST_VALUE));
                                onValuesUpdated();
                            }
                        }, new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        });
                bld.show();
            }
        });

        BMILabel = (TextView) findViewById(R.id.textViewBMI);
        BMIDiagnoz = (TextView) findViewById(R.id.bmi_diagnoz);
       /* BMRtextView = (TextView) findViewById(R.id.textViewMeta);
        MetatextView = (TextView) findViewById(R.id.textViewBMR);*/
        ////


        setSpinnerValues();
        Locale locale;
        if ("".endsWith(SaveUtils.getLang(this))) {
            locale = new Locale("ru");
        } else {
            locale = new Locale(SaveUtils.getLang(this));
        }
        sdf = new SimpleDateFormat("EEE dd MMMM", locale);

        activityLevelSelector.setSelectedItem(SaveUtils.getActivity(this));
        modeSelector.setSelectedItem(SaveUtils.getMode(this)+1);
    }

    private void initLimits() {
        int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, 0, this);
        int limitCarbon = SaveUtils.readInt(SaveUtils.LIMIT_CARBON, 0, this);
        int limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, 0, this);
        int sum = limitCarbon + limitFat + limitProtein;
        if (sum > 0 && sum != 100) {
            limitProtein = (int) (limitProtein * 100 / sum);
            limitCarbon = limitCarbon * 100 / sum;
            limitFat = 100 - limitProtein - limitCarbon;
            SaveUtils.writeInt(SaveUtils.LIMIT_PROTEIN, limitProtein, this);
            SaveUtils.writeInt(SaveUtils.LIMIT_CARBON, limitCarbon, this);
            SaveUtils.writeInt(SaveUtils.LIMIT_FAT, limitFat, this);
        }
    }

    private void setSpinnerValues() {
        try {
            ageSpinner.setText("" + (SaveUtils.getAge(this) + MIN_AGE));
            weightSelector.setText("" + (SaveUtils.getRealWeight(this)));
            highSpinner.setText("" + (SaveUtils.getHeight(this) + MIN_HEIGHT));

            ArrayList<DishType> genders = new ArrayList<DishType>();
            genders.add(new DishType(166, getString(R.string.male)));
            genders.add(new DishType(0, getString(R.string.female)));
            ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
                    android.R.layout.simple_spinner_item, genders);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        } catch (Exception e) {
            e.printStackTrace();
            SaveUtils.saveAge(0, Info.this);
            SaveUtils.saveActivity(0, Info.this);
            SaveUtils.saveHeight(0, Info.this);
            SaveUtils.saveSex(0, Info.this);
            SaveUtils.saveWeight(0, Info.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SaveUtils.isTrialNotified(Info.this) && TodayDishHelper.getDaysCount(Info.this) < 2) {
            CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(Info.this.getParent().getParent());
            bld.setLayout(R.layout.section_alert_dialog_one_button)
                    .setMessage(Info.this.getParent().getString(R.string.info_trial))
                    .setPositiveButton(R.id.dialogButtonOk, null, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            SaveUtils.setTrialNotified(true, Info.this);
                        }
                    })
                    .setPositiveButtonText(R.string.ok);
            bld.show();
        }
        try {
            BMI = Double.parseDouble(SaveUtils.getBMI(this));
            String addText = "";
            if (BMI < 18.5) {
                addText = getString(R.string.underweight);

            } else if (BMI < 24.9) {
                addText = getString(R.string.normal_weight);

            } else if (BMI < 29.9) {
                addText = getString(R.string.overweight);

            } else {
                addText = getString(R.string.obese);

            }
            BMILabel.setText(getString(R.string.bmi) + BMI);
            BMIDiagnoz.setText(addText);
            int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, 0, this);
            if (limitKkal > 0) {
                cbLimit.setChecked(true);
                limitTextView.setText(String.valueOf(limitKkal) + " " + getString(R.string.kcal));
            } else {
                switch (SaveUtils.getMode(this)) {
                    case 0:
                        limitTextView.setText(String.valueOf(Integer.valueOf(SaveUtils.getBMR(this))) + " " + getString(R.string.kcal));
                        break;
                    case 1:
                        limitTextView.setText(String.valueOf(Integer.valueOf(SaveUtils.getMETA(this))) + " " + getString(R.string.kcal));
                        break;
                    case 2:
                        limitTextView.setText(String.valueOf(SaveUtils.getMETA(this)) + " " + getString(R.string.kcal));
                        break;
                    default:
                        break;
                }
            }
            /*BMRtextView.setText(SaveUtils.getBMR(this) + "("
                    + getString(R.string.kcal_day) + ")");
            MetatextView.setText(SaveUtils.getMETA(this) + "("
                    + getString(R.string.kcal_day) + ")");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSpinnerValues();
    }

    private void onValuesUpdated() {
        BMI = SaveUtils.getRealWeight(this)
                / (0.01 * (SaveUtils.getHeight(this) + MIN_HEIGHT)
                * 0.01 * (SaveUtils.getHeight(this) + MIN_HEIGHT));
        BMI = Math.round(BMI * 10.0) / 10.0;
        String addText = "";
        if (BMI < 18.5) {
            addText = getString(R.string.underweight);
        } else if (BMI < 24.9) {
            addText = getString(R.string.normal_weight);
        } else if (BMI < 29.9) {
            addText = getString(R.string.overweight);
        } else {
            addText = getString(R.string.obese);
        }

        BMILabel.setText(getString(R.string.bmi) + BMI);
        BMIDiagnoz.setText(addText);

        BMR = (int) (
                (10 * SaveUtils.getRealWeight(this)) +
                        (6.25 * (SaveUtils.getHeight(this) + MIN_HEIGHT)) -
                        (5 * (SaveUtils.getAge(this) + MIN_AGE)) -
                        161 +
                        (SaveUtils.getSex(this) == 1 ? 0 : 166));

/*  The Harris–Benedict equations revised by Mifflin and St Jeor in 1990:[
      Men	BMR = (10 × weight in kg) + (6.25 × height in cm) - (5 × age in years) + 5
      Women	BMR = (10 × weight in kg) + (6.25 × height in cm) - (5 × age in years) - 161*/

        BMR = (int) BMR;
        if (SaveUtils.getActivity(this) == 1) {
            META = (int) (BMR * 1.2);

        } else if (SaveUtils.getActivity(this) == 2) {
            META = (int) (BMR * 1.35);
        } else if (SaveUtils.getActivity(this) == 3) {
            META = (int) (BMR * 1.55);
        } else if (SaveUtils.getActivity(this) == 4) {
            META = (int) (BMR * 1.75);
        } else if (SaveUtils.getActivity(this) == 5) {
            META = (int) (BMR * 1.92);
        }
        int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, 0, this);
        if (limitKkal > 0) {
            limitTextView.setText(String.valueOf(limitKkal) + " " + getString(R.string.kcal));
        } else {
            switch (SaveUtils.getMode(this)) {
                case 0:
                    limitTextView.setText((int)(META * 0.8) + " " + getString(R.string.kcal));
                    break;
                case 1:
                    limitTextView.setText(META + " " + getString(R.string.kcal));
                    break;
                case 2:
                    limitTextView.setText(META + " " + getString(R.string.kcal));
                    break;
                default:
                    break;
            }
        }


           /* BMRtextView.setText(String.valueOf((int) (META * 0.8)) + " "
                    + getString(R.string.calorie_day));

            MetatextView.setText(String.valueOf(META) + " "
                    + getString(R.string.calorie_day));*/
        saveAll();
    }

    public void saveAll() {

        try {

            SaveUtils.saveBMI(String.valueOf(BMI), Info.this);
            SaveUtils.saveBMR(String.valueOf((int) (META * 0.8)), Info.this);
            SaveUtils.saveMETA(String.valueOf(META), Info.this);

            //SaveUtils.saveHeight((int) highSpinner.getSelectedItemId(),
            //Info.this);
            //SaveUtils.saveSex((int) sexSpinner.getSelectedItemId(), Info.this);
            //SaveUtils.saveWeight((int) weightSpinner.getSelectedItemId(),
            //		Info.this);
            //SaveUtils.saveWeightDec((int) weightSpinnerDec.getSelectedItemId(),
            //		Info.this);
            Date curentDateandTime = new Date();
            Date start = new Date();

            try {
                start = sdf.parse(sdf.format(new Date(curentDateandTime.getTime())));
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            start.setYear(curentDateandTime.getYear());
            TodayDishHelper.updateBobyParams(
                    Info.this,
                    String.valueOf(sdf.format(new Date(curentDateandTime.getTime()))),
                    String.valueOf(SaveUtils.getRealWeight(Info.this)),
                    new BodyParams(String.valueOf((float) SaveUtils.getChest(Info.this) + VolumeInfo.MIN_CHEST + (float) SaveUtils.getChestDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getBiceps(Info.this) + VolumeInfo.MIN_BICEPS + (float) SaveUtils.getBicepsDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getPelvis(Info.this) + VolumeInfo.MIN_PELVIS + (float) SaveUtils.getPelvisDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getNeck(Info.this) + VolumeInfo.MIN_NECK + (float) SaveUtils.getNeckDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getWaist(Info.this) + VolumeInfo.MIN_WAIST + (float) SaveUtils.getWaistDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getForearm(Info.this) + VolumeInfo.MIN_FOREARM + (float) SaveUtils.getForearmDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getHip(Info.this) + VolumeInfo.MIN_HIP + (float) SaveUtils.getHipDec(Info.this) / 10),
                            String.valueOf((float) SaveUtils.getShin(Info.this) + VolumeInfo.MIN_SHIN + (float) SaveUtils.getShinDec(Info.this) / 10)));

            // update social profile
            if (SaveUtils.getUserUnicId(this) != 0) {
                new SocialUpdater(this).execute();
            }
			/*
			 * BMI = Double.parseDouble(SaveUtils.getBMI(Info.this)); if(BMI <
			 * 18.5 ){ SaveUtils.saveMode(2,Info.this); } else if(BMI < 24.9 ){
			 * SaveUtils.saveMode(1,Info.this); } else if(BMI < 29.9 ){
			 * SaveUtils.saveMode(0,Info.this); } else {
			 * SaveUtils.saveMode(0,Info.this); }
			 */
            SaveUtils.setActivated(Info.this, true);
            // DialogUtils.showDialog(Info.this.getParent(),
            // getString(R.string.save_prove));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
