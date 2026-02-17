package com.softmegatron.shared.meta.extension.spring.config;

import com.softmegatron.shared.meta.extension.spring.factory.SpringExtensionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * Meta Extension Spring 自动配置
 * <p>
 * 自动注册 {@link SpringExtensionFactory}，使其能够从 Spring 容器获取扩展实例。
 * </p>
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ExtensionAutoConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SpringExtensionFactory springExtensionFactory() {
        return new SpringExtensionFactory();
    }
}
