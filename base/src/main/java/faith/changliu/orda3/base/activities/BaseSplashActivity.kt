package faith.changliu.orda3.base.activities

import android.os.Bundle
import android.os.Handler
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.firebase.FireAuth
import faith.changliu.orda3.base.utils.isConnected
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

abstract class BaseSplashActivity : BaseActivity() {
	
	private val mHandler = Handler()

	protected abstract val logoResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		setLogo()

		if (isConnected()) {
			launch(UI) {
				async(CommonPool) {
					AppRepository.syncAll()
				}.await()
				if (FireAuth.isLoggedIn()) toMain()
				else toLogin()
			}
		} else {
			mHandler.postDelayed({
				if (FireAuth.isLoggedIn()) toMain()
				else toLogin()
			}, 2000)
		}
		

	}

	protected abstract fun toLogin()
	protected abstract fun toMain()
	
	private fun setLogo() {
		mImvLogo.setImageResource(logoResId)
	}

}
