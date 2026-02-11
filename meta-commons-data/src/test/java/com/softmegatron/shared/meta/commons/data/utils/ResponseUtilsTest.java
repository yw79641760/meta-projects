package com.softmegatron.shared.meta.commons.data.utils;

import com.softmegatron.shared.meta.commons.data.base.ListResponse;
import com.softmegatron.shared.meta.commons.data.base.PageData;
import com.softmegatron.shared.meta.commons.data.base.PageInfo;
import com.softmegatron.shared.meta.commons.data.base.PageRequest;
import com.softmegatron.shared.meta.commons.data.base.PageResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ResponseUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ResponseUtils 的各种功能
 * @date 2026/2/6 14:40
 * @since 1.0.0
 */
public class ResponseUtilsTest {

    private static class TestDto {
        private String name;
        private int age;

        public TestDto(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    private static class TestVo {
        private String name;
        private String age;

        public TestVo(String name, String age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }
    }

    @Test
    public void testEmptyListResponse() {
        ListResponse<TestDto> response = ResponseUtils.emptyListResponse();
        assertNotNull("空列表响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertTrue("数据应为空列表", response.getData().isEmpty());
    }

    @Test
    public void testEmptyPageResponse() {
        PageResponse<TestDto> response = ResponseUtils.emptyPageResponse();
        assertNotNull("空分页响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("数据应为EMPTY", PageData.EMPTY, response.getData());
    }

    @Test
    public void testConvertItem() {
        TestDto dto = new TestDto("Alice", 25);
        Optional<TestVo> result = ResponseUtils.convertItem(dto, d -> new TestVo(d.getName(), String.valueOf(d.getAge())));

        assertTrue("结果应存在", result.isPresent());
        assertEquals("名称应正确", "Alice", result.get().getName());
        assertEquals("年龄应正确", "25", result.get().getAge());
    }

    @Test
    public void testConvertItemWithNull() {
        Optional<TestVo> result = ResponseUtils.convertItem((TestDto) null, d -> new TestVo(d.getName(), String.valueOf(d.getAge())));
        assertTrue("null输入应返回empty", result.isEmpty());
    }

    @Test
    public void testConvertList() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30)
        );
        List<TestVo> result = ResponseUtils.convertList(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())));

        assertNotNull("结果不应为null", result);
        assertEquals("结果大小应正确", 2, result.size());
        assertEquals("第一项名称应正确", "Alice", result.get(0).getName());
        assertEquals("第二项名称应正确", "Bob", result.get(1).getName());
    }

    @Test
    public void testConvertListWithEmpty() {
        List<TestDto> dtoList = Collections.emptyList();
        List<TestVo> result = ResponseUtils.convertList(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())));

        assertNotNull("结果不应为null", result);
        assertTrue("结果应为空", result.isEmpty());
    }

    @Test
    public void testConvertListWithFilters() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30),
            new TestDto("Charlie", 20)
        );
        List<TestVo> result = ResponseUtils.convertList(
            dtoList,
            d -> d.getAge() > 20,
            d -> new TestVo(d.getName(), String.valueOf(d.getAge())),
            v -> !v.getName().equals("Bob")
        );

        assertNotNull("结果不应为null", result);
        assertEquals("结果大小应正确", 1, result.size());
        assertEquals("结果应为Alice", "Alice", result.get(0).getName());
    }

    @Test
    public void testToListResponse() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30)
        );
        ListResponse<TestVo> response = ResponseUtils.toListResponse(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())));

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("数据大小应正确", 2, response.getData().size());
    }

    @Test
    public void testToListResponseWithFilters() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30),
            new TestDto("Charlie", 20)
        );
        ListResponse<TestVo> response = ResponseUtils.toListResponse(
            dtoList,
            d -> d.getAge() > 20,
            d -> new TestVo(d.getName(), String.valueOf(d.getAge())),
            v -> v.getName().equals("Alice")
        );

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("过滤后应只有1项", 1, response.getData().size());
        assertEquals("结果应为Alice", "Alice", response.getData().get(0).getName());
    }

    @Test
    public void testToPageResponse() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30)
        );
        PageInfo pageInfo = new PageInfo(1, 10, 2, 2);
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())), pageInfo);

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("数据大小应正确", 2, response.getData().getListInfo().size());
    }

    @Test
    public void testToPageResponseWithFilters() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30),
            new TestDto("Charlie", 20)
        );
        PageInfo pageInfo = new PageInfo(1, 10, 3, 1);
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(
            dtoList,
            d -> d.getAge() > 20,
            d -> new TestVo(d.getName(), String.valueOf(d.getAge())),
            v -> v.getName().startsWith("A"),
            pageInfo
        );

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("过滤后应只有1项", 1, response.getData().getListInfo().size());
    }

    @Test
    public void testToPageResponseWithPageRequest() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30)
        );
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrentPage(1);
        pageRequest.setPageSize(10);
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())), 2, pageRequest);

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("数据大小应正确", 2, response.getData().getListInfo().size());
        assertEquals("当前页应为1", 1, response.getData().getPageInfo().getCurrentPage());
    }

    @Test
    public void testToPageResponseWithPageRequestAndFilters() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25),
            new TestDto("Bob", 30),
            new TestDto("Charlie", 20)
        );
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrentPage(1);
        pageRequest.setPageSize(10);
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(
            dtoList,
            d -> d.getAge() > 20,
            d -> new TestVo(d.getName(), String.valueOf(d.getAge())),
            v -> v.getName().startsWith("A"),
            2,
            pageRequest
        );

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertNotNull("数据不应为null", response.getData());
        assertEquals("过滤后应只有1项", 1, response.getData().getListInfo().size());
    }

    @Test(expected = NullPointerException.class)
    public void testToPageResponseWithNullPageRequest() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25)
        );
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())), 1, null);
    }

    @Test
    public void testToPageResponseWithZeroCurrentPage() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25)
        );
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrentPage(0);
        pageRequest.setPageSize(10);
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())), 1, pageRequest);

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertEquals("当前页应默认为1", 1, response.getData().getPageInfo().getCurrentPage());
    }

    @Test
    public void testToPageResponseWithNegativePageSize() {
        List<TestDto> dtoList = Arrays.asList(
            new TestDto("Alice", 25)
        );
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrentPage(1);
        pageRequest.setPageSize(-1);
        PageResponse<TestVo> response = ResponseUtils.toPageResponse(dtoList, d -> new TestVo(d.getName(), String.valueOf(d.getAge())), 1, pageRequest);

        assertNotNull("响应不应为null", response);
        assertTrue("响应应成功", response.isSuccess());
        assertEquals("页大小应默认为200", 200, response.getData().getPageInfo().getPageSize());
    }
}
