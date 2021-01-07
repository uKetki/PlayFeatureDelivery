package com.sample.buck.playfeaturedelivery.ondemand;

import android.os.Bundle
import android.widget.TextView
import com.google.android.play.core.splitinstall.SplitInstallHelper
import com.sample.buck.playfeaturedelivery.BaseSplitActivity
import com.sample.buck.playfeaturedelivery.ondemand.ccode.R

/** A simple activity displaying some text coming through via JNI. */
class NativeSampleActivity : BaseSplitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         If you want ot load a native library directly from the split install you'll have to create
         a new context by calling `createPackageContext(packageName, 0)` and pass it into the
         `loadLibrary` call like outlined here:
         https://developer.android.com/guide/app-bundle/playcore#load_native_libs
         */
        SplitInstallHelper.loadLibrary(this, "hello-jni")

        setContentView(R.layout.activity_hello_jni)
        findViewById<TextView>(R.id.hello_textview).text = stringFromJNI()
    }

    /** Read a string from packaged native code. */
    external fun stringFromJNI(): String
}