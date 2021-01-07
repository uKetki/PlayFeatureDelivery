package com.sample.buck.playfeaturedelivery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class MainActivity : BaseSplitActivity() {
    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        val multiInstall = state.moduleNames().size > 1

        val names = state.moduleNames().joinToString(" - ")

        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.
                displayLoadingState(state, getString(R.string.downloading, names))
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                /*
                  This may occur when attempting to download a sufficiently large module.

                  In order to see this, the application has to be uploaded to the Play Store.
                  Then features can be requested until the confirmation path is triggered.
                 */
                manager.startConfirmationDialogForResult(state, this, CONFIRMATION_REQUEST_CODE)
            }
            SplitInstallSessionStatus.INSTALLED -> {
                onSuccessfulLoad(names, launch = !multiInstall)
            }

            SplitInstallSessionStatus.INSTALLING -> displayLoadingState(
                state,
                getString(R.string.installing, names)
            )
            SplitInstallSessionStatus.FAILED -> {
                toastAndLog(getString(R.string.error_for_module, state.errorCode(),
                    state.moduleNames()))
            }
        }
    }

    /** This is needed to handle the result of the manager.startConfirmationDialogForResult
    request that can be made from SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION
    in the listener above. */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONFIRMATION_REQUEST_CODE) {
            // Handle the user's decision. For example, if the user selects "Cancel",
            // you may want to disable certain functionality that depends on the module.
            if (resultCode == Activity.RESULT_CANCELED) {
                toastAndLog(getString(R.string.user_cancelled))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val moduleKotlin by lazy { getString(R.string.module_feature_kotlin) }
    private val moduleJava by lazy { getString(R.string.module_feature_java) }
    private val moduleNative by lazy { getString(R.string.module_native) }
    private val moduleInitial by lazy { getString(R.string.module_initial) }

    // Modules to install through installAll functions.
    private val installableModules by lazy {
        listOf(moduleKotlin, moduleJava, moduleNative)
    }

    private val clickListener by lazy {
        View.OnClickListener {
            when (it.id) {
                R.id.btn_load_kotlin -> loadAndLaunchModule(moduleKotlin)
                R.id.btn_load_java -> loadAndLaunchModule(moduleJava)
                R.id.btn_load_native -> loadAndLaunchModule(moduleNative)
                R.id.btn_install_all_now -> installAllFeaturesNow()
                R.id.btn_install_all_deferred -> installAllFeaturesDeferred()
                R.id.btn_request_uninstall -> requestUninstall()
                R.id.btn_launch_initial_install -> loadAndLaunchModule(moduleInitial)
            }
        }
    }

    private lateinit var manager: SplitInstallManager

    private lateinit var progress: Group
    private lateinit var buttons: Group
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.app_name)
        setContentView(R.layout.activity_main)
        manager = SplitInstallManagerFactory.create(this)
        initializeViews()
    }

    override fun onResume() {
        // Listener can be registered even without directly triggering a download.
        manager.registerListener(listener)
        super.onResume()
    }

    override fun onPause() {
        // Make sure to dispose of the listener once it's no longer needed.
        manager.unregisterListener(listener)
        super.onPause()
    }

    /**
     * Load a feature by module name.
     * @param name The name of the feature module to load.
     */
    private fun loadAndLaunchModule(name: String) {
        updateProgressMessage(getString(R.string.loading_module, name))
        // Skip loading if the module already is installed. Perform success action directly.
        if (manager.installedModules.contains(name)) {
            updateProgressMessage(getString(R.string.already_installed))
            onSuccessfulLoad(name, launch = true)
            return
        }

        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder()
            .addModule(name)
            .build()

        // Load and install the requested feature module.
        manager.startInstall(request)

        updateProgressMessage(getString(R.string.starting_install_for, name))
    }

    /** Install all features but do not launch any of them. */
    private fun installAllFeaturesNow() {
        // Request all known modules to be downloaded in a single session.
        val requestBuilder = SplitInstallRequest.newBuilder()

        installableModules.forEach { name ->
            if (!manager.installedModules.contains(name)) {
                requestBuilder.addModule(name)
            }
        }

        val request = requestBuilder.build()

        manager.startInstall(request).addOnSuccessListener {
            toastAndLog("Loading ${request.moduleNames}")
        }.addOnFailureListener {
            toastAndLog("Failed loading ${request.moduleNames}")
        }
    }

    /** Install all features deferred. */
    private fun installAllFeaturesDeferred() {
        manager.deferredInstall(installableModules).addOnSuccessListener {
            toastAndLog("Deferred installation of $installableModules")
        }
    }

    /** Request uninstall of all features. */
    private fun requestUninstall() {
        toastAndLog("Requesting uninstall of all modules." +
                "This will happen at some point in the future.")

        val installedModules = manager.installedModules.toList()
        manager.deferredUninstall(installedModules).addOnSuccessListener {
            toastAndLog("Uninstalling $installedModules")
        }.addOnFailureListener {
            toastAndLog("Failed installation of $installedModules")
        }
    }

    /**
     * Define what to do once a feature module is loaded successfully.
     * @param moduleName The name of the successfully loaded module.
     * @param launch `true` if the feature module should be launched, else `false`.
     */
    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        if (launch) {
            when (moduleName) {
                moduleKotlin -> launchActivity(KOTLIN_SAMPLE_CLASSNAME)
                moduleJava -> launchActivity(JAVA_SAMPLE_CLASSNAME)
                moduleNative -> launchActivity(NATIVE_SAMPLE_CLASSNAME)
                moduleInitial -> launchActivity(INITIAL_INSTALL_CLASSNAME)
            }
        }

        displayButtons()
    }

    /** Launch an activity by its class name. */
    private fun launchActivity(className: String) {
        val intent = Intent().setClassName(BuildConfig.APPLICATION_ID, className)
        startActivity(intent)
    }

    /** Display a loading state to the user. */
    private fun displayLoadingState(state: SplitInstallSessionState, message: String) {
        displayProgress()

        progressBar.max = state.totalBytesToDownload().toInt()
        progressBar.progress = state.bytesDownloaded().toInt()

        updateProgressMessage(message)
    }

    /** Set up all view variables. */
    private fun initializeViews() {
        buttons = findViewById(R.id.buttons)
        progress = findViewById(R.id.progress)
        progressBar = findViewById(R.id.progress_bar)
        progressText = findViewById(R.id.progress_text)
        setupClickListener()
    }

    /** Set all click listeners required for the buttons on the UI. */
    private fun setupClickListener() {
        setClickListener(R.id.btn_load_kotlin, clickListener)
        setClickListener(R.id.btn_load_java, clickListener)
        setClickListener(R.id.btn_load_native, clickListener)
        setClickListener(R.id.btn_install_all_now, clickListener)
        setClickListener(R.id.btn_install_all_deferred, clickListener)
        setClickListener(R.id.btn_request_uninstall, clickListener)
        setClickListener(R.id.btn_launch_initial_install, clickListener)
    }

    private fun setClickListener(id: Int, listener: View.OnClickListener) {
        findViewById<View>(id).setOnClickListener(listener)
    }

    private fun updateProgressMessage(message: String) {
        if (progress.visibility != View.VISIBLE) displayProgress()
        progressText.text = message
    }

    /** Display progress bar and text. */
    private fun displayProgress() {
        progress.visibility = View.VISIBLE
        buttons.visibility = View.GONE
    }

    /** Display buttons to accept user input. */
    private fun displayButtons() {
        progress.visibility = View.GONE
        buttons.visibility = View.VISIBLE
    }
}

fun MainActivity.toastAndLog(text: String) {
    android.widget.Toast.makeText(this, text, android.widget.Toast.LENGTH_LONG).show()
    android.util.Log.d(TAG, text)
}

private const val PACKAGE_NAME = "com.sample.buck.playfeaturedelivery"
private const val PACKAGE_NAME_ONDEMAND = "$PACKAGE_NAME.ondemand"
private const val KOTLIN_SAMPLE_CLASSNAME = "$PACKAGE_NAME_ONDEMAND.KotlinSampleActivity"
private const val JAVA_SAMPLE_CLASSNAME = "$PACKAGE_NAME_ONDEMAND.JavaSampleActivity"
private const val NATIVE_SAMPLE_CLASSNAME = "$PACKAGE_NAME_ONDEMAND.NativeSampleActivity"
private const val INITIAL_INSTALL_CLASSNAME = "$PACKAGE_NAME.InitialInstallActivity"
private const val CONFIRMATION_REQUEST_CODE = 1
private const val TAG = "DynamicFeatures"
