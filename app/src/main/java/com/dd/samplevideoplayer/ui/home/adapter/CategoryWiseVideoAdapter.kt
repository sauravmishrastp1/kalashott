package com.dd.samplevideoplayer.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.*
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.LayoutCategoryBinding
import com.dd.samplevideoplayer.databinding.LayoutLatestBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CategoryWiseVideoAdapter(val latestVideoCallBack: LatestVideoCallBack) : RecyclerView.Adapter<CategoryWiseVideoAdapter.ViewHolder>() {
    private  var data= ArrayList<CatWiseVideoResponseData>()
    private var selectedPosition = -1
    private lateinit var adapter: LatestAdapter
    companion object{
        var callBack:LatestVideoCallBack2?=null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            LayoutLatestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setData(position)
    }

    fun setData(data: ArrayList<CatWiseVideoResponseData>) {
        this.data = data
        notifyItemChanged(data.size-1)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: LayoutLatestBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(position: Int) {
            Glide.with(binding.imgThumbNail.context).load(data[position].thumbnail)
                .into(binding.imgThumbNail)
            binding.tvTittle.text = data[position].title
            var tsec =data[position].duration.split(".")[0]
            var  hours = tsec.toInt() / 3600
            var  minutes = (tsec.toInt() % 3600) / 60
            var   seconds = tsec.toInt() % 60
            var   timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            binding.tvVideoDuration.text = "${data[position].views} View| ${timeString}"
            binding.tvNext.setOnClickListener {
                if(selectedPosition!=position){
                    selectedPosition = position
                    notifyDataSetChanged()
                }
                callBack?.getData(data,position,"")
            }

            binding.shareLink.setOnClickListener {
                callBack?.getData(data,position,"s")

            }

            binding.saveVideo.setOnClickListener {
                callBack?.getData(data,position,"saved")

            }

            if(selectedPosition==position) {
                binding.cardView.setStrokeColor(ContextCompat.getColor(itemView.context, R.color.black))
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
        fun getData(data: ArrayList<CatWiseVideoResponseData>, position: Int,type:String)
    }


}

interface LatestVideoCallBack2{
    fun getData(data: ArrayList<CatWiseVideoResponseData>, position: Int,type:String)
}