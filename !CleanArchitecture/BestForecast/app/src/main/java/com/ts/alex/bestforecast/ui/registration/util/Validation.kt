package com.ts.alex.bestforecast.ui.registration.util

import java.util.regex.Pattern

fun isValidText(text: String) = text.isNotEmpty()

fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean {
    val passwordPattern = "[a-zA-Z0-9!@#$]{8,24}"
    val pattern = Pattern.compile(passwordPattern)
    val matcher = pattern.matcher(password)
    return matcher.matches()
}