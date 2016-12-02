package bulat.diet.helper_sport.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.controls.SegmentedGroup;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.Day;
import bulat.diet.helper_sport.item.DishType;
import bulat.diet.helper_sport.utils.CustomAlertDialogBuilder;
import bulat.diet.helper_sport.utils.SaveUtils;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.MyMarkerView;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class WeightChartActivity extends Activity implements
		OnChartValueSelectedListener, OnCheckedChangeListener {
	protected int mCurrnetChartType = 0;
	protected String[] mDates;
	private CombinedChart mChart;
	private int itemcount = 1;
	List<Day> days;
	private EditText goalET;
	private Spinner chartTypeSpiner;
	private SegmentedGroup timePeriodRG;
	private boolean readyToRate;
	private float min;
	private float max;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		readyToRate = (!SaveUtils.isAllreadyRate(WeightChartActivity.this.getParent().getParent())) && (TodayDishHelper.getDaysStat(this).size() > 9);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View viewToLoad = LayoutInflater.from(this.getParent().getParent())
				.inflate(R.layout.activity_combined, null);
		this.setContentView(viewToLoad);
		
		
		Button vkButton = (Button) findViewById(R.id.buttonVKChart);
		vkButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {						
				Intent i = new Intent(getApplicationContext(), VkActivity.class);				
				i.putExtra(VkActivity.IMAGE_PATH, getBitmapFromView(mChart));
				i.putExtra(VkActivity.IMAGE_DESK, ((DishType)chartTypeSpiner.getSelectedItem()).getDescription());
				startActivity(i);
			}
		});

		goalET = (EditText) findViewById(R.id.editTextLimitValue);
		Button saveGoalButton = (Button) findViewById(R.id.buttonSetGoal);
		saveGoalButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				goalET.setBackgroundColor(Color.WHITE);
				if (goalET.getText().length() < 1) {
					goalET.setBackgroundColor(Color.RED);
				} else {
					try {
						SaveUtils.writeFloat(
								getIdealValueId(mCurrnetChartType),
								Float.valueOf(goalET.getText().toString()),
								WeightChartActivity.this);
						Toast.makeText(WeightChartActivity.this,
								getString(R.string.save_limit),
								Toast.LENGTH_LONG).show();
						drawChart(mCurrnetChartType);

					} catch (Exception e) {
						goalET.setBackgroundColor(Color.RED);
						e.printStackTrace();
					}
				}
			}
		});

		initChart();

		initChartTypeSpiner();

		initTimeIntervalSelector();

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
        
        String uriStr = Images.Media.insertImage(WeightChartActivity.this.getContentResolver(), returnedBitmap, "title", null); 
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

	private void initTimeIntervalSelector() {
		timePeriodRG = (SegmentedGroup) findViewById(R.id.weightChartTimeSegments);
		timePeriodRG.setOnCheckedChangeListener(this);
		timePeriodRG.check(timePeriodRG.getChildAt(0).getId());
	}

	private void initChartTypeSpiner() {
		chartTypeSpiner = (Spinner) findViewById(R.id.spinnerChartType);
		// chartTypeSpiner
		ArrayList<DishType> types = getChartTypes(this);
		ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
				android.R.layout.simple_spinner_item, types);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chartTypeSpiner.setAdapter(adapter);
		chartTypeSpiner.setSelection(0);
		chartTypeSpiner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mCurrnetChartType = ((DishType) chartTypeSpiner
						.getSelectedItem()).getTypeKey();
				// set value for ideal weight
				goalET.setText(SaveUtils.readFloat(
						getIdealValueId(mCurrnetChartType), Float.valueOf(0),
						WeightChartActivity.this).toString());
				// redraw chart
				drawChart(mCurrnetChartType);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private String getIdealValueId(int chartType) {
		switch (chartType) {
		case 1:
			return SaveUtils.USER_IDEAL_WEIGHT;
		case 2:
			return SaveUtils.USER_IDEAL_CHEST;
		case 3:
			return SaveUtils.USER_IDEAL_BICEPS;
		case 4:
			return SaveUtils.USER_IDEAL_FOREARM;
		case 5:
			return SaveUtils.USER_IDEAL_HIP;
		case 6:
			return SaveUtils.USER_IDEAL_NECK;
		case 7:
			return SaveUtils.USER_IDEAL_SHIN;
		case 8:
			return SaveUtils.USER_IDEAL_PELVIS;
		case 9:
			return SaveUtils.USER_IDEAL_WAIST;
		default:
			break;
		}
		return null;
	}

	private void initChart() {
		mChart = (CombinedChart) findViewById(R.id.chart1);
		mChart.setOnChartValueSelectedListener(this);
		try {
			mChart.setDescription(((DishType)chartTypeSpiner.getSelectedItem()).getDescription());
		} catch (Exception e) {
			mChart.setDescription ("");
		}
		mChart.setBackgroundColor(Color.WHITE);
		mChart.setDrawGridBackground(true);
		mChart.setDrawBarShadow(false);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
      //  mChart.setPinchZoom(true);
		LimitLine ll1 = new LimitLine(Float.valueOf(goalET.getText().toString()), getString(R.string.idealWeight));
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll1.setTextSize(10f);
		// draw bars behind lines
		mChart.setDrawOrder(new CombinedChart.DrawOrder[] {
				CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE,
				CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE,
				CombinedChart.DrawOrder.SCATTER });
		
		// create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);

		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.setDrawGridLines(false);
		rightAxis.setEnabled(false);
		
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.removeAllLimitLines();
		leftAxis.addLimitLine(ll1);
		leftAxis.setDrawGridLines(false);
		leftAxis.setAxisMinValue(getMin()); // this replaces setStartAtZero(true)
		leftAxis.setAxisMaxValue(getMax()); // this replaces setStartAtZero(true)

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTH_SIDED);
	}

	

	private float getMax() {
		float goal = Float.valueOf(goalET.getText().toString());
		return goal > max ? goal+5:max+5;
	}

	private float getMin() {
		float goal = Float.valueOf(goalET.getText().toString());
		return goal < min ? goal-10:min-10;
	}

	private void drawChart(int chartType) {
		CombinedData data = new CombinedData(mDates);

		//data.setData(generateLineData());
		data.setData(generateBarData(chartType));
		// data.setData(generateBubbleData());
		// data.setData(generateScatterData());
		// data.setData(generateCandleData());
		initChart();
		if (mDates == null || mDates.length == 0) {

		} else {
			mChart.setData(data);
			mChart.invalidate();
			mChart.animateX(1000);
		}
	}

	private void initDataForChart(int daysCount) {
		days = TodayDishHelper.getDaysStat(getApplicationContext(), daysCount);
		Collections.reverse(days);
		SimpleDateFormat formatter = new SimpleDateFormat("EEE dd MMMM",
				new Locale(SaveUtils.getLang(WeightChartActivity.this)));

		if (days != null) {
			itemcount = days.size();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", new Locale(
					SaveUtils.getLang(WeightChartActivity.this)));
			mDates = new String[itemcount];
			int i = 0;
			for (Day day : days) {
				String dateInString = day.getDateStr();
				try {
					Date date = formatter.parse(dateInString);
					mDates[i] = String.valueOf(sdf.format(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				i++;
			}

		} else {
			itemcount = 0;
		}
	}

	private LineData generateLineData() {

		LineData d = new LineData();

		ArrayList<Entry> entries = new ArrayList<Entry>();

		for (int index = 0; index < itemcount; index++)
			entries.add(new Entry(Float.valueOf(goalET.getText().toString()),
					index));

		LineDataSet set = new LineDataSet(entries,
				getString(R.string.currentWeight));
		set.setColor(Color.rgb(240, 238, 70));
		set.setLineWidth(2.5f);
		set.setCircleColor(Color.rgb(240, 238, 70));
		set.setCircleRadius(5f);
		set.setFillColor(Color.rgb(240, 238, 70));
		set.setDrawCubic(true);
		set.setDrawValues(true);
		set.setValueTextSize(10f);
		set.setValueTextColor(Color.rgb(240, 238, 70));

		set.setAxisDependency(YAxis.AxisDependency.LEFT);
		set.setDrawValues(false);
		d.addDataSet(set);

		return d;
	}
	private void setMinMax(float tempVal) {
		min = tempVal<min ? tempVal:min;
		max = tempVal>max ? tempVal:max;
	}
	private LineData generateBarData(int chartType) {
		LineData d = new LineData();
		
		min = 200;
		max= 0;
		ArrayList<Entry> entries = new ArrayList<Entry>();
		for (int index = 0; index < itemcount; index++)
			switch (chartType) {
			case 1:
				entries.add(new Entry(days.get(index).getBodyWeight(), index));
				setMinMax(days.get(index).getBodyWeight());
				break;
			case 2:
				entries.add(new Entry(days.get(index).getChest(), index));
				setMinMax(days.get(index).getChest());
				break;
			case 3:
				entries.add(new Entry(days.get(index).getBiceps(), index));
				setMinMax(days.get(index).getBiceps());
				break;
			case 4:
				entries.add(new Entry(days.get(index).getForearm(), index));
				setMinMax(days.get(index).getForearm());
				break;
			case 5:
				entries.add(new Entry(days.get(index).getHip(), index));
				setMinMax(days.get(index).getHip());
				break;
			case 6:
				entries.add(new Entry(days.get(index).getNeck(), index));
				setMinMax(days.get(index).getNeck());
				break;
			case 7:
				entries.add(new Entry(days.get(index).getShin(), index));
				setMinMax(days.get(index).getShin());
				break;
			case 8:
				entries.add(new Entry(days.get(index).getPelvis(), index));
				setMinMax(days.get(index).getPelvis());
				break;
			case 9:
				entries.add(new Entry(days.get(index).getWaist(), index));
				setMinMax(days.get(index).getWaist());
				break;
			default:
				break;
			}
		
		LineDataSet set = new LineDataSet(entries,
				getString(R.string.currentWeight));
		set.setColor(Color.rgb(140, 138, 70));
		set.setLineWidth(2.5f);
		set.setCircleColor(Color.rgb(240, 38, 70));
		set.setCircleRadius(5f);
		set.setFillColor(Color.rgb(40, 238, 70));
		set.setDrawCubic(false);
		set.setDrawValues(true);
		set.setValueTextSize(10f);
		set.setValueTextColor(Color.rgb(40, 40, 40));
		set.setAxisDependency(YAxis.AxisDependency.LEFT);
		set.setDrawValues(true);
		set.setDrawFilled(true);
		d.addDataSet(set);

		return d;

	}

	@Override
	protected void onDestroy() {
		if (readyToRate) {
		try {
			SaveUtils.setAllreadyRate(true, WeightChartActivity.this.getParent().getParent());
			CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(WeightChartActivity.this.getParent().getParent());
			bld.setLayout(R.layout.section_alert_dialog_two_buttons)
			.setMessage(WeightChartActivity.this.getParent().getString(R.string.do_you_like))		
			.setPositiveButton(R.id.dialogButtonOk, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(WeightChartActivity.this.getParent().getParent());
					bld.setLayout(R.layout.section_alert_dialog_two_buttons)
					.setMessage(WeightChartActivity.this.getParent().getString(R.string.rating_plaese))			
					.setPositiveButton(R.id.dialogButtonOk, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							WeightChartActivity.this.getParent().getParent().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + WeightChartActivity.this.getParent().getParent().getPackageName())));
						}
					})
					.setPositiveButtonText(R.string.agree)
					.setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					})
					.setNegativeButtonText(R.string.disagree);
					bld.show();
				}
			})
			.setPositiveButtonText(R.string.yes_shure)
			.setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {

				@Override
				public void onClick(View v) {
					CustomAlertDialogBuilder bld = new CustomAlertDialogBuilder(WeightChartActivity.this.getParent().getParent());
					bld.setLayout(R.layout.section_alert_dialog_two_buttons)
					.setMessage(WeightChartActivity.this.getParent().getString(R.string.complain_plaese))			
					.setPositiveButton(R.id.dialogButtonOk, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
						            "mailto","bulat.yauheni@gmail.com", null));
							emailIntent.putExtra(Intent.EXTRA_SUBJECT, WeightChartActivity.this.getParent().getString(R.string.app_name));
							emailIntent.putExtra(Intent.EXTRA_TEXT, "");
							WeightChartActivity.this.getParent().getParent().startActivity((Intent.createChooser(emailIntent, "Send email...")));
						}
					})
					.setPositiveButtonText(R.string.agree)
					.setNegativeButton(R.id.dialogButtonCancel, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					})
					.setNegativeButtonText(R.string.disagree);
					bld.show();
				}
			})
			.setNegativeButtonText(R.string.not_shure);
			bld.show();
		}catch (BadTokenException e) {
			e.printStackTrace();
		}
		}
		super.onDestroy();
	}

	private ArrayList<DishType> getChartTypes(Context ctx) {
		ArrayList<DishType> types = new ArrayList<DishType>();
		types.add(new DishType(1,
				getString(R.string.change_weight_dialog_title)));
		if (SaveUtils.getChestEnbl(ctx))
			types.add(new DishType(2, getString(R.string.volume_chest)));
		if (SaveUtils.getBicepsEnbl(ctx))
			types.add(new DishType(3, getString(R.string.volume_biceps)));
		if (SaveUtils.getForearmEnbl(ctx))
			types.add(new DishType(4, getString(R.string.volume_forearm)));
		if (SaveUtils.getHipEnbl(ctx))
			types.add(new DishType(5, getString(R.string.volume_hip)));
		if (SaveUtils.getNeckEnbl(ctx))
			types.add(new DishType(6, getString(R.string.volume_neck)));
		if (SaveUtils.getShinEnbl(ctx))
			types.add(new DishType(7, getString(R.string.volume_shin)));
		if (SaveUtils.getPelvisEnbl(ctx))
			types.add(new DishType(8, getString(R.string.volume_pelvis)));
		if (SaveUtils.getWaistEnbl(ctx))
			types.add(new DishType(9, getString(R.string.volume_waist)));
		return types;
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub
		int i = 8;
		i = 8 + i;
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.lastweek:
			initDataForChart(7);
			break;
		case R.id.lastmonth:
			initDataForChart(30);
			break;
		case R.id.alltime:
			initDataForChart(800);
			break;
		default:
			// Nothing to do
		}
		drawChart(mCurrnetChartType);
	}
}

