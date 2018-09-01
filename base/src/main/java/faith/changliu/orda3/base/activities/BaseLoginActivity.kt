package faith.changliu.orda3.base.activities

import android.os.Bundle
import android.view.View
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.firebase.FireAuth
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.User
import faith.changliu.orda3.base.data.models.UserType
import faith.changliu.orda3.base.data.preferences.UserPref
import faith.changliu.orda3.base.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.toast


abstract class BaseLoginActivity : BaseActivity(), View.OnClickListener {

	protected abstract val mRegisterTextResId: Int
	private val mUserType: Int by lazy {
		if (mRegisterTextResId == R.string.register_agent) UserType.AGENT
		else UserType.TRAVELER
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		setupViews()
		
		mBtnLogin.setOnClickListener(this)
		mBtnRegister.setOnClickListener(this)
		mBtnResetPwd.setOnClickListener(this)
	}

	private fun setupViews() {
		mBtnRegister.setText(mRegisterTextResId)
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.mBtnLogin -> login()
			R.id.mBtnRegister -> register()
			R.id.mBtnResetPwd -> resetPwd()
		}
	}
	
	private fun resetPwd() {
		val email = mEtLoginEmail.getEmail() ?: return
		FireAuth.resetPassword(email)
	}
	
	protected open fun register() {

	}
	
	private fun login() {
		val email = mEtLoginEmail.getEmail() ?: return
		val pwd = mEtPwd.getString() ?: return
		
		ifConnected {
			tryBlock {
				// try login and get user id
				val userId = async(CommonPool) {
					FireAuth.login(email, pwd)
				}.await()

				// check user type
				val user = async(CommonPool) {
					FireDB.readUserWithId(userId)
				}.await()

				ifUserTypeRight(user.type) {
					UserPref.mUser = user
					toMain()
				}
			}
		}
	}

	protected abstract fun toMain()

	private inline fun ifUserTypeRight(userType: Int, onUserTypeRight: () -> Unit) {
		if (userType == mUserType) {
			toast("Log in successful")
			onUserTypeRight()
		} else {
			toast("User not registered")
			FireAuth.logout()
		}
	}



}