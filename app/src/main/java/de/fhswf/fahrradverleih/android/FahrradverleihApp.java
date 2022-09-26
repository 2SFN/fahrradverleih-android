package de.fhswf.fahrradverleih.android;

import android.app.Application;

import com.google.android.gms.maps.MapsInitializer;

public class FahrradverleihApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Benutze vorzugsweise neuen Renderer für Google Maps; die Legacy-Implementierung
        // führt mittlerweile möglicherweise zu defekten Maps.
        // s. https://developers.google.com/maps/documentation/android-sdk/renderer
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null);
    }
}
