package com.hnam.seekbarwithlabel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hnam on 12/15/2016.
 */

public class SeekBarWithLabels extends LinearLayout {

    private static final String TAG = SeekBarWithLabels.class.getSimpleName();
    private RelativeLayout mRelativeLayout = null;
    private SeekBar mSeekBar;

    private boolean isIntervals = false;
    private int max = 0;

    public SeekBarWithLabels(Context context) {
        super(context);
    }

    public SeekBarWithLabels(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarWithLabels);
        if (a != null){
            isIntervals = a.getBoolean(R.styleable.SeekBarWithLabels_isIntervals, false);
            max = a.getInteger(R.styleable.SeekBarWithLabels_maxProgress, 100);
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getActivity().getLayoutInflater().inflate(R.layout.layout_seek_bar_interval, this);
    }

    private int mWidthMeasureSpec = 0;
    private int mHeightMeasureSpec = 1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        //dieu chinh kich thuoc that te hien len man hinh
        super.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            alignIntervals();

            //we've changed the intervals layout, we need to refresh
            mRelativeLayout.measure(mWidthMeasureSpec, mHeightMeasureSpec);
            mRelativeLayout.layout(mRelativeLayout.getLeft(), mRelativeLayout.getTop(),
                    mRelativeLayout.getRight(), mRelativeLayout.getBottom());

        }
    }

    public void setIntervals(List<String> intervals) {
        numberOfIntervals = intervals.size();
        displayIntervals(intervals);
        if (isIntervals) {
            getSeekBar().setMax(intervals.size()-1);
        } else {
            getSeekBar().setMax(max);
        }
    }

    public void setOnSeekbarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        mSeekBar.setOnSeekBarChangeListener(listener);
    }


    private void alignIntervals() {
        //the location of the first interval is too far left - it needs to start at the middle of the thumb
        //get seekbarThumb width
        int widthOfSeekBarThumb = getSeekBarThumbWidth();
        int offset = widthOfSeekBarThumb / 2;
        //add align to interval
        alignFirstInterval(offset);

        //align the intervals in between the first and last interval
        int widthOfSeekBar = getSeekBar().getWidth();
        int remainingPaddableWidth = widthOfSeekBar - widthOfSeekBarThumb * 2;
        int maximumWidthOfEachInterval = remainingPaddableWidth / (numberOfIntervals-1);
        Log.e(TAG, "widthSeekBar " + widthOfSeekBar);
        alignIntervalsInBetween(maximumWidthOfEachInterval);

        alignLastInterval(offset, maximumWidthOfEachInterval);
    }

    private void alignIntervalsInBetween(int maximunWidthOfEachInterval) {
        TextView firstIntervals = (TextView) getRelativeLayout().getChildAt(0);
        int widthOfPreviousIntervalsText = firstIntervals.getWidth();

        //don't align the first or last interval
        for(int index = 1; index < (getRelativeLayout().getChildCount() - 1); index++) {
            TextView tvInterval = (TextView) getRelativeLayout().getChildAt(index);
            int widthOfText = tvInterval.getMeasuredWidth();

            int leftPadding = Math.round(maximunWidthOfEachInterval - (widthOfText/2) - widthOfPreviousIntervalsText/2);
            tvInterval.setPadding(leftPadding, 0 , 0 , 0);
            widthOfPreviousIntervalsText = widthOfText;
        }
    }

    private void alignLastInterval(int offset, int maximumWidthOfEachInterval) {
        int lastIndex = getRelativeLayout().getChildCount() - 1;
        TextView lastInterval = (TextView) getRelativeLayout().getChildAt(lastIndex);
        int widthOfText = lastInterval.getWidth();

        int leftPadding = Math.round(maximumWidthOfEachInterval - widthOfText);
        lastInterval.setPadding(leftPadding, 0 ,0 ,0);
    }



    private int getSeekBarThumbWidth(){
        return getResources().getDimensionPixelOffset(R.dimen.seekbar_thumb_width);
    }

    private void alignFirstInterval(int offset){
        TextView firstInterval = (TextView) getRelativeLayout().getChildAt(0);
        firstInterval.setPadding(offset, 0, 0 ,0);

    }

    private Activity getActivity(){
        return (Activity) getContext();
    }

    private RelativeLayout getRelativeLayout(){
        if (mRelativeLayout == null) {
            mRelativeLayout = (RelativeLayout) findViewById(R.id.intervals);
        }
        return mRelativeLayout;
    }

    private SeekBar getSeekBar(){
        if (mSeekBar == null) {
            mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        }
        return mSeekBar;
    }

    private int numberOfIntervals = 0;

    private void displayIntervals(List<String> intervals) {
        int idOfPreviousInterval = 0;

        if (getRelativeLayout().getChildCount() == 0) {
            for (String interval : intervals) {
                TextView textViewInterval = createInterval(interval);
                alignTextViewToRightOfPreviousInterval(textViewInterval, idOfPreviousInterval);
                idOfPreviousInterval = textViewInterval.getId();
                getRelativeLayout().addView(textViewInterval);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private TextView createInterval(String interval) {
        View textBoxView =
                LayoutInflater.from(getContext()).inflate(R.layout.seekbar_with_intervals_labels, null);
        TextView textView = (TextView) textBoxView.findViewById(R.id.textViewInterval);
        textView.setId(View.generateViewId());
        textView.setText(interval);
        return textView;
    }

    private void alignTextViewToRightOfPreviousInterval(TextView textViewInterval, int idOfPreviousInterval) {
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (idOfPreviousInterval > 0) {
            params.addRule(RelativeLayout.RIGHT_OF, idOfPreviousInterval);
        }
        textViewInterval.setLayoutParams(params);
    }
}
