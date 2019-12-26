package com.jianzhi.crashhandlersample

import android.app.Application
import android.net.Uri
import com.jianzhi.jzcrashhandler.CrashHandle

class Myapp : Application() {

    override fun onCreate() {
        super.onCreate()
        CrashHandle.newInstance(this,MainActivity::class.java).SetUp(CrashHandle.UPLOAD_CRASH_MESSAGE)
    }
}