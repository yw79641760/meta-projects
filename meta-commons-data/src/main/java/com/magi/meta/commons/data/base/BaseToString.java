package com.magi.meta.commons.data.base;

import java.io.Serializable;

/**
 * BaseToString
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 2019-04-22 23:25
 */
public abstract class BaseToString implements Serializable {

    private static final long serialVersionUID = 8445222833791659584L;

    @Override
    public String toString() {
        // TODO fastjson vs. jackson vs. apache commons
        return "BaseToString{}";
    }
}
