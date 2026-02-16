package com.softmegatron.shared.meta.extension;

/**
 * 默认测试实现
 */
public class DefaultTestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + " (default)";
    }
}
