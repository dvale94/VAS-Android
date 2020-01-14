package com.GCI.attendancesystem.networking.endpoints

enum class Services(val path: String) {
    Base("http://commandsapiname.azurewebsites.net"),
    LoginService("/ApplicationUser/Login"),
    Authenticate("/UserProfile")
}