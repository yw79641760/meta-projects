package com.softmegatron.shared.meta.commons.data.base;

import com.softmegatron.shared.meta.commons.data.enums.MessageCode;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * BaseResponse 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 BaseResponse 的泛型数据处理功能
 * @date 2026/2/11 22:35
 * @since 1.0.0
 */
public class BaseResponseTest {

    // 测试用的数据类
    private static class TestData {
        private String name;
        private Integer value;

        public TestData() {}

        public TestData(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getValue() { return value; }
        public void setValue(Integer value) { this.value = value; }
    }

    @Test
    public void testDefaultConstructor() {
        BaseResponse<TestData> response = new BaseResponse<>();
        
        assertNotNull("response不应为null", response);
        assertNull("默认data应该是null", response.getData());
        assertTrue("默认应该成功", response.isSuccess());
        System.out.println("默认BaseResponse: " + response);
    }

    @Test
    public void testConstructorWithData() {
        TestData testData = new TestData("测试数据", 123);
        String requestId = "DATA-TEST-ID";
        
        BaseResponse<TestData> response = new BaseResponse<>(requestId, testData);
        
        assertEquals("requestId应该正确", requestId, response.getRequestId());
        assertEquals("data应该正确", testData, response.getData());
        System.out.println("带数据的响应: " + response);
    }

    @Test
    public void testFullConstructor() {
        TestData testData = new TestData("完整测试", 456);
        String requestId = "FULL-DATA-ID";
        boolean success = false;
        String code = "DATA_ERROR";
        String message = "数据错误";
        HashMap<String, Object> feature = new HashMap<>();
        feature.put("dataType", "test");
        
        BaseResponse<TestData> response = new BaseResponse<>(requestId, success, code, message, feature, testData);
        
        assertEquals("requestId应该正确", requestId, response.getRequestId());
        assertEquals("data应该正确", testData, response.getData());
        assertEquals("success应该正确", success, response.isSuccess());
        assertEquals("code应该正确", code, response.getCode());
        assertEquals("message应该正确", message, response.getMessage());
        assertEquals("feature应该正确", feature, response.getFeature());
        System.out.println("完整构造响应: " + response);
    }

    @Test
    public void testReturnCodeConstructor() {
        BaseResponse<TestData> response = new BaseResponse<>(MessageCode.SUCCESSFUL);
        
        assertTrue("应该成功", response.isSuccess());
        assertEquals("code应该正确", MessageCode.SUCCESSFUL.getCode(), response.getCode());
        assertEquals("message应该正确", MessageCode.SUCCESSFUL.getDesc(), response.getMessage());
        assertNull("data应该是null", response.getData());
        System.out.println("ReturnCode构造响应: " + response);
    }

    @Test
    public void testBuilderPattern() {
        TestData testData = new TestData("Builder数据", 789);
        
        BaseResponse<TestData> response = new BaseResponse.Builder<TestData>()
                .requestId("BUILDER-DATA-ID")
                .success(true)
                .code("BUILD_DATA_CODE")
                .message("构建数据消息")
                .feature(Collections.singletonMap("builder", "data"))
                .data(testData)
                .build();
        
        assertEquals("requestId应该正确", "BUILDER-DATA-ID", response.getRequestId());
        assertEquals("data应该正确", testData, response.getData());
        assertTrue("应该成功", response.isSuccess());
        assertEquals("code应该正确", "BUILD_DATA_CODE", response.getCode());
        assertEquals("message应该正确", "构建数据消息", response.getMessage());
        assertNotNull("feature不应该为null", response.getFeature());
        System.out.println("Builder构建响应: " + response);
    }

    @Test
    public void testBuilderSuccessShortcut() {
        TestData testData = new TestData("成功数据", 999);
        
        BaseResponse<TestData> response = new BaseResponse.Builder<TestData>()
                .success()
                .data(testData)
                .build();
        
        assertTrue("应该成功", response.isSuccess());
        assertEquals("data应该正确", testData, response.getData());
        assertEquals("code应该是SUCCESSFUL", MessageCode.SUCCESSFUL.getCode(), response.getCode());
        System.out.println("Builder成功快捷方式: " + response);
    }

    @Test
    public void testNullDataHandling() {
        BaseResponse<TestData> response = new BaseResponse<>();
        response.setData(null);
        
        assertNull("data可以设置为null", response.getData());
        
        // 测试构造函数接受null数据
        BaseResponse<TestData> response2 = new BaseResponse<>("NULL-TEST", null);
        assertNull("构造函数应该接受null数据", response2.getData());
    }

    @Test
    public void testDataSetterGetter() {
        BaseResponse<TestData> response = new BaseResponse<>();
        TestData testData = new TestData("设置测试", 111);
        
        response.setData(testData);
        assertEquals("data应该正确获取", testData, response.getData());
        
        // 测试重新设置
        TestData newData = new TestData("新数据", 222);
        response.setData(newData);
        assertEquals("data应该被更新", newData, response.getData());
    }

    @Test
    public void testGenericTypeSafety() {
        // 测试不同类型的数据
        BaseResponse<String> stringResponse = new BaseResponse<>();
        stringResponse.setData("字符串数据");
        assertEquals("字符串数据应该正确", "字符串数据", stringResponse.getData());
        
        BaseResponse<Integer> intResponse = new BaseResponse<>();
        intResponse.setData(999);
        assertEquals("整数数据应该正确", Integer.valueOf(999), intResponse.getData());
        
        System.out.println("字符串响应: " + stringResponse);
        System.out.println("整数响应: " + intResponse);
    }

    @Test
    public void testSerialVersionUID() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = BaseResponse.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        Long uid = (Long) field.get(null);
        
        assertEquals("serialVersionUID应该正确", -6533323665813662409L, uid.longValue());
    }

    @Test
    public void testInheritanceFromSimpleResponse() {
        BaseResponse<TestData> response = new BaseResponse<>();
        assertTrue("BaseResponse应该继承SimpleResponse", response instanceof SimpleResponse);
        assertTrue("BaseResponse应该继承BaseModel", response instanceof BaseModel);
    }

    @Test
    public void testToStringIncludesData() {
        TestData testData = new TestData("ToString测试", 333);
        BaseResponse<TestData> response = new BaseResponse<>("TO_STRING_TEST", testData);
        
        String result = response.toString();
        assertNotNull("toString结果不应为null", result);
        assertTrue("应该包含数据信息", result.contains("ToString测试") || result.contains("TestData"));
        System.out.println("包含数据的toString: " + result);
    }
}