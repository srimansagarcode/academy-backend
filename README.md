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

Jackson → Entity binding issue,
---

## 🔍 What the Error Is REALLY Saying

### SQL log (key clue)

```sql
insert into students (age, email, name) values (?, ?, ?)
```

### PostgreSQL error

```
Failing row contains (4, null, null, null)
```

👉 Hibernate **received a Student object where all fields were `null`**
👉 Yet your JSON clearly has values

This means:

> ❌ The HTTP request body is NOT being converted into the `Student` object

---

## 🧠 Why This Happened (Root Cause)

### Your Controller

```java
@PostMapping
public ResponseEntity<Student> create(@RequestBody @Valid Student student) {
    return ResponseEntity.ok(studentService.create(student));
}
```

### Your JSON

```json
{
  "name": "John",
  "email": "john@gmail.com",
  "age": 22
}
```

### The Missing Piece ❌

👉 **Jackson cannot set values if your entity has no setters**

Jackson:

* creates the object using the no-args constructor
* tries to call setters
* finds none (or incorrect ones)
* leaves fields as `null`

---

## ✅ Verify This (Very Important Learning Step)

Open your `Student` entity and check **carefully**:

### ❌ Problematic Student Entity (Typical mistake)

```java
@Entity
public class Student {

    private Long id;
    private String name;
    private String email;
    private Integer age;

    // getters only ❌
}
```

or:

* setters are missing
* setters are private
* field names don’t match JSON

---

## ✅ Correct Student Entity (REQUIRED)

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer age;

    public Student() {} // REQUIRED by JPA & Jackson

    // 🔑 REQUIRED SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // getters
}
```

---

## 🧪 Why Validation Did NOT Catch This

You used:

```java
@Valid
```

But:

* Validation runs **after deserialization**
* Deserialization already produced `null` values
* Validation only checks constraints, not mapping success

So:

* Jackson failed silently
* Hibernate hit DB
* DB enforced NOT NULL

This is **expected behavior**.

---

## 🔥 Very Important Lesson (Day-1 Gold)

### ❗ Never use JPA entities directly as request bodies in real projects

You did it **intentionally here for learning**, which is good.

In production:

```java
POST → Request DTO → Entity
```

Why?

* Prevents silent binding failures
* Avoids exposing entity internals
* Cleaner validation

We will refactor to DTOs **after Day 2**, once JPA internals are solid.

---

## 🧠 Why This Bug Is GOOD for Learning

You just learned:

* Jackson deserialization rules
* Entity vs DTO responsibility
* Why SQL logs matter
* Why DB constraints are last line of defense

This is **exactly** the kind of bug senior engineers debug daily.

---

## ✅ Action Items (Do This Now)

1. Add **setters** to `Student`
2. Restart application
3. Re-send POST request
4. Observe SQL again

You should now see:

* non-null parameters
* successful INSERT
* no DB error

---

### Day 2 Preview

* Read APIs
* JPQL `SELECT`
* Pagination (manual JPQL)
* Hibernate flush timing
* Why `open-in-view=false` matters
--------

Great 👍
That was a **textbook Day-1 learning bug**, and you handled it correctly.
Now we move forward **cleanly and deliberately**.

---

# 🚀 Project Day 2

## Student Read APIs + JPQL + Pagination (No Derived Queries)

> 🎯 **Day 2 Goal**
> You will:

* Read data using **JPQL**
* Implement **pagination properly**
* Observe **Hibernate SELECT behavior**
* Understand **flush timing & transactions**
* Strengthen your mental model of JPA reads

---

## 🔒 Rules Still Apply

* ❌ No derived queries
* ✅ JPQL only
* ✅ SQL logging ON
* ✅ `open-in-view=false`

---

## 🧱 Step 1: StudentRepository — JPQL Reads

### Add these methods (NO derived queries)

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
        SELECT s FROM Student s
        ORDER BY s.id
    """)
    List<Student> findAllStudents();

    @Query("""
        SELECT s FROM Student s
        WHERE s.age >= :minAge
        ORDER BY s.name
    """)
    List<Student> findStudentsWithMinAge(@Param("minAge") Integer minAge);
}
```

