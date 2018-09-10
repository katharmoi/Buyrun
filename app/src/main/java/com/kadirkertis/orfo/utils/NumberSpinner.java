package com.kadirkertis.orfo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kadirkertis.orfo.R;


/**
 * Created by Kadir Kertis on 1.3.2017.
 */

public class NumberSpinner extends LinearLayout {

    private ImageButton mIncBtn;
    private ImageButton mDecBtn;
    private TextView mResultText;
    private int mValue = 1;
    private int mMaxValue=100;
    private OnValueChangeListener mListener;

    private static final String SELECTED_VALUE ="selectedValue";
    private static final String SUPER_CLASS_STATE ="superState";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(SUPER_CLASS_STATE,super.onSaveInstanceState());
        bundle.putInt(SELECTED_VALUE,mValue);
        Log.d("SAVE E GIRDIIII","SAVEEE GIRDI");
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle =(Bundle)state;

            super.onRestoreInstanceState(bundle.getParcelable(SUPER_CLASS_STATE));
            setValue(bundle.getInt(SELECTED_VALUE));
            Log.d("Degeri set etti","Deger " +Integer.toString(bundle.getInt(SELECTED_VALUE)));
        }else
            super.onRestoreInstanceState(state);
    }


    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    public NumberSpinner(Context context) {
        super(context);
        initializeSpinner(context);
    }

    public NumberSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.NumberSpinner);
        try {
            mMaxValue = typedArray.getInteger(R.styleable.NumberSpinner_maxValue,100);
        }finally {
            typedArray.recycle();
        }
        initializeSpinner(context);
    }

    public NumberSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.NumberSpinner);
        try {
            mMaxValue = typedArray.getInteger(R.styleable.NumberSpinner_maxValue,100);
        }finally {
            typedArray.recycle();
        }
        initializeSpinner(context);
    }

    private void initializeSpinner(Context context) {
        LayoutInflater inflator = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.number_spinner,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIncBtn = this.findViewById(R.id.spinner_increment);
        mDecBtn = this.findViewById(R.id.spinner_decrement);
        mResultText = this.findViewById(R.id.spinner_current_value);
        mResultText.setText(Integer.toString(mValue));

        mIncBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mValue < mMaxValue){
                    mValue++;
                    mResultText.setText(Integer.toString(mValue));
                    notifyListener(mValue);
                    invalidate();
                    requestLayout();
                }

            }
        });

        mDecBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mValue > 1){
                    mValue--;
                    mResultText.setText(Integer.toString(mValue));
                    notifyListener(mValue);
                    invalidate();
                    requestLayout();
                }
            }
        });

    }

    public void setOnValueChangeListener(OnValueChangeListener listener){
        if(mListener == null){
            mListener = listener;
        }
    }

    public void removeOnValueChangeListener(){
        if(mListener != null){
            mListener = null;
        }
    }

    private void notifyListener(int value){
        if(mListener != null){
            mListener.onChange(value);
        }
    }

    public int getValue(){
        return mValue;
    }

    public void setValue(int value){
        mValue = value;
        mResultText.setText(Integer.toString(value));
//        invalidate();
//        requestLayout();
    }

    public interface OnValueChangeListener{
        void onChange(int value);
    }
}
