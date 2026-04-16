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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            val item = repository.getItemById(itemId)
            _uiState.update { it.copy(item = item) }

            // Load cached museum data if available
            item?.let {
                val cached = repository.getCachedMuseumData(it.name)
                if (cached != null) {
                    _uiState.update { state -> state.copy(museumCache = cached) }
                }
            }
        }
    }

    fun fetchMuseumInfo() {
        val name = _uiState.value.item?.name ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMuseum = true, museumError = null) }

            val result = repository.fetchMuseumInfo(name)

            result.fold(
                onSuccess = { cache ->
                    // Log the event
                    repository.insertEvent(
                        CollectionEvent(
                            antiqueId = itemId,
                            eventType = EventType.MUSEUM_INFO_FETCHED
                        )
                    )
                    _uiState.update { it.copy(
                        museumCache = cache,
                        isLoadingMuseum = false,
                        museumError = null
                    )}
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isLoadingMuseum = false,
                        museumError = error.message
                    )}
                }
            )
        }
    }
}
