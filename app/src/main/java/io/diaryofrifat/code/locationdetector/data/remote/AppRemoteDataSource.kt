package io.diaryofrifat.code.locationdetector.data.remote

import android.content.Context
import io.diaryofrifat.code.locationdetector.data.remote.retrophoto.RetroPhoto
import io.diaryofrifat.code.locationdetector.data.remote.retrophoto.RetroPhotoService
import io.reactivex.Flowable

class AppRemoteDataSource(context: Context){
    fun getAllPhotos(): Flowable<List<RetroPhoto>> {
        return RetroPhotoService.on().getAllPhotos().onBackpressureLatest()
    }
}