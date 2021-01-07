package com.sample.buck.playfeaturedelivery;

import android.os.Bundle
import com.sample.buck.playfeaturedelivery.initialInstall.R

/** This is a simple Activity. */
class InitialInstallActivity : BaseSplitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_install)
    }
}