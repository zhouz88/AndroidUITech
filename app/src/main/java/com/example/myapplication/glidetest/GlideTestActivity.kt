package com.example.myapplication.glidetest

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.example.myapplication.databinding.GlideTestLayoutBinding
import com.zz.router_annotation.Destination
import java.security.MessageDigest


@Destination(url = "route://glidetest", description = "glide测试页面")
class GlideTestActivity : AppCompatActivity(){
    // 原图: 由datacachekey 索引的文件 （原始inputstream得到流文件）, resource图： 已经解码过的图片资源由resourcecachekey索引的文件
    //网络图
    // data 只存原图，resource 除了原图都存， None 什么也不存。  ALL : data和Resource都存， Auto: 等价于 data
    // 500dp
    // 用 Data: 原图大小: 3.139kb   点击后 3.139
    // 用 ALL: 都下了  102.501kb   点击后  130.239
    // 用 Resource: 99.256kb   点击后  126.926
    // 用 AUTO:    3.139kb     点击后  3.139
    // 用 NONE:   0KB    点击后 0KB

    // 10dp
    // 用 Data: 原图大小: 3.139kb   点击后 3.139
    // 用 ALL: 都下了  4.699kb   点击后  7.053   //图全部存了
    // 用 Resource: 1.454kb   点击后  3.739
    // 用 AUTO:    3.139kb     点击后  3.139
    // 用 NONE:   0KB    点击后 0KB

    // 原图SIZE （resource 参数和 data一样但是用不一样的key）
    // 用 Data: 原图大小: 3.139kb   点击后 3.139
    // 用 Resource: 3.202kb   点击后  4.626kb
    // 用 NONE:   0KB    点击后 0KB

    // 用 ALL: 都下了  6.447kb   点击后  7.939KB
    // 用 AUTO:    3.139kb     点击后  3.139

    //本地数据 鉴定
    // data 只存原图，resource 除了原图都存， None 什么也不存。  ALL : 此时只有Resource存， Auto: 等价于 resource
    // 10dp
    // 用 Data: 原图大小: 187.193kb   点击后 187.193kb
    // 用 Resource: 1.409kb   点击后  3.522kb
    // 用 NONE:   0KB    点击后 0KB

    // 用 ALL:   1.409kb   点击后  3.522kb
    // 用 AUTO:    1.409kb     点击后  3.522kb


    //补充： resourceKey  和 dataKEY 一模一样的额时候 下面方法无论先data还是先resource
    // （只有都用Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAl）: 貌似不会走到磁盘缓存，只会用内存缓存。

    //分析原因 暂时不知道： 可能文件名字就存的是 url + original + original 这样存的 导致resource 和data 两个key 都映射过来这个文件
    //使用signature可以避免

    private lateinit var binding: GlideTestLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GlideTestLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //网络图
        Glide.with(this).load("https://pic1.zhimg.com/v2-a6b58e82c8cdb830ad7bd85d86469458_l.jpg?source=172ae18b")
            .diskCacheStrategy(
                DiskCacheStrategy.RESOURCE
            ).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).into(binding.image)
        binding.imag1e.setOnClickListener {
           // Glide.
            Glide.with(this).load("https://pic1.zhimg.com/v2-a6b58e82c8cdb830ad7bd85d86469458_l.jpg?source=172ae18b")
                .diskCacheStrategy(
                    DiskCacheStrategy.DATA
                ).override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).into(binding.image)
            binding.root.postDelayed({
                ToastUtils.showShort("OK啊")
                binding.imag1e.text = "diskcache大小:" + com.blankj.utilcode.util.FileUtils.getSize(cacheDir)
            }, 4000)
        }
        binding.root.postDelayed({
            binding.imag1e.text = "diskcache大小:" + com.blankj.utilcode.util.FileUtils.getSize(cacheDir)
        }, 4000)

        //本地图
//        binding.root.postDelayed({
//            binding.imag1e.text = "diskcache大小:" + com.blankj.utilcode.util.FileUtils.getSize(cacheDir)
//        }, 4000)
//
//        Glide.with(this).load(R.mipmap.item1)
//            .diskCacheStrategy(
//                DiskCacheStrategy.DATA
//            ).override(SizeUtils.dp2px(10f), SizeUtils.dp2px(10f)).into(binding.image)
//        binding.imag1e.setOnClickListener {
//            // Glide.
//            Glide.with(this).load(R.mipmap.item1)
//                .diskCacheStrategy(
//                    DiskCacheStrategy.DATA
//                ).override(SizeUtils.dp2px(10f), SizeUtils.dp2px(10f)).into(binding.image)
//            binding.root.postDelayed({
//                ToastUtils.showShort("OK啊")
//                binding.imag1e.text = "diskcache大小:" + com.blankj.utilcode.util.FileUtils.getSize(cacheDir)
//            }, 4000)
//        }
//
//
//        binding.root.postDelayed({
//            binding.imag1e.text = "diskcache大小:" + com.blankj.utilcode.util.FileUtils.getSize(cacheDir)
//        }, 4000)
    }
}

class MyImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {


    override fun setImageDrawable(drawable: Drawable?) {
        drawable?.let {
            val bitmap = (it as BitmapDrawable).bitmap
            Log.d("zhouzheng", "我的hashcode和w,h${bitmap.hashCode()}:${bitmap.width}:${bitmap.height}")
        }
        super.setImageDrawable(drawable)
    }
}