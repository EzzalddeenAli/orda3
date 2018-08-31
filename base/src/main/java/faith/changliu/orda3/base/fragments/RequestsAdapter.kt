package faith.changliu.orda3.base.fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.utils.snackConfirm
import kotlinx.android.synthetic.main.cell_request.view.*

class RequestsAdapter(
		var requests: ArrayList<Request>,
		private val onUpdate: (Request) -> Unit,
		private val onDelete: (Request) -> Unit,
		private val onClick: (Request) -> Unit
) : RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_request, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return requests.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(requests[position])
	}

	inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

		fun bind(request: Request) {
			itemView.apply {
				mTvTitle.text = request.title
				mTvMemo.text = request.description

				// todo: enable
//				mBtnDelete.setOnClickListener {
//					snackConfirm(context.getString(R.string.confirm_to_delete)) {
//						onDelete(request)
//					}
//				}
//				mBtnEdit.setOnClickListener {
//					onUpdate(request)
//				}
				mCellView.setOnClickListener {
					onClick(request)
				}
			}
		}
	}
}