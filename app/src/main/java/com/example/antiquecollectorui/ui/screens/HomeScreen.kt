package com.example.antiquecollectorui.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class AntiqueItem(
    val id: Int,
    val name: String,
    val category: String,
    val value: Double
)

@Composable
fun HomeScreen(
    onItemClick: () -> Unit
) {

    /*
    ==================================
back-end
    ==================================

    val viewModel: AntiqueViewModel = viewModel()

    val antiqueList by viewModel.antiqueList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAntiques()
    }
    */

    val antiqueList = remember {
        listOf(
            AntiqueItem(1, "Victorian Vase", "Ceramics", 250.0),
            AntiqueItem(2, "Pocket Watch", "Metalwork", 420.0),
            AntiqueItem(3, "Bronze Statue", "Sculpture", 900.0),
            AntiqueItem(4, "Antique Lamp", "Decor", 310.0),
            AntiqueItem(5, "Wooden Chest", "Furniture", 650.0)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Antique Collection",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(antiqueList) { item ->
                AntiqueCard(
                    item = item,
                    onClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun AntiqueCard(
    item: AntiqueItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text("Category: ${item.category}")

            Text("Estimated Value: £${item.value}")
        }
    }
}