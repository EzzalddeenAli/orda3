package faith.changliu.orda3.base.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.adapters.ApplicationsAdapter
import faith.changliu.orda3.base.adapters.RequestsAdapter
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.models.RequestStatus
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3.base.utils.*
import kotlinx.android.synthetic.main.fragment_applications_list.*
import kotlinx.android.synthetic.main.fragment_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.fragment_requests_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.properties.Delegates

class RequestsFragment : BaseFragment(), View.OnClickListener {

	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: RequestsAdapter

	// for tablet only
	private var mDisplayedRequest by Delegates.observable(Request()) { _, _, newValue ->
		if (newValue.id.isEmpty() ) {
			// todo: first time load, add new request
		} else {
			mBtnSubmitNewRequest.setVisible(true)
			mBtnCancelNewRequest.setVisible(true)
			bind(newValue)
		}
	}
	private var mIsAddingNew by Delegates.observable(false) { _, _, newValue ->
		// hide if is adding new
		include_requests_list.setVisible(!newValue)
		mDisplayDataLayout.setVisible(!newValue)
		mFabAddRequest.setVisible(!newValue)
		mEtStatusLayout.setVisible(!newValue)
		mEtAssignedToLayout.setVisible(!newValue)
		// show
		mBtnSubmitNewRequest.setVisible(newValue)
		mBtnCancelNewRequest.setVisible(newValue)
	}

	private val onUpdate by lazy {
		if (include_request_detail == null)
			{ request: Request ->
				// todo: phone
				toast("Phone: " + request.toString())
				Unit
			}
		else
			{ request: Request ->
				// todo: tablet
				mDisplayedRequest = request
			}
	}

	private val onDelete by lazy {
		if (include_request_detail == null)
			{ request: Request ->
				// todo: phone
				toast("Phone: " + request.toString())
				Unit
			}
		else
			{ request: Request ->
				mFabAddRequest.snackConfirm("Confirm Delete Request") { _ ->
					tryBlock {
						async(CommonPool) {
							AppRepository.deleteRequest(request.id)
						}.await()
						toast("Deleted")
					}
				}
			}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_requests, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mFabAddRequest.setOnClickListener(this)

		// setup for both phone and tablet
		mRcvRequests.layoutManager = LinearLayoutManager(context)

		// todo: implement onUpdate, onDelete
		mRequestAdapter = RequestsAdapter(arrayListOf(), onUpdate, onDelete)

		// check which layout
		if (include_request_detail == null) {
			// setup for phone
		} else {
			// setup for tablet
			mRcvApplications.layoutManager = LinearLayoutManager(context)
			mBtnSubmitNewRequest.setOnClickListener(this)
			mBtnCancelNewRequest.setOnClickListener(this)
			mEtDeadline.setOnClickListener(this)
		}
	}

	// todo: enable
//	private fun toDetail() {
//		// to display in activity
//		val intent = Intent(context, RequestDetailActivity::class.java).putExtra(KEY_REQUEST, request)
//		startActivity(intent)
//	}

	override fun onResume() {
		super.onResume()
		mViewModel.requests.observe(this, Observer { requests ->
			requests?.let {
				mRequestAdapter.requests.apply {
					clear()
					addAll(it)
				}
				mRcvRequests.adapter = mRequestAdapter
			}
		})
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.mFabAddRequest -> {
				mDisplayedRequest = Request()
				mIsAddingNew = true
				bindTextData(mDisplayedRequest)
			}
			R.id.mBtnSubmitNewRequest -> {
				addOrUpdateRequest()
			}
			R.id.mBtnCancelNewRequest -> {
				mIsAddingNew = false
			}
			R.id.mEtDeadline -> {
				// todo: display date picker
				toast("Date Picked")
			}
		}
	}

	private fun bind(request: Request) {
		bindTextData(request)

		// applications
		tryBlock {
			val applications = async(CommonPool) {
				FireDB.readAllApplicationsWithRequestId(request.id)
			}.await()

			val mApplicationsAdapter = ApplicationsAdapter(applications) {
				toast(it.appliedBy)
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

	//
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

		val id = if (mDisplayedRequest.id.isEmpty()) UUID.randomUUID().toString() else mDisplayedRequest.id
		val assignTo = if (mDisplayedRequest.id.isEmpty()) "" else mDisplayedRequest.assignedTo
		val createdAt = if (mDisplayedRequest.id.isEmpty()) Date() else mDisplayedRequest.createdAt
		val status = if (mDisplayedRequest.id.isEmpty()) RequestStatus.PENDING else mDisplayedRequest.status
		val newRequest = Request(id, title, status, assignTo, deadline, country, city, address, weight, volume, compensation, description, createdAt, UserPref.getId(), UserPref.getEmail())

		tryBlock {
			async(CommonPool) {
				AppRepository.insertRequest(newRequest)
			}.await()
			toast("inserted")
			mIsAddingNew = false
		}
	}
}