package de.fhswf.fahrradverleih.android.widget.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * Utility-Klasse, welche das erzeugen von Views aus Layouts vereinfacht.
 */
public class InflationHelper {

    @NonNull
    private final ViewGroup container;

    @NonNull
    private final LayoutInflater inflater;

    public InflationHelper(@NonNull Context context, @NonNull ViewGroup container) {
        this.container = container;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    public View inflate(@LayoutRes int layout) {
        return inflater.inflate(layout, container, false);
    }
}
