# Fruitshop

A full-featured e-commerce web application for selling fruits, built with Spring Boot, Thymeleaf, and MySQL. Includes both web and REST API for mobile clients.

## Features

## Project Structure
  - `api/` — REST API controllers (auth, product, order, review, category)
  - `controller/` — Web controllers for UI
  - `service/` — Business logic
  - `repository/` — Spring Data JPA repositories
  - `entity/` — JPA entities (User, Product, Order, etc.)
  - `security/` — Security config, JWT utils
  - `dto/` — Data transfer objects
  - `application.yaml` — Main configuration
  - `templates/` — Thymeleaf HTML templates (user & admin)
  - `static/` — CSS, JS, images
  - `db/insert_orders_data.sql` — Sample order data

## Requirements

## Setup & Run
1. **Clone the repository**
2. **Configure database**: Update `src/main/resources/application.yaml` with your MySQL credentials.
3. **Import sample data** (optional): Run the SQL in `src/main/resources/db/insert_orders_data.sql`.
4. **Build & run**:
   ```bash
   mvn clean spring-boot:run
   ```
5. **Access the app**:
   - Web: http://localhost:8080
   - API: http://localhost:8080/api/

## Testing
  ```bash
  mvn test
  ```

## Author

## License
This project is for educational/demo purposes.

## Tính năng
- Đăng ký, đăng nhập, xác thực JWT (API)
- Danh mục sản phẩm, tìm kiếm, lọc theo loại
- Giỏ hàng và quản lý đơn hàng
- Đánh giá, nhận xét sản phẩm
- Trang quản trị: quản lý sản phẩm, danh mục, đơn hàng, người dùng
- Giao diện responsive với Thymeleaf template và tài nguyên tĩnh
- RESTful API cho tích hợp mobile
- Phân quyền truy cập theo vai trò (Spring Security)

## Cấu trúc dự án
- `src/main/java/com/thandinh/fruitshop/` — Mã nguồn Java chính
  - `api/` — Controller REST API (auth, product, order, review, category)
  - `controller/` — Controller cho giao diện web
  - `service/` — Xử lý nghiệp vụ
  - `repository/` — Spring Data JPA repository
  - `entity/` — Entity JPA (User, Product, Order, ...)
  - `security/` — Cấu hình bảo mật, JWT utils
  - `dto/` — Đối tượng truyền dữ liệu
- `src/main/resources/`
  - `application.yaml` — File cấu hình chính
  - `templates/` — Giao diện HTML Thymeleaf (user & admin)
  - `static/` — CSS, JS, hình ảnh
  - `db/insert_orders_data.sql` — Dữ liệu mẫu đơn hàng
- `pom.xml` — File build Maven

## Yêu cầu
- Java 21
- Maven
- MySQL 8 trở lên

## Cài đặt & chạy
1. **Clone source về máy**
2. **Cấu hình database**: Sửa `src/main/resources/application.yaml` với thông tin MySQL của bạn
3. **Import dữ liệu mẫu** (tùy chọn): Chạy file SQL `src/main/resources/db/insert_orders_data.sql`
4. **Build & chạy ứng dụng**:
   ```bash
   mvn clean spring-boot:run
   ```
5. **Truy cập ứng dụng**:
   - Web: http://localhost:8080
   - API: http://localhost:8080/api/

## Kiểm thử
- Chạy toàn bộ test:
  ```bash
  mvn test
  ```

## Tác giả
Đinh Giáp Thân

## Giấy phép
Dự án phục vụ mục đích học tập, demo.