/*
 * protected BarChart mChart; private SeekBar mSeekBarX, mSeekBarY; private
 * TextView tvX, tvY;
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
 * setContentView(R.layout.activity_barchart);
 * 
 * tvX = (TextView) findViewById(R.id.tvXMax); tvY = (TextView)
 * findViewById(R.id.tvYMax);
 * 
 * mSeekBarX = (SeekBar) findViewById(R.id.seekBar1); mSeekBarY = (SeekBar)
 * findViewById(R.id.seekBar2);
 * 
 * mChart = (BarChart) findViewById(R.id.chart1);
 * mChart.setOnChartValueSelectedListener(this);
 * 
 * mChart.setDrawBarShadow(false); mChart.setDrawValueAboveBar(true);
 * 
 * mChart.setDescription("");
 * 
 * // if more than 60 entries are displayed in the chart, no values will be //
 * drawn mChart.setMaxVisibleValueCount(60);
 * 
 * // scaling can now only be done on x- and y-axis separately
 * mChart.setPinchZoom(false);
 * 
 * mChart.setDrawGridBackground(false); // mChart.setDrawYLabels(false);
 * 
 * 
 * 
 * XAxis xAxis = mChart.getXAxis(); xAxis.setPosition(XAxisPosition.BOTTOM);
 * xAxis.setDrawGridLines(false); xAxis.setSpaceBetweenLabels(2);
 * 
 * YAxisValueFormatter custom = new MyYAxisValueFormatter();
 * 
 * YAxis leftAxis = mChart.getAxisLeft(); leftAxis.setLabelCount(8, false);
 * leftAxis.setValueFormatter(custom);
 * leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
 * leftAxis.setSpaceTop(15f); leftAxis.setAxisMinValue(0f); // this replaces
 * setStartAtZero(true)
 * 
 * YAxis rightAxis = mChart.getAxisRight(); rightAxis.setDrawGridLines(false);
 * rightAxis.setLabelCount(8, false); rightAxis.setValueFormatter(custom);
 * rightAxis.setSpaceTop(15f); rightAxis.setAxisMinValue(0f); // this replaces
 * setStartAtZero(true)
 * 
 * Legend l = mChart.getLegend();
 * l.setPosition(LegendPosition.BELOW_CHART_LEFT); l.setForm(LegendForm.SQUARE);
 * l.setFormSize(9f); l.setTextSize(11f); l.setXEntrySpace(4f); //
 * l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc", // "def",
 * "ghj", "ikl", "mno" }); // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new
 * String[] { "abc", // "def", "ghj", "ikl", "mno" });
 * 
 * setData(12, 50);
 * 
 * // setting data mSeekBarY.setProgress(50); mSeekBarX.setProgress(12);
 * 
 * mSeekBarY.setOnSeekBarChangeListener(this);
 * mSeekBarX.setOnSeekBarChangeListener(this);
 * 
 * // mChart.setDrawLegend(false); }
 * 
 * @Override public boolean onOptionsItemSelected(MenuItem item) {
 * 
 * switch (item.getItemId()) { case R.id.actionToggleValues: { for (IDataSet set
 * : mChart.getData().getDataSets())
 * set.setDrawValues(!set.isDrawValuesEnabled());
 * 
 * mChart.invalidate(); break; } case R.id.actionToggleHighlight: {
 * if(mChart.getData() != null) {
 * mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
 * mChart.invalidate(); } break; } case R.id.actionTogglePinch: { if
 * (mChart.isPinchZoomEnabled()) mChart.setPinchZoom(false); else
 * mChart.setPinchZoom(true);
 * 
 * mChart.invalidate(); break; } case R.id.actionToggleAutoScaleMinMax: {
 * mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
 * mChart.notifyDataSetChanged(); break; } case R.id.actionToggleBarBorders: {
 * for (IBarDataSet set : mChart.getData().getDataSets())
 * ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f :
 * 1.f);
 * 
 * mChart.invalidate(); break; } case R.id.actionToggleHighlightArrow: { if
 * (mChart.isDrawHighlightArrowEnabled()) mChart.setDrawHighlightArrow(false);
 * else mChart.setDrawHighlightArrow(true); mChart.invalidate(); break; } case
 * R.id.animateX: { mChart.animateX(3000); break; } case R.id.animateY: {
 * mChart.animateY(3000); break; } case R.id.animateXY: {
 * 
 * mChart.animateXY(3000, 3000); break; } case R.id.actionSave: { if
 * (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
 * Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
 * Toast.LENGTH_SHORT).show(); } else Toast.makeText(getApplicationContext(),
 * "Saving FAILED!", Toast.LENGTH_SHORT) .show(); break; } } return true; }
 * 
 * @Override public void onProgressChanged(SeekBar seekBar, int progress,
 * boolean fromUser) {
 * 
 * tvX.setText("" + (mSeekBarX.getProgress() + 1)); tvY.setText("" +
 * (mSeekBarY.getProgress()));
 * 
 * setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());
 * mChart.invalidate(); }
 * 
 * @Override public void onStartTrackingTouch(SeekBar seekBar) { // TODO
 * Auto-generated method stub
 * 
 * }
 * 
 * @Override public void onStopTrackingTouch(SeekBar seekBar) { // TODO
 * Auto-generated method stub
 * 
 * }
 * 
 * private void setData(int count, float range) {
 * 
 * ArrayList<String> xVals = new ArrayList<String>(); for (int i = 0; i < count;
 * i++) { xVals.add(mMonths[i % 12]); }
 * 
 * ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
 * 
 * for (int i = 0; i < count; i++) { float mult = (range + 1); float val =
 * (float) (Math.random() * mult); yVals1.add(new BarEntry(val, i)); }
 * 
 * BarDataSet set1;
 * 
 * if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
 * set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
 * set1.setYVals(yVals1); mChart.getData().setXVals(xVals);
 * mChart.getData().notifyDataChanged(); mChart.notifyDataSetChanged(); } else {
 * set1 = new BarDataSet(yVals1, "DataSet"); set1.setBarSpacePercent(35f);
 * set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
 * 
 * ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
 * dataSets.add(set1);
 * 
 * BarData data = new BarData(xVals, dataSets); data.setValueTextSize(10f);
 * 
 * mChart.setData(data); } }
 * 
 * @Override public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
 * {
 * 
 * if (e == null) return;
 * 
 * RectF bounds = mChart.getBarBounds((BarEntry) e); PointF position =
 * mChart.getPosition(e, AxisDependency.LEFT);
 * 
 * Log.i("bounds", bounds.toString()); Log.i("position", position.toString());
 * 
 * Log.i("x-index", "low: " + mChart.getLowestVisibleXIndex() + ", high: " +
 * mChart.getHighestVisibleXIndex()); }
 * 
 * public void onNothingSelected() { }
 * 
 * }
 */

