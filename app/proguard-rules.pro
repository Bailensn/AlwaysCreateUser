# YukiHook keeps the generated entry class and its metadata automatically.
# Keeping the entry object readable makes crash reports and hook logs easier to follow.
-keep class io.github.icepony.alwayscreateuser.hook.HookEntry { *; }

-repackageclasses
-allowaccessmodification

# libxposed API recommendations (the metadata file is rewritten when R8 renames entries).
-dontwarn io.github.libxposed.annotation.**
-adaptresourcefilecontents META-INF/xposed/java_init.list
-keep,allowoptimization,allowobfuscation public class * extends io.github.libxposed.api.XposedModule {
    public <init>();
}
