package com.jianzhi.jzcrashhandler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.jzsql.lib.mmySql.JzSqlHelper
import com.jzsql.lib.mmySql.Sql_Result
import java.lang.Exception

class Act_Show_Error : AppCompatActivity() {
var error=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act__show__error)
        var transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frage, Errorfrage())
            .commit()
//        try{
//            val base = JzSqlHelper(this, "crash.db")
//            base.query("select * from crash order by id desc limit 0,1", Sql_Result {
//                error.add(it.getString(1))
//            })
//        }catch (e:Exception){e.printStackTrace()}
//        if(error.size>0)findViewById<TextView>(R.id.textView).text=error[0]
    }

}
