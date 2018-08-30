package faith.changliu.orda3.base

import android.app.Application
import android.content.ContextWrapper

private lateinit var INSTANCE: Application

class MyApp : Application() {

	override fun onCreate() {
		super.onCreate()
		INSTANCE = this
	}

}

object AppContext: ContextWrapper(INSTANCE)