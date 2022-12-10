package com.dd.samplevideoplayer.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.HomeResponse
import com.dd.api.baseModel.MarkupeTextRespponse
import com.dd.api.baseModel.VideosResponseModel
import com.dd.samplevideoplayer.ui.about.model.About
import com.dd.samplevideoplayer.ui.home.model.GetLiveBanner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AboutViewModel : ViewModel() {



    val about: MutableLiveData<About> by lazy {
        MutableLiveData<About>()
    }



    val getSreemBannerLive :MutableLiveData<GetLiveBanner> by lazy {
        MutableLiveData<GetLiveBanner>()
    }
    val aboutLive: LiveData<About> = about



    fun hitApi() {
        WebServiceRequests.webServiceRequests.about()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<About>() {
                override fun onNext(t: About) {
                    about.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }




}