📌 Why this matters:

* You are **explicitly choosing JPQL**
* You are controlling sorting
* No magic method parsing

---

## 🧱 Step 2: Pagination with JPQL (IMPORTANT)

### Repository

```java
@Query("""
    SELECT s FROM Student s
    ORDER BY s.id
""")
Page<Student> findAllStudents(Pageable pageable);
```

📌 Spring Data:

* applies pagination **after JPQL**
* generates `LIMIT / OFFSET`
* still JPQL → SQL

---

## 🧱 Step 3: Service Layer (Read-Only Transactions)

```java
@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return repository.findAllStudents();
    }

    @Transactional(readOnly = true)
    public Page<Student> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllStudents(pageable);
    }
}
```

📌 Why `readOnly = true`?

* No dirty checking
* Better performance
* Clear intent

---

## 🧱 Step 4: Controller — Read APIs

```java
@GetMapping
public ResponseEntity<List<Student>> getAll() {
    return ResponseEntity.ok(studentService.getAll());
}

@GetMapping("/paged")
public ResponseEntity<Page<Student>> getPaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
) {
    return ResponseEntity.ok(studentService.getAllPaged(page, size));
}
```

---

## 🧪 Step 5: Test & Observe SQL (VERY IMPORTANT)

### Call

```
GET /api/v1/students
```

Watch SQL:

```sql
select s.id, s.age, s.email, s.name from students s order by s.id
```

---

### Call

```
GET /api/v1/students/paged?page=0&size=2
```

Watch SQL:

```sql
select s.id, s.age, s.email, s.name
from students s
order by s.id
limit ? offset ?
```

Hibernate is now:

* translating JPQL
* applying pagination
* generating DB-specific SQL

---

## 🧠 CRITICAL LEARNING (Read Carefully)

### 1️⃣ Pagination is NOT JPQL

JPQL has no `LIMIT`.

Spring Data:

* modifies SQL **after translation**
* DB dialect decides syntax

---

### 2️⃣ Why `ORDER BY` is mandatory

Pagination without ordering is **undefined behavior**.

Always sort when paginating.

---

### 3️⃣ Why no extra SELECT?

Because:

* no relationships yet
* no lazy loading
* clean transaction scope

---

## 🧪 Optional Experiment (Do This)

Inside service:

```java
@Transactional
public void testFlush() {
    Student s = repository.findById(1L).get();
    s.setName("Updated Name");
}
```

Watch logs:

* UPDATE happens **at transaction commit**
* Not immediately

This is **dirty checking** in action.

---

## 🧠 Questions You MUST Answer (Write Them Down)

1️⃣ Why does Hibernate not run UPDATE immediately?
2️⃣ Why does pagination require ORDER BY?
3️⃣ Why is JPQL portable but SQL is not?
4️⃣ What happens if `readOnly = true` and you modify entity?

---

---

# 🚀 Project Day 3

## Dynamic Search API using Specifications (The Right Way)

> 🎯 **Day 3 Goal**
> You will:

* Build a real **search API**
* Understand **why derived queries are insufficient**
* Learn **Specification composition**
* Control **filter + search + pagination + sorting**
* See exactly how Hibernate builds SQL

This directly connects to the pain you experienced earlier in your projects.

---

## 🔴 Problem Statement (Real-World)

Frontend sends a payload like:

```json
{
  "page": 0,
  "size": 5,
  "search": "john",
  "filters": {
    "age": 22
  }
}
```

Requirements:

* Search by **name OR email**
* Filters are optional
* Pagination mandatory
* Sorting later
* Clean, scalable design

👉 **Derived queries CANNOT handle this**.

---

## 🧠 Why Derived Queries Fail Here

To support this dynamically, you would need methods like:

```java
findByNameContainingAndAge(...)
findByEmailContainingAndAge(...)
findByNameContaining(...)
findByEmailContaining(...)
```

