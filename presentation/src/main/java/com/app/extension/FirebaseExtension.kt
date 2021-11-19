//package com.app.extension
//
//import com.google.firebase.installations.FirebaseInstallations
//
//fun getFirebaseId(): String {
//    var token = ""
//    try {
//        FirebaseInstallations.getInstance().id
//            .addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    return@addOnCompleteListener
//                }
//                // Get new Instance ID token
//                token = task.result
//                println("token-->$token")
//            }
//    } catch (e: Exception) {
//        e.stackTrace
//    }
//    return token;
//}
