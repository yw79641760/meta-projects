package com.softmegatron.shared.meta.extension.core;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.enums.ExtensionScope;

/**
 * 测试用扩展点接口 - 单例模式
 */
@Spi(value = "default", scope = ExtensionScope.SINGLETON)
public interface TestService {
    String sayHello(String name);
}
