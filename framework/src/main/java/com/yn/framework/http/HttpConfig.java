package com.yn.framework.http;

import com.yn.framework.system.ContextManager;
import com.yn.framework.R;

/**
 * Created by youjiannuo on 16/5/17.
 */
public class HttpConfig {

    public static final String STATUS_KEY = ContextManager.getString(R.string.yn_from_service_class_status);

    public static final String TOKEN_FAIL_KEY = ContextManager.getString(R.string.yn_token_fail);

    public static final String DATA_KEY = ContextManager.getString(R.string.yn_service_class_right_data);

    public static final String ERROR_KEY = ContextManager.getString(R.string.yn_service_class_toast_error);

}
