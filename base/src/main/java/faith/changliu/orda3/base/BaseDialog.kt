package faith.changliu.orda3.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import faith.changliu.orda3.base.widgets.LoadingDialog

abstract class BaseDialog(ctx: Context) : Dialog(ctx) {
	val mLoading: LoadingDialog by lazy { LoadingDialog(this.context) }

	abstract val layoutResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(layoutResId)
		setupOnConfirm()
	}

	protected open fun setupOnConfirm() {}
}