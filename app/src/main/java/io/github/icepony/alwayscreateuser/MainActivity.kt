package io.github.icepony.alwayscreateuser

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.highcapable.yukihookapi.YukiHook

/**
 * Hosts the module settings and reports whether the LSPosed service is reachable.
 *
 * The preference screen is added only after the first service callback so that
 * [com.highcapable.yukihookapi.hook.xposed.preference.YukiHookPreferences] opens the
 * remote storage instead of the private fallback.
 */
class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var statusBanner: TextView

    private val markUnavailable = Runnable {
        renderStatus(active = false)
        showSettings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        statusBanner = findViewById(R.id.status_banner)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        observeModuleStatus()
    }

    private fun observeModuleStatus() {
        YukiHook.Status.observeFrameworkService(lifecycle) { available ->
            handler.removeCallbacks(markUnavailable)
            if (available) {
                renderStatus(active = YukiHook.Status.isModuleActive)
                showSettings()
            } else {
                handler.postDelayed(markUnavailable, SERVICE_TIMEOUT_MS)
            }
        }
    }

    private fun showSettings() {
        if (supportFragmentManager.findFragmentById(R.id.settings_container) != null) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }

    private fun renderStatus(active: Boolean) {
        statusBanner.visibility = if (active) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        handler.removeCallbacks(markUnavailable)
        super.onDestroy()
    }

    private companion object {
        /** libxposed connects to the framework service asynchronously. */
        const val SERVICE_TIMEOUT_MS = 500L
    }
}
