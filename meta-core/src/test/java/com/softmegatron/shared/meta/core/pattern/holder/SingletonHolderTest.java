package com.softmegatron.shared.meta.core.pattern.holder;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * SingletonHolder 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class SingletonHolderTest {

    @Test
    public void testGetInstance() {
        TestSingletonHolder holder = new TestSingletonHolder();
        String instance = holder.getInstance();
        assertNotNull(instance);
        assertEquals("test-instance", instance);
    }

    @Test
    public void testGetInstanceReturnsSameInstance() {
        TestSingletonHolder holder = new TestSingletonHolder();
        String instance1 = holder.getInstance();
        String instance2 = holder.getInstance();
        assertSame("多次获取应返回同一实例", instance1, instance2);
    }

    @Test
    public void testNewInstanceCalledOnlyOnce() {
        CountingSingletonHolder holder = new CountingSingletonHolder();
        holder.getInstance();
        holder.getInstance();
        holder.getInstance();
        assertEquals("newInstance应只被调用一次", 1, holder.getCreateCount());
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        final CountingSingletonHolder holder = new CountingSingletonHolder();
        final int threadCount = 10;
        final CountDownLatch latch = new CountDownLatch(threadCount);
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    holder.getInstance();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals("并发访问时newInstance应只被调用一次", 1, holder.getCreateCount());
    }

    @Test
    public void testNullInstance() {
        SingletonHolder<Object> holder = new SingletonHolder<Object>() {
            @Override
            protected Object newInstance() {
                return null;
            }
        };
        Object instance = holder.getInstance();
        assertNull(instance);
    }

    @Test
    public void testDifferentHolderTypes() {
        SingletonHolder<Integer> intHolder = new SingletonHolder<Integer>() {
            private int counter = 0;

            @Override
            protected Integer newInstance() {
                return ++counter;
            }
        };
        Integer instance = intHolder.getInstance();
        assertNotNull(instance);
        assertEquals(Integer.valueOf(1), instance);
    }

    @Test
    public void testExceptionInNewInstance() {
        SingletonHolder<String> holder = new SingletonHolder<String>() {
            @Override
            protected String newInstance() {
                throw new RuntimeException("Test exception");
            }
        };
        try {
            holder.getInstance();
            fail("应抛出异常");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    private static class TestSingletonHolder extends SingletonHolder<String> {
        @Override
        protected String newInstance() {
            return "test-instance";
        }
    }

    private static class CountingSingletonHolder extends SingletonHolder<String> {
        private final AtomicInteger createCount = new AtomicInteger(0);

        @Override
        protected String newInstance() {
            createCount.incrementAndGet();
            return "counting-instance";
        }

        public int getCreateCount() {
            return createCount.get();
        }
    }
}
