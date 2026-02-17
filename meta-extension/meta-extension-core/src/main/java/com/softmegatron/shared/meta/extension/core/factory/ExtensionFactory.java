package com.softmegatron.shared.meta.extension.core.factory;

/**
 * 扩展工厂接口
 * <p>
 * 统一的扩展实例获取入口，支持多种来源：
 * <ul>
 *   <li>SPI 配置文件</li>
 *   <li>Spring 容器</li>
 *   <li>注册中心（远程服务代理）</li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ExtensionFactory {

    /**
     * 获取扩展实例
     *
     * @param type 扩展点接口类型
     * @param name 扩展名称
     * @param <T>  扩展点类型
     * @return 扩展实例，不存在则返回 null
     */
    <T> T getExtension(Class<T> type, String name);

    /**
     * 工厂优先级
     * <p>
     * 数值越小优先级越高，优先级高的工厂先被尝试。
     * </p>
     *
     * @return 优先级值，默认为 100
     */
    default int getOrder() {
        return 100;
    }
}
