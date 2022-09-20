package de.fhswf.fahrradverleih.android.view.tabs.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Verwaltet, welcher Tab angezeigt wird, und verwaltet Navigationsevents.
 */
public class TabsViewModel extends ViewModel {

    private final MutableLiveData<TabsContent> content =
            new MutableLiveData<>(TabsContent.MAP);

    public MutableLiveData<TabsContent> getContent() {
        return content;
    }

    public void setContent(@NonNull TabsContent content) {
        if (getContentValue() != content)
            this.content.setValue(content);
    }

    @NonNull
    public TabsContent getContentValue() {
        if (content.getValue() == null) throw new IllegalStateException();
        return content.getValue();
    }

    /**
     * Verarbeitet Navigationsevents.
     *
     * @return true -> Activity beenden ; false -> Standardverhalten unterdrücken
     */
    public boolean backPressed() {
        if (getContentValue() != TabsContent.MAP) {
            setContent(TabsContent.MAP);
            return false;
        } else return true;
    }
}
