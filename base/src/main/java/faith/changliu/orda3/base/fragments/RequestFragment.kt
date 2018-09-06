package faith.changliu.orda3.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.utils.FRAG_TAG_REQUEST_DETAIL
import kotlinx.android.synthetic.main.fragment_requests.*
import org.jetbrains.anko.support.v4.toast

class RequestFragment : BaseFragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_requests, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mFabAddRequest.setOnClickListener { toast("Add Request") }
		
		activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.requests_list_container, RequestListFragment.newInstance(mRequestListListener))
				?.commit()
		
	}
	
	// region { Implement List Listener }
	private val mRequestListListener by lazy {
		if (request_detail_container == null) {
			// todo: phone
			object : RequestListListener {
				override fun onRead(request: Request) {
					toast(request.title)
				}
				
				override fun onUpdate(request: Request) {
					toast(request.description)
				}
				
				override fun onDelete(request: Request) {
					toast(request.id)
				}
			}
		} else {
			object : RequestListListener {
				override fun onRead(request: Request) {
					toast(request.title)
				}
				
				override fun onUpdate(request: Request) {
					activity?.supportFragmentManager?.beginTransaction()
							?.replace(R.id.request_detail_container, RequestEditFragment.newInstance(request, mEditListener), FRAG_TAG_REQUEST_DETAIL)
							?.commit()
				}
				
				override fun onDelete(request: Request) {
					toast(request.id)
				}
			}
		}
	}
	// endregion
	
	// region { Implement Detail Listener }
	
	private val mEditListener by lazy {
		if (request_detail_container == null) {
			// todo: phone
			object : RequestEditListener {
				override fun onFinished() {
					with(activity?.supportFragmentManager) {
						this?.beginTransaction()?.remove(findFragmentByTag(FRAG_TAG_REQUEST_DETAIL))?.commit()
					}
				}
			}
		} else {
			object : RequestEditListener {
				override fun onFinished() {
					with(activity?.supportFragmentManager) {
						this?.beginTransaction()?.remove(findFragmentByTag(FRAG_TAG_REQUEST_DETAIL))?.commit()
					}
				}
			}
		}
	}
	
	// endregion
	
}