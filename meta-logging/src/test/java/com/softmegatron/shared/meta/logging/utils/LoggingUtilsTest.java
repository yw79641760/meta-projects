package com.softmegatron.shared.meta.logging.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * LoggingUtils 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 LoggingUtils 的各种功能
 * @date 2026/2/6 15:05
 * @since 1.0.0
 */
public class LoggingUtilsTest {

    @Test
    public void testBuildWithNoArgs() {
        String result = LoggingUtils.build("Hello World");
        assertNotNull("日志内容不应为null", result);
        assertEquals("无参数应返回原消息", "Hello World", result);
    }

    @Test
    public void testBuildWithEmptyMessage() {
        String result = LoggingUtils.build("");
        assertNotNull("日志内容不应为null", result);
        assertEquals("空消息应返回空字符串", "", result);
    }

    @Test
    public void testBuildWithNullArgs() {
        String result = LoggingUtils.build("Hello {}", (Object[]) null);
        assertNotNull("日志内容不应为null", result);
        assertEquals("null参数应返回原消息", "Hello {}", result);
    }

    @Test
    public void testBuildWithEmptyArgs() {
        String result = LoggingUtils.build("Hello {}", new Object[]{});
        assertNotNull("日志内容不应为null", result);
        assertEquals("空参数数组应返回原消息", "Hello {}", result);
    }

    @Test
    public void testBuildWithSingleArg() {
        String result = LoggingUtils.build("Hello {}", "World");
        assertNotNull("日志内容不应为null", result);
        assertEquals("单个参数应正确替换", "Hello World", result);
    }

    @Test
    public void testBuildWithMultipleArgs() {
        String result = LoggingUtils.build("Hello {} and {}", "Alice", "Bob");
        assertNotNull("日志内容不应为null", result);
        assertEquals("多个参数应正确替换", "Hello Alice and Bob", result);
    }

    @Test
    public void testBuildWithStringFormat() {
        String result = LoggingUtils.build("User {} logged in at {}", "Alice", "2026-02-06");
        assertNotNull("日志内容不应为null", result);
        assertEquals("字符串格式化应正确", "User Alice logged in at 2026-02-06", result);
    }

    @Test
    public void testBuildWithNumberArg() {
        String result = LoggingUtils.build("Count: {}", 42);
        assertNotNull("日志内容不应为null", result);
        assertEquals("数字参数应正确替换", "Count: 42", result);
    }

    @Test
    public void testBuildWithNullArg() {
        String result = LoggingUtils.build("Value: {}", (Object) null);
        assertNotNull("日志内容不应为null", result);
        assertEquals("null参数应显示为null", "Value: null", result);
    }

    @Test
    public void testBuildWithMixedArgs() {
        String result = LoggingUtils.build("User: {}, Age: {}, Active: {}", "Alice", 25, true);
        assertNotNull("日志内容不应为null", result);
        assertEquals("混合类型参数应正确替换", "User: Alice, Age: 25, Active: true", result);
    }

    @Test
    public void testBuildWithMorePlaceholdersThanArgs() {
        String result = LoggingUtils.build("Hello {} and {}", "Alice");
        assertNotNull("日志内容不应为null", result);
        assertEquals("占位符多于参数时应部分替换", "Hello Alice and {}", result);
    }

    @Test
    public void testBuildWithMoreArgsThanPlaceholders() {
        String result = LoggingUtils.build("Hello {}", "Alice", "Bob");
        assertNotNull("日志内容不应为null", result);
        assertEquals("参数多于占位符时应丢弃多余参数", "Hello Alice", result);
    }

    @Test
    public void testBuildWithSpecialChars() {
        String result = LoggingUtils.build("Price: {}, Status: {}", "$100.50", "OK");
        assertNotNull("日志内容不应为null", result);
        assertEquals("特殊字符应正确处理", "Price: $100.50, Status: OK", result);
    }

    @Test
    public void testBuildWithUnicode() {
        String result = LoggingUtils.build("姓名: {}, 年龄: {}", "张三", 25);
        assertNotNull("日志内容不应为null", result);
        assertEquals("Unicode字符应正确处理", "姓名: 张三, 年龄: 25", result);
    }

    @Test
    public void testBuildWithFormattedMessage() {
        String result = LoggingUtils.build("Operation {} completed in {} ms", "login", 123);
        assertNotNull("日志内容不应为null", result);
        assertEquals("格式化消息应正确", "Operation login completed in 123 ms", result);
    }

    @Test
    public void testBuildWithExceptionHandling() {
        String result = LoggingUtils.build("Invalid format", new Object[]{"arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8", "arg9", "arg10"});
        assertNotNull("日志内容不应为null", result);
        assertTrue("日志应包含原消息", result.contains("Invalid format"));
    }

    @Test
    public void testBuildWithObjectArg() {
        TestObject obj = new TestObject("test");
        String result = LoggingUtils.build("Object: {}", obj);
        assertNotNull("日志内容不应为null", result);
        assertTrue("对象应调用toString", result.contains("test"));
    }

    @Test
    public void testBuildWithArrayArg() {
        String[] array = new String[]{"A", "B", "C"};
        String result = LoggingUtils.build("Array: {}", array);
        assertNotNull("日志内容不应为null", result);
    }

    @Test
    public void testBuildWithLongMessage() {
        String longMessage = "This is a very long message that contains many words and should still be handled correctly by the logging utility without any issues or problems occurring during the processing of the message.";
        String result = LoggingUtils.build(longMessage);
        assertNotNull("日志内容不应为null", result);
        assertEquals("长消息应正确处理", longMessage, result);
    }

    @Test
    public void testBuildWithManyArgs() {
        String result = LoggingUtils.build("{} {} {} {} {} {} {} {} {} {}", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        assertNotNull("日志内容不应为null", result);
        assertEquals("多个参数应正确替换", "A B C D E F G H I J", result);
    }

    private static class TestObject {
        private String name;

        public TestObject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestObject{" + name + "}";
        }
    }
}
