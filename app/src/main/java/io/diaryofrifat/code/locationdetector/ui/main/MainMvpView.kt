package io.diaryofrifat.code.locationdetector.ui.main

import android.location.Location
import io.diaryofrifat.code.locationdetector.ui.base.callback.MvpView

interface MainMvpView : MvpView {
    fun onGettingLocation(location: Location)
}