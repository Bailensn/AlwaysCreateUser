package io.github.icepony.alwayscreateuser

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreferenceCompat
import com.highcapable.yukihookapi.hook.xposed.preference.ui.ModulePreferenceFragment

/**
 * Settings backed by [com.highcapable.yukihookapi.hook.xposed.preference.YukiHookPreferences],
 * which is the storage LSPosed exposes to the hooked system process.
 */
class SettingsFragment : ModulePreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.prefs, rootKey)

        val masterSwitch = findPreference<SwitchPreferenceCompat>(ModulePreferences.ENABLE_MODULE)
        val hookCategory = findPreference<PreferenceCategory>(ModulePreferences.CATEGORY_SETTINGS)

        fun updateHookCategory(enabled: Boolean) {
            hookCategory?.isEnabled = enabled
        }

        updateHookCategory(masterSwitch?.isChecked == true)

        masterSwitch?.setOnPreferenceChangeListener { _, newValue ->
            updateHookCategory(newValue as? Boolean == true)
            true
        }

        findPreference<Preference>(ModulePreferences.ABOUT_VERSION)?.summary = BuildConfig.VERSION_NAME
    }
}
