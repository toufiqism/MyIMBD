package com.tofiq.myimdb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tofiq.myimdb.data.model.domain.MovieResponse
import com.tofiq.myimdb.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMovieScreen(
    movieViewModel: MovieViewModel,
    onMovieAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var posterUrl by remember { mutableStateOf("") }
    var genres by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    var isYearError by remember { mutableStateOf(false) }
    var isGenresError by remember { mutableStateOf(false) }

    fun validateFields(): Boolean {
        isTitleError = title.isBlank()
        isYearError = year.isBlank() || year.toIntOrNull() == null || year.length != 4
        isGenresError = genres.isBlank()
        return !isTitleError && !isYearError && !isGenresError
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Movie") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    isTitleError = it.isBlank()
                },
                label = { Text("Title") },
                isError = isTitleError,
                modifier = Modifier.fillMaxWidth()
            )
            if (isTitleError) {
                Text("Title cannot be empty", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = year,
                onValueChange = {
                    year = it
                    isYearError = it.isBlank() || it.toIntOrNull() == null || it.length != 4
                },
                label = { Text("Year") },
                isError = isYearError,
                modifier = Modifier.fillMaxWidth()
            )
            if (isYearError) {
                Text("Year must be a 4-digit number", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = posterUrl,
                onValueChange = { posterUrl = it },
                label = { Text("Poster URL") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = genres,
                onValueChange = {
                    genres = it
                    isGenresError = it.isBlank()
                },
                label = { Text("Genres (comma-separated)") },
                isError = isGenresError,
                modifier = Modifier.fillMaxWidth()
            )
            if (isGenresError) {
                Text("Genres cannot be empty", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (validateFields()) {
                        val genreList = genres.split(",").map { it.trim() }
                        val newMovie = MovieResponse.Movie(
                            id = System.currentTimeMillis().toInt(), // Temporary ID
                            title = title,
                            year = year,
                            posterUrl = posterUrl,
                            genres = genreList,
                            actors = "",
                            director = "",
                            plot = "",
                            runtime = "",
                        )
                        movieViewModel.addMovie(newMovie)
                        onMovieAdded()
                    }
                },
                enabled = title.isNotBlank() && year.isNotBlank() && year.length == 4 && genres.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Movie")
            }
        }
    }
}