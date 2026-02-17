package com.softmegatron.shared.meta.extension;

import java.util.UUID;

/**
 * 多例测试实现
 */
public class PrototypeServiceImpl implements PrototypeService {
    private final String id = UUID.randomUUID().toString().substring(0, 8);

    @Override
    public String getId() {
        return id;
    }
}
