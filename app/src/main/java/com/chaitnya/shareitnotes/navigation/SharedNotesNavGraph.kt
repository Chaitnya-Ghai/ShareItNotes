package com.chaitnya.shareitnotes.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.chaitnya.sharedNotes.ui.SharedNotesScreen
import kotlinx.serialization.Serializable

object SharedNotesNavGraph: BaseNavGraph {
    sealed interface Dest{
        @Serializable
        data object Root: Dest

        @Serializable
        data object SharedNotes: Dest
    }


    override fun build(
        modifier: Modifier,
        navController: NavController,
        navGraphBuilder: NavGraphBuilder,
    ) {
        navGraphBuilder.navigation<Dest.Root>(startDestination = Dest.SharedNotes){
            composable<Dest.SharedNotes> {
                SharedNotesScreen(modifier)
            }
        }
    }
}