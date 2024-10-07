package com.example.todoappnew.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.credentials.CredentialManager
import androidx.navigation.NavController
import com.example.todoappnew.AuthState
import com.example.todoappnew.AuthViewModel
import com.example.todoappnew.R

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
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
        //Slicing Logo and Subtitle
        Column (horizontalAlignment = Alignment.CenterHorizontally){
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

        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            // Username input
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email Address", color = Color(0xFF767676)) },
                shape = RoundedCornerShape(25.dp),
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
                    authViewModel.login(email, password)
                },
                enabled = authState.value != AuthState.Loading,
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

        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            //Forgot Password
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(24.dp)
                    .clickable { },
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

            //Google Login
            Button(
                onClick = {  },
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
                    // Google Icon
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Google Login Text
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
                        modifier = Modifier.clickable { },
                        color = Color(0xFFFD8C00)
                    )
                }
            }
        }
    }
}