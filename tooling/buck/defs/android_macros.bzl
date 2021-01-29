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

    # Add other dynamic feature related arguments i.e application_modules_with_manifest and deps
    kwargs.update(dict(
        application_modules_with_manifest = module_configs.keys(),
        use_dynamic_feature = True,
        use_split_dex = True,
        application_module_configs = module_configs,
        deps = deps,
    ))

    bundle_name = "bundle_debug" if "debug" in name else "bundle_release"

    # Skeleton Manifest used by AndroidManifestMerger tool to merge all dynamic features AndroidManifest.
    okbuck_manifest_target_name = "manifest_module_" + bundle_name
    okbuck_manifest(
        name = okbuck_manifest_target_name,
        main_manifest = "ModuleManifest.xml",
        min_sdk = "16",
        target_sdk = "29",
        package = "com.sample.buck.playfeaturedelivery",
        version_code = "1",
        version_name = "1.0",
        debuggable = False,
    )

    # Call native bundle rule
    native.android_bundle(
        name = bundle_name,
        module_manifest_skeleton = ":" + okbuck_manifest_target_name + "",
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
