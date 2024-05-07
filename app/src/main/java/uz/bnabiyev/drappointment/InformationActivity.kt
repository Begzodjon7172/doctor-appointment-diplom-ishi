package uz.bnabiyev.drappointment

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import uz.bnabiyev.drappointment.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityInformationBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://medall.uz/")


    }

    //    class MyWebViewClient extends WebViewClient{
    //        @Override
    //        public void onPageStarted(WebView view, String url, Bitmap favicon) {
    //            super.onPageStarted(view, url, favicon);
    //        }
    //
    //        @Override
    //        public void onPageFinished(WebView view, String url) {
    //            super.onPageFinished(view, url);
    //            progressBar.setVisibility(View.GONE);
    //            webView.setVisibility(View.VISIBLE);
    //        }
    //    }
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}