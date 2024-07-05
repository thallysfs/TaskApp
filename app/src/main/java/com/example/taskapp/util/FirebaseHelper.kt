package com.example.taskapp.util

import com.example.taskapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseHelper {
    companion object {
        // iniciando firebase
        fun getDatabase() = Firebase.database.reference

        // pegando instância do auth
        fun getAuth() = FirebaseAuth.getInstance()

        // Pega id do usuário
        fun getIdUser() = getAuth().currentUser?.uid ?: ""

        // verificando se o usuário está logado
        fun isAuthenticated() = getIdUser().isNotEmpty()

        fun valideError(error: String): Int =
            when {
                error.contains("There is no user record corresponding to this identifier") -> {
                    R.string.account_not_registered
                }
                error.contains("The email address is badly formatted") -> {
                    R.string.invalida_email_register
                }
                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.invalid_password_register
                }
                error.contains("The email address is already in use by another account") -> {
                    R.string.email_in_use
                }
                error.contains("Password should be at least 6 characters") -> {
                    R.string.strong_password_register
                }
                error.contains("The given password is invalid") -> {
                    R.string.invalid_password_register
                }
                error.contains("The supplied auth credential is incorrect, malformed or has expired") -> {
                    R.string.account_not_registered
                }
                else -> {
                    R.string.error_generic
                }
            }
    }
}
