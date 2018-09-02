package faith.changliu.orda3.agent

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.models.Order
import faith.changliu.orda3.base.data.viewmodels.MainViewModel
import faith.changliu.orda3.base.utils.KEY_ORDER
import faith.changliu.orda3.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class OrderFragment : BaseFragment(), View.OnClickListener {
	
	private val mViewModel by lazy { MainViewModel() }
	private lateinit var mOrderAdapter: OrdersAdapter


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_orders, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mFabAddOrder.setOnClickListener(this)
		
		mRcvOrders.layoutManager = LinearLayoutManager(context)
		// todo: implement onUpdate, onDelete
		mOrderAdapter = OrdersAdapter(arrayListOf(), {
			order ->
			startActivity<AddOrderActivity>(KEY_ORDER to order)
		}, {
			order ->
			tryBlock {
				async(CommonPool) {
					AppRepository.deleteOrder(order.id)
				}.await()
				toast("Deleted")
			}
		})
	}
	
	override fun onResume() {
		super.onResume()
		mViewModel.orders.observe(this, Observer<List<Order>> { orders ->
			orders?.let { orders ->
				mOrderAdapter.orders.apply {
					// todo: opt delete/insert
					clear()
					addAll(orders)
				}
				mRcvOrders.adapter = mOrderAdapter
			}
		})
	}
	
	override fun onClick(v: View) {
		when (v.id) {
			R.id.mFabAddOrder -> {
				val intent = Intent(context, AddOrderActivity::class.java)
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
				startActivity(intent)
			}
		}
	}
}

