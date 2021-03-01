package com.example.androiddevchallenge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DetailView(navController: NavHostController) {
    Scaffold(topBar = {
        Row(Modifier.height(android.R.attr.actionBarSize)) {

            Spacer(modifier = Modifier.weight(1f))
        }
    }) {

    }
}