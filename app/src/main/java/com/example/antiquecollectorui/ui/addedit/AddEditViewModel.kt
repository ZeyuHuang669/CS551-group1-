package com.example.antiquecollectorui.ui.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollectorui.data.repository.AntiqueRepository
import com.example.antiquecollectorui.domain.model.AntiqueItem
import com.example.antiquecollectorui.domain.model.CollectionEvent
import com.example.antiquecollectorui.domain.model.EventType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditUiState(
    val itemId: Int? = null,
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val estimatedValue: String = "",
    val imageUrl: String = "",
    val dateAcquired: String = "",
    val condition: String = "",
    val notes: String = "",
    val provenance: String = "",
    // Validation errors
    val nameError: String? = null,
    val categoryError: String? = null,
    val valueError: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: AntiqueRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editItemId: Int? = savedStateHandle["itemId"]

    private val _uiState = MutableStateFlow(AddEditUiState(itemId = editItemId))
    val uiState: StateFlow<AddEditUiState> = _uiState

    init {
        editItemId?.let { id ->
            viewModelScope.launch {
                repository.getItemById(id)?.let { item ->
                    _uiState.value = _uiState.value.copy(
                        name = item.name,
                        category = item.category,
                        description = item.description,
                        estimatedValue = item.estimatedValue.toString(),
                        imageUrl = item.imageUrl,
                        dateAcquired = item.dateAcquired,
                        condition = item.condition,
                        notes = item.notes,
                        provenance = item.provenance
                    )
                }
            }
        }
    }

    fun onNameChanged(v: String) { _uiState.value = _uiState.value.copy(name = v, nameError = null) }
    fun onCategoryChanged(v: String) { _uiState.value = _uiState.value.copy(category = v, categoryError = null) }
    fun onDescriptionChanged(v: String) { _uiState.value = _uiState.value.copy(description = v) }
    fun onEstimatedValueChanged(v: String) { _uiState.value = _uiState.value.copy(estimatedValue = v, valueError = null) }
    fun onImageUrlChanged(v: String) { _uiState.value = _uiState.value.copy(imageUrl = v) }
    fun onDateAcquiredChanged(v: String) { _uiState.value = _uiState.value.copy(dateAcquired = v) }
    fun onConditionChanged(v: String) { _uiState.value = _uiState.value.copy(condition = v) }
    fun onNotesChanged(v: String) { _uiState.value = _uiState.value.copy(notes = v) }
    fun onProvenanceChanged(v: String) { _uiState.value = _uiState.value.copy(provenance = v) }

    fun saveItem() {
        val state = _uiState.value
        var hasError = false

        if (state.name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "Name is required")
            hasError = true
        }
        if (state.category.isBlank()) {
            _uiState.value = _uiState.value.copy(categoryError = "Category is required")
            hasError = true
        }
        val value = state.estimatedValue.toDoubleOrNull()
        if (state.estimatedValue.isNotBlank() && value == null) {
            _uiState.value = _uiState.value.copy(valueError = "Enter a valid number")
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            val item = AntiqueItem(
                id = editItemId ?: 0,
                name = state.name,
                category = state.category,
                description = state.description,
                estimatedValue = value ?: 0.0,
                imageUrl = state.imageUrl,
                dateAcquired = state.dateAcquired,
                condition = state.condition,
                notes = state.notes,
                provenance = state.provenance
            )
            if (editItemId == null) {
                val newId = repository.insertItem(item).toInt()
                repository.insertEvent(
                    CollectionEvent(antiqueId = newId, eventType = EventType.ITEM_ADDED)
                )
            } else {
                repository.updateItem(item)
                repository.insertEvent(
                    CollectionEvent(antiqueId = editItemId, eventType = EventType.ITEM_EDITED)
                )
            }
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}