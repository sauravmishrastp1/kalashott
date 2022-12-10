package com.dd.samplevideoplayer.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.FragmentAboutBinding
import com.dd.samplevideoplayer.databinding.FragmentHomeBinding
import com.dd.samplevideoplayer.ui.home.HomeViewModel


class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var aboutVm: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        aboutVm = ViewModelProvider(this).get(AboutViewModel::class.java)
        aboutVm.hitApi()
        aboutVm.aboutLive.observe(viewLifecycleOwner){
            Glide.with(requireContext()).load(it.persons[0].img).into(binding.aboutImg)
            binding.name.text = it.persons[0].name
            binding.tittle.text = it.persons[0].title
            binding.content.text = it.description

            Glide.with(requireContext()).load(it.persons[1].img).into(binding.aboutImg2)
            binding.name2.text = it.persons[1].name
            binding.tittle2.text = it.persons[1].title
        }

        return _binding!!.root
    }

}