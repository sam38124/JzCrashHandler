package com.jianzhi.jzcrashhandler


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jzsql.lib.mmySql.JzSqlHelper
import com.jzsql.lib.mmySql.Sql_Result
import kotlinx.android.synthetic.main.fragment_errorfrage.view.*


class Errorfrage : Fragment() {
    var errorlist=ArrayList<String>()
    var id=ArrayList<String>()
    lateinit var rootview:View
    lateinit var Ad_Error_List:Ad_Error_List
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview=inflater.inflate(R.layout.fragment_errorfrage, container, false)
        errorlist.clear()
        id.clear()
           val base = JzSqlHelper(activity!!, "crash.db")
        base.exsql(
            "CREATE TABLE IF NOT EXISTS crash (\n" +
                                    " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                    " data VARCHAR NOT NULL,\n" +
                                    "time TIME ," +
                                    "uploader INT DEFAULT 0" +
                                    ");\n"
        )
            base.query("select * from crash order by id desc", Sql_Result {
                id.add(it.getString(0))
                errorlist.add(it.getString(2))
            })
        rootview.re.layoutManager=LinearLayoutManager(activity)
        rootview.re.adapter=Ad_Error_List(id,errorlist)
        if(errorlist.size>0){rootview.textView4.visibility=View.GONE}else{rootview.textView4.visibility=View.VISIBLE}
        return rootview
    }


}
