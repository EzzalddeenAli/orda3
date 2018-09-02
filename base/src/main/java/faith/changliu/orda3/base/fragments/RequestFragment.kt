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
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3.base.utils.snackConfirm
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_applications_list.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.fragment_requests_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import kotlin.properties.Delegates

class RequestsFragment : BaseFragment(), View.OnClickListener {

	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: RequestsAdapter
	// for tablet only
	private var mDisplayedRequest by Delegates.observable(Request()) { _, _, newValue ->
		if (newValue.id.isEmpty() ) {

		} else {
			bind(newValue)
		}
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
				// todo: enable
//				addNew()
			}
		}
	}

	private fun bind(request: Request) {
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

	private fun getStatus(status: Int): String {
		return when (status) {
			0 -> "PENDING"
			1 -> "ASSIGNED"
			else -> "CLOSED"
		}
	}

	// todo: enable
//	private fun addNew() {
//		val intent = Intent(context, AddRequestActivity::class.java)
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//		startActivity(intent)
//	}
}