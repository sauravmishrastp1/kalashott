package com.dd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dd.samplevideoplayer.TestAdapter
import com.dd.samplevideoplayer.databinding.FragmentTestBinding


class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!
    private val arrayList = ArrayList<String>()
    private lateinit var adapter: TestAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arrayList.add("All")
        arrayList.add("Eyeglasses")
        arrayList.add("Necklace")
        arrayList.add("Bracelet")



        return root
    }


}