package com.softmegatron.shared.meta.core.pattern.chain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ChainBuilder 和 ChainHandler 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @since 1.0.0
 */
public class ChainBuilderTest {

    @Test
    public void testBuildSingleHandler() {
        LoggingHandler handler = new LoggingHandler("single", 0);
        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler);

        ChainHandler<String, String> chain = builder.build();
        assertNotNull(chain);
        String result = chain.handle("input", new ChainContext());
        assertTrue(result.contains("single"));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildEmptyChain() {
        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.build();
    }

    @Test
    public void testBuildChainWithMultipleHandlers() {
        List<String> executionOrder = new ArrayList<>();

        LoggingHandler handler1 = new LoggingHandler("first", 0, executionOrder);
        LoggingHandler handler2 = new LoggingHandler("second", 1, executionOrder);
        LoggingHandler handler3 = new LoggingHandler("third", 2, executionOrder);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler3);
        builder.addHandler(handler1);
        builder.addHandler(handler2);

        ChainHandler<String, String> chain = builder.build();
        chain.handle("input", new ChainContext());

        assertEquals(3, executionOrder.size());
        assertEquals("first", executionOrder.get(0));
        assertEquals("second", executionOrder.get(1));
        assertEquals("third", executionOrder.get(2));
    }

    @Test
    public void testChainOrderWithDifferentPriorities() {
        List<String> executionOrder = new ArrayList<>();

        LoggingHandler handler1 = new LoggingHandler("handler1", 100, executionOrder);
        LoggingHandler handler2 = new LoggingHandler("handler2", 0, executionOrder);
        LoggingHandler handler3 = new LoggingHandler("handler3", 50, executionOrder);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler1);
        builder.addHandler(handler2);
        builder.addHandler(handler3);

        ChainHandler<String, String> chain = builder.build();
        chain.handle("input", new ChainContext());

        assertEquals("handler2", executionOrder.get(0));
        assertEquals("handler3", executionOrder.get(1));
        assertEquals("handler1", executionOrder.get(2));
    }

    @Test
    public void testChainStopInMiddle() {
        StopHandler handler1 = new StopHandler("stop", 0, true);
        LoggingHandler handler2 = new LoggingHandler("should-not-execute", 1);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler1);
        builder.addHandler(handler2);

        ChainHandler<String, String> chain = builder.build();
        String result = chain.handle("input", new ChainContext());

        assertEquals("stop:STOPPED", result);
    }

    @Test
    public void testChainContinueToEnd() {
        StopHandler handler1 = new StopHandler("first", 0, false);
        StopHandler handler2 = new StopHandler("second", 1, false);
        StopHandler handler3 = new StopHandler("third", 2, true);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler1);
        builder.addHandler(handler2);
        builder.addHandler(handler3);

        ChainHandler<String, String> chain = builder.build();
        String result = chain.handle("input", new ChainContext());

        assertEquals("third:STOPPED", result);
    }

    @Test
    public void testGetNextReturnsNullForLastHandler() {
        LoggingHandler handler1 = new LoggingHandler("first", 0);
        LoggingHandler handler2 = new LoggingHandler("second", 1);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler1);
        builder.addHandler(handler2);

        ChainHandler<String, String> chain = builder.build();

        assertNotNull(chain.getNext());
        assertNull(chain.getNext().getNext());
    }

    @Test
    public void testHandlerWithDefaultOrder() {
        DefaultOrderHandler handler = new DefaultOrderHandler();
        assertEquals(0, handler.getOrder());
    }

    @Test
    public void testContextPassedThroughChain() {
        ContextHandler handler1 = new ContextHandler("handler1", 0);
        ContextHandler handler2 = new ContextHandler("handler2", 1);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler1);
        builder.addHandler(handler2);

        ChainHandler<String, String> chain = builder.build();
        ChainContext context = new ChainContext();
        context.put("start", "yes");

        chain.handle("input", context);

        assertEquals("handler1-executed", context.get("handler1"));
        assertEquals("handler2-executed", context.get("handler2"));
    }

    @Test
    public void testAddHandlerReturnsBuilder() {
        ChainBuilder<String, String> builder = new ChainBuilder<>();
        ChainBuilder<String, String> returned = builder.addHandler(new LoggingHandler("test", 0));
        assertSame(builder, returned);
    }

    @Test
    public void testChainWithSameOrder() {
        List<String> executionOrder = new ArrayList<>();

        LoggingHandler handler1 = new LoggingHandler("first", 0, executionOrder);
        LoggingHandler handler2 = new LoggingHandler("second", 0, executionOrder);

        ChainBuilder<String, String> builder = new ChainBuilder<>();
        builder.addHandler(handler1);
        builder.addHandler(handler2);

        ChainHandler<String, String> chain = builder.build();
        chain.handle("input", new ChainContext());

        assertEquals(2, executionOrder.size());
    }

    private static class LoggingHandler implements ChainHandler<String, String> {
        private final String name;
        private final int order;
        private final List<String> executionOrder;
        private ChainHandler<String, String> next;

        LoggingHandler(String name, int order) {
            this(name, order, null);
        }

        LoggingHandler(String name, int order, List<String> executionOrder) {
            this.name = name;
            this.order = order;
            this.executionOrder = executionOrder;
        }

        @Override
        public String handle(String request, ChainContext context) {
            if (executionOrder != null) {
                executionOrder.add(name);
            }
            if (next != null) {
                return next.handle(request, context);
            }
            return name;
        }

        @Override
        public void setNext(ChainHandler<String, String> next) {
            this.next = next;
        }

        @Override
        public ChainHandler<String, String> getNext() {
            return next;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }

    private static class StopHandler implements ChainHandler<String, String> {
        private final String name;
        private final int order;
        private final boolean shouldStop;
        private ChainHandler<String, String> next;

        StopHandler(String name, int order, boolean shouldStop) {
            this.name = name;
            this.order = order;
            this.shouldStop = shouldStop;
        }

        @Override
        public String handle(String request, ChainContext context) {
            if (shouldStop) {
                return name + ":STOPPED";
            }
            if (next != null) {
                return next.handle(request, context);
            }
            return name;
        }

        @Override
        public void setNext(ChainHandler<String, String> next) {
            this.next = next;
        }

        @Override
        public ChainHandler<String, String> getNext() {
            return next;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }

    private static class DefaultOrderHandler implements ChainHandler<String, String> {
        private ChainHandler<String, String> next;

        @Override
        public String handle(String request, ChainContext context) {
            return "default";
        }

        @Override
        public void setNext(ChainHandler<String, String> next) {
            this.next = next;
        }

        @Override
        public ChainHandler<String, String> getNext() {
            return next;
        }
    }

    private static class ContextHandler implements ChainHandler<String, String> {
        private final String name;
        private final int order;
        private ChainHandler<String, String> next;

        ContextHandler(String name, int order) {
            this.name = name;
            this.order = order;
        }

        @Override
        public String handle(String request, ChainContext context) {
            context.put(name, name + "-executed");
            if (next != null) {
                return next.handle(request, context);
            }
            return name;
        }

        @Override
        public void setNext(ChainHandler<String, String> next) {
            this.next = next;
        }

        @Override
        public ChainHandler<String, String> getNext() {
            return next;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }
}
