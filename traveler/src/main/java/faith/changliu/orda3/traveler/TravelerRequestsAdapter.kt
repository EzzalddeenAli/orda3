package faith.changliu.orda3.traveler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.data.models.Request
import kotlinx.android.synthetic.main.cell_request_traveler.view.*
import kotlin.properties.Delegates

class TravelerRequestsAdapter(
		var requests: ArrayList<Request>,
		private val onContactAgent: (Request) -> Unit,
		private val onApply: (Request) -> Unit
) : RecyclerView.Adapter<TravelerRequestsAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_request_traveler, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return requests.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(requests[position])
	}

	inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

		private var isOpen by Delegates.observable(false) { _, _, newValue ->
			itemView.mLayoutReveal.visibility = if (newValue) View.VISIBLE else View.GONE
		}

		fun bind(request: Request) {
			itemView.apply {
				mTvTitle.text = request.title
				mTvMemo.text = request.description
				mBtnContactAgent.setOnClickListener {
					onContactAgent(request)
					isOpen = false
				}
				mBtnApply.setOnClickListener {
					onApply(request)
					isOpen = false
				}

				mCellView.setOnClickListener {
					isOpen = !isOpen
				}
			}
		}
	}
}