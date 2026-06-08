package com.example.personalfinancetracker.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        val mockBarChart = listOf(
            BarChartItem("Sat", 0.4f, "340 SAR", false),
            BarChartItem("Sun", 0.5f, "425 SAR", false),
            BarChartItem("Mon", 0.6f, "510 SAR", false),
            BarChartItem("Tue", 1.0f, "850 SAR", true),
            BarChartItem("Wed", 0.4f, "340 SAR", false),
            BarChartItem("Thu", 0.3f, "255 SAR", false),
            BarChartItem("Fri", 0.5f, "425 SAR", false)
        )

        val mockBudgets = listOf(
            BudgetUiModel(id = "1", categoryName = "Shopping", percentage = 0.75f, colorHex = 0xFFA855F7), // Purple
            BudgetUiModel(id = "2", categoryName = "Transport", percentage = 0.32f, colorHex = 0xFF2DD4BF)   // Cyan
        )

        val mockTransactions = listOf(
            com.example.personalfinancetracker.features.transaction.model.TransactionUi(
                id = "t1", userId = "u1", amount = 340.0, currency = "SAR", currencySymbol = "SAR",
                categoryId = "shopping", date = System.currentTimeMillis(), notes = "Danube Supermarket",
                createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis(),
                type = com.example.domain.model.Type.EXPENSE, syncStatusEnum = com.example.data.sync.SyncStatusEnum.SYNCED.name
            ),
            com.example.personalfinancetracker.features.transaction.model.TransactionUi(
                id = "t2", userId = "u1", amount = 12450.0, currency = "SAR", currencySymbol = "SAR",
                categoryId = "salary", date = System.currentTimeMillis(), notes = "Monthly Salary",
                createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis(),
                type = com.example.domain.model.Type.INCOME, syncStatusEnum = com.example.data.sync.SyncStatusEnum.SYNCED.name
            ),
            com.example.personalfinancetracker.features.transaction.model.TransactionUi(
                id = "t3", userId = "u1", amount = 115.0, currency = "SAR", currencySymbol = "SAR",
                categoryId = "transport", date = System.currentTimeMillis(), notes = "Gas Station",
                createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis(),
                type = com.example.domain.model.Type.EXPENSE, syncStatusEnum = com.example.data.sync.SyncStatusEnum.PENDING.name
            )
        )

        _homeUiState.value = HomeUiState.Success(
            HomeData(
                userName = "Ahmed Mohamed",
                totalBalance = "42,500",
                balancePercentageChange = "+4.5%",
                selectedAnalysisTimeframe = Timeframe.WEEKLY,
                barChartData = mockBarChart,
                dailyAverage = "412 SAR",
                budgets = mockBudgets,
                recentTransactions = mockTransactions
            )
        )
    }

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.OnLoadHomeData -> loadMockData()
                is HomeEvent.OnClickAddTransaction -> _sideEffect.emit(HomeSideEffect.NavigateAddTransaction)
                is HomeEvent.OnClickTransfer -> _sideEffect.emit(HomeSideEffect.NavigateTransfer)
                is HomeEvent.OnClickSettings -> _sideEffect.emit(HomeSideEffect.NavigateSettings)
                is HomeEvent.OnClickTransaction -> _sideEffect.emit(HomeSideEffect.NavigateTransactionDetails(event.transactionId))
                is HomeEvent.OnAnalysisTimeframeChanged -> updateTimeframe(event.timeframe)
                is HomeEvent.OnClickBudget -> { /* Navigate to budget details */ }
                is HomeEvent.OnClickViewAllBudgets -> _sideEffect.emit(HomeSideEffect.NavigateAllBudgets)
                is HomeEvent.OnClickViewAllTransactions -> _sideEffect.emit(HomeSideEffect.NavigateAllTransactions)
            }
        }
    }

    private fun updateTimeframe(timeframe: Timeframe) {
        val currentState = _homeUiState.value
        if (currentState is HomeUiState.Success) {
            _homeUiState.value = currentState.copy(
                data = currentState.data.copy(
                    selectedAnalysisTimeframe = timeframe
                )
            )
        }
    }
}
