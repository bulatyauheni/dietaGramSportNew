package bulat.diet.helper_sport.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.adapter.TemplatesAdapter;
import bulat.diet.helper_sport.db.TemplateDishHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;

import android.widget.Toast;

import au.com.bytecode.opencsv.CSVWriter;


public class TemplateActivity extends BaseActivity {
    protected static final int DIALOG_CHART = 0;
    protected static final int DIALOG_WEIGHT = 1;
    protected static final int DIALOG_EMAIL = 2;
    TextView header;
    ListView daysList;
    Cursor c;
    int sum;
    private TextView avgFatView;
    private TextView avgProteinView;
    private TextView avgCarbonView;
    private ProgressBar progresBar;
    private EditText userNameET;
    private Button nobutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.template_list, null);
        header = (TextView) viewToLoad.findViewById(R.id.textViewTitle);
        progresBar = (ProgressBar) viewToLoad.findViewById(R.id.progress_bar);
        Button calendarTab = (Button) viewToLoad.findViewById(R.id.daysTab);
        calendarTab.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    //Intent intent = new Intent();
                    //intent.setClass(getParent(), CalendarActivity.class);
                    CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
                    //activityStack.push("CalendarActivity", intent);
                    activityStack.pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button recepyBtn = (Button) viewToLoad.findViewById(R.id.buttonRecepy);
        recepyBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    showDialog(DIALOG_EMAIL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Button addButton = (Button) viewToLoad.findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(addBtnListener);
        setContentView(viewToLoad);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (c != null)
            c.close();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final Dialog dialog;
        Button buttonOk;
        switch (id) {
            case DIALOG_EMAIL:
                // graph with custom labels and drawBackground
                dialog = new Dialog(this.getParent());

                dialog.setContentView(R.layout.user_email_dialog);
                dialog.setTitle(R.string.user_email_title);
                TextView idView2 = (TextView) dialog.findViewById(R.id.textViewId);

                idView2.setText("id" + SaveUtils.getUserAdvId(TemplateActivity.this));


                userNameET = (EditText) dialog.findViewById(R.id.editTextUserEmail);
                buttonOk = (Button) dialog.findViewById(R.id.buttonYes);
                buttonOk.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {

                        if (userNameET.getText().toString().length() < 1) {
                            userNameET.setBackgroundColor(Color.RED);
                        } else {
                            dialog.cancel();
                            try {
                                new RecepyUpdateTask(userNameET.getText().toString().trim()).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            onResume();
                        }
                    }

                });
                nobutton = (Button) dialog.findViewById(R.id.buttonNo);
                nobutton.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                break;
            default:
                dialog = null;
        }

        return dialog;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (c != null)
            c.close();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // TODO Auto-generated method stub
        avgCalorisityView = (TextView) findViewById(R.id.textViewAVGValue);
        avgFatView = (TextView) findViewById(R.id.textViewFat);
        avgProteinView = (TextView) findViewById(R.id.textViewProtein);
        avgCarbonView = (TextView) findViewById(R.id.textViewCarbon);
        String[] avgVals = TodayDishHelper.getAvgDishCalorisity(this);


        avgCalorisityView.setText(avgVals[0] + " " + getString(R.string.kcal));

        avgFatView.setText(avgVals[1]);
        avgCarbonView.setText(avgVals[2]);
        avgProteinView.setText(avgVals[3]);


        checkLimit(Integer.valueOf(avgVals[0]));
        header.setText(getString(R.string.tab_template_header));
        c = TemplateDishHelper.getDays(getApplicationContext());
        if (c != null) {
            if (c.getCount() == 0) {
                LinearLayout emptyHeader = (LinearLayout) findViewById(R.id.linearLayoutEmptyListHeader);
                emptyHeader.setVisibility(View.VISIBLE);
                emptyHeader.setOnClickListener(addBtnListener);
            }
            try {
                TemplatesAdapter da = new TemplatesAdapter(getApplicationContext(), c, (CalendarActivityGroup) getParent());
                //TodayDishAdapter  da = new TodayDishAdapter(getApplicationContext(), c,(DishActivityGroup) getParent());
                daysList = (ListView) findViewById(R.id.listViewDays);
                daysList.setAdapter(da);
                daysList.setItemsCanFocus(true);
                daysList.setOnItemClickListener(daysListOnItemClickListener);
            } catch (Exception e) {
                if (c != null)
                    c.close();
            } finally {
                //c.close();
            }
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (c != null)
            c.close();
    }

    private OnItemClickListener daysListOnItemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                long arg3) {
            //switchTabInActivity(1)
            Intent intent = new Intent();
            TextView day = (TextView) v.findViewById(R.id.textViewDay);

            intent.putExtra(AddTodayDishActivity.TITLE, getString(R.string.edit_today_dish));
            intent.putExtra(DishActivity.DATE, day.getText());
            intent.putExtra(DishActivity.BACKBTN, true);
            intent.putExtra(DishActivity.PARENT_NAME, CalendarActivityGroup.class.toString());
            intent.setClass(getParent(), NewTemplateActivity.class);
            CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
            activityStack.push("NewTemplateActivity", intent);
        }
    };
    private TextView avgCalorisityView;

    public void switchTabInActivity(int indexTabToSwitchTo) {
        DietHelperActivity parentActivity;
        parentActivity = (DietHelperActivity) this.getParent();
        parentActivity.switchTab(indexTabToSwitchTo);
    }

    public void checkLimit(int sum) {
        int mode = SaveUtils.getMode(this);
        int customLimit = SaveUtils.getCustomLimit(this);
        if (customLimit > 0) {
            SaveUtils.saveBMR(String.valueOf(customLimit), this);
            SaveUtils.saveMETA(String.valueOf(customLimit), this);
        }
        try {
            switch (mode) {
                case 0:
                    if (sum > Integer.parseInt(SaveUtils.getBMR(this))) {
                        LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
                        totalLayout.setBackgroundResource(R.color.light_red);
                    } else {
                        LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
                        totalLayout.setBackgroundResource(R.color.light_green);
                    }

                    break;
                case 1:
                    if (sum > Integer.parseInt(SaveUtils.getMETA(this))) {
                        LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
                        totalLayout.setBackgroundResource(R.color.light_red);
                    } else {
                        LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
                        totalLayout.setBackgroundResource(R.color.light_green);
                    }
                    break;
                case 2:
                    if (sum < Integer.parseInt(SaveUtils.getMETA(this))) {
                        LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
                        totalLayout.setBackgroundResource(R.color.light_red);

                    } else {
                        LinearLayout totalLayout = (LinearLayout) findViewById(R.id.linearLayoutAVG);
                        totalLayout.setBackgroundResource(R.color.light_green);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener showChartListener = new OnClickListener() {

        public void onClick(View v) {
            showDialog(DIALOG_CHART);

        }
    };
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        TodayDishHelper.clearAll(TemplateActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    OnClickListener addBtnListener = new OnClickListener() {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.putExtra(AddTodayDishActivity.TITLE, getString(R.string.edit_today_dish));
            intent.putExtra(DishActivity.BACKBTN, true);
            intent.putExtra(DishActivity.PARENT_NAME, CalendarActivityGroup.class.toString());
            intent.putExtra(DishActivity.DATE, getString(R.string.new_template_header) + SaveUtils.getNextInt(TemplateActivity.this));
            intent.setClass(getParent(), NewTemplateActivity.class);
            CalendarActivityGroup activityStack = (CalendarActivityGroup) getParent();
            activityStack.push("NewTemplateActivity", intent);
        }
    };

    private class RecepyUpdateTask extends AsyncTask<Void, Void, Void> {
        Boolean emptyFlag = false;
        private Context context = TemplateActivity.this;
        String email = "";

        public RecepyUpdateTask(String email) {
            this.email = email;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = null;

            String result = "";
            StringBuilder builder = new StringBuilder();
            try {


                JSONObject Parent = new JSONObject();
                JSONArray array = new JSONArray();
                JSONObject version = new JSONObject();
                version.put("lastid", SaveUtils.getLastRationId(context));

                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://old.dietagram.ru/recepy.php?" + "lastid=" + SaveUtils.getLastRationId(context) + "&" + "email=" + email);
                StringEntity se = new StringEntity(Parent.toString());  //new ByteArrayEntity(json.toString().getBytes(            "UTF8"))
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
                String resultString = builder.toString().trim();
                try {
                    JSONObject jsonRoot = new JSONObject(resultString);
                    JSONArray jsonArray = new JSONObject(jsonRoot.getString("update")).getJSONArray("dishes");

                    ArrayList<TodayDish> newDishes = new ArrayList<TodayDish>();
                    if (jsonArray.length() < 1) {
                        emptyFlag = true;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TodayDish dish = new TodayDish();

                        JSONObject jsonObj = new JSONObject(); //jsonObject.getString("name");
                        //dish.setServerId(jsonObject.getInt("id"));
                        dish.setBodyweight(Float.parseFloat(jsonObject.getString("bodyweight")));
                        dish.setName(jsonObject.getString("name"));
                        dish.setDescription(jsonObject.getString("daytime"));
                        dish.setCaloricity(jsonObject.getInt("calorisity"));
                        dish.setAbsolutCaloricity(jsonObject.getInt("calorie"));
                        dish.setCategory(jsonObject.getString("category"));
                        dish.setWeight(jsonObject.getInt("dishweight"));
                        dish.setDate(jsonObject.getString("datetext"));
                        dish.setDateTime(jsonObject.getLong("datelong"));
                        dish.setFat(Float.parseFloat(jsonObject.getString("fat")));
                        dish.setCarbon(Float.parseFloat(jsonObject.getString("carbon")));
                        dish.setProtein(Float.parseFloat(jsonObject.getString("protein")));
                        dish.setAbsFat(Float.parseFloat(jsonObject.getString("fatabs")));
                        dish.setAbsCarbon(Float.parseFloat(jsonObject.getString("carbonabs")));
                        dish.setAbsProtein(Float.parseFloat(jsonObject.getString("proteinabs")));
                        dish.setType(jsonObject.getString("type"));
                        dish.setDateTimeHH(jsonObject.getInt("hh"));
                        dish.setDateTimeMM(jsonObject.getInt("mm"));
                        SaveUtils.setLastRationId(TemplateActivity.this, jsonObject.getInt("id"));
                        newDishes.add(dish);
                    }
                    for (TodayDish todayDish : newDishes) {
                        TemplateDishHelper.addNewDish(todayDish, context);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    emptyFlag = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (emptyFlag) {
                Toast.makeText(TemplateActivity.this, getString(R.string.user_ration_empty), Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(TemplateActivity.this, getString(R.string.user_ration_ok), Toast.LENGTH_LONG)
                        .show();
            }
            progresBar.setVisibility(View.GONE);
        }

    }

    class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean>

    {

        private final ProgressDialog dialog = new ProgressDialog(
                TemplateActivity.this.getParent());

        // can use UI thread here

        @Override
        protected void onPreExecute()

        {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)

        protected Boolean doInBackground(final String... args)

        {
            File exportDir = new File(
                    Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM", new Locale(
                    SaveUtils.getLang(TemplateActivity.this)));
            String date = sdf.format(new Date());
            String filename = "calorie_" + SaveUtils.getUserAdvId(TemplateActivity.this) + ".csv";
            File file = new File(exportDir, filename);
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = TodayDishHelper.getAllDishes(
                        TemplateActivity.this,
                        String.valueOf((new Date()).getTime() - 30
                                * DateUtils.DAY_IN_MILLIS));
                csvWrite.writeNext(curCSV.getColumnNames());
                while (curCSV.moveToNext()) {
                    String arrStr[] = {curCSV.getString(0),
                            curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4),
                            curCSV.getString(5), curCSV.getString(6),
                            curCSV.getString(7), curCSV.getString(8),
                            curCSV.getString(9), curCSV.getString(10),
                            curCSV.getString(11), curCSV.getString(12),
                            curCSV.getString(13), curCSV.getString(14),
                            curCSV.getString(15), curCSV.getString(16),
                            curCSV.getString(17), curCSV.getString(18),
                            curCSV.getString(19),};
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
                SaveUtils.setLastExportedFileName(filename,
                        TemplateActivity.this);
                return true;

            } catch (IOException e) {
                return false;
            }
        }

        // can use UI thread here

        @Override
        protected void onPostExecute(final Boolean success)

        {

            if (this.dialog.isShowing())

            {

                this.dialog.dismiss();

            }

            if (success)

            {

                Toast.makeText(TemplateActivity.this, "Export successful!", Toast.LENGTH_SHORT)
                        .show();

                SaveUtils.getLastExportedFileName(TemplateActivity.this);
            } else

            {

                Toast.makeText(TemplateActivity.this, "Export failed", Toast.LENGTH_SHORT).show();

            }

        }

    }
}
