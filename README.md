# PlayCore API sample

This sample demonstrates usage of the PlayCore API with gradle and buck build tool system **with support for dynamic feature delivery**.

Read more at http://g.co/androidappbundle

## Introduction

The sample contains several modules.

`app` -> Contains the base application which always will be installed on device.

The `MainActivity` class demonstrates how to use the API to load and launch features.

The `BaseSplitActivity` abstract class implements the required `SplitCompat.Install()` call
in the `attachBaseContext` method. This allow to launch an activity from a freshly downloaded
dynamic module without having the restart the application.

`features/*` -> Contains features which can be downloaded on demand using the PlayCore API.

Each feature has some distinctly unique characteristics.

* `features/kotlin` -> Feature written in Kotlin and will be available **on-demand**
* `features/java` -> Feature written in Java and will be available **on-demand**
* `features/native` -> Feature written in Kotlin using JNI  and will be available **on-demand**
* `features/initiallInstall` -> Feature written in Kotlin and will be available **at install-time**

The `AndroidManifest` files in each feature shows how to declare a feature module as part of a dynamic app.

## Working Demo

<img src="screenshots/working_sample.gif" width="30%" />

## Build project with Gradle and Buck
|        | **Build command**                  | **Output Path**                                  |
|--------|------------------------------------|--------------------------------------------------|
| Gradle | ./gradlew bundleRelease            | app/build/outputs/bundle/release/app-release.aab |
| Buck   | ./buckw build //app:bundle_release | buck-out/gen/app/bundle_release.aab              |

## Testing dynamic delivery
_**Note you can't build the bundle file manually as the changes are supported only with the custom Buck pex, which is downloaded from a protected repository._

With the proposed solution **buck is generating extra apks for dynamic features** as follows, similar to AGP:
```
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/initialInstall-xxhdpi.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/initialInstall-master.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/java-xxhdpi.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/java-master.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/kotlin-xxhdpi.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/kotlin-master.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/native-xxhdpi.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/native-master_2.apk"
Pushed "/sdcard/Android/data/com.sample.buck.playfeaturedelivery/files/local_testing/native-x86_2.apk"
```
Further, the generated aab contains all the dynamic modules:
```
.
├── BundleConfig.pb
├── base
├── initialInstall
├── java
├── kotlin
└── native
```
