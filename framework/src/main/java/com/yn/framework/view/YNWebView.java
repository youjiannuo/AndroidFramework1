package com.yn.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.yn.framework.R;
import com.yn.framework.system.UrlUtil;

import java.util.Map;

/**
 * Created by youjiannuo on 16/1/23.
 */
public class YNWebView extends FrameLayout {

    private WebView mWebView;
    private ProgressBar mProgress;
    private WebViewUtil mWebViewUtil;

    public YNWebView(Context context) {
        super(context);
        initView();
    }

    public YNWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public YNWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_web, this);
        mWebView = (WebView) findViewById(R.id.webView);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mWebViewUtil = new WebViewUtil(mWebView, mProgress);
    }

    public void setOnWebViewInfoListener(WebViewUtil.OnWebViewInfoListener l) {
        mWebViewUtil.setOnWebViewInfoListener(l);
    }

    public void setUrl(String url) {
        mWebViewUtil.setUrl(url);
    }

    public void setTokenUrl(String url) {
        setUrlWitchHead(UrlUtil.getRedirectUrl(url));
    }

    public void setUrlWitchHead(String url) {
        mWebViewUtil.setUrlWitchHead(url);
    }

    public void setUrl(String url, Map<String, String> httpHeaders) {
        mWebViewUtil.setUrl(url, httpHeaders);
    }

    public void runJs(String function) {
        if (mWebViewUtil != null) {
            mWebViewUtil.runJS(function);
        }
    }

    public void setData(String data) {
        mWebViewUtil.setData(data);
    }

    public void setPostUrl(String url, Map<String, String> httpHeaders, String params) {
        mWebViewUtil.setPostUrl(url, httpHeaders, params);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mWebViewUtil.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mWebViewUtil.onKeyDown(keyCode, event);
    }

    public void onDestroy() {
        if (mWebViewUtil != null) {
            mWebViewUtil.onDestroy();
        }
    }

    public void onResume() {
        if (mWebViewUtil != null) {
            mWebViewUtil.onResume();
        }
    }

    public void onResume(boolean isReLoad) {
        if (mWebViewUtil != null) {
            mWebViewUtil.onResume(isReLoad);
        }
    }

    public void onPause() {
        if (mWebViewUtil != null) {
            mWebViewUtil.onPause();
        }
    }


}
