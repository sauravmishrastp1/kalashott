package com.dd.samplevideoplayer.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebChromeClient.CustomViewCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.dd.samplevideoplayer.R

class VideoPlayerActivity : Activity() {
    private var webView: WebView? = null
    private var customViewContainer: FrameLayout? = null
    private var customViewCallback: CustomViewCallback? = null
    private var mCustomView: View? = null
    private var mWebChromeClient: myWebChromeClient? = null
    private var mWebViewClient: myWebViewClient? = null
    private var screenId =0

    /**
     * Called when the activity is first created.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_l)
        customViewContainer = findViewById<View>(R.id.customViewContainer) as FrameLayout
        webView = findViewById<View>(R.id.webView) as WebView
        mWebViewClient = myWebViewClient()
        webView!!.webViewClient = mWebViewClient!!
        mWebChromeClient = myWebChromeClient()
        webView!!.webChromeClient = mWebChromeClient
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.setAppCacheEnabled(true)
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.saveFormData = true
        webView!!.settings.useWideViewPort = true;

        webView!!.loadUrl(intent.getStringExtra("videoUrl").toString())



    }

    fun inCustomView(): Boolean {
        return mCustomView != null
    }

    fun hideCustomView() {
        mWebChromeClient!!.onHideCustomView()
    }

    override fun onPause() {
        super.onPause() //To change body of overridden methods use File | Settings | File Templates.
        webView!!.onPause()
    }

    override fun onResume() {
        super.onResume() //To change body of overridden methods use File | Settings | File Templates.
        webView!!.onResume()
    }

    override fun onStop() {
        super.onStop() //To change body of overridden methods use File | Settings | File Templates.
        if (inCustomView()) {
            hideCustomView()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView()
                return true
            }
            if (mCustomView == null && webView!!.canGoBack()) {
                webView!!.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    internal inner class myWebChromeClient : WebChromeClient() {
        private val mDefaultVideoPoster: Bitmap? = null
        private var mVideoProgressView: View? = null
        override fun onShowCustomView(
            view: View,
            requestedOrientation: Int,
            callback: CustomViewCallback
        ) {
            onShowCustomView(
                view,
                callback
            ) //To change body of overridden methods use File | Settings | File Templates.
        }

        override fun onShowCustomView(view: View, callback: CustomViewCallback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden()
                return
            }
            mCustomView = view
            webView!!.visibility = View.GONE
            customViewContainer!!.visibility = View.VISIBLE
            customViewContainer!!.addView(view)
            customViewCallback = callback
        }

        override fun getVideoLoadingProgressView(): View? {
            if (mVideoProgressView == null) {
                val inflater = LayoutInflater.from(this@VideoPlayerActivity)
//                mVideoProgressView = inflater.inflate(R.layout.video_progress, null)
            }
            return mVideoProgressView
        }

        override fun onHideCustomView() {
            super.onHideCustomView() //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null) return
            webView!!.visibility = View.VISIBLE
            customViewContainer!!.visibility = View.GONE

            // Hide the custom view.
            mCustomView!!.visibility = View.GONE

            // Remove the custom view from its container.
            customViewContainer!!.removeView(mCustomView)
            customViewCallback!!.onCustomViewHidden()
            mCustomView = null
        }
    }

    internal inner class myWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return super.shouldOverrideUrlLoading(
                view,
                url
            ) //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById<LinearLayout>(R.id.layoutR_).layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )


            screenId =1

        } else {
            findViewById<LinearLayout>(R.id.layoutR_).layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                500
            )
            screenId =0


        }
    }

    override fun onBackPressed() {
        if(screenId ==1){
           requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }else{
            super.onBackPressed()

        }
    }

}