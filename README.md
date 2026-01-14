"# academy-backend" 

---

## ✅ Decisions Locked (No Ambiguity)

### ✔ Scope

**Production-style project**

* Layered architecture
* DTOs, validation, exception handling
* Clean package structure
* SQL logging enabled

---

### ✔ API Style

**REST controllers from Day 1**

* You’ll always see request → service → JPA → SQL
* No “hidden” repository magic

---

### ✔ Query Discipline (Very Important)

**Derived queries: STRICTLY FORBIDDEN initially**

* ❌ `findByName`, `findByAge`, etc.
* ✅ JPQL first
* ✅ Specifications for search/filter
* Derived queries will be **introduced later deliberately**, not casually

This directly fixes the issue you raised 👏

---

### ✔ Database

**PostgreSQL**

* Real-world behavior
* Real SQL
* Real constraints
* No H2 shortcuts

---

### ✔ Pace

**Feature-by-feature**

* Build one feature end-to-end
* Learn JPA concepts *while implementing*
* Refactor consciously

This is how senior engineers learn.

---

## 🧱 Project Blueprint (Learning + Production)

### Project Name (suggested)

`academy-backend`
(or you can rename later)

---

### Core Domain (Final)

We’ll build this incrementally:

1️⃣ Student
2️⃣ Course
3️⃣ Teacher
4️⃣ Enrollment (join entity)

Each step adds **new JPA concepts**.

---

## 📦 Package Structure (Final)

```text
com.example.academy
├── api
│   └── v1
│       ├── controller
│       ├── dto
│       │   ├── request
│       │   └── response
│       ├── mapper
│       └── specification
├── domain
│   ├── student
│   ├── course
│   ├── teacher
│   └── enrollment
├── repository
├── service
│   └── impl
├── exception
└── AcademyApplication.java
```

Why this matters:

* Domain-first thinking
* API versioning baked in
* Easy to scale

---

## 🧠 Learning Rules (Non-Negotiable)

We will follow these rules throughout the project:

1. **No derived queries** unless explicitly justified
2. Every query must answer:

   * Why JPQL?
   * Why Specification?
3. SQL logs must be readable and discussed
4. Entity design comes **before** repository design
5. Transactions always explicit (`@Transactional`)
6. Controllers never touch entities directly

---

## 🚀 Project Roadmap (High Level)

### 🔹 Phase A – Student (Foundation)

* Student entity
* Student CRUD (JPQL only)
* DTO + validation
* Basic pagination (JPQL)
* SQL analysis

👉 Concepts learned:

* Entity lifecycle
* JPQL basics
* Transaction boundaries

---

### 🔹 Phase B – Course

* OneToMany / ManyToOne
* Owning side mastery
* JOIN FETCH
* N+1 problem (real example)

👉 Concepts learned:

* Relationship mapping
* Fetch strategies
* Performance thinking

---

### 🔹 Phase C – Teacher

* Many Courses → One Teacher
* LAZY defaults
* Query tuning

---

### 🔹 Phase D – Enrollment

* Replace ManyToMany with join entity
* Composite logic
* Advanced Specifications

---

## 🧪 Tooling (From Day 1)

* PostgreSQL
* Hibernate SQL logging ON
* Postman
* Maven
* IntelliJ

---

## 🧠 What You’ll Gain (Real Outcome)

By the end of this project, you will be able to:

✅ Design entities confidently
✅ Choose JPQL vs Specification intentionally
✅ Read Hibernate SQL without fear
✅ Avoid N+1 and cascade disasters
✅ Explain JPA decisions in interviews
✅ Refactor existing JPA projects cleanly

This directly addresses the discomfort you started with.

---

Here’s a clean, professional **`README.md`** document generated from your steps, suitable for a real project repository.

---

# Academy Backend – Spring Boot Setup Guide

This document explains how to create, configure, and run the **Academy Backend** Spring Boot application using Java 21, PostgreSQL, and JPA.

---

## 🛠 Prerequisites

Ensure the following are installed on your system:

* Java **21**
* Maven
* IntelliJ IDEA (recommended)
* PostgreSQL
* pgAdmin 4

---

## 🚀 Project Creation

### Step 1: Generate Project from Spring Initializr

1. Go to **Spring Initializr**
2. Configure the project with the following details:

#### Project Configuration

* **Project**: Maven
* **Language**: Java
* **Spring Boot**: 4.0.1

#### Project Metadata

* **Group**: `academy`
* **Artifact**: `academy-backend`
* **Name**: `academy-backend`
* **Description**: Student, Teacher, Courses
* **Package Name**: `academy.academy-backend`

#### Packaging & Java

* **Packaging**: JAR
* **Java Version**: 21

#### Dependencies

* Spring Web
* Spring Data JPA
* PostgreSQL Driver

3. Click **GENERATE**
4. Download the ZIP file
5. Extract it
6. Open the project in **IntelliJ IDEA**

---

## 🗄 Database Setup

### Step 2: Create PostgreSQL Database

1. Open **pgAdmin 4**
2. Create a new database with the name:

```
academy_db
```

---

## ⚙️ Application Configuration

### Step 3: Configure `application.properties`

Navigate to:

```
src/main/resources/application.properties
```

Add the following configuration:

```properties
spring.application.name=academy-backend
server.port=8080

# --- PostgreSQL DataSource ---
spring.datasource.url=jdbc:postgresql://localhost:5432/academy_db
spring.datasource.username=postgres
spring.datasource.password=Admin@123

# --- JPA / Hibernate ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

# Show SQL in console
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE
```

> ⚠️ **Note**: Update the database username and password if your local PostgreSQL setup is different.

---

## ▶️ Run the Application

### Step 4: Start Spring Boot App

* Open the main application class in IntelliJ
* Run the application using:

    * **Run ▶ button**, or
    * `mvn spring-boot:run`

### Expected Result

* Application starts on **port 8080**
* Database tables are automatically created/updated
* SQL logs are printed in the console

---