class MyYAxisValueFormatter implements YAxisValueFormatter {

	private DecimalFormat mFormat;

	public MyYAxisValueFormatter() {
		mFormat = new DecimalFormat("###,###,###,##0.0");
	}

	@Override
	public String getFormattedValue(float value, YAxis yAxis) {
		return mFormat.format(value) + " $";
	}
}

/*
 * Spinner chartTypeSpiner; GraphView graphView;
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); View viewToLoad =
 * LayoutInflater.from(this.getParent().getParent()).inflate(
 * R.layout.statistics_weight, null); setContentView(viewToLoad);
 * 
 * /// Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);
 * backButton.setOnClickListener(new OnClickListener() { public void
 * onClick(View v) { onBackPressed(); } });
 * 
 * chartTypeSpiner = (Spinner) viewToLoad .findViewById(R.id.SpinnerChartType);
 * // chartTypeSpiner ArrayList<DishType> types = new ArrayList<DishType>();
 * types.add(new DishType(1, getString(R.string.change_weight_dialog_title)));
 * if (SaveUtils.getChestEnbl(WeightChartActivity.this)) types.add(new
 * DishType(2, getString(R.string.volume_chest))); if
 * (SaveUtils.getBicepsEnbl(WeightChartActivity.this)) types.add(new DishType(3,
 * getString(R.string.volume_biceps))); if
 * (SaveUtils.getForearmEnbl(WeightChartActivity.this)) types.add(new
 * DishType(4, getString(R.string.volume_forearm))); if
 * (SaveUtils.getHipEnbl(WeightChartActivity.this)) types.add(new DishType(5,
 * getString(R.string.volume_hip))); if
 * (SaveUtils.getNeckEnbl(WeightChartActivity.this)) types.add(new DishType(6,
 * getString(R.string.volume_neck))); if
 * (SaveUtils.getShinEnbl(WeightChartActivity.this)) types.add(new DishType(7,
 * getString(R.string.volume_shin))); if
 * (SaveUtils.getPelvisEnbl(WeightChartActivity.this)) types.add(new DishType(8,
 * getString(R.string.volume_pelvis))); if
 * (SaveUtils.getWaistEnbl(WeightChartActivity.this)) types.add(new DishType(9,
 * getString(R.string.volume_waist)));
 * 
 * ArrayAdapter<DishType> adapter = new ArrayAdapter<DishType>(this,
 * android.R.layout.simple_spinner_item, types); // init example series data //
 * graph with dynamically genereated horizontal and vertical labels
 * 
 * // graph with custom labels and drawBackground
 * 
 * graphView = new LineGraphView( this , "" ); ((LineGraphView)
 * graphView).setDrawBackground(false);
 * 
 * // custom static labels
 * 
 * final List<Day> days = TodayDishHelper.getDaysStat(getApplicationContext());
 * Collections.reverse(days); final GraphViewData[] data = new
 * GraphViewData[days.size()];
 * 
 * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
 * ; chartTypeSpiner.setAdapter(adapter); chartTypeSpiner.setSelection(0);
 * chartTypeSpiner .setOnItemSelectedListener(new OnItemSelectedListener() {
 * 
 * public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long
 * arg3) { try {
 * 
 * int i = 0; int dtype = ((DishType) chartTypeSpiner
 * .getSelectedItem()).getTypeKey(); for (Day day : days) { switch (dtype) {
 * case 1: data[i] = new GraphViewData(i, day .getBodyWeight()); break; case 2:
 * data[i] = new GraphViewData(i, day .getChest()); break; case 3: data[i] = new
 * GraphViewData(i, day .getBiceps()); break; case 4: data[i] = new
 * GraphViewData(i, day .getForearm()); break; case 5: data[i] = new
 * GraphViewData(i, day .getHip()); break; case 6: data[i] = new
 * GraphViewData(i, day .getNeck()); break; case 7: data[i] = new
 * GraphViewData(i, day .getShin()); break; case 8: data[i] = new
 * GraphViewData(i, day .getPelvis()); break; case 9: data[i] = new
 * GraphViewData(i, day .getWaist()); break; default: break; } i++; }
 * 
 * }catch (Exception e) { e.printStackTrace(); } GraphViewSeries exampleSeries =
 * new GraphViewSeries(data); if(days.size() > 1){ TextView tv = (TextView)
 * findViewById(R.id.textViewEmpty); tv.setVisibility(View.GONE); String[] hor =
 * new String[days.size()]; int j=0; SimpleDateFormat sdf = new
 * SimpleDateFormat("dd/MM",new Locale(
 * SaveUtils.getLang(WeightChartActivity.this))); for (Day day : days) {
 * if(days.size()>8 && days.size()<16){ if((j%2)==0){
 * hor[j]=String.valueOf(sdf.format(new Date(day.getDateInt()))); }else{
 * hor[j]=""; } }else if(days.size()>=16 && days.size()<24){ if((j%3)==0){
 * hor[j]=String.valueOf(sdf.format(new Date(day.getDateInt()))); }else{
 * hor[j]=""; } }else if(days.size()>=24){ if((j%4)==0){
 * hor[j]=String.valueOf(sdf.format(new Date(day.getDateInt()))); }else{
 * hor[j]=""; } }else{ hor[j]=String.valueOf(sdf.format(new
 * Date(day.getDateInt()))); } j++; } String[] vertical = new
 * String[data.length]; for (int i = 0; i < data.length; i++) {
 * vertical[i]=String.valueOf(data[i].valueY); }
 * graphView.setHorizontalLabels(hor); graphView.setScalable(false);
 * graphView.setVerticalLabels(null); graphView.addSeries(exampleSeries); //
 * data
 * 
 * LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutChart);
 * layout.removeAllViews(); layout.addView(graphView); }else{ TextView tv =
 * (TextView) findViewById(R.id.textViewEmpty); tv.setVisibility(View.VISIBLE);
 * } }
 * 
 * public void onNothingSelected(AdapterView<?> arg0) { } }); }
 */

