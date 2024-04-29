package com.example.app.util

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowInfo(): WindowInfo{
    val configuration = LocalConfiguration.current

    if(configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
        return WindowInfo(
            screenWidthInfo = when {
                configuration.screenHeightDp < 600 -> WindowInfo.WindowType.Compact
                configuration.screenHeightDp < 840 -> WindowInfo.WindowType.Medium
                else -> WindowInfo.WindowType.Expanded
            },
            screenHeightInfo = when {
                configuration.screenWidthDp < 480 -> WindowInfo.WindowType.Compact
                configuration.screenWidthDp < 900 -> WindowInfo.WindowType.Medium
                else -> WindowInfo.WindowType.Expanded
            },
            screenWidth = configuration.screenHeightDp.dp,
            screenHeight = configuration.screenWidthDp.dp
        )
    }

    return WindowInfo(
        screenWidthInfo = when {
            configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenHeightInfo = when {
            configuration.screenHeightDp < 480 -> WindowInfo.WindowType.Compact
            configuration.screenHeightDp < 900 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

@Composable
fun relative(
    size : Dp
): Dp{
    val windowInfo = rememberWindowInfo()

    return size.times(windowInfo.screenHeight.div(814.0.dp))
}

data class  WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
    ){
    sealed class WindowType{
        object Compact : WindowType()
        object Medium : WindowType()
        object Expanded : WindowType()
    }
}