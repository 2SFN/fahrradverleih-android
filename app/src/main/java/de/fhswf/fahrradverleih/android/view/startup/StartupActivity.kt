package de.fhswf.fahrradverleih.android.view.startup

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import de.fhswf.fahrradverleih.android.R
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.AuthenticationStatus
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupContent
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupViewModel
import de.fhswf.fahrradverleih.android.view.tabs.TabsActivity

class StartupActivity : AppCompatActivity() {

    private lateinit var viewModel: StartupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        // View-Model Instanz erstellen/abrufen
        this.viewModel = ViewModelProvider(this, StartupViewModel.Factory).get()

        // Container für Inhalte
        viewModel.getContent().observe(this) { content ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, getFragmentForContentType(content))
                .commit()
        }

        // Auf Navigationsevents reagieren
        viewModel.getAuthenticationStatus().observe(this) { status ->
            if (status == AuthenticationStatus.AUTHENTICATED) navigateAway()
        }

        // Eigenes Verhalten, wenn der Anwender zurück navigiert (Button/Geste)
        onBackPressedDispatcher.addCallback(this) {
            if (viewModel.backPressed()) finish()
        }

        // Erstes Event auslösen
        viewModel.navigateTo(StartupContent.AUTHENTICATION)
    }

    private fun getFragmentForContentType(content: StartupContent): Fragment {
        return when (content) {
            StartupContent.AUTHENTICATION -> StartupAuthFragment.newInstance()
            StartupContent.LOGIN -> StartupLoginFragment.newInstance()
            StartupContent.REGISTER -> StartupWelcomeFragment.newInstance()
            StartupContent.WELCOME -> StartupWelcomeFragment.newInstance()
        }
    }

    private fun navigateAway() {
        val launchIntent = Intent(this, TabsActivity::class.java)
        startActivity(launchIntent)
        finish()
    }
}