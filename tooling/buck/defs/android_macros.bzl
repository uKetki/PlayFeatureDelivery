# Custom macro to perform common bundle related actions before calling the native android_bundle rule.
def _android_bundle(name, **kwargs):
    bundle_name = "bundle_debug" if "debug" in name else "bundle_release"

    # Call native bundle rule
    native.android_bundle(
        name = bundle_name,
        **kwargs
    )

def _android_binary(name, **kwargs):
    # Call native binary rule
    native.android_binary(
        name = name,
        **kwargs
    )

def dfm_android_binary(name, **kwargs):
    _android_binary(name, **kwargs)
    _android_bundle(name, **kwargs)
