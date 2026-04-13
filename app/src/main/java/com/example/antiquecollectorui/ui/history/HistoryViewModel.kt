package com.example.antiquecollectorui.ui.history

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.antiquecollectorui.data.repository.AntiqueRepository
    import com.example.antiquecollectorui.domain.model.CollectionEvent
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.SharingStarted
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.map
    import kotlinx.coroutines.flow.stateIn
    import javax.inject.Inject

    data class HistoryUiState(
        val events: List<CollectionEvent> = emptyList()
    )

    @HiltViewModel
    class HistoryViewModel @Inject constructor(
        repository: AntiqueRepository
    ) : ViewModel() {

        val uiState: StateFlow<HistoryUiState> = repository.getAllEvents()
            .map { HistoryUiState(events = it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUiState())
    }
