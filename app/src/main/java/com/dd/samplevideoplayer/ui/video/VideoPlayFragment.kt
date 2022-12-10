package com.dd.samplevideoplayer.ui.video

import android.app.AlertDialog
import android.app.UiModeManager
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore.Audio.Media
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
//import com.danikula.videocache.HttpProxyCacheServer
import com.db.AppDatabase
import com.db.ChangeOrintation
import com.db.ContactEntity
import com.dd.api.App
import com.dd.api.Constants
import com.dd.api.Constants.Companion.isRotate
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.VideoData_
import com.dd.api.baseModel.VideosResponseModel
import com.dd.getParentActivity
import com.dd.samplevideoplayer.MainActivity2
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentVideoPlayBinding
import com.dd.samplevideoplayer.ui.ShareViewModel
import com.dd.samplevideoplayer.ui.dashboard.DashboardViewModel
import com.dd.samplevideoplayer.ui.home.adapter.FunAdapter
import com.dd.toggleLoader
import com.devbrackets.android.exomedia.core.listener.ExoPlayerListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*
import xyz.doikki.videoplayer.player.VideoView


class VideoPlayFragment : Fragment() {
    lateinit var prepareView: PrepareView

    private var _binding: FragmentVideoPlayBinding? = null
    private val binding get() = _binding!!
    private var paramsNotFullscreen
            : RelativeLayout.LayoutParams? = null
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var latestAdapter: FunAdapter
    private lateinit var appDatabase: AppDatabase
    private var url = ""
    private var mRewardedAd: RewardedAd? = null
    private lateinit var adView: AdView
    private var initialLayoutComplete = false
    private var mWebChromeClient: myWebChromeClient? = null
    private var mWebViewClient: myWebViewClient? = null
    private var mCoinCount: Int = 0
    private var mCountDownTimer: CountDownTimer? = null
    private var mGameOver = false
    private var mGamePaused = false
    private var mIsLoading = false
    private var mTimeRemaining: Long = 0L
    private var videoId = ""
    private var playPauseState = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private var latestVideo: VideosResponseModel? = null
    private var u =""
//    private lateinit var exoPlayer: ExoPlayer
private var exoPlayer: ExoPlayer? = null
    private lateinit var exoPlayerListener: ExoPlayerListener

    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private var webView: VideoEnabledWebView? = null
    private val webChromeClient: VideoEnabledWebChromeClient? = null
    private var quality ="720p"
    private var videoBaseUrl ="https://ott-upload-video.s3.ap-south-1.amazonaws.com/"

