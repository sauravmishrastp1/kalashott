package com.dd.samplevideoplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dd.api.baseModel.VideoData_
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentHorrorVideoBinding
import com.dd.samplevideoplayer.ui.dashboard.DashboardViewModel
import com.dd.samplevideoplayer.ui.home.adapter.FunAdapter
import com.dd.toggleLoader


class HorrorVideoFragment : Fragment() {

    private var _binding: FragmentHorrorVideoBinding? = null
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var latestAdapter: FunAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHorrorVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        latestAdapter = FunAdapter(object : FunAdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<VideoData_>, position: Int, type: String) {
                if (type == "") {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    findNavController().navigate(R.id.videoPlayFragment, bundle)

                } else {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KalaSh")

                        shareIntent.putExtra(Intent.EXTRA_TEXT, data[position].url)
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }

            }

        })

        dashboardViewModel.hitApi()
        dashboardViewModel.getLatestVideo()
        observer()
        // Inflate the layout for this fragment
        return root
    }

    fun observer() {

        dashboardViewModel.latestVideoResponse_.observe(viewLifecycleOwner) {
            latestAdapter.setData(it.data as ArrayList<VideoData_>)
            binding.rvhorrorList.adapter = latestAdapter
            toggleLoader(requireContext(), false)

        }
    }

}