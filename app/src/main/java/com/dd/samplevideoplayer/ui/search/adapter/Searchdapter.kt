package com.dd.samplevideoplayer.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.db.ContactEntity
import com.dd.api.baseModel.Data_
import com.dd.api.baseModel.SearchResponseData
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.LayoutFunHorrorVideoBinding
import com.dd.samplevideoplayer.databinding.LayoutLatestBinding

class Searchdapter(val latestVideoCallBack: LatestVideoCallBack) : RecyclerView.Adapter<Searchdapter.ViewHolder>() {
    private lateinit var data: ArrayList<SearchResponseData>
    private var selectedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            LayoutFunHorrorVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position)
    }

    fun setData(data: ArrayList<SearchResponseData>) {
        let {
            it.data = data
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: LayoutFunHorrorVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun setData(position: Int) {
            Glide.with(binding.imgThumbNail.context).load(data[position].thumbnail)
                .into(binding.imgThumbNail)
            binding.tvTittle.text = data[position].title
            var tsec =data[position].duration.split(".")[0]
            var  hours = tsec.toInt() / 3600
            var  minutes = (tsec.toInt() % 3600) / 60;
            var   seconds = tsec.toInt() % 60;
            var   timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            binding.tvVideoDuration.text = "${data[position].views} VIew|${timeString}"
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

            binding.saveVideo.setOnClickListener {
                latestVideoCallBack.getData(data,position,"saved")

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

    interface LatestVideoCallBack{
        fun getData(data: ArrayList<SearchResponseData>, position: Int,type:String)
    }
}