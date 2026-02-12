package com.softmegatron.shared.meta.commons.remoting.http.model;

import com.softmegatron.shared.meta.commons.remoting.core.model.RemoteParam;

/**
 * HttpRemoteParam
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:05 PM
 */
public class HttpRemoteParam extends RemoteParam {

    private static final long serialVersionUID = -4323172016331211688L;

    private String name;

    public HttpRemoteParam() {
        super();
    }

    public HttpRemoteParam(String name, Object argument) {
        super(argument);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static HttpRemoteParam ofData(String name, Object argument) {
        HttpRemoteParam result = new HttpRemoteParam();
        result.setName(name);
        result.setArgument(argument);
        return result;
    }
}
