package com.example.app

sealed class Routes(val route: String) {
    object Login : Routes("Login")
}