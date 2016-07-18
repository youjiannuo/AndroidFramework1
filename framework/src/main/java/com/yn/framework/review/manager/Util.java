package com.yn.framework.review.manager;

import android.view.View;

import com.yn.framework.review.OnCheckParams;
import com.yn.framework.review.model.ReplaceModel;
import com.yn.framework.system.StringUtil;

/**
 * Created by youjiannuo on 16/4/26.
 */
public class Util {

    public static OnCheckParams[] getClickTextViews(View view, String mViewIds) {
        String ids[] = mViewIds.split(",");
        OnCheckParams textViews[] = new OnCheckParams[ids.length];
        for (int i = 0; i < ids.length; i++) {
            textViews[i] = (OnCheckParams) view.findViewById(YNResourceUtil.getId(ids[i]));
        }
        return textViews;
    }

    public static int getInt(String result) {
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {

        }
        return 0;

    }

    public static ReplaceModel[] getCodeReplace(String code) {
        ReplaceModel params[] = new ReplaceModel[0];
        if (!StringUtil.isEmpty(code)) {
            String a[] = code.split(",");
            if (a.length != 2) {
                throw new NullPointerException("app:replace please input right code ,for example old:A,replace:B");
            }
            params = new ReplaceModel[2];
            params[0] = new ReplaceModel(getParam(a[0]), 0);
            params[1] = new ReplaceModel(getParam(a[1]), (a[1].contains("replace all:") ? 1 : 0));
        }

        return params;
    }

    public static String getParam(String param) {
        int a = param.indexOf(":");
        if (a == -1) return param;
        return param.substring(a + 1, param.length());
    }

}
