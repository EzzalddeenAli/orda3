package faith.changliu.orda3.agent

import android.os.Bundle
import faith.changliu.orda3.base.BaseActivity
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
	}
}