❌ Explosion of methods
❌ Impossible to maintain
❌ Still static

👉 This is exactly why **Specifications exist**.

---

## 🧱 Step 1: Create Search Request DTO

```java
package academy.academy_backend.api.v1.dto.request;

import java.util.Map;

public class StudentSearchRequest {

    private int page;
    private int size;
    private String search;
    private Map<String, Object> filters;

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }

    public Map<String, Object> getFilters() { return filters; }
    public void setFilters(Map<String, Object> filters) { this.filters = filters; }
}
```

📌 This mirrors real frontend payloads.

---

## 🧱 Step 2: Create StudentSpecification

```java
package academy.academy_backend.api.v1.specification;

import academy.academy_backend.domain.student.Student;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.Map;

public class StudentSpecification {

    public static Specification<Student> withSearchAndFilters(
            String search,
            Map<String, Object> filters
    ) {
        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction(); // TRUE

            // 🔍 Search (name OR email)
            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                Predicate searchPredicate = cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern)
                );
                predicate = cb.and(predicate, searchPredicate);
            }

            // 🎯 Filters (equals-based)
            if (filters != null) {
                for (Map.Entry<String, Object> entry : filters.entrySet()) {
                    predicate = cb.and(
                            predicate,
                            cb.equal(root.get(entry.getKey()), entry.getValue())
                    );
                }
            }

            return predicate;
        };
    }
}
```

📌 Key concept:

* `cb.conjunction()` = neutral TRUE predicate
* Conditions added only if present
* Fully dynamic

---

## 🧱 Step 3: Update Repository (Enable Specifications)

```java
public interface StudentRepository
        extends JpaRepository<Student, Long>,
                JpaSpecificationExecutor<Student> {
}
```

📌 No methods needed — Specifications drive everything.

---

## 🧱 Step 4: Service Layer (Search Logic)

```java
@Transactional(readOnly = true)
public Page<Student> search(StudentSearchRequest request) {

    Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize()
    );

    Specification<Student> spec =
            StudentSpecification.withSearchAndFilters(
                    request.getSearch(),
                    request.getFilters()
            );

    return studentRepository.findAll(spec, pageable);
}
```

📌 Clean separation:

* Controller → request
* Service → logic
* Specification → query

---

## 🧱 Step 5: Controller Endpoint

```java
@PostMapping("/search")
public ResponseEntity<Page<Student>> search(
        @RequestBody StudentSearchRequest request
) {
    return ResponseEntity.ok(studentService.search(request));
}
```

---

## 🧪 Step 6: Test with Postman

### POST `/api/v1/students/search`

```json
{
  "page": 0,
  "size": 5,
  "search": "john",
  "filters": {
    "age": 22
  }
}
```

### Observe SQL

You should see:

```sql
select ...
from students s
where (
    lower(s.name) like ?
    or lower(s.email) like ?
)
and s.age = ?
limit ? offset ?
```

🔥 This is **exactly** what we wanted.

---

## 🧠 Critical Learnings (Lock These In)

### 1️⃣ Specification ≠ Query

It’s a **predicate builder**.

### 2️⃣ `cb.conjunction()` is mandatory

Never return `null`.

### 3️⃣ This design scales

Add new filters without touching repository.

---

## 🔥 Why This Is Senior-Level Design

* No query explosion
* Fully dynamic
* Easy to extend
* Matches frontend needs
* SQL is predictable

This is **why your earlier project search was the right instinct** — now it’s clean and intentional.

---

## 🛠 Your Tasks (Very Important)

Answer these in your own words:

1️⃣ Why are derived queries unsuitable for search APIs?
2️⃣ Why is `cb.conjunction()` used instead of `null`?
3️⃣ Where does pagination apply — JPQL or SQL?

---


# 🚀 Project Day 4

## Sorting + Advanced Filters (Production-Grade Search API)

> 🎯 **Day 4 Goal**
> You will:

* Add **multi-column sorting**
* Prevent **invalid column attacks**
* Safely handle **dynamic filters**
* Understand **why naive implementations break in prod**

