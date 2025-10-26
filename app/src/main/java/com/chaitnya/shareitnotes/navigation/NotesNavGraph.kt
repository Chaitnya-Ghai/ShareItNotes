package com.chaitnya.shareitnotes.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.chaitnya.notes.ui.addEdit.AddEditScreen
import com.chaitnya.notes.ui.notes.NotesScreen
import kotlinx.serialization.Serializable

object NotesNavGraph : BaseNavGraph {
    sealed interface Dest{

        @Serializable
        data object Root : Dest

        @Serializable
        data object NoteList : Dest

        @Serializable
        data class NoteDetail(val noteId : String?) : Dest
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun build(
        modifier: Modifier,
        navController: NavController,
        navGraphBuilder: NavGraphBuilder,
    ) {
        navGraphBuilder.navigation<Dest.Root>(
            startDestination = Dest.NoteList
        ){
            composable<Dest.NoteList> {
                NotesScreen(
                    modifier,
                    goToAddEditNoteScreen = {
                        navController.navigate(Dest.NoteDetail(it))
                    },
                    goToEditProfile = {  navController.navigate(AuthNavGraph.Dest.ResetPassword )}
                )
            }
            composable<Dest.NoteDetail> { noteDetail ->
                val id = noteDetail.toRoute<Dest.NoteDetail>().noteId
                AddEditScreen(
                    modifier,
                    id=id
                ) { navController.popBackStack() }
            }

        }
    }
}