package io.github.icepony.alwayscreateuser

import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication

/**
 * Initializes the YukiHook runtime in the module process and lets
 * [io.github.icepony.alwayscreateuser.hook.HookEntry.onInit] run there as well.
 */
class AlwaysCreateUserApplication : ModuleApplication()
