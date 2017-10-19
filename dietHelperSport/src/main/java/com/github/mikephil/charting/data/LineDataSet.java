
package com.github.mikephil.charting.data;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LineDataSet extends LineRadarDataSet<Entry> implements ILineDataSet {

    /** List representing all colors that are used for the circles */
    private List<Integer> mCircleColors = null;

    /** the color of the inner circles */
    private int mCircleColorHole = Color.WHITE;

    /** the radius of the circle-shaped value indicators */
    private float mCircleRadius = 8f;

    /** sets the intensity of the cubic lines */
    private float mCubicIntensity = 0.2f;

    /** the path effect of this DataSet that makes dashed lines possible */
    private DashPathEffect mDashPathEffect = null;

    /** formatter for customizing the position of the fill-line */
    private FillFormatter mFillFormatter = new DefaultFillFormatter();

    /** if true, drawing circles is enabled */
    private boolean mDrawCircles = true;

    /** if true, cubic lines are drawn instead of linear */
    private boolean mDrawCubic = false;

    private boolean mDrawCircleHole = true;


    public LineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);

        // mCircleRadius = Utils.convertDpToPixel(4f);
        // mLineWidth = Utils.convertDpToPixel(1f);

        mCircleColors = new ArrayList<Integer>();

        // default colors
        // mColors.add(Color.rgb(192, 255, 140));
        // mColors.add(Color.rgb(255, 247, 140));
        mCircleColors.add(Color.rgb(140, 234, 255));
    }

    @Override
    public DataSet<Entry> copy() {

        List<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < mYVals.size(); i++) {
            yVals.add(mYVals.get(i).copy());
        }

        LineDataSet copied = new LineDataSet(yVals, getLabel());
        copied.mColors = mColors;
        copied.mCircleRadius = mCircleRadius;
        copied.mCircleColors = mCircleColors;
        copied.mDashPathEffect = mDashPathEffect;
        copied.mDrawCircles = mDrawCircles;
        copied.mDrawCubic = mDrawCubic;
        copied.mHighLightColor = mHighLightColor;

        return copied;
    }

    @Override
    public float getCubicIntensity() {
        return mCubicIntensity;
    }


    /**
     * sets the radius of the drawn circles.
     * Default radius = 4f
     *
     * @param radius
     */
    public void setCircleRadius(float radius) {
        mCircleRadius = Utils.convertDpToPixel(radius);
    }

    @Override
    public float getCircleRadius() {
        return mCircleRadius;
    }

    @Override
    public boolean isDashedLineEnabled() {
        return mDashPathEffect == null ? false : true;
    }

    @Override
    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    @Override
    public boolean isDrawCirclesEnabled() {
        return mDrawCircles;
    }

    /**
     * If set to true, the linechart lines are drawn in cubic-style instead of
     * linear. This affects performance! Default: false
     * 
     * @param enabled
     */
    public void setDrawCubic(boolean enabled) {
        mDrawCubic = enabled;
    }

    @Override
    public boolean isDrawCubicEnabled() {
        return mDrawCubic;
    }

    /** ALL CODE BELOW RELATED TO CIRCLE-COLORS */

    @Override
    public int getCircleColor(int index) {
        return mCircleColors.get(index % mCircleColors.size());
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet.
     * Internally, this recreates the colors array and adds the specified color.
     * 
     * @param color
     */
    public void setCircleColor(int color) {
        resetCircleColors();
        mCircleColors.add(color);
    }

    /**
     * resets the circle-colors array and creates a new one
     */
    public void resetCircleColors() {
        mCircleColors = new ArrayList<Integer>();
    }

    @Override
    public int getCircleHoleColor() {
        return mCircleColorHole;
    }

    @Override
    public boolean isDrawCircleHoleEnabled() {
        return mDrawCircleHole;
    }

    @Override
    public FillFormatter getFillFormatter() {
        return mFillFormatter;
    }
}
