package faith.changliu.orda3.base.activities

import android.os.Bundle
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.adapters.ApplicationsAdapter
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.models.RequestStatus
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.*
import kotlinx.android.synthetic.main.fragment_applications_list.*
import kotlinx.android.synthetic.main.fragment_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.toast
import java.util.*

class AddRequestActivity : BaseActivity() {

	private var mDisplayedRequest: Request? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_request)


		mDisplayedRequest = intent?.getSerializableExtra(KEY_REQUEST) as? Request
		mDisplayedRequest?.let {
			bind(it)
		}

		mBtnSubmitNewRequest.setVisible(true)
		mBtnCancelNewRequest.setVisible(true)

		mBtnSubmitNewRequest.setOnClickListener {
			addOrUpdateRequest()
		}

		mBtnCancelNewRequest.setOnClickListener {
			finish()
		}
	}

	private fun addOrUpdateRequest() {

		val title = mEtTitle.getString() ?: return
		// todo: date picker
		val deadline = Date()
		val address = mEtAddress.getString() ?: return
		val city = mEtCity.getString() ?: return
		val country = mEtCountry.getString() ?: return
		val weight = mEtWeight.getDouble() ?: return
		val volume = mEtVolume.getDouble() ?: return
		val compensation = mEtCompensation.getDouble() ?: return
		val description = mEtDescription.getString() ?: return

		val id = if (mDisplayedRequest == null) UUID.randomUUID().toString() else mDisplayedRequest!!.id
		val assignTo = if (mDisplayedRequest == null) "" else mDisplayedRequest!!.assignedTo
		val createdAt = if (mDisplayedRequest == null) Date() else mDisplayedRequest!!.createdAt
		val status = if (mDisplayedRequest == null) RequestStatus.PENDING else mDisplayedRequest!!.status
		val newRequest = Request(id, title, status, assignTo, deadline, country, city, address, weight, volume, compensation, description, createdAt, UserPref.getId(), UserPref.getEmail())

		tryBlock {
			async(CommonPool) {
				AppRepository.insertRequest(newRequest)
			}.await()

			toast("inserted")
			finish()
		}
	}

	private fun bind(request: Request) {
		bindTextData(request)

		// applications
		tryBlock {
			val applications = async(CommonPool) {
				FireDB.readAllApplicationsWithRequestId(request.id)
			}.await()

			val mApplicationsAdapter = ApplicationsAdapter(applications) { application ->
				// assign application to id
				request.assignedTo = application.appliedBy

				tryBlock {
					async(CommonPool) {
						AppRepository.insertRequest(request)
					}.await()

					mDisplayedRequest?.assignedTo = application.appliedBy
					mEtAssignedTo.setText(application.appliedBy)
					toast("Assigned")
				}
			}

			mRcvApplications.adapter = mApplicationsAdapter
		}

	}

	private fun bindTextData(request: Request) {
		mEtTitle.setText(request.title)
		mEtDeadline.setText(request.deadline.toString())
		mEtStatus.setText(getStatus(request.status))
		mEtAssignedTo.setText(request.assignedTo)
		mEtAddress.setText(request.address)
		mEtCity.setText(request.city)
		mEtCountry.setText(request.country)
		mEtWeight.setText("${request.weight}")
		mEtVolume.setText("${request.volume}")
		mEtCompensation.setText("${request.compensation}")
		mEtDescription.setText(request.description)
	}

	private fun getStatus(status: Int): String {
		return when (status) {
			0 -> "PENDING"
			1 -> "ASSIGNED"
			else -> "CLOSED"
		}
	}
}