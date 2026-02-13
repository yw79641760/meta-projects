package com.softmegatron.shared.meta.commons.serial.lang;

import com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer;

/**
 * CommonsLangObjectSerializer 使用演示
 * 展示基于Apache Commons Lang3的序列化器的各种用法
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @since 1.0.0
 */
public class CommonsLangSerializerDemo {

    // 演示用的业务对象
    public static class User {
        private String username;
        private String email;
        private int age;
        private boolean active;
        
        public User(String username, String email, int age, boolean active) {
            this.username = username;
            this.email = email;
            this.age = age;
            this.active = active;
        }
        
        // getters
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public int getAge() { return age; }
        public boolean isActive() { return active; }
    }

    // 演示用的复杂对象
    public static class Order {
        private String orderId;
        private User customer;
        private double amount;
        private String[] items;
        
        public Order(String orderId, User customer, double amount, String[] items) {
            this.orderId = orderId;
            this.customer = customer;
            this.amount = amount;
            this.items = items;
        }
        
        // getters
        public String getOrderId() { return orderId; }
        public User getCustomer() { return customer; }
        public double getAmount() { return amount; }
        public String[] getItems() { return items; }
    }

    public static void main(String[] args) {
        System.out.println("=== CommonsLangObjectSerializer 演示 ===\n");

        // 1. 基本使用
        demonstrateBasicUsage();
        
        // 2. 不同样式对比
        demonstrateDifferentStyles();
        
        // 3. 复杂对象序列化
        demonstrateComplexObject();
        
        // 4. 集合和数组处理
        demonstrateCollections();
        
        // 5. SPI集成演示
        demonstrateSpiIntegration();
    }

    private static void demonstrateBasicUsage() {
        System.out.println("1. 基本使用演示:");
        
        ObjectSerializer serializer = new CommonsLangObjectSerializer();
        
        User user = new User("张三", "zhangsan@example.com", 25, true);
        String result = serializer.serialize(user);
        
        System.out.println("用户对象序列化结果:");
        System.out.println(result);
        System.out.println();
    }

    private static void demonstrateDifferentStyles() {
        System.out.println("2. 不同样式对比:");
        
        User user = new User("李四", "lisi@example.com", 30, false);
        
        // 默认样式
        ObjectSerializer defaultSerializer = CommonsLangObjectSerializer.createDefault();
        System.out.println("默认样式 (SHORT_PREFIX_STYLE):");
        System.out.println(defaultSerializer.serialize(user));
        
        // 多行样式
        ObjectSerializer multiLineSerializer = CommonsLangObjectSerializer.createMultiLine();
        System.out.println("\n多行样式 (MULTI_LINE_STYLE):");
        System.out.println(multiLineSerializer.serialize(user));
        
        // 简单样式
        ObjectSerializer simpleSerializer = CommonsLangObjectSerializer.createSimple();
        System.out.println("\n简单样式 (SIMPLE_STYLE):");
        System.out.println(simpleSerializer.serialize(user));
        
        System.out.println();
    }

    private static void demonstrateComplexObject() {
        System.out.println("3. 复杂对象序列化:");
        
        User customer = new User("王五", "wangwu@example.com", 28, true);
        String[] items = {"商品A", "商品B", "商品C"};
        Order order = new Order("ORD20260205001", customer, 299.99, items);
        
        ObjectSerializer serializer = new CommonsLangObjectSerializer();
        String result = serializer.serialize(order);
        
        System.out.println("订单对象序列化结果:");
        System.out.println(result);
        System.out.println();
    }

    private static void demonstrateCollections() {
        System.out.println("4. 集合和数组处理:");
        
        ObjectSerializer serializer = new CommonsLangObjectSerializer();
        
        // List
        java.util.List<String> stringList = java.util.Arrays.asList("苹果", "香蕉", "橙子");
        System.out.println("List序列化:");
        System.out.println(serializer.serialize(stringList));
        
        // Map
        java.util.Map<String, Object> dataMap = new java.util.HashMap<>();
        dataMap.put("姓名", "赵六");
        dataMap.put("年龄", 35);
        dataMap.put("城市", "北京");
        System.out.println("\nMap序列化:");
        System.out.println(serializer.serialize(dataMap));
        
        // Array
        Integer[] numbers = {1, 2, 3, 4, 5};
        System.out.println("\n数组序列化:");
        System.out.println(serializer.serialize(numbers));
        
        System.out.println();
    }

    private static void demonstrateSpiIntegration() {
        System.out.println("5. SPI集成演示:");
        
        // 模拟SPI加载过程
        System.out.println("通过ServiceLoader加载的序列化器:");
        
        java.util.ServiceLoader<com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer> loader = 
            java.util.ServiceLoader.load(com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer.class);
        
        for (com.softmegatron.shared.meta.commons.serial.spi.ObjectSerializer serializer : loader) {
            if (serializer instanceof CommonsLangObjectSerializer) {
                System.out.println("- " + serializer.getName() + " (当前实现)");
                
                // 测试支持性检查
                User testUser = new User("测试用户", "test@example.com", 20, true);
                boolean supported = serializer.checkSupport(testUser);
                System.out.println("  支持性检查: " + supported);
                
                // 测试序列化
                String result = serializer.serialize(testUser);
                System.out.println("  序列化结果预览: " + result.substring(0, Math.min(50, result.length())) + "...");
            }
        }
        
        System.out.println();
    }
}