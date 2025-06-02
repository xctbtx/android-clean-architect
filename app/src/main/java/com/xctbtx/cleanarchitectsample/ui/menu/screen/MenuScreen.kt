package com.xctbtx.cleanarchitectsample.ui.menu.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MenuItem(val title: String, val icon: ImageVector, val onClick: () -> Unit = {})

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    val menuItems = listOf(
        MenuItem("Profile", Icons.Default.Face, onClick = {}),
        MenuItem("Settings", Icons.Default.Settings),
        MenuItem("Marketplace", Icons.Default.ShoppingCart),
        MenuItem("Message requests", Icons.Default.Warning),
        MenuItem("Archive", Icons.Default.Delete),
        //more
        MenuItem("Channel invites", Icons.Default.Info),
        MenuItem("AI Studio chats", Icons.Default.Face),
        MenuItem("Create an AI", Icons.Default.Add),
        //communities
        MenuItem("Create community", Icons.Default.Add),
        //fb groups
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Messenger") },
                modifier = Modifier.background(Color.Blue),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(menuItems) { item ->
                MenuItemCard(item)
            }
        }
    }
}

@Composable
fun MenuItemCard(item: MenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { item.onClick.invoke() },
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.Transparent,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberVectorPainter(item.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}