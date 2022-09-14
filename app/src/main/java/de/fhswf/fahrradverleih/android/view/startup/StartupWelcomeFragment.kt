package de.fhswf.fahrradverleih.android.view.startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import de.fhswf.fahrradverleih.android.R
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupContent
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupViewModel

class StartupWelcomeFragment : Fragment() {

    private lateinit var viewModel: StartupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModel-Instanz
        this.viewModel = ViewModelProvider(requireActivity()).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_startup_welcome, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Views konfigurieren
        val anmeldenButton = view.findViewById<Button>(R.id.login)
        anmeldenButton.setOnClickListener { viewModel.navigateTo(StartupContent.LOGIN) }

        val registrierenButton = view.findViewById<Button>(R.id.register)
        registrierenButton.setOnClickListener {
            Toast.makeText(
                view.context,
                "Im Rahmen des Fallbeispiels nicht implementiert",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StartupWelcomeFragment().apply {}
    }
}