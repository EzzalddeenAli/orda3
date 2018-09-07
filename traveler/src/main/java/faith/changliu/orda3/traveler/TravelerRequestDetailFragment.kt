package faith.changliu.orda3.traveler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment

class TravelerRequestDetailFragment : BaseFragment() {
	
	companion object {
		fun newInstance(): TravelerRequestDetailFragment {
			val instance = TravelerRequestDetailFragment()
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail_traveler, container, false)
	}
}