    private val adSize: AdSize
        get() {
            val display = requireActivity().windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.containerAdd.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireContext(),
                adWidth
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPlayBinding.inflate(inflater, container, false)
        val root: View = binding.root
        appDatabase = AppDatabase.getDatabase(requireContext())
        MobileAds.initialize(requireContext()) {

        }
        exoPlayerListener = ExoPlayerListener()

        binding.containerAdd.visibility = View.VISIBLE
        adView = AdView(requireContext())
        binding.layoutViewView.visibility = View.VISIBLE

        binding.containerAdd.addView(adView)
        binding.crossImg.setOnClickListener {
            binding.videoView.start()
            binding.layoutAdd.visibility = View.GONE



        }
        binding.webLayout.setOnClickListener {
            binding.fullscreen.visibility =View.GONE
//            hideBackButton()
        }
//        hideBackButton()

        binding.fullscreen.setOnClickListener {
            findNavController().popBackStack()
        }
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        val seeting = binding.ecxo.findViewById<ImageButton>(R.id.exo_ff)
        seeting.setOnClickListener {
            chooseQualityAlertBox()
        }

        exoPlayer?.let {
            binding.ecxo.player = exoPlayer

            it.addListener(exoPlayerListener)
            val mediaSource = buildMediaSource("${videoBaseUrl}${quality}/${arguments?.getString("videoUrl").toString()}","")
            it.setMediaSource(mediaSource)
            it.playWhenReady = playPauseState
            it.seekTo(currentWindow, playbackPosition)
            it.prepare()
            it.play()
            binding.lottieMain.visibility = View.GONE

        }
        binding.fullscreen.setOnClickListener {
            activity!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        }
        binding.layoutAdd.visibility = View.GONE
        binding.relativeLayout.visibility =View.VISIBLE
        binding.progressBar.visibility =View.VISIBLE

        val controller = StandardVideoController(requireContext())
        binding.lottieMain.visibility = View.VISIBLE
        controller.setEnableOrientation(true)
        prepareView = PrepareView(requireContext())
        prepareView.setClickStart()
        val thumb = prepareView.findViewById<ImageView>(R.id.thumb)
//        Glide.with(this).load(arguments?.getString("thumb").toString()).into(thumb)
       // val proxyUrl = proxy.getProxyUrl("https://ottsbucket.s3.ap-south-1.amazonaws.com/1659957511562-Assassin's+Creed+Unity+E3+2014+World+Premiere+Cinematic+Trailer+%5BEUROPE%5D.mp4")
     //   val proxyUrl = proxy.getProxyUrl(arguments?.getString("videoUrl").toString())

//        binding.videoView.setUrl(arguments?.getString("videoUrl").toString())
//        binding.videoView.setVideoUri(Uri.parse(arguments?.getString("videoUrl").toString()))

//        binding.videoView.start()

        binding.lottieMain.visibility =View.GONE

        controller.addControlComponent(prepareView)
        controller.addControlComponent(CompleteView(requireContext()))
        controller.addControlComponent(ErrorView(requireContext()))
        val titleView = TitleView(requireContext())
        controller.addControlComponent(titleView)

        controller.setEnableOrientation(true)
        prepareView = PrepareView(requireContext())
        prepareView.setClickStart()

        controller.addControlComponent(prepareView)
        videoId = arguments?.getString("id").toString()
//        val webview = binding.webview as WebView
//        webview.webViewClient = WebViewClient()
//        webview.settings.javaScriptEnabled = true
//        webview.settings.javaScriptCanOpenWindowsAutomatically = true
//        webview.settings.pluginState = WebSettings.PluginState.ON
//        webview.settings.mediaPlaybackRequiresUserGesture = true
//        webview!!.settings.useWideViewPort = true
//
//        webview.webChromeClient = WebChromeClient()
//        webview.setPadding(0, 0, 0, 0);

     //   webview.loadUrl(arguments?.getString("videoUrl").toString())

        val isLive = false
        if (isLive) {
            controller.addControlComponent(LiveControlView(requireContext()))
        } else {
            val vodControlView = VodControlView(requireContext())

            controller.addControlComponent(vodControlView)
        }
        val gestureControlView = GestureView(requireContext())
        controller.addControlComponent(gestureControlView)
        controller.setCanChangePosition(!isLive)
       binding.lottieMain.visibility =View.GONE
        if (arguments?.getString("desc").toString().isNotEmpty()) {

        }

        binding.tvExploreMore.text = arguments?.getString("title").toString()
        binding.tvView.text = arguments?.getString("view").toString()+"View"
        if (arguments?.getString("desc").toString().isNotEmpty()) {
            binding.tvDesc.text = arguments?.getString("desc").toString()

        } else {
            binding.tvDesc.text = ""

        }
        url = arguments?.getString("videoUrl").toString()
            binding.shareLink.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KalaSh")

            shareIntent.putExtra(Intent.EXTRA_TEXT, "${Constants.WEB_URL}")
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }

        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        latestAdapter = FunAdapter(object : FunAdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<VideoData_>, position: Int, type: String) {
                if (type == "") {
                    binding.videoView.pause()
                    binding.videoView.release()
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("desc", data[position].description)
                    bundle.putString("view", data[position].views.toString())

                    bundle.putString("id", data[position].id)
                    binding.tvExploreMore.text = data[position].title
                    binding.tvDesc.text = data[position].description
                    videoId = data[position].id

//                    Glide.with(requireContext()).load(data[position].thumbnail).into(thumb)
//                    val proxy: HttpProxyCacheServer = getProxy(requireContext())
//
//                    val proxyUrl = proxy.getProxyUrl(data[position].url)
//                    binding.videoView.setVideoUri(Uri.parse(proxyUrl))
//                    binding.videoView.start()
//                    webview.loadUrl(proxyUrl)
                    exoPlayer!!.pause()
//                    exoPlayer!!.release()
                    val mediaSource = buildMediaSource("${videoBaseUrl}${quality}/${data[position].url}","")
                    exoPlayer!!.setMediaSource(mediaSource)
                    exoPlayer!!.play()
//                    exoPlayer?.let {
//                        binding.ecxo.player = exoPlayer
//
//                        it.addListener(exoPlayerListener)
//                        val mediaSource = buildMediaSource(data[position].url,"")
//                        it.setMediaSource(mediaSource)
//                        it.playWhenReady = playPauseState
//                        it.seekTo(currentWindow, playbackPosition)
//                        it.prepare()
//                        it.play()
//                    }

                    latestAdapter.setData(latestVideo!!.data.filter { videoId != it.id } as ArrayList<VideoData_>)

                    // findNavController().navigate(R.id.videoPlayFragment, bundle)

                } else if (type == "saved") {
                    appDatabase.getContactDao().insert(
                        ContactEntity(
                            data[position].id,
                            data[position].url,
                            data[position].thumbnail,
                            data[position].title,
                            data[position].description,
                            data[position].duration
                        )
                    )
                    Toast.makeText(requireContext(), "Video saved successfully", Toast.LENGTH_LONG)
                        .show()
                } else {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KalaSh")

                        shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "${Constants.WEB_URL}${data[position].id}"
                        )
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }

            }

        })

        dashboardViewModel.hitApi()
        dashboardViewModel.getLatestVideo()
        val uiModeManager = activity?.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            Log.d(ContentValues.TAG, "Running on a TV Device")

            val numberOfColumns = 4
            binding.rvExploereMore.layoutManager = GridLayoutManager(
                requireContext(),
                numberOfColumns
            )


        } else {
            binding.rvExploereMore.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            Log.d(ContentValues.TAG, "Running on a non-TV Device")
        }
        observer()

