# Custom android_bundle macro to perform common bundle related action before calling native android_bundle rule.
def android_bundle(
    **kwargs
    ):

    # Check if the bundle contains dynamic feature
    is_dynamic = kwargs.pop("is_dynamic", False)

    # Get the dict dataset of application_module_configs from the args
    module_configs = kwargs.get("application_module_configs", {})

    if is_dynamic and module_configs:
            # Get all targets defined in application_module_configs
            module_targets = [target for build_targets in module_configs.values() for target in build_targets]

            # Modify deps to include the defined module_targets
            deps = kwargs.pop("deps", []) + module_targets

            # Add other dynamic feature related arguments i.e use_dynamic_feature, application_modules_with_manifest and deps
            kwargs.update(dict(
                use_dynamic_feature = True,
                application_modules_with_manifest = module_configs.keys(),
                deps = deps,
    ))

    # Call native bundle rule
    native.android_bundle(
        **kwargs
    )