package com.softmegatron.shared.meta.commons.data.base;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PageRequest 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 PageRequest 的分页请求参数管理
 * @date 2026/2/11 22:35
 * @since 1.0.0
 */
public class PageRequestTest {

    @Test
    public void testDefaultConstructor() {
        PageRequest pageRequest = new PageRequest();
        
        assertNotNull("pageRequest不应为null", pageRequest);
        assertEquals("默认当前页应该是1", Integer.valueOf(1), pageRequest.getCurrentPage());
        assertEquals("默认页大小应该是10", Integer.valueOf(10), pageRequest.getPageSize());
        assertNotNull("应该有requestId", pageRequest.getRequestId());
        System.out.println("默认PageRequest: " + pageRequest);
    }

    @Test
    public void testParameterizedConstructor() {
        Integer currentPage = 3;
        Integer pageSize = 20;
        
        PageRequest pageRequest = new PageRequest(currentPage, pageSize);
        
        assertEquals("当前页应该正确", currentPage, pageRequest.getCurrentPage());
        assertEquals("页大小应该正确", pageSize, pageRequest.getPageSize());
        assertNotNull("应该有requestId", pageRequest.getRequestId());
        System.out.println("参数化构造PageRequest: " + pageRequest);
    }

    @Test
    public void testSetterGetter() {
        PageRequest pageRequest = new PageRequest();
        
        pageRequest.setCurrentPage(5);
        pageRequest.setPageSize(50);
        
        assertEquals("当前页应该正确设置", Integer.valueOf(5), pageRequest.getCurrentPage());
        assertEquals("页大小应该正确设置", Integer.valueOf(50), pageRequest.getPageSize());
    }

    @Test
    public void testNullValues() {
        PageRequest pageRequest = new PageRequest();
        
        pageRequest.setCurrentPage(null);
        pageRequest.setPageSize(null);
        
        assertNull("当前页可以设置为null", pageRequest.getCurrentPage());
        assertNull("页大小可以设置为null", pageRequest.getPageSize());
    }

    @Test
    public void testZeroAndNegativeValues() {
        PageRequest pageRequest = new PageRequest();
        
        pageRequest.setCurrentPage(0);
        pageRequest.setPageSize(0);
        
        assertEquals("当前页0应该被接受", Integer.valueOf(0), pageRequest.getCurrentPage());
        assertEquals("页大小0应该被接受", Integer.valueOf(0), pageRequest.getPageSize());
        
        pageRequest.setCurrentPage(-1);
        pageRequest.setPageSize(-5);
        
        assertEquals("负数当前页应该被接受", Integer.valueOf(-1), pageRequest.getCurrentPage());
        assertEquals("负数页大小应该被接受", Integer.valueOf(-5), pageRequest.getPageSize());
    }

    @Test
    public void testLargeValues() {
        PageRequest pageRequest = new PageRequest();
        
        Integer largePage = 999999;
        Integer largeSize = 10000;
        
        pageRequest.setCurrentPage(largePage);
        pageRequest.setPageSize(largeSize);
        
        assertEquals("大数值当前页应该正确", largePage, pageRequest.getCurrentPage());
        assertEquals("大数值页大小应该正确", largeSize, pageRequest.getPageSize());
    }

    @Test
    public void testInheritanceFromBaseRequest() {
        PageRequest pageRequest = new PageRequest();
        
        assertTrue("PageRequest应该继承BaseRequest", pageRequest instanceof BaseRequest);
        assertTrue("PageRequest应该继承BaseModel", pageRequest instanceof BaseModel);
        assertTrue("PageRequest应该实现Serializable", pageRequest instanceof java.io.Serializable);
    }

    @Test
    public void testSerialVersionUID() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = PageRequest.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        Long uid = (Long) field.get(null);
        
        assertEquals("serialVersionUID应该正确", 8943673630842445243L, uid.longValue());
    }

    @Test
    public void testToStringIncludesPaginationParams() {
        PageRequest pageRequest = new PageRequest(2, 15);
        String result = pageRequest.toString();
        
        assertNotNull("toString结果不应为null", result);
        assertTrue("应该包含当前页信息", result.contains("currentPage=2"));
        assertTrue("应该包含页大小信息", result.contains("pageSize=15"));
        assertTrue("应该包含requestId", result.contains("requestId"));
        System.out.println("toString结果: " + result);
    }

    @Test
    public void testRequestIdGeneration() {
        PageRequest request1 = new PageRequest();
        PageRequest request2 = new PageRequest();
        
        assertNotNull("requestId不应为null", request1.getRequestId());
        assertNotNull("requestId不应为null", request2.getRequestId());
        assertNotEquals("不同的请求应该有不同的requestId", 
                       request1.getRequestId(), 
                       request2.getRequestId());
        
        System.out.println("请求1 ID: " + request1.getRequestId());
        System.out.println("请求2 ID: " + request2.getRequestId());
    }

    @Test
    public void testBuilderPatternCompatibility() {
        // 验证PageRequest可以与BaseRequest的builder模式兼容使用
        PageRequest pageRequest = new PageRequest();
        pageRequest.setRequestId("BUILDER-COMPATIBLE");
        pageRequest.setCurrentPage(3);
        pageRequest.setPageSize(25);
        
        assertEquals("requestId应该正确", "BUILDER-COMPATIBLE", pageRequest.getRequestId());
        assertEquals("当前页应该正确", Integer.valueOf(3), pageRequest.getCurrentPage());
        assertEquals("页大小应该正确", Integer.valueOf(25), pageRequest.getPageSize());
        System.out.println("Builder兼容性测试: " + pageRequest);
    }

    @Test
    public void testPerformanceWithMultipleInstances() {
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            new PageRequest(i, i * 10);
        }
        long endTime = System.nanoTime();
        
        long avgTime = (endTime - startTime) / 1000;
        System.out.println("创建1000个PageRequest平均耗时: " + avgTime + " ns");
        
        // 性能应该合理
        assertTrue("创建性能应该合理", avgTime < 50000000); // 50ms以内
    }
}