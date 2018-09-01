package faith.changliu.orda3.base.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import faith.changliu.orda3.base.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

private val cm = AppContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun isConnected(): Boolean {
	val activeNetwork = cm.activeNetworkInfo ?: return false
	return activeNetwork.isConnected
}

inline fun ifConnected(onConnected: () -> Unit) {
	if (isConnected()) onConnected()
	else toastExt(R.string.no_network)
}


// ---------- Edit Ext ----------

fun EditText.getDouble(): Double? {
	val stringText = getString() ?: return null
	return stringText.toDouble()
}

fun EditText.getString(): String? {
	if (text.isNullOrEmpty()) {
		this.error = "Input is required"
		return null
	}

	return text.toString()
}

fun EditText.getEmail(): String? {
	val email = getString() ?: return null
	if (email.isEmail().not()) {
		error = "Not an email "
		return null
	}

	return email
}

fun TextInputEditText.getDouble(): Double? {
	val stringText = getString() ?: return null
	return stringText.toDouble()
}

fun TextInputEditText.getString(): String? {
	if (text.isNullOrEmpty()) {
		this.error = "Input is required"
		return null
	}

	return text.toString()
}

fun TextInputEditText.getEmail(): String? {
	val email = getString() ?: return null
	if (email.isEmail().not()) {
		error = "Not an email "
		return null
	}

	return email
}

/**
 * Email format check
 */
fun String.isEmail(): Boolean {
	val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
	return matches(p)
}

// Coroutine
fun BaseActivity.tryBlock(block: suspend () -> Unit) {
	launch(UI) {
		try {
			mLoading.startLoading()
			block()
		} catch (ex: NullPointerException) {
			ex.printStackTrace()
			toast(ex.localizedMessage)
		} catch (ex: Exception) {
			ex.printStackTrace()
			toast(ex.localizedMessage)
		} finally {
			mLoading.stopLoading()
		}
	}
}

fun BaseFragment.tryBlock(block: suspend () -> Unit) {
	launch(UI) {
		try {
			mLoading?.startLoading()
			block()
		} catch (ex: NullPointerException) {
			ex.printStackTrace()
			toast(ex.localizedMessage)
		} catch (ex: Exception) {
			ex.printStackTrace()
			toast(ex.localizedMessage)
		} finally {
			mLoading?.stopLoading()
		}
	}
}



// Debug and Prompts
fun toastExt(msgResId: Int) {
	with(AppContext) {
		toast(getString(msgResId))
	}
}

fun View.snackConfirm(msg: String, onConfirmed: (View) -> Unit) {
	Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
			.setAction(context.getString(R.string.confirm), onConfirmed)
			.setActionTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
			.show()
}

fun log(msg: String, tag: String = "changserror") {
	Log.wtf(tag, msg)
}
