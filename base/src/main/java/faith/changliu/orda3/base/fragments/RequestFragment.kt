package faith.changliu.orda3.base.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.fragment_requests_list.*
import org.jetbrains.anko.support.v4.toast

class RequestsFragment : BaseFragment(), View.OnClickListener {

	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: RequestsAdapter

	private val toDetail by lazy {
		if (include_request_detail == null)
			{ request: Request ->
				// todo: phone
				toast("Phone: " + request.toString())
				Unit
			}
		 else
			{ request: Request ->
				// todo: tablet
				toast("Tablet: " + request.toString())
				Unit
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
		mRequestAdapter = RequestsAdapter(arrayListOf(), toDetail)

		// check which layout
		if (include_request_detail == null) {
			// setup for phone
		} else {
			// setup for tablet
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

	// todo: enable
//	private fun addNew() {
//		val intent = Intent(context, AddRequestActivity::class.java)
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//		startActivity(intent)
//	}
}