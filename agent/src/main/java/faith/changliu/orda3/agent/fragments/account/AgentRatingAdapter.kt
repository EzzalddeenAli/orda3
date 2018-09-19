package faith.changliu.orda3.agent.fragments.account

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.agent.R
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.Rating
import kotlinx.android.synthetic.main.cell_rating_agent.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class AgentRatingAdapter(val ratings: ArrayList<Rating>) : RecyclerView.Adapter<AgentRatingAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_rating_agent, parent, false)
		return ViewHolder(view)
	}
	
	override fun getItemCount() = ratings.size
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(ratings[position])
	}
	
	inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
		fun bind(rating: Rating) {
			itemView.apply {
				mCusRatingBar.mRating = rating.rate
				mCusTvRatingComment.setText(rating.comment)
				
				launch(UI) {
					val traveler = async(CommonPool) {
						FireDB.readUserWithId(rating.travelerId)
					}.await()
					mCusTvRatingTraveler.setText(traveler.email)
				}
			}
		}
	}
}