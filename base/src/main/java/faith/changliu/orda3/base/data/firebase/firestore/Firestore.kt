package faith.changliu.orda3.base.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import faith.changliu.orda3.base.data.models.Order
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.models.RequestApplication
import faith.changliu.orda3.base.data.models.User

object FireDB {
	private val mFirestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

	// region { Orders }

	suspend fun saveOrder(order: Order) {
		mFirestore.saveOrder(order)
	}
	
	suspend fun readAllOrders(): ArrayList<Order> {
		return mFirestore.readAllOrders()
	}
	
	suspend fun deleteOrder(orderId: String) {
		mFirestore.deleteOrder(orderId)
	}

	// endregion

	// region { Requests }

	suspend fun saveRequest(request: Request) {
		mFirestore.saveRequest(request)
	}

	suspend fun readAllRequestsCreatedByCurrentUser(): ArrayList<Request> {
		return mFirestore.readAllRequestsCreatedByCurrentUser()
	}

	suspend fun readAllRequests(): ArrayList<Request> {
		return mFirestore.readAllRequests()
	}

	suspend fun deleteRequest(requestId: String) {
		mFirestore.deleteRequest(requestId)
	}

	// endregion

	// region { Request Application }

	suspend fun saveApplication(application: RequestApplication) {
		mFirestore.saveApplication(application)
	}

	suspend fun readAllApplicationsWithRequestId(requestId: String): ArrayList<RequestApplication> {
		return mFirestore.readAllApplicationsWithRequestId(requestId)
	}

	// endregion

	// region { User }

	suspend fun saveUser(user: User) {
		mFirestore.saveUser(user)
	}

	suspend fun readUserWithId(id: String): User {
		return mFirestore.readUserWithId(id)
	}

	suspend fun getUserTypeWithEmail(email: String): Int {
		return mFirestore.getUserTypeWithEmail(email)
	}

	// endregion

	// region { Agent Register Requests }

	suspend fun saveAgentRegisterRequest(user: User) {
		mFirestore.saveAgentRegisterRequest(user)
	}

	// endregion
}
