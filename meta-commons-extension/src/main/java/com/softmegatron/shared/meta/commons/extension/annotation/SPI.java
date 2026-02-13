package com.softmegatron.shared.meta.commons.extension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.softmegatron.shared.meta.commons.extension.constants.ExtensionConstants.DEFAULT_SOURCE_DIRECTORY;

/**
 * SPI 标注扩展点接口
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 1:44 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Spi {
    /**
     * 默认扩展点键值
     *
     * @return
     */
    String value() default "";

    /**
     * 扩展配置扫描路径
     *
     * @return
     */
    String path() default DEFAULT_SOURCE_DIRECTORY;
}