//        61
//

        Handler(Looper.getMainLooper()).postDelayed({
            videoVieCount(arguments?.getString("id").toString())
        }, 10000)


        binding.progressBar.visibility=View.GONE



        return root
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.pause()
        binding.videoView.release()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer!!.play()
    }

    private val mOnStateChangeListener: VideoView.OnStateChangeListener =
        object : VideoView.SimpleOnStateChangeListener() {
            override fun onPlayerStateChanged(playerState: Int) {
                when (playerState) {
                    VideoView.PLAYER_NORMAL -> {
                    }
                    VideoView.PLAYER_FULL_SCREEN -> {
                    }
                }
            }

            override fun onPlayStateChanged(playState: Int) {

                when (playState) {
                    VideoView.STATE_IDLE -> {
                    }
                    VideoView.STATE_PREPARING -> {
                        binding.lottieMain.visibility = View.VISIBLE

                    }
                    VideoView.STATE_PREPARED -> {
                    }
                    VideoView.STATE_PLAYING -> {

                       // val videoSize = binding.videoView.videoSize
                        binding.lottieMain.visibility = View.GONE


                    }
                    VideoView.STATE_PAUSED -> {
                    }
                    VideoView.STATE_BUFFERING -> {
                    }
                    VideoView.STATE_BUFFERED -> {
                    }
                    VideoView.STATE_PLAYBACK_COMPLETED -> {
                    }
                    VideoView.STATE_ERROR -> {
                    }
                }
            }
        }

    fun observer() {

        dashboardViewModel.latestVideoResponse_.observe(viewLifecycleOwner) {
            if (it.data.isNotEmpty()) {
                latestVideo =
                    it /* = java.util.ArrayList<com.dd.api.baseModel.VideosResponseModel> *//* = java.util.ArrayList<com.dd.api.baseModel.VideosResponseModel> */
                latestAdapter.setData(it.data.filter { arguments?.getString("id") != it.id } as ArrayList<VideoData_>)
                binding.rvExploereMore.adapter = latestAdapter
            }
            toggleLoader(requireContext(), false)


        }
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
        exoPlayer!!.pause()
    }


    fun showAdd() {
        val adRequest = AdRequest.Builder().build()
        adRequest.isTestDevice(requireContext())
        //  ca-app-pub-3940256099942544/5224354917
        //  ca-app-pub-6915285524914614/4254105391
        adView.adUnitId = "ca-app-pub-3940256099942544/5224354917"

        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        adView.loadAd(adRequest)
//        RewardedAd.load(
//            requireContext(),
//            "ca-app-pub-3940256099942544/5224354917",
//            adRequest,
//            object : RewardedAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    Log.d(TAG, adError.toString())
//                    mRewardedAd = null
//                }
//
//                override fun onAdLoaded(rewardedAd: RewardedAd) {
//                    Log.d(TAG, "Ad was loaded.")
//                    mRewardedAd = rewardedAd
//                }
//            })
//
//        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//            override fun onAdClicked() {
//                // Called when a click is recorded for an ad.
//                Log.d(TAG, "Ad was clicked.")
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                // Set the ad reference to null so you don't show the ad a second time.
//                Log.d(TAG, "Ad dismissed fullscreen content.")
//                mRewardedAd = null
//            }
//
//
//            override fun onAdImpression() {
//                // Called when an impression is recorded for an ad.
//                Log.d(TAG, "Ad recorded an impression.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.d(TAG, "Ad showed fullscreen content.")
//            }
//        }
//
//        if (mRewardedAd != null) {
//            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener {
//                fun onUserEarnedReward(rewardItem: RewardItem) {
//                    var rewardAmount = rewardItem.amount
//                    var rewardType = rewardItem.type
//                    Log.d(TAG, "User earned the reward.")
//                }
//            })
//        } else {
//            Log.d(TAG, "The rewarded ad wasn't ready yet.")
//        }


        adView.adListener =
            object : AdListener() {
                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Code to be executed when an ad request fails.
                }

                override fun onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                override fun onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            }


    }

    private fun createTimer(time: Long) {
        mCountDownTimer?.cancel()

        mCountDownTimer = object : CountDownTimer(time * 1000, 50) {
            override fun onTick(millisUnitFinished: Long) {
                mTimeRemaining = millisUnitFinished / 1000 + 1
                binding.timer.text = "Video will show in: $mTimeRemaining"
            }

            override fun onFinish() {
                binding.layoutAdd.visibility = View.GONE
                binding.videoView.start()
                binding.timer.text = "The game has ended!"
                mGameOver = true
            }
        }

        mCountDownTimer?.start()
    }



    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        Toast.makeText(requireContext(),"hi2",Toast.LENGTH_LONG).show()

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            binding.webLayout.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            MainActivity2.binding.appBarMain.toolbar.visibility =View.GONE

