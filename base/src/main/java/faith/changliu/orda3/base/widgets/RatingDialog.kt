package faith.changliu.orda3.base.widgets

import android.content.Context
import faith.changliu.orda3.base.BaseDialog
import faith.changliu.orda3.base.R
import kotlinx.android.synthetic.main.rating_layout.*
import org.jetbrains.anko.toast

class RatingDialog(ctx: Context) : BaseDialog(ctx) {
	override val layoutResId = R.layout.rating_layout
	
	override fun setupOnConfirm() {
		mBtnSubmit.setOnClickListener {
			context.toast("Confirmed: ${mRatingBar.mRating}")
		}
	}
}