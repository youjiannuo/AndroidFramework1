package com.yn.framework.system;

/**
 * Created by youjiannuo on 15/8/8.
 *
 * 主要是处理项目出现了异常错误需要执行的错误
 *
 *
 */
public interface OnUnCaughtExceptionListener {

    public void dispatchException();

}
