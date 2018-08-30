package faith.changliu.orda3.agent

import android.content.Intent
import faith.changliu.orda3.base.activities.BaseLoginActivity
import faith.changliu.orda3.base.activities.BaseSplashActivity
import org.jetbrains.anko.clearTop

class AgentSplashActivity : BaseSplashActivity() {
	override val logoResId: Int = R.drawable.box
	
	override fun toMain() {
		val intent = Intent(this, BaseLoginActivity::class.java).clearTop()
		startActivity(intent)
		finish()
	}
}