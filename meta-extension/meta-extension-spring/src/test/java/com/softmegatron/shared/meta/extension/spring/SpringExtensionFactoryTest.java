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

    @Test
    public void testEmptyContexts() {
        SpringExtensionFactory.clearContexts();
        TestService service = factory.getExtension(TestService.class, "springService");
        assertNull(service);
        // 重新添加 context
        SpringExtensionFactory.addApplicationContext(context);
    }

    @Test
    public void testFactoryImplementsExtensionFactory() {
        assertTrue(factory instanceof ExtensionFactory);
    }

    @Test
    public void testMultipleBeansSameType() {
        // 测试多个同类型 Bean 的情况
        AnnotationConfigApplicationContext multiContext = new AnnotationConfigApplicationContext(MultiBeanConfig.class);
        SpringExtensionFactory.addApplicationContext(multiContext);
        
        try {
            // 按名称获取
            MultiService service1 = factory.getExtension(MultiService.class, "service1");
            assertNotNull(service1);
            assertEquals("service1", service1.getName());
            
            MultiService service2 = factory.getExtension(MultiService.class, "service2");
            assertNotNull(service2);
            assertEquals("service2", service2.getName());
        } finally {
            multiContext.close();
            SpringExtensionFactory.removeApplicationContext(multiContext);
        }
    }

    @Test
    public void testGetContextsReturnsModifiableSet() {
        int originalSize = SpringExtensionFactory.getContexts().size();
        SpringExtensionFactory.getContexts().clear();
        // 清空后验证
        assertTrue(SpringExtensionFactory.getContexts().isEmpty());
        // 恢复
        SpringExtensionFactory.addApplicationContext(context);
    }

    @Test
    public void testAddDuplicateContext() {
        int size = SpringExtensionFactory.getContexts().size();
        SpringExtensionFactory.addApplicationContext(context);
        assertEquals(size, SpringExtensionFactory.getContexts().size());
    }

    @Test
    public void testRemoveNonExistentContext() {
        AnnotationConfigApplicationContext otherContext = new AnnotationConfigApplicationContext();
        try {
            int size = SpringExtensionFactory.getContexts().size();
            SpringExtensionFactory.removeApplicationContext(otherContext);
            assertEquals(size, SpringExtensionFactory.getContexts().size());
        } finally {
            otherContext.close();
        }
    }

    @Test
    public void testSingletonBehaviorInSpring() {
        TestService service1 = factory.getExtension(TestService.class, "springService");
        TestService service2 = factory.getExtension(TestService.class, "springService");
        assertSame("Spring Bean 应该是单例", service1, service2);
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
     * 多 Bean 测试接口
     */
    public interface MultiService {
        String getName();
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
     * MultiService 实现1
     */
    public static class MultiServiceImpl1 implements MultiService {
        @Override
        public String getName() {
            return "service1";
        }
    }

    /**
     * MultiService 实现2
     */
    public static class MultiServiceImpl2 implements MultiService {
        @Override
        public String getName() {
            return "service2";
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

    /**
     * 多 Bean 测试配置
     */
    @Configuration
    static class MultiBeanConfig {
        @Bean
        public MultiService service1() {
            return new MultiServiceImpl1();
        }
        
        @Bean
        public MultiService service2() {
            return new MultiServiceImpl2();
        }
    }
}
