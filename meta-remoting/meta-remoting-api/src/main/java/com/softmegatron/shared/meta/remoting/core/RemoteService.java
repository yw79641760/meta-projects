package com.softmegatron.shared.meta.remoting.core;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.remoting.core.model.RemoteInvocation;
import com.softmegatron.shared.meta.remoting.core.model.RemoteResponse;

/**
 * RemoteService
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 2:55 PM
 */
@Spi("http")
public interface RemoteService<I extends RemoteInvocation, R extends RemoteResponse> {

    /**
     * 远程调用
     * @param invocation
     * @return
     */
    R invoke(I invocation);
}
