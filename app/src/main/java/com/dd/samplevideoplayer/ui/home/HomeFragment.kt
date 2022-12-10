package com.dd.samplevideoplayer.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.StateStatus
//import com.danikula.videocache.HttpProxyCacheServer
import com.db.AppDatabase
import com.db.ContactEntity
import com.db.StopPlayer
import com.dd.api.App
import com.dd.api.Constants
import com.dd.api.baseModel.Data_
import com.dd.api.baseModel.VideoData_
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentHomeBinding
import com.dd.samplevideoplayer.databinding.LayoutBannerBinding
import com.dd.samplevideoplayer.ui.VideoPlayerActivity
import com.dd.samplevideoplayer.ui.home.adapter.BannerAdapter
import com.dd.samplevideoplayer.ui.home.adapter.BannerAdapter.Companion.playerStopListner
import com.dd.samplevideoplayer.ui.home.adapter.LatestAdapter
import com.dd.toggleLoader
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*
import xyz.doikki.videoplayer.player.VideoView


class HomeFragment : Fragment(), SliderView.OnSliderPageListener, StopPlayer {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var latestAdapter: LatestAdapter
    private lateinit var trendingTitleAdapter: LatestAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapterLayoutBannerBinding: LayoutBannerBinding
    lateinit var prepareView: PrepareView


    //    lateinit var prepareView: PrepareView
    private var bannerList = ArrayList<Data_>()
    private var p = 0


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playerStopListner = this

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getDatabase(requireContext())
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        val proxy: HttpProxyCacheServer = App.getProxy(requireContext())

            val controller = StandardVideoController(requireContext())
            controller.setEnableOrientation(true)
            prepareView = PrepareView(requireContext())
            prepareView.setClickStart()

            controller.addControlComponent(prepareView)
            controller.addControlComponent(CompleteView(requireContext()))
           // controller.addControlComponent(ErrorView(requireContext()))
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
        binding.videoView.setVideoController(controller)

        binding.videoView.addOnStateChangeListener(mOnStateChangeListener)
        prepareView.isSoundEffectsEnabled = false
        bannerAdapter = BannerAdapter(object : BannerAdapter.BannerAdapterCallBack {
            override fun sendData(
                data: ArrayList<Data_>,
                position: Int,
                type: String,
                binding: LayoutBannerBinding
            ) {


                if (type.isEmpty()) {

//                    val proxyUrl = proxy.getProxyUrl(data[position].url)
                    val bundle = Bundle()
                    bundle.putString("videoUrl","https://ott-upload-video.s3.ap-south-1.amazonaws.com/"+data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("desc", data[position].description)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("id", data[position].id)

                    findNavController().navigate(R.id.videoPlayFragment, bundle)
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
//                    try {
//                        val shareIntent = Intent(Intent.ACTION_SEND)
//                        shareIntent.type = "text/plain"
//                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KalaSh")
//
//                        shareIntent.putExtra(
//                            Intent.EXTRA_TEXT,
//                            "${Constants.WEB_URL}${data[position].id}"
//                        )
//                        startActivity(Intent.createChooser(shareIntent, "choose one"))
//                    } catch (e: Exception) {
//                        //e.toString();
//                    }
                }

            }

        })

//        binding.videoView.pause()
//        binding.videoView.release()
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)

