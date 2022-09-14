package de.fhswf.fahrradverleih.android.view.startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import de.fhswf.fahrradverleih.android.R
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.AuthenticationStatus
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupViewModel

class StartupLoginFragment : Fragment() {

    private lateinit var viewModel: StartupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModel-Instanz
        this.viewModel = ViewModelProvider(requireActivity()).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_startup_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Views konfigurieren
        val emailInput = view.findViewById<TextInputEditText>(R.id.email_input)

        // Aktualisiert den Wert des ViewModels, wenn der Anwender den Text manipuliert
        emailInput.addTextChangedListener { text -> viewModel.setEmail(text.toString()) }

        // Deaktiviert das View, wenn die Authentifizierung im Gange ist
        viewModel.getAuthenticationStatus().observe(viewLifecycleOwner) { status ->
            emailInput.isEnabled = (status == AuthenticationStatus.IDLE)
        }

        // Passwort-Input
        val passwordInput = view.findViewById<TextInputEditText>(R.id.password_input)
        passwordInput.addTextChangedListener { text -> viewModel.setPassword(text.toString()) }
        viewModel.getAuthenticationStatus().observe(viewLifecycleOwner) { status ->
            passwordInput.isEnabled = (status == AuthenticationStatus.IDLE)
        }

        // Anmelden-Button
        val anmeldenButton = view.findViewById<Button>(R.id.login)
        anmeldenButton.setOnClickListener { viewModel.login() }
        viewModel.getAuthenticationStatus().observe(viewLifecycleOwner) { status ->
            anmeldenButton.isEnabled = (status == AuthenticationStatus.IDLE)
        }

        // Login-Events
        viewModel.getAuthenticationStatus().observe(viewLifecycleOwner) { status ->
            if (status == AuthenticationStatus.FAILED) {
                Snackbar.make(view, "Anmeldung fehlgeschlagen!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StartupLoginFragment().apply {}
    }
}