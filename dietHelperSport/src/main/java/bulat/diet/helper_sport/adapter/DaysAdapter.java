package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.activity.CalendarActivity;
import bulat.diet.helper_sport.activity.CalendarActivityGroup;
import bulat.diet.helper_sport.activity.Info;
import bulat.diet.helper_sport.activity.VolumeInfo;
import bulat.diet.helper_sport.db.DishProvider;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.BodyParams;
import bulat.diet.helper_sport.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.CustomBottomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;
import bulat.diet.helper_sport.utils.SocialUpdater;


public class DaysAdapter extends CursorAdapter {
    private static final String CALENDAR_WEIGHT_BUTTON_CLICK = "CALENDAR_WEIGHT_BUTTON_CLICK";
    public static final String WEIGHT_BTN = "WEIGHT_BTN";
    DecimalFormat df = new DecimalFormat("###.#");
    private Context ctx;
    private CalendarActivityGroup parent;
    CalendarActivity page;
    int height = 0;
    int age = 0;
    int sex = 0;
    int activity = 0;
    private String[] mParties;

    public DaysAdapter(CalendarActivity page, Context context, Cursor c,
                       CalendarActivityGroup calendarActivityGroup) {
        super(context, c);
        mParties = new String[]{"", "", ""};
        this.page = page;
        ctx = context;
        height = SaveUtils.getHeight(ctx) + Info.MIN_HEIGHT;
        age = SaveUtils.getAge(ctx) + Info.MIN_AGE;
        sex = SaveUtils.getSex(ctx);
        activity = SaveUtils.getActivity(ctx);
        this.parent = calendarActivityGroup;
        // TODO Auto-generated constructor stub
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.days_list_row, parent, false);
        return v;
        // return inflater.inflate(R.layout.item_list_row, null);
    }

    @Override
    public int getCount() {
        if (super.getCount() != 0) {
            return super.getCount();
        }
        return 0;
    }

    @Override
    public void bindView(View v, final Context context, Cursor c) {
        String dayDate = c.getString(c
                .getColumnIndex(DishProvider.TODAY_DISH_DATE));
        String itemCaloricity = c.getString(c.getColumnIndex("val"));
        String itemWoterWeight = c.getString(c.getColumnIndex("woterweight"));
        String itemWeight = c.getString(c.getColumnIndex("weight"));
        String itemBodyWeight = "";
        //if (Integer.parseInt(itemCaloricity) > 0) {

        try {
            if ((c.getInt(c.getColumnIndex("count")) - 1) > 0) {
                itemBodyWeight = String.valueOf(c.getFloat(c
                        .getColumnIndex("bodyweight")));
                v.setBackgroundResource(R.color.main_color);
            } else {
                itemBodyWeight = String.valueOf(c.getFloat(c
                        .getColumnIndex("bodyweight")));
                v.setBackgroundResource(R.color.main_color);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //} else {
        //	itemBodyWeight = String.valueOf(SaveUtils.getWeight(context)
        //			+ Info.MIN_WEIGHT);
        //v.setBackgroundResource(R.color.light_broun);
        //}
        TextView dateTextView = (TextView) v.findViewById(R.id.textViewDay);

        dateTextView.setText(dayDate.split(" ")[1]);
        dateTextView.setTag(dayDate);
        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEE", new Locale(
                SaveUtils.getLang(context)));
        SimpleDateFormat month = new SimpleDateFormat("MMM", new Locale(
                SaveUtils.getLang(context)));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
                SaveUtils.getLang(context)));
        TextView monthTextView = (TextView) v.findViewById(R.id.textViewMonth);

        try {
            monthTextView.setText(month.format(sdf.parse(dayDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView dayOfWeekTextView = (TextView) v.findViewById(R.id.textViewDayOFWeek);
        dayOfWeekTextView.setText(dayDate.split(" ")[0]);

        View iv = (View) v.findViewById(R.id.imageViewDay);
        int flag = checkLimit(Integer.parseInt(itemCaloricity), Float.valueOf(itemBodyWeight));
        if (0 == flag) {
            iv.setBackgroundResource(R.color.today_green);
        } else if (1 == flag) {
            iv.setBackgroundResource(R.color.red);
        } else {
            iv.setBackgroundResource(R.color.red);
        }

        LinearLayout le = (LinearLayout) v.findViewById(R.id.layoutEmpty);
        LinearLayout lf = (LinearLayout) v.findViewById(R.id.layoutParams);
        if ("0".equals(itemWeight) && "0".equals(itemCaloricity)) {
            lf.setVisibility(View.GONE);
            le.setVisibility(View.VISIBLE);

        } else {
            lf.setVisibility(View.VISIBLE);
            le.setVisibility(View.GONE);

            TextView fatView = (TextView) v.findViewById(R.id.textViewFat);
            fatView.setText(c.getString(c.getColumnIndex("fat")) == null ? "0" : c.getString(c.getColumnIndex("fat")));

            TextView carbonView = (TextView) v
                    .findViewById(R.id.textViewCarbon);
            carbonView.setText(c.getString(c.getColumnIndex("carbon")) == null ? "0" : c.getString(c.getColumnIndex("carbon")));

            TextView proteinView = (TextView) v
                    .findViewById(R.id.textViewProtein);
            proteinView.setText(c.getString(c.getColumnIndex("protein")) == null ? "0" : c.getString(c.getColumnIndex("protein")));

            TextView tvFatPercent = (TextView) v
                    .findViewById(R.id.textViewFatPercent);
            TextView tvCarbPercent = (TextView) v
                    .findViewById(R.id.textViewCarbonPercent);
            TextView tvProtPercent = (TextView) v
                    .findViewById(R.id.textViewProteinPercent);

            float protein = Float.valueOf(c.getString(c.getColumnIndex("protein")) == null ? "0" : c.getString(c.getColumnIndex("protein")));
            float fat = Float.valueOf(c.getString(c.getColumnIndex("fat")) == null ? "0" : c.getString(c.getColumnIndex("fat")));
            float carbon = Float.valueOf(c.getString(c.getColumnIndex("carbon")) == null ? "0" : c.getString(c.getColumnIndex("carbon")));

            float sum = protein + fat + carbon;

            tvFatPercent.setText("(" + df.format(fat * 100 / sum) + "%)");
            tvCarbPercent.setText("(" + df.format(carbon * 100 / sum) + "%)");
            tvProtPercent.setText("(" + df.format(protein * 100 / sum) + "%)");

            if (sum > 0) {
                tvFatPercent.setText("(" + df.format(Float.valueOf(fat) * 100 / sum)
                        + "%)");
                tvCarbPercent.setText("(" + df.format(Float.valueOf(carbon) * 100 / sum)
                        + "%)");
                tvProtPercent.setText("(" + df.format(Float.valueOf(protein) * 100 / sum)
                        + "%)");
            } else {
                tvFatPercent.setText("(0%)");
                tvCarbPercent.setText("(0%)");
                tvProtPercent.setText("(0%)");
            }

            TextView waterweightView = (TextView) v.findViewById(R.id.textViewWoter);
            waterweightView.setText(itemWoterWeight + " " + context.getString(R.string.gram));
            TextView caloricityView = (TextView) v
                    .findViewById(R.id.textViewCaloricity);

            caloricityView.setText(itemCaloricity + " "
                    + context.getString(R.string.kcal));
            TextView weightView = (TextView) v
                    .findViewById(R.id.textViewWeightTotal);

            weightView.setText(itemWeight == null ? "0" : itemWeight + " "
                    + context.getString(R.string.gram));
            if (itemWeight == null) {
                iv.setBackgroundResource(R.color.grey);
            }
            TextView bodyweightView = (TextView) v
                    .findViewById(R.id.textViewWeightBodyTotal);


            float currWeight = SaveUtils.getRealWeight(ctx);

            if (itemBodyWeight == null) {
                itemBodyWeight = "" + currWeight;
            }
            bodyweightView.setText(itemBodyWeight + " "
                    + context.getString(R.string.kgram));
        }


        Button weightButton = (Button) v.findViewById(R.id.buttonWeight);
        if (weightButton != null) {
            weightButton.setId(c.getInt(c.getColumnIndex("_id")));
        }
        TextView tvi = (TextView) v.findViewById(R.id.textViewId);
        tvi.setText(c.getString(c
                .getColumnIndex(DishProvider.TODAY_DISH_DATE)));
        if (weightButton != null) {

            // weightButton.setId(Integer.parseInt(c.getString(c.getColumnIndex(DishProvider.TODAY_DISH_DATE_LONG))));
            weightButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    Button mbut = (Button) v;
                    GATraker.sendEvent(WEIGHT_BTN, CALENDAR_WEIGHT_BUTTON_CLICK);
                    final TextView tvi2 = (TextView) ((View) mbut.getParent())
                            .findViewById(R.id.textViewId);
                    CustomBottomAlertDialogBuilder bld = new CustomBottomAlertDialogBuilder(parent);
                    bld.setLayout(R.layout.section_alert_dialog_picker_one_button)
                            .setFirstPicker(Info.MIN_WEIGHT, Info.MAX_WEIGHT, Info.MIN_WEIGHT + SaveUtils.getWeight(context))
                            .setSecondPicker(0, 9, SaveUtils.getWeightDec(context))
                            .setDimensionLabel(context.getString(R.string.kgram))
                            .setMessage(context.getString(R.string.body_weight))

                            .setPositiveButton(R.id.dialogButtonOk, new CustomAlertDialogBuilder.DialogValueListener() {

                                @Override
                                public void onNewDialogValue(Map<String, String> value) {
                                    updateWeight(tvi2.getText().toString(),
                                            (Integer.parseInt(value.get(CustomAlertDialogBuilder.FIRST_VALUE)) - Info.MIN_WEIGHT),
                                            Integer.parseInt(value.get(CustomAlertDialogBuilder.SECOND_VALUE)));
                                    notifyDataSetChanged();
                                }


                            }, new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                }
                            });
                    bld.show();

					


								
			/*		Button nobutton = (Button) dialog
                            .findViewById(R.id.buttonNo);
					nobutton.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.cancel();
						}
					});
					dialog.show();*/

                }
            });
        }

    }

    private void updateWeight(String date, int value, int valueDec) {
        String lastDate = TodayDishHelper.getLastDate(ctx);

        if (date.equals(lastDate)) {
            SaveUtils.saveWeight(value, ctx);
            SaveUtils.saveWeightDec(valueDec, ctx);

            if (SaveUtils.getUserUnicId(ctx) != 0) {
                new SocialUpdater(ctx).execute();
            }
        }
        TodayDishHelper.updateBobyParams(
                ctx,
                date,
                String.valueOf(((float) value + Info.MIN_WEIGHT) + (float) valueDec / 10),
                new BodyParams(String.valueOf((float) SaveUtils.getChest(ctx) + VolumeInfo.MIN_CHEST + (float) SaveUtils.getChestDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getBiceps(ctx) + VolumeInfo.MIN_BICEPS + (float) SaveUtils.getBicepsDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getPelvis(ctx) + VolumeInfo.MIN_PELVIS + (float) SaveUtils.getPelvisDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getNeck(ctx) + VolumeInfo.MIN_NECK + (float) SaveUtils.getNeckDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getWaist(ctx) + VolumeInfo.MIN_WAIST + (float) SaveUtils.getWaistDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getForearm(ctx) + VolumeInfo.MIN_FOREARM + (float) SaveUtils.getForearmDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getHip(ctx) + VolumeInfo.MIN_HIP + (float) SaveUtils.getHipDec(ctx) / 10),
                        String.valueOf((float) SaveUtils.getShin(ctx) + VolumeInfo.MIN_SHIN + (float) SaveUtils.getShinDec(ctx) / 10)));
        page.resume();

    }

    public int checkLimit(int sum, float bodyweght) {
        int mode = SaveUtils.getMode(ctx);
        int customLimit = SaveUtils.getCustomLimit(ctx);
        if (customLimit > 0) {
        } else {
            return checkLimitAuto(sum, bodyweght);
        }

        switch (mode) {
            case 0:
                if (sum > customLimit) {
                    return 1;
                } else {
                    return 0;
                }
            case 1:
                if (sum > customLimit) {
                    return 1;
                } else {
                    return 0;
                }
            case 2:
                if (sum > customLimit) {
                    return 0;
                } else {
                    return 1;
                }
            default:
                return 0;
        }

    }

    public int checkLimitAuto(int sum, float bodyweight) {
        int BMR = getBMR(bodyweight);
        int META = getMeta(bodyweight);
        int mode = SaveUtils.getMode(ctx);
        try {
            switch (mode) {
                case 0:
                    if (sum > BMR) {
                        return 1;
                    } else {
                        return 0;
                    }
                case 1:
                    if (sum > META) {
                        return 1;
                    } else {
                        return 0;
                    }
                case 2:
                    if (sum < BMR) {
                        return 2;
                    } else {
                        return 0;
                    }
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getMeta(float bobyweight) {

        int sexValue = 0;
        if (sex == 0) {
            sexValue = 166;
        }
        int BMR = (int) (10 * (bobyweight) +
                6.25 * (height) -
                5 * (age) - 161 +
                sexValue);
        BMR = (int) BMR;
        int META = 0;
        if (activity == 1) {
            META = (int) (BMR * 1.2);

        } else if (activity == 2) {
            META = (int) (BMR * 1.35);
        } else if (activity == 3) {
            META = (int) (BMR * 1.55);
        } else if (activity == 4) {
            META = (int) (BMR * 1.75);
        } else if (activity == 5) {
            META = (int) (BMR * 1.92);
        }
        return META;
    }

    private int getBMR(float bobyweight) {

        return (int) (getMeta(bobyweight) * 0.8);


    }

    public int checkLimit(int sum) {
        int mode = SaveUtils.getMode(ctx);
        try {
            switch (mode) {
                case 0:
                    if (sum > Integer.parseInt(SaveUtils.getBMR(ctx))) {
                        return 1;
                    } else {
                        return 0;
                    }
                case 1:
                    if (sum > Integer.parseInt(SaveUtils.getMETA(ctx))) {
                        return 1;
                    } else {
                        return 0;
                    }
                case 2:
                    if (sum < Integer.parseInt(SaveUtils.getMETA(ctx))) {
                        return 2;
                    } else {
                        return 0;
                    }
                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
