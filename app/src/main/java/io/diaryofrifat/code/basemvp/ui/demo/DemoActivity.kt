package io.diaryofrifat.code.basemvp.ui.demo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import io.diaryofrifat.code.basemvp.R
import io.diaryofrifat.code.basemvp.databinding.ActivityDemoBinding
import io.diaryofrifat.code.basemvp.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.PermissionUtils
import io.diaryofrifat.code.utils.helper.imagepicker.ImagePickerUtils
import io.diaryofrifat.code.utils.libs.GlideUtils
import io.diaryofrifat.code.utils.libs.ImageCropperUtils
import timber.log.Timber

class DemoActivity : BaseActivity<DemoMvpView, DemoPresenter>() {

    private lateinit var mBinding: ActivityDemoBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_demo

    override fun getActivityPresenter(): DemoPresenter {
        return DemoPresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityDemoBinding

        setClickListener(mBinding.buttonDo)
    }

    override fun onClick(view: View) {
        super.onClick(view)

        when (view.id) {
            R.id.button_do -> {
                if (PermissionUtils.requestPermission(this,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    pickImage()
                }
            }
        }
    }

    private fun pickImage() {
        ImagePickerUtils.pickImageAndCrop(this, object : ImageCropperUtils.Listener {
            override fun onError(error: Throwable) {
                Timber.e(error)
            }

            override fun onSuccess(imageUri: Uri) {
                Timber.d(imageUri.toString())

                GlideUtils.default(mBinding.imageViewPickedPhoto,
                        imageUri, true,
                        GlideUtils.ImageCropType.SQUARE, null)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionUtils.REQUEST_CODE_PERMISSION_DEFAULT) {
            for (i in permissions.indices) {
                when (permissions[i]) {
                    Manifest.permission.CAMERA -> {

                    }

                    Manifest.permission.READ_EXTERNAL_STORAGE -> {

                    }

                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            pickImage()
                        }
                    }
                }
            }
        }
    }

    override fun stopUI() {

    }
}