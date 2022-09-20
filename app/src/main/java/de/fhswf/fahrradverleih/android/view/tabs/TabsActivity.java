package de.fhswf.fahrradverleih.android.view.tabs;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.view.ausleihen.AusleihenFragment;
import de.fhswf.fahrradverleih.android.view.map.MapFragment;
import de.fhswf.fahrradverleih.android.view.profil.ProfilFragment;
import de.fhswf.fahrradverleih.android.view.tabs.viewmodel.TabsContent;
import de.fhswf.fahrradverleih.android.view.tabs.viewmodel.TabsViewModel;

public class TabsActivity extends AppCompatActivity {

    private TabsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this).get(TabsViewModel.class);

        // Inhalt des Containers
        viewModel.getContent().observe(this, content ->
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_container, getFragmentByContent(content))
                        .commit());

        // Navigation konfigurieren
        BottomNavigationView nav = findViewById(R.id.nav_view);

        nav.setOnItemSelectedListener(item -> {
            // Funktion im ViewModel nur aufrufen, wenn das ausgewählte Item nicht das
            // bereits angezeigte ist. Ohne diesen Check kann eine Änderung zu einer
            // Endlosschleife führen.
            if (getItemIdByContent(viewModel.getContentValue()) != item.getItemId())
                viewModel.setContent(getContentByItemId(item.getItemId()));
            return true;
        });

        // Aktualisiere den Indikator für das ausgewählte Item, falls noch nicht
        // automatisch geschehen (beispielsweise, weil das Feld im ViewModel durch
        // den Programmablauf aktualisiert wurde, und nicht durch Klick des Anwenders)
        viewModel.getContent().observe(this,
                content -> nav.setSelectedItemId(getItemIdByContent(content)));

        // Eigenes Verhalten, wenn der Anwender zurück navigiert (Button/Geste)
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (viewModel.backPressed()) finish();
                    }
                });

        // Anfänglich zum ersten Tab navigieren
        viewModel.setContent(TabsContent.MAP);
    }

    // Map, welche Fragment-Instanzen für die Tabs hält
    private final Map<TabsContent, Fragment> fragmentInstances = new HashMap<>(3);

    private Fragment getFragmentByContent(@NonNull TabsContent content) {
        switch (content) {
            case MAP:
                return fragmentInstances.computeIfAbsent(
                        content, k -> MapFragment.newInstance());
            case AUSLEIHEN:
                return fragmentInstances.computeIfAbsent(
                        content, k -> AusleihenFragment.newInstance());
            case PROFIL:
                return fragmentInstances.computeIfAbsent(
                        content, k -> ProfilFragment.newInstance());
            default:
                throw new IllegalArgumentException("No Fragment for this TabsContent.");
        }
    }

    private final Map<Integer, TabsContent> contentMapping = Map.of(
            R.id.navigation_map, TabsContent.MAP,
            R.id.navigation_ausleihen, TabsContent.AUSLEIHEN,
            R.id.navigation_profil, TabsContent.PROFIL);

    @IdRes
    private int getItemIdByContent(@NonNull TabsContent content) {
        for (Map.Entry<Integer, TabsContent> c : contentMapping.entrySet())
            if (c.getValue() == content) return c.getKey();
        throw new IllegalArgumentException("Cannot map TabsContent to resource ID.");
    }

    private TabsContent getContentByItemId(@IdRes int itemId) {
        return contentMapping.getOrDefault(itemId, TabsContent.MAP);
    }
}
