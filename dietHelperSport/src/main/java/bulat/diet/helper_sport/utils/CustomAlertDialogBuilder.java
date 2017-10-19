package bulat.diet.helper_sport.utils;

import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.DishType;

import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.ColorDrawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;
import java.util.TreeMap;

/**
 * Alert dialog builder for custom application dialogs
 */
public class CustomAlertDialogBuilder extends AlertDialog.Builder {
    private static final String TAG = "CustomAlertDialogBuilder";

    private Activity context;

    protected CharSequence title;
    protected CharSequence message;
    protected View rootView;
    protected TextView titleView;
    protected TextView messageView;
    protected ListView listView;

    public static final String FIRST_VALUE = "FIRST_VALUE";
    public static final String SECOND_VALUE = "SECOND_VALUE";

    protected EditText inputView;

    protected int neutralButtonId;
    protected String neutralButtonText;
    protected OnClickListener neutralButtonListener;

    protected int positiveButtonId;
    protected String positiveButtonText;
    protected Map<String, String> dialogValue = new TreeMap<>();
    protected OnClickListener positiveButtonListener;

    protected int negativeButtonId;
    protected String negativeButtonText;
    protected OnClickListener negativeButtonListener;

    protected AlertDialog dialog;

    protected boolean canceledOnTouchOutside = true;
    private boolean isAutoClose = true;
    private DialogValueListener dialogValueListener;

    /**
     * Class constructor.
     *
     * @param context Parent activity for dialog
     */
    public CustomAlertDialogBuilder(Activity context) {
        super(context);
        this.context = context;
    }
    
    public CustomAlertDialogBuilder(Activity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    public CustomAlertDialogBuilder setTitle(int titleId) {
        this.title = context.getResources().getText(titleId);
        return this;
    }

    @Override
    public CustomAlertDialogBuilder setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public AlertDialog getDialog() {

        return dialog;
    }


    public CustomAlertDialogBuilder setAutoCLose(boolean flag) {
        this.isAutoClose = flag;
        return this;
    }


    @Override
    public CustomAlertDialogBuilder setMessage(int messageId) {
        this.message = context.getResources().getText(messageId);
        return this;
    }

    @Override
    public CustomAlertDialogBuilder setMessage(CharSequence message) {
        this.message = message.toString().trim();
        return this;
    }

    @Override
    public CustomAlertDialogBuilder setView(View view) {
        super.setView(view);
        this.rootView = view;
        titleView = (TextView) view.findViewById(R.id.dialogTitle);
        messageView = (TextView) view.findViewById(R.id.dialogMessage);
        listView = (ListView) view.findViewById(R.id.dialog_listview);
        inputView = (EditText) view.findViewById(R.id.dialog_edittext);
        return this;
    }

    public EditText getInputView() {
        return inputView;
    }

    @Override
    public AlertDialog show() {
        if (messageView != null) {
            messageView.setText(message);
        }
        if (title == null || title.length() == 0) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setText(title);
        }
        setButton(neutralButtonId, neutralButtonListener, null, neutralButtonText, isAutoClose);
        setButton(positiveButtonId, positiveButtonListener, dialogValueListener, positiveButtonText, isAutoClose);
        setButton(negativeButtonId, negativeButtonListener, null, negativeButtonText, isAutoClose);
        dialog = super.show();
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public CustomAlertDialogBuilder setCanceledOnTouchOutside(boolean cancel) {
        canceledOnTouchOutside = cancel;
        return this;
    }

    @Override
    public CustomAlertDialogBuilder setOnCancelListener(OnCancelListener onCancelListener) {
        super.setOnCancelListener(onCancelListener);
        return this;
    }

    @Override
    public AlertDialog create() {        
        dialog = super.create();
        return dialog;
    }

    public CustomAlertDialogBuilder setNeutralButton(int buttonId,
                                                     OnClickListener onClickListener) {
        neutralButtonId = buttonId;
        neutralButtonListener = onClickListener;
        return this;
    }

    public CustomAlertDialogBuilder setPositiveButton(int buttonId,
                                                      DialogValueListener onDialogValueListener, OnClickListener onClickListener) {
        positiveButtonId = buttonId;
        positiveButtonListener = onClickListener;
        dialogValueListener = onDialogValueListener;
        return this;
    }

    public CustomAlertDialogBuilder setNegativeButton(int buttonId,
                                                      OnClickListener onClickListener) {
        negativeButtonId = buttonId;
        negativeButtonListener = onClickListener;
        return this;
    }

    public CustomAlertDialogBuilder setListView(final OnClickListener listener, ArrayAdapter<DishType> adapter) {
        if (listener != null) {

            ListView listView = (ListView) rootView.findViewById(R.id.dialog_listview);
            if (listView != null) {
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listener.onClick(view);
                        dialog.dismiss();
                    }
                });
            }
        }
        return this;
    }

    /**
     * Set a text to the neutral button in dialog
     *
     * @param neutralButtonTextId Id of String resource corresponds to wished text
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public CustomAlertDialogBuilder setNeutralButtonText(int neutralButtonTextId) {
        this.neutralButtonText = context.getResources().getString(neutralButtonTextId);
        return this;
    }

    /**
     * Set text on positive button from resource with given Id
     *
     * @param neutralButtonTextId
     * @return
     */
    public CustomAlertDialogBuilder setPositiveButtonText(int positiveButtonTextId) {
        this.positiveButtonText = context.getResources().getString(positiveButtonTextId);
        return this;
    }

    /**
     * Set text on negative button from resource with given Id
     *
     * @param neutralButtonTextId
     * @return
     */
    public CustomAlertDialogBuilder setNegativeButtonText(int negativeButtonTextId) {
        this.negativeButtonText = context.getResources().getString(negativeButtonTextId);
        return this;
    }

    protected void setButton(int id, final OnClickListener listener, final DialogValueListener valueListener, final String text, final boolean isAutoClose) {
        if (listener != null) {
            final Button button = (Button) rootView.findViewById(id);
            if (button != null) {
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (dialogValue != null) {
                            if (valueListener != null) {
                                valueListener.onNewDialogValue(dialogValue);
                            }
                            listener.onClick(v);

                            if (isAutoClose) {
                                dialog.dismiss();
                            }
                        }
                    }
                });
                if (text != null) {
                    button.setText(text);
                }
            }
        }
    }

    /**
     * Set layout used for inflating in dialog
     *
     * @param layoutId
     * @return
     */
    public CustomAlertDialogBuilder setLayout(int layoutId) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialog = inflater.inflate(
                layoutId,
                (ViewGroup) context.findViewById(R.id.dialogRoot));
        setView(dialog);
        return this;
    }
    public interface DialogValueListener {
        void onNewDialogValue(Map<String,String> value);
    }
}
