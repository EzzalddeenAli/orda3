package faith.changliu.orda3.traveler.account

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.data.models.Rating
import faith.changliu.orda3.base.utils.log
import faith.changliu.orda3.traveler.R
import kotlinx.android.synthetic.main.fragment_ratings_list_traveler.*
import java.util.*

class RatingsListFragment : BaseFragment() {
	
	companion object {
		fun newInstance(): RatingsListFragment {
			val instance = RatingsListFragment()
			
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		log(container.toString())
		return inflater.inflate(R.layout.fragment_ratings_list_traveler, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mRcvRatings.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = TravelerRatingAdapter(arrayListOf(
					Rating("1", "Agent ID", "Traveler ID", 4.0, Date(), "Comment 1"),
					Rating("1", "Agent ID", "Traveler ID", 4.0, Date(), "Comment 1"),
					Rating("1", "Agent ID", "Traveler ID", 4.0, Date(), "Comment 1"),
					Rating("1", "Agent ID", "Traveler ID", 4.0, Date(), "Comment 1"),
					Rating("1", "Agent ID", "Traveler ID", 4.0, Date(), "Comment 1")
			))
		}
	}
}