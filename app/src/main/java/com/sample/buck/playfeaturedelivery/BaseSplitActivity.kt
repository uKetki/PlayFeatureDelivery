package com.sample.buck.playfeaturedelivery

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat

/**
 * This base activity unifies calls to attachBaseContext as described in:
 * https://developer.android.com/guide/app-bundle/playcore#invoke_splitcompat_at_runtime
 */
abstract class BaseSplitActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}