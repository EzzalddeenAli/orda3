package faith.changliu.orda3.traveler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.utils.KEY_REQUEST
import kotlinx.android.synthetic.main.fragment_request_detail_traveler.*
import org.jetbrains.anko.support.v4.toast
import kotlin.properties.Delegates

class TravelerRequestDetailFragment : BaseFragment() {
	
	private var mRequest by Delegates.observable(Request()) { _, _, newValue ->
		mTvTitle.text = newValue.title
		mCusTVTitle.setText(newValue.title)
	}
	
	companion object {
		fun newInstance(request: Request): TravelerRequestDetailFragment {
			val instance = TravelerRequestDetailFragment()
			val bundle = Bundle()
			bundle.putSerializable(KEY_REQUEST, request)
			instance.arguments = bundle
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail_traveler, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		(arguments?.getSerializable(KEY_REQUEST) as? Request)?.let {
			mRequest = it
		}
		
	}
}