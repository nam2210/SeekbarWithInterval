package com.hnam.seekbarwithinterval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.hnam.seekbarwithlabel.SeekBarWithLabels;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    SeekBarWithLabels mSeekBarWithIntervals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSeekBarWithIntervals = (SeekBarWithLabels) findViewById(R.id.seekbarInterval);
        List<String> intervals = new ArrayList<>();
        intervals.add("aaa");
        intervals.add("bbb");
        intervals.add("ccc");
        intervals.add("ddd");
        intervals.add("eee");
        mSeekBarWithIntervals.setIntervals(intervals);
        mSeekBarWithIntervals.setOnSeekbarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "value " + seekBar.getProgress());
            }
        });
    }
}
