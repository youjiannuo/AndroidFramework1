package com.yn.framework.http;


import android.content.Context;

import com.yn.framework.controller.BackTask;
import com.yn.framework.data.JSON;
import com.yn.framework.data.MyGson;
import com.yn.framework.data.YNSharedPreferences;
import com.yn.framework.exception.YNVisitNetworkFailException;
import com.yn.framework.exception.YNVisitNetworkSuccessException;
import com.yn.framework.exception.YNVisitTokenFailureException;
import com.yn.framework.model.BackDataModel;
import com.yn.framework.remind.ToastUtil;
import com.yn.framework.system.BuildConfig;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.MethodUtil;
import com.yn.framework.system.SystemUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yn.framework.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HttpExecute {

    //post请求
    public static HttpRequest.HttpMethod METHOD_POST = HttpRequest.HttpMethod.POST;
    //get请求
    public static final HttpRequest.HttpMethod METHOD_GET = HttpRequest.HttpMethod.GET;
    //PUT请求
    public static final HttpRequest.HttpMethod METHOD_PUT = HttpRequest.HttpMethod.PUT;
    //DELETE请求
    public static final HttpRequest.HttpMethod METHOD_DELETE = HttpRequest.HttpMethod.DELETE;
    //OPTIONS
    public static final HttpRequest.HttpMethod METHOD_OPTIONS = HttpRequest.HttpMethod.OPTIONS;
    //HEAD
    public static final HttpRequest.HttpMethod METHOD_HEAD = HttpRequest.HttpMethod.HEAD;
    //TRACE
    public static final HttpRequest.HttpMethod METHOD_TRACE = HttpRequest.HttpMethod.TRACE;
    //CONNECT
    public static final HttpRequest.HttpMethod METHOD_CONNECT = HttpRequest.HttpMethod.CONNECT;
    //copy
    public static final HttpRequest.HttpMethod METHOD_COPY = HttpRequest.HttpMethod.COPY;

    NetworkTask mTask = null;

    public void execute(NetworkTask task) {
        if (task == null) {
            throw new NullPointerException("NetworkTask is not null");
        }
        try {
            if (!HttpManager.HTTP_MANAGER.checkoutHttp(task)) return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTask = task;
        //判断当前是否有可用
        if (!SystemUtil.isNetworkAvailable()) {
            ToastUtil.showFailMessage("当前网络不可用，请检查网络设置");
            mTask.call.getCache(mTask.backTask);
            mTask.call.visitNetworkFail("", mTask.backTask);
            return;
        }

        String result = "\nurl = " + task.url + "\n";

        RequestParams params = null;
        if (task.method == METHOD_GET) {
            getGetParams();
        } else {
            params = new RequestParams();
            if (mTask.keys != null && mTask.values != null && mTask.keys.length != 0 && mTask.values.length != 0) {
                if (mTask.keys.length != mTask.values.length) {
                    throw new ArrayIndexOutOfBoundsException("task.key的长度和task.value的长度不等");
                }
                for (int i = 0; i < mTask.keys.length; i++) {
                    params.addBodyParameter(mTask.keys[i], mTask.values[i]);
                    result += mTask.keys[i] + "=" + mTask.values[i] + "\n";
                }
            } else if (mTask.params != null) {
                List<String> keys = new ArrayList<>();
                List<String> values = new ArrayList<>();
                MethodUtil.getParams(mTask.params, keys, values);
                for (int i = 0; i < keys.size(); i++) {
                    params.addBodyParameter(keys.get(i), values.get(i));
                    result += keys.get(i) + "=" + values.get(i) + "\n";
                }
            }
            SystemUtil.printlnInfo(result);
        }

        if (params == null) {
            new Http(ContextManager.getContext()).send(HttpRequest.HttpMethod.GET, task.url, getBackCall());
        } else {
            new Http(ContextManager.getContext()).send(mTask.method, task.url, params, getBackCall());
        }
    }


    public RequestCallBack<String> getBackCall() {
        if (mTask.call != null) {
            mTask.call.visitNetworkStart(mTask.backTask);
        }
        return new com.lidroid.xutils.http.callback.RequestCallBack<String>() {

            @Override
            public void onCancelled() {
                super.onCancelled();
                if (mTask.call != null) {
                    mTask.call.visitNetworkCancel(mTask.backTask);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                super.onFailure(error, msg);
                if (mTask.call != null) {
                    if (mTask.backTask != null && mTask.backTask.isToast) {
                        ToastUtil.showFailMessage(R.string.hfh_network_shutdown);
                    }
                    mTask.call.visitNetworkFail(msg, mTask.backTask);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SystemUtil.printlnInfo("\nurl = " + mTask.url + "\t\t\n获取数据:" + responseInfo.result);
                try {
                    if (mTask.call.visitAllNetworkSuccess(responseInfo.result, null)) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSON json = new JSON(responseInfo.result);

                int status = -1;
                try {
                    status = Integer.parseInt(json.getString(HttpConfig.STATUS_KEY));
                } catch (Exception e) {
                    e.printStackTrace();
                    //数据异常
                    if (!BuildConfig.ENVIRONMENT) {
                        if (mTask.backTask != null && mTask.backTask.isToast) {
                            ToastUtil.showFailMessage("服务器数据异常");
                        }
                        String key = "url = " + mTask.url + "\n result = " + responseInfo.result + "\n" + new Date().toLocaleString() + "\n";
                        key += YNSharedPreferences.getInfo("erro", "erro");
                        YNSharedPreferences.saveInfo("erro", key, "erro");
                    }
                }

                if (isRightStatue(status)) {
                    try {
                        String data = json.getString(HttpConfig.DATA_KEY);
                        if ("{}".equals(data)) {
                            data = "";
                        }
                        mTask.call.visitNetworkSuccess(data, mTask.backTask);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new YNVisitNetworkSuccessException(e).throwException();
                    }

                } else if (Integer.parseInt(HttpConfig.TOKEN_FAIL_KEY) == status) {
                    //失效token
                    try {
                        mTask.call.visitTokenFailure(mTask);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new YNVisitTokenFailureException(e).throwException();
                    }
                } else {
                    String error = json.getStrings(HttpConfig.ERROR_KEY);
                    if (mTask.backTask != null && mTask.backTask.isToast) {
                        ToastUtil.showNormalMessage(error);
                    }
                    try {
                        mTask.call.visitNetworkFail(new MyGson().fromJson(responseInfo.result, BackDataModel.class), mTask.backTask);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new YNVisitNetworkFailException(e).throwException();
                    }
                }

            }
        };
    }

    private boolean isRightStatue(int statue) {
        String text = ContextManager.getString(R.string.yn_status_right);
        try {
            int a = Integer.getInteger(text);
            return a == statue;
        } catch (Exception e) {
        }

        if (text.contains(">")) {
            try {
                int a = Integer.parseInt(text.substring(1, text.length()));
                return statue > a;
            } catch (Exception e) {
                throw new NullPointerException("string.xml of yn_status_right must > number or number");
            }
        }

        return false;
    }

    public static class NetworkTask {
        public HttpRequest.HttpMethod method = METHOD_POST;
        public String url = null;
        public HttpVisitCallBack call = null;
        public String charSet = "UTF-8";
        public String keys[];
        public String values[];
        public Object params;
        public List<Head> head = null;
        public BackTask backTask;
        public Context context;
        public boolean notCheckout = false;
    }


    private void getGetParams() {
        if (mTask.keys == null || mTask.values == null) {
            if (mTask.params != null) {
                List<String> keys = new ArrayList<>();
                List<String> values = new ArrayList<>();
                MethodUtil.getParams(mTask.params, keys, values);
                mTask.keys = new String[keys.size()];
                mTask.values = new String[values.size()];
                keys.toArray(mTask.keys);
                values.toArray(mTask.values);
            }
        }

        if (mTask.keys != null && mTask.values != null && mTask.keys.length != 0 && mTask.values.length != 0) {
            if (mTask.keys.length != mTask.values.length) {
                throw new ArrayIndexOutOfBoundsException("task.key的长度和task.value的长度不等");
            }
            mTask.url += "?";
            mTask.url += mTask.keys[0] + "=" + mTask.values[0];
            for (int i = 1; i < mTask.keys.length; i++) {
                mTask.url += "&" + mTask.keys[i] + "=" + mTask.values[i];
                SystemUtil.printlnInfo(mTask.keys[i] + "=" + mTask.values[i]);
            }
        }
    }


    public static class Head {
        private String name;
        private String values;

        public Head(String name, String value) {
            setName(name);
            setValues(value);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }


    }

}
