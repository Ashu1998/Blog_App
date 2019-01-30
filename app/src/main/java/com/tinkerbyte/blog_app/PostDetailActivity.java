package com.tinkerbyte.blog_app;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PostDetailActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    Toolbar mToolbar;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        mWebView=(WebView)findViewById(R.id.descriptionView);

        setSupportActionBar(mToolbar);
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(view.GONE);
                mWebView.setVisibility(View.VISIBLE);

            }
        });

        mWebView.loadUrl(getIntent().getStringExtra("URL"));
    }
}
