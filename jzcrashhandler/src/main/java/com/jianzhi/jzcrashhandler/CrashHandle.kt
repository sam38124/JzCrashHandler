package com.jianzhi.jzcrashhandler

import android.app.Application
import android.content.Intent
import android.util.Log
import com.jzsql.lib.mmySql.ItemDAO
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat


class CrashHandle(var app: Application, var startpage: Class<*>?) {
    companion object {
        val SHOW_CRASH_MESSAGE = 1
        val RESTART = 2
        val UPLOAD_CRASH_MESSAGE = 3
        var CrashHandle: CrashHandle? = null
        fun newInstance(app: Application, startpage: Class<*>?): CrashHandle {
            if (CrashHandle == null) {
                CrashHandle = CrashHandle(app, startpage)
            }
            return CrashHandle!!
        }

        fun getInstance(): CrashHandle {
            return CrashHandle!!
        }
    }

    fun ReadLog() {
        restartApp(Act_Show_Error::class.java)
    }

    fun DeltetRecord() {
        try {
            val base = ItemDAO(app, "crash.db")
            base.ExSql("drop table  `crash`")
            base.close()
        } catch (E: java.lang.Exception) {
            E.printStackTrace()
        }
    }

    var selection = 0
    private val restartHandler = Thread.UncaughtExceptionHandler { th, ex ->
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        ex.printStackTrace(pw)
        Log.e("error:", "error\n${sw}")
        try {
            val base = ItemDAO(app, "crash.db")
            base.ExSql(
                "CREATE TABLE IF NOT EXISTS crash (\n" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        " data VARCHAR NOT NULL,\n" +
                        "time TIME" +
                        ");\n"
            )
            base.ExSql("insert into `crash` (data,time) values ('${sw}','${getDateTime()}')")
            base.close()
            when (selection) {
                SHOW_CRASH_MESSAGE -> {
                    restartApp(Act_Show_Error::class.java)
                }
                RESTART -> {
                    restartApp(startpage)
                }
                UPLOAD_CRASH_MESSAGE -> {

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun SetUp(a: Int) {
        selection = a
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
    }

    fun restartApp(srartpage: Class<*>?) {
        val intent = Intent(app, srartpage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    fun getDateTime(): String {

        val sdFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        val date = java.util.Date()

//System.out.println(strDate);

        return sdFormat.format(date)

    }
}