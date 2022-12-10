package com.dd.samplevideoplayer.ui.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.LiveVideoResponse
import com.dd.api.baseModel.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NotificationsViewModel() : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    private val response:MutableLiveData<LiveVideoResponse> by lazy {
        MutableLiveData<LiveVideoResponse>()
    }
    val response_:LiveData<LiveVideoResponse> = response

    private val searchResponse:MutableLiveData<SearchResponse> by lazy {
        MutableLiveData<SearchResponse>()
    }

    val searchRequests_:LiveData<SearchResponse> = searchResponse

    val error = MutableLiveData<Throwable>()


    fun hitApi(webServiceRequests: WebServiceRequests){
        webServiceRequests.getAllLiveVideo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<LiveVideoResponse>(){
                override fun onNext(t: LiveVideoResponse) {
                    response.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }


    fun search(keyWord:String){
        WebServiceRequests.webServiceRequests.getSearchVideo(keyWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<SearchResponse>(){
                override fun onNext(t: SearchResponse) {
                    searchResponse.value = t
                }

                override fun onError(e: Throwable) {
                    error.value = e

                }

                override fun onComplete() {
                }

            })
    }
}