//            if (!binding.videoView.isFullScreen) {
//                binding.videoView.startFullScreen()
//                binding.videoView.resume()
//
//
//            }
        } else {
//            Toast.makeText(requireContext(),"hi",Toast.LENGTH_LONG).show()
            isRotate =0

            binding.webLayout.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                400
            )
            MainActivity2.binding.appBarMain.toolbar.visibility =View.VISIBLE

//            if (binding.videoView.isFullScreen) {
//                binding.videoView.stopFullScreen()
//                binding.videoView.resume()
//
//
//            }

        }
    }

    fun openNav() {
        val drawer = MainActivity2.binding.drawerLayout
        drawer.openDrawer(Gravity.LEFT)
    }

    override fun onDetach() {
        super.onDetach()
        binding.videoView.pause()
        binding.videoView.release()
    }

    override fun onStop() {
        super.onStop()
        binding.videoView.pause()
        binding.videoView.release()

    }
    private fun fitSurfaceToView(surfaceView: SurfaceView, width: Int, height: Int) {
        val parent = surfaceView.parent as View
        val oldWidth = parent.width
        val oldHeight = parent.height
        val newWidth: Int
        val newHeight: Int
        val ratio = height.toFloat() / width.toFloat()
        if (oldHeight.toFloat() > oldWidth.toFloat() * ratio) {
            newWidth = oldWidth
            newHeight = (oldWidth.toFloat() * ratio).toInt()
        } else {
            newWidth = (oldHeight.toFloat() / ratio).toInt()
            newHeight = oldHeight
        }
        val layoutParams = surfaceView.layoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        surfaceView.layoutParams = layoutParams
    }

    internal inner class myWebChromeClient : WebChromeClient() {
        private val mDefaultVideoPoster: Bitmap? = null
        private var mVideoProgressView: View? = null
        override fun onShowCustomView(
            view: View,
            requestedOrientation: Int,
            callback: CustomViewCallback
        ) {
            onShowCustomView(
                view,
                callback
            ) //To change body of overridden methods use File | Settings | File Templates.
        }

        override fun onShowCustomView(view: View, callback: CustomViewCallback) {

            // if a view already exists then immediately terminate the new one

        }

        override fun getVideoLoadingProgressView(): View? {
            if (mVideoProgressView == null) {
                val inflater = LayoutInflater.from(requireContext())
//                mVideoProgressView = inflater.inflate(R.layout.video_progress, null)
            }
            return mVideoProgressView
        }

        override fun onHideCustomView() {
            super.onHideCustomView() //To change body of overridden methods use File | Settings | File Templates.

        }
    }

    internal inner class myWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return super.shouldOverrideUrlLoading(
                view,
                url
            ) //To change body of overridden methods use File | Settings | File Templates.
        }
    }
    private fun buildMediaSource(url: String): MediaSource {

        val dataSourceFactory = DefaultHttpDataSource.Factory()

        // smooth stream media source
        return SsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri((url)))
    }
    private fun buildMediaSource(url1: String, url2: String): MediaSource {

        val dataSourceFactory = DefaultHttpDataSource.Factory()

        val mediaSource1 = ProgressiveMediaSource.Factory(dataSourceFactory)

            .createMediaSource(MediaItem.fromUri((url1)))

        val mediaSource2 = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri((url2)))

        // Heterogeneous Playlist
        return ConcatenatingMediaSource(mediaSource1, mediaSource2, mediaSource1, mediaSource2)
    }


    inner class ExoPlayerListener : Player.Listener {
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            exoPlayer?.let {
                val manifest = it.currentManifest
                manifest?.let {
                    val smoothStreamManifest = it as SsManifest
//                    basic_audio_player_with_smooth_streaming_text_view.text =
//                        "durationUs = ${smoothStreamManifest.durationUs} \n dvrWindowLengthUs = ${smoothStreamManifest.dvrWindowLengthUs} \n isLive = ${smoothStreamManifest.isLive} \n lookAheadCount = ${smoothStreamManifest.lookAheadCount} \n majorVersion = ${smoothStreamManifest.majorVersion} \n minorVersion = ${smoothStreamManifest.minorVersion} \n protectionElement = ${smoothStreamManifest.protectionElement} \n streamElements = ${smoothStreamManifest.streamElements}"
                }
            }
        }


    }

    fun hideBackButton(){
        Handler(Looper.getMainLooper()).postDelayed({
          binding.fullscreen.visibility =View.GONE
        }, 3000)
    }

    fun chooseQualityAlertBox(){

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        var alert_dialog: androidx.appcompat.app.AlertDialog? = null
        val layoutInflater = layoutInflater
        val customView = layoutInflater.inflate(R.layout.quality_layout, null)
        val tvalertMessage = customView.findViewById<TextView>(R.id.tvTittle)
        val lowButton = customView.findViewById<Button>(R.id.low)
        val highButton = customView.findViewById<Button>(R.id.high)
        val mediumButton = customView.findViewById<Button>(R.id.medium)
        lowButton.setOnClickListener {
            exoPlayer!!.pause()
            quality = "320p"
            alert_dialog!!.dismiss()
            exoPlayer!!.play()

        }
        highButton.setOnClickListener {
            quality = "1080p"
            alert_dialog!!.dismiss()
        }
        mediumButton.setOnClickListener {
            quality = "720p"
            alert_dialog!!.dismiss()
        }
        tvalertMessage.text = "Select Quality"
        builder.setView(customView)
        alert_dialog = builder.create()
        alert_dialog.setCancelable(true)



        alert_dialog.show()


    }

//    fun chooseQualityAlertBox() {
//        val contextThemeWrapper = ContextThemeWrapper(context, Gravity.CENTER_VERTICAL)
//        val popup = PopupMenu(contextThemeWrapper, binding.fullscreen)
//        popup.inflate(R.menu.recycle_menu)
//        popup.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.low -> {
//                quality = "320p"
//
//
//                    true
//                }
//                R.id.medium -> {
//                    quality = "720p"
//
//                    true
//                }
//                R.id.high -> {
//                    quality = "1080p"
//
//
//                true
//                   }
//                else -> {
//                    true
//                }
//            }
//
//        }
//        //displaying the popup
//        popup.show()
//    }
   fun videoVieCount(videoId:String){
       WebServiceRequests.webServiceRequests.apiService.videoViewCount(videoId)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({},{})
   }

}

