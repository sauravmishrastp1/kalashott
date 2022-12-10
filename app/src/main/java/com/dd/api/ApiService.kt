package com.dd.api


import com.dd.api.baseModel.*
import com.dd.samplevideoplayer.ui.about.model.About
import com.dd.samplevideoplayer.ui.home.model.GetLiveBanner
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @GET(Constants.Partials.liveVideoList)
    fun getAllLiveVideo(): Observable<LiveVideoResponse>

    @GET(Constants.Partials.getHomeScreenVideos)
    fun getHomeScreenVideos(): Observable<HomeResponse>

    @GET(Constants.Partials.latestVideo)
    fun getLatestVideo(): Observable<VideosResponseModel>

    @GET(Constants.Partials.trendingVideo)
    fun getTrendingVideo(): Observable<VideosResponseModel>

    @GET(Constants.Partials.category)
    fun getCategory(): Observable<CategoryResponseModel>

    @GET(Constants.Partials.getVideosByCategory)
    fun getFriendRequest(@Path("categoryId") userId: String): Observable<CatWiseVideoResponse>

    @PUT(Constants.Partials.videoViewCount)
    fun videoViewCount(@Path("id") userId: String): Observable<ResponseBody>


    @GET(Constants.Partials.searchVideo)
    fun getSearchVideo(@Path("searchText") userId: String): Observable<SearchResponse>

    @GET(Constants.Partials.marqueTex)
    fun getMarqueText(): Observable<MarkupeTextRespponse>

    @GET(Constants.Partials.getStreamForBanner)
    fun getStreamForBanner():Observable<GetLiveBanner>



    @GET(Constants.Partials.about)
    fun about():Observable<About>



}



