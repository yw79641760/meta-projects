package com.softmegatron.shared.meta.extension.core.exception;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ExtensionException 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ExtensionExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Test exception message";
        ExtensionException exception = new ExtensionException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        String message = "Test exception message";
        Throwable cause = new RuntimeException("Cause");
        ExtensionException exception = new ExtensionException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessage() {
        ExtensionException exception = new ExtensionException((String) null);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithNullCause() {
        String message = "Test message";
        ExtensionException exception = new ExtensionException(message, null);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testExtendsRuntimeException() {
        ExtensionException exception = new ExtensionException("test");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void testCanThrowAndCatch() {
        ExtensionException exception = new ExtensionException("test");
        try {
            throw exception;
        } catch (ExtensionException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testCanCatchAsRuntimeException() {
        ExtensionException exception = new ExtensionException("test");
        try {
            throw exception;
        } catch (RuntimeException e) {
            assertTrue(e instanceof ExtensionException);
            assertEquals(exception, e);
        }
    }

    @Test
    public void testCanCatchAsException() {
        ExtensionException exception = new ExtensionException("test");
        try {
            throw exception;
        } catch (Exception e) {
            assertTrue(e instanceof ExtensionException);
            assertEquals(exception, e);
        }
    }

    @Test
    public void testCausePreserved() {
        Throwable originalCause = new IllegalArgumentException("original");
        ExtensionException exception = new ExtensionException("wrapper", originalCause);
        
        assertEquals("wrapper", exception.getMessage());
        assertEquals(originalCause, exception.getCause());
        assertEquals("original", exception.getCause().getMessage());
    }

    @Test
    public void testNestedExceptions() {
        Throwable level1 = new RuntimeException("level1");
        Throwable level2 = new ExtensionException("level2", level1);
        Throwable level3 = new ExtensionException("level3", level2);
        
        assertEquals("level3", level3.getMessage());
        assertEquals(level2, level3.getCause());
        assertEquals(level1, level3.getCause().getCause());
    }

    @Test
    public void testEmptyMessage() {
        ExtensionException exception = new ExtensionException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    public void testMessageWithSpecialCharacters() {
        String message = "Error: 无法加载扩展 [key=test] \n\t原因: 类不存在";
        ExtensionException exception = new ExtensionException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testIsPublicClass() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ExtensionException.class.getModifiers()));
    }

    @Test
    public void testIsNotFinalClass() {
        assertFalse(java.lang.reflect.Modifier.isFinal(ExtensionException.class.getModifiers()));
    }
}
