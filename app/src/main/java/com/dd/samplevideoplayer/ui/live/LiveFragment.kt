package com.dd.samplevideoplayer.ui.live

import android.app.UiModeManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.UI_MODE_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd.api.Constants
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.LiveVideoResponseData
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentNotificationsBinding
import com.dd.samplevideoplayer.ui.live.adapter.LiveVideoAdapter
import com.dd.toggleLoader

class LiveFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var liveVideoAdapter: LiveVideoAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var notificationsViewModel: NotificationsViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        liveVideoAdapter = LiveVideoAdapter(object : LiveVideoAdapter.LiveVideoAdapterCallBack {
            override fun getData(
                data: ArrayList<LiveVideoResponseData>,
                position: Int,
                type: String
            ) {
                if (type == "") {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("thumb", data[position].thumbnail)
                    findNavController().navigate(R.id.videoPlayFragment, bundle)

                } else {

                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KalaSh")

                        shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "${Constants.LIVE_URL}${data[position].id}"
                        )
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }
            }

        })
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        notificationsViewModel.hitApi(WebServiceRequests())
        observer()

        val uiModeManager = activity?.getSystemService(UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            Log.d(TAG, "Running on a TV Device")
            val numberOfColumns = 4
            binding.rvGetAllLiveVideo.layoutManager = GridLayoutManager(
                requireContext(),
                numberOfColumns
            )

        } else {
            binding.rvGetAllLiveVideo.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            Log.d(TAG, "Running on a non-TV Device")
        }
//        if(getScreenResolution(requireContext())!! <1000){
//
//        }else{

        //}
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun observer() {
        toggleLoader(requireContext(), true)
        notificationsViewModel.response_.observe(viewLifecycleOwner) {
            liveVideoAdapter.setData(it.data as ArrayList<LiveVideoResponseData>)
            binding.rvGetAllLiveVideo.adapter = liveVideoAdapter
            toggleLoader(requireContext(), false)

        }

    }

    private fun getScreenResolution(context: Context): Int? {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        return width
    }

}