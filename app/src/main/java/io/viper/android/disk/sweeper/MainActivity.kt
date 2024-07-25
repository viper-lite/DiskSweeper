package io.viper.android.disk.sweeper

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.ContentLoadingProgressBar
import java.io.File
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private fun walkDir(dir: File, result: MutableMap<String, Long>) {
        if (dir.isDirectory) {
            dir.listFiles()?.forEach { item ->
                walkDir(item, result)
            }
        } else {
            dir.length()
            result[dir.absolutePath] = dir.length()
        }
    }

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Executors.newSingleThreadExecutor().submit {
            mHandler.post { useLoading(true) }
            val result = mutableMapOf<String, Long>()
            walkDir(Environment.getExternalStorageDirectory(), result)
            mHandler.post { useLoading(false) }
        }
    }

    private fun useLoading(flag: Boolean) {
        val loadingView = findViewById<ContentLoadingProgressBar>(R.id.loading_view)
        if (flag) {
            loadingView.show()
        } else {
            loadingView.hide()
        }
    }
}