package com.lambda.customedittextexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Context mContext;
    private AttributeSet mAttrs;
    private TypedArray mTypedArray;

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
     * @param context The context of the view it is in
     * @param attributeSet The attributes that are being passed by the xml
     */
    private void init(Context context, AttributeSet attributeSet) {
        mContext = context;
        mAttrs = attributeSet;
        mTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.CustomEditText);
        mLabelText = mTypedArray.getString(R.styleable.CustomEditText_label);
        mHelpText = mTypedArray.getString(R.styleable.CustomEditText_helpText);
        mMessageText = mTypedArray.getString(R.styleable.CustomEditText_messageText);
        mIsRequired = mTypedArray.getBoolean(R.styleable.CustomEditText_required, false);
        mIsInfoImageRequired = mTypedArray.getBoolean(R.styleable.CustomEditText_displayInfoButton, true);
        mInputType = mTypedArray.getInt(R.styleable.CustomEditText_inputType, TEXT);

        ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
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
        tvHelpText.setText(mHelpText);
        tvMessage.setText(mMessageText);
        ivInfo.setVisibility(mIsInfoImageRequired ? VISIBLE : GONE);
        if (ivInfo.getVisibility() == VISIBLE) {
            ivInfo.setOnClickListener(showHelp());
        }
        setUpKeyBoard(mInputType);
    }

    private OnClickListener showHelp() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mContext;
                if (activity != null && !activity.isFinishing()) {
                    activity.showPopup((View) v.getParent());
                }
            }
        };
    }

    private void setUpKeyBoard(@CustomInputType int inputType) {
        if (inputType == NUMBER) {
            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            etInput.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }
}
