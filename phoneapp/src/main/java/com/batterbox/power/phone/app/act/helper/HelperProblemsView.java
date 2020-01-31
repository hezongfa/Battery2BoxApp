package com.batterbox.power.phone.app.act.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.http.DomainHelper;
import com.chenyi.baselib.ui.NavTabPagerViewI;

/**
 * Created by ass on 2019-08-08.
 * Description
 */
public class HelperProblemsView extends LinearLayout implements NavTabPagerViewI {
    WebView webView;
    ProgressBar pb;

    public HelperProblemsView(Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_helper_problems, this);
    }

    @Override
    public View getContentView() {
        return this;
    }

    @Override
    public void onCreateView(Context context) {
        pb = findViewById(R.id.view_helper_problems_pb);
        webView = findViewById(R.id.view_helper_problems_wv);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // KITKAT
        {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //开启硬件加速
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
                pb.setSecondaryProgress(newProgress + 8);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }

        });

        webView.loadUrl(DomainHelper.getUserCommonProblemH5());
        DisplayMetrics dm = new DisplayMetrics();

        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        manager.getDefaultDisplay().getMetrics(dm);
        //viewpager与webview滑 动冲突问题
        webView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    int point = (int) event.getX();

                    if (point > 0 && point < 50) {

                        webView.requestDisallowInterceptTouchEvent(false);

                    } else {

                        webView.requestDisallowInterceptTouchEvent(true);

                    }

                    break;

            }

            return false;

        });

    }
}
