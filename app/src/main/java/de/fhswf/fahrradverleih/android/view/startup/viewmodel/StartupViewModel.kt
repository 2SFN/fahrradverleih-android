package de.fhswf.fahrradverleih.android.view.startup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import de.fhswf.fahrradverleih.android.api.RadApi
import de.fhswf.fahrradverleih.android.api.remote.RemoteRadApi

enum class StartupContent {
    AUTHENTICATION,
    LOGIN,
    REGISTER,
    WELCOME
}

enum class AuthenticationStatus {
    IDLE, AUTHENTICATING, FAILED, AUTHENTICATED
}

class StartupViewModel(
    private val api: RadApi,
) : ViewModel() {

    private val content = MutableLiveData(StartupContent.AUTHENTICATION)
    private val authenticationStatus = MutableLiveData(AuthenticationStatus.IDLE)

    private val email = MutableLiveData("")
    private val password = MutableLiveData("")

    fun getContent(): LiveData<StartupContent> = content
    fun getAuthenticationStatus(): LiveData<AuthenticationStatus> = authenticationStatus

    fun setEmail(email: String) {
        this.email.value = email
    }

    fun setPassword(password: String) {
        this.password.value = password
    }

    /**
     * Ändert {@link content} und führt ggf. eine mit dem neuen Inhalt verbundene,
     * anfängliche Aktion aus.
     */
    fun navigateTo(content: StartupContent) {
        this.content.value = content

        when (this.content.value) {
            StartupContent.AUTHENTICATION -> {
                this.authenticationStatus.value = AuthenticationStatus.IDLE
                authenticate()
            }
            StartupContent.LOGIN -> {
                authenticationStatus.value = AuthenticationStatus.IDLE
                email.value = ""
                password.value = ""
            }
            else -> {}
        }
    }

    /**
     * Erstellt eine neue Session mit den vom Anwender bereitgestellten Anmeldedaten.
     */
    fun login() {
        if (this.authenticationStatus.value != AuthenticationStatus.IDLE) return
        this.authenticationStatus.value = AuthenticationStatus.AUTHENTICATING
        api.login(email.value!!, password.value!!,
            { result ->
                if (api is RemoteRadApi) api.updateToken(result.token)
                navigateTo(StartupContent.AUTHENTICATION)
            },
            {
                this.authenticationStatus.value = AuthenticationStatus.FAILED
                this.authenticationStatus.value = AuthenticationStatus.IDLE
            }
        )
    }

    /**
     * Geht mit Navigationsevents um.
     *
     * @return true -> Event an Avtivity weitergeben
     *         false -> ViewModel ist zuständig
     */
    fun backPressed(): Boolean {
        return when (this.content.value) {
            StartupContent.AUTHENTICATION -> true
            StartupContent.WELCOME -> true
            else -> {
                navigateTo(StartupContent.AUTHENTICATION)
                false
            }
        }
    }

    private fun authenticate() {
        this.authenticationStatus.value = AuthenticationStatus.AUTHENTICATING
        api.auth(
            { this.authenticationStatus.value = AuthenticationStatus.AUTHENTICATED },
            {
                this.authenticationStatus.value = AuthenticationStatus.FAILED
                this.authenticationStatus.value = AuthenticationStatus.IDLE
                navigateTo(StartupContent.WELCOME)
            }
        )
    }

    companion object {
        /**
         * Factory erlaubt die Instantiierung des ViewModels mit Extras, welche unter anderem
         * eine Instanz der {@link android.app.Application} enthalten (hier für die Erzeugung
         * einer API-Instanz erforderlich).
         */
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!

                return StartupViewModel(
                    api = RemoteRadApi(application)
                ) as T
            }
        }
    }
}
