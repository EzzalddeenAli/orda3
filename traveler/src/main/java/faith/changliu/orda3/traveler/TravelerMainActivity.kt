package faith.changliu.orda3.traveler

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3.base.utils.snackConfirm

import kotlinx.android.synthetic.main.activity_traveler_main.*
import kotlinx.android.synthetic.main.content_traveler_main.*
import org.jetbrains.anko.toast

class TravelerMainActivity : BaseActivity() {

	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: TravelerRequestsAdapter

	private val onApply by lazy {
		{ request: Request ->
			// todo:
			mRcvRequests.snackConfirm(request.toString()) {
				toast("Applied")
			}
		}
	}

	private val onContactAgent by lazy {
		{ request: Request ->
			// todo:
			toast("Contact ${request.agentEmail}")
			Unit
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_traveler_main)
		setSupportActionBar(toolbar)

		mRcvRequests.layoutManager = LinearLayoutManager(this)
		mRequestAdapter = TravelerRequestsAdapter(arrayListOf(), onContactAgent, onApply)

		// todo: debug, to be removed
		toast(UserPref.mUser.toString())
	}

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

}
