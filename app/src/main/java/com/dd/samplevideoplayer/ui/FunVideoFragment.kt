package com.dd.samplevideoplayer.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.db.AppDatabase
import com.db.ContactEntity
import com.dd.api.baseModel.CatWiseVideoResponseData
import com.dd.api.baseModel.Data_
import com.dd.api.baseModel.LiveVideoResponseData
import com.dd.samplevideoplayer.MainActivity
import com.dd.samplevideoplayer.MainActivity2
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentFunVideoBinding
import com.dd.samplevideoplayer.databinding.FragmentVideoPlayBinding
import com.dd.samplevideoplayer.ui.dashboard.DashboardViewModel
import com.dd.samplevideoplayer.ui.home.adapter.CateWiseVideo2Adapter
import com.dd.samplevideoplayer.ui.home.adapter.CategoryWiseVideoAdapter
import com.dd.samplevideoplayer.ui.home.adapter.FunAdapter
import com.dd.samplevideoplayer.ui.home.adapter.LatestAdapter
import com.dd.samplevideoplayer.ui.live.adapter.LiveVideoAdapter
import com.dd.toggleLoader

class FunVideoFragment : Fragment() {
    private var _binding: FragmentFunVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var latestAdapter: CateWiseVideo2Adapter
    private lateinit var appDatabase: AppDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFunVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        appDatabase = AppDatabase.getDatabase(requireContext())
        // Inflate the layout for this fragment
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        latestAdapter = CateWiseVideo2Adapter(object :CateWiseVideo2Adapter.LatestVideoCallBack{
            override fun getData(
                data: ArrayList<CatWiseVideoResponseData>,
                position: Int,
                type: String
            ) {
                if(type==""){
                    val bundle = Bundle()
                    bundle.putString("videoUrl",data[position].url)
                    bundle.putString("title",data[position].title)
                    bundle.putString("desc",data[position].description)
                    findNavController().navigate(R.id.videoPlayFragment,bundle)

                } else if(type=="saved"){
                    appDatabase.getContactDao().insert(ContactEntity(data[position].id,data[position].url,data[position].thumbnail,data[position].title,data[position].description,data[position].duration))
                    Toast.makeText(requireContext(),"Video saved successfully", Toast.LENGTH_LONG).show()
                }else{
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
        dashboardViewModel.getCateWiseVideo(arguments?.getString("id").toString())
        observer()
        return root
    }

    override fun onPause() {
        super.onPause()

    }


    fun observer(){

        dashboardViewModel.catWiseVideo_.observe(viewLifecycleOwner){
            latestAdapter.setData(it.data as ArrayList<CatWiseVideoResponseData>)
            binding.rvfunList.adapter = latestAdapter
            toggleLoader(requireContext(),false)

        }
    }


}