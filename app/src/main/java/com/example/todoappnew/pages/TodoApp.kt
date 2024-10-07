package com.example.todoappnew.pages

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoappnew.AuthState
import com.example.todoappnew.AuthViewModel
import com.example.todoappnew.TodoItem
import com.example.todoappnew.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    todoViewModel: TodoViewModel
) {
    val authState by authViewModel.authState.observeAsState()
    val todoList by todoViewModel.todoList.observeAsState(emptyList())
    var newTodoTitle by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo("todoApp") { inclusive = true }
                }
            }
            is AuthState.Authenticated -> {
                todoViewModel.loadTodos()
            }
            else -> {}

            //ahsiap
        }
    }

    Scaffold(
        containerColor = Color(0xFF87A2FF),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "To-Do List", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFFFFD7C4))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (authState is AuthState.Authenticated) {
                    val email = (authState as AuthState.Authenticated).user.email
                    Text(
                        text = email ?: "Authenticated User",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }


                TextField(
                    value = newTodoTitle,
                    onValueChange = { newTodoTitle = it },
                    label = { Text("New Todo") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (newTodoTitle.isNotBlank()) {
                            todoViewModel.addTodo(TodoItem(title = newTodoTitle))
                            newTodoTitle = ""
                        } else {
                            Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFD7C4))
                ) {
                    Text("Add Todo", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        authViewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFD8C00),
                        contentColor = Color(0xFFFFF7F7)
                    ),
                    modifier = Modifier
                        .width(280.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(todoList) { todo ->
                        TodoItemView(todo = todo, todoViewModel = todoViewModel)
                    }
                }
            }
        }
    )
}

@Composable
fun TodoItemView(todo: TodoItem, todoViewModel: TodoViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFC4D7FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = todo.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(4.dp),
                    color = Color(0xFF000000)
                )
                Text(
                    text = if (todo.completed) "Done" else "",
                    color = if (todo.completed) Color(0xFF347928) else Color(0xFFCC3636),
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            Checkbox(
                checked = todo.completed,
                onCheckedChange = {
                    todoViewModel.updateTodo(todo.copy(completed = it))
                }
            )

            IconButton(onClick = { todoViewModel.deleteTodo(todo.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
            }
        }
    }
}