This directly fixes real-world issues you faced earlier.

---

## 🔴 Real-World Problem

Frontend sends:

```json
{
  "page": 0,
  "size": 5,
  "search": "john",
  "sorting": [
    { "field": "name", "direction": "asc" },
    { "field": "age", "direction": "desc" }
  ],
  "filters": {
    "age": 22
  }
}
```

Problems to solve:

* How do we sort dynamically?
* How do we prevent invalid fields?
* How do we avoid SQL injection–style issues?

---

## 🧱 Step 1: Extend Search Request DTO

```java
package academy.academy_backend.api.v1.dto.request;

import java.util.List;
import java.util.Map;

public class StudentSearchRequest {

    private int page;
    private int size;
    private String search;
    private Map<String, Object> filters;
    private List<SortField> sorting;

    public static class SortField {
        private String field;
        private String direction;

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
    }

    // getters & setters omitted for brevity
}
```

---

## 🧠 Key Security Rule (Memorize This)

> **Never trust column names from the client.**

Even if it’s not SQL injection, it can:

* crash queries
* expose internals
* break production

---

## 🧱 Step 2: Allowed Fields Whitelist

```java
package academy.academy_backend.api.v1.specification;

import java.util.Set;

public class StudentSearchFields {

    public static final Set<String> SORTABLE_FIELDS = Set.of(
            "id",
            "name",
            "email",
            "age"
    );

    public static final Set<String> FILTERABLE_FIELDS = Set.of(
            "age"
    );
}
```

📌 This is **non-negotiable** in production.

---

## 🧱 Step 3: Safe Sorting Builder

```java
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortBuilder {

    public static Sort build(List<StudentSearchRequest.SortField> sorting) {

        if (sorting == null || sorting.isEmpty()) {
            return Sort.by("id").ascending();
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (var sortField : sorting) {

            if (!StudentSearchFields.SORTABLE_FIELDS.contains(sortField.getField())) {
                continue; // silently ignore invalid fields
            }

            Sort.Direction direction =
                    "desc".equalsIgnoreCase(sortField.getDirection())
                            ? Sort.Direction.DESC
                            : Sort.Direction.ASC;

            orders.add(new Sort.Order(direction, sortField.getField()));
        }

        return orders.isEmpty()
                ? Sort.by("id").ascending()
                : Sort.by(orders);
    }
}
```

---

## 🧱 Step 4: Safe Filter Validation

Update `StudentSpecification`:

```java
if (filters != null) {
    for (var entry : filters.entrySet()) {

        if (!StudentSearchFields.FILTERABLE_FIELDS.contains(entry.getKey())) {
            continue; // ignore invalid filters
        }

        predicate = cb.and(
                predicate,
                cb.equal(root.get(entry.getKey()), entry.getValue())
        );
    }
}
```

📌 This prevents:

* invalid field access
* runtime exceptions
* accidental data exposure

---

## 🧱 Step 5: Update Service (Sorting + Paging)

```java
@Transactional(readOnly = true)
public Page<Student> search(StudentSearchRequest request) {

    Sort sort = SortBuilder.build(request.getSorting());

    Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            sort
    );

    Specification<Student> spec =
            StudentSpecification.withSearchAndFilters(
                    request.getSearch(),
                    request.getFilters()
            );

    return studentRepository.findAll(spec, pageable);
}
```

---

## 🧪 Step 6: Test & Observe SQL

### Request

```json
{
  "page": 0,
  "size": 5,
  "search": "john",
  "sorting": [
    { "field": "name", "direction": "asc" },
    { "field": "age", "direction": "desc" }
  ],
  "filters": {
    "age": 22,
    "password": "hack"   // ignored safely
  }
}
```

### SQL

```sql
order by s.name asc, s.age desc
```

🔥 Clean, predictable, safe.

---

## 🧠 Critical Learnings (Lock These In)

### 1️⃣ Sorting belongs in Pageable, not Specification

Specification = WHERE
Pageable = ORDER BY + LIMIT/OFFSET

