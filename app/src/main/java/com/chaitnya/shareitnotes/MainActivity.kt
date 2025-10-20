package com.chaitnya.shareitnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chaitnya.shareitnotes.navigation.AuthNavGraph
import com.chaitnya.shareitnotes.navigation.BaseNavGraph
import com.chaitnya.shareitnotes.ui.theme.ShareItNotesTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShareItNotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = AuthNavGraph.Dest.Root) {
                        listOf<BaseNavGraph>(
                            AuthNavGraph
                        ).forEach {
                            it.build(
                                modifier = Modifier.padding(innerPadding),
                                navController,
                                this
                            )
                        }
                    }
                }
            }
        }
    }
}