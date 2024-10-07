package com.example.todoappnew.pages

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoappnew.AuthState
import com.example.todoappnew.AuthViewModel
import com.example.todoappnew.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Configure Google Sign In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context as Activity, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    scope.launch {
                        authViewModel.firebaseAuthWithGoogle(token)
                    }
                }
            } catch (e: ApiException) {
                Log.e("LoginPage", "Google sign in failed", e)
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Log.d("LoginPage", "Authentication successful, navigating to TodoApp")
                navController.navigate("todoApp") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Log.e("LoginPage", "Authentication error: $errorMessage")
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF81248A))
            .offset(y = (-30).dp)
    ) {
        // Logo and Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 0.dp, top = 0.dp)
                    .offset(y = 30.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Learn Graphic and UI/UX designing in Hindi\n" +
                        "for free with live projects.",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Email input
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email Address", color = Color(0xFF767676)) },
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Mail Icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.width(280.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password input
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password", color = Color(0xFF767676)) },
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color.Gray
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.width(280.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Login button
            Button(
                onClick = {
                    scope.launch {
                        authViewModel.login(email, password)
                    }
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
                    text = "Login",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Forgot Password
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(24.dp)
                    .clickable { /* TODO: Implement forgot password functionality */ },
                contentAlignment = Alignment.CenterEnd
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Forgot Password?",
                    textAlign = TextAlign.End,
                    color = Color(0xFFFD8C00)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "or login with",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Google Login
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        } catch (e: Exception) {
                            Log.e("LoginPage", "Failed to launch Google Sign-In", e)
                            Toast.makeText(context, "Failed to start Google Sign-In", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .height(50.dp)
                    .width(280.dp)
                    .shadow(2.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Continue with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Text(
                        text = "Don't have an account?",
                        modifier = Modifier.padding(end = 5.dp),
                        color = Color.White
                    )
                    Text(
                        text = "Register now",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navController.navigate("register")
                        },
                        color = Color(0xFFFD8C00)
                    )
                }
            }

            Spacer(modifier = Modifier.height(70.dp))

            Box(
                modifier = Modifier
                    .width(350.dp)
                    .height(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Text(
                        text = "By signing up, you agree with our",
                        modifier = Modifier.padding(end = 4.dp),
                        color = Color.White
                    )
                    Text(
                        text = "Terms & Conditions",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { /* TODO: Navigate to Terms & Conditions */ },
                        color = Color(0xFFFD8C00)
                    )
                }
            }
        }
    }
}