package com.android.hammad.testapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.android.hammad.testapp.R;

public class WebActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_KEY_ARTICLE_URL = "key_article_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if(intent.hasExtra(INTENT_EXTRA_KEY_ARTICLE_URL)) {
            // Get the url from the intent extra string and load it in the WebView
            String url = intent.getStringExtra(INTENT_EXTRA_KEY_ARTICLE_URL);
            WebView webView = (WebView) findViewById(R.id.webView_article);
            webView.loadUrl(url);
        }
    }

}
