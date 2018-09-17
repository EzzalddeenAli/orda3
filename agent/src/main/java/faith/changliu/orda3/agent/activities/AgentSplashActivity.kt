package faith.changliu.orda3.agent.activities

import faith.changliu.orda3.agent.R
import faith.changliu.orda3.base.activities.BaseSplashActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class AgentSplashActivity : BaseSplashActivity() {

	override val logoResId: Int = R.drawable.box
	
	override fun toMain() {
		tryBlock {
			AppRepository.syncAll()
			val user = async(CommonPool) {
				FireDB.readUserWithId(UserPref.getId())
			}.await()
			UserPref.mUser = user
			startActivitySingleTop(MainActivity::class.java)
		}
	}

	override fun toLogin() {

		startActivitySingleTop(AgentLoginActivity::class.java)

	}
}