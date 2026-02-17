package com.softmegatron.shared.meta.extension.spring.factory;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.factory.ExtensionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring 扩展工厂
 * <p>
 * 从 Spring 容器获取扩展实例。支持多个 ApplicationContext，
 * 会依次尝试每个容器直到找到对应的 Bean。
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpringExtensionFactory implements ExtensionFactory, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringExtensionFactory.class);

    /** 已注册的 ApplicationContext 集合 */
    private static final Set<ApplicationContext> CONTEXTS = ConcurrentHashMap.newKeySet();

    private static volatile boolean initialized = false;

    public SpringExtensionFactory() {
        initialized = true;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (context != null) {
            addApplicationContext(context);
        }
    }

    /**
     * 添加 ApplicationContext
     *
     * @param context Spring 应用上下文
     */
    public static void addApplicationContext(ApplicationContext context) {
        if (context != null && CONTEXTS.add(context)) {
            LOGGER.info("Added ApplicationContext: {}", context.getDisplayName());
        }
    }

    /**
     * 移除 ApplicationContext
     *
     * @param context Spring 应用上下文
     */
    public static void removeApplicationContext(ApplicationContext context) {
        if (context != null && CONTEXTS.remove(context)) {
            LOGGER.info("Removed ApplicationContext: {}", context.getDisplayName());
        }
    }

    /**
     * 清除所有 ApplicationContext（用于测试）
     */
    public static void clearContexts() {
        CONTEXTS.clear();
    }

    /**
     * 获取所有 ApplicationContext
     *
     * @return ApplicationContext 集合
     */
    public static Set<ApplicationContext> getContexts() {
        return CONTEXTS;
    }

    /**
     * 是否已初始化
     */
    public static boolean isInitialized() {
        return initialized;
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type == null || CONTEXTS.isEmpty()) {
            return null;
        }

        // SPI 接口交给 SpiExtensionFactory 处理
        if (type.isInterface() && type.isAnnotationPresent(Spi.class)) {
            return null;
        }

        for (ApplicationContext context : CONTEXTS) {
            try {
                // 优先按名称获取
                if (name != null && !name.isEmpty()) {
                    T bean = context.getBean(name, type);
                    if (bean != null) {
                        return bean;
                    }
                }
            } catch (NoSuchBeanDefinitionException e) {
                // 按名称未找到，尝试按类型获取
            } catch (BeansException e) {
                LOGGER.debug("Failed to get bean by name [{}] from context [{}]: {}",
                        name, context.getDisplayName(), e.getMessage());
            }

            // 按类型获取
            try {
                T bean = context.getBean(type);
                if (bean != null) {
                    return bean;
                }
            } catch (NoSuchBeanDefinitionException e) {
                // 继续尝试下一个 context
            } catch (BeansException e) {
                LOGGER.debug("Failed to get bean by type [{}] from context [{}]: {}",
                        type.getName(), context.getDisplayName(), e.getMessage());
            }
        }

        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
