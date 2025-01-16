package com.magi.meta.remoting.common.model;

import com.magi.meta.commons.data.base.BaseModel;

/**
 * RemoteParam
 *
 * @author <a href="mailto:akagi@magi.com">akagi</a>
 * @version 1.0.0
 * @since 5/4/20 2:49 PM
 */
public class RemoteParam extends BaseModel {

    private static final long serialVersionUID = 3109924389899796918L;
    /**
     * 入参
     */
    private Object argument;

    public RemoteParam() {
        super();
    }

    public RemoteParam(Object argument) {
        this.argument = argument;
    }

    public Object getArgument() {
        return argument;
    }

    public void setArgument(Object argument) {
        this.argument = argument;
    }
}
