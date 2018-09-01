package faith.changliu.orda3.base.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.firebase.FireAuth
import kotlinx.android.synthetic.main.activity_splash.*

abstract class BaseSplashActivity : BaseActivity() {
	
	private val mHandler = Handler()

	protected abstract val logoResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		setLogo()
		
		mHandler.postDelayed({

			if (FireAuth.isLoggedIn()) toMain()
			else toLogin()

		}, 2000)
	}

	protected abstract fun toLogin()
	protected abstract fun toMain()
	
	private fun setLogo() {
		mImvLogo.setImageResource(logoResId)
	}

}