---

### 2️⃣ Whitelisting fields is mandatory

Never dynamically trust client-provided fields.

---

### 3️⃣ Ignoring invalid input is safer than failing hard

Failing hard leaks system behavior.

---

## 🔥 Why This Is Enterprise-Level Design

* Safe dynamic APIs
* No query explosion
* Frontend-friendly
* Backward compatible
* Easy to extend

This is **exactly how real search APIs are written**.

---

## 🛠 Your Reflection (Short Answers)

1️⃣ Why is sorting NOT part of Specification?
2️⃣ Why do we whitelist fields instead of throwing errors?
3️⃣ What happens if you allow arbitrary field names?

---

# 🚀 Project Day 5

## DTO Mapping + Clean API Contracts (Production Rule)

> 🎯 **Day 5 Goal**
> You will:

* Stop exposing JPA entities
* Introduce **Request DTOs & Response DTOs**
* Control API shape explicitly
* Understand **why entities must never leak**
* Prepare the codebase for relationships (Day 6+)

---

## 🔴 The Core Rule (Memorize This)

> **JPA entities are NOT API models.**

Entities:

* are persistence models
* change frequently
* contain lazy proxies
* are tied to DB concerns

DTOs:

* are API contracts
* stable
* explicit
* frontend-friendly

---

## 🧠 What We’ll Change Today

### ❌ Before (Learning Phase)

```java
ResponseEntity<Student>
```

### ✅ After (Production)

```java
ResponseEntity<StudentResponseDTO>
```

---

## 🧱 Step 1: Define Request & Response DTOs

### 📥 StudentCreateRequest (POST)

```java
package academy.academy_backend.api.v1.dto.request;

import jakarta.validation.constraints.*;

public class StudentCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Min(18)
    private Integer age;

    // getters & setters
}
```

---

### 📤 StudentResponseDTO (API Output)

```java
package academy.academy_backend.api.v1.dto.response;

public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Integer age;

    // getters & setters
}
```

---

## 🧱 Step 2: Mapper (Manual on Purpose)

> ❌ No MapStruct yet
> ✅ Manual mapping to understand flow

```java
package academy.academy_backend.api.v1.mapper;

import academy.academy_backend.domain.student.Student;
import academy.academy_backend.api.v1.dto.request.StudentCreateRequest;
import academy.academy_backend.api.v1.dto.response.StudentResponseDTO;

public class StudentMapper {

    public static Student toEntity(StudentCreateRequest request) {
        Student s = new Student();
        s.setName(request.getName());
        s.setEmail(request.getEmail());
        s.setAge(request.getAge());
        return s;
    }

    public static StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        return dto;
    }
}
```

📌 Manual mapping builds **clarity**, not boilerplate fear.

---

## 🧱 Step 3: Update Service Layer

```java
@Transactional
public StudentResponseDTO create(StudentCreateRequest request) {

    Student student = StudentMapper.toEntity(request);
    Student saved = studentRepository.save(student);

    return StudentMapper.toDTO(saved);
}
```

---

```java
@Transactional(readOnly = true)
public StudentResponseDTO getById(Long id) {

    Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    return StudentMapper.toDTO(student);
}
```

---

## 🧱 Step 4: Update Controller

```java
@PostMapping
public ResponseEntity<StudentResponseDTO> create(
        @RequestBody @Valid StudentCreateRequest request
) {
    return ResponseEntity.ok(studentService.create(request));
}
```

---

```java
@GetMapping("/{id}")
public ResponseEntity<StudentResponseDTO> get(@PathVariable Long id) {
    return ResponseEntity.ok(studentService.getById(id));
}
```

---

## 🧱 Step 5: Update Search Response

```java
@PostMapping("/search")
public ResponseEntity<Page<StudentResponseDTO>> search(
        @RequestBody StudentSearchRequest request
) {
    return ResponseEntity.ok(studentService.search(request));
}
```

Service:

