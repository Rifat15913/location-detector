package io.diaryofrifat.code.utils.libs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yalantis.ucrop.UCrop
import io.diaryofrifat.code.locationdetector.R
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.FileUtils
import io.diaryofrifat.code.utils.helper.imagepicker.ImagePickerUtils
import java.io.File

object ImageCropperUtils {

    /**
     * Fields
     * */
    private var mListener: Listener? = null
    private var mCroppedImage: File? = null

    /**
     * This method crops image in square shape using default max width and height
     *
     * @param activity current activity
     * @param sourceUri the image to be cropped
     * @param listener callback to get the states
     * */
    fun cropImage(activity: Activity, sourceUri: Uri, listener: Listener) {
        val destinationUri = getCroppedImageDestinationUri(activity)

        if (destinationUri != null) {
            mListener = listener
            cropImage(activity, null, sourceUri, destinationUri, CropRatio.DEFAULT)
        }
    }

    /**
     * This method crops image in square shape using default max width and height
     *
     * @param fragment current fragment
     * @param sourceUri the image to be cropped
     * @param listener callback to get the states
     * */
    fun cropImage(fragment: Fragment, sourceUri: Uri, listener: Listener) {
        if (fragment.context != null) {
            val destinationUri = getCroppedImageDestinationUri(fragment.context!!)

            if (destinationUri != null) {
                mListener = listener
                cropImage(null, fragment, sourceUri, destinationUri, CropRatio.DEFAULT)
            }
        }
    }

    /**
     * This method crops image in square shape using default max width and height
     *
     * @param activity current activity
     * @param sourceUri the image to be cropped
     * @param destinationUri the cropped image
     * */
    fun cropImage(activity: Activity, sourceUri: Uri, destinationUri: Uri) {
        cropImage(activity, null, sourceUri, destinationUri, CropRatio.DEFAULT)
    }

    /**
     * This method crops image in custom shape using default max width and height
     *
     * @param activity current activity
     * @param sourceUri the image to be cropped
     * @param destinationUri the cropped image
     * @param cropRatio crop ratio of the image
     * */
    fun cropImage(activity: Activity, sourceUri: Uri, destinationUri: Uri, cropRatio: CropRatio) {
        cropImage(activity, null, sourceUri, destinationUri, cropRatio)
    }

    /**
     * This method crops image in custom shape using default max width and height
     *
     * @param fragment current fragment
     * @param sourceUri the image to be cropped
     * @param destinationUri the cropped image
     * */
    fun cropImage(fragment: Fragment, sourceUri: Uri, destinationUri: Uri) {
        cropImage(null, fragment, sourceUri, destinationUri, CropRatio.DEFAULT)
    }

    /**
     * This method crops image with custom params
     *
     * @param fragment current fragment
     * @param sourceUri the image to be cropped
     * @param destinationUri the cropped image
     * @param cropRatio crop ratio of the image
     * */
    private fun cropImage(fragment: Fragment, sourceUri: Uri, destinationUri: Uri, cropRatio: CropRatio) {
        cropImage(null, fragment, sourceUri, destinationUri, cropRatio)
    }

    private fun cropImage(activity: Activity?,
                          fragment: Fragment?,
                          sourceUri: Uri, destinationUri: Uri,
                          cropRatio: CropRatio) {

        val context: Context = when {
            activity != null -> activity
            fragment != null -> fragment.context
            else -> null
        } ?: return

        val options = UCrop.Options()
        options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorPrimary))
        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        options.setCompressionQuality(100)
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)

        val cropper = UCrop.of(sourceUri, destinationUri)
                .withOptions(options)

        when (cropRatio) {
            CropRatio.DEFAULT -> {
                cropper.withAspectRatio(1.toFloat(), 1.toFloat())
            }

            CropRatio.SQUARE -> {
                cropper.withAspectRatio(1.toFloat(), 1.toFloat())
            }

            CropRatio.WIDE -> {
                cropper.withAspectRatio(16.toFloat(), 9.toFloat())
            }

            CropRatio.EXTRA_WIDE -> {
                cropper.withAspectRatio(18.toFloat(), 9.toFloat())
            }

            CropRatio.REGULAR -> {
                cropper.withAspectRatio(4.toFloat(), 3.toFloat())
            }

            else -> {
                // Skip
            }
        }

        if (activity != null) {
            cropper.start(activity)
        } else if (fragment != null) {
            cropper.start(context, fragment)
        }
    }

    /**
     * This method provides the uri of the cropped image
     *
     * @param context current UI context
     * */
    fun getCroppedImageDestinationUri(context: Context): Uri? {
        val imageFile = FileUtils.getEmptyFileForSavingCroppedImage(context)

        return if (imageFile != null) {
            mCroppedImage = imageFile
            Uri.fromFile(imageFile)
        } else {
            null
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val croppedImageUri = UCrop.getOutput(data)

                        if (croppedImageUri != null) {
                            mListener?.onSuccess(croppedImageUri)
                        } else {
                            mListener?.onError(NullPointerException(
                                    DataUtils.getString(R.string.error_image_uri_is_null)))
                            mCroppedImage?.delete()
                            clearUtil()
                        }

                        ImagePickerUtils.deleteTheImageFile()
                        ImagePickerUtils.clearUtil()
                    }
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    ToastUtils.error(DataUtils.getString(R.string.error_could_not_crop_image))

                    if (data != null && UCrop.getError(data) != null) {
                        mListener?.onError(UCrop.getError(data)!!)
                    }

                    mCroppedImage?.delete()
                    clearUtil()
                    ImagePickerUtils.deleteTheImageFile()
                    ImagePickerUtils.clearUtil()
                } else {
                    mCroppedImage?.delete()
                    clearUtil()
                    ImagePickerUtils.deleteTheImageFile()
                    ImagePickerUtils.clearUtil()
                }
            }

            else -> {
                return
            }
        }
    }

    private fun clearUtil() {
        mListener = null
        mCroppedImage = null
    }

    enum class CropRatio {
        DEFAULT, // 1:1
        SQUARE, // 1:1
        WIDE, // 16:9
        EXTRA_WIDE, // 18:9
        REGULAR, // 4:3
        CUSTOM
    }

    interface Listener {
        fun onSuccess(imageUri: Uri)
        fun onError(error: Throwable)
    }
}
