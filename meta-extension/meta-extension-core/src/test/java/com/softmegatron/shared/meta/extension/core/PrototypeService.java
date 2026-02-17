package com.softmegatron.shared.meta.extension;

import com.softmegatron.shared.meta.extension.annotation.Spi;
import com.softmegatron.shared.meta.extension.enums.ExtensionScope;

/**
 * 测试用扩展点接口 - 多例模式
 */
@Spi(scope = ExtensionScope.PROTOTYPE)
public interface PrototypeService {
    String getId();
}