```java
@Transactional(readOnly = true)
public Page<StudentResponseDTO> search(StudentSearchRequest request) {

    Page<Student> page = studentRepository.findAll(spec, pageable);

    return page.map(StudentMapper::toDTO);
}
```

📌 `Page.map()` is **clean and efficient**.

---

## 🧠 Why This Matters (Deep Reason)

### ❌ Exposing entities causes:

* LazyInitializationException
* Accidental data exposure
* Infinite JSON recursion
* Tight coupling with DB

### ✅ DTOs give:

* Stable API contracts
* Versioning control
* Security
* Clean evolution

---

## 🔥 Interview-Ready Answers

**Q:** Why not return entities directly?
**A:** Entities represent persistence state, not API contracts.

**Q:** Where should mapping happen?
**A:** Between service and controller.

**Q:** Why manual mapping first?
**A:** To understand data flow before automation.

---

## 🧠 Phase Checkpoint

You have now completed:

✅ JPQL CRUD
✅ Pagination
✅ Specifications
✅ Sorting & filtering
✅ Safe API design
✅ DTO mapping

This is **solid backend foundation**.

---
Excellent question 👍
This shows you’re now **thinking like an API designer**, not just following code.

Let me answer clearly and honestly.

---

## ✅ Short Answer

You are **absolutely right** to question this.

> Using `Course` entity directly in the controller is **NOT the ideal design**.

I did it **intentionally for Day 6 only**, to teach **relationship mechanics first**, not API design.

From Day 7 onward → we **must switch to `CourseCreateRequest`**
(and you were correct to notice the inconsistency 👏)

---

## 🎯 Why I Temporarily Used `Course` Directly

### Day 6 goal was **NOT DTO design**

It was to teach:

✅ Owning side
✅ Foreign key behavior
✅ How `student_id` is set
✅ How JPA persists relationships
✅ Cascade behavior
✅ LAZY loading

If we added DTO + mapper + validation **at the same time**, you would not clearly see:

* where the FK comes from
* when it is assigned
* how Hibernate flushes it
* how relationships are persisted

So this was **a teaching simplification**, not a best practice.

---

## ❗ Important Rule (Now You’re Ready For It)

> **Entities should NEVER be used in controllers in real projects.**

You already followed this rule for:

* Student ✔
* Search ✔
* DTO mapping ✔

So your instinct is 100% correct.

---

## ✅ Proper Design (What We’ll Do Next)

### ✔ Correct Version (What You Expected)

### 1️⃣ CourseCreateRequest

```java
public class CourseCreateRequest {
    private String title;
    private Integer credits;

    // getters & setters
}
```

---

### 2️⃣ Controller (Correct Way)

```java
@PostMapping
public ResponseEntity<CourseResponseDTO> createCourse(
        @PathVariable Long studentId,
        @RequestBody CourseCreateRequest request
) {
    return ResponseEntity.ok(
            courseService.createCourse(studentId, request)
    );
}
```

---

### 3️⃣ Service (Correct Way)

```java
@Transactional
public CourseResponseDTO createCourse(
        Long studentId,
        CourseCreateRequest request
) {
    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    Course course = new Course();
    course.setTitle(request.getTitle());
    course.setCredits(request.getCredits());
    course.setStudent(student);

    Course saved = courseRepository.save(course);
    return CourseMapper.toDTO(saved);
}
```

---

### 4️⃣ Why This Is Better

| Concern      | Old Way      | Correct Way |
| ------------ | ------------ | ----------- |
| API contract | Leaks entity | Clean DTO   |
| Validation   | Weak         | Strong      |
| Coupling     | High         | Low         |
| Security     | Risky        | Safe        |
| Refactoring  | Hard         | Easy        |

---

## 🧠 Why I Did NOT Do This Earlier (Important Insight)

If I had introduced:

* DTO
* Mapper
* Relationship
* FK logic
* Cascade rules

…all at once, it would become **mechanical copying**, not learning.

So Day 6 was:

> “Understand relationships first, clean architecture later”

Now you’re **ready for clean architecture**.

---

## ✅ Final Answer to Your Question

