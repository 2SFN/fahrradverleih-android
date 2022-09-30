package de.fhswf.fahrradverleih.android.view.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.AuthenticationStatus;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupContent;
import de.fhswf.fahrradverleih.android.view.startup.viewmodel.StartupViewModel;
import de.fhswf.fahrradverleih.android.view.tabs.TabsActivity;

public class StartupActivity extends AppCompatActivity {

    private StartupViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));

        // ViewModel Instanz erstellen/abrufen
        this.viewModel = new ViewModelProvider(this,
                ViewModelProvider.Factory.from(StartupViewModel.initializer))
                .get(StartupViewModel.class);

        // Container für Inhalte (Fragments)
        viewModel.getContent().observe(this, content ->
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_container, getFragmentForContentType(content))
                        .commit());

        // Auf Navigationsevents reagieren
        viewModel.getAuthenticationStatus().observe(this, status -> {
            if (status == AuthenticationStatus.AUTHENTICATED) navigateAway();
        });

        // Eigenes Verhalten, wenn der Anwender zurück navigiert (Button/Geste)
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        viewModel.backPressed();
                    }
                });

        // Anfängliches Event auslösen
        viewModel.setContent(StartupContent.AUTHENTICATION);
    }

    private Fragment getFragmentForContentType(@NonNull StartupContent content) {
        switch (content) {
            case AUTHENTICATION:
                return StartupAuthFragment.newInstance();
            case LOGIN:
                return StartupLoginFragment.newInstance();
            case REGISTER:
            case WELCOME:
            default:
                return StartupWelcomeFragment.newInstance();
        }
    }

    private void navigateAway() {
        startActivity(new Intent(this, TabsActivity.class));
        finish();
    }
}
