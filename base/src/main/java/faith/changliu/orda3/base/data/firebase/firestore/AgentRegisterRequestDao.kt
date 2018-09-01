package faith.changliu.orda3.base.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import faith.changliu.orda3.base.data.models.User
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

private const val AGENT_REGISTER_REQUESTS = "regs"

suspend fun FirebaseFirestore.saveAgentRegisterRequest(user: User) = suspendCancellableCoroutine<Unit> { cont ->
	collection(AGENT_REGISTER_REQUESTS).add(user)
			.addOnSuccessListener {
				cont.resume(Unit)
			}.addOnFailureListener { exception ->
				cont.resumeWithException(exception)
			}.addOnCanceledListener {
				cont.cancel(Throwable("Cancelled"))
			}
}