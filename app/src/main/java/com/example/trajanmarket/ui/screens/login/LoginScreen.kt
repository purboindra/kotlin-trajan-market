package com.example.trajanmarket.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.trajanmarket.R
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.navigation.Main
import com.example.trajanmarket.ui.theme.blue100
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.ui.theme.yellow80
import com.example.trajanmarket.utils.VerticalSpacer
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    
    val loginState by loginViewModel.loginState.collectAsState()
    
    val userNamePref by loginViewModel.userNamePref.collectAsState(initial = null)
    
    val userName by loginViewModel.userName.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val hasShowPassword by loginViewModel.showPassword.collectAsState()
    val userNameError by loginViewModel.userNameError.collectAsState()
    val passwordError by loginViewModel.passwordError.collectAsState()
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val keyboardController = LocalSoftwareKeyboardController.current
    
    LaunchedEffect(loginState) {
        if (loginState is State.Failure) {
            val throwable: Throwable = (loginState as State.Failure).throwable
            scope.launch {
                snackbarHostState.showSnackbar(throwable.message ?: "Unknown Error Occured")
            }
        } else if (loginState is State.Succes) {
            navHostController.navigate(route = Main)
        }
    }
    
    val annotatedText = buildAnnotatedString {
        append("Don't have an account?")
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
            )
        ) {
            append(" Sign Up")
        }
    }
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(containerColor = Color.Red, snackbarData = data)
            }
        },
        modifier = Modifier
            .statusBarsPadding()
            .safeContentPadding()
            .padding(vertical = 12.dp),
        bottomBar = {
            Column {
                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        if (userName.isBlank()) {
                            loginViewModel.onUserNameErrorChange("Username cannot be empty!")
                        }
                        
                        if (password.isBlank()) {
                            loginViewModel.onPasswordErrorChange("Password cannot be empty!")
                            return@ElevatedButton
                        }
                        
                        if (passwordError == null && userNameError == null) {
                            keyboardController?.hide()
                            loginViewModel.login(userName, password)
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        contentColor = yellow80,
                        containerColor = yellow80,
                        disabledContainerColor = yellow80,
                        disabledContentColor = yellow80
                    )
                ) {
                    if (loginState is State.Loading) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(26.dp),
                                color = grayLight,
                                strokeWidth = 5.dp
                            )
                        }
                    } else {
                        Text(
                            text = "Log In",
                            color = Color.White
                        )
                    }
                }
                10.VerticalSpacer()
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    BasicText(annotatedText)
                }
                10.VerticalSpacer()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Gray.copy(alpha = 0.2f)),
                )
                20.VerticalSpacer()
                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        contentColor = blue100,
                        containerColor = blue100,
                        disabledContainerColor = blue100,
                        disabledContentColor = blue100
                    )
                ) {
                    Text("Log In With Google", color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            item {
                Text("Hello ${userNamePref ?: ""}", fontSize = 20.sp, fontWeight = FontWeight.W700)
                18.VerticalSpacer()
                Text("Let’s Sign You In", fontSize = 20.sp, fontWeight = FontWeight.W700)
                Text(
                    "Welcome back, you’ve been missed!", fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.DarkGray
                )
                40.VerticalSpacer()
                OutlinedTextField(
                    modifier = Modifier.fillParentMaxWidth(),
                    value = userName,
                    onValueChange = {
                        loginViewModel.onUserNameChange(it)
                    },
                    isError = userNameError != null,
                    label = {
                        Text(text = "Username", color = Color.Gray)
                    },
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 18.sp),
                    prefix = {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = "Username",
                            tint = Color.Gray
                        )
                    }
                )
                if (userNameError != null)
                    Text(
                        text = userNameError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                10.VerticalSpacer()
                OutlinedTextField(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 0.dp),
                    value = password,
                    onValueChange = {
                        loginViewModel.onPasswordChange(it)
                    },
                    isError = passwordError != null,
                    label = {
                        Text(text = "Password", color = Color.Gray)
                    },
                    
                    visualTransformation = if (hasShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontSize = 18.sp),
                    prefix = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Username",
                            tint = Color.Gray
                        )
                    },
                    suffix = {
                        Box(
                            Modifier
                                .padding(end = 0.dp)
                                .clickable {
                                    loginViewModel.toggleShowPassword()
                                }
                        ) {
                            Icon(
                                painterResource(id = if (hasShowPassword) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                contentDescription = "password",
                                tint = Color.Gray
                            )
                        }
                    }
                )
                if (passwordError != null)
                    Text(
                        text = passwordError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
            }
        }
    }
}