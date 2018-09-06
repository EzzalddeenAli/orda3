package faith.changliu.orda3.agent.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.agent.R
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.data.models.Order
import faith.changliu.orda3.base.utils.FRAG_TAG_ORDER_DETAIL
import faith.changliu.orda3.base.utils.FRAG_TAG_REQUEST_DETAIL
import kotlinx.android.synthetic.main.fragment_orders.*
import org.jetbrains.anko.support.v4.toast

class OrderFragment : BaseFragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_orders, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mFabAddOrder.setOnClickListener {
			toast("Add Order")
		}
		
		activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.orders_list_container, OrdersListFragment.newInstance(mOrdersListListener))
				?.commit()
	}
	
	// region { Implement Order List Listener }
	
	private val mOrdersListListener by lazy {
		if (order_detail_container == null) {
			object : OrdersListListener {
				override fun onUpdate(order: Order) {
					activity?.supportFragmentManager?.beginTransaction()
							?.add(R.id.orders_list_container, OrderDetailFragment.newInstance(order, mOrderDetailListener), FRAG_TAG_ORDER_DETAIL)
							?.commit()
				}
			}
		} else {
			object : OrdersListListener {
				override fun onUpdate(order: Order) {
					activity?.supportFragmentManager?.beginTransaction()
							?.replace(R.id.order_detail_container, OrderDetailFragment.newInstance(order, mOrderDetailListener), FRAG_TAG_ORDER_DETAIL)
							?.commit()
				}
			}
		}
	}
	
	// endregion
	
	// region { Implement Order Detail Listener }
	
	private val mOrderDetailListener by lazy {
		if (order_detail_container == null) {
			object : OrderDetailListener {
				override fun onFinished() {
					activity?.supportFragmentManager?.popBackStack()
				}
				
			}
		} else {
			object : OrderDetailListener {
				override fun onFinished() {
					with(activity?.supportFragmentManager) {
						this?.beginTransaction()?.remove(findFragmentByTag(FRAG_TAG_ORDER_DETAIL))
								?.commit()
					}
				}
				
			}
		}
		
	}
	
	// endregion
}