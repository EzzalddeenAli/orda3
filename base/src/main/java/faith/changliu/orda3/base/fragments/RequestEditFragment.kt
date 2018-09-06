package faith.changliu.orda3.base.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.adapters.ApplicationsAdapter
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.KEY_REQUEST
import faith.changliu.orda3.base.utils.getDouble
import faith.changliu.orda3.base.utils.getString
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_applications_list.*
import kotlinx.android.synthetic.main.fragment_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.properties.Delegates

class RequestEditFragment : BaseFragment() {
	
	private var mRequest by Delegates.observable(Request()) { _, _, newValue ->
		bind(newValue)
	}
	
	private lateinit var mListener: RequestEditListener
	
	companion object {
		/**
		 * Factory
		 */
		fun newInstance(request: Request, listener: RequestEditListener): RequestEditFragment {
			val instance = RequestEditFragment()
			instance.mListener = listener
			
			val bundle = Bundle()
			bundle.putSerializable(KEY_REQUEST, request)
			instance.arguments = bundle
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(arguments?.getSerializable(KEY_REQUEST) as? Request)?.let {
			mRequest = it
		}
		
		mBtnSubmitNewRequest.setOnClickListener {
			updateRequest()
		}
		
		mBtnCancelNewRequest.setOnClickListener {
			toast("Update Cancelled")
			mListener.onFinished()
		}
	}
	
	// region { bind request }
	
	private fun bind(request: Request) {
		bindTextData(request)
		mRcvApplications.layoutManager = LinearLayoutManager(context)
		
		// applications
		tryBlock {
			val applications = async(CommonPool) {
				FireDB.readAllApplicationsWithRequestId(request.id)
			}.await()
			
			val mApplicationsAdapter = ApplicationsAdapter(applications) { application ->
				mRequest.assignedTo = application.appliedBy
				mEtAssignedTo.setText(application.appliedBy)
				toast("Assigned. Please submit to confirm.")
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
	
	// endregion
	
	/**
	 * update request
	 */
	private fun updateRequest() {

		val title = mEtTitle.getString() ?: return
		// todo: date picker for deadline, set to mRequest
		val address = mEtAddress.getString() ?: return
		val city = mEtCity.getString() ?: return
		val country = mEtCountry.getString() ?: return
		val weight = mEtWeight.getDouble() ?: return
		val volume = mEtVolume.getDouble() ?: return
		val compensation = mEtCompensation.getDouble() ?: return
		val description = mEtDescription.getString() ?: return

		val newRequest = Request(mRequest.id, title, mRequest.status, mRequest.assignedTo, mRequest.deadline, country, city, address, weight, volume, compensation, description, mRequest.createdAt, UserPref.getId(), UserPref.getEmail())

		tryBlock {
			async(CommonPool) {
				AppRepository.insertRequest(newRequest)
			}.await()
			toast("Request Updated")
			mListener.onFinished()
		}
	}
	
}

interface RequestEditListener {
	fun onFinished()
}