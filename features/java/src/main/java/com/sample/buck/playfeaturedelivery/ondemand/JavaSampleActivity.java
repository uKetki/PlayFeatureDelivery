package com.sample.buck.playfeaturedelivery.ondemand;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.sample.buck.playfeaturedelivery.BaseSplitActivity;
import com.sample.buck.playfeaturedelivery.ondemand.java.R;

/** A simple activity displaying text written in Java. */
public class JavaSampleActivity extends BaseSplitActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_java);
    }

}
