package faith.changliu.orda3.traveler.account

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.Rating
import faith.changliu.orda3.traveler.R
import kotlinx.android.synthetic.main.cell_rating_traveler.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class TravelerRatingAdapter(val ratings: ArrayList<Rating>) : RecyclerView.Adapter<TravelerRatingAdapter.ViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_rating_traveler, parent, false)
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
					val agent = async(CommonPool) {
						FireDB.readUserWithId(rating.agentId)
					}.await()
					mCusTvRatingAgent.setText(agent.email)
				}
			}
		}
	}
}