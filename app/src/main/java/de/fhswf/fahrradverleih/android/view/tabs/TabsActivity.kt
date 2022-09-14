package de.fhswf.fahrradverleih.android.view.tabs

import android.os.Bundle
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.fhswf.fahrradverleih.android.R
import de.fhswf.fahrradverleih.android.view.ausleihen.AusleihenFragment
import de.fhswf.fahrradverleih.android.view.map.MapFragment
import de.fhswf.fahrradverleih.android.view.profil.ProfilFragment
import de.fhswf.fahrradverleih.android.view.tabs.viewmodel.TabsContent
import de.fhswf.fahrradverleih.android.view.tabs.viewmodel.TabsViewModel

class TabsActivity : AppCompatActivity() {

    private lateinit var viewModel: TabsViewModel

    private val contentMapping = mapOf(
        R.id.navigation_map to TabsContent.MAP,
        R.id.navigation_ausleihen to TabsContent.AUSLEIHEN,
        R.id.navigation_profil to TabsContent.PROFIL,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)

        // ViewModel-Instanz
        this.viewModel = ViewModelProvider(this)[TabsViewModel::class.java]

        // Container konfigurieren
        viewModel.getContent().observe(this) { content ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, getFragmentForStatus(content))
                .commit()
        }

        // Navigation konfigurieren
        val nav = findViewById<BottomNavigationView>(R.id.nav_view)
        nav.setOnItemSelectedListener { item ->
            // Funktion im ViewModel nur aufrufen, wenn das ausgewählte Item nicht das
            // bereits angezeigte ist. Ohne diesen Check kann eine Änderung zu einer
            // Endlosschleife führen.
            if (itemIdOf(viewModel.getContent().value!!) != item.itemId)
                viewModel.navigateTo(contentOf(item.itemId))
            true
        }

        viewModel.getContent().observe(this) { content ->
            // Wenn Content nicht durch einen Klick des Anwenders, sondern durch den
            // Programmablauf geändert wird, dann aktualisiert sich das scheinbar
            // ausgewählte Item in dem BottomNavigationView nicht automatisch.
            nav.selectedItemId = itemIdOf(content)
        }

        // Eigenes Verhalten, wenn der Anwender zurück navigiert (Button/Geste)
        onBackPressedDispatcher.addCallback(this) {
            if (viewModel.backPressed()) {
                viewModel.navigateTo(TabsContent.MAP)
            } else {
                finish()
            }
        }

        // Anfänglich zum ersten Tab navigieren
        viewModel.navigateTo(TabsContent.MAP)
    }

    private fun getFragmentForStatus(content: TabsContent): Fragment {
        return when (content) {
            TabsContent.MAP -> MapFragment.newInstance()
            TabsContent.AUSLEIHEN -> AusleihenFragment.newInstance()
            TabsContent.PROFIL -> ProfilFragment.newInstance()
        }
    }

    @IdRes
    private fun itemIdOf(content: TabsContent): Int {
        for ((key, value) in contentMapping) {
            if (value == content) return key
        }

        throw Exception("Unbekannter Inhalt")
    }

    private fun contentOf(id: Int): TabsContent {
        if (!contentMapping.containsKey(id))
            throw Exception("Unbekannter Identifier")
        return contentMapping[id]!!
    }

}