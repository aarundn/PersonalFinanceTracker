package com.example.personalfinancetracker.features.settings

import androidx.lifecycle.viewModelScope
import com.example.conversion_rate.domain.usecase.ConvertCurrencyUseCase
import com.example.conversion_rate.domain.usecase.GetProvidersUseCase
import com.example.conversion_rate.domain.usecase.InitializeRateSyncUseCase
import com.example.core.common.BaseViewModel
import com.example.core.model.Currency
import com.example.core.model.DefaultCurrencies
import com.example.domain.repo.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.RoundingMode

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val convertCurrency: ConvertCurrencyUseCase,
    private val initializeRateSync: InitializeRateSyncUseCase,
    private val getProviders: GetProvidersUseCase,
) : BaseViewModel<SettingsState, SettingsEvent, SettingsSideEffect>() {

    override fun createInitialState(): SettingsState = SettingsState(
        selectedCurrency = DefaultCurrencies.USD,
        availableCurrencies = DefaultCurrencies.all
    )

    init {
        getInitialPreferences()
    }

    private fun getInitialPreferences() {
        viewModelScope.launch {
            launch {
                userPreferencesRepository.baseCurrency.collect { currencyId ->
                    setState {
                        copy(
                            selectedCurrency = DefaultCurrencies.fromId(currencyId)
                                ?: DefaultCurrencies.USD
                        )
                    }
                }
            }
            
            launch {
                val savedProviderId = userPreferencesRepository.selectedProviderId.first()
                getProviders().onSuccess { providers ->
                    setState {
                        copy(
                            availableProviders = providers,
                            selectedProviderId = providers
                                .firstOrNull { (id, _) -> id == savedProviderId }?.first
                                ?: providers.firstOrNull()?.first,
                        )
                    }
                }
            }
        }
    }

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnClickCurrencyPicker -> setState { copy(isCurrencyPickerVisible = true) }
            SettingsEvent.OnDismissCurrencyPicker -> setState { copy(isCurrencyPickerVisible = false) }
            is SettingsEvent.OnCurrencySelected -> selectCurrency(event.currency)
            SettingsEvent.OnNavigateBack -> triggerSideEffect(SettingsSideEffect.NavigateBack)

            is SettingsEvent.OnAmountChanged -> setState {
                copy(
                    conversionAmount = event.amount,
                    conversionResult = null,
                    conversionError = null
                )
            }

            is SettingsEvent.OnTargetCurrencySelected -> setState {
                copy(
                    targetCurrency = event.currency,
                    conversionResult = null,
                    conversionError = null
                )
            }

            is SettingsEvent.OnProviderSelected -> {
                setState {
                    copy(
                        selectedProviderId = event.providerId,
                        conversionResult = null,
                        conversionError = null
                    )
                }
                viewModelScope.launch {
                    userPreferencesRepository.setSelectedProviderId(event.providerId)
                    initializeRateSync(
                        userPreferencesRepository.baseCurrency.first(),
                        event.providerId
                    )
                }
            }

            SettingsEvent.OnConvertClicked -> performConversion()
        }
    }

    private fun selectCurrency(currency: Currency) {
        viewModelScope.launch {
            userPreferencesRepository.setBaseCurrency(currency.id)
            setState { copy(isCurrencyPickerVisible = false) }
            val providerId = userPreferencesRepository.selectedProviderId.first()
            initializeRateSync(currency.id, providerId)
        }
    }

    private fun performConversion() {
        val current = uiState.value
        val providerId = current.selectedProviderId ?: return
        val target = current.targetCurrency ?: return
        val amount = current.conversionAmount.toBigDecimalOrNull() ?: return

        setState { copy(isConverting = true, conversionResult = null, conversionError = null) }

        viewModelScope.launch {
            val baseCurrency = uiState.value.selectedCurrency.id

            convertCurrency(
                providerId = providerId,
                fromCurrencyCode = baseCurrency,
                toCurrencyCode = target.id,
                amount = amount,
            ).onSuccess { result ->
                setState {
                    copy(
                        isConverting = false,
                        conversionResult = result.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    )
                }
            }.onFailure { error ->
                setState {
                    copy(
                        isConverting = false,
                        conversionError = error.message ?: "Conversion failed",
                    )
                }
            }
        }
    }
}
