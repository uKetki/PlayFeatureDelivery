# Custom macro to perform common bundle related actions before calling the native android_bundle rule.
load("//.okbuck/defs:okbuck_targets.bzl", "okbuck_keystore", "okbuck_manifest", "res_glob", "subdir_glob")

def _android_bundle(name, **kwargs):
    # Define module config which includes key,value pair of module name and respective target
    module_configs = {
        "kotlin": ["//features/kotlin:src_release"],
        "java": ["//features/java:src_release"],
        "native": ["//features/native:src_release"],
        "initialInstall": ["//features/initialInstall:src_release"],
    }

    # Get all targets defined in application_module_configs
    module_targets = [target for build_targets in module_configs.values() for target in build_targets]

    # Modify deps to include the defined module_targets
    deps = kwargs.pop("deps", []) + module_targets

    # Add other dynamic feature related arguments
    kwargs.update(dict(
        application_modules_with_resources = module_configs.keys(),
        use_split_dex = True,
        application_module_configs = module_configs,
        aapt_mode = "aapt2",
        module_manifest_skeleton = "ModuleManifest.xml",
        deps = deps,
    ))

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
