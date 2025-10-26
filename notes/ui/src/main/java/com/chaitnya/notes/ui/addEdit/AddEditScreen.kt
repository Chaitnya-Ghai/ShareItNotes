package com.chaitnya.notes.ui.addEdit

import android.graphics.BitmapFactory
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.chaitnya.notes.ui.ui.theme.ShareItNotesTheme
import kotlin.time.Duration.Companion.milliseconds

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    id: String?,
    onNavigateBack: () -> Unit
) {
    val viewModel = hiltViewModel<AddEditViewModel>()

    val isEdit by viewModel.isEdit.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val shared by viewModel.shared.collectAsStateWithLifecycle()
    val note by viewModel.editNote.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.isError.collectAsState(initial = null)
    val imgData by viewModel.imgData.collectAsStateWithLifecycle()
    val imgUrl by viewModel.imgUrl.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val imgPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { resultUri->
            resultUri?.let {
                // Convert URI → ByteArray
                val inputStream = context.contentResolver.openInputStream(it)
                val byteArray = inputStream?.readBytes() ?: ByteArray(0)
                viewModel.setUriAsByteArray(byteArray)
            }
        }
    )
    LaunchedEffect(id) {
        id?.let { viewModel.loadNoteData(it) }
    }

    // Show snackbar on error
    LaunchedEffect(errorMessage) {
        errorMessage?.let { snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Long) }
    }

    // Navigate back
    LaunchedEffect(viewModel.navigateBack) {
        viewModel.navigateBack.collect { onNavigateBack() }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = if (isEdit) "Edit Note" else "Add Note",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveNote() }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "save"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {

            // Add/Edit Screen
            if (isEdit && note != null) {
                AddEditScreenContent(
                    modifier = modifier.padding(padding),
                    title = title,
                    content = content,
                    shared = shared,
                    onTitleChange = viewModel::setTitle,
                    onContentChange = viewModel::setContent,
                    onSharedChange = viewModel::setShared,
                    imgPickerLauncher = { imgPickerLauncher.launch("image/*") },
                    imgData = imgData,
                    imgUrl=imgUrl
                )
            } else {
                AddEditScreenContent(
                    modifier = modifier.padding(padding),
                    title = title,
                    content = content,
                    shared = shared,
                    onTitleChange = viewModel::setTitle,
                    onContentChange = viewModel::setContent,
                    onSharedChange = viewModel::setShared,
                    imgPickerLauncher = { imgPickerLauncher.launch("image/*") }
                )
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEditScreenContent(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    shared: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSharedChange: (Boolean) -> Unit,
    imgPickerLauncher: () -> Unit,
    imgData: ByteArray? = null,
    imgUrl: String? = null
) {
    Column(modifier = modifier.padding(4.dp)) {
        ImageSection(
            imgData = imgData,
            onPickImage = imgPickerLauncher,
            onRemoveImage = {},
            imgUrl = imgUrl
        )
        TextField(
            value = title,
            onValueChange = onTitleChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
            colors = TextFieldDefaults.colors(
                Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                focusedContainerColor = Color.White
                ),
            placeholder = { Text("Title") }
        )
        // Content
        TextField(
            value = content,
            onValueChange = onContentChange,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            placeholder = { Text("Start typing...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            // Additional properties to ensure no visual indicators
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color(0xFFE3F2FD)),
            verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = shared,
                onCheckedChange = onSharedChange
            )
            Text("Shared")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun DPrev() {
    ShareItNotesTheme {
        AddEditScreenContent(
            title = "Title",
            content = "Content",
            shared = true,
            onTitleChange = {},
            onContentChange = {},
            onSharedChange = {},
            imgPickerLauncher = { }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewAddEditScreen() {
    ShareItNotesTheme {
        // Mock data for Add/Edit
        val sampleTitle = "Sample Note Title"
        val sampleContent = "This is a sample note content to preview your Add/Edit screen."
        val sampleShared = true

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text("Edit Note") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {  }) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "save",
                                tint = Color(0xFF66BB6A)
                            )
                        }
                    }
                )
            }
        ) { padding ->
            AddEditScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                title = sampleTitle,
                content = sampleContent,
                shared = sampleShared,
                onTitleChange = {},
                onContentChange = {},
                onSharedChange = {},
                imgPickerLauncher = { }
            )
        }
    }
}

@Composable
fun ImageSection(
    imgData: ByteArray?,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit,
    imgUrl: String? = null
) {
    val imageBitmap = remember(imgData) {
        imgData?.takeIf { it.isNotEmpty() }?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .heightIn(min = 180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF8FBFF), Color(0xFFE3F2FD))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                imageBitmap != null -> {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onPickImage() },
                        contentScale = ContentScale.Crop
                    )
                }
                imgUrl?.isNotEmpty() == true -> {
                    Image(
                        painter = rememberAsyncImagePainter(model = imgUrl),
                        contentDescription = "Note Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onPickImage() },
                        contentScale = ContentScale.Crop
                    )
                }
                // Case 3 No image selected yet
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Add Image",
                            tint = Color.Gray,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap below to add an image",
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ElevatedButton(
                            onClick = onPickImage,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Pick image")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Select Image")
                        }
                    }
                }
            }

            // Overlay buttons for remove / replace (if image exists)
            if (imageBitmap != null || imgUrl?.isNotEmpty() == true) {
                // Remove Button
                IconButton(
                    onClick = onRemoveImage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Image",
                        tint = Color.Red
                    )
                }

                // Replace Button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable { onPickImage() }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Replace Image",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