> ❓ Why did you use `Course` entity directly?

✔ To isolate **JPA relationship behavior**
✔ To reduce moving parts while learning
✔ NOT because it’s best practice

> ❓ Should we use CourseCreateRequest instead?

✅ YES — and we will from **Day 7**

---

# 📘 **Spring Boot JPA Learning – Day 7**

## **Cascade Types & Orphan Removal**

This topic is **very important** for real-world backend development and interviews.

---

## 🎯 **Day 7 Objectives**

By the end of this day, you will:

* Understand **Cascade types**
* Learn **orphanRemoval**
* Know **when and when NOT to use cascade**
* Implement examples using **Student – Course**
* Avoid common mistakes

---

## 1️⃣ What is Cascade in JPA?

**Cascade** means:

> When you perform an operation on a parent entity, the same operation is automatically applied to its related child entities.

Example:

* Save Student → Courses also saved
* Delete Student → Courses also deleted

---

## 2️⃣ Cascade Types (Important for Interviews)

```java
CascadeType.PERSIST
CascadeType.MERGE
CascadeType.REMOVE
CascadeType.REFRESH
CascadeType.DETACH
CascadeType.ALL
```

### 🔹 Commonly Used:

| Cascade Type | Meaning                              |
| ------------ | ------------------------------------ |
| `PERSIST`    | Saves child when parent is saved     |
| `MERGE`      | Updates child when parent is updated |
| `REMOVE`     | Deletes child when parent is deleted |
| `ALL`        | Applies all above                    |

---

## 3️⃣ Example: Student – Course (OneToMany)

### Student Entity

```java
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "student",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Course> courses = new ArrayList<>();
}
```

### Course Entity

```java
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
```

---

## 4️⃣ What is `orphanRemoval = true`?

👉 It removes **child records** that are no longer linked to the parent.

### Example:

```java
student.getCourses().remove(0);
studentRepository.save(student);
```

✅ Result:

* That course row is **deleted from DB**

❌ Without `orphanRemoval = true`:

* Course remains in DB (orphan record)

---

## 5️⃣ Cascade vs Orphan Removal (Very Important)

| Feature            | Cascade          | Orphan Removal   |
| ------------------ | ---------------- | ---------------- |
| Works on           | Parent operation | Child removal    |
| Example            | save(), delete() | remove from list |
| Deletes child      | Yes (REMOVE)     | Yes              |
| Interview favorite | ✅                | ✅                |

---

## 6️⃣ Real-World Example

### Saving Student with Courses

```java
Student s = new Student();
s.setName("Vidya");

Course c1 = new Course();
c1.setCourseName("Java");

Course c2 = new Course();
c2.setCourseName("Spring Boot");

c1.setStudent(s);
c2.setStudent(s);

s.getCourses().add(c1);
s.getCourses().add(c2);

studentRepository.save(s);
```

✅ Saves:

* 1 Student
* 2 Courses automatically

---

### Removing a Course

```java
student.getCourses().remove(0);
studentRepository.save(student);
```

✅ Course deleted from DB (because of `orphanRemoval = true`)

---

## 7️⃣ ⚠️ Common Mistakes (Interview Question)

### ❌ Mistake 1:

```java
cascade = CascadeType.ALL
```

Used blindly → causes **unexpected deletes**

### ❌ Mistake 2:

Using `REMOVE` in `@ManyToOne`
➡️ Dangerous (can delete parent)

### ❌ Mistake 3:

Not setting both sides of relation

```java
course.setStudent(student); // mandatory
```

---

## 8️⃣ Best Practices ✅

✔ Use `CascadeType.PERSIST, MERGE` instead of ALL
✔ Use `orphanRemoval = true` only for strong ownership
✔ Avoid cascade REMOVE in `@ManyToOne`
✔ Always maintain both sides of relation

---

## 9️⃣ Interview Questions (Very Important)

❓ Difference between Cascade and Orphan Removal
❓ When to use CascadeType.ALL?
❓ What happens if orphanRemoval = false?
❓ Can orphanRemoval delete DB records?
❓ Difference between delete and orphanRemoval

