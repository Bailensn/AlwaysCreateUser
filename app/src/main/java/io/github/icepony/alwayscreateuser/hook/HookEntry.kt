package io.github.icepony.alwayscreateuser.hook

import com.highcapable.yukihookapi.annotation.xposed.YukiHookLibXposedEntry
import com.highcapable.yukihookapi.hook.factory.configure
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.YukiHookXposedModule
import io.github.icepony.alwayscreateuser.BuildConfig

/**
 * LSPosed entry point.
 *
 * The `scope` entry `system` is the virtual package name libxposed uses for the
 * Android system framework (`system_server`). KSP turns this annotation into
 * `META-INF/xposed/java_init.list`, `module.prop` and `scope.list`.
 */
@YukiHookLibXposedEntry(
    targetApiVersion = 101,
    scope = ["system"]
)
object HookEntry : YukiHookXposedModule {

    override fun onInit() = configure {
        debug = BuildConfig.DEBUG
        logging {
            tag = "AlwaysCreateUser"
            enabled = BuildConfig.DEBUG
            recording = false
        }
    }

    override fun onHook() = encase {
        loadSystem {
            UserManagerServiceHooker.hook(this)
        }
    }
}
