package com.example.jangco

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.school_list_item.view.*


class SchoolRecyclerViewAdapter(private var schoolNameData : MutableSet<String>, var context : Context?) :
    RecyclerView.Adapter<SchoolRecyclerViewAdapter.ItemViewHolder>(){

    private var copyKeysSet: MutableSet<String>? = null

    init {
        //동일한 데이터 copy 추후 필터에서 사용.
        copyKeysSet = schoolNameData.toMutableSet()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.school_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return schoolNameData.size
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = schoolNameData.elementAt(position)
        holder.bind(item)

    }

    // 검색용 필터 메소드.
    fun searchFilter(newText: String?) {

        var str = newText?.trim()?.replace(" ", "")
        schoolNameData.clear()
        if (str?.isEmpty()!! && str?.length ==0) {
            schoolNameData = copyKeysSet?.toMutableSet()!!
            return
        }

        for (item in copyKeysSet!!) {
            if (item.trim()?.replace(" ", "").contains(str)) {
                Log.d("search_filter_contains", "${str}")
                Log.d("search_filter_contains", "${item.trim()?.replace(" ", "")}")
                schoolNameData.add(item)
            }
        }
    }

    inner class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        fun bind(item: String) = with(view){
            school.text = item
        }

    }

}