---
Perfect 👍
This is one of the **most important JPA topics** for real-world projects and interviews.

---

# 📘 **Spring Boot JPA – Day 8**

## **Fetch Types (LAZY vs EAGER)**

---

## 🎯 Day 8 Objectives

By the end of this session, you will:

* Understand **FetchType.LAZY vs FetchType.EAGER**
* Know **default fetch types**
* Understand **N+1 query problem**
* Learn **best practices**
* Be ready for **interview questions**

---

## 1️⃣ What is Fetch Type?

Fetch type defines:

> **When related entities are loaded from the database**

---

## 2️⃣ Types of Fetching

```java
FetchType.LAZY
FetchType.EAGER
```

---

## 3️⃣ Default Fetch Types (Very Important)

| Relationship  | Default Fetch |
| ------------- | ------------- |
| `@OneToMany`  | LAZY          |
| `@ManyToMany` | LAZY          |
| `@ManyToOne`  | EAGER         |
| `@OneToOne`   | EAGER         |

⚠️ Many developers fail interviews here.

---

## 4️⃣ FetchType.EAGER

### Example

```java
@ManyToOne(fetch = FetchType.EAGER)
private Student student;
```

### Behavior

* Parent + Child loaded immediately
* Single query or join
* Can cause **performance issues**

### SQL Example

```sql
SELECT * FROM student;
SELECT * FROM course;
```

or sometimes:

```sql
SELECT * FROM student
JOIN course ON ...
```

---

## 5️⃣ FetchType.LAZY (Recommended)

### Example

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Course> courses;
```

### Behavior

* Data loaded **only when accessed**
* Uses proxy objects
* Better performance

---

## 6️⃣ ⚠️ LazyInitializationException (Very Important)

### Scenario:

```java
Student student = studentRepo.findById(1L).get();
student.getCourses().size(); // ❌ Error
```

### Error:

```
LazyInitializationException:
could not initialize proxy – no Session
```

### Why?

Because:

* Session closed
* Lazy data accessed outside transaction

---

## 7️⃣ How to Fix LazyInitializationException

### ✅ Option 1: Use @Transactional

```java
@Transactional
public Student getStudent(Long id) {
    return studentRepository.findById(id).get();
}
```

---

### ✅ Option 2: Use JOIN FETCH (Best)

```java
@Query("SELECT s FROM Student s JOIN FETCH s.courses WHERE s.id = :id")
Student findWithCourses(@Param("id") Long id);
```

---

### ✅ Option 3: DTO Projection (Recommended for APIs)

```java
SELECT new com.dto.StudentDTO(s.name, c.courseName)
FROM Student s JOIN s.courses c
```

---

## 8️⃣ N+1 Query Problem (🔥 Interview Favorite)

### Example:

```java
List<Student> students = studentRepo.findAll();

for(Student s : students) {
    s.getCourses().size();
}
```

### Queries Executed:

1. One query → fetch students
2. N queries → each student’s courses

💥 Performance issue!

---

## 9️⃣ Fix for N+1 Problem

### ✅ Solution 1: JOIN FETCH

```java
@Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.courses")
List<Student> findAllWithCourses();
```

---

### ✅ Solution 2: EntityGraph

```java
@EntityGraph(attributePaths = {"courses"})
List<Student> findAll();
```

---

## 🔟 Best Practices (Very Important)

✔ Always use **LAZY** by default
✔ Use **JOIN FETCH** only when needed
✔ Avoid EAGER in collections
✔ Use DTOs for APIs
✔ Never expose entities directly to UI
✔ Monitor SQL logs (`spring.jpa.show-sql=true`)

---

## 🧠 Interview Questions

1. Difference between LAZY and EAGER?
2. Default fetch types in JPA?
3. What is N+1 problem?
4. How to fix LazyInitializationException?
5. When to use JOIN FETCH?
6. Why EAGER is dangerous?

---

## ✅ Day 8 Completed 🎉
