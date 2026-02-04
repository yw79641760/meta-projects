package com.softmegatron.shared.meta.remoting.common;

import com.softmegatron.shared.meta.extension.annotation.SPI;
import com.softmegatron.shared.meta.remoting.common.model.RemoteInvocation;
import com.softmegatron.shared.meta.remoting.common.model.RemoteResponse;

/**
 * RemoteService
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:55 PM
 */
@SPI("http")
public interface RemoteService<I extends RemoteInvocation, R extends RemoteResponse> {

    /**
     * 远程调用
     * @param invocation
     * @return
     */
    R invoke(I invocation);
}
