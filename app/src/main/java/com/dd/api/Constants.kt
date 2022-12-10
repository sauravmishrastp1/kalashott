package com.dd.api

class Constants {
    companion object {
        const val BASE_URL = "https://webapi.ottsplatform.com/api/"
        const val WEB_URL = "https://ott-upload-video.s3.ap-south-1.amazonaws.com/"
        const val LIVE_URL = "https://webapi.ottsplatform.com/#/watch-live/"
        var isRotate = 0

    }

    internal object Partials {
        const val liveVideoList = "stream/getAllLiveStreams"
        const val getHomeScreenVideos = "video/getCategoryScreenVideos"
        const val latestVideo = "video"
        const val category = "category"
        const val getVideosByCategory = "video/getVideosByCategory/{categoryId}"
        const val searchVideo = "video/searchVideo/{searchText}"
        const val marqueTex = "video/marque/text"
        const val getStreamForBanner ="stream/getStreamForBanner"
        const val about ="common/getAboutDetails"
        const val trendingVideo ="video/getTrendingVideos"
        const val videoViewCount ="video/addViewCount/{id}"


    }

    internal object Keys


}