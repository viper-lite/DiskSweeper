package io.viper.android.disk.sweeper

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.io.File
import java.sql.SQLException
import java.util.concurrent.Executors

class FileScanService : Service() {

    private fun walkDir(dir: File, result: MutableMap<String, Long>) {
        if (dir.isDirectory) {
            dir.listFiles()?.forEach { item ->
                walkDir(item, result)
            }
        } else {
            Log.i("SVEN", "visit ${dir.absolutePath}")
            result[dir.absolutePath] = dir.length()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Executors.newSingleThreadExecutor().submit {
            val result = mutableMapOf<String, Long>()
            walkDir(Environment.getExternalStorageDirectory(), result)
            Log.i("SVEN", "total size ${result.size}")
            val objects = mutableListOf<FileRecord>()
            result.forEach { (key, value) ->
                objects.add(FileRecord(key, value))
            }
            val dbHelper = DBHelper(this)
            try {
                Log.i("SVEN","database start")
                val dao = dbHelper.daoImpl
                dao.create(objects)
                dbHelper.close()
                Log.i("SVEN","database end")
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return START_NOT_STICKY
    }
}