package com.chaitnya.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit,
    navigateToNotesNavGraph: () -> Unit
) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.isError.collectAsState(initial = null)

    val onEmailChange = remember { { text: String -> viewModel.onEmailChange(text) } }
    val onPasswordChange = remember { { text: String -> viewModel.onPasswordChange(text) } }

    LaunchedEffect(uiState.navigateToNotesNavGraph) {
        if (uiState.navigateToNotesNavGraph) {
            navigateToNotesNavGraph()
        }
    }
    val localContext = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let { Toast.makeText(localContext, it, Toast.LENGTH_SHORT).show() }
    }

    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(6.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GradientBorderOutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholderText = "Enter Email",
                labelText = "Email"
            )
            GradientBorderOutlinedTextField(
                value = password,
                placeholderText = "Enter Password",
                onValueChange = onPasswordChange,
                labelText = "Password",
                isPassword = true
            )
            Button(
                onClick = {
                    viewModel.login()
                }
            ) {
                Text("login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Don't have account?",
                    color = Color.LightGray
                )
                Text(
                    text = "Click here",
                    color = Color(0xFF42A5F5),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            onNavigateToRegister()
                        }
                )
            }
        }
    }
}


@Composable
fun RegisterScreen(modifier: Modifier = Modifier, navigateToLogin:()->Unit,navigateToNotesNavGraph:()-> Unit ){
    val viewModel = hiltViewModel<AuthViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    var confirmPassword by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val onEmailChange = remember { { text: String -> viewModel.onEmailChange(text) } }
    val onPasswordChange = remember { { text: String -> viewModel.onPasswordChange(text) } }
    val localContext = LocalContext.current
    val errorMessage by viewModel.isError.collectAsState(initial = null)
    LaunchedEffect(errorMessage) {
        errorMessage?.let { Toast.makeText(localContext, it, Toast.LENGTH_SHORT).show() }
    }
    LaunchedEffect(uiState) {
        if (uiState.navigateToNotesNavGraph) {
            navigateToNotesNavGraph()
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp , Alignment.CenterVertically)
    ) {
        if (isLoading) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(6.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }else {
            GradientBorderOutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholderText = "Enter Email",
                labelText = "Email"
            )
            GradientBorderOutlinedTextField(
                value = password,
                placeholderText = "Enter Password",
                onValueChange = onPasswordChange,
                labelText = "Password",
                isPassword = true
            )
            GradientBorderOutlinedTextField(
                value = confirmPassword,
                placeholderText = "Enter Confirm Password",
                onValueChange = { confirmPassword = it },
                labelText = "Confirm Password",
                isPassword = true
            )
            Button(
                onClick = {
                    viewModel.register()
                }
            ) {
                Text("register")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "have an account?",
                    color = Color.LightGray
                )
                Text(
                    text = "Login here",
                    color = Color(0xFF42A5F5),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            navigateToLogin()
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientBorderOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "Enter Username",
    labelText: String = "Username",
    gradientColors: List<Color> = listOf(Color(0xFFADD8E6), Color(0xFFFFC0CB)),
    placeholderColor: Color = Color.Gray,
    isPassword: Boolean = false
) {
    val gradientBrush = Brush.linearGradient(colors = gradientColors)
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = placeholderColor) },
        label = { Text(labelText) },
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
        singleLine = true,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(2.dp, brush = gradientBrush, RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Color.Black
        )
    )
}
