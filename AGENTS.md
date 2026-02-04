# AGENTS.md

This file contains build commands and code style guidelines for the Meta Projects repository (Java/Maven multi-module).

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
- Base package: `com.megatron.shared.meta`
- Module pattern: `com.megatron.shared.meta.{module}[.{submodule}].{type}`
- Type suffixes: `common`, `http`, `enums`, `model`, `constants`, `utils`, `exception`, `annotation`, `registry`

### Import Order
1. Standard Java imports (java.*)
2. Third-party imports (org.apache.commons, okhttp3, com.google.guava, etc.)
3. Internal imports (com.megatron.shared.meta.*)
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

### Required Members
- Serializable classes: `private static final long serialVersionUID`
- Classes needing logging: `private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class)`
- Javadoc for all public/protected/package-private members

### Annotations
- SPI extension points: `@SPI("defaultKey")`
- Nullability: `@Nullable`, `@NotNull` (from JetBrains annotations)
- JUnit tests: `@Test`

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
- Framework: JUnit 4.12 (Surefire 3.2.5)
- Test classes extend `TestCase`
- Test methods: `@Test public void testMethodName()`
- Coverage: JaCoCo 0.8.12 (runs automatically with tests)

### Code Quality
- Checkstyle: Use Sun and Google bundled checks (configured in .idea/checkstyle-idea.xml)
- Java version: Target 1.8 (CI uses Java 21)
- Encoding: UTF-8

### Module List
meta-commons-logging, meta-commons-exception, meta-commons-data, meta-commons-data-model,
meta-commons-utils, meta-core, meta-extension, meta-remoting, meta-commons-validation,
meta-commons-serialization (with meta-commons-json-core submodule)

### Common Patterns
- Constants interfaces (e.g., `HttpRemoteConstants`) for shared constants
- Builder pattern for complex object construction (e.g., `BaseResponse.Builder`)
- Package-info.java files for package-level documentation
- Static helper methods in utility classes (e.g., `HttpRemoteUtils`)
