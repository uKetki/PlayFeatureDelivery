# PlayCore API sample

This sample demonstrates usage of the PlayCore API with gradle and buck build tool system **with support for dynamic feature delivery**.

Read more at http://g.co/androidappbundle

## Introduction

The sample contains several modules.

`app` -> Contains the base application which always will be installed on device.

The `MainActivity` class demonstrates how to use the API to load and launch features.

The `BaseSplitActivity` abstract class implements the required `SplitCompat.Install()` call
in the `attachBaseContext` method. This allows to launch an activity from a freshly downloaded
dynamic module without having the restart the application.

`features/*` -> Contains features which can be downloaded on demand using the PlayCore API.

Each feature has some distinctly unique characteristics.

* `features/kotlin` -> Feature written in Kotlin and will be available **instantly**
* `features/java` -> Feature written in Java and will be available **instantly**
* `features/native` -> Feature written in Kotlin using JNI  and will be available **instantly**
* `features/initiallInstall` -> Feature written in Kotlin and will be available **instantly**

The `AndroidManifest` files in each feature shows how to declare a feature module as part of a instant app.

## Build project with Gradle and Buck
|        | **Build command**                  | **Output Path**                                  |
|--------|------------------------------------|--------------------------------------------------|
| Gradle | ./gradlew bundleRelease            | app/build/outputs/bundle/release/app-release.aab |
| Buck   | ./buckw build //app:bundle_release | buck-out/gen/app/bundle_release.aab              |

## Testing instant app
To test instant app locally use [bundletool](https://developer.android.com/studio/command-line/bundletool>bundletool) and IA tool as follows:
_**Note you can't build the bundle file manually as the changes are supported only with the custom Buck pex, which is downloaded from a protected repository._
* Navigate to instantapps tool path: <path_to_sdk>/extras/google/instantapps
* Create apks from aab: **bundletool build-apks --bundle=<path_to_aab> --output=<path_to_apks>**
* ./ia run <path_to_apks>

With the proposed solution **buck is generating extra apks for instant features** as follows, similar to AGP:
```
INFO - Analyzing files...

INFO - Checking device...

INFO - Pushing instant app to device...

INFO - Launching app...

INFO - Instant app started
```
Further, the generated aab contains all the instant modules:
```
.
├── BundleConfig.pb
├── base
├── initialInstall
├── java
├── kotlin
└── native
```
