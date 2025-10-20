package com.chaitnya.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onNavigateToRegister : ()-> Unit
){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {onNavigateToRegister()}
        ) {
            Text("register")
        }
    }

}

@Composable
fun RegisterScreen( modifier: Modifier = Modifier , onNavigate:()->Unit ){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp , Alignment.CenterVertically)
    ) {
        Text(
            " hii channo bhaa \n kaah haal chal",
            modifier= Modifier.padding(8.dp)
        )
        Button(
            onClick = {onNavigate()}
        ) {
            Text("pop")
        }
    }
}
