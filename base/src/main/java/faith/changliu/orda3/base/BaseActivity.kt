package faith.changliu.orda3.base

import android.support.v7.app.AppCompatActivity
import faith.changliu.orda3.base.widgets.LoadingDialog

open class BaseActivity : AppCompatActivity() {

	val mLoading by lazy { LoadingDialog(this) }

}