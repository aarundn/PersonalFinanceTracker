import com.example.core.navigation.Feature
import com.example.core.navigation.features.HomeFeature
import com.example.personalfinancetracker.features.home.HomeViewModel
import com.example.personalfinancetracker.features.home.navigation.HomeFeatureImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeFeatureImpl) binds arrayOf(HomeFeature::class, Feature::class)
    viewModelOf(::HomeViewModel)
}