package de.fhswf.fahrradverleih.android.widget.error_panel;

import androidx.annotation.Nullable;

import de.fhswf.fahrradverleih.android.widget.recycler.BaseItem;

/**
 * {@link BaseItem}-Implementierung für das ErrorPanel.
 */
public class ErrorPanelListItem implements BaseItem {
    public static final int ADAPTER_TYPE = 377;

    @Nullable
    private final OnRetryListener onRetryListener;

    public ErrorPanelListItem(@Nullable OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public ErrorPanelListItem() {
        this(null);
    }

    @Override
    public int getAdapterType() {
        return ADAPTER_TYPE;
    }

    @Nullable
    public OnRetryListener getOnRetryListener() {
        return onRetryListener;
    }

    public interface OnRetryListener {
        void onRetry();
    }

}
