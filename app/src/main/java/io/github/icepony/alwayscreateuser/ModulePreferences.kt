package io.github.icepony.alwayscreateuser

/**
 * Keys shared by the preference screen and the hook callbacks.
 * They intentionally keep the historical names so existing installations keep their settings.
 */
internal object ModulePreferences {

    const val ENABLE_MODULE = "enable_module"

    const val CATEGORY_SETTINGS = "settings"
    const val ABOUT_VERSION = "about_version"

    const val HOOK_IS_CREATION_OVERRIDE_ENABLED = "hook_isCreationOverrideEnabled"
    const val HOOK_CAN_ADD_MORE_PROFILES_TO_USER = "hook_canAddMoreProfilesToUser"
    const val HOOK_CAN_ADD_MORE_MANAGED_PROFILES = "hook_canAddMoreManagedProfiles"
    const val HOOK_IS_USER_LIMIT_REACHED = "hook_isUserLimitReached"
    const val HOOK_IS_USER_LIMIT_REACHED_LOCKED = "hook_isUserLimitReachedLocked"
}
