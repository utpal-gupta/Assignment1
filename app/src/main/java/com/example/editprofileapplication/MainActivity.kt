package com.example.editprofileapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.editprofileapplication.ui.theme.EditProfileApplicationTheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModelProvider
import com.example.editprofileapplication.data.UserRepository
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.format.TextStyle
import java.util.regex.Pattern

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditProfileApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                      ProfileScreen(mainViewModel)

                }
            }
        }
    }
}

@Composable
fun ProfileScreen(mainViewModel: MainViewModel){

    val notification= rememberSaveable{ mutableStateOf("")}
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current,notification.value,Toast.LENGTH_LONG).show()
        notification.value=""
    }
    var (name,setName) = rememberSaveable { mutableStateOf("") }
    var (phone,setPhone) = rememberSaveable { mutableStateOf("") }
    var (email,setEmail) = rememberSaveable { mutableStateOf("") }
    var IsEmailinValid by rememberSaveable { mutableStateOf(false) }
    var IsNameinValid by rememberSaveable { mutableStateOf(false) }
    var IsPhoneinValid by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit ){
        mainViewModel.readToLocal.collect{
            setEmail.invoke(it.email)
            setName.invoke(it.name)
            setPhone.invoke(it.phone)
        }
    }
    var enabled by remember { mutableStateOf(false)}

    Column(modifier = Modifier
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(0.dp)
        .background(Color(0xFF6635DF)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 1.dp),
        horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,


        ) {
            Button(
                onClick = { notification.value = "Cancelled" } ,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text(text = "Cancel")
            }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
            .background(Color.White),
            verticalArrangement = Arrangement.SpaceAround


        ) {
            Column(
                modifier=Modifier.fillMaxHeight(1f),
                verticalArrangement = Arrangement.SpaceAround
            ) {

            val imageUri = rememberSaveable { mutableStateOf("") }
            val painter = rememberImagePainter(
                if (imageUri.value.isEmpty()){
                    R.drawable.ic_user}
                else
                    imageUri.value
            )
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let { imageUri.value = it.toString() }
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .offset(0.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally ,

            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp)
                        .offset(0.dp, 0.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable { launcher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                }
                Text(text = "Change profile picture")
            }
            Spacer(modifier = Modifier.height(96.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                val NAME_REGEX = "^[\\p{L} .'-]{4,30}+$"
                TextField(

                    label = { Text(text = "Name", modifier = Modifier.width(350.dp)) },
                    value = name,
                    onValueChange = { setName.invoke(it)},
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    )
                )
                if (!Pattern.matches(NAME_REGEX,name)) {
                    Text(
                        text = "Enter Valid Name",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                val PHONE_REGEX="^[0-9]{10}\$"
                TextField(
                    label = { Text(text = "Phone", modifier = Modifier.width(350.dp)) },
                    value = phone,
                    onValueChange = { newText->
                        setPhone.invoke(newText)
                    },
                    leadingIcon = {
                        Text(text = "+91")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    )
                )
                if (!Pattern.matches(PHONE_REGEX,phone)) {
                    Text(
                        text = "Enter Valid Phone number",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                val EMAIL_VALIDATION_REGEX="^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$"

                TextField(
                    label = { Text(text = "Email", modifier = Modifier.width(350.dp)) },
                    value = email,
                    onValueChange = { newText->
                        setEmail.invoke(newText)
                                    },

                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black
                    ),

                )
                if (!Pattern.matches(EMAIL_VALIDATION_REGEX,email)) {

                    Text(
                        text = "Enter Valid Email",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }
            }
        Spacer(modifier = Modifier.height(110.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if(email.isEmpty()){
                            notification.value="Please enter Email"
                        }
                        else if(name.isEmpty()){
                            notification.value="Please enter Name"
                        }
                        else if(phone.isEmpty()){
                            notification.value="Please enter Phone Number"
                        }
                        else{
                            mainViewModel.writeToLocal(name, phone, email)
                            notification.value = "Profile updated"
                            enabled=false

                        }
                    }

                ) {
                    Text(text = "Save Profile")
                }
            }
        }
    }
    }
}

@Composable
fun ProfileImage(){

}


fun EmailValidation(email:String): Boolean{
    val EMAIL_VALIDATION_REGEX="^(.+)@(.+)\$"
    return Pattern.matches(EMAIL_VALIDATION_REGEX,email)
}
fun NameValidation(name:String):Boolean{
    val NAME_REGEX = "^[\\p{L} .'-]{4,30}+$"
    return Pattern.matches(NAME_REGEX,name)
}
fun PhoneValidation(phone:String):Boolean {
    val PHONE_REGEX="/(\\+\\d{1,3}\\s?)?((\\(\\d{3}\\)\\s?)|(\\d{3})(\\s|-?))(\\d{3}(\\s|-?))(\\d{4})(\\s?(([E|e]xt[:|.|]?)|x|X)(\\s?\\d+))?/g"
    return Pattern.matches(PHONE_REGEX,phone)
}

