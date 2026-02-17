package com.softmegatron.shared.meta.extension.annotation;

import com.softmegatron.shared.meta.extension.enums.ExtensionScope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SPI 扩展点标注
 * <p>
 * 标注在接口上，表示该接口是一个可扩展的 SPI 扩展点。
 * 扩展实现通过配置文件声明，默认路径为 META-INF/extensions/。
 * </p>
 *
 * <pre>
 * 示例配置文件 (META-INF/extensions/com.example.MyService):
 * http=com.example.HttpServiceImpl
 * dubbo=com.example.DubboServiceImpl
 * </pre>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Spi {

    /**
     * 默认扩展实现的键值
     *
     * @return 默认扩展键值，空字符串表示无默认值
     */
    String value() default "";

    /**
     * 扩展配置文件路径
     *
     * @return 配置文件所在目录，默认为 META-INF/extensions
     */
    String path() default "META-INF/extensions";

    /**
     * 实例作用域
     * <ul>
     *   <li>SINGLETON: 单例模式，全局共享一个实例</li>
     *   <li>PROTOTYPE: 多例模式，每次获取创建新实例</li>
     * </ul>
     *
     * @return 作用域，默认为单例
     */
    ExtensionScope scope() default ExtensionScope.SINGLETON;
}
