package faith.changliu.orda3.base.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast

class RequestsFragment : BaseFragment(), View.OnClickListener {

	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: RequestsAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_requests, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mFabAddRequest.setOnClickListener(this)

		mRcvRequests.layoutManager = LinearLayoutManager(context)
		// todo: implement onUpdate, onDelete
		mRequestAdapter = RequestsAdapter(arrayListOf(), {toast("Feature to be added")}, {
			request ->
			this.tryBlock {
				async(CommonPool) {
					AppRepository.deleteRequest(request.id)
				}.await()
				toast("Deleted")
			}
		}, { request ->
			// todo: enable
//			toDetail()
		})
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