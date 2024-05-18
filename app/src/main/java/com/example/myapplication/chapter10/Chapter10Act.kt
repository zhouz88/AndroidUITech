package com.example.myapplication.chapter10

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

class Chapter10Act: AppCompatActivity() {

    //private lateinit var binding: TestSurfaceViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var root = TextView(this)
        setContentView(root.apply {
            layoutParams = ViewGroup.LayoutParams(-1,-1)
            textSize = 48f
        })

        val okHttpClient = OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

        val request = Request
            .Builder()
            .url(HttpUrl.Builder()
                .scheme("https")
                .host("www.google.com")
                .build())
            .header("Range" ,"bytes=1000")
            .get()
            .build()

        /* 百度AI回答
        Android中使用OkHttpClient来处理HTTP的Range头以请求剩余数据部分，你可以按照以下步骤操作：

初始化OkHttpClient实例：
首先，你需要一个OkHttpClient实例。如果你还没有创建，可以创建一个新的实例。
java
OkHttpClient client = new OkHttpClient();
构建请求：
构建一个Request对象，指定要下载的URL，并添加Range头来指示服务器你想要从哪个位置开始下载数据。
java
String url = "http://example.com/largefile.zip";
long startByte = 1000; // 假设你已经下载了1000字节

// 构建Range头，指定从startByte开始到文件末尾
String rangeHeader = "bytes=" + startByte + "-";

Request request = new Request.Builder()
        .url(url)
        .header("Range", rangeHeader)
        .build();
发送请求并处理响应：
使用OkHttpClient的newCall方法来发送请求，并处理响应。
         */

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showShort("FAIL ${Thread.currentThread().name}")
                root.text = "FAIL"
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    root.text = response.body?.string()
                }
                ToastUtils.showShort("success${Thread.currentThread().name}")
            }

        })

//        binding = TestSurfaceViewBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        ImmersionBar.with(this).init()
    }

    private fun getActivity(cont: Context?): Activity? {
        return if (cont == null) {
            null
        } else if (cont is Activity) {
            cont
        } else {
            if (cont is ContextWrapper) getActivity(cont.baseContext) else null
        }
    }
}