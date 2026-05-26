# E-Commerce System

A full-stack **REST API** e-commerce backend built with **Java 17**, **Spring Boot 3**, **Spring Security (JWT)**, **Spring Data JPA**, and **SQL** (H2 or MySQL).

## Features

- **Web frontend** — Shop, cart, checkout, orders, and admin panel in the browser
- User registration & login (JWT authentication)
- Browse products & categories (public)
- Shopping cart (add, update, remove items)
- Place orders with stock management
- Admin panel: manage products, categories, and order status

## Tech Stack

| Layer        | Technology              |
|-------------|-------------------------|
| Language    | Java 17                 |
| Framework   | Spring Boot 3.2         |
| Database    | H2 (dev) / MySQL (prod) |
| ORM         | Spring Data JPA         |
| Security    | Spring Security + JWT   |
| Build       | Maven                   |

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+

### Run the application

```bash
cd E_COMMERCE_SYSTEM
mvn spring-boot:run
```

Server starts at: **http://localhost:8080**

**Web UI (Frontend):** **http://localhost:8080/index.html**

H2 Console: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:ecommerce`
- Username: `sa`
- Password: *(empty)*

### Demo accounts (auto-seeded)

| Role     | Email              | Password     |
|----------|--------------------|--------------|
| Admin    | admin@shop.com     | admin123     |
| Customer | customer@shop.com  | customer123  |

## API Endpoints

### Auth (public)

| Method | Endpoint              | Description        |
|--------|-----------------------|--------------------|
| POST   | `/api/auth/register`  | Register customer  |
| POST   | `/api/auth/login`     | Login, get JWT     |

### Products & Categories (public GET)

| Method | Endpoint                    | Description              |
|--------|-----------------------------|--------------------------|
| GET    | `/api/products`             | List all products        |
| GET    | `/api/products?categoryId=1`| Filter by category       |
| GET    | `/api/products?search=java` | Search by name           |
| GET    | `/api/products/{id}`        | Get product details      |
| GET    | `/api/categories`           | List categories          |

### Cart & Orders (requires `Authorization: Bearer <token>`)

| Method | Endpoint                      | Description           |
|--------|-------------------------------|-----------------------|
| GET    | `/api/cart`                   | View cart             |
| POST   | `/api/cart/items`             | Add item to cart      |
| PUT    | `/api/cart/items/{id}?quantity=2` | Update quantity   |
| DELETE | `/api/cart/items/{id}`        | Remove cart item      |
| POST   | `/api/orders`                 | Place order           |
| GET    | `/api/orders`                 | My order history      |
| GET    | `/api/orders/{id}`            | Order details         |

### Admin (requires ADMIN role + JWT)

| Method | Endpoint                           | Description          |
|--------|------------------------------------|----------------------|
| POST   | `/api/admin/products`              | Create product       |
| PUT    | `/api/admin/products/{id}`         | Update product       |
| DELETE | `/api/admin/products/{id}`         | Delete product       |
| POST   | `/api/admin/categories`            | Create category      |
| PUT    | `/api/admin/categories/{id}`       | Update category      |
| DELETE | `/api/admin/categories/{id}`       | Delete category      |
| GET    | `/api/admin/orders`                  | All orders           |
| PATCH  | `/api/admin/orders/{id}/status?status=SHIPPED` | Update status |

## Frontend Pages

| Page | URL | Access |
|------|-----|--------|
| Shop | http://localhost:8080/index.html | Public |
| Login | http://localhost:8080/login.html | Public |
| Register | http://localhost:8080/register.html | Public |
| Cart | http://localhost:8080/cart.html | Logged in |
| Orders | http://localhost:8080/orders.html | Logged in |
| Admin | http://localhost:8080/admin.html | Admin only |

## Example: Login & Shop (API)

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"customer@shop.com\",\"password\":\"customer123\"}"

# 2. Add to cart (use token from login response)
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"productId\":1,\"quantity\":2}"

# 3. Place order
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"shippingAddress\":\"123 Main St, Chennai, India\"}"
```

## MySQL Setup

1. Create database: `CREATE DATABASE ecommerce_db;`
2. Edit `src/main/resources/application-mysql.properties` with your credentials
3. Run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

SQL schema reference: `src/main/resources/schema.sql`

## Project Structure

```
src/main/java/com/ecommerce/
├── config/          # Security, data seeding
├── controller/      # REST endpoints
├── dto/             # Request/response objects
├── entity/          # JPA entities
├── exception/       # Error handling
├── repository/      # Data access
├── security/        # JWT & auth
└── service/         # Business logic
```

## License

MIT — free to use for learning and projects.
