package com.example.kotlinextensions

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


fun Context.getCompatColor(@ColorRes colorId: Int) = ResourcesCompat.getColor(resources, colorId, null)

fun Context.getCompatDrawable(@DrawableRes drawableId: Int) = AppCompatResources.getDrawable(this, drawableId)!!



fun Context.getMainPendingIntent(randomId: Int): PendingIntent {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        this, randomId, intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    return pendingIntent
}


//notificaiton Builder
fun Context.createNotificationBuilder(title: String = "", body: String = "", dSound: Uri?) =
    NotificationCompat.Builder(this, "")
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .setSound(dSound)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationManager.IMPORTANCE_HIGH)


fun Bitmap.toByteArray():ByteArray{
    val baos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()
    return data
}

fun Any?.isNull() = this == null


val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

fun Date.formatter() : String = formatter.format(this)

fun Date.convertTo(format: String): String? {
    var dateStr: String? = null
    val df = SimpleDateFormat(format)
    try {
        dateStr = df.format(this)
    } catch (ex: Exception) {
        Log.d("date", ex.toString())
    }

    return dateStr
}

// Converts current date to Calendar
fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}



fun EditText.onTextChanged(listener: (String) -> Unit) {


    this.addTextChangedListener(object : TextWatcher {


        override fun afterTextChanged(s: Editable?) {


            listener(s.toString())


        }


        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}


    })


}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
}



