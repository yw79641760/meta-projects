package com.softmegatron.shared.meta.extension.enums;

import com.softmegatron.shared.meta.data.base.BaseEnum;

import java.util.Arrays;
import java.util.List;

/**
 * ExtensionProtocol
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 1:47 PM
 */
public enum ExtensionProtocol implements BaseEnum {
    /**
     * 普通文件
     */
    FILE("file", "普通文件"),
    /**
     * JAR包
     */
    JAR("jar", "jar包"),
    ;
    /**
     * 支持的扩展配置类型
     */
    public static final List<String> SUPPORTED_PROTOCOL = Arrays.asList(FILE.getCode(),
                                                                        JAR.getCode());

    private final String code;

    private final String desc;

    ExtensionProtocol(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