        latestAdapter = LatestAdapter(object : LatestAdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<VideoData_>, position: Int, type: String) {
                if (type == "") {
//                    val proxyUrl = proxy.getProxyUrl(data[position].url)

                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("view", data[position].views.toString())
                    bundle.putString("title", data[position].title)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("desc", data[position].description)
                    bundle.putString("id", data[position].id)
                    findNavController().navigate(R.id.videoPlayFragment, bundle)
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
                            "${Constants.WEB_URL}720p/${data[position].url}"
                        )
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }

            }

        })



        trendingTitleAdapter = LatestAdapter(object : LatestAdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<VideoData_>, position: Int, type: String) {
                if (type == "") {
//                    val proxyUrl = proxy.getProxyUrl(data[position].url)

                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("view", data[position].views.toString())
                    bundle.putString("title", data[position].title)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("desc", data[position].description)
                    findNavController().navigate(R.id.videoPlayFragment, bundle)

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
                            "${Constants.WEB_URL}720p/${data[position].url}"
                        )
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }

            }

        })



        homeViewModel.hitApi()
        homeViewModel.getLatestVideo()
        homeViewModel.getTrendingVideo()
        homeViewModel.getMarkupeText()
        homeViewModel.getLiveStreemBanner()
        observer()
        // Toast.makeText(requireContext(),getScreenResolution(requireContext()).toString(),Toast.LENGTH_LONG).show()
        //  bannerObserver(p)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerAdapter.setState(StateStatus.ONPAUSE.value)

    }


    override fun onStop() {
        super.onStop()
        bannerAdapter.setState(StateStatus.ONPAUSE.value)
//        binding.videoView.pause()
//        binding.videoView.release()

    }

    fun bannerObserver(position: Int) {
        binding.lottieMain2.visibility = View.VISIBLE
        if (bannerList.isNotEmpty()) {
            // startVideoPlayer(_binding!!, bannerList[position].url, bannerList[position].thumbnail)

        }


    }

    fun observer() {
        binding.noLoiveVideo.visibility =View.GONE

        bannerList.clear()
        toggleLoader(requireContext(), true)
        binding.layout.visibility = View.GONE
        homeViewModel.markupeTextRespponse.observe(viewLifecycleOwner) {
            if (it.data.isNotEmpty()) {
                binding.tvVideoDisc.text = it.data[0].text
                binding.tvVideoDisc.isSelected = true
            }

        }
//        homeViewModel.homeResponse_.observe(viewLifecycleOwner) {
//
//
//            if (it.data.isNotEmpty()) {
//                binding.layout.visibility = View.VISIBLE
//                toggleLoader(requireContext(), false)
//
//                bannerAdapter.setData(it.data as ArrayList<Data_>)
//                binding.imageSlider.setSliderAdapter(bannerAdapter)
//
//
//            }
//
//        }
        homeViewModel.latestVideoResponse_.observe(viewLifecycleOwner) {
            binding.layout.visibility = View.VISIBLE
            toggleLoader(requireContext(), false)
            if (it.data.isNotEmpty()) {
                latestAdapter.setData(it.data as ArrayList<VideoData_>)
                binding.rvlatestVideo.adapter = latestAdapter
            }

        }

        homeViewModel.trendingVideoResponse_.observe(viewLifecycleOwner) {
            binding.layout.visibility = View.VISIBLE
            toggleLoader(requireContext(), false)
            if (it.data.isNotEmpty()) {
                trendingTitleAdapter.setData(it.data as ArrayList<VideoData_>)
                binding.rvHorrorList.adapter = trendingTitleAdapter
            }

        }

        homeViewModel.getSreemBannerLive.observe(viewLifecycleOwner){
            if(it.data.isNotEmpty()){
                binding.videoView.setUrl(it.data[0].url)
//                binding.videoView.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")

            }else{
                binding.noLoiveVideo.visibility =View.VISIBLE
                binding.noLoiveVideo.text ="Stream will be live soon ...\n" +
                        "Please stay tune, we will back soon.."
            }
//            Toast.makeText(context,it.data[0].url,Toast.LENGTH_LONG).show()
//            binding.videoView.setUrl("https://ottsbucket.s3.ap-south-1.amazonaws.com/1663943863732-Jubin+Nautiyal%2C+Payal+Dev-+Meethi+Meethi+-+Rashmi+Virag+-+Shanvi+Srivastava+-+Vijay%2CBosco-+Bhushan+K.mp4")
            binding.videoView.start()

        }
    }

    private fun getScreenResolution(context: Context): String? {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        return "{$width,$height}"
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.resume()

    }

    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.pause()
    }

    override fun onDetach() {
        super.onDetach()
        binding.videoView.pause()

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

                    }
                    VideoView.STATE_PREPARED -> {

                    }
                    VideoView.STATE_PLAYING -> {

                        binding.lottieMain2.visibility = View.GONE
                        binding.noLoiveVideo.visibility =View.GONE

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
                        binding.noLoiveVideo.visibility =View.VISIBLE
                        binding.noLoiveVideo.text ="Stream will be live soon ...\n" +
                                "Please stay tune, we will back soon.."


                    }
                }
            }
        }

    //    fun startVideoPlayer(binding: FragmentHomeBinding, videoUrl: String, thumbnail: String) {
//        binding.videoView.pause()
//        binding.videoView.release()
//        if (videoUrl.isNotEmpty()) {

//
//
//            controller.setEnableOrientation(true)
//            prepareView = PrepareView(requireContext())
//            prepareView.setClickStart()
//            controller.addControlComponent(prepareView)
//
//            val isLive = false
//            if (isLive) {
//                controller.addControlComponent(LiveControlView(requireContext()))
//            } else {
//                val vodControlView = VodControlView(requireContext())
//
//                controller.addControlComponent(vodControlView)
//            }
//            val gestureControlView = GestureView(requireContext())
//            controller.addControlComponent(gestureControlView)
//            controller.setCanChangePosition(!isLive)
//
//            binding.videoView.setUrl(videoUrl)
//            binding.videoView.start()
//
//            binding.videoView.setVideoController(controller)
//
//            binding.videoView.addOnStateChangeListener(mOnStateChangeListener)
//        } else {
//            binding.videoView.pause()
//
//        }
//
//
//    }



    override fun onSliderPageChanged(position: Int) {


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!binding.videoView.isFullScreen) {
                binding.videoView.startFullScreen()
                binding.videoView.resume()


            }
        } else {
            if (binding.videoView.isFullScreen) {
                binding.videoView.stopFullScreen()
                binding.videoView.resume()


            }

        }
    }




}