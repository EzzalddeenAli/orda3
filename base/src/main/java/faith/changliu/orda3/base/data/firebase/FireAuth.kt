package faith.changliu.orda3.base.data.firebase

import com.google.firebase.auth.FirebaseAuth
import faith.changliu.orda3.base.AppContext
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.toast

object FireAuth {
	private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
	
	fun isLoggedIn() = mAuth.currentUser != null
	
	suspend fun login(email: String, pwd: String) = suspendCancellableCoroutine<String> { cont ->
		mAuth.signInWithEmailAndPassword(email, pwd).addOnSuccessListener { authResult ->
			cont.resume(authResult.user.uid)
		}.addOnFailureListener { ex ->
			ex.printStackTrace()
			cont.resumeWithException(ex)
		}.addOnCanceledListener { cont.cancel() }
	}
	
	fun resetPassword(email: String) {
		mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
			AppContext.toast("Reset password email sent")
		}.addOnFailureListener {
			it.printStackTrace()
			AppContext.toast("Error: ${it.localizedMessage}")
		}
	}
	
	fun logout() {
		mAuth.signOut()
	}
	
}