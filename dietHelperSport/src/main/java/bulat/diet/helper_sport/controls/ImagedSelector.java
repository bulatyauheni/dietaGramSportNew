package bulat.diet.helper_sport.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import bulat.diet.helper_sport.R;

/**
 * Created by Yauheni.Bulat on 05.09.2017.
 */

public class ImagedSelector extends LinearLayout implements View.OnClickListener {
    private static final int DEFAULT_LAYOUT = R.layout.imaged_selector;
    private int layoutId;
    private ItemSelectedListener mListener;
    private View selectedView;

    public ImagedSelector(Context context) {
        super(context);

    }

    public ImagedSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImagedSelector, 0, 0);
        this.layoutId = attributes.getResourceId(R.styleable.ImagedSelector_custom_layout, this.DEFAULT_LAYOUT);
        attributes.recycle();
        inflateView(context, layoutId);
    }

    public ImagedSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImagedSelector, 0, 0);
        this.layoutId = attributes.getResourceId(R.styleable.ImagedSelector_custom_layout, this.DEFAULT_LAYOUT);
        attributes.recycle();
        inflateView(context, layoutId);
    }

    private void inflateView(Context context, int view) {
        View layout = LayoutInflater.from(context).inflate(layoutId, this, true);
        LinearLayout llayout = (LinearLayout) ((LinearLayout)layout).getChildAt(0);
        int count = llayout.getChildCount();
        View v = null;
        for(int i=0; i<count; i++) {
            v = llayout.getChildAt(i);
            v.setTag(i + 1);
            v.setOnClickListener(this);
        }
    }

    public void setOnItemSelectListener(ItemSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (selectedView != null) {
            selectedView.setActivated(false);
        }
        v.setActivated(true);
        selectedView = v;
        if (mListener != null) {
            mListener.onItemSelected((Integer) v.getTag());
        }
    }

    public void setSelectedItem(int position) {
        LinearLayout llayout = (LinearLayout) getChildAt(0);
        View v = llayout.getChildAt(position-1);
        if (v != null) {
            v.setActivated(true);
            if (selectedView != null) {
                selectedView.setSelected(false);
            }
            selectedView = v;
            if (mListener != null) {
                mListener.onItemSelected((Integer) v.getTag());
            }
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(int position);
    }
}
