package com.example.myapplication.dialogs

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.FEATURE_NO_TITLE
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogBaseBinding
import com.gyf.immersionbar.ImmersionBar

class BaseDialog: DialogFragment() {
    private lateinit var binding: DialogBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        dialog?.requestWindowFeature(FEATURE_NO_TITLE)
//        if (this.enableTransparentBackground()) {
//            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0x000000))
//        }
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_Normal)
        super.onCreate(savedInstanceState)
    }

    protected fun enableTransparentBackground(): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(false).init()
        binding.root.setOnClickListener {
            dismissAllowingStateLoss()
        }

        Log.d("zhouzheng", "sb${((dialog?.window?.decorView?.findViewById<View>(android.R.id.content)?.parent) as ViewGroup).getChildAt(0)::class.java.name}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBaseBinding.inflate(inflater)
        return binding.root
    }
}

fun show(context: Context?, fragment: DialogFragment): Boolean {
    val activity: Activity = getActivity(context)!!
    if (activity is AppCompatActivity) {
        val compatActivity = activity
        if (!compatActivity.isFinishing) {
            try {
                fragment.showNow(compatActivity.supportFragmentManager, null)
                return true
            } catch (var6: Exception) {
            }
        } else {
        }
    }
    return false
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