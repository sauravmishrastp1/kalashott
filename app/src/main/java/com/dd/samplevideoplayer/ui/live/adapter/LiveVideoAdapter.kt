package com.dd.samplevideoplayer.ui.live.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dd.api.baseModel.Data_
import com.dd.api.baseModel.LiveVideoResponseData
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.LayoutLatestBinding
import com.dd.samplevideoplayer.databinding.LayoutLiveVideoBinding

class LiveVideoAdapter(val latestVideoCallBack: LiveVideoAdapterCallBack) : RecyclerView.Adapter<LiveVideoAdapter.ViewHolder>() {
    private var selectedPosition: Int =-1
    private lateinit var data: ArrayList<LiveVideoResponseData>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            LayoutLiveVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position)
    }

    fun setData(data: ArrayList<LiveVideoResponseData>) {
        let {
            it.data = data
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: LayoutLiveVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(position: Int) {
            Glide.with(binding.imgThumbNail.context).load(data[position].thumbnail)
                .into(binding.imgThumbNail)
            binding.tvTittle.text = data[position].title
            binding.tvNext.setOnClickListener {
                if(selectedPosition!=position){
                    selectedPosition = position
                    notifyDataSetChanged()
                }
                latestVideoCallBack.getData(data,position,"")

            }
            binding.shareLink.setOnClickListener {
                latestVideoCallBack.getData(data,position,"s")

            }

            if(selectedPosition==position) {
                binding.cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.red)
            }else{
                binding.cardView.setStrokeColor(ContextCompat.getColor(itemView.context, R.color.black))

            }
            binding.cardView.setOnClickListener {
                if(selectedPosition!=position){
                    selectedPosition = position
                    notifyDataSetChanged()
                }

            }


        }
    }

    interface LiveVideoAdapterCallBack{
        fun getData(data: ArrayList<LiveVideoResponseData>, position: Int,type:String)
    }
}