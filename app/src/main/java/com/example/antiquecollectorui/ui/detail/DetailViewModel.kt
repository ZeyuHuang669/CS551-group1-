package com.example.antiquecollectorui.ui.detail

    import androidx.lifecycle.SavedStateHandle
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.antiquecollectorui.data.repository.AntiqueRepository
    import com.example.antiquecollectorui.domain.model.AntiqueItem
    import com.example.antiquecollectorui.domain.model.CollectionEvent
    import com.example.antiquecollectorui.domain.model.EventType
    import com.example.antiquecollectorui.domain.model.MuseumCache
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.*
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    data class DetailUiState(
        val item: AntiqueItem? = null,
        val museumCache: MuseumCache? = null,
        val isLoadingMuseum: Boolean = false,
        val museumError: String? = null
    )

    @HiltViewModel
    class DetailViewModel @Inject constructor(
        private val repository: AntiqueRepository,
        savedStateHandle: SavedStateHandle
    ) : ViewModel() {

        private val itemId: Int = checkNotNull(savedStateHandle["itemId"])

        private val _museumState = MutableStateFlow<Pair<Boolean, String?>>(false to null)

        val uiState: StateFlow<DetailUiState> = combine(
            flow { emit(repository.getItemById(itemId)) },
            _museumState
        ) { item, (loading, error) ->
            DetailUiState(item = item, isLoadingMuseum = loading, museumError = error)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailUiState())

        fun fetchMuseumInfo() {
            val name = uiState.value.item?.name ?: return
            viewModelScope.launch {
                _museumState.value = true to null
                val result = repository.fetchMuseumInfo(name)
                result.fold(
                    onSuccess = { cache ->
                        repository.insertMuseumCache(cache)
                        repository.insertEvent(
                            CollectionEvent(
                                antiqueId = itemId,
                                eventType = EventType.MUSEUM_INFO_FETCHED
                            )
                        )
                        _museumState.value = false to null
                    },
                    onFailure = { _museumState.value = false to (it.message ?: "Unknown error") }
                )
            }
        }

}