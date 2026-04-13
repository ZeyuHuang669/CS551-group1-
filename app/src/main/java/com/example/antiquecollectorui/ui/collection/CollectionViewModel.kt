package com.example.antiquecollectorui.ui.collection

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.antiquecollectorui.data.repository.AntiqueRepository
    import com.example.antiquecollectorui.domain.model.AntiqueItem
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.*
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    data class CollectionUiState(
        val items: List<AntiqueItem> = emptyList(),
        val searchQuery: String = "",
        val selectedCategory: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    @HiltViewModel
    class CollectionViewModel @Inject constructor(
        private val repository: AntiqueRepository
    ) : ViewModel() {

        private val _searchQuery = MutableStateFlow("")
        private val _selectedCategory = MutableStateFlow("")

        val uiState: StateFlow<CollectionUiState> = combine(
            repository.getAllItems(),
            _searchQuery,
            _selectedCategory
        ) { items, query, category ->
            val filtered = items
                .filter { item ->
                    (query.isBlank() || item.name.contains(query, ignoreCase = true) ||
                            item.category.contains(query, ignoreCase = true))
                            && (category.isBlank() || item.category == category)
                }
            CollectionUiState(items = filtered, searchQuery = query, selectedCategory = category)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CollectionUiState())

        fun onSearchQueryChanged(query: String) { _searchQuery.value = query }
        fun onCategorySelected(category: String) { _selectedCategory.value = category }

        fun deleteItem(item: AntiqueItem) {
            viewModelScope.launch { repository.deleteItem(item) }
        }
    }
