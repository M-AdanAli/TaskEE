<p align="center">
  <img src="src/main/webapp/assets/images/logo.png" alt="TaskEE Logo" width="140" />
</p>

<h1 align="center">TaskEE</h1>

<p align="center">
  Java 25 · Jakarta EE 11 · MySQL · HikariCP · JUnit 5 · BCrypt
</p>

<p align="center">
  <strong>A servlet-first task manager built to expose backend architecture, security boundaries, and operational behavior clearly.</strong>
</p>

TaskEE is packaged as a WAR, targets Java 25, runs on Tomcat 11 through Cargo, uses MySQL via HikariCP, renders server-side JSP views, and ships with JUnit 5 tests.

## The Philosophy (Why This Exists)

TaskEE is not trying to impress by hiding complexity. It is trying to make the fundamentals inspectable.

This project is intentionally not built with Spring Boot, JPA/Hibernate, an ORM-driven repository layer, or a JavaScript SPA frontend. Instead, it uses a Front Controller servlet, explicit controllers, explicit JDBC DAOs, JSP/JSTL views, session-based authentication, and hand-written SQL. That is the point: you can trace a request from the browser, through filters and routing, into the service layer, down to a concrete SQL query, and back to a rendered view without guessing what a framework did for you.

## System Overview

TaskEE is a server-rendered task management application with user registration, login, dashboard metrics, task CRUD, status tracking, profile management, and a separate admin entry point. Regular users get a personal dashboard and paginated task views. The admin path exposes runtime and platform-level observability such as active sessions, uptime, JVM memory usage, and global completion rate.

Package layout, reconstructed from the codebase:

```text
src/main/java/com/adanali/taskee/
├── config
│   └── DBConnectionManager.java
├── controller
│   ├── AdminController.java
│   ├── Controller.java
│   ├── DashboardController.java
│   ├── LoginController.java
│   ├── LogoutController.java
│   ├── ProfileController.java
│   ├── RegistrationController.java
│   ├── TaskController.java
│   └── TaskListController.java
├── dao
│   ├── query
│   │   ├── TaskQuery.java
│   │   └── UserQuery.java
│   ├── TaskDAO.java
│   ├── TaskDaoJDBCImpl.java
│   ├── UserDAO.java
│   └── UserDaoJDBCImpl.java
├── domain
│   ├── enums
│   │   └── TaskStatus.java
│   ├── Task.java
│   └── User.java
├── dto
│   ├── Page.java
│   └── SessionUser.java
├── exception
│   ├── AuthenticationException.java
│   ├── AuthorizationException.java
│   ├── ServiceException.java
│   ├── TaskNotFoundException.java
│   ├── UserAlreadyExistsException.java
│   └── UserNotFoundException.java
├── filter
│   ├── AuthenticationFilter.java
│   └── LoggingFilter.java
├── listener
│   └── AppEventListener.java
├── service
│   ├── TaskService.java
│   ├── TaskServiceImpl.java
│   ├── UserService.java
│   └── UserServiceImpl.java
├── servlet
│   └── DispatcherServlet.java
└── util
    └── ValidationUtil.java
```

This structure is one of the strongest parts of the project because it makes the separation of concerns obvious before anyone reads business logic.

## Key Features

### 4.1 — User Experience

- Custom design system in `main.css` with a consistent palette, typography, card system, badges, stat cards, task grid, and pagination styling.
- Dashboard summary for each user with counts by status and a recent-tasks slice.
- Paginated task listing with status filters, 6-item page size, and stable page-window navigation.
- Relative timestamps through a reusable JSP tag (`timeAgo.tag`).
- Source-aware routing for task forms: the `source` parameter controls where save/cancel/delete returns the user, so task edits can bounce back to dashboard or tasks cleanly.
- PRG-style redirects are used on login, registration, and task create/update/delete flows. Profile edits are the exception: they re-render the same page with inline feedback instead of redirecting.

### 4.2 — Security Model

