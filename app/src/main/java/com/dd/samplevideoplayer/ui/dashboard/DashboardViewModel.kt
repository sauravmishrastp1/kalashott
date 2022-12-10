package com.dd.samplevideoplayer.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val homeResponse: MutableLiveData<HomeResponse> by lazy {
        MutableLiveData<HomeResponse>()
    }

    val catWiseVideo: MutableLiveData<CatWiseVideoResponse> by lazy {
        MutableLiveData<CatWiseVideoResponse>()
    }


    val latestVideoResponse: MutableLiveData<VideosResponseModel> by lazy {
        MutableLiveData<VideosResponseModel>()
    }

    val homeResponse_: LiveData<HomeResponse> = homeResponse

    val catWiseVideo_: LiveData<CatWiseVideoResponse> = catWiseVideo

    var markupeTextRespponse = MutableLiveData<MarkupeTextRespponse>()


    val latestVideoResponse_: LiveData<VideosResponseModel> = latestVideoResponse

    val categoryResponseModel: MutableLiveData<CategoryResponseModel> by lazy {
        MutableLiveData<CategoryResponseModel>()
    }

    val categoryResponseModel_: LiveData<CategoryResponseModel> = categoryResponseModel


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

    fun getCateofry() {
        WebServiceRequests.webServiceRequests.getCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<CategoryResponseModel>() {
                override fun onNext(t: CategoryResponseModel) {
                    categoryResponseModel.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })

    }

    fun getCateWiseVideo(
        vid: String
    ) {

        WebServiceRequests.webServiceRequests.getFriendRequest(vid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<CatWiseVideoResponse>() {
                override fun onNext(t: CatWiseVideoResponse) {

                    catWiseVideo.value = t

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


}