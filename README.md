# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.1/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.1/maven-plugin/build-image.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/4.1.1/specification/configuration-metadata/annotation-processor.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.1.1/reference/using/devtools.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.1/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

---

## Project Build Lifecycle & Spring Boot Architecture Documentation

This document serves as a chronological guide to the ProductService Spring Boot project, documenting how the application was built from the ground up using the MVC (Model-View-Controller) architecture pattern.

---

## Phase 1: Project Foundation & Core Dependencies

### Step 1.1: Initial Project Setup with Maven POM Configuration

**Status**: ✅ Completed

**Objective**: Establish the foundation of a Spring Boot application with essential dependencies for web service and data access.

**Dependencies Added to `pom.xml`**:

```xml
<!-- Spring Boot Web MVC Starter - REST API and Web Controller support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- Spring Boot Data JPA - Object-Relational Mapping (ORM) and Database Access -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>4.1.0</version>
    <scope>compile</scope>
</dependency>

<!-- MySQL Connector - JDBC Driver for MySQL Database Communication -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>26.7.0</version>
    <scope>compile</scope>
</dependency>

<!-- Lombok - Code Generation for Getters, Setters, and Constructors -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Spring Boot DevTools - Hot Reload and Live Reload Support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**Architectural Role**:
- **Web MVC**: Enables Controller layer to handle HTTP requests and responses
- **Data JPA**: Enables Model layer with database persistence via Hibernate ORM
- **MySQL Connector**: Facilitates database communication (Model persistence)
- **Lombok**: Reduces boilerplate code in entity classes

**Java Version**: Java 17 (specified in `properties`)

---

## Phase 2: Application Configuration & Bean Management

### Step 2.1: Create Spring Configuration Class with RestTemplate Bean

**Status**: ✅ Completed

**Location**: `src/main/java/org/example/productservice/configs/AppConfig.java`

**Objective**: Configure application-level beans and external service communication capability.

**File Content**:

```java
package org.example.productservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Configuration marks a class as a Spring configuration class.
 * Spring scans it and treats it as a source of bean definitions.
 */
@Configuration
public class AppConfig {
    
    /**
     * @Bean marks a method whose return value is managed by Spring
     * as an object (bean) in the application context.
     * RestTemplate becomes injectable via @Autowired or constructor injection.
     */
    @Bean
    public RestTemplate createRestBeanTemplate(){
        return new RestTemplate();
    }
}
```

**Architectural Role**:
- **Type**: Application Configuration (Cross-cutting infrastructure)
- **Purpose**: Centralized bean definition for Spring's Dependency Injection (DI) container
- **RestTemplate**: HTTP client for invoking external REST APIs (used by Service layer to call third-party services)

**MVC Context**:
- **Service Layer Usage**: Services inject this bean to communicate with other microservices
- **Controller**: Controllers don't directly use RestTemplate; Services handle external calls

---

## Phase 3: Model Layer - Entity Design & Database Persistence

### Step 3.1: Update BaseModel with JPA Inheritance Annotations

**Status**: ✅ Completed

**Location**: `src/main/java/org/example/productservice/models/BaseModel.java`

**Objective**: Establish a base entity class that all domain models inherit from, implementing the Mapped Superclass inheritance strategy.

**Previous State**:
```java
@Getter
@Setter
public class BaseModel {
    private Long id;
    private Date createdAt;
    private Date lastModifiedAt;
}
```

**Updated State**:
```java
package org.example.productservice.models;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@MappedSuperclass  // Marks this class as a JPA Mapped Superclass (Inheritance strategy)
public class BaseModel {
    @Id  // Designates id as the primary key for all child entities
    private Long id;
    private Date createdAt;
    private Date lastModifiedAt;
}
```

**Key Annotations Explained**:
- `@MappedSuperclass`: Declares that this class is NOT an entity itself but provides mapped fields to child entities
- `@Id`: JPA annotation specifying the primary key field
- `@Getter/@Setter`: Lombok annotations generating getter and setter methods at compile time

**Architectural Role**:
- **MVC Layer**: Model (Data Access Layer)
- **Purpose**: Base entity ensuring all domain models have consistent id, audit timestamps (createdAt, lastModifiedAt)
- **Database Mapping**: Child entities inherit these fields directly in their respective tables (no separate base table)

---

### Step 3.2: Create Product Entity with Category Relationship

**Status**: ✅ Completed

**Location**: `src/main/java/org/example/productservice/models/Product.java`

**Objective**: Define the Product entity as a core domain model representing items available in the product catalog.

**File Content**:
```java
package org.example.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "products")  // Maps this class to the "products" table
public class Product extends BaseModel {
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    
    @ManyToOne  // Many products can belong to one category
    private Category category;
}
```

**Key Annotations**:
- `@Entity`: Marks this class as a persistent entity
- `@ManyToOne`: Defines a many-to-one relationship with Category
- Inherits `id`, `createdAt`, `lastModifiedAt` from `BaseModel`

**Architectural Role**:
- **MVC Layer**: Model (Domain Entity)
- **Purpose**: Core business domain representing a product in the catalog
- **Database**: Persisted in `products` table with columns: `id`, `title`, `description`, `price`, `imageUrl`, `category_id`

---

### Step 3.3: Create Category Entity

**Status**: ✅ Completed

**Location**: `src/main/java/org/example/productservice/models/Category.java`

**Objective**: Define the Category entity for organizing products into logical categories.

**Architectural Role**:
- **MVC Layer**: Model (Domain Entity)
- **Purpose**: Parent entity in one-to-many relationship with Product
- **Relationship**: One category has many products

---

## Phase 4: Database Configuration & Connection Setup

### Step 4.1: Configure MySQL Database Connection

**Status**: ✅ Completed

**Location**: `src/main/resources/application.properties`

**Updated Configuration**:
```properties
# Logging Configuration
logging.level.root=WARN
logging.level.org.springframework.boot=INFO

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update  # Auto-update schema on startup
spring.jpa.show-sql=true              # Log SQL queries to console

