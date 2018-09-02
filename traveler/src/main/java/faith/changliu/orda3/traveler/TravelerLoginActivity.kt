package faith.changliu.orda3.traveler

import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.activities.BaseLoginActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.utils.tryBlock

class TravelerLoginActivity : BaseLoginActivity() {
	override fun toMain() {
		tryBlock {
			AppRepository.syncAllRequests()
			startActivitySingleTop(TravelerMainActivity::class.java)
		}
	}

	private val toMain = {
		toMain()
	}

	override val mRegisterTextResId = R.string.register_traveler

	override fun register() {
		TravelerRegisterDialog(this, toMain).show()
	}
}