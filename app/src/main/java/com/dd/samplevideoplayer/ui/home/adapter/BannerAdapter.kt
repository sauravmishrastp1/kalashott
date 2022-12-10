package com.dd.samplevideoplayer.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.db.StopPlayer
import com.dd.api.baseModel.Data_
import com.dd.api.baseModel.MarkupeTextRespponseData
import com.dd.samplevideoplayer.databinding.LayoutBannerBinding
import com.smarteist.autoimageslider.SliderViewAdapter

//import xyz.doikki.videocontroller.component.PrepareView

class BannerAdapter(val bannerAdapterCallBack: BannerAdapterCallBack) :
    SliderViewAdapter<BannerAdapter.SliderAdapterVH>() {
    private var selectedPosition: Int = 0

    //    lateinit var prepareView: PrepareView
    private var lifecycleState = 0

    companion object {
        var binding: LayoutBannerBinding? = null
        var playerStopListner: StopPlayer? = null

    }


    private lateinit var data: ArrayList<Data_>
    private var markupeText = ArrayList<MarkupeTextRespponseData>()

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        binding = LayoutBannerBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return SliderAdapterVH(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {
        viewHolder!!.setData(position)
    }

    fun setState(state: Int) {
        this.lifecycleState = state


    }


    fun setData(data: ArrayList<Data_>) {
        let {
            it.data = data
        }
    }


    override fun getCount(): Int {
        return data.size
    }


    inner class SliderAdapterVH(itemView: LayoutBannerBinding?) :
        SliderViewAdapter.ViewHolder(itemView!!.root) {
        fun setData(position: Int) {
            if (data.isNotEmpty()) {

                Glide.with(binding!!.imgThumbNail.context).load(data[position].thumbnail)
                    .into(binding!!.imgThumbNail)
                binding!!.tvTittle.text = data[position].title
                binding!!.tvVideoDuration.text = data[position].description

                var tsec = data[position].duration.split(".")[0]
                var hours = tsec.toInt() / 3600
                var minutes = (tsec.toInt() % 3600) / 60
                var seconds = tsec.toInt() % 60

                var timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding!!.tvDesc.text = "Duration: ${timeString}"

                binding!!.tvNext.setOnClickListener {

                    if (selectedPosition != position) {
                        selectedPosition = position
                        notifyDataSetChanged()
                    }
                    binding!!.imgThumbNail.visibility = View.GONE
                    binding!!.videoLayout.visibility = View.GONE

                    binding!!.PlayVideo.visibility = View.GONE
                    bannerAdapterCallBack.sendData(data, position, "", binding!!)


                }





                binding!!.cardView.setOnClickListener {
                    if (selectedPosition != position) {
                        selectedPosition = position
                        notifyDataSetChanged()
                    }

                }


            } else {
                //  startVideoPlayer(binding!!, "", "")


            }


        }

    }


    interface BannerAdapterCallBack {
        fun sendData(
            data: ArrayList<Data_>,
            position: Int,
            type: String,
            binding: LayoutBannerBinding
        )
    }


//    fun startVideoPlayer(binding: LayoutBannerBinding, videoUrl: String, thumbnail: String) {
//        if (videoUrl.isNotEmpty()) {
//            val controller = StandardVideoController(binding.layoutViewView.context)
//            binding.lottieMain.visibility = View.VISIBLE
//            controller.setEnableOrientation(true)
//            prepareView = PrepareView(binding.layoutViewView.context)
//            prepareView.setClickStart()
//            val thumb = prepareView.findViewById<ImageView>(R.id.thumb)
//            Glide.with(binding.layoutViewView.context).load(thumbnail).into(thumb)
//            controller.addControlComponent(prepareView)
//            controller.addControlComponent(CompleteView(binding.layoutViewView.context))
//            controller.addControlComponent(ErrorView(binding.layoutViewView.context))
//            val titleView = TitleView(binding.layoutViewView.context)
//            controller.addControlComponent(titleView)
//
//
//            controller.setEnableOrientation(true)
//            prepareView = PrepareView(binding.layoutViewView.context)
//            prepareView.setClickStart()
//            controller.addControlComponent(prepareView)
//
//            val isLive = false
//            if (isLive) {
//                controller.addControlComponent(LiveControlView(binding.layoutViewView.context))
//            } else {
//                val vodControlView = VodControlView(binding.layoutViewView.context)
//
//                controller.addControlComponent(vodControlView)
//            }
//            val gestureControlView = GestureView(binding.layoutViewView.context)
//            controller.addControlComponent(gestureControlView)
//            controller.setCanChangePosition(!isLive)
//
//            binding.videoView.setUrl(videoUrl)
//            if (lifecycleState == StateStatus.ONPAUSE.value) {
//                binding.videoView.pause()
//                binding.videoView.release()
//
//            } else {
//                binding.videoView.start()
//
//            }
//
//            binding.videoView.setVideoController(controller)
//
//            binding.videoView.addOnStateChangeListener(mOnStateChangeListener)
//        } else {
//
//            binding.videoView.pause()
//        }
//
//
//    }
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
//                        binding!!.lottieMain.visibility = View.GONE
//                        binding!!.PlayVideo.visibility = View.GONE
//                    }
//                    VideoView.STATE_PLAYING -> {
//                        binding!!.lottieMain.visibility = View.GONE
//                        binding!!.PlayVideo.visibility = View.GONE
//
//
//                    }
//                    VideoView.STATE_PAUSED -> {
//                        binding!!.lottieMain.visibility = View.GONE
//
//                        binding!!.PlayVideo.visibility = View.VISIBLE
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

}
