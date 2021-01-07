# PlayCore API sample

This sample demonstrates usage of the PlayCore API.

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

* `features/kotlin` -> Feature written in Kotlin and will be available on-demand
* `features/java` -> Feature written in Java and will be available on-demand
* `features/native` -> Feature written in Kotlin using JNI  and will be available on-demand
* `features/initiallInstall` -> Feature written in Kotlin and will be available at install-time

The `AndroidManifest` files in each feature shows how to declare a feature module as part of a dynamic app.

## Screenshots

<img src="screenshots/main.png" width="30%" />

## Testing dynamic delivery

To test dynamic delivery with this sample, you'll need to upload it to the Google Play Store's
internal testing channel.

