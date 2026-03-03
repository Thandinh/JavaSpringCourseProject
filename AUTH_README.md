# Chức Năng Đăng Nhập & Đăng Ký - Fruitshop

## 📋 Tổng Quan

Hệ thống đăng nhập và đăng ký đã được hoàn thiện với các tính năng:

✅ Đăng ký tài khoản mới với kiểm tra Gmail
✅ Đăng nhập với session management
✅ Mã hóa mật khẩu bằng BCrypt
✅ Phân quyền USER/ADMIN tự động
✅ Validation đầy đủ (frontend & backend)
✅ API RESTful hỗ trợ

---

## 🛠️ Các File Đã Tạo/Cập Nhật

### Backend (Java)

**Repository:**
- `RoleRepository.java` - Quản lý role trong database

**DTO:**
- `RegisterDTO.java` - Data Transfer Object cho đăng ký
- `LoginDTO.java` - Data Transfer Object cho đăng nhập

**Service:**
- `AuthService.java` - Logic xử lý đăng ký/đăng nhập
- `UserService.java` - CRUD operations cho User

**Controller:**
- `AuthController.java` - Controller xử lý view (HTML)
- `AuthApiController.java` - REST API endpoints

### Frontend (HTML/JS)

- `login.html` - Trang đăng nhập (đã cập nhật)
- `register.html` - Trang đăng ký (đã cập nhật)

### Database

- `insert_roles.sql` - Script khởi tạo roles (USER, ADMIN)

---

## 🚀 Hướng Dẫn Sử Dụng

### Bước 1: Chuẩn Bị Database

Chạy script SQL để tạo roles:

```sql
-- Chạy file: insert_roles.sql
mysql -u root -P 3307 spring_fruitshop < insert_roles.sql
```

Hoặc dùng MySQL Workbench/phpMyAdmin để import file.

### Bước 2: Khởi Động Ứng Dụng

```bash
mvn spring-boot:run
```

### Bước 3: Truy Cập

- **Đăng ký:** http://localhost:8080/dang-ky
- **Đăng nhập:** http://localhost:8080/dang-nhap
- **Trang chủ:** http://localhost:8080/trang-chu

---

## 📝 Quy Tắc Validation

### Đăng Ký:
- **Họ tên:** Bắt buộc, tối thiểu 3 ký tự
- **Email:** Bắt buộc, phải là địa chỉ Gmail (@gmail.com)
- **Số điện thoại:** Tùy chọn, 10-11 chữ số
- **Địa chỉ:** Tùy chọn
- **Mật khẩu:** Bắt buộc, tối thiểu 6 ký tự
- **Nhập lại mật khẩu:** Phải khớp với mật khẩu

### Đăng Nhập:
- **Email:** Bắt buộc, định dạng email hợp lệ
- **Mật khẩu:** Bắt buộc

---

## 🔐 Bảo Mật

- Mật khẩu được mã hóa bằng **BCrypt** với strength 10
- Kiểm tra email Gmail bắt buộc khi đăng ký
- Kiểm tra trùng email trong database
- Session-based authentication
- Validation cả frontend và backend

---

## 🎯 Luồng Hoạt Động

### Đăng Ký:
1. User nhập thông tin vào form
2. Frontend validate (JavaScript)
3. Gửi POST request đến `/dang-ky`
4. Backend validate lại (Java)
5. Kiểm tra email phải là Gmail
6. Kiểm tra email đã tồn tại chưa
7. Mã hóa mật khẩu bằng BCrypt
8. Tạo user mới với role USER
9. Lưu vào database
10. Redirect về trang đăng nhập

### Đăng Nhập:
1. User nhập email & password
2. Frontend validate
3. Gửi POST request đến `/dang-nhap`
4. Backend tìm user theo email
5. Kiểm tra mật khẩu bằng BCrypt
6. Kiểm tra tài khoản có bị khóa không
7. Tạo session lưu thông tin user
8. Redirect theo role:
   - ADMIN → `/admin/dashboard`
   - USER → `/trang-chu`

