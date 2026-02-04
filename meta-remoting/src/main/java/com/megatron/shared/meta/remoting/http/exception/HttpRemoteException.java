package com.megatron.shared.meta.remoting.http.exception;

/**
 * HttpRemoteException
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:04 PM
 */
public class HttpRemoteException extends RuntimeException{

    private static final long serialVersionUID = -7511898403559950644L;

    public HttpRemoteException(String message) {
        super(message);
    }

    public HttpRemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRemoteException(Throwable cause) {
        super(cause);
    }

    public HttpRemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
