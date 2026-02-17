package com.softmegatron.shared.meta.extension.spring;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.factory.ExtensionFactory;
import com.softmegatron.shared.meta.extension.spring.factory.SpringExtensionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.*;

/**
 * SpringExtensionFactory 测试类
 */
public class SpringExtensionFactoryTest {

    private AnnotationConfigApplicationContext context;
    private SpringExtensionFactory factory;

    @Before
    public void setUp() {
        SpringExtensionFactory.clearContexts();
        factory = new SpringExtensionFactory();
        context = new AnnotationConfigApplicationContext(TestConfig.class);
        SpringExtensionFactory.addApplicationContext(context);
    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
        SpringExtensionFactory.clearContexts();
    }

    @Test
    public void testGetExtensionByName() {
        TestService service = factory.getExtension(TestService.class, "springService");
        assertNotNull(service);
        assertEquals("Hello from Spring", service.sayHello());
    }

    @Test
    public void testGetExtensionByType() {
        TestService service = factory.getExtension(TestService.class, null);
        assertNotNull(service);
        assertEquals("Hello from Spring", service.sayHello());
    }

    @Test
    public void testGetExtensionNotFound() {
        TestService service = factory.getExtension(TestService.class, "notExist");
        assertNotNull(service);
    }

    @Test
    public void testSpiInterfaceReturnsNull() {
        SpiInterface service = factory.getExtension(SpiInterface.class, "test");
        assertNull(service);
    }

    @Test
    public void testSpringExtensionFactoryOrder() {
        assertEquals(0, factory.getOrder());
    }

    @Test
    public void testAddApplicationContext() {
        assertTrue(SpringExtensionFactory.getContexts().contains(context));
    }

    @Test
    public void testRemoveApplicationContext() {
        SpringExtensionFactory.removeApplicationContext(context);
        assertFalse(SpringExtensionFactory.getContexts().contains(context));
    }

    @Test
    public void testIsInitialized() {
        assertTrue(SpringExtensionFactory.isInitialized());
    }

    @Test
    public void testClearContexts() {
        SpringExtensionFactory.clearContexts();
        assertTrue(SpringExtensionFactory.getContexts().isEmpty());
    }

    @Test
    public void testNullType() {
        TestService service = factory.getExtension(null, "springService");
        assertNull(service);
    }

    /**
     * 测试用扩展点接口（带 @Spi 注解）
     */
    @Spi("default")
    public interface SpiInterface {
        String sayHello();
    }

    /**
     * 测试用服务接口
     */
    public interface TestService {
        String sayHello();
    }

    /**
     * Spring Bean 实现
     */
    public static class SpringTestServiceImpl implements TestService {
        @Override
        public String sayHello() {
            return "Hello from Spring";
        }
    }

    /**
     * 测试配置
     */
    @Configuration
    static class TestConfig {
        @Bean
        public TestService springService() {
            return new SpringTestServiceImpl();
        }
    }
}
