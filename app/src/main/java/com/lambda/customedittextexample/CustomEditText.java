package com.lambda.customedittextexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomEditText extends LinearLayout {

    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.tv_information)
    TextView tvHelpText;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.et_input)
    EditText etInput;

    @IntDef({TEXT, NUMBER})
    public @interface CustomInputType {}

    public static final int TEXT = 0;
    public static final int NUMBER = 1;
    private static final String TAG = CustomEditText.class.getSimpleName();

    private Context mContext;

    // To show the label
    private String mLabelText;

    // To show the info on pressing the i button
    private String mHelpText;

    // To show the message, if any mistake is there
    private String mMessageText;

    // Check to see if the current field is required or not
    private boolean mIsRequired;

    // Check if info button is required or not
    private boolean mIsInfoImageRequired;

    // Input mode to set the Keyboard type
    private int mInputType;

    //Constructors to be implemented

    public CustomEditText(Context context) {
        super(context);
        init(context, null);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // Since this works only for API level >= 21
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * This method is used to initialize the variables and inflate the layout
     *
     * @param context      The context of the view it is in
     * @param attributeSet The attributes that are being passed by the xml
     */
    private void init(Context context, AttributeSet attributeSet) {
        mContext = context;
        TypedArray mTypedArray = mContext.obtainStyledAttributes(attributeSet, R.styleable.CustomEditText);
        mLabelText = mTypedArray.getString(R.styleable.CustomEditText_label);
        mHelpText = mTypedArray.getString(R.styleable.CustomEditText_helpText);
        mMessageText = mTypedArray.getString(R.styleable.CustomEditText_messageText);
        mIsRequired = mTypedArray.getBoolean(R.styleable.CustomEditText_required, false);
        mIsInfoImageRequired = mTypedArray.getBoolean(R.styleable.CustomEditText_displayInfoButton, true);
        mInputType = mTypedArray.getInt(R.styleable.CustomEditText_inputType, TEXT);

        ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.sample_view, this);
        // Don't forget to recycle TypedArray
        mTypedArray.recycle();
    }

    /**
     * This method is called after the xml layout has finished inflating
     * We need to set up most of the business logic here
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUpViews();
    }

    private void setUpViews() {
        tvLabel.setText(mLabelText);
        if (mHelpText != null)
            tvHelpText.setText(mHelpText);
        if (mMessageText != null)
            tvMessage.setText(mMessageText);
        ivInfo.setVisibility(mIsInfoImageRequired ? VISIBLE : GONE);
        if (ivInfo.getVisibility() == VISIBLE) {
            ivInfo.setOnClickListener(showHelp());
        }
        setUpKeyBoard(mInputType);
        //etInput.addTextChangedListener(watchText());
        etInput.setOnFocusChangeListener(checkFocus());
    }

    /**
     * Handles the click events on the i button
     * @return An OnClickListener
     */
    private OnClickListener showHelp() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mContext;
                if (activity != null && !activity.isFinishing()) {
                    activity.showPopup();
                }
            }
        };
    }

    /**
     * This method takes care of the keyboard type to be displayed while typing on the EditText
     * @param inputType An integer representing either TEXT or NUMBER
     */
    private void setUpKeyBoard(@CustomInputType int inputType) {
        if (inputType == NUMBER) {
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private boolean getIsRequired() {
        return mIsRequired;
    }

    public void setEditTextFilters(InputFilter ... inputFilters) {
        etInput.setFilters(inputFilters);
    }

    public void showMessageText() {
        tvMessage.setVisibility(VISIBLE);
        tvMessage.setTextColor(Color.RED);
    }

    public void hideMessageText() {
        tvMessage.setVisibility(GONE);
    }

    private TextWatcher watchText() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                showMessageText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private OnFocusChangeListener checkFocus() {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showMessageText();
                } else {
                    hideMessageText();
                }
            }
        };
    }

}
