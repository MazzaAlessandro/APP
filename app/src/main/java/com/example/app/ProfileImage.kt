package com.example.app

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileImage(
    uri : String,
    clickable : Boolean = true,
    size : Dp = 120.dp,
    selected : (String) -> Unit
){

    val imageUri = rememberSaveable { mutableStateOf("")}

    if (uri.isNotBlank())
        imageUri.value = uri

    val painter = rememberImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.ic_user
        else
            imageUri.value
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? ->
        uri?.let {
            imageUri.value = it.toString()
        }
    }

    Card(shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(size))
    {
            Image(painter = painter,
                contentDescription = "profilePic",
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { if(clickable)launcher.launch("image/*")},
                contentScale = ContentScale.Crop)
    }

    selected(imageUri.value)

}