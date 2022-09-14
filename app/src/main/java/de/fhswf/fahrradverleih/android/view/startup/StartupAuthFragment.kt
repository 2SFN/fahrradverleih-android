package de.fhswf.fahrradverleih.android.view.startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.fhswf.fahrradverleih.android.R

class StartupAuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_startup_auth, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            StartupAuthFragment().apply {}
    }
}