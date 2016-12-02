
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object that allows the combination of Line-, Bar-, Scatter-, Bubble- and
 * CandleData. Used in the CombinedChart class.
 * 
 * @author Philipp Jahoda
 */
public class CombinedData extends BarLineScatterCandleBubbleData<IBarLineScatterCandleBubbleDataSet<?>> {

    private LineData mLineData;
    private BarData mBarData;

    public CombinedData() {
        super();
    }

    public CombinedData(List<String> xVals) {
        super(xVals);
    }

    public CombinedData(String[] xVals) {
        super(xVals);
    }

    public void setData(LineData data) {
        mLineData = data;
        mDataSets.addAll(data.getDataSets());
        init();
    }

    public void setData(BarData data) {
        mBarData = data;
        mDataSets.addAll(data.getDataSets());
        init();
    }


    public LineData getLineData() {
        return mLineData;
    }

    public BarData getBarData() {
        return mBarData;
    }

    /**
     * Returns all data objects in row: line-bar-scatter-candle-bubble if not null.
     * @return
     */
    public List<ChartData> getAllData() {

        List<ChartData> data = new ArrayList<ChartData>();
        if(mLineData != null)
            data.add(mLineData);
        if(mBarData != null)
            data.add(mBarData);
        
        return data;
    }

    @Override
    public void notifyDataChanged() {
        if (mLineData != null)
            mLineData.notifyDataChanged();
        if (mBarData != null)
            mBarData.notifyDataChanged();
        

        init(); // recalculate everything
    }
}
