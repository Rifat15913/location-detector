package io.diaryofrifat.code.locationdetector.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import io.diaryofrifat.code.BaseApplication
import io.diaryofrifat.code.locationdetector.R
import io.diaryofrifat.code.locationdetector.databinding.ActivityMainBinding
import io.diaryofrifat.code.locationdetector.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.PermissionUtils
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider


class MainActivity : BaseActivity<MainMvpView, MainPresenter>(), MainMvpView {

    /**
     * Fields
     * */
    private val REQUEST_CHECK_SETTINGS = 1
    private lateinit var mBinding: ActivityMainBinding
    private var mMyLocationMarker: Marker? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun getActivityPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun shouldShowBackIconAtToolbar(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Configuration.getInstance().load(BaseApplication.getBaseApplicationContext(),
                SharedPrefUtils.preferences)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun startUI() {
        mBinding = viewDataBinding as ActivityMainBinding

        mBinding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mBinding.mapView.setMultiTouchControls(true)

        val compassOverlay = CompassOverlay(this,
                InternalCompassOrientationProvider(this), mBinding.mapView)
        compassOverlay.enableCompass()
        mBinding.mapView.overlays.add(compassOverlay)
    }

    override fun stopUI() {

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionUtils.REQUEST_CODE_PERMISSION_DEFAULT) {
            var isValid = true

            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) isValid = false
            }

            if (isValid) {
                createLocationRequest()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        presenter.startGettingLocationUpdates()
                    }

                    else -> {
                        ToastUtils.error(getString(R.string.error_disabled_gps))
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.stopGettingLocationUpdates()
        mBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (PermissionUtils.requestPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            createLocationRequest()
        }
        mBinding.mapView.onResume()
    }

    private fun createLocationRequest() {
        val shouldRequest = presenter.checkIfTheDeviceHasGooglePlayServices(this)

        if (shouldRequest) {
            presenter.checkLocationSettings(REQUEST_CHECK_SETTINGS)
        } else {
            ToastUtils.error(getString(R.string.error_this_device_does_not_have_google_play_services))
        }
    }

    override fun onGettingLocation(location: Location) {
        if (mMyLocationMarker == null)
            mMyLocationMarker = Marker(mBinding.mapView)
        else
            mBinding.mapView.overlays.remove(mMyLocationMarker)

        mMyLocationMarker?.position = GeoPoint(location)
        mMyLocationMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        mBinding.mapView.controller.setZoom(20.0)
        mBinding.mapView.controller.animateTo(GeoPoint(location))
        mBinding.mapView.overlays.add(mMyLocationMarker)
    }
}
