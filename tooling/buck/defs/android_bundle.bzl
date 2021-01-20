# Custom android_bundle macro to perform common bundle related action before calling native android_bundle rule.
def android_bundle(
    **kwargs
    ):

  native.android_bundle(
    **kwargs
  )