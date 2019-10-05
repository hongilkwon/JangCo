package com.example.jangco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class SearchAddressActivity : AppCompatActivity() {


    private var browser: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_address)

        browser = findViewById(R.id.webView) as WebView
        browser!!.getSettings().javaScriptEnabled = true
        browser!!.addJavascriptInterface(MyJavaScriptInterface(), "Android")

        browser!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

                browser!!.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        })


        browser!!.loadUrl("http://www.inspond.com/daum.html")
        // 경고! 위 주소대로 서비스에 사용하시면 파일이 삭제됩니다.
        // 꼭 자신의 웹 서버에 해당 파일을 복사해서 주소를 변경해 사용하세요.
    }

    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String) {

            val extra = Bundle()
            val intent = Intent()
            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(RESULT_OK, intent)
            finish()

        }
    }
}
