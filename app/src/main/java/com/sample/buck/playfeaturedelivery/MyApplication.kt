package com.sample.buck.playfeaturedelivery

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat

/** We have to use a custom Application class, because we want to initialize SplitCompat#install() */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}