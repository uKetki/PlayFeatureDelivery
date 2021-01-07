package com.sample.buck.playfeaturedelivery.ondemand;

import android.os.Bundle
import com.sample.buck.playfeaturedelivery.BaseSplitActivity
import com.sample.buck.playfeaturedelivery.ondemand.kotlin.R

/** A simple Activity displaying some text, written in Kotlin. */
class KotlinSampleActivity : BaseSplitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_kotlin)
    }
}
