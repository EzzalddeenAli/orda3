package faith.changliu.orda3.traveler

import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.activities.BaseSplashActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.utils.tryBlock

class TravelerSplashActivity : BaseSplashActivity() {
	override fun toLogin() {
		startActivitySingleTop(TravelerLoginActivity::class.java)
	}

	override val logoResId: Int = R.drawable.ic_airplane
	
	override fun toMain() {
		tryBlock {
			AppRepository.syncAllRequests()
			startActivitySingleTop(TravelerMainActivity::class.java)
		}
	}
}