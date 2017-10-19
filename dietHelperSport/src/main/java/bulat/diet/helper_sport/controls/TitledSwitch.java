package bulat.diet.helper_sport.controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;

import bulat.diet.helper_sport.R;

public class TitledSwitch extends CompoundButton {
    private static final int TOUCH_MODE_IDLE     = 0;
    private static final int TOUCH_MODE_DOWN     = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final String TAG              = "MySwitch";

    // Enum for the "typeface" XML parameter.
    private static final int SANS       = 1;
    private static final int SERIF      = 2;
    private static final int MONOSPACE  = 3;

    private static final int VERTICAL   = 0;
    private static final int HORIZONTAL = 1;

    private int mOrientation = HORIZONTAL;
    private OnChangeAttemptListener mOnChangeAttemptListener;
    private boolean mPushStyle;
    private boolean mTextOnThumb;
    private int mThumbExtraMovement;
    private Drawable mLeftBackground;
    private Drawable mRightBackground;
    private Drawable mMaskDrawable;
    private Drawable mThumbDrawable;
    private Drawable mTrackDrawable;
    private int mThumbTextPadding;
    private int mTrackTextPadding;
    private int mSwitchMinWidth;
    private int mSwitchMinHeight;
    private int mSwitchPadding;
    private CharSequence mTextOn;
    private CharSequence mTextOff;
    private Drawable mDrawableOn;
    private Drawable mDrawableOff;
    private boolean fixed         = false;
    private boolean clickDisabled = false;
    private boolean onOrOff=true;
    private Bitmap pushBitmap;
    private Bitmap maskBitmap;
    private Bitmap tempBitmap;
    private Canvas backingLayer;

    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private int mMinFlingVelocity;

    private float mThumbPosition = 0;
    private int mSwitchWidth;
    private int mSwitchHeight;
    private int mThumbWidth;
    private int mThumbHeight;

    private int mSwitchLeft;
    private int mSwitchTop;
    private int mSwitchRight;
    private int mSwitchBottom;


    private TextPaint mTextPaintOn;
    private TextPaint mTextPaintOff;
    private ColorStateList mTextColors;
    private int mTextColorsHighlighted;
    private Layout mOnLayout;
    private Layout mOffLayout;

    private Paint xferPaint;
    private Bitmap leftBitmap, rightBitmap;

    private final Rect mTrackPaddingRect = new Rect();
    private final Rect mThPad = new Rect();
    private final Rect canvasClipBounds = new Rect();

    int alpha = (int) (255*0.9);
    //Animation support
    long  mStartTime;
    float mStartPosition;
    float mAnimDuration;
    float   mMaxAnimDuration = 250;
    boolean mRunning         = false;
    final int FRAME_DURATION = 10;
    Interpolator mInterpolator;

    public boolean isIndicatorVisible() {
        return isIndicatorVisible;
    }

    public void setIndicatorVisible(boolean indicatorVisible) {
        isIndicatorVisible = indicatorVisible;
    }

    private boolean isIndicatorVisible = false;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    /**
     * Construct a new MySwitch with default styling.
     *
     * @param context The Context that will determine this widget's theming.
     */
    public TitledSwitch(Context context) {
        this(context, null);
    }

