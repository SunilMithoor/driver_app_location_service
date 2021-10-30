@file:JvmName("DialogUtils")

package com.app.extension

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.app.domain.extention.calendar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun Dialog.customView(@LayoutRes layoutId: Int, isCancelable: Boolean = true) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(layoutId)
    setCancelable(isCancelable)
}

fun Dialog.customView(view: View, isCancelable: Boolean = true) {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(view)
    setCancelable(isCancelable)
}

fun Context.alertMaterial(f: MaterialAlertDialogBuilder.() -> Unit) {
    val dialog = MaterialAlertDialogBuilder(this)
    f(dialog)
    dialog.show()
}

fun Context.alertMaterial(@StyleRes themeResId: Int, f: MaterialAlertDialogBuilder.() -> Unit) {
    val dialog = MaterialAlertDialogBuilder(this, themeResId)
    f(dialog)
    dialog.show()
}


fun Context.alert(f: MaterialAlertDialogBuilder.() -> Unit) {
    val dialog = MaterialAlertDialogBuilder(this)
    f(dialog)
    dialog.show()
}

fun Context.alert(@StyleRes themeResId: Int, f: MaterialAlertDialogBuilder.() -> Unit) {
    val dialog = MaterialAlertDialogBuilder(this, themeResId)
    f(dialog)
    dialog.show()
}

fun View.snackBar(message: String?) {
    message?.let {
        Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
    }
}

fun View.snackBar(message: String?, actionMessage: String, f: View.() -> Unit) {
    message?.let {
        Snackbar.make(this, it, Snackbar.LENGTH_SHORT)
            .setAction(actionMessage) {
                f()
            }.show()
    }
}

fun View.longSnackBar(message: String?) {
    message?.let {
        Snackbar.make(this, it, Snackbar.LENGTH_LONG).show()
    }
}

fun View.longSnackBar(message: String?, actionMessage: String, f: View.() -> Unit) {
    message?.let {
        Snackbar.make(this, it, Snackbar.LENGTH_LONG)
            .setAction(actionMessage) {
                f()
            }.show()
    }
}


fun View.snackBar(@StringRes resId: Int) = Snackbar.make(this, resId, Snackbar.LENGTH_SHORT).show()

fun View.snackBar(@StringRes resId: Int, actionMessage: String, f: View.() -> Unit) {
    Snackbar.make(this, resId, Snackbar.LENGTH_SHORT)
        .setAction(actionMessage) {
            f()
        }.show()
}

fun View.longSnackBar(@StringRes resId: Int) =
    Snackbar.make(this, resId, Snackbar.LENGTH_LONG).show()

fun View.longSnackBar(@StringRes resId: Int, actionMessage: String, f: View.() -> Unit) {
    Snackbar.make(this, resId, Snackbar.LENGTH_LONG)
        .setAction(actionMessage) {
            f()
        }.show()
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.longToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.longToast(@StringRes resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()

fun Context.datePicker(
    year: Int = calendar.get(Calendar.YEAR),
    month: Int = calendar.get(Calendar.MONTH),
    day: Int = calendar.get(Calendar.DAY_OF_MONTH),
    dialog: (DatePickerDialog) -> Unit,
    result: (String, String, String) -> Unit
) {
    val pickerDialog = DatePickerDialog(this, { _, y, m, d ->
        result(String.format("%04d", y), String.format("%02d", (m + 1)), String.format("%02d", d))
    }, year, month, day)
    dialog(pickerDialog)
    pickerDialog.show()
}


