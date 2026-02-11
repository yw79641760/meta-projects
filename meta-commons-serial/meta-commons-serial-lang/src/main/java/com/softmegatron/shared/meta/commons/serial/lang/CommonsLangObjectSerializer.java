package com.softmegatron.shared.meta.commons.serial.lang;

import com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基于Apache Commons Lang3的ObjectSerializer实现
 * 使用ReflectionToStringBuilder.toString(this)方法实现对象序列化
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class CommonsLangObjectSerializer implements ObjectSerializer {

    /**
     * 序列化样式配置
     */
    private final ToStringStyle style;
    
    /**
     * 默认构造函数，使用SHORT_PREFIX_STYLE样式
     */
    public CommonsLangObjectSerializer() {
        this(ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    /**
     * 带样式参数的构造函数
     * 
     * @param style 序列化样式
     */
    public CommonsLangObjectSerializer(ToStringStyle style) {
        this.style = style;
    }

    @Override
    public String serialize(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        try {
            // 使用Apache Commons Lang3的ReflectionToStringBuilder
            return ReflectionToStringBuilder.toString(obj, style);
        } catch (Exception e) {
            // 发生异常时返回错误信息
            return "Serialization failed: " + e.getMessage() + 
                   " [objectClass=" + obj.getClass().getName() + "]";
        }
    }

    @Override
    public String getName() {
        return "commons-lang";
    }

    @Override
    public boolean checkSupport(Object obj) {
        // 支持所有非null对象的序列化
        return obj != null;
    }
    
    /**
     * 获取当前使用的序列化样式
     * 
     * @return ToStringStyle
     */
    public ToStringStyle getStyle() {
        return style;
    }
    
    /**
     * 创建使用默认样式的序列化器
     * 
     * @return CommonsLangObjectSerializer
     */
    public static CommonsLangObjectSerializer createDefault() {
        return new CommonsLangObjectSerializer();
    }
    
    /**
     * 创建使用多行样式的序列化器
     * 
     * @return CommonsLangObjectSerializer
     */
    public static CommonsLangObjectSerializer createMultiLine() {
        return new CommonsLangObjectSerializer(ToStringStyle.MULTI_LINE_STYLE);
    }
    
    /**
     * 创建使用简单样式的序列化器
     * 
     * @return CommonsLangObjectSerializer
     */
    public static CommonsLangObjectSerializer createSimple() {
        return new CommonsLangObjectSerializer(ToStringStyle.SIMPLE_STYLE);
    }
}