package com.megatron.shared.meta.commons.utils.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.List;

/**
 * LoggingUtils
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/3/20 9:52 PM
 */
public class LoggingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingUtils.class);

    private static final String ARG_SEPARATOR = "|";

    private LoggingUtils() {
    }

    /**
     * 拼接日志内容
     *
     * @param message
     * @param args
     * @return
     */
    public static String build(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        try {
            return MessageFormatter.arrayFormat(message, args).getMessage();
        } catch (Exception e) {
            List<Object> argsList = Arrays.asList(args);
            LOGGER.error("Failed to format message. [message={}][args={}]",
                         message, StringUtils.join(argsList, ARG_SEPARATOR), e);
            return message + ", args:[" + StringUtils.join(argsList, ARG_SEPARATOR) + "]";
        }
    }
}
