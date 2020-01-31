package com.batterbox.power.phone.app.hybrid;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.widget.dialog.DialogUtils;


/**
 * Created by ass on 2018/2/26.
 */
public abstract class HybridBaseNavigationActivity extends NavigationActivity {

    //    public final static String PARAM_URL = "url";
    ProgressBar pb;
    protected WebView webView;
    private FrameLayout flVideoContainer;
    String url;
    String defTitle;
    String htmlData;
//    @Override
//    protected void onCreated(@Nullable Bundle savedInstanceState) {
//        super.onCreated(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_hybrid;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            defTitle = getIntent().getStringExtra("defTitle");
            htmlData = getIntent().getStringExtra("htmlData");
        }
//        if (!StringUtil.isEmpty(url)) {
//            ArrayList<String> urlRules = AppConfigUtil.getUrlRule();
//            if (urlRules != null) {
//                for (String urlRule : urlRules) {
//                    if (url.contains(urlRule)) {
//                        setNavigationVisible(false);
//                        break;
//                    }
//                }
//            }
//        }
        pb = findViewById(R.id.act_hybird_pb);
        webView = findViewById(R.id.act_hybird_webView);
        flVideoContainer = findViewById(R.id.act_hybird_flVideoContainer);
        WebSettings webSettings = webView.getSettings();
        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.setJavaScriptEnabled(true);
//        webView.removeJavascriptInterface("searchBoxJavaBridge_");
//        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
//        webView.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");
        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
//        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

//        //禁用file协议
//        webSettings.setAllowFileAccess(false);
//        webSettings.setAllowFileAccessFromFileURLs(false);
//        webSettings.setAllowUniversalAccessFromFileURLs(false);
//        //需要用file协议
//        webSettings.setAllowFileAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(false);
//        webSettings.setAllowUniversalAccessFromFileURLs(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // KITKAT
        {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
                pb.setSecondaryProgress(newProgress + 8);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtil.isEmpty(defTitle))
                    setNavigationTitle(StringUtil.fixNullStr(title));
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (!StringUtil.isEmpty(url) && !StringUtil.isEmpty(message) && result != null) {
                    DialogUtils.showDialog(getSupportFragmentManager(), "", StringUtil.fixNullStr(message), getString(R.string.app_11), v -> result.confirm()).setOnDismissListener(dialog -> result.cancel());
                    return true;
                }
                return super.onJsAlert(view, url, message, result);
            }

            //            @Override
//            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//                return super.onJsPrompt(view, url, message, defaultValue, result);
//            }
            CustomViewCallback mCallback;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                barLay.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
                flVideoContainer.setVisibility(View.VISIBLE);
                flVideoContainer.addView(view);
                mCallback = callback;
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                barLay.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                flVideoContainer.setVisibility(View.GONE);
                flVideoContainer.removeAllViews();
                super.onHideCustomView();

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb.setVisibility(View.VISIBLE);
                FQL.d("HybridBaseNavigationActivity", StringUtil.fixNullStr(url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }

        });

        //开启硬件加速
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setNavigationTitle(StringUtil.fixNullStr(defTitle));
//        registerForContextMenu(webView);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
//        final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();
//        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
//                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
////            contextMenu.setHeaderTitle("网页中下载图片");
//            contextMenu.add(0, 1, 0, getString(R.string.user_52))
//                    .setOnMenuItemClickListener(menuItem -> {
//                        if (webViewHitTestResult.getExtra() != null) {
//                            String base64 = webViewHitTestResult.getExtra().replace("data:image/png;base64,", "")
//                                    .replace("data:image/jpg;base64,", "");
//                            Bitmap bitmap = Base64Util.base64ToBitmap(base64);
//                            if (bitmap == null) {
//                                FQT.showShort(HybridBaseNavigationActivity.this, getString(R.string.user_51));
//                                return false;
//                            }
//                            File file = FileUtil.savePictureToAlbum(HybridBaseNavigationActivity.this, bitmap);
//                            if (file != null) {
//                                FQT.showShort(HybridBaseNavigationActivity.this, getString(R.string.user_50));
//                            } else {
//                                FQT.showShort(HybridBaseNavigationActivity.this, getString(R.string.user_51));
//                            }
//                        } else {
//                            FQT.showShort(HybridBaseNavigationActivity.this, getString(R.string.user_51));
//                        }
//
//                        return false;
//                    });
//        }
//    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            if (!StringUtil.isEmpty(htmlData)) {
                webView.loadData(htmlData, "text/html; charset=UTF-8", null);
            } else if (!StringUtil.isEmpty(url)) {
                webView.loadUrl(url);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        webView.clearFormData();
        webView.clearCache(true);
        webView.destroy();
        super.onDestroy();
    }
}
