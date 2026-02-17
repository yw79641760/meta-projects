package com.softmegatron.shared.meta.extension;

/**
 * 自定义测试实现
 */
public class CustomTestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + " (custom)";
    }
}