- BCrypt password hashing via jBCrypt. The project depends on `org.mindrot:jbcrypt`, and the service layer hashes passwords on registration and password change, then verifies with `BCrypt.checkpw()` on login. One detail matters: the cost is not explicitly pinned in code, so I would describe this as BCrypt-based hashing, not as a hardcoded configured factor.
- IDOR prevention. Task operations do not trust the incoming ID alone. The service layer fetches the task record and compares ownership before update, status change, delete, or fetch. A user with another task’s ID still fails the ownership check.
- XSS prevention. User-controlled values are rendered with `<c:out>` in the JSP layer, including task title, task description, echoed form values, and session-backed profile fields.
- Session fixation prevention. Login, admin login, and registration all invalidate any old session before creating a new one; logout invalidates the session outright.
- Unauthorized access logging. The authentication filter logs both the attempted path and the remote IP when an unauthenticated request hits a protected path.

### 4.3 — Admin Console & Observability

- Separate `/admin` entry point with a reserved admin identity (`admin@taskee.com`) and a dedicated login screen.
- Admin dashboard exposes live sessions, app uptime, JVM memory usage, total tasks, completed tasks, and global completion rate.
- Active session tracking is maintained by `AppEventListener` via `AtomicInteger`.
- Request timing is captured by `LoggingFilter`, and the app also has rolling file logging via Logback.

## Quick Start

### 7.1 — Prerequisites

- Java 25
- Maven
- MySQL
- Internet access on first run so Cargo can pull Tomcat 11 if it is not already available locally

### 7.2 — Setup

```bash
# 1) Clone the repo

# 2) Copy DB config
cp src/main/resources/db.properties.example src/main/resources/db.properties

# 3) Edit db.properties with your credentials and timezone

# 4) Run the schema
mysql -u root -p < src/main/resources/META-INF/sql/schema.sql

# 5) Launch
chmod +x run.sh   # Mac/Linux only, first time
./run.sh          # Mac/Linux
run.bat           # Windows
```

The helper scripts run `mvn clean install`, open `http://localhost:8080/TaskEE`, and then start Tomcat through Cargo.

### 7.3 — Default Accounts

**Admin**  
Email: `admin@taskee.com`  
Password: `Admin@123`

**Regular user**  
No default member account is seeded by the application logic itself; regular users are expected to register through `/register`.

## Architecture Deep Dive

### 5.1 — The Front Controller (`DispatcherServlet`)

`DispatcherServlet` is the routing core. It registers the application routes in `init()`, resolves the matching controller from a `Map<String, Controller>`, executes `handle()`, then either redirects or forwards to `/WEB-INF/views/<view>.jsp`. It also centralizes HTTP error mapping for not found, forbidden, and internal errors.

```text
HTTP Request
   |
   v
LoggingFilter
   |
   v
AuthenticationFilter
   |
   v
DispatcherServlet
   |
   +--> Controller.handle(...)
           |
           +--> Service layer
                   |
                   +--> DAO layer
                           |
                           +--> MySQL
           |
           +--> "redirect:/..."
           |        or
           +--> forward to /WEB-INF/views/*.jsp
```

### 5.2 — Exception Translation Strategy

The codebase uses a clean boundary between infrastructure errors and application-level behavior. JDBC methods throw `SQLException` inside DAO implementations; service methods translate that into `ServiceException`, and more specific domain exceptions such as `AuthorizationException`, `AuthenticationException`, `TaskNotFoundException`, `UserNotFoundException`, and `UserAlreadyExistsException`. `DispatcherServlet` then maps those to the correct HTTP response codes. That is a strong design decision because it keeps SQL exceptions from leaking into controller flow.

### 5.3 — Security Architecture

There are two distinct layers:

1. `AuthenticationFilter` decides whether the request may even reach protected routes by checking the session.
2. Service-layer ownership checks decide whether the authenticated user owns the specific record being acted on.

That matters because authentication alone is not enough. Even if someone reaches a task endpoint with a valid session, they still fail if the task belongs to another user. `/admin` is a good example of layered enforcement: it is public at the filter level because it must serve the admin login page, but the controller still blocks logged-in non-admin users from the actual dashboard.

### 5.4 — Data Layer

The data layer is interface-driven and explicit:

- `TaskDAO` / `UserDAO` define the contract.
- `TaskDaoJDBCImpl` / `UserDaoJDBCImpl` implement it with plain JDBC.
- SQL is centralized in `TaskQuery` and `UserQuery`.
- Connections come from HikariCP.
- Statements are parameterized with `PreparedStatement` across CRUD and count flows.

HikariCP is configured with a max pool size of 10, minimum idle of 2, idle timeout of 30 seconds, and connection timeout of 20 seconds.

### 5.5 — Observability

