package com.db

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.LayoutCategoryTitleBinding

class CategoryAdapter_ : RecyclerView.Adapter<CategoryAdapter_.CategoryViewHolder>() {
    private var selectPosition = 0
    private var data = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            LayoutCategoryTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.setData(position)
    }

    fun setData(data: ArrayList<String>) {
        this.data = data
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CategoryViewHolder(val binding: LayoutCategoryTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(position: Int) {
            binding.rvCategoryTitle.text = data[position]

            if (selectPosition == position) {
                binding.rvCategoryTitle.setTextColor(
                    ContextCompat.getColor(
                        binding.rvCategoryTitle.context,
                        R.color.black
                    )
                )
            } else {
                binding.rvCategoryTitle.setTextColor(
                    ContextCompat.getColor(
                        binding.rvCategoryTitle.context,
                        R.color.grey2
                    )
                )
            }
            binding.rvCategoryTitle.setOnClickListener {
                if (selectPosition != position) {
                    selectPosition = position
                }
                notifyDataSetChanged()
            }


        }
    }
}

