package de.fhswf.fahrradverleih.android.view.ausleihen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.fhswf.fahrradverleih.android.R

class AusleihenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ausleihen, container, false)

    companion object {
        @JvmStatic
        fun newInstance() =
            AusleihenFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}