Observability is one of the most underrated strengths of this codebase:

- `LoggingFilter` measures request duration.
- `AppEventListener` tracks startup time and active sessions.
- `AdminController` turns that into operator-facing metrics.
- Logback writes both console logs and rolling file logs with 30-day retention and 1 GB total cap.

## API / Route Reference

These are the routes registered in `DispatcherServlet`, with GET/POST behavior inferred from each controller’s `handle()` logic.

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/login` | Public | Render login page |
| POST | `/login` | Public | Authenticate user and start a fresh session |
| GET | `/logout` | Public / session-aware | Invalidate current session and redirect to login |
| GET | `/register` | Public | Render registration page |
| POST | `/register` | Public | Create user, then auto-login with a fresh session |
| GET | `/dashboard` | Authenticated | User dashboard with task metrics and recent tasks |
| GET | `/tasks` | Authenticated | Paginated task list, optional status filter |
| GET | `/tasks/new` | Authenticated | New task form |
| POST | `/tasks/save` | Authenticated | Create task |
| GET | `/tasks/edit?id=...` | Authenticated | Edit task form |
| POST | `/tasks/update` | Authenticated | Update task details |
| POST | `/tasks/status` | Authenticated | Update task status |
| POST | `/tasks/delete` | Authenticated | Delete task |
| GET | `/profile` | Authenticated | Profile page |
| POST | `/profile/update` | Authenticated | Update profile name |
| POST | `/profile/password` | Authenticated | Change password |
| GET | `/admin` | Public for login page, Admin-only for dashboard | Render admin login or admin dashboard depending on session |
| POST | `/admin` | Public | Authenticate reserved admin user |

## Testing Strategy

### 8.1 — What’s tested

The test suite is better than average for a project at this size because it checks both infrastructure and behavior against a real database.

- `DBConnectionTest` checks that the Hikari-backed connection pool can return a live connection.
- `TaskDAOTest` checks task save, fetch-all-by-user, update, and count flows.
- `UserDAOTest` checks save, find-by-email, find-by-id, exists, find-all, update, and delete. The `findAll` assertion is explicitly index-agnostic rather than relying on row order.
- `TaskServiceTest` checks normal task creation/update plus ownership enforcement by verifying that a second user cannot update another user’s task.
- `UserServiceTest` checks registration, duplicate email rejection, login success, and wrong-password failure.

### 8.2 — Testing Philosophy

The tests use unique emails generated with `System.currentTimeMillis()`, clean up after themselves, and hit a real MySQL-backed stack through the real DAO/service implementations. That is the right call for this project because the value here is integration confidence, not fake-green unit tests around mocked persistence. The assertions are also mostly data-agnostic rather than depending on hardcoded row positions or fixed IDs.

## Design Assumptions & Tradeoffs

- This is a server-rendered monolith, not an API-first system. That keeps the request lifecycle easy to reason about, but it is not optimized for SPA/mobile clients.
- Authentication is session-based, which fits JSP rendering well, but there is no token-based auth layer yet.
- Admin authorization is currently modeled as a reserved email, not true RBAC. That is workable for a focused project, but it is the biggest architectural simplification in the codebase.
- Persistence uses plain JDBC and explicit SQL. That is excellent for clarity and control, but more verbose than JPA/Hibernate.
- Tasks and users are hard-deleted. There is no soft-delete or audit trail yet.
- Profile update/password flows return the same page with inline messages instead of using PRG. That is acceptable here, but it is inconsistent with the redirect-based flow elsewhere.

## What’s Next

- **Real RBAC** — replace reserved-email admin logic with DB-backed roles and service-layer authorization per role.
- **Task assignment** — allow admin-to-user assignment for collaborative workflows.
- **Soft deletes** — add `deleted_at` and hide deleted records by default.
- **JWT or token auth option** — useful if the project ever grows beyond JSP-driven browser flows.
- **Rate limiting on auth endpoints** — login and admin login are the obvious candidates.

## Closing Read on the Project

The strongest thing about TaskEE is not that it is a task manager. It is that it is a cleanly decomposed backend learning artifact that still behaves like a real application. You have explicit routing, explicit SQL, real connection pooling, actual session handling, ownership enforcement, observability hooks, and database-backed tests. That combination makes it much more credible than a CRUD demo that hides everything behind Spring annotations and generated abstractions.
