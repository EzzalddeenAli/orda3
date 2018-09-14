package faith.changliu.orda3.base.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.AppRepository
import faith.changliu.orda3.base.data.models.Request
import faith.changliu.orda3.base.data.models.RequestStatus
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.*
import kotlinx.android.synthetic.main.fragment_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates

class RequestAddFragment : BaseFragment() {
	
	private var deadline: Date by Delegates.observable(Date()) {
		_, _, newValue ->
		mEtDeadline.setText(parseDateToString(newValue))
	}
	private lateinit var mListener: RequestEditListener
	
	companion object {
		fun newInstance(listener: RequestEditListener): RequestAddFragment {
			val instance = RequestAddFragment()
			instance.mListener = listener
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mEtStatusLayout.setVisible(false)
		mDisplayApplicationLayout.setVisible(false)
		mEtAssignedToLayout.setVisible(false)
		
		
		// todo: pick date
		mEtDeadline.setOnClickListener {
			
			val calendar = GregorianCalendar()
			calendar.time = deadline
			val year = calendar.get(GregorianCalendar.YEAR)
			val month = calendar.get(GregorianCalendar.MONTH)
			val day = calendar.get(GregorianCalendar.DAY_OF_MONTH)
			
			val datePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
				deadline = parseToDate(year, month + 1, dayOfMonth)
			}, year, month, day)
			datePicker.show()
		}
		
		mBtnSubmitNewRequest.setOnClickListener {
			addRequest()
		}
		
		mBtnCancelNewRequest.setOnClickListener {
			mListener.onFinished()
		}
	}
	
	private fun addRequest() {
		if (deadline == null) {
			toast("Deadline not picked")
			return
		}
		val title = mEtTitle.getString() ?: return
		val address = mEtAddress.getString() ?: return
		val city = mEtCity.getString() ?: return
		val country = mEtCountry.getString() ?: return
		val weight = mEtWeight.getDouble() ?: return
		val volume = mEtVolume.getDouble() ?: return
		val compensation = mEtCompensation.getDouble() ?: return
		val description = mEtDescription.getString() ?: return
		val id = UUID.randomUUID().toString()
		
		val newRequest = Request(id, title, RequestStatus.PENDING, "", deadline!!, country, city, address, weight, volume, compensation, description, Date(), UserPref.getId(), UserPref.getEmail())
		
		tryBlock {
			async(CommonPool) {
				AppRepository.insertRequest(newRequest)
			}.await()
			
			toast("New request added")
			mListener.onFinished()
		}
	}
}