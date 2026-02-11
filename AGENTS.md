# AGENTS.md

This file contains build commands and code style guidelines for the Meta Projects repository (Java/Maven multi-module).

## 项目现状

当前项目包含以下模块，其中部分模块已完成开发，部分仍在开发中：

### 已完成模块
- ✅ **meta-commons-data**: 核心数据处理库，包含完整的测试用例(187个测试)
- ✅ **meta-commons-serial**: 序列化框架，包含SPI接口和多种实现
- ✅ **meta-bom**: Maven依赖版本管理

### 开发中模块
- ⚠️ **meta-commons-data-ext**: 数据模型扩展
- ⚠️ **meta-commons-logging**: 日志记录工具
- ⚠️ **meta-commons-utils**: 通用工具类集合
- ⚠️ **meta-core**: 核心功能模块
- ⚠️ **meta-extension**: 扩展机制模块
- ⚠️ **meta-remoting**: 远程调用模块
- ⚠️ **meta-commons-validation**: 数据验证工具

## Build Commands

### Build all modules
```bash
cd meta-bom && mvn clean install
```

### Build single module
```bash
cd <module-name> && mvn clean install
```

### Run all tests
```bash
cd meta-bom && mvn test
```

### Run single test class
```bash
cd <module-name> && mvn test -Dtest=ClassName
```

### Run single test method
```bash
cd <module-name> && mvn test -Dtest=ClassName#methodName
```

### Generate coverage report
```bash
cd meta-bom && mvn clean test jacoco:report
```

## Code Style Guidelines

### Package Structure
- Base package: `com.softmegatron.shared.meta`
- Module pattern: `com.softmegatron.shared.meta.{module}[.{submodule}].{type}`
- Type suffixes: `common`, `http`, `enums`, `model`, `constants`, `utils`, `exception`, `annotation`, `registry`

### Import Order
1. Standard Java imports (java.*)
2. Third-party imports (org.apache.commons, okhttp3, com.google.guava, etc.)
3. Internal imports (com.softmegatron.shared.meta.*)
4. Static imports (`import static`)

Separate each group with a blank line.

### Naming Conventions
- Classes: PascalCase (`RemoteService`, `HttpRemoteServiceImpl`)
- Interfaces: PascalCase, descriptive of type (`RemoteService`, `BaseEnum`)
- Enums: PascalCase (`RemoteProtocol`, `HttpMethod`)
- Interface constants: UPPER_SNAKE_CASE (`DEFAULT_CONN_TIMEOUT`)
- Class constants: UPPER_SNAKE_CASE (`EXTENSION_TYPE_CACHE`)
- Methods: camelCase (`invoke`, `getParams`)
- Variables/parameters: camelCase (`invocation`, `builder`)
- Packages: lowercase (`http`, `utils`, `common`)

### Class Structure
```java
package ...;

import ...;
import ...;

// Javadoc header
public class ClassName extends BaseType {
    private static final long serialVersionUID = <generated>;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class);

    // Static constants
    // Instance fields
    // Constructors
    // Public methods
    // Private methods
}
```

### Base Classes & Interfaces
- Models extend `BaseModel`
- Enums implement `BaseEnum` (requires `getDesc()`)
- Responses use `BaseResponse<T>` with Builder pattern
- Exceptions extend `RuntimeException`
- Serializable classes extend `BaseSerializable`

### Required Members
- Serializable classes: `private static final long serialVersionUID`
- Classes needing logging: `private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class)`
- Javadoc for all public/protected/package-private members

### Annotations
- SPI extension points: `@SPI("defaultKey")`
- Nullability: `@Nullable`, `@NotNull` (from JetBrains annotations)
- JUnit tests: `@Test`
- Validation: JSR-303 annotations (`@NotNull`, `@NotEmpty`, etc.)

### Generics
- Single uppercase letters: `<T>`, `<R>`, `<P>`, `<I>`
- Type bounds: `I extends RemoteInvocation, R extends RemoteResponse`

### Static Imports
Preferred for: constants, Preconditions (`checkNotNull`, `checkArgument`), time units (`MILLISECONDS`)

### Error Handling
- Custom exceptions extend `RuntimeException`
- Use `Preconditions.checkNotNull()` and `checkArgument()` for validation
- Log errors at ERROR level: `LOGGER.error("message", exception)`

### Validation
- Framework: JSR-303 Bean Validation (javax.validation)
- Use `@NotNull`, `@NotEmpty`, `@Size`, etc. annotations on fields
- Validate with `ValidatorUtils.validate()` or manual validator
- Validation failures throw `ViolationException`

### Testing
- Framework: JUnit 4.13.2 (Surefire 3.2.5)
- Test coverage: JaCoCo 0.8.12
- Test classes naming: `*Test.java`
- Test methods: `@Test public void testMethodName()`
- Assertion library: JUnit Assert
- Mock framework: Mockito (when needed)

### Code Quality
- Checkstyle: Use Sun and Google bundled checks
- Java version: Target 17
- Encoding: UTF-8
- Line endings: LF (Unix style)

### Current Module List
```
meta-bom/                    # ✅ 完整 - 依赖版本管理
meta-commons-data/          # ✅ 完整 - 数据处理基础类(187个测试)
├── base/                   # 基础数据模型和接口
├── constants/              # 常量定义
├── enums/                  # 枚举类型
├── serial/                 # 序列化相关工具
└── utils/                  # 数据处理工具类
meta-commons-data-ext/      # ⚠️ 开发中 - 数据模型扩展
meta-commons-logging/       # ⚠️ 开发中 - 日志记录工具
meta-commons-utils/         # ⚠️ 开发中 - 通用工具类集合
meta-core/                  # ⚠️ 开发中 - 核心功能模块
meta-extension/             # ⚠️ 开发中 - 扩展机制模块
meta-remoting/              # ⚠️ 开发中 - 远程调用模块
meta-commons-validation/    # ⚠️ 开发中 - 数据验证工具
meta-commons-serial/        # ✅ 完整 - 序列化框架
├── meta-commons-serial-spi/     # SPI接口定义
├── meta-commons-serial-fastjson/ # FastJSON实现
├── meta-commons-serial-jackson/  # Jackson实现
└── meta-commons-serial-lang/     # Java原生实现
```

### Common Patterns
- Constants interfaces (e.g., `MetaConstants`) for shared constants
- Builder pattern for complex object construction (e.g., `BaseResponse.Builder`)
- Package-info.java files for package-level documentation
- Static helper methods in utility classes (e.g., `RequestUtils`)
- Reflection caching for performance optimization
- SPI-based plugin architecture for extensibility

### Performance Considerations
- Use reflection caching for frequently accessed class metadata
- Implement lazy initialization where appropriate
- Consider thread safety in concurrent environments
- Profile performance bottlenecks before optimization

### Documentation Requirements
- All public APIs must have Javadoc
- Complex algorithms should have inline comments
- Package-level documentation in package-info.java
- README.md updates for major feature additions