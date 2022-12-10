package com.dd.samplevideoplayer.ui.myList

import android.app.UiModeManager
import android.content.ContentValues
import android.content.Context
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
import com.db.AppDatabase
import com.db.ContactEntity
import com.dd.api.Constants
import com.dd.api.WebServiceRequests
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentMyListBinding
import com.dd.samplevideoplayer.ui.live.NotificationsViewModel
import com.dd.samplevideoplayer.ui.myList.adapter.MyListdapter
import com.dd.toggleLoader

class MyListFragment : Fragment() {
    private var _binding: FragmentMyListBinding? = null


    private val binding get() = _binding!!
    private lateinit var liveVideoAdapter: MyListdapter
    private lateinit var appDatabase: AppDatabase

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        appDatabase = AppDatabase.getDatabase(requireContext())

        liveVideoAdapter = MyListdapter(object : MyListdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<ContactEntity>, position: Int, type: String) {
                if (type == "") {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("id", data[position].id.toString())
                    bundle.putString("desc", data[position].desc)
                    findNavController().navigate(R.id.videoPlayFragment, bundle)

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
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        notificationsViewModel.hitApi(WebServiceRequests())
        observer()
        val uiModeManager = activity?.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            Log.d(ContentValues.TAG, "Running on a TV Device")
            val numberOfColumns = 4
            binding.rvGetAllLiveVideo.layoutManager = GridLayoutManager(
                requireContext(),
                numberOfColumns
            )

        } else {
            binding.rvGetAllLiveVideo.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            Log.d(ContentValues.TAG, "Running on a non-TV Device")
        }
        return root

    }

    private fun observer() {

        toggleLoader(requireContext(), true)
        liveVideoAdapter.setData(
            appDatabase.getContactDao().getAllContact() as ArrayList<ContactEntity>
        )
        binding.rvGetAllLiveVideo.adapter = liveVideoAdapter
        toggleLoader(requireContext(), false)

        notificationsViewModel.response_.observe(viewLifecycleOwner) {


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