    /**
     * Construct a new MySwitch with default styling, overriding specific style
     * attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from default styling.
     */
    public TitledSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchStyleAttr);
    }

    /**
     * Construct a new MySwitch with a default style determined by the given theme attribute,
     * overriding specific style attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from the default styling.
     * @param defStyle An attribute ID within the active theme containing a reference to the
     *                 default style for this widget. e.g. android.R.attr.switchStyle.
     */
    public TitledSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //if (Build.VERSION.SDK_INT >= 11) {
        //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //}
        Resources res = getResources();
        float     density = res.getDisplayMetrics().scaledDensity;

        int textNormal = 0xff000000; //res.getColor(R.color.textNormal)
        int textInvertedNormal = 0xffffffff; //res.getColor(R.color.textInvertedNormal);
        int colorAccent = 0xffffcc00; //res.getColor(R.color.colorAccent)

        mTextPaintOn = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaintOn.density = res.getDisplayMetrics().density;
        mTextPaintOn.setTextSize(16 * density);
        mTextPaintOn.setColor(textInvertedNormal);

        mTextPaintOff = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaintOff.density = res.getDisplayMetrics().density;
        mTextPaintOff.setTextSize(16 * density);
        mTextPaintOff.setColor(textNormal);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitledSwitch, defStyle, 0);

        mLeftBackground = a.getDrawable(R.styleable.TitledSwitch_leftBackground);
        mRightBackground = a.getDrawable(R.styleable.TitledSwitch_rightBackground);
        mOrientation = a.getInteger(R.styleable.TitledSwitch_orientation, HORIZONTAL);
        mThumbDrawable = a.getDrawable(R.styleable.TitledSwitch_thumb);
        mTrackDrawable = a.getDrawable(R.styleable.TitledSwitch_track);
        mTextOn = a.getText(R.styleable.TitledSwitch_textOn);
        mTextOff = a.getText(R.styleable.TitledSwitch_textOff);
        mDrawableOn = a.getDrawable(R.styleable.TitledSwitch_drawableOn);
        mDrawableOff = a.getDrawable(R.styleable.TitledSwitch_drawableOff);
        mPushStyle = a.getBoolean(R.styleable.TitledSwitch_pushStyle, false);

        mTextOnThumb = a.getBoolean(R.styleable.TitledSwitch_textOnThumb, true);
        mThumbExtraMovement = a.getDimensionPixelSize(R.styleable.TitledSwitch_thumbExtraMovement, 0);
        mThumbTextPadding = a.getDimensionPixelSize(R.styleable.TitledSwitch_thumbTextPadding, (int) (5 * density));
        mTrackTextPadding = a.getDimensionPixelSize(R.styleable.TitledSwitch_trackTextPadding, (int) (5 * density));

        mSwitchMinWidth = a.getDimensionPixelSize(R.styleable.TitledSwitch_switchMinWidth, (int) (60 * density));
        mSwitchMinHeight = a.getDimensionPixelSize(R.styleable.TitledSwitch_switchMinHeight, 0);
        mSwitchPadding =  a.getDimensionPixelSize(R.styleable.TitledSwitch_switchPadding, 0);

        if (mThumbDrawable == null)
        {
            StateListDrawable back = new StateListDrawable();
            GradientDrawable backn = new GradientDrawable();
            backn.setColor(colorAccent);
            backn.setCornerRadius(12 * res.getDisplayMetrics().scaledDensity);
            GradientDrawable backu = new GradientDrawable();
            backu.setColor(textNormal);
            backu.setCornerRadius(12 * res.getDisplayMetrics().scaledDensity);
            backu.setAlpha(255);
            GradientDrawable backd = new GradientDrawable();
            backd.setColor(colorAccent);
            backd.setCornerRadius(12 * res.getDisplayMetrics().scaledDensity);
            backd.setAlpha(96);
            back.addState(new int[]{-android.R.attr.state_enabled}, backd);
            back.addState(new int[]{-android.R.attr.state_checked}, backu);
            back.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_checked}, backn);
            back.addState(StateSet.WILD_CARD, backn);
            mThumbDrawable = back;
        }
        if (mTrackDrawable == null)
        {
            StateListDrawable back = new StateListDrawable();
            GradientDrawable trk = new GradientDrawable();
            trk.setColor(colorAccent);
            trk.setCornerRadius(10 * density);
            trk.setAlpha(128);
            GradientDrawable backu = new GradientDrawable();
            backu.setColor(textNormal);
            backu.setCornerRadius(12 * res.getDisplayMetrics().scaledDensity);
            backu.setAlpha(192);
            back.addState(new int[]{-android.R.attr.state_checked}, backu);
            back.addState(StateSet.WILD_CARD, trk);
            mTrackDrawable = new InsetDrawable(back, 0, (int) (density), 0, (int) (density));
        }

        mTrackDrawable.getPadding(mTrackPaddingRect);
        Log.d(TAG, "mTrackPaddingRect=" + mTrackPaddingRect);
        mThumbDrawable.getPadding(mThPad);
        Log.d(TAG, "mThPad=" + mThPad);

        mMaskDrawable = a.getDrawable(R.styleable.TitledSwitch_backgroundMask);

        RuntimeException e = null;
        if ((mLeftBackground != null || mRightBackground != null) && mMaskDrawable == null) {
            e = new IllegalArgumentException(a.getPositionDescription()
                    + " if left/right background is given, then a mask has to be there");
        }

        if (((mLeftBackground != null) ^ (mRightBackground != null)) && mMaskDrawable == null) {
            e = new IllegalArgumentException(a.getPositionDescription()
                    + " left and right background both should be there. only one is not allowed ");
        }

        if (mTextOnThumb && mPushStyle) {
            e = new IllegalArgumentException(a.getPositionDescription()
                    + " Text On Thumb and Push Style are mutually exclusive. Only one can be present ");
        }

        xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //xferPaint.setColor(Color.TRANSPARENT);
        xferPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));


        int appearance = a.getResourceId(R.styleable.TitledSwitch_switchTextAppearanceAttrib, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        a.recycle();
        if (e != null) {
            throw e;
        }
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();

        mInterpolator = new DecelerateInterpolator();
        // Refresh display with current params
        refreshDrawableState();
        setChecked(isChecked());
        this.setClickable(true);
    }

    /**
     * Sets the switch text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     */
    public void setSwitchTextAppearance(Context context, int resid) {
        TypedArray appearance =
                context.obtainStyledAttributes(resid, R.styleable.switchTextAppearanceAttrib);

        ColorStateList colors;
        int ts;
        int colorsHighlight;

        colors = appearance.getColorStateList(R.styleable.switchTextAppearanceAttrib_textColor);
        colorsHighlight = appearance.getColor(R.styleable.switchTextAppearanceAttrib_textColorHighlight, Color.WHITE);
        if (colors != null) {
            mTextColors = colors;
            mTextColorsHighlighted = colorsHighlight;
        } else {
            // If no color set in TextAppearance, default to the view's textColor
            mTextColors = getTextColors();
        }

        ts = appearance.getDimensionPixelSize(R.styleable.switchTextAppearanceAttrib_textSize, 0);
        if (ts != 0) {
            if (ts != mTextPaintOn.getTextSize()) {
                mTextPaintOn.setTextSize(ts);
                mTextPaintOff.setTextSize(ts);
                requestLayout();
            }
        }

        appearance.recycle();
    }


    /**
     * Returns the text displayed when the button is in the checked state.
     */
    public CharSequence getTextOn() {
        return mTextOn;
    }

    /**
     * Sets the text displayed when the button is in the checked state.
     */
    public void setTextOn(CharSequence textOn) {
        mTextOn = textOn;
        this.mOnLayout = null;
        requestLayout();
    }

    /**
     * Returns the text displayed when the button is not in the checked state.
     */
    public CharSequence getTextOff() {
        return mTextOff;
    }

    /**
     * Sets the text displayed when the button is not in the checked state.
     */
    public void setTextOff(CharSequence textOff) {
        mTextOff = textOff;
        this.mOffLayout = null;
        requestLayout();
    }


    /**
     * Interface definition for a callback to be invoked when the switch is
     * in a fixed state and there was an attempt to change its state either
     * via a click or drag
     */
    public static interface OnChangeAttemptListener {
        /**
         * Called when an attempt was made to change the checked state of the
         * switch while the switch was in a fixed state.
         *
         * @param isChecked  The current state of switch.
         */
        void onChangeAttempted(boolean isChecked);
    }

    /**
     * Register a callback to be invoked when there is an attempt to change the
     * state of the switch when its in fixated
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnChangeAttemptListener(OnChangeAttemptListener listener) {
        mOnChangeAttemptListener = listener;
    }


    /**
     * fixates the switch on one of the positions ON or OFF.
     * if the switch is fixated, then it cannot be switched to the other position
     *
     * @param fixed   If true, sets the switch to fixed mode.
     *                If false, sets the switch to switched mode.
     * @param onOrOff The switch position to which it will be fixed.
     *                If it is true then the switch is fixed on ON.
     *                If it is false then the switch is fixed on OFF
     * @Note The position is only fixed from the user interface. It can still be
     *       changed through program by using {@link #setChecked(boolean) setChecked}
     */
    public void fixate(boolean fixed, boolean onOrOff) {
        fixate(fixed);
        this.onOrOff = onOrOff;
        if (onOrOff)
            this.setChecked(true);
    }

    /**
     * fixates the switch on one of the positions ON or OFF.
     * if the switch is fixated, then it cannot be switched to the other position
     *
     * @param fixed   if true, sets the switch to fixed mode.
     *                if false, sets the switch to switched mode.
     */
    public void fixate(boolean fixed) {
        this.fixed = fixed;
    }

    /**
     * returns if the switch is fixed to one of its positions
     */
    public boolean isFixed() {
        return fixed;
    }


    private Layout makeLayout(CharSequence text, boolean isOn) {
        if (isOn) {
            return new StaticLayout(text, mTextPaintOn,
                    (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaintOn)),
                    Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
        }else {
            return new StaticLayout(text, mTextPaintOff,
                    (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaintOff)),
                    Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
        }
    }

    /**
     * @return true if (x, y) is within the target area of the switch thumb
     */
    private boolean hitThumb(float x, float y) {
        if (mOrientation == HORIZONTAL) {
            final int thumbTop = mSwitchTop - mTouchSlop;
            final int thumbLeft = mSwitchLeft + (int) (mThumbPosition + 0.5f) - mTouchSlop;
            final int thumbRight = thumbLeft + mThumbWidth + mTouchSlop;// + mThPad.left + mThPad.right
            final int thumbBottom = mSwitchBottom + mTouchSlop;
            return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom;
        }

        //if (mOrientation == VERTICAL)
        if (mSwitchHeight >150) {
            final int thumbLeft = mSwitchLeft - mTouchSlop;
            final int thumbTop = mSwitchTop + (int) (mThumbPosition + 0.5f) - mTouchSlop;
            final int thumbBottom = thumbTop + mThumbHeight + mTouchSlop;// + mThPad.top + mThPad.bottom
            final int thumbRight = mSwitchRight + mTouchSlop;
            Log.d(TAG, "returning " +  (x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom));
            return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom;
        }
        else  {
            return x > mSwitchLeft && x < mSwitchRight && y > mSwitchTop && y < mSwitchBottom;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //if (fixed) {
        //Log.d(TAG, "the switch position is fixed to " + (onOrOff ? "On":"Off") + "position.");
        //return true;
        //}
        mVelocityTracker.addMovement(ev);
        //Log.d(TAG, "onTouchEvent(ev="+ev.toString()+")");
        //Log.d(TAG, "mTouchMode="+mTouchMode);
        final int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                if (isEnabled() && hitThumb(x, y)) {
                    mTouchMode = TOUCH_MODE_DOWN;
                    mTouchX = x;
                    mTouchY = y;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                switch (mTouchMode) {
                    case TOUCH_MODE_IDLE:
                        // Didn't target the thumb, treat normally.
                        break;

                    case TOUCH_MODE_DOWN: {
                        final float x = ev.getX();
                        final float y = ev.getY();
                        if (Math.abs(x - mTouchX) > mTouchSlop / 2 ||
                                Math.abs(y - mTouchY) > mTouchSlop / 2) {
                            mTouchMode = TOUCH_MODE_DRAGGING;
                            if (getParent() != null) {
                                getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            mTouchX = x;
                            mTouchY = y;
                            return true;
                        }
                        break;
                    }

                    case TOUCH_MODE_DRAGGING: {
                        if (mOrientation == HORIZONTAL) {
                            final float x = ev.getX();
                            final float dx = x - mTouchX;
                            float newPos = Math.max(0, Math.min(mThumbPosition + dx, getThumbScrollRange()));
                            if (newPos != mThumbPosition) {
                                mThumbPosition = newPos;
                                mTouchX = x;
                                invalidate();
                            }
                            return true;
                        }
                        if (mOrientation == VERTICAL) {
                            final float y = ev.getY();
                            final float dy = y - mTouchY;
                            float newPos = Math.max(0, Math.min(mThumbPosition + dy, getThumbScrollRange()));
                            if (newPos != mThumbPosition) {
                                mThumbPosition = newPos;
                                mTouchY = y;
                                invalidate();
                            }
                            return true;
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mTouchMode == TOUCH_MODE_DRAGGING) {
                    stopDrag(ev);
                    return true;
                }
                mTouchMode = TOUCH_MODE_IDLE;
                mVelocityTracker.clear();
                break;
            }
        }

        //Log.d(TAG, "mThumbPosition="+mThumbPosition);
        boolean flag = super.onTouchEvent(ev);
        //Log.d(TAG, "super.onTouchEvent(ev) returned="+flag);
        return flag;
    }


    @Override
    public boolean performClick() {
        if (!clickDisabled) {
            if (!fixed) {
                boolean flag = super.performClick();
                return flag;
            } else {
                if (this.mOnChangeAttemptListener != null)
                    this.mOnChangeAttemptListener.onChangeAttempted(isChecked());
                return false;
            }
        } else {
            return false;
        }
    }

    public void disableClick() {
        clickDisabled = true;
    }

    public void enableClick() {
        clickDisabled = false;
    }


    public CharSequence getCurrentText() {
        if (isChecked())
            return mTextOn;

        return mTextOff;
    }

    public CharSequence getText(boolean checkedState) {
        return checkedState ? mTextOn : mTextOff;
    }


    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    /**
     * Called from onTouchEvent to end a drag operation.
     *
     * @param ev Event that triggered the end of drag mode - ACTION_UP or ACTION_CANCEL
     */
    private void stopDrag(MotionEvent ev) {
        mTouchMode = TOUCH_MODE_IDLE;
        // Up and not canceled, also checks the switch has not been disabled during the drag
        boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP && isEnabled();

        //check if the swtich is fixed to a position
        commitChange = commitChange && (!fixed);
        cancelSuperTouch(ev);

        if (commitChange) {
            boolean newState;
            mVelocityTracker.computeCurrentVelocity(1000);
            if (mOrientation == HORIZONTAL) {
                float xvel = mVelocityTracker.getXVelocity();
                if (Math.abs(xvel) > mMinFlingVelocity) {
                    newState = xvel > 0;
                } else {
                    newState = getTargetCheckedState();
                }
            } else {
                float yvel = mVelocityTracker.getYVelocity();
                if (Math.abs(yvel) > mMinFlingVelocity) {
                    newState = yvel > 0;
                } else {
                    newState = getTargetCheckedState();
                }
            }

            animateThumbToCheckedState(!mTextOnThumb ^ newState);
        } else {
            animateThumbToCheckedState(isChecked());
            if (fixed)
                if (this.mOnChangeAttemptListener != null)
                    this.mOnChangeAttemptListener.onChangeAttempted(isChecked());
        }
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        setChecked(newCheckedState);
    }

    private boolean getTargetCheckedState() {
        return mThumbPosition >= getThumbScrollRange() / 2;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        float newPos = (checked ^ !mTextOnThumb) ? getThumbScrollRange() : 0;
        if (mThumbPosition != newPos) {
            startAnimation();
        }
    }

    private float getThumbPosition() {
        float sr = getThumbScrollRange();
        float chk = (isChecked() ^ !mTextOnThumb) ? sr : 0;
        float nchk = sr - chk;
        return (mThumbPosition - nchk) / (chk - nchk);
    }

    private void setThumbPosition(float pos) {
        float sr = 0;

        //need to evaluate this once
        if (sr == 0)
            sr = getThumbScrollRange();

        float chk = (isChecked() ^ !mTextOnThumb) ? sr : 0;
        float nchk = sr - chk;
        mThumbPosition = nchk + (chk - nchk) * pos;
        invalidate();
    }

    private void resetAnimation() {
        mStartTime = System.currentTimeMillis();
        mStartPosition = getThumbPosition();
        mAnimDuration = (int) (mMaxAnimDuration * (1f - mStartPosition));
    }

    private void startAnimation() {
        if (getHandler() != null) {
            resetAnimation();
            mRunning = true;
            getHandler().post(mUpdater);
        } else {
            setThumbPosition(1);
        }
        invalidate();
    }

    private void stopAnimation() {
        mRunning = false;
        setThumbPosition(1);
        if (getHandler() != null) {
            getHandler().removeCallbacks(mUpdater);
        }
        invalidate();
    }

    private final Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            long  curTime  = System.currentTimeMillis();
            float progress = Math.min(1f, (float) (curTime - mStartTime) / mAnimDuration);
            float value    = mInterpolator.getInterpolation(progress);

            setThumbPosition(mStartPosition * (1 - value) + value);

            if (progress == 1f) {
                stopAnimation();
            } else if (mRunning) {
                if (getHandler() != null) {
                    getHandler().post(mUpdater);
                } else {
                    stopAnimation();
                }
            }
        }
    };

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);


        if (mOnLayout == null) {
            mOnLayout = makeLayout(mTextOn, true);
            // prepare canvas

        }
        if (mOffLayout == null) {
            mOffLayout = makeLayout(mTextOff, false);

        }

        final int maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth());
        final int maxTextHeight = Math.max(mOnLayout.getHeight(), mOffLayout.getHeight());
        mThumbWidth = maxTextWidth + mThumbTextPadding * 2 + mThPad.left + mThPad.right;
        mThumbWidth = Math.max(mThumbWidth, mThumbDrawable.getIntrinsicWidth());
        if (mTextOnThumb == false) {
            mThumbWidth = mThumbDrawable.getIntrinsicWidth() ;
            if (mThumbWidth <15) {
                //TODO: change this to something guessed based on the other parameters.
                mThumbWidth = 15;
            }
        }

        mThumbHeight = maxTextHeight + mThumbTextPadding * 2 + mThPad.bottom + mThPad.top;
        mThumbHeight = Math.max(mThumbHeight, mThumbDrawable.getIntrinsicHeight());
        if (mTextOnThumb == false) {
            mThumbHeight = mThumbDrawable.getIntrinsicHeight() ;
            if (mThumbHeight <15) {
                //TODO: change this to something guessed based on the other parameters.
                mThumbHeight = 15;
            }
        }

        int switchWidth;
        if (mOrientation == HORIZONTAL) {
            switchWidth = Math.max(mSwitchMinWidth, maxTextWidth * 2 +
                    mThumbTextPadding * 2 + mTrackTextPadding * 2 +
                    mTrackPaddingRect.left + mTrackPaddingRect.right);
            if (mTextOnThumb == false) {
                switchWidth = Math.max(  maxTextWidth
                                + mThumbWidth + mTrackTextPadding * 2
                                + (mTrackPaddingRect.right + mTrackPaddingRect.left)/2
                        , mSwitchMinWidth);
            }

            if (mPushStyle) {
                switchWidth = Math.max(mSwitchMinWidth, maxTextWidth+ mThumbWidth +
                        mTrackTextPadding +
                        (mTrackPaddingRect.left + mTrackPaddingRect.right)/2);
            }
        } else {
            switchWidth = Math.max(maxTextWidth +
                            mThumbTextPadding * 2 + mThPad.left + mThPad.right,
                    mThumbWidth);
            if ((mPushStyle) ||(mTextOnThumb == false)) {
                switchWidth = Math.max(maxTextWidth +
                                mTrackTextPadding * 2 +
                                mTrackPaddingRect.left + mTrackPaddingRect.right,
                        mThumbWidth);
            }
        }
        switchWidth = Math.max(mSwitchMinWidth, switchWidth);

        final int trackHeight = mTrackDrawable.getIntrinsicHeight();
        final int thumbHeight = mThumbDrawable.getIntrinsicHeight();
        int switchHeight = Math.max(mSwitchMinHeight, maxTextHeight);
        switchHeight = Math.max(trackHeight, switchHeight);
        switchHeight = Math.max(switchHeight, thumbHeight);

        switch (widthMode) {
            case View.MeasureSpec.AT_MOST:
                widthSize = Math.min(widthSize, switchWidth);
                break;

            case View.MeasureSpec.UNSPECIFIED:
                widthSize = switchWidth;
                break;

            case View.MeasureSpec.EXACTLY:
                widthSize = switchWidth;
                // Just use what we were given
                break;
        }

        switch (heightMode) {
            case View.MeasureSpec.AT_MOST:
                heightSize = Math.min(heightSize, switchHeight);
                break;

            case View.MeasureSpec.UNSPECIFIED:
                heightSize = switchHeight;
                break;

            case View.MeasureSpec.EXACTLY:
                heightSize = switchHeight;
                // Just use what we were given
                break;
        }

        mSwitchWidth = switchWidth;
        mSwitchHeight = switchHeight;

        Log.d(TAG, "onMeasure():mSwitchWidth="+mSwitchWidth+" mSwitchHeight="+mSwitchHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int measuredHeight = getMeasuredHeight();
        final int measuredWidth = getMeasuredWidth();
        if (measuredHeight < switchHeight) {
            setMeasuredDimension(getMeasuredWidth(), switchHeight);
        }
        if (measuredWidth < switchWidth) {
            setMeasuredDimension(switchWidth, getMeasuredHeight());
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        int switchTop = 0;
        int switchBottom = 0;
        switch (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
            default:
            case Gravity.TOP:
                switchTop = getPaddingTop();
                switchBottom = switchTop + mSwitchHeight;
                break;

            case Gravity.CENTER_VERTICAL:
                switchTop = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 -
                        mSwitchHeight / 2;
                switchBottom = switchTop + mSwitchHeight;
                break;

            case Gravity.BOTTOM:
                switchBottom = getHeight() - getPaddingBottom();
                switchTop = switchBottom - mSwitchHeight;
                break;
        }

        mSwitchBottom = mSwitchHeight - getPaddingBottom();
        mSwitchTop = mSwitchBottom - mSwitchHeight;
        mSwitchRight = mSwitchWidth - getPaddingRight();
        mSwitchLeft = mSwitchRight - mSwitchWidth;
        if (this.mTextOnThumb) {
            mThumbPosition = isChecked() ? getThumbScrollRange() : 0;
        } else  {
            mThumbPosition = isChecked() ? 0 : getThumbScrollRange();
        }

        //now that the layout is known, prepare the drawables
        mTrackDrawable.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom);
        if (mDrawableOn != null)
            mDrawableOn.setBounds(0, 0, mDrawableOn.getIntrinsicWidth(), mDrawableOn.getIntrinsicHeight());
        if (mDrawableOff != null)
            mDrawableOff.setBounds(0, 0, mDrawableOff.getIntrinsicWidth(), mDrawableOff.getIntrinsicHeight());
        if (mLeftBackground != null)
            mLeftBackground.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom);
        if (mRightBackground != null)
            mRightBackground.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom);

        if (mMaskDrawable != null) {
            tempBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
            backingLayer = new Canvas(tempBitmap);
            mMaskDrawable.setBounds(mSwitchLeft, mSwitchTop, mSwitchRight, mSwitchBottom);

            mMaskDrawable.draw(backingLayer);
            maskBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
            int width = tempBitmap.getWidth(),  height = tempBitmap.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    maskBitmap.setPixel(x, y, (tempBitmap.getPixel(x, y) & 0xFF000000));
                }
            }
            if (mLeftBackground != null) {
                mLeftBackground.draw(backingLayer);
                backingLayer.drawBitmap(maskBitmap, 0, 0, xferPaint);
                leftBitmap = tempBitmap.copy(tempBitmap.getConfig(), true);
            }

            if (mRightBackground != null) {
                mRightBackground.draw(backingLayer);
                backingLayer.drawBitmap(maskBitmap, 0, 0, xferPaint);
                rightBitmap = tempBitmap.copy(tempBitmap.getConfig(), true);
            }
        }
        if (mPushStyle) {
            final int switchInnerTop = mSwitchTop + mTrackPaddingRect.top;
            final int switchInnerBottom = mSwitchBottom - mTrackPaddingRect.bottom;
            final int switchVerticalMid = (switchInnerTop + switchInnerBottom) / 2;
            final int maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth());
            final int maxTextHeight = Math.max(mOnLayout.getHeight(), mOffLayout.getHeight());
            int width = maxTextWidth * 2 +
                    mTrackPaddingRect.left + mTrackPaddingRect.right +
                    mThumbWidth + mTrackTextPadding * 4 ;
            int height = mSwitchBottom - mSwitchTop;

            pushBitmap = Bitmap.createBitmap(width, height,  Config.ARGB_8888);
            Canvas backingLayer = new Canvas(pushBitmap);
            mTextPaintOn.drawableState = getDrawableState();
            mTextPaintOff.drawableState = getDrawableState();
            // mTextColors should not be null, but just in case
            if (mTextColors != null) {
                mTextPaintOn.setColor(mTextColors.getColorForState(getDrawableState(),
                        mTextColors.getDefaultColor()));
                mTextPaintOff.setColor(mTextColorsHighlighted);
            }

            //for vertical orientation leftBitmap is used as top bitmap
            if (leftBitmap != null) {
                backingLayer.save();
                if (backingLayer.getClipBounds(canvasClipBounds)) {
                    if (mOrientation == HORIZONTAL) {
                        canvasClipBounds.right -= width/2;
                    }
                    if (mOrientation == VERTICAL) {
                        canvasClipBounds.bottom -= height/2;
                    }
                    backingLayer.clipRect(canvasClipBounds);
                }
                backingLayer.drawBitmap(leftBitmap, 0, 0, null);
                backingLayer.restore();
            }

            if (rightBitmap != null) {
                backingLayer.save();
                if (backingLayer.getClipBounds(canvasClipBounds)) {
                    if (mOrientation == HORIZONTAL) {
                        canvasClipBounds.left += (width)/2;
                    }
                    if (mOrientation == VERTICAL) {
                        canvasClipBounds.top += (height)/2;
                    }
                    backingLayer.clipRect(canvasClipBounds);
                }
                if (mOrientation == HORIZONTAL) {
                    backingLayer.translate(width / 2 - mTrackPaddingRect.right, 0);
                }
                if (mOrientation == VERTICAL) {
                    backingLayer.translate(0, height / 2 - mTrackPaddingRect.bottom);
                }
                backingLayer.drawBitmap(rightBitmap, 0, 0, null);
                backingLayer.restore();
            }


        }
    }

    // Draw the switch
    @Override
    protected void onDraw(Canvas canvas) {
        int switchInnerLeft = mSwitchLeft + mTrackPaddingRect.left;
        int switchInnerTop = mSwitchTop + mTrackPaddingRect.top;
        int switchInnerRight = mSwitchRight - mTrackPaddingRect.right;
        int switchInnerBottom = mSwitchBottom - mTrackPaddingRect.bottom;
        int thumbRange =  getThumbScrollRange();
        int thumbPos = (int) (mThumbPosition + 0.5f);

        mTextPaintOn.drawableState = getDrawableState();
        mTextPaintOff.drawableState = getDrawableState();

        if (mOrientation == HORIZONTAL) {
            int thumbL = switchInnerLeft;// + mThPad.left;
            int thumbR = switchInnerLeft + mThumbWidth;// - mThPad.right;
            int dxOffText = mTextOnThumb ? (thumbL + thumbR) / 2
                    - mOffLayout.getWidth() / 2 + mTrackTextPadding
                    - mThumbTextPadding
                    : switchInnerLeft + mTrackTextPadding;

            thumbL = thumbL + thumbRange;
            thumbR = thumbR + thumbRange;
            int dxOnText = mTextOnThumb ? (thumbL + thumbR) / 2 - mOnLayout.getWidth() / 2

                    : switchInnerRight - mOnLayout.getWidth() - mTrackTextPadding;

            int switchVerticalMid = (switchInnerTop + switchInnerBottom) / 2;

            int thumbBoundL = switchInnerLeft + thumbPos - mThumbExtraMovement;// + mThPad.left
            int thumbBoundR = switchInnerLeft + thumbPos + mThumbWidth - mThumbExtraMovement;// - mThPad.right

            if (mPushStyle) {
                final int maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth());
                //tempBitmap = Bitmap.createBitmap(mSwitchRight - mSwitchLeft, mSwitchBottom - mSwitchTop,  Config.ARGB_8888);
                //backingLayer = new Canvas(tempBitmap);
                backingLayer.save();
                backingLayer.translate(-thumbRange + thumbPos,0);
                backingLayer.drawBitmap(pushBitmap, 0, 0, null);
                backingLayer.restore();
                backingLayer.drawBitmap(maskBitmap, 0, 0, xferPaint);
                canvas.drawBitmap(tempBitmap, 0, 0, null);
                mTrackDrawable.draw(canvas);

                backingLayer.drawColor(0x01000000, Mode.DST_IN);
                backingLayer.save();
                backingLayer.translate(-thumbRange + thumbPos,0);
                backingLayer.translate(mTrackPaddingRect.left, 0);
                backingLayer.save();
                backingLayer.translate((maxTextWidth - mOffLayout.getWidth()) / 2, switchVerticalMid - mOffLayout.getHeight() / 2);
                mOffLayout.draw(backingLayer);
                if (mDrawableOff != null) mDrawableOff.draw(backingLayer);
                backingLayer.restore();
                backingLayer.translate(maxTextWidth + mTrackTextPadding * 2 +
                                (maxTextWidth - mOnLayout.getWidth()) / 2 +
                                mThumbWidth,//+ mThPad.left + mThPad.right,
                        switchVerticalMid - mOnLayout.getHeight() / 2);
                mOnLayout.draw(backingLayer);
                if (mDrawableOn != null) mDrawableOn.draw(backingLayer);
                backingLayer.restore();
                backingLayer.drawBitmap(maskBitmap, 0, 0, xferPaint);
                canvas.drawBitmap(tempBitmap, 0, 0, null);

            } else {
                if (rightBitmap != null) {
                    canvas.save();
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.left += (mThumbPosition + mThumbWidth / 2);
                        canvas.clipRect(canvasClipBounds);
                    }
                    canvas.drawBitmap(rightBitmap, 0, 0, null);
                    canvas.restore();
                }

                if (leftBitmap != null) {
                    canvas.save();
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.right -= (thumbRange - mThumbPosition + mThumbWidth / 2);
                        canvas.clipRect(canvasClipBounds);
                    }
                    canvas.drawBitmap(leftBitmap, 0, 0, null);
                    canvas.restore();
                }

                mTextPaintOff.setAlpha(alpha);
                mTrackDrawable.setAlpha(alpha);
                //draw the track
                mTrackDrawable.draw(canvas);

                // evaluate the coordinates for drawing the Thumb and Text
                canvas.save();
                canvas.clipRect(switchInnerLeft, mSwitchTop, switchInnerRight, mSwitchBottom);

                // mTextColors should not be null, but just in case
                if (getTargetCheckedState() && mTextColors != null) {
                    mTextPaintOn.setColor(mTextColors.getColorForState(getDrawableState(), mTextColors.getDefaultColor()));
                    mTextPaintOff.setColor(mTextColorsHighlighted);
                } else if (!getTargetCheckedState() && mTextColors != null) {
                    mTextPaintOn.setColor(mTextColorsHighlighted);
                    mTextPaintOff.setColor(mTextColors.getColorForState(getDrawableState(), mTextColors.getDefaultColor()));
                }

                // draw the texts for On/Off in reduced alpha mode.


                if (getTargetCheckedState()) {
                    canvas.save();
                    canvas.translate(dxOnText, switchVerticalMid - mOnLayout.getHeight() / 2);
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.left += (mThumbPosition + mThumbWidth / 2);
                        canvas.clipRect(canvasClipBounds);
                    }
                    mOnLayout.draw(canvas);
                    if (mDrawableOn != null) mDrawableOn.draw(canvas);
                    canvas.restore();


                    canvas.save();
                    canvas.translate(dxOffText, switchVerticalMid - mOffLayout.getHeight() / 2);
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.right -= (thumbRange - mThumbPosition + mThumbWidth / 2);
                        canvas.clipRect(canvasClipBounds);
                    }


                    mOffLayout.draw(canvas);
                    if (mDrawableOff != null) mDrawableOff.draw(canvas);
                    canvas.restore();
                } else {
                    canvas.save();
                    canvas.translate(dxOffText, switchVerticalMid - mOffLayout.getHeight() / 2);
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.right -= (thumbRange - mThumbPosition + mThumbWidth / 2);
                        canvas.clipRect(canvasClipBounds);
                    }

                    mOffLayout.draw(canvas);
                    if (mDrawableOff != null) mDrawableOff.draw(canvas);
                    canvas.restore();

                    canvas.save();
                    canvas.translate(dxOnText, switchVerticalMid - mOnLayout.getHeight() / 2);
                    if (canvas.getClipBounds(canvasClipBounds)) {
                        canvasClipBounds.left += (mThumbPosition + mThumbWidth / 2);
                        canvas.clipRect(canvasClipBounds);
                    }



                    mOnLayout.draw(canvas);
                    if (mDrawableOn != null) mDrawableOn.draw(canvas);
                    canvas.restore();

                }
                canvas.restore();


            }


            mThumbDrawable.setBounds(thumbBoundL, mSwitchTop, thumbBoundR, mSwitchBottom);
            mThumbDrawable.draw(canvas);

            //Draw the text on the Thumb
            if (mTextOnThumb) {
                Layout onSwitchText = getTargetCheckedState() ? mOnLayout : mOffLayout;
                canvas.save();
                canvas.translate((thumbBoundL + thumbBoundR) / 2 - onSwitchText.getWidth() / 2,
                        (switchInnerTop + switchInnerBottom) / 2 - onSwitchText.getHeight() / 2);

                onSwitchText.draw(canvas);

                canvas.restore();
            }
        }
    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap((int)(drawable.getIntrinsicWidth()),
                (int)(drawable.getIntrinsicHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public BitmapDrawable writeOnDrawable(int drawableId, String text){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, 0, bm.getHeight()/2, paint);

        return new BitmapDrawable(bm);
    }
    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight() + mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    @Override
    public int getCompoundPaddingTop() {
        int padding = super.getCompoundPaddingTop() + mSwitchHeight;
        if (!TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    private int getThumbScrollRange() {
        if (mTrackDrawable == null) {
            return 0;
        }

        int range = 0;
        if (mOrientation == VERTICAL)
            range = mSwitchHeight - mThumbHeight - mTrackPaddingRect.top - mTrackPaddingRect.bottom + mThumbExtraMovement * 2;
        if (mOrientation == HORIZONTAL)
            range = mSwitchWidth - mThumbWidth - mTrackPaddingRect.left - mTrackPaddingRect.right + mThumbExtraMovement * 2;
        if (this.mPushStyle)
            range += this.mTrackTextPadding * 2;
        return range;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        int[] myDrawableState = getDrawableState();

        // Set the state of the Drawable
        // Drawable may be null when checked state is set from XML, from super constructor
        if (mThumbDrawable != null) mThumbDrawable.setState(myDrawableState);
        if (mTrackDrawable != null) mTrackDrawable.setState(myDrawableState);

        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mThumbDrawable || who == mTrackDrawable;
    }
}