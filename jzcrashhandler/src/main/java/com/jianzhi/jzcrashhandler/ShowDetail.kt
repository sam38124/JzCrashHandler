package com.jianzhi.jzcrashhandler


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jzsql.lib.mmySql.ItemDAO
import com.jzsql.lib.mmySql.Sql_Result
import kotlinx.android.synthetic.main.fragment_show_detail.view.*

class ShowDetail(var id:String) : Fragment() {
lateinit var rootview:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview=inflater.inflate(R.layout.fragment_show_detail, container, false)
        try{
            val base = ItemDAO(activity!!, "crash.db")
            base.Query("select * from crash where id='${id}'", Sql_Result {
                rootview.textView.text=it.getString(1)
            })
        }catch (e:Exception){e.printStackTrace()}
        Log.e("errorl","${rootview.textView.text.length}")
        return rootview
    }


}
