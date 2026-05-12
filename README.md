# Fruitshop

Ứng dụng bán trái cây (Spring Boot + Thymeleaf + MySQL) gồm giao diện web và REST API cho mobile.

## Tổng quan
- Web UI: Thymeleaf + session
- Mobile API: REST + JWT
- Phân quyền: USER/ADMIN (Spring Security)

## Cấu trúc project
### Kiến trúc lớp
- `controller/`: web UI (render Thymeleaf)
- `api/`: REST (JSON)
- `service/`: business logic
- `repository/`: JPA data access
- `entity/`: mô hình dữ liệu
- `security/`: Spring Security + JWT
- `dto/`: dữ liệu truyền giữa client và server

### Thư mục quan trọng
- [src/main/java/com/thandinh/fruitshop](src/main/java/com/thandinh/fruitshop)
  - `api/`, `controller/`, `service/`, `repository/`, `entity/`, `security/`, `dto/`
- [src/main/resources](src/main/resources)
  - `application.yaml`, `templates/`, `static/`, `db/`
- [pom.xml](pom.xml)

## Tính năng
- Đăng ký, đăng nhập web (session)
- Đăng ký, đăng nhập API (JWT)
- Danh mục, sản phẩm, tìm kiếm và lọc
- Giỏ hàng, đặt hàng
- Đánh giá sản phẩm, wishlist
- Quản trị: sản phẩm, danh mục, đơn hàng, người dùng

## Xác thực & phân quyền
- Web UI: form login -> session
- API `/api/**`: JWT (Bearer token)
- Admin routes: ROLE_ADMIN

## Luồng hoạt động chính
- Web UI:
  - Form login -> Spring Security -> session
  - Trang home/products gọi API bằng fetch để lấy dữ liệu
- Mobile API:
  - Đăng nhập/đăng ký -> nhận JWT
  - Gọi `/api/**` kèm `Authorization: Bearer <token>`

## Web routes (UI)
### Public
- GET `/` -> redirect `/trang-chu`
- GET `/trang-chu`
- GET `/san-pham-list`
- GET `/san-pham/{slug}`
- GET `/san-pham?id={id}`
- GET `/gio-hang`
- POST `/gio-hang` (action: create, update, delete, clear)
- GET `/dang-ky`
- POST `/dang-ky`
- GET `/dang-nhap`
- POST `/dang-nhap` (handled by Spring Security)
- POST `/dang-xuat`

### Yêu cầu đăng nhập
- GET `/dat-hang`
- POST `/dat-hang`
- GET `/thong-tin-ca-nhan`
- POST `/thong-tin-ca-nhan/cap-nhat`
- POST `/thong-tin-ca-nhan/doi-mat-khau`
- POST `/thong-tin-ca-nhan/cap-nhat-avatar`

### Admin (ROLE_ADMIN)
- GET `/admin` hoặc `/admin/dashboard`
- GET `/admin/products`
- GET `/admin/products/create`
- POST `/admin/products/create`
- GET `/admin/products/edit/{id}`
- POST `/admin/products/edit/{id}`
- POST `/admin/products/delete/{id}`

- GET `/admin/categories`
- GET `/admin/categories/create`
- POST `/admin/categories/create`
- GET `/admin/categories/edit/{id}`
- POST `/admin/categories/update/{id}`
- POST `/admin/categories/delete/{id}`
- GET `/admin/categories/delete/{id}` (compat)

- GET `/admin/orders`
- GET `/admin/orders/edit/{id}`
- POST `/admin/orders/update/{id}`
- GET `/admin/orders/details/{id}`
- POST `/admin/orders/delete/{id}`

- GET `/admin/users`
- GET `/admin/users/create`
- POST `/admin/users/create`
- GET `/admin/users/edit/{id}`
- POST `/admin/users/edit/{id}`
- POST `/admin/users/delete/{id}`
- GET `/admin/users/profile`
- POST `/admin/users/profile/update`

## Web gọi API từ client
### User UI
- `/api/categories`, `/api/products`, `/api/products/category/{categoryId}`
  - Nơi gọi: [src/main/resources/templates/home.html](src/main/resources/templates/home.html)
- `/api/products/paged`, `/api/categories`
  - Nơi gọi: [src/main/resources/templates/products.html](src/main/resources/templates/products.html)

### Admin UI
- POST `/api/admin/products`
  - Nơi gọi: [src/main/resources/templates/admin/product/create.html](src/main/resources/templates/admin/product/create.html)
- PUT `/api/admin/products/{id}`
  - Nơi gọi: [src/main/resources/templates/admin/product/edit.html](src/main/resources/templates/admin/product/edit.html)
