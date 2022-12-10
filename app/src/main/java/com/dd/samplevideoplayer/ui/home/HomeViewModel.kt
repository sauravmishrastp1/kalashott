package com.dd.samplevideoplayer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.HomeResponse
import com.dd.api.baseModel.MarkupeTextRespponse
import com.dd.api.baseModel.VideosResponseModel
import com.dd.samplevideoplayer.ui.home.model.GetLiveBanner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val homeResponse: MutableLiveData<HomeResponse> by lazy {
        MutableLiveData<HomeResponse>()
    }

    val latestVideoResponse: MutableLiveData<VideosResponseModel> by lazy {
        MutableLiveData<VideosResponseModel>()
    }

    val trendingVideoResponse: MutableLiveData<VideosResponseModel> by lazy {
        MutableLiveData<VideosResponseModel>()
    }

    val getSreemBannerLive :MutableLiveData<GetLiveBanner> by lazy {
        MutableLiveData<GetLiveBanner>()
    }
    val homeResponse_: LiveData<HomeResponse> = homeResponse

    val latestVideoResponse_: LiveData<VideosResponseModel> = latestVideoResponse

    val trendingVideoResponse_: LiveData<VideosResponseModel> = trendingVideoResponse

    var markupeTextRespponse = MutableLiveData<MarkupeTextRespponse>()


    fun hitApi() {
        WebServiceRequests.webServiceRequests.getHomeScreenVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<HomeResponse>() {
                override fun onNext(t: HomeResponse) {
                    homeResponse.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }


    fun getLatestVideo() {
        WebServiceRequests.webServiceRequests.getLatestVideo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<VideosResponseModel>() {
                override fun onNext(t: VideosResponseModel) {
                    latestVideoResponse.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }

    fun getTrendingVideo() {
        WebServiceRequests.webServiceRequests.getTrendingVideo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<VideosResponseModel>() {
                override fun onNext(t: VideosResponseModel) {
                    trendingVideoResponse.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }


    fun getMarkupeText() {
        WebServiceRequests.webServiceRequests.getMarkupeText()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<MarkupeTextRespponse>() {
                override fun onNext(t: MarkupeTextRespponse) {
                    markupeTextRespponse.value = t
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })
    }

    fun getLiveStreemBanner(){
        WebServiceRequests.webServiceRequests.getStreemBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<GetLiveBanner>() {
                override fun onNext(t: GetLiveBanner) {
                    getSreemBannerLive.value = t
                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })
    }


}