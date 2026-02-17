package com.softmegatron.shared.meta.extension;

import com.softmegatron.shared.meta.extension.annotation.Spi;

/**
 * 测试用扩展点接口 - 单例模式
 */
@Spi(value = "default", scope = com.softmegatron.shared.meta.extension.enums.ExtensionScope.SINGLETON)
public interface TestService {
    String sayHello(String name);
}
