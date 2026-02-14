package com.softmegatron.shared.meta.data.base;

import com.softmegatron.shared.meta.data.enums.MessageCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * PageResponse 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 PageResponse 的分页响应处理功能
 * @date 2026/2/11 22:35
 * @since 1.0.0
 */
public class PageResponseTest {

    // 测试用的实体类
    private static class TestEntity {
        private String name;
        private Integer id;

        public TestEntity() {}

        public TestEntity(String name, Integer id) {
            this.name = name;
            this.id = id;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
    }

    @Test
    public void testDefaultConstructor() {
        PageResponse<TestEntity> response = new PageResponse<>();
        
        assertNotNull("response不应为null", response);
        assertNull("默认data应该是null", response.getData());
        assertTrue("默认应该成功", response.isSuccess());
        System.out.println("默认PageResponse: " + response);
    }

    @Test
    public void testConstructorWithListAndPageInfo() {
        List<TestEntity> dataList = Arrays.asList(
            new TestEntity("实体1", 1),
            new TestEntity("实体2", 2)
        );
        PageInfo pageInfo = new PageInfo(1, 10, 20, 2);
        String requestId = "LIST-PAGE-TEST";
        
        PageResponse<TestEntity> response = new PageResponse<>(requestId, dataList, pageInfo);
        
        assertEquals("requestId应该正确", requestId, response.getRequestId());
        assertNotNull("data不应该为null", response.getData());
        assertEquals("数据列表大小应该正确", 2, response.getData().getListInfo().size());
        assertEquals("分页信息应该正确", pageInfo, response.getData().getPageInfo());
        System.out.println("列表+分页信息构造: " + response);
    }

    @Test
    public void testConstructorWithPageData() {
        List<TestEntity> dataList = Arrays.asList(
            new TestEntity("页面数据实体", 100)
        );
        PageInfo pageInfo = PageInfo.ofData(2, 5, 15, 1);
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        String requestId = "PAGE-DATA-TEST";
        
        PageResponse<TestEntity> response = new PageResponse<>(requestId, pageData);
        
        assertEquals("requestId应该正确", requestId, response.getRequestId());
        assertEquals("pageData应该正确", pageData, response.getData());
        System.out.println("PageData构造: " + response);
    }

    @Test
    public void testFullConstructor() {
        List<TestEntity> dataList = Arrays.asList(new TestEntity("完整测试", 999));
        PageInfo pageInfo = PageInfo.ofData(1, 10, 100, 1);
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        
        PageResponse<TestEntity> response = new PageResponse<>(
            "FULL-CONSTRUCTOR-TEST",
            true,
            "CUSTOM_CODE",
            "自定义消息",
            Collections.singletonMap("custom", "value"),
            pageData
        );
        
        assertEquals("requestId应该正确", "FULL-CONSTRUCTOR-TEST", response.getRequestId());
        assertTrue("应该成功", response.isSuccess());
        assertEquals("code应该正确", "CUSTOM_CODE", response.getCode());
        assertEquals("message应该正确", "自定义消息", response.getMessage());
        assertEquals("pageData应该正确", pageData, response.getData());
        assertNotNull("feature不应该为null", response.getFeature());
        System.out.println("完整构造函数: " + response);
    }

    @Test
    public void testReturnCodeConstructor() {
        PageResponse<TestEntity> response = new PageResponse<>(MessageCode.SUCCESSFUL);
        
        assertTrue("应该成功", response.isSuccess());
        assertEquals("code应该正确", MessageCode.SUCCESSFUL.getCode(), response.getCode());
        assertEquals("message应该正确", MessageCode.SUCCESSFUL.getDesc(), response.getMessage());
        assertNull("data应该是null", response.getData());
        System.out.println("ReturnCode构造: " + response);
    }

    @Test
    public void testBuilderPattern() {
        List<TestEntity> dataList = Arrays.asList(
            new TestEntity("Builder实体1", 1),
            new TestEntity("Builder实体2", 2)
        );
        PageInfo pageInfo = PageInfo.ofData(1, 10, 20, 2);
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        
        PageResponse<TestEntity> response = new PageResponse.Builder<TestEntity>()
                .requestId("BUILDER-PAGE-ID")
                .success(true)
                .code("PAGE_BUILD_CODE")
                .message("分页构建消息")
                .feature(Collections.singletonMap("page", "builder"))
                .data(pageData)
                .build();
        
        assertEquals("requestId应该正确", "BUILDER-PAGE-ID", response.getRequestId());
        assertEquals("data应该正确", pageData, response.getData());
        assertTrue("应该成功", response.isSuccess());
        assertEquals("code应该正确", "PAGE_BUILD_CODE", response.getCode());
        assertEquals("message应该正确", "分页构建消息", response.getMessage());
        System.out.println("Builder模式: " + response);
    }

