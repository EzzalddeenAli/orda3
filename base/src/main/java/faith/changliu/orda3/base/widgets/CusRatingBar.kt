package faith.changliu.orda3.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import faith.changliu.orda3.base.R
import kotlinx.android.synthetic.main.widget_rating_bar.view.*
import kotlin.properties.Delegates

class CusRatingBar @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
	
	var mRating by Delegates.observable(1.0) { _, _, newValue ->
		setAllRatingDown()
		setRatingDisplay(newValue)
	}

	init {
		View.inflate(context, R.layout.widget_rating_bar, this)
		mBtnRating5.setOnClickListener {
			mRating = 5.0
		}
		mBtnRating4.setOnClickListener {
			mRating = 4.0
		}
		mBtnRating3.setOnClickListener {
			mRating = 3.0
		}
		mBtnRating2.setOnClickListener {
			mRating = 2.0
		}
		mBtnRating1.setOnClickListener {
			mRating = 1.0
		}
	}
	
	private fun setRatingDisplay(rating: Double) {
		if (rating >= 2.0) {
			mBtnRating2.up()
		}
		
		if (rating >= 3.0) {
			mBtnRating3.up()
		}
		
		if (rating >= 4.0) {
			mBtnRating4.up()
		}
		
		if (rating == 5.0) {
			mBtnRating5.up()
		}
	}
	
	private fun setAllRatingDown() {
		mBtnRating5.down()
		mBtnRating4.down()
		mBtnRating3.down()
		mBtnRating2.down()
	}
	
	private fun ImageButton.up() {
		setImageResource(R.drawable.ic_star)
	}
	
	private fun ImageButton.down() {
		setImageResource(R.drawable.ic_star_empty)
	}
}