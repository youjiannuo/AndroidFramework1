package com.yn.framework.feedmission;


import com.yn.framework.controller.BackTask;
import com.yn.framework.controller.BaseController;
import com.yn.framework.http.HttpExecute;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.StringUtil;
import com.yn.framework.system.UrlUtil;


/**
 * 这个类主要是用来发送网络请求，装载一些需要发送的数据
 */
public class BaseFeedMission {

    protected BaseController mBaseController;
    protected HttpExecute mHttpExecute;

    public BaseFeedMission(BaseController baseController) {
        mBaseController = baseController;
        mHttpExecute = new HttpExecute();
    }

    public void sendMessage(HttpExecute.NetworkTask task) {
        task.call = mBaseController;
        if (!StringUtil.isURL(task.url)) {
            task.url = UrlUtil.getUrl(task.url);
        }
        task.context = mBaseController.mActivity;
        mHttpExecute.execute(task);
    }

    public void sendMessage(int param, String... values) {
        sendMessage(param, -1, values);
    }

    public void sendMessage(int param, int call, String... values) {
        sendMessage("", param, call, values);
    }

    public void sendMessage(String backMethod, int param, int call, String... values) {
        HttpExecute.NetworkTask task = getBackTask(backMethod, param, call, values);
        sendMessage(task);
    }

    public static HttpExecute.NetworkTask getBackTask(String backMethod, int param, int call, String... values) {
        String params[] = ContextManager.getArrayString(param);
        HttpExecute.NetworkTask task = new HttpExecute.NetworkTask();
        task.backTask = BackTask.build(call);
        task.backTask.method = backMethod;
        String cache = "";
        boolean isCacheAdd = false;
        for (int i = 0; i < params.length; i++) {
            String result = params[i];
            params[i] = params[i].toLowerCase();
            if (params[i].contains("url:")) {
                task.url = getString(result);
            } else if (params[i].contains("call:")) {
                try {
                    task.backTask.callInterface = Integer.parseInt(getString(result));
                } catch (Exception e) {
                    throw new NullPointerException("call must is integer");
                }
            } else if (params[i].toLowerCase().contains("key:")) {

                task.keys = getArray(result);
            } else if (params[i].toLowerCase().contains("value:")) {
                task.values = getArray(result);
            } else if (params[i].contains("method:") && StringUtil.isEmpty(task.backTask.method)) {
                task.backTask.method = getString(result);
            } else if (params[i].contains("http:")) {
                String method = getString(params[i]);
                if (method == null || method.length() == 0) continue;
                if (method.contains("get")) {
                    task.method = HttpExecute.METHOD_GET;
                } else if (method.contains("put")) {
                    task.method = HttpExecute.METHOD_PUT;
                } else if (method.contains("delete")) {
                    task.method = HttpExecute.METHOD_DELETE;
                } else if (method.contains("head")) {
                    task.method = HttpExecute.METHOD_HEAD;
                }
            } else if (params[i].contains("cache:")) {
                cache = getString(result);
            } else if (params[i].contains("cacheaddparam:")) {
                try {
                    isCacheAdd = Boolean.parseBoolean(getString(params[i]));
                } catch (Exception e) {
                    throw new NullPointerException("cacheaddparams must is false or true");
                }
            } else if (params[i].contains("char:")) {
                task.charSet = getString(getString(result));
            } else if (params[i].contains("istoast:")) {
                task.backTask.isToast = Boolean.parseBoolean(getString(params[i]));
            } else if (params[i].contains("isprogress")) {
                task.backTask.isProgress = Boolean.parseBoolean(getString(params[i]));
            }
        }


        if (task.values != null && task.keys != null && task.keys.length != 0 && values != null && values.length != 0) {
            String newValues[] = new String[task.keys.length];
            System.arraycopy(values, 0, newValues, 0, values.length);
            System.arraycopy(task.values, 0, newValues, values.length, task.values.length);

            task.values = newValues;
            values = null;
        }
        if (values != null && values.length != 0) {
            task.values = values;
        }

        if (isCacheAdd) {
            if (task.values != null) {
                for (int i = 0; i < task.values.length; i++) {
                    cache += task.values[i];
                }
            }
        }
        if (cache != null && cache.length() != 0) {
            task.backTask.cacheKey = task.url + cache;
        }

        return task;
    }

    private static String[] getArray(String s) {
        s = getString(s);
        if (s == null || s.length() == 0) return new String[0];
        return s.split(",");
    }

    private static String getString(String s) {
        int index = s.indexOf(":");
        if (index == -1) return null;
        return s.substring(index + 1, s.length());
    }
}
