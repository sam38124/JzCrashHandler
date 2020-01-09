package com.jianzhi.jzcrashhandler

import android.app.Application
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import com.jianzhi.jzcrashhandler.Post_Server.post
import com.jzsql.lib.mmySql.ItemDAO
import com.jzsql.lib.mmySql.Sql_Result
import java.io.PrintStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


class CrashHandle(var app: Application, var startpage: Class<*>?) {
    companion object {
        val SHOW_CRASH_MESSAGE = 1
        val RESTART = 2
        val UPLOAD_CRASH_MESSAGE = 3
        var CrashHandle: CrashHandle? = null
        var appname = ""
        var timer = Timer()
        fun newInstance(app: Application, startpage: Class<*>?): CrashHandle {
            Log.e("CrashHandle", "註冊包名:" + app.packageName)
            Log.e("CrashHandle", "MAKE:" + Build.MANUFACTURER)
            var pi = app.packageManager.getPackageInfo(app.packageName, PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                Log.e("CrashHandle:","versionName:"+pi.versionName)
                Log.e("CrashHandle:","versionCode:"+pi.versionCode)
                Log.e("CrashHandle:","SDK_INT:"+ Build.VERSION.SDK_INT)
            }
            appname = app.packageName
            if (CrashHandle == null) {
                CrashHandle = CrashHandle(app, startpage)
            }
            return CrashHandle!!
        }

        fun getInstance(): CrashHandle {
            return CrashHandle!!

                    }
    }

    var handler = Handler()
    fun readLog() {
        restartApp(Act_Show_Error::class.java)
    }

    fun deleteRecord() {
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
            if (selection == SHOW_CRASH_MESSAGE || selection == RESTART) {
                val base = ItemDAO(app, "crash.db")
                base.ExSql(
                    "CREATE TABLE IF NOT EXISTS crash (\n" +
                                    " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                    " data VARCHAR NOT NULL,\n" +
                                    "time TIME ," +
                                    "uploader INT DEFAULT 0" +
                                    ");\n"
                )
                base.Query("select count(1) from `crash`", Sql_Result {
                    if(it.getInt(0)>10){
                        base.ExSql("delete from `crash` where id in(select id from `crash` limit (select count(1) from `crash`)-10)")
                    }
                })
                base.ExSql("insert into `crash` (data,time) values ('${sw}','${getDateTime()}')")
                base.close()
            }
            when (selection) {
                SHOW_CRASH_MESSAGE -> {
                    restartApp(Act_Show_Error::class.java)
                }
                RESTART -> {
                    restartApp(startpage)
                }
                UPLOAD_CRASH_MESSAGE -> {
                    Thread {
                        var temp="Make:${Build.MANUFACTURER}\n" +
                                "Model:${Build.MODEL}\n"
                        val pi = app.packageManager.getPackageInfo(app.packageName, PackageManager.GET_ACTIVITIES);
                        if (pi != null) {
                            temp=temp+"VERSION_NAME:"+pi.versionName+"\n"
                            temp=temp+"VERSION_CODE:"+pi.versionCode+"\n"
                            temp=temp+"SDK_INT:"+ Build.VERSION.SDK_INT+"\n"
                        }
                        if (!post("/topics/${appname}", temp, "$sw")) {
                            val base = ItemDAO(app, "crash.db")
                            base.ExSql(
                                "CREATE TABLE IF NOT EXISTS crash (\n" +
                                    " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                    " data VARCHAR NOT NULL,\n" +
                                    "time TIME ," +
                                    "uploader INT DEFAULT 0" +
                                    ");\n"
                            )
                            base.Query("select count(1) from `crash`", Sql_Result {
                                if(it.getInt(0)>10){
                                    base.ExSql("delete from `crash` where id in(select id from `crash` limit (select count(1) from `crash`)-10)")
                                }
                            })
                            base.ExSql("insert into `crash` (data,time) values ('${sw}','${getDateTime()}')")
                            base.close()
                        };
                    }.start()
                    try {
                        Thread.sleep(2000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    restartApp(startpage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUP(a: Int) {
        selection = a
        Thread.setDefaultUncaughtExceptionHandler(restartHandler);
        if (selection == UPLOAD_CRASH_MESSAGE) {
            Thread {
                try {
                    while (true) {
                        Thread.sleep(10000)
                        val base = ItemDAO(app, "crash.db")
                        base.ExSql(
                            "CREATE TABLE IF NOT EXISTS crash (\n" +
                                    " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                    " data VARCHAR NOT NULL,\n" +
                                    "time TIME ," +
                                    "uploader INT DEFAULT 0" +
                                    ");\n"
                        )
                        base.Query("select * from `crash` where uploader=0", Sql_Result {
                            if (it.getInt(3) == 0) {
                                var temp="Make:${Build.MANUFACTURER}\n" +
                                        "Model:${Build.MODEL}\n"
                                val pi = app.packageManager.getPackageInfo(app.packageName, PackageManager.GET_ACTIVITIES);
                                if (pi != null) {
                                    temp=temp+"VERSION_NAME:"+pi.versionName+"\n"
                                    temp=temp+"VERSION_CODE:"+pi.versionCode+"\n"
                                    temp=temp+"SDK_INT:"+ Build.VERSION.SDK_INT+"\n"
                                }
                                if (post(
                                        "/topics/${appname}",
                                        temp,
                                        "${it.getString(1)}"
                                    )
                                ) {
                                    base.ExSql("update `crash` set `uploader`=1 where id=${it.getString(0)}")
                                }
                            }
                        })
                        base.close()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }.start()
        }
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

        return sdFormat.format(date)

    }
}