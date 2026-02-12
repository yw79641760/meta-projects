package com.softmegatron.shared.meta.commons.extension.enums;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ExtensionProtocol 测试类
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">yw</a>
 * @description 测试 ExtensionProtocol 枚举
 * @date 2026/2/6 16:00
 * @since 1.0.0
 */
public class ExtensionProtocolTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testFileEnum() {
        ExtensionProtocol fileProtocol = ExtensionProtocol.FILE;
        System.out.println("FILE protocol: " + fileProtocol.getCode() + " - " + fileProtocol.getDesc());
        
        assertEquals("FILE的code应该正确", "file", fileProtocol.getCode());
        assertEquals("FILE的desc应该正确", "普通文件", fileProtocol.getDesc());
    }

    @Test
    public void testJarEnum() {
        ExtensionProtocol jarProtocol = ExtensionProtocol.JAR;
        System.out.println("JAR protocol: " + jarProtocol.getCode() + " - " + jarProtocol.getDesc());
        
        assertEquals("JAR的code应该正确", "jar", jarProtocol.getCode());
        assertEquals("JAR的desc应该正确", "jar包", jarProtocol.getDesc());
    }

    @Test
    public void testSupportedProtocolList() {
        assertNotNull("SUPPORTED_PROTOCOL 不应为null", ExtensionProtocol.SUPPORTED_PROTOCOL);
        assertTrue("SUPPORTED_PROTOCOL 应该包含file", 
                  ExtensionProtocol.SUPPORTED_PROTOCOL.contains("file"));
        assertTrue("SUPPORTED_PROTOCOL 应该包含jar", 
                  ExtensionProtocol.SUPPORTED_PROTOCOL.contains("jar"));
        assertEquals("SUPPORTED_PROTOCOL 应该有2个元素", 2, ExtensionProtocol.SUPPORTED_PROTOCOL.size());
        System.out.println("SUPPORTED_PROTOCOL: " + ExtensionProtocol.SUPPORTED_PROTOCOL);
    }

    @Test
    public void testGetCode() {
        assertEquals("FILE.getCode() 应该返回 'file'", "file", ExtensionProtocol.FILE.getCode());
        assertEquals("JAR.getCode() 应该返回 'jar'", "jar", ExtensionProtocol.JAR.getCode());
    }

    @Test
    public void testGetDesc() {
        assertEquals("FILE.getDesc() 应该返回 '普通文件'", "普通文件", ExtensionProtocol.FILE.getDesc());
        assertEquals("JAR.getDesc() 应该返回 'jar包'", "jar包", ExtensionProtocol.JAR.getDesc());
    }

    @Test
    public void testSetCode() {
        ExtensionProtocol protocol = ExtensionProtocol.FILE;
        String originalCode = protocol.getCode();
        protocol.setCode("custom");
        
        assertEquals("code应该被更新", "custom", protocol.getCode());
        
        protocol.setCode(originalCode);
    }

    @Test
    public void testSetDesc() {
        ExtensionProtocol protocol = ExtensionProtocol.JAR;
        String originalDesc = protocol.getDesc();
        protocol.setDesc("自定义描述");
        
        assertEquals("desc应该被更新", "自定义描述", protocol.getDesc());
        
        protocol.setDesc(originalDesc);
    }

    @Test
    public void testEnumValues() {
        ExtensionProtocol[] values = ExtensionProtocol.values();
        assertEquals("应该有2个枚举值", 2, values.length);
        
        boolean hasFile = false;
        boolean hasJar = false;
        
        for (ExtensionProtocol protocol : values) {
            if (protocol == ExtensionProtocol.FILE) {
                hasFile = true;
            }
            if (protocol == ExtensionProtocol.JAR) {
                hasJar = true;
            }
        }
        
        assertTrue("应该包含FILE枚举", hasFile);
        assertTrue("应该包含JAR枚举", hasJar);
    }

    @Test
    public void testValueOf() {
        ExtensionProtocol file = ExtensionProtocol.valueOf("FILE");
        assertEquals("valueOf('FILE') 应该返回 FILE", ExtensionProtocol.FILE, file);
        
        ExtensionProtocol jar = ExtensionProtocol.valueOf("JAR");
        assertEquals("valueOf('JAR') 应该返回 JAR", ExtensionProtocol.JAR, jar);
    }

    @Test
    public void testBaseEnumImplementation() {
        ExtensionProtocol protocol = ExtensionProtocol.FILE;
        assertNotNull("应该实现getDesc()方法", protocol.getDesc());
        assertNotNull("应该实现getCode()方法", protocol.getCode());
    }
}
