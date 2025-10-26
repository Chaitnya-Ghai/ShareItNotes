package com.chaitnya.notes.ui.notes

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.chaitnya.notes.domain.model.Note
import com.chaitnya.notes.ui.ui.theme.ShareItNotesTheme

val listOfColors = listOf(
    Color(0xFFE0E6F4),
    Color(0xFFF5EBF2),
    Color(0xFFFCF4A5),
    Color(0xFFFBC8C9),
    Color(0xFFCCEDED)
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(modifier: Modifier = Modifier, goToAddEditNoteScreen:(String?)-> Unit , goToEditProfile:()-> Unit , navigateToSharedNotes:()-> Unit ) {
    val viewModel = hiltViewModel<NotesViewModel>()
    val notesList by viewModel.notes.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Notes")
                },
                actions = {
                    IconButton(onClick = {
                        navigateToSharedNotes()
                    }){
                        Icon(Icons.Default.Share , null)
                    }

                    IconButton(onClick = {
                        goToEditProfile()
                    }){
                        Icon(Icons.Default.AccountCircle , null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { goToAddEditNoteScreen(null) },
                containerColor = listOfColors.random(),
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(18.dp),
                    tint = Color.Black,
                )
            }
        }
    ) {
        if (notesList.isEmpty()) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No notes yet ✨",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        } else {
            NoteScreenContent(
                modifier = modifier.padding(it),
                notes = notesList,
                onDelete = viewModel::deleteNote,
                goToAddEditNoteScreen = goToAddEditNoteScreen
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onDelete: (String) -> Unit,
    goToAddEditNoteScreen: (String?) -> Unit
) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
                .fillMaxSize()
        ) {
            items(
                items = notes,
                key = { it.id },
                contentType = { it.id }
            ) { note ->
                NoteCard(
                    note = note,
                    colors = listOfColors,
                    onClick = { goToAddEditNoteScreen(note.id) },
                    onLongClick = { onDelete(note.id) },
                )
            }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(
    note: Note,
    colors: List<Color>,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val backgroundColor = remember(note.id) {
        if (note.shared) Color(0xFFD1F9DD)
        else colors.random()
    }
    val scale = remember { Animatable(0.8f) }
    LaunchedEffect(Unit) { scale.animateTo(1f, animationSpec = tween(300)) }

    Column(
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (note.imgUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = note.imgUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 100.dp, max = 180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            )
        }

        Text(
            text = note.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = note.content,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.DarkGray
            ),
            maxLines = 7,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PrevNoteScreenContent() {
    ShareItNotesTheme {
        val list = listOf(
            Note("1", "My First Note", "Preview example", "test@example.com", false, "", "https://via.placeholder.com/150", System.currentTimeMillis()),
            Note("2", "Another Note", "Second preview note", "test@example.com\ntest@example.com", true, "", "https://via.placeholder.com/150", System.currentTimeMillis()),
            Note("3", "Third Note", "This is third preview note", "test@example.com", false, "", "https://via.placeholder.com/150", System.currentTimeMillis())
        )
        NoteScreenContent(
            notes = list,
            goToAddEditNoteScreen = {},
            onDelete = {}
        )
    }
}