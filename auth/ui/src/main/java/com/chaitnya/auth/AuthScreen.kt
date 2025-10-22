package com.chaitnya.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onNavigateToRegister : ()-> Unit,
    navigateToNotesNavGraph : ()-> Unit
){
    val viewModel = hiltViewModel<AuthViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val isLogin = viewModel.isLogin.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState.navigateToNotesNavGraph){
            navigateToNotesNavGraph()
        }
    }

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
fun RegisterScreen( modifier: Modifier = Modifier , popToRegisterScreen:()->Unit ){
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
            onClick = {popToRegisterScreen()}
        ) {
            Text("pop")
        }
    }
}
