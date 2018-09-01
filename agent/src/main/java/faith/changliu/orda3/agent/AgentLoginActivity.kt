package faith.changliu.orda3.agent

import faith.changliu.orda3.base.activities.BaseLoginActivity
import faith.changliu.orda3.base.widgets.RegisterAgentDialog

class AgentLoginActivity : BaseLoginActivity() {
	override fun toMain() {
		startActivitySingleTop(MainActivity::class.java)
	}

	override val mRegisterTextResId = R.string.register_agent

	override fun register() {
		RegisterAgentDialog(this).show()
	}
}