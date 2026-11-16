package io.github.icepony.alwayscreateuser.hook

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.param.PackageParam
import com.highcapable.yukihookapi.hook.xposed.preference.YukiHookPreferences
import io.github.icepony.alwayscreateuser.ModulePreferences
import java.lang.reflect.Method

/**
 * Hooks the user-limit checks in `com.android.server.pm.UserManagerService`.
 *
 * Every entry of [rules] is resolved through KavaRef by name only, so the same
 * code keeps working when a method is missing or has a different signature on
 * another Android release.
 */
internal object UserManagerServiceHooker {

    private const val TARGET_CLASS = "com.android.server.pm.UserManagerService"

    /**
     * @param methodName the method looked up on the host class.
     * @param preferenceKey the switch that toggles this rule.
     * @param forcedResult the value returned while the rule is enabled.
     */
    private data class HookRule(
        val methodName: String,
        val preferenceKey: String,
        val forcedResult: Boolean
    )

    private val rules = listOf(
        HookRule("isCreationOverrideEnabled", ModulePreferences.HOOK_IS_CREATION_OVERRIDE_ENABLED, true),
        HookRule("canAddMoreProfilesToUser", ModulePreferences.HOOK_CAN_ADD_MORE_PROFILES_TO_USER, true),
        HookRule("canAddMoreManagedProfiles", ModulePreferences.HOOK_CAN_ADD_MORE_MANAGED_PROFILES, true),
        HookRule("isUserLimitReached", ModulePreferences.HOOK_IS_USER_LIMIT_REACHED, false),
        HookRule("isUserLimitReachedLocked", ModulePreferences.HOOK_IS_USER_LIMIT_REACHED_LOCKED, false)
    )

    fun hook(param: PackageParam) {
        val userManagerService = runCatching {
            Class.forName(TARGET_CLASS, false, param.hostClassLoader)
        }.onFailure {
            YLog.error("Unable to load $TARGET_CLASS.", it)
        }.getOrNull() ?: return

        val preferences = runCatching { param.preferences }.getOrNull()

        rules.forEach { rule ->
            val methods: List<Method> = userManagerService.resolve()
                .method { name = rule.methodName }
                .map { it.self }

            if (methods.isEmpty()) {
                YLog.warn("${rule.methodName}() is not available on this Android version, skipped.")
                return@forEach
            }

            with(param) {
                methods.hookAll {
                    before {
                        if (!isRuleEnabled(preferences, rule)) return@before
                        result = rule.forcedResult
                        YLog.debug("Bypassed ${rule.methodName}().")
                    }
                }
            }

            YLog.info("Hooked ${rule.methodName}() [${methods.size} overload(s)].")
        }
    }

    /**
     * Reads the switches live so a change applies without recreating the hook.
     * When the framework preferences are not ready yet the module stays enabled.
     */
    private fun isRuleEnabled(preferences: YukiHookPreferences?, rule: HookRule): Boolean {
        if (preferences == null) return true

        return runCatching {
            if (!preferences.isAvailable) return@runCatching true
            preferences.getBoolean(ModulePreferences.ENABLE_MODULE, true) &&
                preferences.getBoolean(rule.preferenceKey, true)
        }.getOrDefault(true)
    }
}