---

## 🔗 API Endpoints

### POST /api/auth/register
Đăng ký tài khoản mới.

**Request Body:**
```json
{
  "email": "user@gmail.com",
  "password": "password123",
  "passwordConfirmation": "password123",
  "fullName": "Nguyễn Văn A",
  "phone": "0901234567",
  "address": "123 Lý Lợi, Quận 1, TP.HCM"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng ký thành công!",
  "user": {
    "id": 1,
    "email": "user@gmail.com",
    "fullName": "Nguyễn Văn A"
  }
}
```

### POST /api/auth/login
Đăng nhập.

**Request Body:**
```json
{
  "email": "user@gmail.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đăng nhập thành công!",
  "user": {
    "id": 1,
    "email": "user@gmail.com",
    "fullName": "Nguyễn Văn A",
    "phone": "0901234567",
    "address": "123 Lý Lợi, Quận 1, TP.HCM"
  }
}
```

### GET /api/auth/check-email?email=xxx@gmail.com
Kiểm tra email tồn tại và có phải Gmail không.

**Response:**
```json
{
  "exists": false,
  "isGmail": true,
  "message": ""
}
```

---

## ⚙️ Cấu Hình

### application.yaml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3307/spring_fruitshop?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh
    username: root
    password: 
```

- Đã cấu hình UTF-8 để hỗ trợ tiếng Việt
- BCrypt không cần cấu hình thêm

---

## 🧪 Test Thử

### Test Đăng Ký:
1. Vào: http://localhost:8080/dang-ky
2. Nhập thông tin hợp lệ (email phải @gmail.com)
3. Click "Đăng ký"
4. Kiểm tra database xem user đã được tạo chưa

### Test Đăng Nhập:
1. Vào: http://localhost:8080/dang-nhap
2. Nhập email & password đã đăng ký
3. Click "Đăng nhập"
4. Kiểm tra redirect về trang chủ

---

## 🐛 Xử Lý Lỗi

Các lỗi phổ biến và cách xử lý:

1. **"Email này đã được đăng ký"**
   - Dùng email khác hoặc đăng nhập

2. **"Vui lòng sử dụng địa chỉ Gmail"**
   - Chỉ chấp nhận @gmail.com

3. **"Email hoặc mật khẩu không đúng"**
   - Kiểm tra lại thông tin đăng nhập

4. **"Tài khoản của bạn đã bị khóa"**
   - Liên hệ quản trị viên

---

## 📊 Database Schema

### Bảng `users`
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  phone VARCHAR(255),
  address VARCHAR(255),
  enabled BIT(1) NOT NULL DEFAULT 1,
  created_at DATETIME,
  updated_at DATETIME
);
```

### Bảng `roles`
```sql
CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL,
  created_at DATETIME,
  updated_at DATETIME
);
```

### Bảng `user_roles`
```sql
CREATE TABLE user_roles (
  user_id BIGINT,
  role_id BIGINT,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

---

## ✅ Checklist Hoàn Thiện

- [x] Tạo RoleRepository
- [x] Tạo RegisterDTO & LoginDTO
- [x] Tạo AuthService với kiểm tra Gmail
- [x] Tạo UserService
- [x] Tạo AuthController
- [x] Tạo AuthApiController
- [x] Cập nhật login.html
- [x] Cập nhật register.html
- [x] Validation frontend (JavaScript)
- [x] Validation backend (Java)
- [x] Mã hóa mật khẩu BCrypt
- [x] Gán role USER tự động
- [x] Session management
- [x] Script SQL tạo roles
- [x] Hỗ trợ UTF-8 tiếng Việt

---

## 📞 Hỗ Trợ

Nếu có vấn đề, kiểm tra:
1. Database đã chạy chưa (port 3307)
2. Đã chạy script `insert_roles.sql` chưa
3. Đã cấu hình UTF-8 trong `application.yaml` chưa
4. Kiểm tra log trong console

---

**Phát triển bởi:** Fruitshop Team
**Ngày cập nhật:** 02/03/2026
