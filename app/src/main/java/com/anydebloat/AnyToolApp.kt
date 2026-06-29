package com.anydebloat

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import com.anydebloat.shizuku.ShizukuService
import rikka.shizuku.Shizuku

class AnyToolApp : Application() {

    companion object {
        const val TAG = "AnyTool"
        lateinit var instance: AnyToolApp
            private set
    }

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        Log.d(TAG, "Shizuku binder received")
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        Log.w(TAG, "Shizuku binder dead")
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "Shizuku service connected")
            ShizukuService.bind(binder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.w(TAG, "Shizuku service disconnected")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Shizuku.addBinderReceivedListener(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)

        if (Shizuku.isPreV11()) {
            Log.w(TAG, "Shizuku is too old, please update")
        }

        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            Shizuku.bindUserService(
                Shizuku.UserServiceArgs(
                    ComponentName(this, ShizukuService::class.java)
                ).daemon(false),
                serviceConnection
            )
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Shizuku.removeBinderReceivedListener(binderReceivedListener)
        Shizuku.removeBinderDeadListener(binderDeadListener)
        Shizuku.unbindUserService(
            Shizuku.UserServiceArgs(
                ComponentName(this, ShizukuService::class.java)
            ),
            serviceConnection,
            true
        )
    }
}
