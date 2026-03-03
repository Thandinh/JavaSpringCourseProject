# Tóm tắt hệ thống quản lý người dùng Admin

## Các file đã tạo/cập nhật:

### 1. Backend - Java

#### DTO (Data Transfer Object)
- **UserDTO.java** - DTO cho User với các trường: id, fullName, email, phone, address, avatar, enabled, roles, createdAt, updatedAt

#### Service Layer  
- **UserService.java** - Đã cập nhật với các chức năng:
  - Tìm kiếm user (findAll, findById, findByEmail)
  - Phân trang (findAll với Pageable)
  - Tạo user mới (createUser)
  - Cập nhật user (updateUser)
  - Xóa user (deleteById)
  - Đếm số lượng user (count)

#### Controller Layer
- **AdminUserController.java** - Controller cho web với các endpoint:
  - `GET /admin/users` - Danh sách user (có phân trang)
  - `GET /admin/users/create` - Form tạo user
  - `POST /admin/users/create` - Xử lý tạo user
  - `GET /admin/users/edit/{id}` - Form sửa user
  - `POST /admin/users/edit/{id}` - Xử lý cập nhật user
  - `POST /admin/users/delete/{id}` - Xóa user
  - `GET /admin/users/profile` - Trang hồ sơ cá nhân
  - `POST /admin/users/profile/update` - Cập nhật hồ sơ cá nhân

#### API Controller
- **AdminUserApiController.java** - REST API với các endpoint:
  - `GET /api/admin/users` - Lấy danh sách user (JSON)
  - `GET /api/admin/users/{id}` - Lấy user theo ID
  - `POST /api/admin/users` - Tạo user mới
  - `PUT /api/admin/users/{id}` - Cập nhật user
  - `DELETE /api/admin/users/{id}` - Xóa user
  - `GET /api/admin/users/profile/current` - Lấy thông tin profile hiện tại
  - `PUT /api/admin/users/profile/current` - Cập nhật profile hiện tại
  - `GET /api/admin/users/statistics/count` - Thống kê số lượng user

### 2. Frontend - Thymeleaf Templates

#### Templates đã cập nhật (chuyển từ JSP sang Thymeleaf):

1. **index.html** - Trang danh sách user
   - Hiển thị danh sách user với phân trang
   - Tìm kiếm và sắp xếp
   - Nút sửa/xóa với xác nhận (SweetAlert2)
   - Hiển thị avatar, thông tin, vai trò, trạng thái

2. **create.html** - Trang tạo user mới
   - Form nhập đầy đủ thông tin
   - Upload avatar
   - Chọn vai trò (User/Admin)
   - Chọn trạng thái (Hoạt động/Không hoạt động)

3. **edit.html** - Trang chỉnh sửa user
   - Hiển thị thông tin user hiện tại
   - Email không thể thay đổi
   - Mật khẩu tùy chọn (chỉ nhập khi muốn đổi)
   - Upload avatar mới

4. **profile.html** - Trang hồ sơ cá nhân (dành cho admin đã đăng nhập)
   - Hiển thị thông tin chi tiết
   - Form cập nhật thông tin cá nhân
   - Đổi mật khẩu (với xác thực mật khẩu cũ)
   - Upload avatar mới

### 3. Navigation

- **sidebar.html** - Đã cập nhật link:
  - Từ: `/admin-user(action='index')` → Thành: `/admin/users`
  - Từ: `/admin-user(action='create')` → Thành: `/admin/users/create`

## Tính năng chính:

### Quản lý User
✅ Xem danh sách user (có phân trang, sắp xếp)
✅ Tạo user mới
✅ Chỉnh sửa thông tin user
✅ Xóa user (có xác nhận, không thể tự xóa)
✅ Quản lý vai trò (User/Admin)
✅ Quản lý trạng thái (Hoạt động/Khóa)
✅ Upload/cập nhật avatar

### Hồ sơ cá nhân (Profile)
✅ Xem thông tin cá nhân
✅ Cập nhật thông tin (tên, SĐT, địa chỉ)
✅ Đổi mật khẩu (với xác thực mật khẩu cũ)
✅ Cập nhật avatar

### Bảo mật
✅ Mã hóa mật khẩu (BCrypt)
✅ Xác thực mật khẩu cũ khi đổi mật khẩu mới
✅ Không cho phép xóa tài khoản của chính mình
✅ Email là duy nhất, không trùng lặp

### API REST
✅ Full CRUD operations
✅ Response chuẩn JSON với success/error
✅ Thống kê số lượng user
✅ Upload file qua multipart

## Cách sử dụng:

### Truy cập trang quản lý user:
- Danh sách: `http://localhost:8080/admin/users`
- Tạo mới: `http://localhost:8080/admin/users/create`
- Sửa: `http://localhost:8080/admin/users/edit/{id}`
- Hồ sơ cá nhân: `http://localhost:8080/admin/users/profile`

### Sử dụng API:
```bash
# Lấy danh sách user
GET http://localhost:8080/api/admin/users

# Lấy user theo ID
GET http://localhost:8080/api/admin/users/1

# Tạo user mới
POST http://localhost:8080/api/admin/users
Content-Type: multipart/form-data
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "Nguyen Van A",
  "roleName": "ROLE_USER",
  "enabled": true
}

# Cập nhật user
PUT http://localhost:8080/api/admin/users/1

# Xóa user
DELETE http://localhost:8080/api/admin/users/1

# Thống kê
GET http://localhost:8080/api/admin/users/statistics/count
```

## Lưu ý quan trọng:

1. **Upload Avatar**: File được lưu trong `src/main/resources/static/uploads/users/`
2. **Vai trò**: Sử dụng `ROLE_USER` và `ROLE_ADMIN` (phải có prefix ROLE_)
3. **Phân trang**: Mặc định 10 items/trang, có thể chọn 5, 10, 20, 50
4. **Xác thực**: Cần đăng nhập với quyền Admin để truy cập
5. **Profile**: Truy cập từ header admin (menu user dropdown)

## Các thư viện sử dụng:

- Spring Boot MVC
- Thymeleaf Template Engine
- Spring Data JPA
- Spring Security (for password encoding)
- SweetAlert2 (for confirmation dialogs)
- Bootstrap 4
- Font Awesome 6

## Database Schema:

Sử dụng các bảng:
- `users` - Thông tin user
- `roles` - Vai trò
- `user_roles` - Bảng liên kết nhiều-nhiều

Tất cả đã hoàn tất và sẵn sàng sử dụng! 🎉