# MySQL Database Connection
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/productservicesep26
spring.datasource.username=productservicesep26
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Configuration Details**:
- **Database Name**: `productservicesep26`
- **User**: `productservicesep26`
- **Host**: Configurable via environment variable `MYSQL_HOST` (defaults to `localhost`)
- **Port**: 3306 (default MySQL port)
- **DDL Strategy**: `update` - Automatically creates/updates schema based on entity definitions
- **SQL Logging**: Enabled for debugging

**Architectural Role**:
- **Infrastructure**: Database connectivity layer
- **Purpose**: Bridges the Model layer with persistent data storage in MySQL
- **Spring Data JPA**: Uses these settings to establish database connections for CRUD operations

---

## Phase 5: Entity Inheritance Patterns - Educational Demonstrations

### Step 5.1: Implement 4 JPA Inheritance Strategies

**Status**: ✅ Completed

**Objective**: Demonstrate all four JPA inheritance strategies through practical examples using a User hierarchy.

**Location**: `src/main/java/org/example/productservice/inheritancedemo/`

**Project Structure**:
```
inheritancedemo/
├── joinedtable/
│   ├── User.java         (@Entity, @Inheritance(JOINED))
│   ├── Instructor.java
│   ├── Mentor.java
│   └── TA.java
├── mappedsuperclass/
│   ├── User.java         (@MappedSuperclass)
│   ├── Instructor.java
│   ├── Mentor.java
│   └── TA.java
├── singletable/
│   ├── User.java         (@Entity, @Inheritance(SINGLE_TABLE))
│   ├── Instructor.java
│   ├── Mentor.java
│   └── TA.java
└── tableperclass/
    ├── User.java         (@Entity, @Inheritance(TABLE_PER_CLASS))
    ├── Instructor.java
    ├── Mentor.java
    └── TA.java
```

#### Strategy 1: Joined Table Inheritance

**Location**: `inheritancedemo/joinedtable/`

**Parent Class (User.java)**:
```java
@Entity(name = "jt_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
}
```

**Child Class Example (Instructor.java)**:
```java
@Entity(name = "jt_instructor")
@PrimaryKeyJoinColumn(name = "user_id")
public class Instructor extends User {
    private String department;
    private Double salary;
}
```

