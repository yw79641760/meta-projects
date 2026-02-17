package com.softmegatron.shared.meta.extension.spring.config;

import com.softmegatron.shared.meta.extension.spring.factory.SpringExtensionFactory;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.*;

/**
 * ExtensionAutoConfiguration 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ExtensionAutoConfigurationTest {

    @Test
    public void testAutoConfigurationRegistersFactory() {
        SpringExtensionFactory.clearContexts();
        
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ExtensionAutoConfiguration.class, TestConfig.class);
        
        try {
            SpringExtensionFactory factory = context.getBean(SpringExtensionFactory.class);
            assertNotNull(factory);
            assertEquals(0, factory.getOrder());
        } finally {
            context.close();
            SpringExtensionFactory.clearContexts();
        }
    }

    @Test
    public void testApplicationContextIsRegistered() {
        SpringExtensionFactory.clearContexts();
        
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ExtensionAutoConfiguration.class);
        
        try {
            assertTrue(SpringExtensionFactory.getContexts().contains(context));
        } finally {
            context.close();
            SpringExtensionFactory.clearContexts();
        }
    }

    @Test
    public void testMultipleContexts() {
        SpringExtensionFactory.clearContexts();
        
        AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(
                ExtensionAutoConfiguration.class);
        AnnotationConfigApplicationContext context2 = new AnnotationConfigApplicationContext(
                ExtensionAutoConfiguration.class);
        
        try {
            assertEquals(2, SpringExtensionFactory.getContexts().size());
            assertTrue(SpringExtensionFactory.getContexts().contains(context1));
            assertTrue(SpringExtensionFactory.getContexts().contains(context2));
        } finally {
            context1.close();
            context2.close();
            SpringExtensionFactory.clearContexts();
        }
    }

    @Test
    public void testConfigurationClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ExtensionAutoConfiguration.class.getModifiers()));
    }

    @Test
    public void testConfigurationClassHasConfigurationAnnotation() {
        assertNotNull(ExtensionAutoConfiguration.class.getAnnotation(Configuration.class));
    }

    @Test
    public void testSpringExtensionFactoryBeanMethod() throws NoSuchMethodException {
        var method = ExtensionAutoConfiguration.class.getMethod("springExtensionFactory");
        assertNotNull(method);
        assertNotNull(method.getAnnotation(Bean.class));
        assertEquals(SpringExtensionFactory.class, method.getReturnType());
    }

    @Configuration
    static class TestConfig {
        // 空配置用于测试
    }
}
