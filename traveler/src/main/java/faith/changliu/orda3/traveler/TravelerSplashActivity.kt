package faith.changliu.orda3.traveler

import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.activities.BaseSplashActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class TravelerSplashActivity : BaseSplashActivity() {
	override fun toLogin() {
		startActivitySingleTop(TravelerLoginActivity::class.java)
	}

	override val logoResId: Int = R.drawable.ic_airplane
	
	override fun toMain() {
		tryBlock {
			AppRepository.syncAllRequests()
			val user = async(CommonPool) {
				FireDB.readUserWithId(UserPref.getId())
			}.await()
			UserPref.mUser = user
			startActivitySingleTop(TravelerMainActivity::class.java)
		}
	}
}