package faith.changliu.orda3.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun Context.readBitmap(resId: Int): Bitmap? {
	return BitmapFactory.decodeResource(resources, resId)
}