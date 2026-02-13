package com.softmegatron.shared.meta.commons.extension.exception;

/**
 * ExtensionException
 * 
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @date 2026/02/13 18:29
 * @since 1.0.0
 */
public class ExtensionException extends RuntimeException {

    public ExtensionException() {
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
