package faith.changliu.orda3.base

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import faith.changliu.orda3.base.utils.readBitmap
import kotlinx.android.synthetic.main.activity_splash.*

abstract class BaseSplashActivity : AppCompatActivity() {

	protected abstract val logoResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		setLogo()
	}

	private fun setLogo() {
		mImvLogo.setImageBitmap(readBitmap(logoResId))
	}

}
