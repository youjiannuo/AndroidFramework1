package com.yn.framework.http;

import android.content.Context;

import com.yn.framework.data.UserSharePreferences;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.SystemUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.HttpRedirectHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.File;

/**
 * Created by youjiannuo on 15/12/18.
 */
public class Http extends HttpUtils {


    private static final String CHANNEL = SystemUtil.getAppMetaData(ContextManager.getContext(), "UMENG_CHANNEL");
    private static final String DEVICE_ID = SystemUtil.getDeviceId(ContextManager.getContext());
    private static final String VERSION = SystemUtil.getAppVersion() + "";

    @Override
    public HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target, RequestParams params, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return super.download(method, url, target, params, autoResume, autoRename, callback);
    }

    public Http() {
        super();
    }

    public Http(Context context) {
        super();
//        this(600000);
    }

    public Http(int connTimeout) {
        super(connTimeout);
    }

    public Http(String userAgent) {
        super(userAgent);
    }

    public Http(int connTimeout, String userAgent) {
        super(connTimeout, userAgent);
    }

    @Override
    public HttpClient getHttpClient() {
        return super.getHttpClient();
    }

    @Override
    public HttpUtils configResponseTextCharset(String charSet) {
        return super.configResponseTextCharset(charSet);
    }

    @Override
    public HttpUtils configHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
        return super.configHttpRedirectHandler(httpRedirectHandler);
    }

    @Override
    public HttpUtils configHttpCacheSize(int httpCacheSize) {
        return super.configHttpCacheSize(httpCacheSize);
    }

    @Override
    public HttpUtils configDefaultHttpCacheExpiry(long defaultExpiry) {
        return super.configDefaultHttpCacheExpiry(defaultExpiry);
    }

    @Override
    public HttpUtils configCurrentHttpCacheExpiry(long currRequestExpiry) {
        return super.configCurrentHttpCacheExpiry(currRequestExpiry);
    }

    @Override
    public HttpUtils configCookieStore(CookieStore cookieStore) {
        return super.configCookieStore(cookieStore);
    }

    @Override
    public HttpUtils configUserAgent(String userAgent) {
        return super.configUserAgent(userAgent);
    }

    @Override
    public HttpUtils configTimeout(int timeout) {
        return super.configTimeout(timeout);
    }

    @Override
    public HttpUtils configSoTimeout(int timeout) {
        return super.configSoTimeout(timeout);
    }

    @Override
    public HttpUtils configRegisterScheme(Scheme scheme) {
        return super.configRegisterScheme(scheme);
    }

    @Override
    public HttpUtils configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        return super.configSSLSocketFactory(sslSocketFactory);
    }

    @Override
    public HttpUtils configRequestRetryCount(int count) {
        return super.configRequestRetryCount(count);
    }

    @Override
    public HttpUtils configRequestThreadPoolSize(int threadPoolSize) {
        return super.configRequestThreadPoolSize(threadPoolSize);
    }

    @Override
    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String url, RequestCallBack<T> callBack) {
        return super.send(method, url, callBack);
    }

    @Override
    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String url, RequestParams params, RequestCallBack<T> callBack) {
        if (params == null) {
            params = new RequestParams();
        }
        params.addHeader("device", "Android");
        params.addHeader("channel", CHANNEL);
        params.addHeader("device_id", DEVICE_ID);
        params.addHeader("version", VERSION);
        params.addHeader("token", UserSharePreferences.getToken());
        return super.send(method, url, params, callBack);
    }


    @Override
    public ResponseStream sendSync(HttpRequest.HttpMethod method, String url) throws HttpException {
        return super.sendSync(method, url);
    }

    @Override
    public ResponseStream sendSync(HttpRequest.HttpMethod method, String url, RequestParams params) throws HttpException {
        return super.sendSync(method, url, params);
    }

    @Override
    public HttpHandler<File> download(String url, String target, RequestCallBack<File> callback) {
        return super.download(url, target, callback);
    }

    @Override
    public HttpHandler<File> download(String url, String target, boolean autoResume, RequestCallBack<File> callback) {
        return super.download(url, target, autoResume, callback);
    }

    @Override
    public HttpHandler<File> download(String url, String target, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return super.download(url, target, autoResume, autoRename, callback);
    }

    @Override
    public HttpHandler<File> download(String url, String target, RequestParams params, RequestCallBack<File> callback) {
        return super.download(url, target, params, callback);
    }

    @Override
    public HttpHandler<File> download(String url, String target, RequestParams params, boolean autoResume, RequestCallBack<File> callback) {
        return super.download(url, target, params, autoResume, callback);
    }

    @Override
    public HttpHandler<File> download(String url, String target, RequestParams params, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return super.download(url, target, params, autoResume, autoRename, callback);
    }

    @Override
    public HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target, RequestParams params, RequestCallBack<File> callback) {
        return super.download(method, url, target, params, callback);
    }

    @Override
    public HttpHandler<File> download(HttpRequest.HttpMethod method, String url, String target, RequestParams params, boolean autoResume, RequestCallBack<File> callback) {
        return super.download(method, url, target, params, autoResume, callback);
    }

}