- DELETE `/api/admin/products/{id}`
  - Nơi gọi: [src/main/resources/templates/admin/product/index.html](src/main/resources/templates/admin/product/index.html)

### Lưu ý
- Có file JS demo gọi API ngoài (không phải backend):
  - [src/main/resources/static/assets/admin/js/plugins-init/select2-init.js](src/main/resources/static/assets/admin/js/plugins-init/select2-init.js) gọi `https://api.github.com/search/repositories`

## API cho mobile (JWT)
Base URL: `http://localhost:8080/api`

### Auth
- POST `/auth/register`
- POST `/auth/login`
- GET `/auth/check-email?email=...`

### Categories
- GET `/categories`

### Products
- GET `/products`
- GET `/products/paged?page=1&size=9&sort=az|za|price-asc|price-desc|newest|oldest&categoryId=&minPrice=&maxPrice=&keyword=`
- GET `/products/category/{categoryId}`
- GET `/products/{id}`

### Product reviews
- GET `/products/{productId}/reviews`
- POST `/products/{productId}/reviews`
- PUT `/products/{productId}/reviews/{reviewId}`
- DELETE `/products/{productId}/reviews/{reviewId}`

### Wishlist
- GET `/wishlist`
- POST `/wishlist/{productId}`
- DELETE `/wishlist/{productId}`
- GET `/wishlist/check/{productId}`

### Orders
- POST `/orders`
- GET `/orders`
- GET `/orders/{id}`

### Profile
- GET `/profile`
- PUT `/profile/update`
- POST `/profile/change-password`
- POST `/profile/upload-avatar`

## Admin API (ROLE_ADMIN)
Base URL: `http://localhost:8080/api/admin`

### Products
- GET `/products`
- GET `/products/{id}`
- POST `/products`
- PUT `/products/{id}`
- DELETE `/products/{id}`
- DELETE `/products/bulk`
- GET `/products/categories`
- GET `/products/statistics`

### Categories
- GET `/categories`
- GET `/categories/{id}`
- POST `/categories`
- PUT `/categories/{id}`
- DELETE `/categories/{id}`
- GET `/categories/count`
- GET `/categories/exists?name=...`

### Users
- GET `/users`
- GET `/users/{id}`
- POST `/users`
- PUT `/users/{id}`
- DELETE `/users/{id}`
- POST `/users/{id}/restore`
- GET `/users/profile/current`
- PUT `/users/profile/current`
- GET `/users/statistics/count`

### Orders
- GET `/orders`
- GET `/orders/{id}`
- GET `/orders/{id}/items`
- PUT `/orders/{id}/status`
- DELETE `/orders/{id}`
- GET `/orders/statistics`

## Cấu hình môi trường
Chỉnh các thông số trong [src/main/resources/application.yaml](src/main/resources/application.yaml):
- Kết nối MySQL (url, username, password)
- JWT secret + expiration
- Port (nếu cần)

Ví dụ cấu hình đang có sẵn:
- MySQL: `username: root`, `password: root123`
- JWT: `secret: fruitshop-jwt-secret-key-2026-thandinh-spring-boot-application-security`
- JWT expiration: `86400000` (24h)

Lưu ý: đổi các giá trị này khi chạy trên máy khác.

## Cài đặt & chạy
1. Cấu hình database trong [src/main/resources/application.yaml](src/main/resources/application.yaml)
2. Import dữ liệu mẫu (tùy chọn):
  - [src/main/resources/db/insert_orders_data.sql](src/main/resources/db/insert_orders_data.sql)
  - [database/fruitshopupdate.sql](database/fruitshopupdate.sql) (đầy đủ hơn)
3. Build & chạy:
  ```bash
  mvn clean spring-boot:run
  ```
4. Truy cập:
  - Web: http://localhost:8080
  - API: http://localhost:8080/api

## Gợi ý kiểm thử nhanh
- Tạo user bằng trang đăng ký web
- Đăng nhập web và thử giỏ hàng, đặt hàng
- Dùng Postman gọi `/api/auth/login` để lấy JWT rồi test API

## Ví dụ gọi API (JWT)
1. Đăng nhập:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@gmail.com","password":"123456"}'
```
2. Gọi API cần JWT:
```bash
curl http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>"
```

## Dữ liệu mẫu và tài khoản
- File [database/fruitshopupdate.sql](database/fruitshopupdate.sql) có dữ liệu mẫu (user/admin), mật khẩu đã hash nên không đọc được trực tiếp.
- Nếu cần tài khoản demo, tạo mới bằng trang đăng ký web hoặc thêm user bằng Admin UI.

## Kiểm thử
```bash
mvn test
```

## Tác giả
Đinh Giáp Thân

## Giấy phép
Dự án phục vụ mục đích học tập, demo.
