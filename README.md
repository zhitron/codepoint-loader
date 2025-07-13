# Codepoint Loader

## 📄 项目简介

`Codepoint Loader` 是一个用于解析、存储和查询 Unicode 编码点（Code Point）信息的 Java 工具库。该工具支持从标准文件格式中加载编码点数据，并提供便捷的 API 查询字符属性（如类别、名称、平面等）。

---

## 🚀 快速开始

### 构建要求

- JDK 21 或以上（推荐使用 JDK 21）
- Maven 3.x

### 添加依赖

你可以通过 Maven 引入该项目：

```xml
<dependency>
    <groupId>com.github.zhitron</groupId>
    <artifactId>codepoint-loader</artifactId>
    <version>1.0.1</version>
</dependency>
```

###  使用案例

```java
package com.github.zhitron.codepoint_loader;

import org.junit.Test;

import java.io.File;

/**
 * @author zhitron
 */
public class CodepointLoaderTest {

    public static void main(String[] args) throws Exception {
        CodepointLoaderTest codepointLoaderTest = new CodepointLoaderTest();
        codepointLoaderTest.test1();
        codepointLoaderTest.test1();
    }

    @Test
    public void test1() throws Exception {
        File file = new File("README.md");
        StringBuilder sb = new StringBuilder();
        try (CodepointLoader loader = CodepointLoaderFactory.of(file)) {
            while (loader.hasNextCodepoint()) {
                sb.appendCodePoint(loader.nextCodepoint());
            }
        }
        System.out.println(sb);
    }

    @Test
    public void test2() throws Exception {
        File file = new File("README.md");
        StringBuilder sb = new StringBuilder();
        try (CodepointLoader loader = CodepointLoaderFactory.of(file)) {
            int codepoint;
            while ((codepoint = loader.popCodepoint()) != -1) {
                sb.appendCodePoint(codepoint);
            }
        }
        System.out.println(sb);
    }
}
```

---

## 🧩 功能特性

- 支持从 Unicode 标准文件加载编码点数据。
- 提供编码点的基本信息查询接口（如字符类型、名称、平面等）。
- 支持自定义数据格式解析。
- 高性能查找与内存优化设计。

---

## ✍️ 开发者

- **Zhitron**
- 邮箱: zhitron@foxmail.com
- 组织: [Zhitron](https://github.com/zhitron)

---

## 📦 发布状态

当前版本：`1.0.1`

该项目已发布至 [Maven Central](https://search.maven.org/)，支持快照版本与正式版本部署。

---

## 🛠 源码管理

GitHub 地址：https://github.com/zhitron/codepoint-loader

使用 Git 进行版本控制：

```bash
git clone https://github.com/zhitron/codepoint-loader.git
```


---

## 📚 文档与社区

- Javadoc 文档可通过 `mvn javadoc:javadoc` 生成。
- 如有问题或贡献，请提交 Issues 或 PR 至 GitHub 仓库。

---

## 📎 License

Apache License, Version 2.0  
详见 [LICENSE](https://www.apache.org/licenses/LICENSE-2.0.txt)

---

如果你有具体的代码结构或功能说明，可以提供更多细节，我可以帮你进一步补充文档内容。