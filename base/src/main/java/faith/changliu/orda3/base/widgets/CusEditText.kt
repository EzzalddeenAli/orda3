package faith.changliu.orda3.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import faith.changliu.orda3.base.R
import kotlinx.android.synthetic.main.cus_edit_text.view.*

class CusEditText @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
	
	var hint: String = ""
	
	init {
		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusEditText)
		hint = typedArray.getString(R.styleable.CusEditText_cus_et_hint)
		
		View.inflate(context, R.layout.cus_edit_text, this)
		mEtLayout.hint = hint
		
		typedArray.recycle()
	}
	
	fun setText(text: String) {
		mEtInput.setText(text)
	}
	
	fun getText(): String {
		return mEtInput.text.toString()
	}
}