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
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun ProfileImage(
    uri : String
){

    val imageUri = rememberSaveable { mutableStateOf("")}

    if (uri.isNullOrBlank())
        imageUri.value = ""
    else
        imageUri.value

    val painter = rememberImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.ic_user
        else
            imageUri.value
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? ->
        uri?.let { imageUri.value = it.toString()}
    }

    Card(shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(120.dp))
    {
            Image(painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*")},
                contentScale = ContentScale.Crop)
    }

}