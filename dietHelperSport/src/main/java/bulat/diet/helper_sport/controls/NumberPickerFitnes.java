/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bulat.diet.helper_sport.controls;

import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;

/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been
 * made public so its included in the project in this fashion for use with the preferences screen; I have made
 * a few slight modifications to the code here, I simply put a MAX and MIN default in the code but these values
 * can still be set publically by calling code.
 *
 * @author Google
 */
public class NumberPickerFitnes extends LinearLayout implements OnClickListener,
OnFocusChangeListener, OnLongClickListener {

    private static final String TAG = "NumberPicker";
    private static final int DEFAULT_MAX = 3000;
    private static final int DEFAULT_MIN = 0;

    public interface OnChangedListener {
        void onChanged(NumberPickerFitnes picker, int oldVal, int newVal);
    }

    public interface Formatter {
        String toString(int value);
    }

    /*
     * Use a custom NumberPicker formatting callback to use two-digit
     * minutes strings like "01".  Keeping a static formatter etc. is the
     * most efficient way to do this; it avoids creating temporary objects
     * on every call to format().
     */
    public static final NumberPickerFitnes.Formatter TWO_DIGIT_FORMATTER =
            new NumberPickerFitnes.Formatter() {
                final StringBuilder mBuilder = new StringBuilder();
                final java.util.Formatter mFmt = new java.util.Formatter(mBuilder);
                final Object[] mArgs = new Object[1];
                public String toString(int value) {
                    mArgs[0] = value;
                    mBuilder.delete(0, mBuilder.length());
                    mFmt.format("%02d", mArgs);
                    return mFmt.toString();
                }
        };

    private final Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if (mIncrement) {
                changeCurrent(mCurrent + 1);
                mHandler.postDelayed(this, mSpeed);
            } else if (mDecrement) {
                changeCurrent(mCurrent - 1);
                mHandler.postDelayed(this, mSpeed);
            }
        }
    };

    private final EditText mText;
    private final InputFilter mNumberInputFilter;

    private String[] mDisplayedValues;
    protected int mStart;
    protected int mEnd;
    protected int mCurrent;
    protected int mPrevious;
    private OnChangedListener mListener;
    private Formatter mFormatter;
    private long mSpeed = 300;

    private boolean mIncrement;
    private boolean mDecrement;

    public NumberPickerFitnes(Context context) {
        this(context, null);
    }

    public NumberPickerFitnes(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public NumberPickerFitnes(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.number_picker_fitnes, this, true);
        mHandler = new Handler();
        InputFilter inputFilter = new NumberPickerInputFilter();
        mNumberInputFilter = new NumberRangeKeyListener();
        mIncrementButton = (NumberPickerButtonFitnes) findViewById(R.id.increment_fit);
        mIncrementButton.setOnClickListener(this);
        mIncrementButton.setOnLongClickListener(this);
        mIncrementButton.setNumberPicker(this);
        mDecrementButton = (NumberPickerButtonFitnes) findViewById(R.id.decrement_fit);
        mDecrementButton.setOnClickListener(this);
        mDecrementButton.setOnLongClickListener(this);
        mDecrementButton.setNumberPicker(this);

               
        mText = (EditText) findViewById(R.id.timepicker_input_fit);
        mText.setOnFocusChangeListener(this);
        mText.setFilters(new InputFilter[] {inputFilter});
        mText.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        if (!isEnabled()) {
            setEnabled(false);
        }

        mStart = DEFAULT_MIN;
        mEnd = DEFAULT_MAX;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mIncrementButton.setEnabled(enabled);
        mDecrementButton.setEnabled(enabled);
        mText.setEnabled(enabled);
    }

    public void onClick(View v) {
        validateInput(mText);
        if (!mText.hasFocus()) mText.requestFocus();

        // now perform the increment/decrement
        if (R.id.increment_fit == v.getId()) {
            changeCurrent(mCurrent + 10);
        } else if (R.id.decrement_fit == v.getId()) {
            changeCurrent(mCurrent - 10);
        }
    }

    private String formatNumber(int value) {
        return (mFormatter != null)
                ? mFormatter.toString(value)
                : String.valueOf(value);
    }

    protected void changeCurrent(int current) {

        // Wrap around the values if we go past the start or end
        if (current > mEnd) {
            current = mStart;
        } else if (current < mStart) {
            current = mEnd;
        }
        mPrevious = mCurrent;
        mCurrent = current;

        notifyChange();
        updateView();
    }

    protected void notifyChange() {
        if (mListener != null) {
            mListener.onChanged(this, mPrevious, mCurrent);
        }
    }

    protected void updateView() {

        /* If we don't have displayed values then use the
         * current number else find the correct value in the
         * displayed values for the current number.
         */
        if (mDisplayedValues == null) {
            mText.setText(formatNumber(mCurrent));
        } else {
            mText.setText(mDisplayedValues[mCurrent - mStart]);
        }
        mText.setSelection(mText.getText().length());
    }

    private void validateCurrentView(CharSequence str) {
        int val = getSelectedPos(str.toString());
        if ((val >= mStart) && (val <= mEnd)) {
            if (mCurrent != val) {
                mPrevious = mCurrent;
                mCurrent = val;
                notifyChange();
            }
        }
        updateView();
    }

    public void onFocusChange(View v, boolean hasFocus) {

        /* When focus is lost check that the text field
         * has valid values.
         */
        if (!hasFocus) {
            validateInput(v);
        }
    }

    private void validateInput(View v) {
        String str = String.valueOf(((TextView) v).getText());
        if ("".equals(str)) {

            // Restore to the old value as we don't allow empty values
            updateView();
        } else {

            // Check the new value and ensure it's in range
            validateCurrentView(str);
        }
    }

    /**
     * We start the long click here but rely on the {@link NumberPickerButton}
     * to inform us when the long click has ended.
     */
    public boolean onLongClick(View v) {

        /* The text view may still have focus so clear it's focus which will
         * trigger the on focus changed and any typed values to be pulled.
         */
        mText.clearFocus();

        if (R.id.increment == v.getId()) {
            mIncrement = true;
            mHandler.post(mRunnable);
        } else if (R.id.decrement == v.getId()) {
            mDecrement = true;
            mHandler.post(mRunnable);
        }
        return true;
    }

    public void cancelIncrement() {
        mIncrement = false;
    }

    public void cancelDecrement() {
        mDecrement = false;
    }

    private static final char[] DIGIT_CHARACTERS = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private NumberPickerButtonFitnes mIncrementButton;
    private NumberPickerButtonFitnes mDecrementButton;

    private class NumberPickerInputFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) {
            if (mDisplayedValues == null) {
                return mNumberInputFilter.filter(source, start, end, dest, dstart, dend);
            }
            CharSequence filtered = String.valueOf(source.subSequence(start, end));
            String result = String.valueOf(dest.subSequence(0, dstart))
                    + filtered
                    + dest.subSequence(dend, dest.length());
            String str = String.valueOf(result).toLowerCase();
            for (String val : mDisplayedValues) {
                val = val.toLowerCase();
                if (val.startsWith(str)) {
                    return filtered;
                }
            }
            return "";
        }
    }

    private class NumberRangeKeyListener extends NumberKeyListener {

        // XXX This doesn't allow for range limits when controlled by a
        // soft input method!
        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER;
        }

        @Override
        protected char[] getAcceptedChars() {
            return DIGIT_CHARACTERS;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) {

            CharSequence filtered = super.filter(source, start, end, dest, dstart, dend);
            if (filtered == null) {
                filtered = source.subSequence(start, end);
            }

            String result = String.valueOf(dest.subSequence(0, dstart))
                    + filtered
                    + dest.subSequence(dend, dest.length());

            if ("".equals(result)) {
                return result;
            }
            int val = getSelectedPos(result);

            /* Ensure the user can't type in a value greater
             * than the max allowed. We have to allow less than min
             * as the user might want to delete some numbers
             * and then type a new number.
             */
            if (val > mEnd) {
                return "";
            } else {
                return filtered;
            }
        }
    }

    private int getSelectedPos(String str) {
        if (mDisplayedValues == null) {
            return Integer.parseInt(str);
        } else {
            for (int i = 0; i < mDisplayedValues.length; i++) {

                /* Don't force the user to type in jan when ja will do */
                str = str.toLowerCase();
                if (mDisplayedValues[i].toLowerCase().startsWith(str)) {
                    return mStart + i;
                }
            }

            /* The user might have typed in a number into the month field i.e.
             * 10 instead of OCT so support that too.
             */
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {

                /* Ignore as if it's not a number we don't care */
            }
        }
        return mStart;
    }

}
