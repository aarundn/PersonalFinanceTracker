import com.example.core.navigation.Feature
import com.example.core.navigation.features.SettingsFeature
import com.example.personalfinancetracker.features.settings.SettingsViewModel
import com.example.personalfinancetracker.features.settings.navigation.SettingsFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

val settingsModule = module {
    singleOf(::SettingsFeatureImpl)  binds arrayOf(SettingsFeature::class , Feature::class)
    viewModel {
        SettingsViewModel(
            userPreferencesRepository = get(),
            convertCurrency = get(),
            initializeRateSync = get(),
            getProviders = get(),
            observeSyncStatus = get()
        )
    }
}
