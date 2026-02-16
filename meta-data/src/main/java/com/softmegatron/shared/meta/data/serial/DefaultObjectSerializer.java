package com.softmegatron.shared.meta.data.serial;

import com.softmegatron.shared.meta.serial.spi.ObjectSerializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultToStringSerializer
 * @description
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @date 2026/2/5 15:39
 * @since 1.0.0
 */
public class DefaultObjectSerializer implements ObjectSerializer {

     private Object object;
     
     // 反射字段缓存：Class -> Field[]
     private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();

     public Object getObject() {
         return object;
     }

     public DefaultObjectSerializer() {
         super();
     }

     public DefaultObjectSerializer(Object object) {
         this.object = object;
     }

     public static String toString(Object obj) {
         if (obj == null) {
             return "<null>";
         }
         
         // 处理基本类型和String
         if (obj instanceof String) {
             return "\"" + obj + "\"";
         }
         if (obj instanceof Number 
            || obj instanceof Boolean 
            || obj instanceof Character 
            || obj instanceof Enum) {
             return obj.toString();
         }
         
         // 处理数组
         if (obj.getClass().isArray()) {
             return formatArray(obj);
         }
         
         // 处理集合
         if (obj instanceof Collection) {
             return formatCollection((Collection<?>) obj);
         }
         
         // 处理Map
         if (obj instanceof Map) {
             return formatMap((Map<?, ?>) obj);
         }
         
         // 处理普通对象
         return formatObject(obj);
     }

     private static String formatArray(Object array) {
         StringBuilder sb = new StringBuilder();
         sb.append("[");
         int length = Array.getLength(array);
         for (int i = 0; i < length; i++) {
             if (i > 0) {
                sb.append(", ");
             }
             sb.append(toString(Array.get(array, i)));
         }
         sb.append("]");
         return sb.toString();
     }

     private static String formatCollection(Collection<?> collection) {
         StringBuilder sb = new StringBuilder();
         sb.append("[");
         boolean first = true;
         for (Object item : collection) {
             if (!first) {
                sb.append(", ");
             }
             sb.append(toString(item));
             first = false;
         }
         sb.append("]");
         return sb.toString();
     }

     private static String formatMap(Map<?, ?> map) {
         StringBuilder sb = new StringBuilder();
         sb.append("{");
         boolean first = true;
         for (Map.Entry<?, ?> entry : map.entrySet()) {
             if (!first) {
                sb.append(", ");
             }
             sb.append(toString(entry.getKey())).append("=").append(toString(entry.getValue()));
             first = false;
         }
         sb.append("}");
         return sb.toString();
     }

     private static String formatObject(Object obj) {
         Class<?> clazz = obj.getClass();
         StringBuilder sb = new StringBuilder();
         sb.append(clazz.getSimpleName()).append("{");
         
         // 获取包括父类在内的所有可序列化字段
         Field[] fields = getAllSerializableFields(clazz);
         
         boolean first = true;
         for (Field field : fields) {
             try {
                 field.setAccessible(true);
                 Object value = field.get(obj);
                 if (!first) {
                    sb.append(", ");
                 }
                 sb.append(field.getName()).append("=").append(toString(value));
                 first = false;
             } catch (IllegalAccessException e) {
                 // 忽略无法访问的字段
             }
         }
         sb.append("}");
         return sb.toString();
     }
     
     /**
      * 获取类及其父类的所有可序列化字段
      * @param clazz 类对象
      * @return 可序列化的字段数组
      */
     private static Field[] getAllSerializableFields(Class<?> clazz) {
         return FIELD_CACHE.computeIfAbsent(clazz, cls -> {
             java.util.List<Field> allFields = new ArrayList<>();
             Class<?> currentClass = cls;
             
             // 遍历类层次结构，收集所有可序列化字段
             while (currentClass != null && currentClass != Object.class) {
                 Field[] declaredFields = currentClass.getDeclaredFields();
                 for (Field field : declaredFields) {
                     if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                         allFields.add(field);
                     }
                 }
                 currentClass = currentClass.getSuperclass();
             }
             
             return allFields.toArray(new Field[0]);
         });
     }

     @Override
     public String serialize(Object obj) {
         return toString(obj);
     }

     @Override
     public String getName() {
         return "default";
     }

     @Override
     public boolean checkSupport(Object obj) {
         // 默认支持所有对象
         return true;
     }
     
     /**
      * 清除字段缓存（用于测试或内存管理）
      */
     public static void clearCache() {
         FIELD_CACHE.clear();
     }
     
     /**
      * 获取缓存大小（用于监控）
      * @return 缓存中的类数量
      */
     public static int getCacheSize() {
         return FIELD_CACHE.size();
     }
}