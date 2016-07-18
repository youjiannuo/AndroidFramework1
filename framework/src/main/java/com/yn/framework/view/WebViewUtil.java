package com.yn.framework.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yn.framework.data.UserSharePreferences;
import com.yn.framework.system.BuildConfig;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.StringUtil;
import com.yn.framework.system.SystemUtil;
import com.yn.framework.system.UrlUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by youjiannuo on 16/1/31.
 */
public class WebViewUtil {

    public static final int POST = 1;

    public static final int GET = 2;

    private WebView mWebView;
    private ProgressBar mProgress;
    private OnWebViewInfoListener mOnWebViewInfoListener;
    //已经点击了back
    private boolean isBack = false;
    //错误的地址
    private String mFailUrl = "";
    private Map<String, String> mExtraHeaders;


    private String mUrl;
    private int mType;
    private String mParams;
    private String mData;
    private String mReloadUrl;

    public WebViewUtil(WebView webView, ProgressBar progressBar) {
        mWebView = webView;
        mProgress = progressBar;
    }

    public void setUrl(String url) {
        setUrl(url, null);
    }

    public void setOnWebViewInfoListener(OnWebViewInfoListener l) {
        this.mOnWebViewInfoListener = l;
    }

    public void setUrlWitchHead(String url) {
        getHead();
        setUrl(url, mExtraHeaders);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    private void getHead() {
        mExtraHeaders = new HashMap<>();
        mExtraHeaders.put("device", "Android");
        mExtraHeaders.put("version", SystemUtil.getProjectVersion());
        mExtraHeaders.put("token", UserSharePreferences.getToken());
    }

    public void setUrl(final String url, final Map<String, String> httpHeaders) {
        setUrl(url, "", httpHeaders, GET, null);
    }

    public void setPostUrl(final String url, final Map<String, String> httpHeaders, String params) {
        getHead();
        setUrl(url, "", httpHeaders, POST, params);
    }

    public void setData(String data) {
        setUrl("", data, null, -1, "");
    }

    public void setUrl(final String url, String data, final Map<String, String> httpHeaders, final int type, final String params) {
        setUrl(url, data, httpHeaders, type, params, false);
    }

    @SuppressLint({"SetJavaScriptEnable", "AddJavascriptInterface", "SetJavaScriptEnabled"})
    public void setUrl(final String url, final String data, final Map<String, String> httpHeaders, final int type, final String params, boolean isLoad) {
        mReloadUrl = mUrl = url;
        mType = type;
        mParams = params;
        mData = data;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(new JsInteration(), "control");
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                ContextManager.getContext().startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                SystemUtil.printlnInfo("url 开始= " + url);
                if (mOnWebViewInfoListener != null && isBack && url.equals("file:///android_asset/loading.html")) {
                    mOnWebViewInfoListener.pageFinish();
                }

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                SystemUtil.printlnInfo("image = " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                SystemUtil.printlnInfo("加载错误" + failingUrl);
                mFailUrl = failingUrl;
                if (view != null) {
                    view.loadUrl("file:///android_asset/login_error.html");
                }

                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                SystemUtil.printlnInfo(" should url = " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgress != null) {
                    mProgress.setVisibility(View.GONE);
                }
                if (mOnWebViewInfoListener != null) {
                    mOnWebViewInfoListener.onTitleInfoString(mWebView.getTitle(), mWebView);
                }
                if (view != null) {
                    view.loadUrl("javascript:window.control.showSource('<head>'+"
                            + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
                if (!"file:///android_asset/loading.html".equals(url) && !"file:///android_asset/login_error.html".equals(url)) {
                    mReloadUrl = url;
                }
                super.onPageFinished(view, url);
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgress != null) {
                    mProgress.setVisibility(View.VISIBLE);
                    mProgress.setProgress(newProgress);
                    if (newProgress == 100) {
                        mProgress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mOnWebViewInfoListener != null) {
                    mOnWebViewInfoListener.onTitleInfoString(title, mWebView);

                }
            }


        });

        String dataUrl1 = url;
        if (!StringUtil.isURL(url)) {
            dataUrl1 = UrlUtil.getRedirectUrl(BuildConfig.HOST + "/" + url);
        }
        final String dataUrl = dataUrl1;
        if (!isLoad) {
            if (!StringUtil.isURL(dataUrl) && (StringUtil.isEmpty(mData))) {
                mWebView.loadUrl("file:///android_asset/login_error.html");
                return;
            }
            mWebView.loadUrl("file:///android_asset/loading.html");
        }

        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    if (httpHeaders != null) {
                        if (mWebView != null) {
                            if (type == GET) {
                                mWebView.loadUrl(dataUrl.trim(), httpHeaders);
                            } else {
                                mWebView.postUrl(dataUrl.trim(), params.getBytes());
                            }
                        }
                    } else {
                        if (mWebView != null) {
                            if (!StringUtil.isEmpty(mData)) {
                                mWebView.loadData(mData, "text/html", "UTF-8");
                            } else if (type == GET) {
                                mWebView.loadUrl(dataUrl.trim());
                            } else {
                                mWebView.postUrl(dataUrl.trim(), params.getBytes());
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }, 200);

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            if (mOnWebViewInfoListener != null && mWebView.getUrl().equals("file:///android_asset/login_error.html")) {
                mOnWebViewInfoListener.pageFinish();
            }
            isBack = true;
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return false;
    }

    public void runJS(String function) {
        if (mWebView != null) {
            mWebView.loadUrl("javascript:" + function + "()");
        }
    }

    public void onDestroy() {
        try {
            if (mWebView != null) {
                ViewGroup mViewGroup = null;
                if (mWebView.getParent() != null) {
                    mViewGroup = (ViewGroup) mWebView.getParent();
                }
                if (mViewGroup != null) {
                    mViewGroup.removeView(mWebView);
                }
                mWebView.removeAllViews();
                mWebView.destroy();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public class JsInteration {

        @JavascriptInterface
        public void onReload() {
            if (mWebView != null) {

                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.goBack();
                    }
                });

                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHead();
                        setUrl(mFailUrl, mData, mExtraHeaders, mType, mParams);
                    }
                }, 500);
            }
        }

        @JavascriptInterface
        public void JsJumpNative(final String s) {
            if (mWebView != null) {
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }

        @JavascriptInterface
        public void showSource(final String html) {
            Message msg = new Message();
            msg.obj = html;
            new android.os.Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (mOnWebViewInfoListener != null) {
                        mOnWebViewInfoListener.htmlText(html);
                    }
                }
            }.sendMessage(msg);
        }

    }

    public void onResume() {
        onResume(false);

    }

    public void onResume(boolean isReLoad) {
        if (mWebView != null) {
            if (isReLoad) {
                getHead();
                setUrl(mReloadUrl, mData, mExtraHeaders, mType, mParams);
            }
            mWebView.onResume();
        }
    }

    public void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
    }


    public interface OnWebViewInfoListener {
        void onTitleInfoString(String title, WebView webView);

        void pageFinish();

        void htmlText(String html);

    }


}
