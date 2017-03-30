package admin4.techelm.com.techelmtechnologies.activity.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import admin4.techelm.com.techelmtechnologies.R;

/**
 * Created by admin 4 on 28/03/2017.
 * WEB VIEW Client
 */

public class WebViewActivity_Generic extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout containerView = (FrameLayout) findViewById(R.id.containerView);

        LayoutInflater inflater = (LayoutInflater) WebViewActivity_Generic.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WebView myWebView = (WebView) inflater.inflate(R.layout.content_web, null);
        containerView.addView(myWebView);

        myWebView.loadUrl("http://www.facebook.com.ph");

        myWebView.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.facebook.com.ph")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
