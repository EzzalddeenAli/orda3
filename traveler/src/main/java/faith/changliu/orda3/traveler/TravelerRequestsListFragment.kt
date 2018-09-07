package faith.changliu.orda3.traveler

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import kotlinx.android.synthetic.main.fragment_requests_list_traveler.*

class TravelerRequestsListFragment : BaseFragment() {
	
	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: TravelerRequestsAdapter
	private lateinit var mListener: Listener
	
	companion object {
		fun newInstance(listener: Listener): TravelerRequestsListFragment {
			val instance = TravelerRequestsListFragment()
			instance.mListener = listener
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_requests_list_traveler, container, false)
	}
	
	private val onShowDetail = { request: Request -> mListener.onShowDetail(request) }
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mRequestAdapter = TravelerRequestsAdapter(arrayListOf(), onShowDetail)
		mRcvRequests.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = mRequestAdapter
		}
	}
	
	override fun onResume() {
		super.onResume()
		mViewModel.requests.observe(this, Observer { requests ->
			requests?.let {
				mRequestAdapter.updateRequests(it)
			}
		})
	}

//	private val onApply by lazy {
//		{ request: Request ->
//			// todo:
//			mRcvRequests.snackConfirm(request.toString()) {
//				// todo: more efficient way to get id
//				val id = request.id + UserPref.getId().subSequence(0, 3)
//				val newApplication = RequestApplication(id, request.id, UserPref.getId(), Date())
//
//				tryBlock {
//					// check if id exists
//					val hasApplied = async(CommonPool) {
//						FireDB.hasApplied(id)
//					}.await()
//
//					if (hasApplied) {
//						toast("You Already Applied")
//					} else {
//						async(CommonPool) {
//							FireDB.saveApplication(newApplication)
//						}.await()
//						toast("Apply Submitted")
//					}
//				}
//			}
//		}
//	}
	
	interface Listener {
		fun onShowDetail(request: Request)
	}
}