**Database Mapping**:
- Parent table: `jt_user` (id, name, email, password, dtype)
- Child table: `jt_instructor` (user_id, department, salary)
- **Joins** child and parent tables using foreign key

**Pros**: Normalized schema, minimal data redundancy
**Cons**: Complex queries requiring multiple JOINs

---

#### Strategy 2: Mapped Superclass Inheritance

**Location**: `inheritancedemo/mappedsuperclass/`

**Base Class (User.java)**:
```java
@MappedSuperclass  // Not an entity itself
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
}
```

**Child Class Example (Instructor.java)**:
```java
@Entity(name = "ms_instructor")
public class Instructor extends User {
    private String department;
    private Double salary;
}
```

**Database Mapping**:
- Only child tables created: `ms_instructor` (id, name, email, password, department, salary)
- No parent table exists
- Each child inherits parent fields directly

**Pros**: Simplicity, single table queries
**Cons**: Duplication of parent fields across tables

---

#### Strategy 3: Single Table Inheritance

**Location**: `inheritancedemo/singletable/`

**Parent Class (User.java)**:
```java
@Entity(name = "st_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")  // Discriminator column to distinguish types
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
}
```

**Child Class Example (Instructor.java)**:
```java
@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User {
    private String department;
    private Double salary;
}
```

**Database Mapping**:
- Single table: `st_user` (id, name, email, password, department, salary, user_type)
- `user_type` discriminator distinguishes between User types (e.g., "INSTRUCTOR", "MENTOR", "TA")

**Pros**: Fast queries, single table lookup
**Cons**: Nullable columns, schema design complexity

---

#### Strategy 4: Table Per Class Inheritance

**Location**: `inheritancedemo/tableperclass/`

**Parent Class (User.java)**:
```java
@Entity(name = "tpc_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
}
```

**Child Class Example (Instructor.java)**:
```java
@Entity(name = "tpc_instructor")
public class Instructor extends User {
    private String department;
    private Double salary;
}
```

**Database Mapping**:
- Parent table: `tpc_user` (id, name, email, password)
- Child table: `tpc_instructor` (id, name, email, password, department, salary)
- Each class has its **own complete table** with all fields

**Pros**: Flexible, independent tables per type
**Cons**: Duplicate columns, complex polymorphic queries

---

### Step 5.2: Inheritance Strategy Comparison Matrix

| Strategy | Parent Table | Child Tables | Discriminator | Query Performance | Storage Efficiency | Polymorphic Query |
|----------|:---:|:---:|:---:|:---:|:---:|:---:|
| **Joined Table** | ✅ Yes | ✅ Yes | No | Medium | High | Medium |
| **Mapped Superclass** | ❌ No | ✅ Yes | No | Fast | Low | Difficult |
| **Single Table** | ✅ Yes | ❌ No | ✅ Yes | Very Fast | Low | Very Easy |
| **Table Per Class** | ✅ Yes | ✅ Yes | No | Slow | Low | Slow |

**Recommendation for ProductService**:
- **BaseModel + Product**: Uses **Mapped Superclass** (current implementation)
- **Reason**: Simple hierarchy, straightforward queries, minimal complexity

---

## Complete Project Folder Structure

```
ProductService/
│
├── pom.xml                                    # Maven configuration
├── HELP.md                                    # This documentation
│
└── src/main/java/org/example/productservice/
    │
    ├── ProductServiceApplication.java         # Spring Boot main class
    │
    ├── configs/
    │   └── AppConfig.java                    # @Configuration with @Bean(RestTemplate)
    │
    ├── models/                               # MODEL LAYER
    │   ├── BaseModel.java                    # @MappedSuperclass (parent)
    │   ├── Product.java                      # @Entity extends BaseModel
    │   └── Category.java                     # @Entity
    │
    ├── dto/                                  # Data Transfer Objects (API layer)
    │   └── [DTOs for REST API contracts]
    │
    ├── controllers/                          # CONTROLLER LAYER
    │   └── [REST endpoints, HTTP mapping]
    │
    ├── service/                              # BUSINESS LOGIC LAYER
    │   └── [Service classes, business rules]
    │
    ├── controlleradvice/                     # EXCEPTION HANDLING
    │   └── [Global exception handlers]
    │
    └── inheritancedemo/                      # EDUCATIONAL: JPA Inheritance Patterns
        ├── joinedtable/
        │   ├── User.java
        │   ├── Instructor.java
        │   ├── Mentor.java
        │   └── TA.java
        ├── mappedsuperclass/
        │   ├── User.java
        │   ├── Instructor.java
        │   ├── Mentor.java
        │   └── TA.java
        ├── singletable/
        │   ├── User.java
        │   ├── Instructor.java
        │   ├── Mentor.java
        │   └── TA.java
        └── tableperclass/
            ├── User.java
            ├── Instructor.java
            ├── Mentor.java
            └── TA.java
```

