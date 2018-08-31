package faith.changliu.orda3.base.data.viewmodels

import android.arch.lifecycle.ViewModel
import faith.changliu.orda3.base.data.AppRepository

class MainViewModel : ViewModel() {
	var orders = AppRepository.getAllOrders()
}

class RequestViewModel : ViewModel() {
	var requests = AppRepository.getAllRequests()
}