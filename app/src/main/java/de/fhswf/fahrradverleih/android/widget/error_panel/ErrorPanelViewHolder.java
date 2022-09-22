package de.fhswf.fahrradverleih.android.widget.error_panel;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import de.fhswf.fahrradverleih.android.R;
import de.fhswf.fahrradverleih.android.widget.recycler.BaseViewHolder;

public class ErrorPanelViewHolder extends BaseViewHolder<ErrorPanelListItem> {
    @LayoutRes
    public static final int LAYOUT = R.layout.fragment_error_panel;

    public ErrorPanelViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(@NonNull ErrorPanelListItem item) {
        findViewById(R.id.retry).setOnClickListener(v -> {
            if(item.getOnRetryListener() != null) item.getOnRetryListener().onRetry();
        });
    }
}