    @Test
    public void testBuilderSuccessShortcut() {
        List<TestEntity> dataList = Arrays.asList(new TestEntity("成功实体", 123));
        PageInfo pageInfo = PageInfo.EMPTY;
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        
        PageResponse<TestEntity> response = new PageResponse.Builder<TestEntity>()
                .success()
                .data(pageData)
                .build();
        
        assertTrue("应该成功", response.isSuccess());
        assertEquals("data应该正确", pageData, response.getData());
        assertEquals("code应该是SUCCESSFUL", MessageCode.SUCCESSFUL.getCode(), response.getCode());
        System.out.println("Builder成功快捷方式: " + response);
    }

    @Test
    public void testNullDataHandling() {
        PageResponse<TestEntity> response = new PageResponse<>();
        response.setData(null);
        
        assertNull("data可以设置为null", response.getData());
        
        // 测试构造函数接受null
        PageResponse<TestEntity> response2 = new PageResponse<>("NULL-TEST", (PageData<TestEntity>) null);
        assertNull("构造函数应该接受null数据", response2.getData());
    }

    @Test
    public void testDataSetterGetter() {
        PageResponse<TestEntity> response = new PageResponse<>();
        
        List<TestEntity> dataList = Arrays.asList(new TestEntity("设置测试", 456));
        PageInfo pageInfo = PageInfo.ofData(1, 10, 10, 1);
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        
        response.setData(pageData);
        assertEquals("data应该正确获取", pageData, response.getData());
        
        // 测试重新设置
        PageData<TestEntity> newData = new PageData<>(
            Arrays.asList(new TestEntity("新数据", 789)), 
            PageInfo.EMPTY
        );
        response.setData(newData);
        assertEquals("data应该被更新", newData, response.getData());
    }

    @Test
    public void testGenericTypeSafety() {
        // 测试不同类型的泛型
        PageResponse<String> stringResponse = new PageResponse<>();
        PageData<String> stringPageData = new PageData<>(
            Arrays.asList("字符串1", "字符串2"), 
            PageInfo.EMPTY
        );
        stringResponse.setData(stringPageData);
        assertEquals("字符串数据应该正确", stringPageData, stringResponse.getData());
        
        PageResponse<Integer> intResponse = new PageResponse<>();
        PageData<Integer> intPageData = new PageData<>(
            Arrays.asList(1, 2, 3), 
            PageInfo.ofData(1, 10, 3, 3)
        );
        intResponse.setData(intPageData);
        assertEquals("整数数据应该正确", intPageData, intResponse.getData());
        
        System.out.println("泛型安全测试通过");
    }

    @Test
    public void testSerialVersionUID() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = PageResponse.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        Long uid = (Long) field.get(null);
        
        assertEquals("serialVersionUID应该正确", -8209295883024586248L, uid.longValue());
    }

    @Test
    public void testInheritanceChain() {
        PageResponse<TestEntity> response = new PageResponse<>();
        
        assertTrue("PageResponse应该继承BaseResponse", response instanceof BaseResponse);
        assertTrue("PageResponse应该继承SimpleResponse", response instanceof SimpleResponse);
        assertTrue("PageResponse应该继承BaseModel", response instanceof BaseModel);
        assertTrue("PageResponse应该实现Serializable", response instanceof java.io.Serializable);
    }

    @Test
    public void testToStringIncludesPageData() {
        List<TestEntity> dataList = Arrays.asList(new TestEntity("ToString测试", 321));
        PageInfo pageInfo = PageInfo.ofData(1, 5, 5, 1);
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        PageResponse<TestEntity> response = new PageResponse<>("TO_STRING_TEST", pageData);
        
        String result = response.toString();
        assertNotNull("toString结果不应为null", result);
        assertTrue("应该包含实体信息", result.contains("ToString测试"));
        assertTrue("应该包含分页信息", result.contains("PageData"));
        System.out.println("toString结果: " + result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyPageResponseCreation() {
        PageResponse<TestEntity> response = new PageResponse<>();
        response.setData(PageData.EMPTY);
        
        assertEquals("应该使用EMPTY常量", PageData.EMPTY, response.getData());
        assertEquals("EMPTY数据列表应该为空", Collections.emptyList(), response.getData().getListInfo());
        assertEquals("EMPTY分页信息应该正确", PageInfo.EMPTY, response.getData().getPageInfo());
        System.out.println("空PageResponse: " + response);
    }
}