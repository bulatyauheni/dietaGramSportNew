package bulat.diet.helper_sport.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.db.TodayDishHelper;

public class Statistics extends Activity {

	
	private static final String Diery = "d";
	private static final String Meat = "m";
	private static final String Fruit = "f";
	private static final String Sweets = "s";
	private static final String Bakery = "b";

	float values[]={245,120,60,120,245};
	float values2[]={0,0,0,0,0};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.statistics, null);
		setContentView(viewToLoad);	
		Button backButton = (Button) viewToLoad.findViewById(R.id.buttonBack);				
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		}

	private float[] getValues(Map<String, Integer> statistic) {
		float res[]={0,0,0,0,0};	
		if(statistic == null){
			return res;
		}
		try{
			if(statistic.get(Bakery) != null){		
				res[0]=statistic.get(Bakery);
			}
			if(statistic.get(Diery) != null){
				res[1]=statistic.get(Diery);
			}
			if(statistic.get(Sweets) != null){
				res[2]=statistic.get(Sweets);
			}
			if(statistic.get(Meat) != null){
				res[3]=statistic.get(Meat);
			}
			if(statistic.get(Fruit) != null){
				res[4]=statistic.get(Fruit);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LinearLayout linear=(LinearLayout) findViewById(R.id.linearLayoutChart);
        values=calculateData(values);
        linear.addView(new MyGraphview(this,values));
        
        LinearLayout linear2=(LinearLayout) findViewById(R.id.linearLayoutChart2);
        values2 = getValues(TodayDishHelper.getStatistic(this));
        values2=calculateData(values2);
        linear2.removeAllViews();
        linear2.addView(new MyGraphview(this,values2));
	}

	private float[] calculateData(float[] data) {
        // TODO Auto-generated method stub
        float total=0;
        for(int i=0;i<data.length;i++)
        {
            total+=data[i];
        }
        for(int i=0;i<data.length;i++)
        {
        data[i]=360*(data[i]/total);            
        }
        return data;

    }
    public class MyGraphview extends View
    {
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        private int[] COLORS={Color.CYAN,Color.LTGRAY,Color.YELLOW,Color.RED,Color.GREEN};
        RectF rectf = new RectF (getDIP(5), getDIP(5), getDIP(155), getDIP(155));
        int temp=-90;
        
        public MyGraphview(Context context, float[] values) {

            super(context);
            value_degree=new float[values.length];
            for(int i=0;i<values.length;i++)
            {
                value_degree[i]=values[i];
            }
        }
        
        private float getDIP(int i) {
        	return getResources().getDisplayMetrics().density * i;
		}
		@Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            temp = -90;
            for (int i = 0; i < value_degree.length; i++) {//values2.length; i++) {
                if (i == 0) {
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, -90, value_degree[i], true, paint);
                } 
                else
                {
                        temp += (int) value_degree[i - 1];
                        paint.setColor(COLORS[i]);
                        canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }

    }
    
    public class LinevChariew extends View
    {
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
      //  private float[] value_degree;
      //  private int[] COLORS={Color.CYAN,Color.LTGRAY,Color.YELLOW,Color.RED,Color.GREEN};
      //  RectF rectf = new RectF (getDIP(5), getDIP(5), getDIP(155), getDIP(155));
      //  int temp=-90;
        
        public LinevChariew(Context context, float[] values) {

            super(context);
        //    value_degree=new float[values.length];
            for(int i=0;i<values.length;i++)
            {
        //        value_degree[i]=values[i];
            }
        }
        
        private float getDIP(int i) {
        	return getResources().getDisplayMetrics().density * i;
		}
		@Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            
            ArrayList<Integer> myArrayListOfValues = new ArrayList<Integer>();
            myArrayListOfValues.add(120 - 19);
            myArrayListOfValues.add(120 - 29);
            myArrayListOfValues.add(120 - 19);
            myArrayListOfValues.add(120 - 39);
            myArrayListOfValues.add(120 - 92);
            int step = 0;
           // canvas.translate(0,canvas.getHeight());   // reset where 0,0 is located
           
            
			for(int i=1; i<myArrayListOfValues.size(); i++){
				step = step + 10;
                Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                myPaint.setStrokeWidth(8/*1 /getResources().getDisplayMetrics().density*/);
                myPaint.setColor(0xffff0000);   //color.RED

                canvas.drawLine(step, myArrayListOfValues.get(i - 1), step + 10, myArrayListOfValues.get(i), myPaint);       

            }
			canvas.save();
			canvas.translate(0,canvas.getHeight());   // reset where 0,0 is located
            canvas.scale(1,-1); 
            canvas.restore();
        }

    }


}
