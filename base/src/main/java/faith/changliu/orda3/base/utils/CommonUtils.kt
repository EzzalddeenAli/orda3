package faith.changliu.orda3.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.EditText
import faith.changliu.orda3.base.AppContext
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
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

/**
 * Email format check
 */
fun String.isEmail(): Boolean {
	val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
	return matches(p)
}

// Coroutine
fun BaseActivity.tryBlock(block: suspend () -> Unit) {
	kotlinx.coroutines.experimental.launch(kotlinx.coroutines.experimental.android.UI) {
		try {
			mLoading.startLoading()
			block()
		} catch (ex: NullPointerException) {
			ex.printStackTrace()
		} catch (ex: Exception) {
			ex.printStackTrace()
		} finally {
			mLoading.stopLoading()
		}
	}
}

fun BaseFragment.tryBlock(block: suspend () -> Unit) {
	kotlinx.coroutines.experimental.launch(kotlinx.coroutines.experimental.android.UI) {
		try {
			mLoading?.startLoading()
			block()
		} catch (ex: NullPointerException) {
			ex.printStackTrace()
		} catch (ex: Exception) {
			ex.printStackTrace()
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