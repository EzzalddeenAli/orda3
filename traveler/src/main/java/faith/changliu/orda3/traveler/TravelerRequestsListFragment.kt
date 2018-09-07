package faith.changliu.orda3.traveler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment

class TravelerRequestsListFragment : BaseFragment() {
	
	companion object {
		fun newInstance(): TravelerRequestsListFragment {
			val instance = TravelerRequestsListFragment()
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_requests_list_traveler, container, false)
	}
	
	
}