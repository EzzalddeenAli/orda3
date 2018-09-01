package faith.changliu.orda3.agent

import faith.changliu.orda3.base.activities.BaseSplashActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.utils.tryBlock

class AgentSplashActivity : BaseSplashActivity() {

	override val logoResId: Int = R.drawable.box
	
	override fun toMain() {
		tryBlock {
			AppRepository.syncAll()
			startActivitySingleTop(MainActivity::class.java)
		}
	}

	override fun toLogin() {

		startActivitySingleTop(AgentLoginActivity::class.java)

	}
}