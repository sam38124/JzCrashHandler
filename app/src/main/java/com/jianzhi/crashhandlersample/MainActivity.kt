package com.jianzhi.crashhandlersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jianzhi.jzcrashhandler.CrashHandle

class MainActivity : AppCompatActivity() {
lateinit var text:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onclick(view: View){
        when(view.id){
            R.id.crash->{
                var a=arrayListOf<String>("ss")
                var b= a[2];
            }
            R.id.read->{
                CrashHandle.getInstance().readLog()
            }
            R.id.delete->{
                CrashHandle.getInstance().deleteRecord()
            }
        }
    }
}
