package faith.changliu.orda3.traveler

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.models.RequestApplication
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3.base.utils.snackConfirm
import faith.changliu.orda3.base.utils.tryBlock

import kotlinx.android.synthetic.main.activity_traveler_main.*
import kotlinx.android.synthetic.main.content_traveler_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.email
import org.jetbrains.anko.toast
import java.util.*

class TravelerMainActivity : BaseActivity() {

	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: TravelerRequestsAdapter

	private val onApply by lazy {
		{ request: Request ->
			// todo:
			mRcvRequests.snackConfirm(request.toString()) {
				// todo: more efficient way to get id
				val id = request.id + UserPref.getId().subSequence(0, 3)
				val newApplication = RequestApplication(id, request.id, UserPref.getId(), Date())

				tryBlock {
					// check if id exists
					val hasApplied = async(CommonPool) {
						FireDB.hasApplied(id)
					}.await()

					if (hasApplied) {
						toast("You Already Applied")
					} else {
						async(CommonPool) {
							FireDB.saveApplication(newApplication)
						}.await()
						toast("Apply Submitted")
					}
				}
			}
		}
	}

	private val onContactAgent by lazy {
		{ request: Request ->
			// todo:
			email(request.agentEmail, "Apply Request ${request.title}", "May I ask if the request is still up and...")
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
		toast(UserPref.getId())
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

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_account, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.mni_account) {



			return true
		}
		return false
	}

}
