package de.fhswf.fahrradverleih.android.view.ausleihen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.stream.Collectors;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.view.ausleihe_beenden.AusleiheBeendenDialogFragment;
import de.fhswf.fahrradverleih.android.view.ausleihen.recycler.AusleiheListItem;
import de.fhswf.fahrradverleih.android.view.ausleihen.recycler.AusleihenAdapter;
import de.fhswf.fahrradverleih.android.view.ausleihen.viewmodel.AusleihenViewModel;
import de.fhswf.fahrradverleih.android.view.ausleihen.viewmodel.Status;
import de.fhswf.fahrradverleih.android.widget.end_of_list_message.EndOfListItem;
import de.fhswf.fahrradverleih.android.widget.error_panel.ErrorPanelListItem;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;

public class AusleihenFragment extends Fragment {

    private AusleihenViewModel viewModel;

    public static AusleihenFragment newInstance() {
        return new AusleihenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz
        this.viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(
                AusleihenViewModel.initializer)).get(AusleihenViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ausleihen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pull-to-Refresh konfigurieren
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> viewModel.fetchData());
        viewModel.getStatus().observe(getViewLifecycleOwner(),
                status -> refreshLayout.setRefreshing(status != Status.IDLE));

        // Container -> Entweder Inhalt oder Error-Panel
        RecyclerView recycler = view.findViewById(R.id.recycler);

        recycler.addItemDecoration(new DividerItemDecoration(
                view.getContext(), DividerItemDecoration.VERTICAL));

        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == Status.FAILED) {
                // Inhalt = ErrorPanel
                recycler.setAdapter(new AusleihenAdapter(List.of(
                        new ErrorPanelListItem(() -> viewModel.fetchData())
                )));
            } else if (status == Status.IDLE) {
                // Inhalt = Ausleihen + End-of-List-Message
                AusleiheListItem.OnRueckgabeListener listener = a -> viewModel.ausleiheSelected(a);
                List<BaseItem> adapterItems = viewModel.getAusleihenValue().stream()
                        .map(a -> new AusleiheListItem(a, listener))
                        .collect(Collectors.toList());
                adapterItems.add(new EndOfListItem());
                recycler.setAdapter(new AusleihenAdapter(adapterItems));
            }
        });

        // Ausleihe ausgewählt
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if(status == Status.RUECKGABE) showRueckgabeModal();
        });

        // Anfänglich Daten abrufen
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if(status == Status.INITIAL) viewModel.fetchData();
        });
    }

    private void showRueckgabeModal() {
        if(viewModel.getAuswahlValue() == null) return;

        // Vor Anzeige des Dialogs: auf FragmentResult hören
        getParentFragmentManager().setFragmentResultListener(
                AusleiheBeendenDialogFragment.REQUEST_KEY, this,
                (key, bundle) -> {
                    // ViewModel informieren
                    viewModel.rueckgabeAbgeschlossen(bundle.containsKey(
                            AusleiheBeendenDialogFragment.RESULT_AUSLEIHE));
                    // Result & Result Callback löschen
                    getParentFragmentManager().clearFragmentResultListener(
                            AusleiheBeendenDialogFragment.REQUEST_KEY);
                    getParentFragmentManager().clearFragmentResult(
                            AusleiheBeendenDialogFragment.REQUEST_KEY);
                }
        );

        // Dialog anzeigen
        AusleiheBeendenDialogFragment.newInstance(viewModel.getAuswahlValue())
                .show(getParentFragmentManager(), "rueckgabe");
    }
}
