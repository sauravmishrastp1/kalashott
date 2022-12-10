package com.dd.samplevideoplayer.ui.search

import android.app.UiModeManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.db.AppDatabase
import com.db.ContactEntity
import com.dd.api.Constants
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.SearchResponseData
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentSearchListBinding
import com.dd.samplevideoplayer.databinding.LayoutBannerBinding
import com.dd.samplevideoplayer.ui.live.NotificationsViewModel
import com.dd.samplevideoplayer.ui.search.adapter.Searchdapter


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchListBinding? = null


    private val binding get() = _binding!!
    private lateinit var liveVideoAdapter: Searchdapter
    private lateinit var appDatabase: AppDatabase

    private lateinit var notificationsViewModel: NotificationsViewModel

    private lateinit var adapterLayoutBannerBinding: LayoutBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        appDatabase = AppDatabase.getDatabase(requireContext())



        liveVideoAdapter = Searchdapter(object : Searchdapter.LatestVideoCallBack {
            override fun getData(data: ArrayList<SearchResponseData>, position: Int, type: String) {
                if (type == "") {
                    val bundle = Bundle()
                    bundle.putString("videoUrl", data[position].url)
                    bundle.putString("title", data[position].title)
                    bundle.putString("thumb", data[position].thumbnail)
                    bundle.putString("id", data[position].id)
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
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.progressBaR.visibility = View.VISIBLE
                notificationsViewModel.search(p0.toString())

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.searchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                notificationsViewModel.search(binding.searchEt.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        notificationsViewModel.search("a")

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

//        BannerAdapter.binding!!.videoView.pause()
//        BannerAdapter.binding!!.videoView.release()

        return root

    }

    private fun observer() {
        binding.progressBaR.visibility = View.VISIBLE

        notificationsViewModel.searchRequests_.observe(viewLifecycleOwner) {
            if (it.data.isEmpty()) {
                Toast.makeText(requireContext(), "Search result not found", Toast.LENGTH_LONG)
                    .show()

            } else {
                liveVideoAdapter.setData(it.data as ArrayList<SearchResponseData>)
                binding.rvGetAllLiveVideo.adapter = liveVideoAdapter
                binding.progressBaR.visibility = View.GONE

            }

        }
        notificationsViewModel.error.observe(viewLifecycleOwner) {
            binding.progressBaR.visibility = View.GONE
            Toast.makeText(requireContext(), "Search result not found", Toast.LENGTH_LONG).show()


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