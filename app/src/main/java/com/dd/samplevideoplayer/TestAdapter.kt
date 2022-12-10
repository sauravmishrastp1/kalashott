package com.dd.samplevideoplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dd.samplevideoplayer.databinding.LayoutCategoryTitleBinding

class TestAdapter : RecyclerView.Adapter<TestAdapter.MyViewHolder>() {
    private var arrayList = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            LayoutCategoryTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position)
    }

    fun setData(arrayList: ArrayList<String>) {
        let {
            it.arrayList = arrayList
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class MyViewHolder(val layoutCategoryBinding: LayoutCategoryTitleBinding) :
        RecyclerView.ViewHolder(layoutCategoryBinding.root) {
        fun setData(position: Int) {
            layoutCategoryBinding.rvCategoryTitle.text = arrayList[position]

        }
    }
}


