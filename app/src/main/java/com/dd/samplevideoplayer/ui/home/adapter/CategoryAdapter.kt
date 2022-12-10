package com.dd.samplevideoplayer.ui.home.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dd.api.WebServiceRequests
import com.dd.api.baseModel.*
import com.dd.samplevideoplayer.R
import com.dd.samplevideoplayer.databinding.LayoutCategoryBinding
import com.dd.toggleLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CategoryAdapter(val latestVideoCallBack: LatestVideoCallBack,val navController: NavController) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var data=ArrayList<CategoryResponseModelData>()
    private var selectedPosition = -1
    private lateinit var adapter: CategoryWiseVideoAdapter

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            LayoutCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapter = CategoryWiseVideoAdapter(object :CategoryWiseVideoAdapter.LatestVideoCallBack{
            override fun getData(
                data: ArrayList<CatWiseVideoResponseData>,
                position: Int,
                type: String
            ) {

            }

        })
        holder.setData(position)
    }

    fun setData(data: ArrayList<CategoryResponseModelData>) {
        this.data = data
        notifyItemChanged(data.size-1)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: LayoutCategoryBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setData(position: Int) {

            binding.tvHorror.text =data[position].categoryName
             var p= ArrayList<Int>()
            p.add(position)
            getCateWiseVideo(data[position].id,binding.rvHorrorList,adapter,binding.tvHorror,binding.tvHorrorViewAll,position,data)
             binding.tvHorrorViewAll.setOnClickListener {
                 val bundel = Bundle()
                 bundel.putString("id",data[position].id)
                 bundel.putString("title",data[position].categoryName)
                 navController.navigate(R.id.funVideoFragment,bundel)

             }

        }


    }

    interface LatestVideoCallBack{
        fun getData(data: ArrayList<CategoryResponseModelData>, position:Int,type:String)
    }


    fun getCateWiseVideo(
        vid: String,
        recyclerview: RecyclerView,
        adapter: CategoryWiseVideoAdapter,
        textView: TextView,
        textView2: TextView,
        position: Int,
        data: ArrayList<CategoryResponseModelData>
    ){

        textView.visibility=View.GONE
        textView2.visibility=View.GONE
        recyclerview.visibility=View.GONE
        WebServiceRequests.webServiceRequests.getFriendRequest(vid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :DisposableObserver<CatWiseVideoResponse>(){
                override fun onNext(t: CatWiseVideoResponse) {
                    if(t.data.isEmpty()){
                        textView.visibility=View.GONE
                        textView2.visibility=View.GONE
                        recyclerview.visibility=View.GONE
                    }else{
                        recyclerview.visibility=View.VISIBLE
                        adapter.setData(t.data as ArrayList<CatWiseVideoResponseData>)
                        recyclerview.adapter = adapter
                        textView.visibility=View.VISIBLE
                        textView2.visibility=View.VISIBLE
                    }
                  

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

            })
    }

}