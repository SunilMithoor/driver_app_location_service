@file:JvmName("DataValidationUtils")

package com.app.extension

import android.text.TextUtils
import android.util.Patterns
import timber.log.Timber
import java.util.regex.Pattern


private const val emailPattern = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
private const val gstInPattern = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}"
private const val panPattern = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
private const val voterIdPattern = "^([a-zA-Z]){3}([0-9]){7}?$"
private const val drivingLicensePattern = "[A-Z]{2}[0-9]{13}"
private const val ifscCodePattern = "^[A-Z]{4}0[A-Z0-9]{6}$"
private const val accountNoPattern = "[0-9]{9,18}"

/**
 * To Validate email .
 *
 * @param email the email
 * @return true/false
 */
fun validateEmail(email: String?): Boolean {
    return try {
        val pattern = Pattern.compile(
            emailPattern,
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(email)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * To Validate email .
 *
 * @param email the email
 * @return true/false
 */
fun isValidMail(email: String?): Boolean {
    return try {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Validate mobileno .
 *
 * @param phone the mobileno
 * @return true/false
 */
fun isValidMobile(phone: String?): Boolean {
    return try {
        Patterns.PHONE.matcher(phone).matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Validate mobileno
 *
 * @param phoneNumber
 * @return true/false
 */
fun isValidPhoneNumber(phoneNumber: String?): Boolean {
    return try {
        if (!TextUtils.isEmpty(phoneNumber)) {
            Patterns.PHONE.matcher(phoneNumber).matches()
        } else false
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


/**
 * Validate GstIn .
 *
 * @param number the number
 * @return true/false
 */
fun validateGstIn(number: String?): Boolean {
    return try {
        val pattern = Pattern.compile(gstInPattern)
        val matcher = pattern.matcher(number)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Validate IFSC code .
 *
 * @param number the number
 * @return true/false
 */
fun validateIFSCCode(number: String?): Boolean {
    return try {
        val pattern = Pattern.compile(ifscCodePattern)
        val matcher = pattern.matcher(number)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Validate account no .
 *
 * @param number the number
 * @return true/false
 */
fun validateAccountNo(number: String?): Boolean {
    return try {
        val pattern = Pattern.compile(accountNoPattern)
        val matcher = pattern.matcher(number)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Validate Pan no .
 *
 * @param number the number
 * @return true/false
 */
fun validatePan(number: String?): Boolean {
    return try {
        val pattern = Pattern.compile(panPattern)
        val matcher = pattern.matcher(number)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


/**
 *
 * @param aadharNumber number
 * @return true/false
 */

fun validateAaadharNumber(number: String?): Boolean {
    return try {
        val pattern = Pattern.compile("\\d{12}")
        var isValidAadhar = pattern.matcher(number).matches()
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(number!!)
        }
        return isValidAadhar
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * @param number number
 * @return true/false
 */
fun validateVoterId(number: String?): Boolean {
    return try {
        val pattern = Pattern.compile(voterIdPattern)
        val matcher = pattern.matcher(number)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


/**
 * @param number number
 * @return true/false
 */
fun validateDrivingLicense(number: String?): Boolean {
    return try {
        val pattern =
            Pattern.compile(drivingLicensePattern)
        val matcher = pattern.matcher(number)
        matcher.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}


/**
 * @param data data
 * @return dataReturn
 */
fun validateUserName(data: String?): String {
    try {
        return if (data.isNullOrBlank()) {
            "empty"
        } else {
            val digitsOnly = TextUtils.isDigitsOnly(data)
            if (digitsOnly && data.length != 10) {
                Timber.d("digitsOnly-->$digitsOnly")
                "invalid_mobile"
            } else if (!digitsOnly && !isValidMail(data)) {
                Timber.d("digitsOnly-->$digitsOnly")
                "invalid_email"
            } else {
                "success"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "empty"
}


/**
 * @param data data
 * @return dataReturn
 */
fun validatePassword(data: String?): String {
    var dataReturn = ""
    try {
        dataReturn = when {
            data.isNullOrEmpty() -> {
                "empty"
            }
            data.length < 6 -> {
                "invalid"
            }
            else -> {
                "success"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dataReturn
}


