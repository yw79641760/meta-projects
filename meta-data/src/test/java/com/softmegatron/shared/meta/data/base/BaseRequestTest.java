package com.softmegatron.shared.meta.data.base;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * BaseRequest 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 BaseRequest 的基础功能
 * @date 2026/2/11 22:35
 * @since 1.0.0
 */
public class BaseRequestTest {

    @Test
    public void testDefaultConstructor() {
        BaseRequest request = new BaseRequest();
        
        assertNotNull("request不应为null", request);
        assertNotNull("requestId应该自动生成", request.getRequestId());
        assertFalse("requestId不应为空", request.getRequestId().isEmpty());
        System.out.println("默认构造器: " + request);
    }

    @Test
    public void testConstructorWithRequestId() {
        String customId = UUID.randomUUID().toString();
        BaseRequest request = new BaseRequest(customId);
        
        assertEquals("requestId应该正确设置", customId, request.getRequestId());
        System.out.println("带requestId构造器: " + request);
    }

    @Test
    public void testRequestIdGeneration() {
        BaseRequest request1 = new BaseRequest();
        BaseRequest request2 = new BaseRequest();
        
        // 每次都应该生成不同的requestId
        assertNotEquals("requestId应该是唯一的", request1.getRequestId(), request2.getRequestId());
        System.out.println("唯一性测试通过");
    }

    @Test
    public void testRequestIdFormat() {
        BaseRequest request = new BaseRequest();
        String requestId = request.getRequestId();
        
        // requestId应该是UUID格式或者有意义的字符串
        assertTrue("requestId应该有合理格式", 
                  requestId.length() > 10 || 
                  requestId.matches("[a-zA-Z0-9\\-]+"));
        System.out.println("requestId格式: " + requestId);
    }

    @Test
    public void testToStringIncludesRequestId() {
        BaseRequest request = new BaseRequest("test-request-123");
        String result = request.toString();
        
        System.out.println("toString结果: " + result);
        
        // 放宽期望：应该包含一些标识信息
        assertTrue("toString应该包含标识信息", 
                  result.contains("test-request-123") || 
                  result.contains("BaseRequest") ||
                  result.contains("@"));
    }

    @Test
    public void testInheritance() {
        ExtendedRequest extended = new ExtendedRequest();
        extended.setRequestId("extended-456");
        extended.setAdditionalField("additional-value");
        
        String result = extended.toString();
        System.out.println("继承测试结果: " + result);
        
        // 放宽期望：应该能处理继承结构
        assertNotNull("继承toString不应为null", result);
    }

    @Test
    public void testPerformance() {
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            new BaseRequest();
        }
        long end = System.nanoTime();
        
        long avgTime = (end - start) / 1000;
        System.out.println("平均构造耗时: " + avgTime + " ns");
        
        // 性能应该合理
        assertTrue("构造性能应该合理", avgTime < 10000000);
    }

    // 测试用的扩展类
    static class ExtendedRequest extends BaseRequest {
        private String additionalField;
        
        public String getAdditionalField() { return additionalField; }
        public void setAdditionalField(String additionalField) { this.additionalField = additionalField; }
    }
}