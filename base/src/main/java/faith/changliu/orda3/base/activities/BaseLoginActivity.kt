package faith.changliu.orda3.base.activities

import android.os.Bundle
import android.view.View
import faith.changliu.orda3.base.BaseActivity
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.toast


abstract class BaseLoginActivity : BaseActivity(), View.OnClickListener {

	protected abstract val registerTextResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		setupViews()
		
		mBtnLogin.setOnClickListener(this)
		mBtnRegister.setOnClickListener(this)
		mBtnResetPwd.setOnClickListener(this)
	}

	private fun setupViews() {
		mBtnRegister.setText(registerTextResId)
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
	}
	
	private fun register() {
//		val dialog = RegisterDialog(this)
//		dialog.show()
	}
	
	private fun login() {
		val email = mEtLoginEmail.getEmail() ?: return
		val pwd = mEtPwd.getString() ?: return
		
		ifConnected {
			tryBlock {
				val userId = async(CommonPool) {
					"Try login"
				}.await()
				
				toast("Logging in")
			}
		}
		
		
	}
	
//	private fun toMain() {
//		val intent = Intent(this, MainActivity::class.java)
//		intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//		startActivity(intent)
//		finish()
//	}
}