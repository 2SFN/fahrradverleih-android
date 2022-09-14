package de.fhswf.fahrradverleih.android.view.tabs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class TabsContent {
    MAP, AUSLEIHEN, PROFIL
}

class TabsViewModel : ViewModel() {

    private val content = MutableLiveData(TabsContent.MAP)

    fun getContent(): LiveData<TabsContent> = content

    fun navigateTo(content: TabsContent) {
        this.content.value = content
    }

    fun backPressed(): Boolean {
        return this.content.value == TabsContent.MAP
    }

}