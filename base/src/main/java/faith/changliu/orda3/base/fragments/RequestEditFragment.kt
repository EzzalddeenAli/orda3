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
import faith.changliu.orda3.base.utils.KEY_REQUEST
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_applications_list.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import kotlin.properties.Delegates

class RequestEditFragment : BaseFragment() {
	
	private var mRequest by Delegates.observable(Request()) { _, _, newValue ->
		bind(newValue)
	}
	
	companion object {
		/**
		 * Factory
		 */
		fun newInstance(request: Request): RequestEditFragment {
			val instance = RequestEditFragment()
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
				// assign application to id
				request.assignedTo = application.appliedBy
				
				tryBlock {
					async(CommonPool) {
						AppRepository.insertRequest(request)
					}.await()
					
					mRequest.assignedTo = application.appliedBy
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
	
	// endregion
	
	
	
}