package faith.changliu.orda3.agent

import faith.changliu.orda3.base.activities.BaseSplashActivity

class AgentSplashActivity : BaseSplashActivity() {

	override val logoResId: Int = R.drawable.box
	
	override fun toMain() {
		startActivitySingleTop(MainActivity::class.java)
	}

	override fun toLogin() {
		startActivitySingleTop(AgentLoginActivity::class.java)
	}
}