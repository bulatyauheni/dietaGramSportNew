package bulat.diet.helper_sport.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.controls.SegmentedGroup;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.GATraker;
import bulat.diet.helper_sport.utils.SaveUtils;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

public class StatisticFCPActivity extends Activity implements
		OnChartValueSelectedListener, OnCheckedChangeListener {

	protected static final String Fat = "f";
	protected static final String Carbon = "c";
	protected static final String Protein = "p";
	private static final String FCP_MODE_TODAY = "FCP_MODE_TODAY";
	private static final String FCP_MODE_YESTERDAY = "FCP_MODE_YESTERDAY";
	private static final String FCP_MODE_LASTMONTH = "FCP_MODE_LASTMONTH";
	private static final String FCP_MODE_LASTWEEK = "FCP_MODE_LASTWEEK";
	private static final String FCP_MODE_CHANGE = "FCP_MODE_CHANGE";
	private static final String FCP_MODE = "FCP_MODE";


	protected int lifestyle = 0;
	protected float valuesNormal[] = { 1, 4, 1 };
	protected float valuesFith[] = { 1, 5, 1 };
	protected float valuesBrain[] = { 1, 3, (float) 0.8 };
	protected float valuesCustom[] = { 0, 0, 0 };
	protected float values2[] = { 0, 0, 0 };
	protected float values[] = { 0, 0, 0 };
	protected PieChart mChartIdeal;
	protected PieChart mChartClient;
	protected TextView tvX, tvY;
	protected boolean isCustomMode = false;
	protected String[] mParties;
	protected Spinner spinnerDiet;
	protected SegmentedGroup timePeriodRG;
	protected LinearLayout chartsLayout;
	protected TextView successInPercentageTV;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mParties = new String[] { getString(R.string.protein),
				getString(R.string.carbon), getString(R.string.fat) };
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		try {
			activity = this.getParent().getParent();
		} catch (Exception ex) {
			activity = this;
		}
		View viewToLoad = LayoutInflater.from(activity)
				.inflate(R.layout.activity_piechart, null);
		setContentView(viewToLoad);

		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
				
		initDietTypeSpinner();
		chartsLayout = (LinearLayout) 	findViewById (R.id.chartsLayout);
		
		Button vkButton = (Button) findViewById(R.id.buttonVKChart);
		vkButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), VkActivity.class);				
				i.putExtra(VkActivity.IMAGE_PATH, getBitmapFromView(chartsLayout));
				i.putExtra(VkActivity.IMAGE_DESK, successInPercentageTV.getText().toString());
				startActivity(i);
			}
		});

		Button fbButton = (Button) findViewById(R.id.buttonFBChart);
		fbButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SharingActivity.class);
				i.putExtra(VkActivity.IMAGE_PATH, getBitmapFromView(chartsLayout));
				i.putExtra(VkActivity.IMAGE_DESK, successInPercentageTV.getText().toString());
				startActivity(i);
			}
		});
	
		mChartIdeal = (PieChart) findViewById(R.id.chart1);
		initChart(mChartIdeal);
		mChartIdeal.setCenterText(getString(R.string.idealCheet));
		
		mChartClient = (PieChart) findViewById(R.id.chart2);
		initChart(mChartClient);
		mChartClient.setCenterText(getString(R.string.yourCheet));
		
		initTimeIntervalSelector();

		successInPercentageTV = (TextView) findViewById(R.id.successInPercentageTV);
		GATraker.sendScreen(this);
	}
	
	private float calculateSuccess() {
		float sum1 = values[0] + values[1] + values[2];
		float sum2 = values2[0] + values2[1] + values2[2];
	
		
		float res = 100* (1 - (Math.abs(values[0]/sum1 - values2[0]/sum2) + Math.abs(values[1]/sum1  - values2[1]/sum2) + Math.abs(values[2]/sum1  - values2[2]/sum2)));
		return res;
	}

	public String getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) 
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else 
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        
        String uriStr = Images.Media.insertImage(this.getContentResolver(), returnedBitmap, "title", null); 
        Uri uri = Uri.parse(uriStr);
        return getRealPathFromURI(this, uri);
   
    }
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}
	protected void initDietTypeSpinner() {
		ArrayList<DishType> time = new ArrayList<DishType>();
		time.add(new DishType(0, getString(R.string.balance_norm)));
		time.add(new DishType(1, getString(R.string.balance_fith)));
		time.add(new DishType(2, getString(R.string.balance_clever)));
		int limitKkal = SaveUtils.readInt(SaveUtils.LIMIT, 0, this);
		if (limitKkal > 0) {
			isCustomMode = true;

			int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, 0,
					this);
			int limitCarbon = SaveUtils
					.readInt(SaveUtils.LIMIT_CARBON, 0, this);
			int limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, 0, this);

			int sum = limitProtein + limitCarbon + limitFat;
			valuesCustom[0] = (float) limitProtein / sum;
			valuesCustom[1] = (float) limitCarbon / sum;
			valuesCustom[2] = (float) limitFat / sum;
			time.add(new DishType(3, getString(R.string.balance_custom)));
		}
				
		ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
				android.R.layout.simple_spinner_item, time);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDiet = (Spinner) findViewById(R.id.diet_type_spinner);
		spinnerDiet.setAdapter(adapter);
		if (isCustomMode) {
			lifestyle = 3;
		} else {
			lifestyle = SaveUtils.getLifeStyle(this);
		}
		spinnerDiet.setSelection(lifestyle);
		spinnerDiet.setOnItemSelectedListener(spinnerListener);
	}
	
	protected void initTimeIntervalSelector() {
		timePeriodRG = (SegmentedGroup) findViewById(R.id.segmented2);		
		timePeriodRG.setOnCheckedChangeListener(this);
		timePeriodRG.check(timePeriodRG.getChildAt(2).getId());
	}
	protected void initChart(PieChart mChart) {
		// TODO Auto-generated method stub
		mChart.setUsePercentValues(true);
		mChart.setDescription("");
		mChart.setExtraOffsets(5, 10, 5, 5);

		mChart.setDragDecelerationFrictionCoef(0.95f);
		mChart.setCenterText(getCenterSpannableText());

		mChart.setExtraOffsets(10.f, 0.f, 10.f, 0.f);

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColor(Color.WHITE);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);

		mChart.setHoleRadius(58f);
		mChart.setTransparentCircleRadius(61f);

		mChart.setDrawCenterText(true);

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);
		mChart.setHighlightPerTapEnabled(true);


		mChart.setOnChartValueSelectedListener(this);
		mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);
		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setEnabled(false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		lifestyle = SaveUtils.getLifeStyle(this);

		switch (lifestyle) {
		case 0:
			values = valuesNormal;
			break;
		case 1:
			values = valuesFith;
			break;
		case 2:
			values = valuesBrain;
			break;
		case 3:
			values = valuesCustom;
			break;
		default:
			values = valuesNormal;
			break;
		}
		setIdealPersents(values);

		values2 = getValues(TodayDishHelper.getStatisticFCP(this, 30));
		setClientsPersents(values2);
		successInPercentageTV.setText(String.format(getString(R.string.success_in_percantage_text), calculateSuccess()) );
		
	}

	private void setClientsPersents(float[] values) {
		setData(mChartClient, 3, 100, values);
	}

	private void setIdealPersents(float[] values) {
		setData(mChartIdeal, 3, 100, values);
	}

	private void setData(PieChart mChart, int count, float range, float[] data) {

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		for (int i = 0; i < count; i++) {
			yVals1.add(new Entry(data[i], i));
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count + 1; i++)
			xVals.add(mParties[i % mParties.length]);

		PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
		dataSet.setSliceSpace(3f);
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);

		/*for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.COLORFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.LIBERTY_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.PASTEL_COLORS)
			colors.add(c);*/

		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		// dataSet.setSelectionShift(0f);

		// dataSet.setValueLinePart1OffsetPercentage(80.f);
		// dataSet.setValueLinePart1Length(0.2f);
		// dataSet.setValueLinePart2Length(0.4f);
		// dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
		// dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

		PieData pieData = new PieData(xVals, dataSet);
		pieData.setValueFormatter(new PercentFormatter());
		pieData.setValueTextSize(11f);
		pieData.setValueTextColor(Color.BLACK);
		mChart.setData(pieData);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

	public static float[] getValues(Map<String, Float> statistic) {
		float res[] = { 0, 0, 0 };
		if (statistic == null) {
			return res;
		}
		try {
			if (statistic.get(Fat) != null) {
				res[0] = statistic.get(Protein);
			}
			if (statistic.get(Carbon) != null) {
				res[1] = statistic.get(Carbon);
			}
			if (statistic.get(Protein) != null) {
				res[2] = statistic.get(Fat);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private SpannableString getCenterSpannableText() {

		SpannableString s = new SpannableString("BZU ideal");
		/*
		 * s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0); s.setSpan(new
		 * StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0); s.setSpan(new
		 * ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
		 * s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
		 * s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14,
		 * s.length(), 0); s.setSpan(new
		 * ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14,
		 * s.length(), 0);
		 */
		return s;
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
						+ ", DataSet index: " + dataSetIndex);
	}

	@Override
	public void onNothingSelected() {
		Log.i("PieChart", "nothing selected");
	}

	private OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			SaveUtils.saveLifeStyle((int) (((DishType) spinnerDiet
					.getSelectedItem()).getTypeKey()),
					StatisticFCPActivity.this);
			onResume();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.today:
			values2 = getValues(TodayDishHelper.getStatisticFCP(this, 1));
			GATraker.sendEvent(FCP_MODE, FCP_MODE_CHANGE, FCP_MODE_TODAY, 1L);
			break;
		case R.id.yesterday:
			values2 = getValues(TodayDishHelper.getStatisticFCP(this, 1, 2));
			GATraker.sendEvent(FCP_MODE, FCP_MODE_CHANGE, FCP_MODE_YESTERDAY, 1L);
			break;
		case R.id.lastweek:
			values2 = getValues(TodayDishHelper.getStatisticFCP(this, 7));
			GATraker.sendEvent(FCP_MODE, FCP_MODE_CHANGE, FCP_MODE_LASTWEEK, 1L);
			break;
		case R.id.lastmonth:
			values2 = getValues(TodayDishHelper.getStatisticFCP(this, 30));
			GATraker.sendEvent(FCP_MODE, FCP_MODE_CHANGE, FCP_MODE_LASTMONTH, 1L);
			break;
		default:
			// Nothing to do
		}
		setClientsPersents(values2);
	}

	/*
	 * private static final String Fat = "f"; private static final String Carbon
	 * = "c"; private static final String Protein = "p"; private static final
	 * String PERCENT = "%"; //белков, жиров и углеводов
	 * должно составлять 1:1:4, //при больших
	 * физических нагрузках - 1:1:5, //для
	 * работников умственного труда - 1:0.8:3. float
	 * valuesNormal[]={1,4,1}; float valuesFith[]={1,5,1}; float
	 * valuesBrain[]={1,3,(float) 0.8}; float valuesCustom[]={0,0,0}; float
	 * values2[]={0,0,0};
	 * 
	 * private Spinner spinnerDiet; private int lifestyle=0; private float[]
	 * values; private TextView proteinIdeal; private TextView fatIdeal; private
	 * TextView carbonIdeal; private TextView proteinUser; private TextView
	 * fatUser; private TextView carbonUser; private boolean isCustomMode =
	 * false;
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); View viewToLoad =
	 * LayoutInflater.from(SettingsActivity.ctx).inflate(
	 * R.layout.statistics_fcp, null); setContentView(viewToLoad); proteinIdeal
	 * = (TextView) viewToLoad.findViewById(R.id.twproteinIdeal); fatIdeal =
	 * (TextView) viewToLoad.findViewById(R.id.twfatIdeal); carbonIdeal =
	 * (TextView) viewToLoad.findViewById(R.id.twcarbonIdeal); proteinUser =
	 * (TextView) viewToLoad.findViewById(R.id.twproteinUser); carbonUser =
	 * (TextView) viewToLoad.findViewById(R.id.twcarbonUser); fatUser =
	 * (TextView) viewToLoad.findViewById(R.id.twfatUser);
	 * 
	 * spinnerDiet = (Spinner) viewToLoad.findViewById(R.id.diet_type_spinner);
	 * ArrayList<DishType> time = new ArrayList<DishType>(); time.add(new
	 * DishType( 0, getString(R.string.balance_norm))); time.add(new DishType(
	 * 1, getString(R.string.balance_fith))); time.add(new DishType( 2,
	 * getString(R.string.balance_clever))); int limitKkal =
	 * SaveUtils.readInt(SaveUtils.LIMIT, 0, this); if(limitKkal>0){
	 * isCustomMode = true;
	 * 
	 * int limitProtein = SaveUtils.readInt(SaveUtils.LIMIT_PROTEIN, 0, this);
	 * int limitCarbon = SaveUtils.readInt(SaveUtils.LIMIT_CARBON, 0, this); int
	 * limitFat = SaveUtils.readInt(SaveUtils.LIMIT_FAT, 0, this);
	 * 
	 * int sum = limitProtein + limitCarbon + limitFat; valuesCustom[0] =
	 * (float)limitProtein/sum; valuesCustom[1] = (float)limitCarbon/sum;
	 * valuesCustom[2] = (float)limitFat/sum; time.add(new DishType( 3,
	 * getString(R.string.balance_custom))); } ArrayAdapter<DishType> adapter =
	 * new ArrayAdapter<DishType>(SettingsActivity.ctx,
	 * android.R.layout.simple_spinner_item, time);
	 * 
	 * 
	 * 
	 * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
	 * ); spinnerDiet.setAdapter(adapter); if (isCustomMode) { lifestyle = 3; }
	 * else { lifestyle = SaveUtils.getLifeStyle(this); }
	 * spinnerDiet.setSelection(lifestyle);
	 * spinnerDiet.setOnItemSelectedListener(spinnerListener); Button backButton
	 * = (Button) viewToLoad.findViewById(R.id.buttonBack);
	 * backButton.setOnClickListener(new OnClickListener() { public void
	 * onClick(View v) { onBackPressed(); } }); }
	 * 
	 * private float[] getValues(Map<String, Float> statistic) { float
	 * res[]={0,0,0}; if(statistic == null){ return res; } try{
	 * if(statistic.get(Fat) != null){ res[0]=statistic.get(Protein); }
	 * if(statistic.get(Carbon) != null){ res[1]=statistic.get(Carbon); }
	 * if(statistic.get(Protein) != null){ res[2]=statistic.get(Fat); } }catch
	 * (Exception e) { e.printStackTrace(); } return res; }
	 * 
	 * @Override protected void onDestroy() { // TODO Auto-generated method stub
	 * super.onDestroy(); }
	 * 
	 * @Override protected void onPause() { // TODO Auto-generated method stub
	 * super.onPause(); }
	 * 
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); lifestyle = SaveUtils.getLifeStyle(this); LinearLayout
	 * linear=(LinearLayout) findViewById(R.id.linearLayoutChart); switch
	 * (lifestyle) { case 0: values=calculateData(valuesNormal); break; case 1:
	 * values=calculateData(valuesFith); break; case 2:
	 * values=calculateData(valuesBrain); break; case 3:
	 * values=calculateData(valuesCustom); break; default:
	 * values=calculateData(valuesNormal); break; } setIdealPersents(values);
	 * linear.removeAllViews(); linear.addView(new MyGraphview(this,values));
	 * 
	 * LinearLayout linear2=(LinearLayout)
	 * findViewById(R.id.linearLayoutChart2); values2 =
	 * getValues(TodayDishHelper.getStatisticFCP(this));
	 * values2=calculateData(values2); setPersents(values2);
	 * linear2.removeAllViews(); linear2.addView(new MyGraphview(this,values2));
	 * }
	 * 
	 * private void setPersents(float[] values) {
	 * proteinUser.setText(String.format("%.1f", values[0]*100/360) + PERCENT);
	 * carbonUser.setText(String.format("%.1f", values[1]*100/360) + PERCENT);
	 * fatUser.setText(String.format("%.1f", values[2]*100/360) + PERCENT); }
	 * 
	 * private void setIdealPersents(float[] values) {
	 * proteinIdeal.setText(String.format("%.1f", values[0]*100/360) + PERCENT);
	 * carbonIdeal.setText(String.format("%.1f", values[1]*100/360) + PERCENT);
	 * fatIdeal.setText(String.format("%.1f", values[2]*100/360) + PERCENT); }
	 * 
	 * private float[] calculateData(float[] data) { // TODO Auto-generated
	 * method stub float total=0; for(int i=0;i<data.length;i++) {
	 * total+=data[i]; } for(int i=0;i<data.length;i++) {
	 * data[i]=360*(data[i]/total); } return data;
	 * 
	 * } public class MyGraphview extends View { private Paint paint=new
	 * Paint(Paint.ANTI_ALIAS_FLAG); private float[] value_degree; private int[]
	 * COLORS={Color.GREEN,Color.BLUE,Color.RED}; RectF rectf = new RectF
	 * (getDIP(5), getDIP(5), getDIP(155), getDIP(155)); int temp=-90;
	 * 
	 * public MyGraphview(Context context, float[] values) {
	 * 
	 * super(context); value_degree=new float[values.length]; for(int
	 * i=0;i<values.length;i++) { value_degree[i]=values[i]; } }
	 * 
	 * private float getDIP(int i) { return
	 * getResources().getDisplayMetrics().density * i; }
	 * 
	 * @Override protected void onDraw(Canvas canvas) { // TODO Auto-generated
	 * method stub super.onDraw(canvas); temp = -90; for (int i = 0; i <
	 * value_degree.length; i++) {//values2.length; i++) { if (i == 0) {
	 * paint.setColor(COLORS[i]); canvas.drawArc(rectf, -90, value_degree[i],
	 * true, paint); } else { temp += (int) value_degree[i - 1];
	 * paint.setColor(COLORS[i]); canvas.drawArc(rectf, temp, value_degree[i],
	 * true, paint); } } }
	 * 
	 * }
	 * 
	 * public class LinevChariew extends View { private Paint paint=new
	 * Paint(Paint.ANTI_ALIAS_FLAG); // private float[] value_degree; // private
	 * int[]
	 * COLORS={Color.CYAN,Color.LTGRAY,Color.YELLOW,Color.RED,Color.GREEN}; //
	 * RectF rectf = new RectF (getDIP(5), getDIP(5), getDIP(155), getDIP(155));
	 * // int temp=-90;
	 * 
	 * public LinevChariew(Context context, float[] values) {
	 * 
	 * super(context); // value_degree=new float[values.length]; for(int
	 * i=0;i<values.length;i++) { // value_degree[i]=values[i]; } }
	 * 
	 * private float getDIP(int i) { return
	 * getResources().getDisplayMetrics().density * i; }
	 * 
	 * @Override protected void onDraw(Canvas canvas) { // TODO Auto-generated
	 * method stub super.onDraw(canvas);
	 * 
	 * ArrayList<Integer> myArrayListOfValues = new ArrayList<Integer>();
	 * myArrayListOfValues.add(120 - 19); myArrayListOfValues.add(120 - 29);
	 * myArrayListOfValues.add(120 - 19); myArrayListOfValues.add(120 - 39);
	 * myArrayListOfValues.add(120 - 92); int step = 0; //
	 * canvas.translate(0,canvas.getHeight()); // reset where 0,0 is located
	 * 
	 * 
	 * for(int i=1; i<myArrayListOfValues.size(); i++){ step = step + 10; Paint
	 * myPaint = new Paint(Paint.ANTI_ALIAS_FLAG); myPaint.setStrokeWidth(81
	 * /getResources().getDisplayMetrics().density);
	 * myPaint.setColor(0xffff0000); //color.RED
	 * 
	 * canvas.drawLine(step, myArrayListOfValues.get(i - 1), step + 10,
	 * myArrayListOfValues.get(i), myPaint);
	 * 
	 * } canvas.save(); canvas.translate(0,canvas.getHeight()); // reset where
	 * 0,0 is located canvas.scale(1,-1); canvas.restore(); }
	 * 
	 * }
	 * 
	 * private OnItemSelectedListener spinnerListener = new
	 * OnItemSelectedListener(){
	 * 
	 * public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long
	 * arg3) {
	 * 
	 * SaveUtils.saveLifeStyle((int)(
	 * ((DishType)spinnerDiet.getSelectedItem()).getTypeKey()),
	 * StatisticFCPActivity.this); onResume(); }
	 * 
	 * public void onNothingSelected(AdapterView<?> arg0) { // TODO
	 * Auto-generated method stub
	 * 
	 * } };
	 */
}
