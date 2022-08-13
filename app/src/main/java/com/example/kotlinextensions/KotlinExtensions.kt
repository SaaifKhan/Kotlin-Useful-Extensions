package com.example.kotlinextensions

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
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

//int extension
fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
//activity extensions


/*Live data extension function which'll set initial value*/
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }


fun AppCompatActivity.replaceFragment(
    fragment: Fragment, frameId: Int,
    addBacktack: Boolean = false,
    clearBackStack: Boolean = false,
    allowMultipleInstances: Boolean = false,
    popBackStackInclusive: String? = null
) {
    //        double tapping to open fragment twice wont work
    if (!allowMultipleInstances) {
//            checking if fragment is present in back stack
        if (supportFragmentManager.backStackEntryCount > 0 &&
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name?.equals(
                fragment::class.java.name
            )!!
        )
            return
//            checkin if fragment is not in backstack but present is fragments list meaning fragment is added but not placed in backstack
        if (supportFragmentManager.fragments.size > 0 &&
            supportFragmentManager.fragments.get(supportFragmentManager.fragments.size - 1)::class.java.name.equals(
                fragment::class.java.name
            )
        )
            return
    }

    supportFragmentManager.transact {
        if (addBacktack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }

        if (popBackStackInclusive != null) {
            supportFragmentManager.popBackStack(
                popBackStackInclusive,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addFragment(
    fragment: Fragment, frameId: Int,
    addBacktack: Boolean = false,
    clearBackStack: Boolean = false,
    allowMultipleInstances: Boolean = false,
    popBackStackInclusive: String? = null
) {
    //        double tapping to open fragment twice wont work
    if (!allowMultipleInstances) {
//            checking if fragment is present in back stack
        if (supportFragmentManager.backStackEntryCount > 0 &&
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name?.equals(
                fragment::class.java.name
            )!!
        )
            return
//            checkin if fragment is not in backstack but present is fragments list meaning fragment is added but not placed in backstack
        if (supportFragmentManager.fragments.size > 0 &&
            supportFragmentManager.fragments.get(supportFragmentManager.fragments.size - 1)::class.java.name.equals(
                fragment::class.java.name
            )
        )
            return
    }

    supportFragmentManager.transact {
        if (addBacktack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }

        if (popBackStackInclusive != null) {
            supportFragmentManager.popBackStack(
                popBackStackInclusive,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        add(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragmentWithAnimation(
    fragment: Fragment,
    frameId: Int,
    addBacktack: Boolean,
    clearBackStack: Boolean,
    enter: Int,
    exit: Int,
    popEnter: Int,
    popExit: Int
) {
    supportFragmentManager.transact {
        setCustomAnimations(enter, exit, popEnter, popExit)
        if (addBacktack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }

        replace(frameId, fragment)
    }
}


/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentTo(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}





fun AppCompatActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    allowMultipleInstances: Boolean = false
) {
    supportFragmentManager.transact {
        add(containerId, fragment)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.popStack() {
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.clearStack() {
    for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
        supportFragmentManager.popBackStack()
    }
}


fun AppCompatActivity.openActivity(
    className: Class<out AppCompatActivity>,
    clearBackStack: Boolean = true,
    bundle: Bundle? = null
) {
    var intent = Intent(this, className)
    if (clearBackStack)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    bundle?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    transactionView: View?,
    transactionName: String?
) {
    supportFragmentManager.transact {

        if (transactionView == null || transactionName == null)
            replace(frameId, fragment)
        else
            replace(frameId, fragment).addSharedElement(transactionView, transactionName)
    }
}
//fun AppCompatActivity.obtainViewModel() =
//    ViewModelProviders.of(this, AuthenticationViewModelFactory())

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()

}





