package io.diaryofrifat.code.locationdetector.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import io.diaryofrifat.code.locationdetector.ui.base.component.BasePresenter

class MainPresenter : BasePresenter<MainMvpView>() {

    private var mSettingsBuilder: LocationSettingsRequest.Builder? = null
    private var mSettingClient: SettingsClient? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private val mLocationRequest = LocationRequest.create()?.apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun checkIfTheDeviceHasGooglePlayServices(context: Context): Boolean {
        val state = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

        return when (state) {
            ConnectionResult.SUCCESS -> true
            else -> false
        }
    }

    fun checkLocationSettings(requestCode: Int) {
        if (activity == null)
            return
        if (mSettingsBuilder == null)
            mSettingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)
        if (mSettingClient == null)
            mSettingClient = LocationServices.getSettingsClient(activity!!)

        mSettingClient?.checkLocationSettings(mSettingsBuilder?.build())!!
                .addOnSuccessListener {
                    startGettingLocationUpdates()
                }.addOnFailureListener {
                    if (it is ResolvableApiException) {
                        try {
                            it.startResolutionForResult(activity!!, requestCode)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error
                        }
                    }
                }
    }

    @SuppressLint("MissingPermission")
    fun startGettingLocationUpdates() {
        if (activity == null) return

        if (mFusedLocationClient == null)
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        if (mLocationCallback == null) {
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult != null)
                        mvpView?.onGettingLocation(locationResult.lastLocation)
                }
            }
        }

        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback,
                Looper.myLooper())
    }

    fun stopGettingLocationUpdates() {
        if (mLocationCallback != null)
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback!!)
    }
}