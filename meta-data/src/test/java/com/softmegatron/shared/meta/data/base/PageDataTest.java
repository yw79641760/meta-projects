package com.softmegatron.shared.meta.data.base;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * PageData 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 PageData 的分页数据包装功能
 * @date 2026/2/11 22:35
 * @since 1.0.0
 */
public class PageDataTest {

    // 测试用的数据类
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

        @Override
        public String toString() {
            return "TestEntity{id=" + id + ", name='" + name + "'}";
        }
    }

    @Test
    public void testConstructor() {
        List<TestEntity> dataList = Arrays.asList(
            new TestEntity("实体1", 1),
            new TestEntity("实体2", 2)
        );
        PageInfo pageInfo = new PageInfo(1, 10, 20, 2);
        
        PageData<TestEntity> pageData = new PageData<>(dataList, pageInfo);
        
        assertEquals("数据列表应该正确", dataList, pageData.getListInfo());
        assertEquals("分页信息应该正确", pageInfo, pageData.getPageInfo());
        System.out.println("构造函数测试: " + pageData);
    }

    @Test
    public void testOfDataFactoryMethod() {
        List<TestEntity> dataList = Arrays.asList(
            new TestEntity("工厂实体1", 10),
            new TestEntity("工厂实体2", 20)
        );
        PageInfo pageInfo = PageInfo.ofData(2, 10, 50, 2);
        
        PageData<TestEntity> pageData = PageData.ofData(dataList, pageInfo);
        
        assertEquals("数据列表应该正确", dataList, pageData.getListInfo());
        assertEquals("分页信息应该正确", pageInfo, pageData.getPageInfo());
        System.out.println("工厂方法测试: " + pageData);
    }

    @Test
    public void testEmptyConstant() {
        PageData<?> empty = PageData.EMPTY;
        
        assertNotNull("EMPTY常量不应为null", empty);
        assertEquals("列表应该为空", Collections.emptyList(), empty.getListInfo());
        assertEquals("分页信息应该是EMPTY", PageInfo.EMPTY, empty.getPageInfo());
        System.out.println("EMPTY常量: " + empty);
    }

    @Test
    public void testSetterGetter() {
        PageData<TestEntity> pageData = new PageData<>(Collections.emptyList(), PageInfo.EMPTY);
        
        List<TestEntity> newList = Arrays.asList(new TestEntity("新实体", 99));
        PageInfo newPageInfo = PageInfo.ofData(3, 5, 15, 5);
        
        pageData.setListInfo(newList);
        pageData.setPageInfo(newPageInfo);
        
        assertEquals("列表应该正确设置", newList, pageData.getListInfo());
        assertEquals("分页信息应该正确设置", newPageInfo, pageData.getPageInfo());
    }

    @Test
    public void testNullListHandling() {
        PageInfo pageInfo = new PageInfo(1, 10, 0, 0);
        PageData<TestEntity> pageData = new PageData<>(null, pageInfo);
        
        assertNull("列表可以为null", pageData.getListInfo());
        assertEquals("分页信息应该正确", pageInfo, pageData.getPageInfo());
        
        // 测试setter接受null
        pageData.setListInfo(null);
        assertNull("setter应该接受null", pageData.getListInfo());
    }

    @Test
    public void testNullPageInfoHandling() {
        List<TestEntity> dataList = Arrays.asList(new TestEntity("测试", 1));
        PageData<TestEntity> pageData = new PageData<>(dataList, null);
        
        assertEquals("数据列表应该正确", dataList, pageData.getListInfo());
        assertNull("分页信息可以为null", pageData.getPageInfo());
        
        // 测试setter接受null
        pageData.setPageInfo(null);
        assertNull("setter应该接受null", pageData.getPageInfo());
    }

    @Test
    public void testDifferentGenericTypes() {
        // 测试String类型
        PageData<String> stringPageData = new PageData<>(
            Arrays.asList("字符串1", "字符串2"), 
            PageInfo.EMPTY
        );
        assertEquals("字符串列表大小应该正确", 2, stringPageData.getListInfo().size());
        
        // 测试Integer类型
        PageData<Integer> intPageData = new PageData<>(
            Arrays.asList(1, 2, 3), 
            PageInfo.ofData(1, 10, 3, 3)
        );
        assertEquals("整数列表大小应该正确", 3, intPageData.getListInfo().size());
        
        System.out.println("泛型类型测试通过");
    }

    @Test
    public void testLargeDataSet() {
        // 测试大数据集
        List<TestEntity> largeList = Arrays.asList(
            new TestEntity("大实体1", 100),
            new TestEntity("大实体2", 101),
            new TestEntity("大实体3", 102)
        );
        PageInfo pageInfo = PageInfo.ofData(1, 3, 1000, 3);
        
        PageData<TestEntity> pageData = new PageData<>(largeList, pageInfo);
        
        assertEquals("大数据列表应该正确", largeList, pageData.getListInfo());
        assertEquals("大数据分页信息应该正确", pageInfo, pageData.getPageInfo());
        System.out.println("大数据集测试: " + pageData);
    }

    @Test
    public void testSerialVersionUID() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = PageData.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        Long uid = (Long) field.get(null);
        
        assertEquals("serialVersionUID应该正确", 5021157350064058810L, uid.longValue());
    }

    @Test
    public void testInheritanceFromBaseModel() {
        PageData<TestEntity> pageData = new PageData<>(Collections.emptyList(), PageInfo.EMPTY);
        assertTrue("PageData应该继承BaseModel", pageData instanceof BaseModel);
        assertTrue("PageData应该实现Serializable", pageData instanceof java.io.Serializable);
    }

    @Test
    public void testToStringIncludesData() {
        List<TestEntity> dataList = Arrays.asList(
            new TestEntity("ToString测试", 123)
        );
        PageData<TestEntity> pageData = new PageData<>(dataList, PageInfo.EMPTY);
        
        String result = pageData.toString();
        assertNotNull("toString结果不应为null", result);
        assertTrue("应该包含数据信息", result.contains("ToString测试"));
        assertTrue("应该包含分页信息", result.contains("PageInfo"));
        System.out.println("toString测试结果: " + result);
    }

    @Test
    public void testModifyExistingPageData() {
        List<TestEntity> originalList = Arrays.asList(new TestEntity("原始", 1));
        PageInfo originalInfo = PageInfo.ofData(1, 10, 10, 1);
        PageData<TestEntity> pageData = new PageData<>(originalList, originalInfo);
        
        // 修改数据
        List<TestEntity> newList = Arrays.asList(
            new TestEntity("修改后1", 10),
            new TestEntity("修改后2", 20)
        );
        PageInfo newInfo = PageInfo.ofData(2, 10, 20, 2);
        
        pageData.setListInfo(newList);
        pageData.setPageInfo(newInfo);
        
        assertEquals("修改后的列表应该正确", newList, pageData.getListInfo());
        assertEquals("修改后的分页信息应该正确", newInfo, pageData.getPageInfo());
        assertNotEquals("应该与原列表不同", originalList, pageData.getListInfo());
        System.out.println("修改测试: " + pageData);
    }
}