# Spring Security 演示项目

这是一个Spring Security演示项目，展示了基本的认证和基于角色的授权功能。

## 功能特点

- 基于表单的认证
- 基于角色的授权控制
- 内存和数据库认证
- 用户注册和管理
- REST API安全配置
- 独立的管理员和用户仪表板

## 技术栈

- Spring Boot 2.7.16
- Spring Security
- Spring Data JPA
- Thymeleaf (模板引擎)
- MySQL数据库
- Maven构建工具

## 快速开始

### 前提条件

- JDK 21
- Maven
- MySQL数据库

### 配置数据库

在`application.properties`中配置您的数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_security_rbac_demo?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### 运行应用

```bash
mvn spring-boot:run
```

应用将在 http://localhost:8080 运行。

### 默认用户

应用启动后，可以使用以下默认用户登录：

- 用户名: user / 密码: password (USER角色)
- 用户名: admin / 密码: password (ADMIN角色)

也可以通过 `/api/users/register` 接口注册新用户。

## API接口

### 用户注册

```
POST /api/users/register
参数: username, password, type(可选，设置为"admin"时注册管理员)
```

## 安全说明

本项目仅用于演示目的，生产环境使用需要进一步加强安全措施。