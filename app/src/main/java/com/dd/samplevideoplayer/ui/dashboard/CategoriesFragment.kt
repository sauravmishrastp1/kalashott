package com.dd.samplevideoplayer.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.db.AppDatabase
import com.db.ContactEntity
import com.dd.api.Constants
import com.dd.api.baseModel.CatWiseVideoResponseData
import com.dd.api.baseModel.CategoryResponseModelData
import com.dd.api.baseModel.Data_
import com.dd.api.baseModel.VideoData_
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentDashboardBinding
import com.dd.samplevideoplayer.databinding.LayoutBannerBinding
import com.dd.samplevideoplayer.ui.home.adapter.*
import com.dd.toggleLoader
import com.smarteist.autoimageslider.SliderAnimations

//import xyz.doikki.videocontroller.StandardVideoController
//import xyz.doikki.videocontroller.component.*
//import xyz.doikki.videoplayer.player.VideoView

class CategoriesFragment : Fragment(), LatestVideoCallBack2 {

    private lateinit var categoryWiseVideoAdapter: CategoryWiseVideoAdapter
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var latestAdapter: LatestAdapter
    private lateinit var horrorAdapter: LatestAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var adapterLayoutBannerBinding: LayoutBannerBinding
    private var p = 0

    //    lateinit var prepareView: PrepareView
    private var bannerList = ArrayList<Data_>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        appDatabase = AppDatabase.getDatabase(requireContext())
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        CategoryWiseVideoAdapter.callBack = this
        bannerAdapter = BannerAdapter(object : BannerAdapter.BannerAdapterCallBack {
            override fun sendData(
                data: ArrayList<Data_>,
                position: Int,
                type: String,
                binding: LayoutBannerBinding
            ) {
                adapterLayoutBannerBinding = binding

                if (type.isEmpty()) {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("desc", data[position].description)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("id", data[position].id)
                    bundle.putString("view", data[position].views.toString())
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
        latestAdapter = LatestAdapter(object : LatestAdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<VideoData_>, position: Int, type: String) {
                if (type == "") {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("view", data[position].views.toString())
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



        horrorAdapter = LatestAdapter(object : LatestAdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<VideoData_>, position: Int, type: String) {
                if (type == "") {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("view", data[position].views.toString())
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
                            "${Constants.WEB_URL}/720p/${data[position].url}"
                        )
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }

            }

        })
        categoryAdapter = CategoryAdapter(object : CategoryAdapter.LatestVideoCallBack {


            override fun getData(
                data: ArrayList<CategoryResponseModelData>,
                position: Int,
                type: String,
            ) {


            }

        }, findNavController())

        categoryWiseVideoAdapter =
            CategoryWiseVideoAdapter(object : CategoryWiseVideoAdapter.LatestVideoCallBack {
                override fun getData(
                    data: ArrayList<CatWiseVideoResponseData>,
                    position: Int,
                    type: String
                ) {


                }

            })
//        binding.videoView.pause()
//        binding.videoView.release()

        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)

        binding.rvlatestVideo.adapter = categoryAdapter


        dashboardViewModel.hitApi()
        dashboardViewModel.getLatestVideo()
        dashboardViewModel.getCateofry()
        dashboardViewModel.getMarkupeText()
        observer()
        // bannerObserver(p)
//        binding.tvFunViewAll.setOnClickListener {
//            findNavController().navigate(R.id.funVideoFragment)
//        }
//        binding.tvHorrorViewAll.setOnClickListener {
//            findNavController().navigate(R.id.horrorVideoFragment)
//        }

//        _binding!!.imageSlider.sliderPager.addOnPageChangeListener(object :
//            SliderPager.OnPageChangeListener {
//
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                p = position
//                bannerObserver(position)
//
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//        })
//

        return root
    }


    fun bannerObserver(position: Int) {
        // _binding!!.lottieMain2.visibility = View.VISIBLE
        if (bannerList.isNotEmpty()) {
            // startVideoPlayer(_binding!!, bannerList[position].url, bannerList[position].thumbnail)

        }


    }

    fun observer() {
        bannerList.clear()
        binding.layout.visibility = View.GONE
        toggleLoader(requireContext(), true)
        dashboardViewModel.homeResponse_.observe(viewLifecycleOwner) {
            for (i in it.data) {
                bannerList.add(i)
            }
            if (it.data.isNotEmpty()) {
                bannerAdapter.setData(it.data as ArrayList<Data_>)
                binding.imageSlider.setSliderAdapter(bannerAdapter)
                binding.layout.visibility = View.VISIBLE
            }

            toggleLoader(requireContext(), false)


        }
        dashboardViewModel.markupeTextRespponse.observe(viewLifecycleOwner) {
            if (it.data.isNotEmpty()) {
                binding.tvVideoDisc.text = it.data[0].text
                binding.tvVideoDisc.isSelected = true
            }

        }


        dashboardViewModel.latestVideoResponse_.observe(viewLifecycleOwner) {
            if (it.data.isNotEmpty()) {
                horrorAdapter.setData(it.data as ArrayList<VideoData_>)
                binding.rvHorrorList.adapter = horrorAdapter
                binding.layout.visibility = View.VISIBLE
            }
            toggleLoader(requireContext(), false)


        }
        dashboardViewModel.categoryResponseModel_.observe(viewLifecycleOwner) {
            if (it.data.isNotEmpty()) {
                categoryAdapter.setData(it.data as ArrayList<CategoryResponseModelData>)

            }


        }

    }

    override fun getData(data: ArrayList<CatWiseVideoResponseData>, position: Int, type: String) {
        if (type == "") {
            val bundle = Bundle()
            bundle.putString("videoUrl", data[position].url)
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
            Toast.makeText(requireContext(), "Video saved successfully", Toast.LENGTH_LONG).show()
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

//    override fun onPause() {
//        super.onPause()
//        binding.videoView.pause()
//        binding.videoView.release()
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        binding.videoView.resume()
//
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//        binding.videoView.pause()
//        binding.videoView.release()
//
//
//    }
//
//    fun startVideoPlayer(binding: FragmentDashboardBinding, videoUrl: String, thumbnail: String) {
//        binding.videoView.pause()
//        binding.videoView.release()
//        if (videoUrl.isNotEmpty()) {
//            val controller = StandardVideoController(requireContext())
//            controller.setEnableOrientation(true)
//            prepareView = PrepareView(requireContext())
//            prepareView.setClickStart()
//            val thumb = prepareView.findViewById<ImageView>(R.id.thumb)
//            Glide.with(requireContext()).load(thumbnail).into(thumb)
//            controller.addControlComponent(prepareView)
//            controller.addControlComponent(CompleteView(requireContext()))
//            controller.addControlComponent(ErrorView(requireContext()))
//            val titleView = TitleView(requireContext())
//            controller.addControlComponent(titleView)
//            prepareView.isSoundEffectsEnabled = false
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
//
////    override fun onConfigurationChanged(newConfig: Configuration) {
////        super.onConfigurationChanged(newConfig)
////        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
////            if (!binding.videoView.isFullScreen) {
////                binding.videoView.startFullScreen()
////                binding.videoView.resume()
////
////
////            }
////        } else {
////            if (binding.videoView.isFullScreen) {
////                binding.videoView.stopFullScreen()
////                binding.videoView.resume()
////
////
////            }
////
////        }
////    }
//
//    private val mOnStateChangeListener: VideoView.OnStateChangeListener =
//        object : VideoView.SimpleOnStateChangeListener() {
//            override fun onPlayerStateChanged(playerState: Int) {
//                when (playerState) {
//                    VideoView.PLAYER_NORMAL -> {
//                    }
//                    VideoView.PLAYER_FULL_SCREEN -> {
//                    }
//                }
//            }
//
//            override fun onPlayStateChanged(playState: Int) {
//
//                when (playState) {
//                    VideoView.STATE_IDLE -> {
//                    }
//                    VideoView.STATE_PREPARING -> {
//
//                    }
//                    VideoView.STATE_PREPARED -> {
//
//                    }
//                    VideoView.STATE_PLAYING -> {
//                        binding.lottieMain2.visibility = View.GONE
//
//
//                    }
//                    VideoView.STATE_PAUSED -> {
//
//                    }
//                    VideoView.STATE_BUFFERING -> {
//                    }
//                    VideoView.STATE_BUFFERED -> {
//                    }
//                    VideoView.STATE_PLAYBACK_COMPLETED -> {
//                    }
//                    VideoView.STATE_ERROR -> {
//                    }
//                }
//            }
//        }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        binding.videoView.pause()
//        binding.videoView.release()
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        binding.videoView.pause()
//        binding.videoView.release()
//    }
}