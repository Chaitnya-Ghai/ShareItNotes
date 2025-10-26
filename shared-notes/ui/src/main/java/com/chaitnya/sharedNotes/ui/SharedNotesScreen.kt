package com.chaitnya.sharedNotes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.chaitnya.sharedNotes.domain.model.SharedNote

@Composable
fun SharedNotesScreen(modifier: Modifier = Modifier) {
    val sharedNoteViewModel = hiltViewModel<SharedNoteViewModel>()
    val notes by sharedNoteViewModel.sharedNotes.collectAsStateWithLifecycle(emptyList())

    SharedNotesScreenContent(
        modifier=modifier,
        list = notes,
        loadNextPage = sharedNoteViewModel::getPaginatedNotes
    )
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedNotesScreenContent(
    modifier: Modifier = Modifier,
    list: List<SharedNote> ,
    loadNextPage :()-> Unit
) {
    val lazyListState = rememberLazyListState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Shared Notes") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        null,
                        modifier = Modifier.clickable { }
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = lazyListState
        ) {
            items(
                count = list.size,
                key = { list[it].id },
                contentType = { list[it].id }
            ) { index ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    val item = list.get(index)
                    Column(modifier= Modifier.fillMaxWidth()){
                        Row (modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier.size(80.dp).background(Color.Black, shape = CircleShape), Alignment.Center) {
                                Text(item.email.first().uppercase())
                            }
                            Spacer(modifier= Modifier.width(8.dp))
                            Text(item.email, style = MaterialTheme.typography.titleSmall)

                        }
                        SubcomposeAsyncImage(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            model = item.imgUrl, loading = {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    Alignment.Center
                                ){
                                    CircularProgressIndicator()
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    Alignment.Center
                                ){
                                    Text(item.email, style = MaterialTheme.typography.titleLarge)
                                }
                            },
                            contentDescription = null,
                        )
                        Spacer(modifier= Modifier.height(12.dp))
                        Text(item.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier= Modifier.height(12.dp))
                        Text(item.content, style = MaterialTheme.typography.bodySmall)
                    }

                }
            }
        }
        val shouldTriggerPagination = remember {
            derivedStateOf {
                val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItemCount = lazyListState.layoutInfo.totalItemsCount

                lastVisibleItem!=null && lastVisibleItem.index >= totalItemCount - 1


            }
        }
        LaunchedEffect(shouldTriggerPagination) {
            if (shouldTriggerPagination.value){
                // trigger pagination
                loadNextPage()
            }
        }
    }

}