---

## MVC Architecture Overview

### The Model-View-Controller Pattern in ProductService

```
    HTTP Request
         ↓
    [CONTROLLER LAYER]
    - REST Endpoints (@RestController)
    - Request Routing (@RequestMapping)
    - Path Variable/Request Param Binding
         ↓
    [SERVICE LAYER]
    - Business Logic
    - Transaction Management (@Transactional)
    - RestTemplate for external calls
         ↓
    [REPOSITORY LAYER]
    - Spring Data JPA Repositories
    - Database Query Methods (extend JpaRepository)
         ↓
    [MODEL/ENTITY LAYER]
    - @Entity classes (Product, Category)
    - @MappedSuperclass (BaseModel)
    - Relationship mappings (@ManyToOne, @OneToMany)
         ↓
    [DATABASE]
    - MySQL persistence
    - Tables: products, categories, users, etc.
         ↓
    HTTP Response (JSON)
```

### Data Flow Example: GET /products

1. **Request**: Client calls `GET /products`
2. **Controller**: `ProductController.getAllProducts()` receives request
3. **Service**: `ProductService.getAll()` fetches business logic
4. **Repository**: `ProductRepository.findAll()` queries database via JPA
5. **Model**: Product entities loaded from `products` table
6. **View**: Spring MVC serializes entities to JSON
7. **Response**: JSON array of products returned to client

---

## Key Spring Framework Concepts Used

### 1. Dependency Injection (DI)
- **AppConfig**: `@Bean` creates singleton RestTemplate managed by Spring
- **Service Layer**: Injects RestTemplate and Repositories via `@Autowired` or constructor injection

### 2. Spring Data JPA
- **Repository Pattern**: Automatic CRUD operations via JpaRepository
- **Entity Mapping**: `@Entity`, `@Id`, `@ManyToOne` map Java classes to database tables
- **Inheritance**: `@MappedSuperclass` for code reuse across entities

### 3. REST API Controller
- **@RestController**: Returns JSON responses
- **@RequestMapping**: Maps HTTP routes to handler methods
- **HTTPMessageConverter**: Automatically serializes entities to JSON

### 4. Transaction Management
- **@Transactional**: Ensures database consistency across multiple operations
- **Hibernate**: ORM framework managing entity persistence

---

## How to Extend the Project

### Add a New Entity (Model)

1. Create class extending `BaseModel`
2. Add `@Entity` annotation
3. Define business fields
4. Create corresponding Repository extending `JpaRepository<YourEntity, Long>`

### Add a New REST Endpoint (Controller)

1. Create `@RestController` class
2. Inject `Service` via constructor
3. Define `@GetMapping`, `@PostMapping`, etc. methods
4. Return DTOs or entities (serialized to JSON)

### Add Business Logic (Service)

1. Create `@Service` class
2. Inject `Repository` and `RestTemplate` (from AppConfig)
3. Implement business methods
4. Optionally inject other services for composition

---

## Summary of Implementation Phases

| Phase | Component | Status | Key Files |
|-------|-----------|--------|-----------|
| 1 | Dependencies & Maven Config | ✅ | pom.xml |
| 2 | Spring Configuration & Beans | ✅ | AppConfig.java |
| 3 | Entity Model Design | ✅ | BaseModel, Product, Category |
| 4 | Database Configuration | ✅ | application.properties |
| 5 | Inheritance Patterns (Educational) | ✅ | inheritancedemo/* |

This documentation provides a complete roadmap of how the ProductService Spring Boot application was built, from foundational dependencies to entity inheritance patterns, all centered around the MVC architecture paradigm.

