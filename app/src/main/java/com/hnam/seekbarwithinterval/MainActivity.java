package com.hnam.seekbarwithinterval;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hnam.seekbarwithlabel.SeekBarWithLabels;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        mSeekBarWithIntervals.setIntervals(intervals);
    }
}
