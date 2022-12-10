package com.dd.api

import com.dd.api.baseModel.*
import com.dd.samplevideoplayer.ui.about.model.About
import com.dd.samplevideoplayer.ui.home.model.GetLiveBanner
import io.reactivex.Observable
import okhttp3.ResponseBody

class WebServiceRequests {
    companion object {
        val webServiceRequests = WebServiceRequests()
    }

    val apiService: ApiService by lazy {
        ApiClient.getClient()!!.create(ApiService::class.java)
    }

    fun getAllLiveVideo(): Observable<LiveVideoResponse> {
        return apiService.getAllLiveVideo()
    }

    fun getHomeScreenVideos(): Observable<HomeResponse> {
        return apiService.getHomeScreenVideos()
    }

    fun getLatestVideo(): Observable<VideosResponseModel> {
        return apiService.getLatestVideo()
    }
    fun getTrendingVideo():Observable<VideosResponseModel>{
        return apiService.getTrendingVideo()

    }


    fun getCategory(): Observable<CategoryResponseModel> {
        return apiService.getCategory()
    }

    fun getFriendRequest(userId: String): Observable<CatWiseVideoResponse> {
        return apiService.getFriendRequest(userId)
    }

    fun getSearchVideo(keyword: String): Observable<SearchResponse> {
        return apiService.getSearchVideo(keyword)
    }

    fun getMarkupeText(): Observable<MarkupeTextRespponse> {
        return apiService.getMarqueText()
    }

    fun getStreemBanner():Observable<GetLiveBanner>{
        return  apiService.getStreamForBanner()
    }

    fun about():Observable<About>{
        return  apiService.about()
    }


}
