package com.yn.framework.model;

import java.util.List;

/**
 * Created by youjiannuo on 16/1/13.
 */
public class BaseExpandListViewModel<P, C> {

    private P p;

    private List<C> c;

    public P getP() {
        return p;
    }

    public void setP(P p) {
        this.p = p;
    }

    public List<C> getC() {
        return c;
    }

    public void setC(List<C> c) {
        this.c = c;
    }
}
