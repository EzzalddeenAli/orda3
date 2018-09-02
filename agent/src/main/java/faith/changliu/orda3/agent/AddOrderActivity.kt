package faith.changliu.orda3.agent

import android.os.Bundle
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.models.Order
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.KEY_ORDER
import faith.changliu.orda3.base.utils.getDouble
import faith.changliu.orda3.base.utils.getString
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.android.synthetic.main.activity_add_order.*
import kotlinx.android.synthetic.main.content_add_order.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.util.*

class AddOrderActivity : BaseActivity() {

	private var mOrder: Order? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_order)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)


		mOrder = intent?.getSerializableExtra(KEY_ORDER) as? Order
		mOrder?.let {
			order ->
			bind(order)
		}

		mBtnSubmit.setOnClickListener { view ->
			
			// validation
			val barcode = mEtBarcode.getString() ?: return@setOnClickListener
			val title = mEtTitle.getString() ?: return@setOnClickListener
			val price = mEtPrice.getDouble() ?: return@setOnClickListener
			val weight = mEtWeight.getDouble() ?: return@setOnClickListener
			val description = mEtDescription.text.toString()
			
			val userId = UserPref.getId()
			
			// todo: remove ph agent test
			val newOrder = Order(barcode, title, weight, price, Date(), Date(), userId, description)
			tryBlock {
				async(CommonPool) {
					AppRepository.insertOrder(newOrder)
				}.await()
				finish()
			}
		}
	}

	private fun bind(order: Order) {
		mEtBarcode.setText(order.id)
		mEtTitle.setText(order.title)
		mEtPrice.setText(order.price.toString())
		mEtWeight.setText(order.weight.toString())
		mEtDescription.setText(order.description)
	}
}
