package com.softmegatron.shared.meta.serial.fastjson;

import com.softmegatron.shared.meta.serial.spi.ObjectSerializer;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

/**
 * 基于Fastjson2的ObjectSerializer实现
 * 使用JSON.toJSONString方法实现对象序列化
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class FastjsonObjectSerializer implements ObjectSerializer {

    /**
     * JSON序列化特性配置
     */
    private final JSONWriter.Feature[] features;
    
    /**
     * 默认构造函数，使用标准特性
     */
    public FastjsonObjectSerializer() {
        this(JSONWriter.Feature.WriteClassName, 
             JSONWriter.Feature.FieldBased);
    }
    
    /**
     * 带特性的构造函数
     * 
     * @param features JSON序列化特性
     */
    public FastjsonObjectSerializer(JSONWriter.Feature... features) {
        this.features = features;
    }

    @Override
    public String serialize(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        try {
            // 使用Fastjson2的toJSONString方法
            return JSON.toJSONString(obj, features);
        } catch (Exception e) {
            // 发生异常时返回错误信息
            return "Fastjson serialization failed: " + e.getMessage() + 
                   " [objectClass=" + obj.getClass().getName() + "]";
        }
    }

    @Override
    public String getName() {
        return "fastjson";
    }

    @Override
    public boolean checkSupport(Object obj) {
        // 支持所有非null对象的序列化
        return obj != null;
    }
    
    /**
     * 获取当前使用的序列化特性
     * 
     * @return JSONWriter.Feature[]
     */
    public JSONWriter.Feature[] getFeatures() {
        return features;
    }
    
    /**
     * 创建使用默认特性的序列化器
     * 
     * @return FastjsonObjectSerializer
     */
    public static FastjsonObjectSerializer createDefault() {
        return new FastjsonObjectSerializer();
    }
    
    /**
     * 创建精简特性的序列化器（不包含类名信息）
     * 
     * @return FastjsonObjectSerializer
     */
    public static FastjsonObjectSerializer createCompact() {
        return new FastjsonObjectSerializer(JSONWriter.Feature.FieldBased);
    }
    
    /**
     * 创建美化输出的序列化器
     * 
     * @return FastjsonObjectSerializer
     */
    public static FastjsonObjectSerializer createPretty() {
        return new FastjsonObjectSerializer(
            JSONWriter.Feature.WriteClassName,
            JSONWriter.Feature.FieldBased,
            JSONWriter.Feature.PrettyFormat
        );
    }
}