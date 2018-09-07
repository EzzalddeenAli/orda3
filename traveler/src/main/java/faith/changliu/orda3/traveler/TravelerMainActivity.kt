package faith.changliu.orda3.traveler

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.utils.FRAG_TAG_REQUEST_DETAIL
import kotlinx.android.synthetic.main.activity_traveler_main.*
import kotlinx.android.synthetic.main.content_traveler_main.*
import org.jetbrains.anko.email
import org.jetbrains.anko.toast

class TravelerMainActivity : BaseActivity() {



	private val onContactAgent by lazy {
		{ request: Request ->
			// todo:
			email(request.agentEmail, "Apply Request ${request.title}", "May I ask if the request is still up and...")
			Unit
		}
	}
	
	private val requestDetailListener = object : TravelerRequestDetailFragment.Listener {
		override fun onFinished() {
			supportFragmentManager.beginTransaction().remove(
					supportFragmentManager.findFragmentByTag(FRAG_TAG_REQUEST_DETAIL)
			).commit()
		}
		
	}

	private val requestsListListener = object : TravelerRequestsListFragment.Listener {
		override fun onShowDetail(request: Request) {
			if (request_detail_container == null) {
				supportFragmentManager.beginTransaction()
						.addToBackStack(null)
						.add(R.id.requests_list_container, TravelerRequestDetailFragment.newInstance(request, requestDetailListener), FRAG_TAG_REQUEST_DETAIL)
						.commit()
			} else {
				supportFragmentManager.beginTransaction()
						.replace(R.id.request_detail_container, TravelerRequestDetailFragment.newInstance(request, requestDetailListener), FRAG_TAG_REQUEST_DETAIL)
						.commit()
			}
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_traveler_main)
		setSupportActionBar(toolbar)

		supportFragmentManager.beginTransaction()
				.replace(R.id.requests_list_container, TravelerRequestsListFragment.newInstance(requestsListListener))
				.commit()
		

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
