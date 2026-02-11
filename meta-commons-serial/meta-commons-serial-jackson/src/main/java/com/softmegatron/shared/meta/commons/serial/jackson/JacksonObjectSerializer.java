package com.softmegatron.shared.meta.commons.serial.jackson;

import com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 基于Jackson的ObjectSerializer实现
 * 使用ObjectMapper.writeValueAsString方法实现对象序列化
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class JacksonObjectSerializer implements ObjectSerializer {

    /**
     * ObjectMapper实例
     */
    private final ObjectMapper objectMapper;
    
    /**
     * 默认构造函数，使用标准配置
     */
    public JacksonObjectSerializer() {
        this(createDefaultObjectMapper());
    }
    
    /**
     * 带ObjectMapper参数的构造函数
     * 
     * @param objectMapper 自定义的ObjectMapper实例
     */
    public JacksonObjectSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        try {
            // 使用Jackson的writeValueAsString方法
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // 发生Jackson处理异常时返回错误信息
            return "Jackson serialization failed: " + e.getMessage() + 
                   " [objectClass=" + obj.getClass().getName() + "]";
        } catch (Exception e) {
            // 捕获其他所有异常
            return "Jackson serialization failed: " + e.getMessage() + 
                   " [objectClass=" + obj.getClass().getName() + "]";
        }
    }

    @Override
    public String getName() {
        return "jackson";
    }

    @Override
    public boolean checkSupport(Object obj) {
        // 支持所有非null对象的序列化
        return obj != null;
    }
    
    /**
     * 获取当前使用的ObjectMapper
     * 
     * @return ObjectMapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    /**
     * 创建使用默认配置的序列化器
     * 
     * @return JacksonObjectSerializer
     */
    public static JacksonObjectSerializer createDefault() {
        return new JacksonObjectSerializer();
    }
    
    /**
     * 创建美化输出的序列化器
     * 
     * @return JacksonObjectSerializer
     */
    public static JacksonObjectSerializer createPretty() {
        ObjectMapper mapper = createDefaultObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return new JacksonObjectSerializer(mapper);
    }
    
    /**
     * 创建包含类名信息的序列化器
     * 
     * @return JacksonObjectSerializer
     */
    public static JacksonObjectSerializer createWithTypeInfo() {
        ObjectMapper mapper = createDefaultObjectMapper();
        // Jackson使用不同的方式处理类型信息
        // 这里简化处理，实际使用时可能需要@JsonTypeInfo注解
        return new JacksonObjectSerializer(mapper);
    }
    
    /**
     * 创建默认的ObjectMapper实例
     * 
     * @return 配置好的ObjectMapper
     */
    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 默认启用一些有用的特性
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}