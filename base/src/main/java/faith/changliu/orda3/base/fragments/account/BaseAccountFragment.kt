package faith.changliu.orda3.base.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3.base.BaseFragment
import faith.changliu.orda3.base.R
import kotlinx.android.synthetic.main.fragment_account_base.*

open class BaseAccountFragment : BaseFragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_account_base, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mCusEtZipcode.setOnTextChangeListener { s ->
			if (s.length > 6) mCusEtZipcode.text = s.substring(0, 6)
		}
	}
	
}