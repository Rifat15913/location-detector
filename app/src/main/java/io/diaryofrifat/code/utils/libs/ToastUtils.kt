package io.diaryofrifat.code.utils.libs

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.Toast
import es.dmoral.toasty.Toasty
import io.diaryofrifat.code.BaseApplication

object ToastUtils {
    init {
        Toasty.Config
                .getInstance()
                .tintIcon(true)
                .apply()
    }

    /**
     * This method shows an error toast for short period
     *
     * @param text the text to be shown
     * */
    fun error(text: String) {
        Toasty.error(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, true)
                .show()
    }

    /**
     * This method shows an error toast for short period but without icon
     *
     * @param text the text to be shown
     * */
    fun errorWithoutIcon(text: String) {
        Toasty.error(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, false)
                .show()
    }

    /**
     * This method shows a success toast for short period
     *
     * @param text the text to be shown
     * */
    fun success(text: String) {
        Toasty.success(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, true)
                .show()
    }

    /**
     * This method shows a success toast for short period but without icon
     *
     * @param text the text to be shown
     * */
    fun successWithoutIcon(text: String) {
        Toasty.success(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, false)
                .show()
    }

    /**
     * This method shows an info toast for short period
     *
     * @param text the text to be shown
     * */
    fun info(text: String) {
        Toasty.info(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, true)
                .show()
    }

    /**
     * This method shows an info toast for short period but without icon
     *
     * @param text the text to be shown
     * */
    fun infoWithoutIcon(text: String) {
        Toasty.info(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, false)
                .show()
    }

    /**
     * This method shows a warning toast for short period
     *
     * @param text the text to be shown
     * */
    fun warning(text: String) {
        Toasty.warning(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, true)
                .show()
    }

    /**
     * This method shows a warning toast for short period but without icon
     *
     * @param text the text to be shown
     * */
    fun warningWithoutIcon(text: String) {
        Toasty.warning(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, false)
                .show()
    }

    /**
     * This method shows a normal toast with icon for short period
     *
     * @param text the text to be shown
     * */
    fun normalWithIcon(text: String, icon: Drawable) {
        Toasty.normal(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, icon, true)
                .show()
    }

    /**
     * This method shows a normal toast without icon for short period
     *
     * @param text the text to be shown
     * */
    fun normalShort(text: String) {
        Toasty.normal(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_SHORT, null, false)
                .show()
    }

    /**
     * This method shows a normal toast without icon for long period
     *
     * @param text the text to be shown
     * */
    fun normalLong(text: String) {
        Toasty.normal(BaseApplication.getBaseApplicationContext(), text,
                Toast.LENGTH_LONG, null, false)
                .show()
    }

    /**
     * This method shows a custom toast
     *
     * @param text the text to be shown
     * */
    fun custom(text: String, icon: Drawable, colorId: Int, shortDuration: Boolean,
               withIcon: Boolean, shouldTint: Boolean) {
        Toasty.custom(BaseApplication.getBaseApplicationContext(), text,
                icon, colorId,
                if (shortDuration) {
                    Toast.LENGTH_SHORT
                } else {
                    Toast.LENGTH_LONG
                }, withIcon, shouldTint)
                .show()
    }

    /**
     * This method creates and shows a native toast for long period
     *
     * @param text the text to be shown
     * */
    fun nativeLong(text: String) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(BaseApplication.getBaseApplicationContext(),
                    text, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * This method creates and shows a native toast for short period
     *
     * @param text the text to be shown
     * */
    fun nativeShort(text: String) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(BaseApplication.getBaseApplicationContext(),
                    text, Toast.LENGTH_SHORT).show()
        }
    }
}