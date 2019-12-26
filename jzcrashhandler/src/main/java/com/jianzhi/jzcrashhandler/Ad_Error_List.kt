package com.jianzhi.jzcrashhandler

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.ad_list_error.view.*

class Ad_Error_List(val id:ArrayList<String>,val time:ArrayList<String>) :RootAdapter(R.layout.ad_list_error){
    override fun SizeInit(): Int {
        return id.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.mView.textView3.text="崩潰時間:${time[position]}"
        holder.mView.setOnClickListener {
            val transaction= ((holder.mView.context)as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frage, ShowDetail(id[position]))
                .addToBackStack(null)
                .commit()
        }
    }

}