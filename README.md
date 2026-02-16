<div style="text-align: center">
  <picture>
    <img src="https://cdn.softmegatron.com/favicon/apple-touch-icon.png" width="250" alt="SoftMegatron Logo">
  </picture>
  <h1>SoftMegatron, Inc.</h1>
  <p>开源项目开发 · 商业技术服务 · 企业级解决方案</p>
  <p>基于商业智能打造的技术品牌，专注于高效、可靠、温暖的技术产品研发</p>
</div>

---

# Meta Projects

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
![build_status](https://github.com/SoftMegatron/meta-projects/actions/workflows/maven.yml/badge.svg)
[![Maintainability](https://qlty.sh/gh/SoftMegatron/projects/meta-projects/maintainability.svg)](https://qlty.sh/gh/SoftMegatron/projects/meta-projects)
[![codecov](https://codecov.io/github/SoftMegatron/meta-projects/graph/badge.svg?token=3CFXBDSBC2)](https://codecov.io/github/SoftMegatron/meta-projects)
[![Known Vulnerabilities](https://snyk.io/test/github/SoftMegatron/meta-projects/badge.svg)](https://snyk.io/test/github/SoftMegatron/meta-projects)

## 项目简介

`Meta Projects` 是一个专注于提供高效、稳定的基础工具库和框架的项目集合。它包括多个子模块，涵盖了数据处理、序列化、日志记录、工具类、核心功能、扩展机制、远程调用、验证工具等多个方面，旨在为开发者提供一套完整的解决方案。

## 目录结构

```plaintext
meta-projects/
├── meta-bom/                   # Maven Bill of Materials (BOM) 管理依赖版本
├── meta-data/          # 数据处理基础类库
├── meta-data-ext/      # 数据模型扩展定义
├── meta-logging/       # 日志记录工具
├── meta-core/                  # 核心功能模块
├── meta-extension/             # 扩展机制模块
├── meta-remoting/              # 远程调用模块
├── meta-validation/    # 数据验证工具
└── meta-serial/        # 序列化模块, 包括spi及fastjson2、jackson和commons-lang3序列化实现
```

## 模块状态

| 模块名称 | 状态 | 描述 |
|---------|------|------|
| meta-bom | ✅ 完成 | Maven依赖版本管理 |
| meta-serial | ✅ 完成 | 序列化框架，SPI及其实现 |
| meta-data | ✅ 完成 | 核心数据模型 |
| meta-data-ext | ⚠️ 开发中 | 数据模型扩展 |
| meta-logging | ✅ 完成 | 日志工具 |
| meta-core | ⚠️ 开发中 | 核心功能 |
| meta-extension | ⚠️ 开发中 | 扩展机制 |
| meta-remoting | ⚠️ 开发中 | 远程调用 |
| meta-validation | ⚠️ 开发中 | 验证工具 |
| meta-monitoring | 📅 计划中 | 监控模块 |

### TODO
* meta-logging
    * LogFilter
* meta-monitoring

## 安装指南

### 环境准备

确保已安装以下环境：
- Java 17 或更高版本
- Maven 3.6 或更高版本

### 下载项目

可以通过 Git 克隆项目仓库：

```bash
git clone https://github.com/SoftMegatron/meta-projects.git
cd meta-projects
```

### 构建项目

在项目根目录下执行以下命令进行构建：

```bash
# 构建所有模块
cd meta-bom && mvn clean install

# 构建单个模块
cd meta-data && mvn clean install
```

### 运行测试

```bash
# 运行所有测试
cd meta-bom && mvn test

# 运行单个模块测试
cd meta-data && mvn test

# 生成覆盖率报告
cd meta-bom && mvn clean test jacoco:report
```

## 使用说明

### 引入依赖

在你的 `pom.xml` 文件中添加以下依赖：

```xml
<!-- 基础数据处理模块 -->
<dependency>
   <groupId>com.softmegatron.shared</groupId>
   <artifactId>meta-data</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 序列化模块 jackson实现 -->
<dependency>
   <groupId>com.softmegatron.shared</groupId>
   <artifactId>meta-serial-jackson</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 根据需要添加其他模块 -->
```

### 示例代码

#### 数据处理示例

```java
import com.softmegatron.shared.meta.data.base.PageRequest;
import com.softmegatron.shared.meta.data.utils.RequestUtils;

public class DataExample {
    public static void main(String[] args) {
        // 创建分页请求
        PageRequest request = new PageRequest(1, 20);
        
        // 使用工具类获取安全的分页参数
        int currentPage = RequestUtils.getCurrentPage(request);
        int pageSize = RequestUtils.getPageSize(request);
        int offset = RequestUtils.getOffset(request);
        
        System.out.println("当前页: " + currentPage);
        System.out.println("页面大小: " + pageSize);
        System.out.println("偏移量: " + offset);
    }
}
```

#### 序列化示例

```java
import com.softmegatron.shared.meta.data.serial.DefaultObjectSerializer;
import com.softmegatron.shared.meta.data.base.BaseSerializable;

public class SerializationExample {
    public static void main(String[] args) {
        // 使用默认序列化器
        MyDataObject obj = new MyDataObject("test", 123);
        String serialized = DefaultObjectSerializer.toString(obj);
        System.out.println("序列化结果: " + serialized);
    }
    
    static class MyDataObject extends BaseSerializable {
        private String name;
        private int value;
        
        public MyDataObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
        
        // getters and setters...
    }
}
```

## 开发指南

### 代码规范

请参考 [AGENTS.md](AGENTS.md) 文件了解详细的代码风格指南和构建命令。

### 测试要求

- 所有公共方法都需要单元测试覆盖
- 测试覆盖率目标：80%以上
- 使用JUnit 4.13.2进行测试
- 集成JaCoCo进行代码覆盖率分析

## 贡献指南

欢迎任何开发者为本项目做出贡献！请遵循以下步骤：

1. **Fork** 本仓库
2. 创建一个新的分支 (`git checkout -b feature/new-feature`)
3. 提交你的更改 (`git commit -am 'feat: Add some feature'`)
4. 推送到分支 (`git push origin feature/new-feature`)
5. 提交 Pull Request

### 提交规范

请遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：
- `feat:` 新功能
- `fix:` 修复bug
- `docs:` 文档更新
- `style:` 代码格式调整
- `refactor:` 代码重构
- `test:` 测试相关
- `chore:` 构建过程或辅助工具变动

## 许可证

本项目采用 [MIT](LICENSE) 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 联系方式

如有任何问题或建议，请通过以下方式联系我们：

- GitHub Issues: [https://github.com/SoftMegatron/meta-projects/issues](https://github.com/SoftMegatron/meta-projects/issues)
- Email: opensource@softmegatron.com