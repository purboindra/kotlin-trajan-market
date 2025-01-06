package com.example.trajanmarket.ui.screens.register

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
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
import com.example.trajanmarket.ui.components.TextFieldCompose
import com.example.trajanmarket.ui.navigation.Login
import com.example.trajanmarket.ui.navigation.OsmdroidMaps
import com.example.trajanmarket.ui.theme.blue100
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.ui.theme.yellow80
import com.example.trajanmarket.utils.LocationHelper
import com.example.trajanmarket.utils.VerticalSpacer
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    
    val registerState by registerViewModel.registerState.collectAsState()
    
    val context = LocalContext.current
    
    val userName by registerViewModel.userName.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val hasShowPassword by registerViewModel.showPassword.collectAsState()
    val userNameError by registerViewModel.userNameError.collectAsState()
    val passwordError by registerViewModel.passwordError.collectAsState()
    val address by registerViewModel.address.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val phoneNumber by registerViewModel.phoneNumber.collectAsState()
    val phoneNumberError by registerViewModel.phoneNumberError.collectAsState()
    val emailError by registerViewModel.emailError.collectAsState()
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val activity = LocalContext.current as? Activity
    
    val locationHelper = remember { LocationHelper(context) }
    
    val requestLocationPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            if (hasFineLocation) {
                println("Location permission granted")
            } else {
                println("Location permission denied")
                scope.launch {
                    snackbarHostState.showSnackbar("Location permission denied")
                }
                activity?.let {
                    locationHelper.requestPermission(it)
                }
            }
        }
    
    LaunchedEffect(Unit) {
        println("Requesting location permission")
        locationHelper.setPermissionLauncher(requestLocationPermissions)
        locationHelper.requestLocationPermission(
            activity = activity!!,
            locationPermissionLauncher = requestLocationPermissions
        )
    }
    
    
    LaunchedEffect(registerState) {
        if (registerState is State.Failure) {
            val throwable: Throwable = (registerState as State.Failure).throwable
            scope.launch {
                snackbarHostState.showSnackbar(throwable.message ?: "Unknown Error Occurred")
            }
        } else if (registerState is State.Succes) {
            navHostController.navigate(route = "main")
        }
    }
    
    val annotatedText = buildAnnotatedString {
        append("Have an account?")
        pushStringAnnotation(tag = "SIGN_IN", annotation = "sign_in")
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
            )
        ) {
            append(" Sign In")
        }
        pop()
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
                            registerViewModel.onUserNameErrorChange("Username cannot be empty!")
                        }
                        
                        if (password.isBlank()) {
                            registerViewModel.onPasswordErrorChange("Password cannot be empty!")
                            return@ElevatedButton
                        }
                        
                        if (passwordError == null && userNameError == null) {
                            keyboardController?.hide()
                            registerViewModel.register()
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
                    if (registerState is State.Loading) {
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
                            text = "Register",
                            color = Color.White
                        )
                    }
                }
                10.VerticalSpacer()
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    BasicText(annotatedText,
                        modifier = Modifier.clickable {
                            navHostController.navigate(Login)
                        },
                        onTextLayout = { textLayoutResult ->
                            // You can add logic here to calculate offsets if needed
                        }
                    )
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
                    Text("Register With Google", color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            item {
                Text("Hello", fontSize = 20.sp, fontWeight = FontWeight.W700)
                18.VerticalSpacer()
                Text("Let’s Sign You Up", fontSize = 20.sp, fontWeight = FontWeight.W700)
                Text(
                    "Welcome to Trajan Market App", fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.DarkGray
                )
                40.VerticalSpacer()
                TextFieldCompose(
                    value = userName,
                    onValueChanged = {
                        registerViewModel.onUserNameChange(it)
                    },
                    isError = userNameError != null,
                    label = {
                        Text(text = "Username", color = Color.Gray)
                    },
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
                TextFieldCompose(
                    value = email,
                    onValueChanged = {
                        registerViewModel.onEmailChange(it)
                    },
                    isError = emailError != null,
                    label = {
                        Text(text = "Email", color = Color.Gray)
                    },
                    prefix = {
                        Icon(
                            Icons.Rounded.Email,
                            contentDescription = "Email",
                            tint = Color.Gray
                        )
                    }
                )
                if (emailError != null)
                    Text(
                        text = emailError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                10.VerticalSpacer()
                TextFieldCompose(
                    value = password,
                    onValueChanged = {
                        registerViewModel.onPasswordChange(it)
                    },
                    isError = passwordError != null,
                    label = {
                        Text(text = "Password", color = Color.Gray)
                    },
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
                                    registerViewModel.toggleShowPassword()
                                }
                        ) {
                            Icon(
                                painterResource(id = if (hasShowPassword) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                contentDescription = "password",
                                tint = Color.Gray
                            )
                        }
                    },
                    visualTransformation = if (hasShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                10.VerticalSpacer()
                TextFieldCompose(
                    value = phoneNumber,
                    onValueChanged = {
                        registerViewModel.onPhoneNumberError(it)
                    },
                    isError = phoneNumberError != null,
                    label = {
                        Text(text = "Phone Number", color = Color.Gray)
                    },
                    prefix = {
                        Icon(
                            Icons.Rounded.Contacts,
                            contentDescription = "Phone Number",
                            tint = Color.Gray
                        )
                    }
                )
                if (phoneNumberError != null)
                    Text(
                        text = phoneNumberError ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                10.VerticalSpacer()
                TextFieldCompose(
                    value = address,
                    onValueChanged = {
                        registerViewModel.onUserNameChange(it)
                    },
                    isError = userNameError != null,
                    label = {
                        Text(text = "Your Address", color = Color.Gray)
                    },
                    prefix = {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = "Username",
                            tint = Color.Gray
                        )
                    },
                    readOnly = true,
                    enabled = false,
                    onClick = {
                        if (activity != null) {
                            locationHelper.requestLocationPermission(
                                activity,
                                requestLocationPermissions
                            )
                            
                            registerViewModel.setAddress()
                            
                            navHostController.navigate(OsmdroidMaps)
                        } else {
                            println("Activity is null")
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