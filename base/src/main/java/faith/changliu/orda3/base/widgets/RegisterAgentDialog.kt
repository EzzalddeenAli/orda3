package faith.changliu.orda3.base.widgets

import android.content.Context
import android.view.View
import faith.changliu.orda3.base.BaseDialog
import faith.changliu.orda3.base.R
import faith.changliu.orda3.base.data.firebase.FireAuth
import faith.changliu.orda3.base.data.firebase.firestore.FireDB
import faith.changliu.orda3.base.data.models.User
import faith.changliu.orda3.base.data.models.UserType
import faith.changliu.orda3.base.utils.getString
import faith.changliu.orda3.base.utils.ifConnected
import faith.changliu.orda3.base.utils.log
import kotlinx.android.synthetic.main.register_dialog.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.email
import org.jetbrains.anko.toast

class RegisterAgentDialog(ctx: Context) : BaseDialog(ctx), View.OnClickListener {

	override val layoutResId: Int = R.layout.register_dialog

	override fun setupOnConfirm() {
		mBtnSubmit.setOnClickListener(this)
	}

	override fun onClick(v: View) {
		if (v.id == mBtnSubmit.id) {
			val email = mEtEmail.getString() ?: return

			ifConnected {
				launch(UI) {
					try {
						mLoading.startLoading()
						val isRegistered = async(CommonPool) {
							FireAuth.isEmailRegistered(email)
						}.await()

						if (isRegistered) {
							// todo: get user type
							val userType = async(CommonPool) {
								FireDB.getUserTypeWithEmail(email)
							}.await()

							// check user type
							if (userType == UserType.TRAVELER) {

								// todo: notify manager to handle it, change UserType from traveler to both after signing contract, etc.
								this@RegisterAgentDialog.context.toast("From Traveler to Agent function to be added")
								dismiss()
							} else {
								this@RegisterAgentDialog.apply {
									context.toast("User already registered")
									dismiss()
								}
							}
						}
						else {
							with(this@RegisterAgentDialog.context) {
								// todo: get real user
								val userId = System.currentTimeMillis().toString()
								val newUser = User()
								async(CommonPool) {
									FireDB.saveAgentRegisterRequest(newUser)
								}.await()

								toast("Register Request submitted.")
								dismiss()
							}
						}

					} catch (ex: Exception) {
						ex.printStackTrace()
						log(ex.localizedMessage)
					} finally {
						// todo: finally sometimes is omitted
						mLoading.stopLoading()
					}
				}
			}
		}
	}
}