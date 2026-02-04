package com.softmegatron.shared.meta.extension.enums;

import com.softmegatron.shared.meta.commons.data.base.BaseEnum;

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

    private String code;

    private String desc;

    ExtensionProtocol(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
