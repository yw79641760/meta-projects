package com.softmegatron.shared.meta.extension.core;

import com.softmegatron.shared.meta.extension.core.annotation.Spi;
import com.softmegatron.shared.meta.extension.core.enums.ExtensionScope;

/**
 * 测试用扩展点接口 - 多例模式
 */
@Spi(scope = ExtensionScope.PROTOTYPE)
public interface PrototypeService {
    String getId();
}
