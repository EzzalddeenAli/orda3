package faith.changliu.orda3.agent.activities

import faith.changliu.orda3.agent.R
import faith.changliu.orda3.base.activities.BaseLoginActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.utils.tryBlock
import faith.changliu.orda3.base.widgets.RegisterAgentDialog

class AgentLoginActivity : BaseLoginActivity() {
	override fun toMain() {
		tryBlock {
			AppRepository.syncAll()
			startActivitySingleTop(MainActivity::class.java)
		}
	}

	override val mRegisterTextResId = R.string.register_agent

	override fun register() {
		RegisterAgentDialog(this).show()
	}
}