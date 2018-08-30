package faith.changliu.orda3.base.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import faith.changliu.orda3.base.R
import kotlinx.android.synthetic.main.activity_splash.*

abstract class BaseSplashActivity : AppCompatActivity() {
	
	private val mHandler = Handler()

	protected abstract val logoResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		setLogo()
		
		mHandler.postDelayed({
			toMain()
		}, 2000)
	}
	
	/**
	 * Executed after 2 seconds
	 */
	abstract fun toMain()
	
	private fun setLogo() {
		mImvLogo.setImageResource(logoResId)
	}

}
