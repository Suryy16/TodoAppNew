package com.example.todoappnew

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoappnew.pages.LoginPage
import com.example.todoappnew.pages.TodoApp
import com.example.todoappnew.pages.RegisterPage

@Composable
fun TodoAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, todoViewModel: TodoViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginPage(modifier, navController, authViewModel)
        }
        composable("register"){
            RegisterPage(modifier, navController, authViewModel)
        }
        composable("home"){
            TodoApp(modifier, navController, authViewModel, todoViewModel)
        }
    } )
}